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
 * Created on Dec 28, 2003
 *
 */
package impl.owls.process;

import java.net.URI;

import org.mindswap.exceptions.ExecutionException;
import org.mindswap.owl.OWLDataValue;
import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owl.OWLOntology;
import org.mindswap.owl.OWLType;
import org.mindswap.owl.vocabulary.OWL;
import org.mindswap.owls.expression.Condition;
import org.mindswap.owls.expression.Expression;
import org.mindswap.owls.process.CompositeProcess;
import org.mindswap.owls.process.ControlConstruct;
import org.mindswap.owls.process.Process;
import org.mindswap.owls.process.Result;
import org.mindswap.owls.process.SimpleProcess;
import org.mindswap.owls.process.execution.ExecutionContext;
import org.mindswap.owls.process.execution.ExecutionSupport;
import org.mindswap.owls.process.variable.Binding;
import org.mindswap.owls.process.variable.Link;
import org.mindswap.owls.process.variable.Loc;
import org.mindswap.owls.process.variable.Local;
import org.mindswap.owls.process.variable.ProcessVar;
import org.mindswap.owls.process.variable.ValueOf;
import org.mindswap.owls.vocabulary.OWLS;
import org.mindswap.owls.vocabulary.OWLS_Extensions;
import org.mindswap.utils.ProcessDataFlow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author unascribed
 * @version $Rev: 2350 $; $Author: nick $; $Date: 2011/01/24 06:37:54 $
 */
public class CompositeProcessImpl extends ProcessImpl<CompositeProcessImpl> implements CompositeProcess
{
	private static final Logger logger = LoggerFactory.getLogger(CompositeProcessImpl.class);

	public CompositeProcessImpl(final OWLIndividual ind)
	{
		super(ind);
		process = this;
	}

	/* @see org.mindswap.owls.process.CompositeProcess#addLocal(org.mindswap.owls.process.variable.Local) */
	public void addLocal(final Local local)
	{
		addProperty(OWLS.Process.hasLocal, local);
	}

	/* @see org.mindswap.owls.process.CompositeProcess#addLocals(org.mindswap.owl.OWLIndividualList) */
	public void addLocals(final OWLIndividualList<? extends Local> locals)
	{
		for (final Local local : locals)
		{
			addLocal(local);
		}
	}

	/* @see org.mindswap.owls.process.CompositeProcess#createLoc(java.net.URI, org.mindswap.owl.OWLType) */
	public Loc createLoc(final URI uri, final OWLType varType)
	{
		final Loc local = getOntology().createLoc(uri);
		if (varType != null) local.setParamType(varType);
		addLocal(local);
		return local;
	}

	/* @see org.mindswap.owls.process.CompositeProcess#createLink(java.net.URI, org.mindswap.owl.OWLType) */
	public Link createLink(final URI uri, final OWLType varType)
	{
		final Link link = getOntology().createLink(uri);
		if (varType != null) link.setParamType(varType);
		addLocal(link);
		return link;
	}

	/* @see org.mindswap.owls.process.CompositeProcess#getLocal() */
	public Local getLocal()
	{
		return getPropertyAs(OWLS.Process.hasLocal, Local.class);
	}

	/* @see org.mindswap.owls.process.CompositeProcess#getLocal(java.lang.String) */
	public Local getLocal(final String localName)
	{
		final OWLIndividualList<Local> locals = getLocals();
		return locals.getIndividual(localName);
	}

	/* @see org.mindswap.owls.process.CompositeProcess#getLocals() */
	public OWLIndividualList<Local> getLocals()
	{
		return getPropertiesAs(OWLS.Process.hasLocal, Local.class);
	}

	/* @see org.mindswap.owls.process.Process#removeLocal(org.mindswap.owls.process.Local) */
	public void removeLocal(final Local local)
	{
		removeProperty(OWLS.Process.hasLocal, local);
	}

	/* @see org.mindswap.owls.process.CompositeProcess#getComposedOf() */
	public ControlConstruct getComposedOf()
	{
		return getPropertyAs(OWLS.Process.composedOf, ControlConstruct.class);
	}

	/* @see org.mindswap.owls.process.CompositeProcess#setComposedOf(org.mindswap.owls.process.ControlConstruct) */
	public void setComposedOf(final ControlConstruct construct)
	{
		setProperty(OWLS.Process.composedOf, construct);
	}

