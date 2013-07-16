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
package impl.owls.process.constructs;

import impl.owl.WrappedIndividual;

import org.mindswap.exceptions.NotImplementedException;
import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owls.process.CompositeProcess;
import org.mindswap.owls.process.ControlConstruct;
import org.mindswap.owls.process.Process;
import org.mindswap.owls.process.execution.ExecutionContext;
import org.mindswap.owls.process.variable.Binding;
import org.mindswap.owls.vocabulary.OWLS;

/**
 *
 * @author unascribed
 * @version $Rev: 2319 $; $Author: nick $; $Date: 2011/01/24 06:37:51 $
 */
public abstract class ControlConstructImpl<C extends ControlConstructImpl<C>> extends WrappedIndividual
	implements ControlConstruct
{
	/** Used for caching purposes for execution. References the prepared version of this control construct. */
	protected C thiz;

	public ControlConstructImpl(final OWLIndividual ind)
	{
		super(ind);
	}

	/* @see org.mindswap.owls.process.ControlConstruct#getEnclosingProcess() */
	public CompositeProcess getEnclosingProcess()
	{
		return getIncomingPropertyAs(OWLS.Process.composedOf, CompositeProcess.class);
	}

	/* @see org.mindswap.owls.process.ControlConstruct#getAllBindings() */
	public OWLIndividualList<Binding<?>> getAllBindings()
	{
		final OWLIndividualList<Binding<?>> bindings = OWLFactory.createIndividualList();
		for (ControlConstruct cc : getConstructs())
		{
			// Bindings are specified only in Perform, Produce, and Set control constructs <-- they overwrite getAllBindings()
			bindings.addWithoutDuplicate(cc.getAllBindings());
		}
		return bindings;
	}

	/* @see org.mindswap.owls.process.ControlConstruct#getTimeout() */
	public OWLIndividual getTimeout()
	{
		return getProperty(OWLS.Process.timeout);
	}

	/* @see org.mindswap.owls.process.ControlConstruct#setTimeout(org.mindswap.owl.OWLIndividual) */
	public void setTimeout(final OWLIndividual timeout)
	{
		setProperty(OWLS.Process.timeout, timeout);
	}

	/* @see org.mindswap.owls.process.ControlConstruct#removeConstruct(org.mindswap.owls.process.ControlConstruct) */
	public boolean removeConstruct(final ControlConstruct cc)
	{
		// TODO implement, in particular re-routing of data flow <-- not trivial in general
		throw new NotImplementedException();
	}

	/* @see org.mindswap.owls.process.ControlConstruct#prepare(org.mindswap.owls.process.execution.ExecutionContext) */
	public C prepare(final ExecutionContext context)
	{
		thiz.doPrepare(context); // sub classes may re-assign thiz field
		return thiz;
	}

	protected abstract void doPrepare(ExecutionContext context);

	protected OWLIndividualList<Process> listProcesses(final Process process, final boolean recursive)
	{
		OWLIndividualList<Process> processes = OWLFactory.createIndividualList();
		processes.add(process);
		if (recursive && process instanceof CompositeProcess)
		{
			final ControlConstruct cc = ((CompositeProcess) process).getComposedOf();
			processes.addAll(cc.getAllProcesses(recursive));
		}
		return processes;
	}

}
