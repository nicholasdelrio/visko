/*
 * Created 27.06.2009
 *
 * (c) 2009 Thorsten Möller - University of Basel Switzerland
 *
 * The MIT License
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
import org.mindswap.owls.process.ControlConstruct;
import org.mindswap.owls.process.ControlConstructVisitor;
import org.mindswap.owls.process.Perform;
import org.mindswap.owls.process.Process;
import org.mindswap.owls.process.Set;
import org.mindswap.owls.process.execution.ExecutionContext;
import org.mindswap.owls.process.execution.ExecutionSupport;
import org.mindswap.owls.process.variable.Binding;
import org.mindswap.owls.process.variable.Loc;
import org.mindswap.owls.process.variable.LocBinding;
import org.mindswap.owls.process.variable.Parameter;
import org.mindswap.owls.process.variable.ParameterValue;
import org.mindswap.owls.vocabulary.OWLS;

/**
 *
 * @author unascribed
 * @version $Rev: 2319 $; $Author: nick $; $Date: 2011/01/24 06:37:51 $
 */
public class SetImpl extends DataFlowControlConstruct<SetImpl> implements Set
{

	public SetImpl(final OWLIndividual ind)
	{
		super(ind);
		thiz = this;
	}

	/* @see org.mindswap.common.Visitable#accept(org.mindswap.common.Visitor) */
	public void accept(final ControlConstructVisitor visitor)
	{
		visitor.visit(this);
	}

	/* @see org.mindswap.owls.process.Set#addBinding(org.mindswap.owls.process.variable.Loc, org.mindswap.owls.process.variable.ParameterValue) */
	public void addBinding(final Loc local, final ParameterValue paramValue)
	{
		final LocBinding binding = getOntology().createLocBinding(null);
		binding.setProcessVar(local);
		binding.setValue(paramValue);

		addBinding(binding);
	}

	/* @see org.mindswap.owls.process.Set#addBinding(org.mindswap.owls.process.variable.Loc, org.mindswap.owls.process.Perform, org.mindswap.owls.process.variable.Parameter) */
	public void addBinding(final Loc sinkLocal, final Perform perform, final Parameter param)
	{
		addBinding(sinkLocal, createValueOf(perform, param));
	}

	/* @see org.mindswap.owls.process.Set#addBinding(org.mindswap.owls.process.variable.Loc, org.mindswap.owls.process.variable.Loc) */
	public void addBinding(final Loc sinkLocal, final Loc sourceLocal)
	{
		addBinding(sinkLocal, createValueOf(sourceLocal));
	}

	/* @see org.mindswap.owls.process.Set#addBinding(org.mindswap.owls.process.variable.LocBinding) */
	public void addBinding(final LocBinding binding)
	{
		addProperty(OWLS.Process.setBinding, binding);
	}

	/* @see org.mindswap.owls.process.ControlConstruct#execute(org.mindswap.owls.process.execution.ExecutionContext, org.mindswap.owls.process.execution.ExecutionSupport) */
	public <C extends ExecutionContext> void execute(final C context, final ExecutionSupport<C> target)
		throws ExecutionException
	{
		target.executeSet(thiz, context);
	}

	/* @see impl.owls.process.constructs.ControlConstructImpl#getAllBindings() */
	@Override
	public OWLIndividualList<Binding<?>> getAllBindings()
	{
		OWLIndividualList<Binding<?>> bindings = OWLFactory.createIndividualList();
		for (Binding<?> binding : getBindings())
		{
			bindings.add(binding);
		}
		return bindings;
	}

	/* @see org.mindswap.owls.process.ControlConstruct#getAllProcesses(boolean) */
	public OWLIndividualList<Process> getAllProcesses(final boolean recursive)
	{
		return OWLFactory.createIndividualList(); // Set does not has process --> return empty list
	}

	/* @see org.mindswap.owls.process.Set#getBindingFor(org.mindswap.owls.process.variable.Loc) */
	public LocBinding getBindingFor(final Loc local)
	{
		return getBindingFor(getBindings(), local);
	}

	/* @see org.mindswap.owls.process.Set#getBindings() */
	public OWLIndividualList<LocBinding> getBindings()
	{
		return getPropertiesAs(OWLS.Process.setBinding, LocBinding.class);
	}

	/* @see org.mindswap.owls.process.ControlConstruct#getConstructName() */
	public String getConstructName()
	{
		return Set.class.getSimpleName();
	}

	/* @see org.mindswap.owls.process.ControlConstruct#getConstructs() */
	public OWLIndividualList<ControlConstruct> getConstructs()
	{
		return OWLFactory.emptyIndividualList();
	}

	/* @see org.mindswap.owls.process.Set#removeBinding(org.mindswap.owls.process.variable.LocBinding) */
	public void removeBinding(final LocBinding binding)
	{
		removeProperty(OWLS.Process.setBinding, binding);
	}

	@Override protected void doPrepare(final ExecutionContext context)
	{
		if (context.isCachingPermitted() && !(thiz instanceof CachedSetImpl))
		{
			thiz = new CachedSetImpl(individual).prepare(context);
		}
	}

	static final class CachedSetImpl extends SetImpl
	{
		private OWLIndividualList<LocBinding> bindings;

		public CachedSetImpl(final OWLIndividual ind)
		{
			super(ind);
		}

		@Override public void addBinding(final LocBinding binding)
		{
			bindings = null;
			super.addBinding(binding);
		}

		@Override public OWLIndividualList<LocBinding> getBindings()
		{
			if (bindings == null) bindings = super.getBindings();
			return bindings;
		}

		@Override public void removeBinding(final LocBinding binding)
		{
			bindings = null;
			super.removeBinding(binding);
		}

		@Override public SetImpl prepare(final ExecutionContext context)
		{
			getBindings();
			return this;
		}

		@Override protected final void doPrepare(final ExecutionContext context) { /* nothing to do */ }
	}
}