	/* @see org.mindswap.owls.process.CompositeProcess#getAllBindings() */
	public OWLIndividualList<Binding<?>> getAllBindings()
	{
		// Bindings appear in enclosed performs and results (OutputBinding).
		// If this composite process has sub composite processes their bindings will be
		// retrieved via the Perform that encloses those sub processes.
		final OWLIndividualList<Binding<?>> bindings = OWLFactory.createIndividualList();

		// first, add Bindings of enclosed constructs
		final ControlConstruct cc = getComposedOf();
		bindings.addAll(cc.getAllBindings());

		// second, add Bindings of this process' results
		for (final Result result : getResults())
		{
			bindings.addAll(result.getBindings());
		}
		return bindings;
	}

	/* @see org.mindswap.owls.process.CompositeProcess#removeComposedOf() */
	public void removeComposedOf()
	{
		removeProperty(OWLS.Process.composedOf, null);
	}

	/* @see org.mindswap.owls.process.Process#execute(org.mindswap.owls.process.execution.ExecutionContext, org.mindswap.owls.process.execution.ExecutionSupport) */
	public <C extends ExecutionContext> void execute(final C context, final ExecutionSupport<C> target)
		throws ExecutionException
	{
		target.executeCompositeProcess(process, context);
	}

	/* @see org.mindswap.owls.process.CompositeProcess#getComputedEffect() */
	public Expression<?> getComputedEffect()
	{
		return getPropertyAs(OWLS.Process.computedEffect, Expression.class);
	}

	/* @see org.mindswap.owls.process.CompositeProcess#getComputedInput() */
	public Expression<?> getComputedInput()
	{
		return getPropertyAs(OWLS.Process.computedInput, Expression.class);
	}

	/* @see org.mindswap.owls.process.CompositeProcess#getComputedOutput() */
	public Expression<?> getComputedOutput()
	{
		return getPropertyAs(OWLS.Process.computedOutput, Expression.class);
	}

	/* @see org.mindswap.owls.process.CompositeProcess#getComputedPrecondition() */
	public Expression<?> getComputedPrecondition()
	{
		return getPropertyAs(OWLS.Process.computedPrecondition, Expression.class);
	}

	/* @see org.mindswap.owls.process.Process#getDataFlow(org.mindswap.owl.OWLOntology) */
	public ProcessDataFlow getDataFlow(OWLOntology dfOntology)
	{
		if (dfOntology == null) dfOntology = getKB().createOntology(
			URI.create(OWLOntology.SYNTHETIC_ONT_PREFIX + "dataflow:Ontology" + System.nanoTime()));
		ProcessVar sourceParam, consParam;
		ValueOf sourceParamValue;
		OWLType mssType, consParamType;
		OWLDataValue mssTypeDV;
		for (final Binding<?> binding : getAllBindings())
		{
			if (binding.isValueOfBinding())
			{
				sourceParamValue = (ValueOf) binding.getValue();
				sourceParam = sourceParamValue.getTheVar();
				consParam = binding.getProcessVar();
				consParamType = consParam.getParamType();
				// add forward data flow link (source->sink)
				dfOntology.addProperty(sourceParam, OWLS_Extensions.Process.connectedTo, consParam);

				// Set most specific sink parameter type for current source parameter.
				// Most specific types in the following hierarchy are D,H,G.
				//       R
				//      / \
				//     A   B   C
				//   / |   |\ /
				//  D  |   F G
				//      \ /
				//       H
				// Note, however, that a constellation where some source parameter p0 of type
				// B has a sink s1 of type F and another one s2 of type G would represent a
				// unsatisfiable data flow because any source object that is an instance of B
				// would always be incompatible with s1 and/or s2.
				//
				// source object type	incompatible with		compatible with
				// type(val(p0)) = B		s1, s2					-
				// type(val(p0)) = G		s1							s2
				// type(val(p0)) = F		s2							s1
				// type(val(p0)) = H		s2							s1

				mssTypeDV = dfOntology.getProperty(sourceParam, OWLS_Extensions.Process.mssType);
				if (mssTypeDV == null)
				{
					dfOntology.addProperty(sourceParam, OWLS_Extensions.Process.mssType, consParamType.getURI());
				}
				else
				{
					mssType = getKB().getType((URI) mssTypeDV.getValue());
					if (mssType.isSubTypeOf(consParamType) || consParamType.isSubTypeOf(OWL.Nothing))
					{
						continue;
					}
					else if (consParamType.isSubTypeOf(mssType))
					{
						dfOntology.setProperty(sourceParam, OWLS_Extensions.Process.mssType, consParamType.getURI());
					}
					else
					{
						logger.warn(
							"Process {} with unsatisfiable data flow: Source variable ({}) has consumers of incompatible types {} and {}",
							new Object[] {getName0(), sourceParam, consParamType, mssType});
					}
				}
			}
		}
		return new ProcessDataFlow(dfOntology);
	}

