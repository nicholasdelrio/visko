// The MIT License
//
// Copyright (c) 2004 Evren Sirin
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to
// deal in the Software without restriction, including without limitation the
// rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
// sell copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
// FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
// IN THE SOFTWARE.

/*
 * Created on Dec 27, 2003
 */
package impl.owls.process;

import java.net.URI;

import org.mindswap.exceptions.ExecutionException;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owl.OWLOntology;
import org.mindswap.owl.OWLType;
import org.mindswap.owls.expression.Condition;
import org.mindswap.owls.grounding.AtomicGrounding;
import org.mindswap.owls.grounding.Grounding;
import org.mindswap.owls.process.AtomicProcess;
import org.mindswap.owls.process.execution.ExecutionContext;
import org.mindswap.owls.process.execution.ExecutionSupport;
import org.mindswap.owls.process.variable.ProcessVar;
import org.mindswap.owls.service.Service;
import org.mindswap.owls.vocabulary.OWLS;
import org.mindswap.utils.ProcessDataFlow;

/**
 * @author unascribed
 * @version $Rev: 2350 $; $Author: nick $; $Date: 2011/01/24 06:37:54 $
 */
public class AtomicProcessImpl extends ProcessImpl<AtomicProcessImpl> implements AtomicProcess
{
	/**
	 * @see impl.owl.WrappedIndividual#WrappedIndividual(OWLIndividual)
	 */
	public AtomicProcessImpl(final OWLIndividual ind)
	{
		super(ind);
		process = this;
	}

	/* @see org.mindswap.owls.process.Process#getDataFlow(org.mindswap.owl.OWLOntology) */
	public ProcessDataFlow getDataFlow(OWLOntology dfOntology)
	{
		if (dfOntology == null) dfOntology = getKB().createOntology(
			URI.create(OWLOntology.SYNTHETIC_ONT_PREFIX + "dataflow:Ontology" + System.nanoTime()));
		return new ProcessDataFlow(dfOntology) // kind of dummy because atomic processes do not have a data flow
		{
			@Override
			public OWLType getMostSpecificSinkType(final ProcessVar source)
			{
				// since there is no data flow, the most specific sink is always the type of the source itself
				return source.getParamType();
			}
		};
	}

	/* @see org.mindswap.owls.process.AtomicProcess#getGrounding() */
	public AtomicGrounding<?> getGrounding()
	{
		final Service service = getService();
		if (service == null) return null;
		final Grounding<?,?> g = service.getGrounding();
		return (g == null)? null : g.getAtomicGrounding(this);
	}

	/* @see org.mindswap.owls.process.AtomicProcess#setGrounding(org.mindswap.owls.grounding.AtomicGrounding) */
	public void setGrounding(final AtomicGrounding<?> grounding)
	{
		grounding.setProcess(this);
	}

	/* @see org.mindswap.owls.process.AtomicProcess#removeGrounding() */
	public void removeGrounding()
	{
		getGrounding().removeProperty(OWLS.Grounding.owlsProcess, this);
	}

	/* @see org.mindswap.owls.process.Process#execute(org.mindswap.owls.process.execution.ExecutionContext, org.mindswap.owls.process.execution.ExecutionSupport) */
	public <C extends ExecutionContext> void execute(final C context, final ExecutionSupport<C> target)
		throws ExecutionException
	{
		target.executeAtomicProcess(process, context);
	}

	@Override protected void doPrepare(final ExecutionContext context)
	{
		if (context.isCachingPermitted() && !(process instanceof CachedAtomicProcessImpl))
		{
			process = new CachedAtomicProcessImpl(individual);
		}
	}

	static final class CachedAtomicProcessImpl extends AtomicProcessImpl
	{
		private AtomicGrounding<?> atomicGrounding;
		private OWLIndividualList<Condition> preconditions;
//		private OWLIndividualList<Result> results;

		CachedAtomicProcessImpl(final OWLIndividual ind) {	super(ind);	}

		@Override public AtomicGrounding<?> getGrounding()
		{
			if (atomicGrounding == null) atomicGrounding = super.getGrounding();
			return atomicGrounding;
		}

		@Override public void removeGrounding()
		{
			atomicGrounding = null;
			super.removeGrounding();
		}

		@Override public void setGrounding(final AtomicGrounding<?> grounding)
		{
			atomicGrounding = null; // do not assign parameter since it may be attached to another model
			super.setGrounding(grounding);
		}

		@Override public void addCondition(final Condition<?> condition)
		{
			preconditions = null;
			super.addCondition(condition);
		}

//		@Override public void addResult(final Result result)
//		{
//			results = null;
//			super.addResult(result);
//		}
//
//		@Override public Result createResult(final URI uri)
//		{
//			results = null;
//			return super.createResult(uri);
//		}

		@Override public OWLIndividualList<Condition> getConditions()
		{
			if (preconditions == null) preconditions = super.getConditions();
			return preconditions;
		}

//		@Override public OWLIndividualList<Result> getResults()
//		{
//			if (results == null) results = super.getResults();
//			return results;
//		}

		@Override public void removeCondition(final Condition<?> condition)
		{
			preconditions = null;
			super.removeCondition(condition);
		}

//		@Override public void removeResult(final Result result)
//		{
//			results = null;
//			super.removeResult(result);
//		}

		// We could invoke getGrounding() in order to prepare atomic processes. However,
		// we decided not to do it in case this cached atomic process will be created but
		// never executed, which is a bit less work then.
		@Override protected void doPrepare(final ExecutionContext context)	{ /* nothing to prepare */ }
		@Override public AtomicProcessImpl prepare(final ExecutionContext context)	{ return this; }
	}
}
