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
 * Created on Dec 23, 2004
 */
package impl.owls.process;

import org.mindswap.exceptions.ExecutionException;
import org.mindswap.exceptions.InvalidOWLSException;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLOntology;
import org.mindswap.owls.process.AtomicProcess;
import org.mindswap.owls.process.CompositeProcess;
import org.mindswap.owls.process.Process;
import org.mindswap.owls.process.SimpleProcess;
import org.mindswap.owls.process.execution.ExecutionContext;
import org.mindswap.owls.process.execution.ExecutionSupport;
import org.mindswap.owls.vocabulary.OWLS;
import org.mindswap.utils.ProcessDataFlow;

/**
 * @author unascribed
 * @version $Rev: 2319 $; $Author: nick $; $Date: 2011/01/24 06:37:54 $
 */
public class SimpleProcessImpl extends ProcessImpl<SimpleProcessImpl> implements SimpleProcess
{
	public SimpleProcessImpl(final OWLIndividual ind)
	{
		super(ind);
		process = this;
	}

	// FIXME Overwrite all methods from ProcessImpl and delegate either to the atomic
	// process realized by this simple process respectively the composite process to
	// which this composite process expands.

	/* @see org.mindswap.owls.process.SimpleProcess#getAtomicProcess() */
	public AtomicProcess getAtomicProcess()
	{
		return getPropertyAs(OWLS.Process.realizedBy, AtomicProcess.class);
	}

	/* @see org.mindswap.owls.process.SimpleProcess#getCompositeProcess() */
	public CompositeProcess getCompositeProcess()
	{
		return getPropertyAs(OWLS.Process.expandsTo, CompositeProcess.class);
	}

	/* @see org.mindswap.owls.process.Process#getDataFlow(org.mindswap.owl.OWLOntology) */
	public ProcessDataFlow getDataFlow(final OWLOntology dfOntology)
	{
		if (hasProperty(OWLS.Process.realizedBy)) return getAtomicProcess().getDataFlow(dfOntology);
		if (hasProperty(OWLS.Process.expandsTo)) return getCompositeProcess().getDataFlow(dfOntology);
		throw new InvalidOWLSException(
			"Simple process does not expand to composite process nor is it realized by an atomic process.");
	}

	/* @see org.mindswap.owls.process.SimpleProcess#removeAtomicProcess() */
	public void removeAtomicProcess()
	{
		removeProperty(OWLS.Process.realizedBy, null);
	}

	/* @see org.mindswap.owls.process.SimpleProcess#removeCompositeProcess() */
	public void removeCompositeProcess()
	{
		removeProperty(OWLS.Process.expandsTo, null);
	}

	/* @see org.mindswap.owls.process.SimpleProcess#setAtomicProcess(org.mindswap.owls.process.AtomicProcess) */
	public void setAtomicProcess(final AtomicProcess process)
	{
		setProperty(OWLS.Process.realizedBy, process);
	}

	/* @see org.mindswap.owls.process.SimpleProcess#setCompositeProcess(org.mindswap.owls.process.CompositeProcess) */
	public void setCompositeProcess(final CompositeProcess process)
	{
		setProperty(OWLS.Process.expandsTo, process);
	}

	/* @see org.mindswap.owls.process.Process#execute(org.mindswap.owls.process.execution.ExecutionContext, org.mindswap.owls.process.execution.ExecutionSupport) */
	public <C extends ExecutionContext> void execute(final C context, final ExecutionSupport<C> target)
		throws ExecutionException
	{
		target.executeSimpleProcess(this, context);
	}

	@Override protected void doPrepare(final ExecutionContext context)
	{
		if (context.isCachingPermitted() && !(process instanceof CachedSimpleProcessImpl))
		{
			process = new CachedSimpleProcessImpl(individual).prepare(context);
		}
	}

	static final class CachedSimpleProcessImpl extends SimpleProcessImpl
	{
		private Process proc;

		public CachedSimpleProcessImpl(final OWLIndividual ind)
		{
			super(ind);
		}

		@Override public AtomicProcess getAtomicProcess()
		{
			if (proc == null || proc instanceof CompositeProcess) proc = super.getAtomicProcess();
			return (AtomicProcess) proc;
		}

		@Override public CompositeProcess getCompositeProcess()
		{
			if (proc == null || proc instanceof AtomicProcess) proc = super.getCompositeProcess();
			return (CompositeProcess) proc;
		}

		@Override public void removeAtomicProcess()
		{
			proc = null;
			super.removeAtomicProcess();
		}

		@Override public void removeCompositeProcess()
		{
			proc = null;
			super.removeCompositeProcess();
		}

		@Override public void setAtomicProcess(final AtomicProcess process)
		{
			proc = null;
			super.setAtomicProcess(process);
		}

		@Override public void setCompositeProcess(final CompositeProcess process)
		{
			proc = null;
			super.setCompositeProcess(process);
		}

		@Override public SimpleProcessImpl prepare(final ExecutionContext context)
		{
			final AtomicProcess ap = getAtomicProcess();
			final CompositeProcess cp = getCompositeProcess();
			if (ap != null && cp == null) proc = ap.prepare(context);
			else if (ap == null && cp != null) proc = cp.prepare(context);
			return this;
		}

		@Override protected void doPrepare(final ExecutionContext context) { /* nothing to do */ }
	}

}