	/* @see org.mindswap.owls.process.CompositeProcess#isInvocable() */
	public boolean isInvocable()
	{
		// First, check if this property is set. If so, assume that it is consistent
		// with what this process actually specifies and return it.
		final OWLDataValue tmp = getProperty(OWLS.Process.invocable);
		if (tmp != null && tmp.getValue() instanceof Boolean) return ((Boolean) tmp.getValue()).booleanValue();

		// Second, if the property is not set (i) compute it, (ii) set the property according
		// to the result and (iii) return it.
		boolean invocable = true;
		final ControlConstruct composedOf = getComposedOf();
		final OWLIndividualList<Process> processes = composedOf.getAllProcesses(true);
		for (final Process p : processes)
		{
			// as soon as we find some SimpleProcess the entire process is uninvocable
			// because simple processes are not directly invocable per definition
			if (p instanceof SimpleProcess)
			{
				invocable = false;
				break;
			}
		}
		setProperty(OWLS.Process.invocable, Boolean.valueOf(invocable));
		return invocable;
	}

	/* @see org.mindswap.owls.process.CompositeProcess#setComputedEffect(org.mindswap.owls.generic.expression.Expression) */
	public void setComputedEffect(final Expression<?> expression)
	{
		setProperty(OWLS.Process.computedEffect, expression);
	}

	/* @see org.mindswap.owls.process.CompositeProcess#setComputedInput(org.mindswap.owls.generic.expression.Expression) */
	public void setComputedInput(final Expression<?> expression)
	{
		setProperty(OWLS.Process.computedInput, expression);
	}

	/* @see org.mindswap.owls.process.CompositeProcess#setComputedOutput(org.mindswap.owls.generic.expression.Expression) */
	public void setComputedOutput(final Expression<?> expression)
	{
		setProperty(OWLS.Process.computedOutput, expression);
	}

	/* @see org.mindswap.owls.process.CompositeProcess#setComputedPrecondition(org.mindswap.owls.generic.expression.Expression) */
	public void setComputedPrecondition(final Expression<?> expression)
	{
		setProperty(OWLS.Process.computedPrecondition, expression);
	}

	@Override
	protected void doPrepare(final ExecutionContext context)
	{
		if (context.isCachingPermitted() && !(process instanceof CachedCompositeProcessImpl))
		{
			process = new CachedCompositeProcessImpl(individual).prepare(context);
		}
	}

	static final class CachedCompositeProcessImpl extends CompositeProcessImpl
	{
		private ControlConstruct body;
		private OWLIndividualList<Local> locals;
		private OWLIndividualList<Condition> preconditions;
		private OWLIndividualList<Result> results;

		public CachedCompositeProcessImpl(final OWLIndividual ind) { super(ind); }

		@Override public ControlConstruct getComposedOf()
		{
			if (body == null) body = super.getComposedOf();
			return body;
		}

		@Override public void removeComposedOf()
		{
			body = null;
			super.removeComposedOf();
		}

		@Override public void setComposedOf(final ControlConstruct construct)
		{
			body = null;
			super.setComposedOf(construct);
		}

		@Override public void addLocal(final Local local)
		{
			locals = null;
			super.addLocal(local);
		}

		@Override public OWLIndividualList<Local> getLocals()
		{
			if (locals == null) locals = super.getLocals();
			return locals;
		}

		@Override public void removeLocal(final Local local)
		{
			locals = null;
			super.removeLocal(local);
		}

		@Override public void addCondition(final Condition<?> condition)
		{
			preconditions = null;
			super.addCondition(condition);
		}

		@Override public void addResult(final Result result)
		{
			results = null;
			super.addResult(result);
		}

		@Override public Result createResult(final URI uri)
		{
			results = null;
			return super.createResult(uri);
		}

		@Override public OWLIndividualList<Condition> getConditions()
		{
			if (preconditions == null) preconditions = super.getConditions();
			return preconditions;
		}

		@Override public OWLIndividualList<Result> getResults()
		{
			if (results == null) results = super.getResults();
			return results;
		}

		@Override public void removeCondition(final Condition<?> condition)
		{
			preconditions = null;
			super.removeCondition(condition);
		}

		@Override public void removeResult(final Result result)
		{
			results = null;
			super.removeResult(result);
		}

		@Override public CompositeProcessImpl prepare(final ExecutionContext context)
		{
			getLocals();
			body = getComposedOf().prepare(context);
			return this;
		}

		@Override protected void doPrepare(final ExecutionContext context) { /* nothing to do */ }
	}
}
