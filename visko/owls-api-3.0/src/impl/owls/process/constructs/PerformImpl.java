/*
 * Created on Aug 26, 2004
 *
 * The MIT License
 *
 * (c) 2008 - Thorsten Möller
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 */
package impl.owls.process.constructs;

import org.mindswap.exceptions.ExecutionException;
import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owls.process.CompositeProcess;
import org.mindswap.owls.process.ControlConstruct;
import org.mindswap.owls.process.ControlConstructVisitor;
import org.mindswap.owls.process.Perform;
import org.mindswap.owls.process.Process;
import org.mindswap.owls.process.execution.ExecutionContext;
import org.mindswap.owls.process.execution.ExecutionSupport;
import org.mindswap.owls.process.variable.Binding;
import org.mindswap.owls.process.variable.Input;
import org.mindswap.owls.process.variable.InputBinding;
import org.mindswap.owls.process.variable.Loc;
import org.mindswap.owls.process.variable.Parameter;
import org.mindswap.owls.process.variable.ParameterValue;
import org.mindswap.owls.vocabulary.OWLS;

/**
 * @author unascribed
 * @version $Rev: 2350 $; $Author: nick $; $Date: 2011/01/24 06:37:51 $
 */
public class PerformImpl extends DataFlowControlConstruct<PerformImpl> implements Perform
{
	public PerformImpl(final OWLIndividual ind)
	{
		super(ind);
		thiz = this;
	}

	/* @see org.mindswap.common.Visitable#accept(org.mindswap.common.Visitor) */
	public void accept(final ControlConstructVisitor visitor)
	{
		visitor.visit(this);
	}

	/* @see org.mindswap.owls.process.Perform#addBinding(org.mindswap.owls.process.Input, org.mindswap.owls.process.ParameterValue) */
	public void addBinding(final Input input, final ParameterValue paramValue)
	{
		final InputBinding binding = getOntology().createInputBinding(null);
		binding.setProcessVar(input);
		binding.setValue(paramValue);

		addBinding(binding);
	}

	/* @see org.mindswap.owls.process.Perform#addBinding(org.mindswap.owls.process.Input, org.mindswap.owls.process.Perform, org.mindswap.owls.process.Parameter) */
	public void addBinding(final Input input, final Perform perform, final Parameter param)
	{
		addBinding(input, createValueOf(perform, param));
	}

	/* @see org.mindswap.owls.process.Perform#addBinding(org.mindswap.owls.process.variable.Input, org.mindswap.owls.process.variable.Loc) */
	public void addBinding(final Input input, final Loc local)
	{
		addBinding(input, createValueOf(local));
	}

	/* @see org.mindswap.owls.process.Perform#addBinding(org.mindswap.owls.process.InputBinding) */
	public void addBinding(final InputBinding binding)
	{
		addProperty(OWLS.Process.hasDataFrom, binding);
	}

	/* @see impl.owls.process.constructs.ControlConstructImpl#getAllBindings() */
	@Override
	public OWLIndividualList<Binding<?>> getAllBindings()
	{
		final OWLIndividualList<Binding<?>> bindings = OWLFactory.createIndividualList();

		// get Bindings for flows into this perform
		for (final Binding<?> binding : getBindings())
		{
			bindings.add(binding);
		}

		// get Bindings of sub process provided that it is composite
		Process process = getProcess();
		if (process instanceof CompositeProcess)
			bindings.addAll(((CompositeProcess) getProcess()).getAllBindings());

		return bindings;
	}

	/* @see org.mindswap.owls.process.Perform#getBindings() */
	public OWLIndividualList<InputBinding> getBindings()
	{
		return OWLFactory.wrapList(getProperties(OWLS.Process.hasDataFrom), InputBinding.class);
	}

	/* @see org.mindswap.owls.process.Perform#getBindingFor(org.mindswap.owls.process.Input) */
	public InputBinding getBindingFor(final Input input)
	{
		return getBindingFor(getBindings(), input);
	}

	/* @see org.mindswap.owls.process.Perform#getProcess() */
	public Process getProcess()
	{
		final Process process = getPropertyAs(OWLS.Process.process, Process.class);
//		if (process != null) process.setPerform(this);
		return process;
	}

	/* @see org.mindswap.owls.process.Perform#removeProcess(org.mindswap.owls.process.Process) */
	public void removeProcess(final Process process)
	{
//		if (process != null) process.removePerform(this);
		if (hasProperty(OWLS.Process.process)) removeProperty(OWLS.Process.process, process);
	}

	/* @see org.mindswap.owls.process.Perform#setProcess(org.mindswap.owls.process.Process) */
	public void setProcess(final Process process)
	{
//		if (process != null) process.setPerform(this);
		setProperty(OWLS.Process.process, process);
	}

	/* @see org.mindswap.owls.process.ControlConstruct#getConstructs() */
	public OWLIndividualList<ControlConstruct> getConstructs()
	{
		return OWLFactory.emptyIndividualList();
	}

	/* @see org.mindswap.owls.process.ControlConstruct#getAllProcesses(boolean) */
	public OWLIndividualList<Process> getAllProcesses(final boolean recursive)
	{
		return listProcesses(getProcess(), recursive);
	}

	/* @see org.mindswap.owls.process.ControlConstruct#getConstructName() */
	public String getConstructName()
	{
		return Perform.class.getSimpleName();
	}

	/* @see org.mindswap.owls.process.ControlConstruct#execute(org.mindswap.owls.process.execution.ExecutionContext, org.mindswap.owls.process.execution.ExecutionSupport) */
	public <C extends ExecutionContext> void execute(final C context, final ExecutionSupport<C> target)
		throws ExecutionException
	{
		target.executePerform(thiz, context);
	}

	/* @see org.mindswap.owls.process.Perform#removeBinding(org.mindswap.owls.process.InputBinding) */
	public void removeBinding(final InputBinding binding)
	{
		removeProperty(OWLS.Process.hasDataFrom, binding);
	}

	@Override protected void doPrepare(final ExecutionContext context)
	{
		if (context.isCachingPermitted() && !(thiz instanceof CachedPerformImpl))
		{
			thiz = new CachedPerformImpl(individual).prepare(context);
		}
	}

	static final class CachedPerformImpl extends PerformImpl
	{
		private Process process;
		private OWLIndividualList<InputBinding> inputBindings;

		public CachedPerformImpl(final OWLIndividual ind)
		{
			super(ind);
		}

		@Override public void addBinding(final InputBinding binding)
		{
			inputBindings = null;
			super.addBinding(binding);
		}

		@Override public OWLIndividualList<InputBinding> getBindings()
		{
			if (inputBindings == null) inputBindings = super.getBindings();
			return inputBindings;
		}

		@Override public Process getProcess()
		{
			if (process == null) process = super.getProcess();
			return process;
		}

		@Override public void removeBinding(final InputBinding binding)
		{
			inputBindings = null;
			super.removeBinding(binding);
		}

		@Override public void removeProcess(final Process p)
		{
			this.process = null;
			super.removeProcess(p);
		}

		@Override public void setProcess(final Process p)
		{
			this.process = null;
			super.setProcess(p);
		}

		@Override public PerformImpl prepare(final ExecutionContext context)
		{
			getBindings();
			process = getProcess().prepare(context);
			return this;
		}

		@Override protected final void doPrepare(final ExecutionContext context) { /* nothing to do */ }
	}
}
