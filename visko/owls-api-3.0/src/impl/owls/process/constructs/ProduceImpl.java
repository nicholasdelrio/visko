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
 * Created on Jan 4, 2005
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
import org.mindswap.owls.process.Produce;
import org.mindswap.owls.process.execution.ExecutionContext;
import org.mindswap.owls.process.execution.ExecutionSupport;
import org.mindswap.owls.process.variable.Binding;
import org.mindswap.owls.process.variable.Link;
import org.mindswap.owls.process.variable.LinkBinding;
import org.mindswap.owls.process.variable.Loc;
import org.mindswap.owls.process.variable.Output;
import org.mindswap.owls.process.variable.OutputBinding;
import org.mindswap.owls.process.variable.Parameter;
import org.mindswap.owls.process.variable.ParameterValue;
import org.mindswap.owls.vocabulary.OWLS;

/**
 * @author unascribed
 * @version $Rev: 2319 $; $Author: nick $; $Date: 2011/01/24 06:37:51 $
 */
public class ProduceImpl extends DataFlowControlConstruct<ProduceImpl> implements Produce
{
	/**
	 * @see impl.owl.WrappedIndividual#WrappedIndividual(OWLIndividual)
	 */
	public ProduceImpl(final OWLIndividual ind)
	{
		super(ind);
		thiz = this;
	}

	/* @see org.mindswap.common.Visitable#accept(org.mindswap.common.Visitor) */
	public void accept(final ControlConstructVisitor visitor)
	{
		visitor.visit(this);
	}

	/* @see impl.owls.process.constructs.ControlConstructImpl#getAllBindings() */
	@Override
	public OWLIndividualList<Binding<?>> getAllBindings()
	{
		OWLIndividualList<Binding<?>> allBindings = OWLFactory.createIndividualList();
		OWLIndividualList<?> bindings = getProperties(OWLS.Process.producedBinding);
		for (OWLIndividual ind : bindings)
		{
			if (ind.canCastTo(OutputBinding.class)) allBindings.add(ind.castTo(OutputBinding.class));
			else if (ind.canCastTo(LinkBinding.class)) allBindings.add(ind.castTo(LinkBinding.class));
		}
		return allBindings;
	}

	public OWLIndividualList<LinkBinding> getLinkBindings()
	{
		OWLIndividualList<LinkBinding> linkBindings = OWLFactory.createIndividualList();
		OWLIndividualList<?> bindings = getProperties(OWLS.Process.producedBinding);
		for (OWLIndividual ind : bindings)
		{
			if (ind.canCastTo(LinkBinding.class)) linkBindings.add(ind.castTo(LinkBinding.class));
		}
		return linkBindings;
	}

	/* @see org.mindswap.owls.process.Produce#getBindings() */
	public OWLIndividualList<OutputBinding> getOutputBindings()
	{
		OWLIndividualList<OutputBinding> outputBindings = OWLFactory.createIndividualList();
		OWLIndividualList<?> bindings = getProperties(OWLS.Process.producedBinding);
		for (OWLIndividual ind : bindings)
		{
			if (ind.canCastTo(OutputBinding.class)) outputBindings.add(ind.castTo(OutputBinding.class));
		}
		return outputBindings;
	}

	/* @see org.mindswap.owls.process.Produce#addBinding(org.mindswap.owls.process.variable.Link, org.mindswap.owls.process.variable.ParameterValue) */
	public void addBinding(final Link link, final ParameterValue paramValue)
	{
		final LinkBinding binding = getOntology().createLinkBinding(null);
		binding.setProcessVar(link);
		binding.setValue(paramValue);

		addBinding(binding);
	}

	/* @see org.mindswap.owls.process.Produce#addBinding(org.mindswap.owls.process.variable.Link, org.mindswap.owls.process.variable.Loc) */
	public void addBinding(final Link link, final Loc local)
	{
		addBinding(link, createValueOf(local));
	}

	/* @see org.mindswap.owls.process.Produce#addBinding(org.mindswap.owls.process.variable.Link, org.mindswap.owls.process.Perform, org.mindswap.owls.process.variable.Parameter) */
	public void addBinding(final Link link, final Perform perform, final Parameter param)
	{
		addBinding(link, createValueOf(perform, param));
	}

	/* @see org.mindswap.owls.process.Produce#addBinding(org.mindswap.owls.process.variable.LinkBinding) */
	public void addBinding(final LinkBinding binding)
	{
		addProperty(OWLS.Process.producedBinding, binding);
	}

	/* @see org.mindswap.owls.process.Produce#addBinding(org.mindswap.owls.process.OutputBinding) */
	public void addBinding(final OutputBinding binding)
	{
		addProperty(OWLS.Process.producedBinding, binding);
	}

	/* @see org.mindswap.owls.process.Produce#addBinding(org.mindswap.owls.process.Output, org.mindswap.owls.process.ParameterValue) */
	public void addBinding(final Output output, final ParameterValue paramValue)
	{
		final OutputBinding binding = getOntology().createOutputBinding(null);
		binding.setProcessVar(output);
		binding.setValue(paramValue);

		addBinding(binding);
	}

	/* @see org.mindswap.owls.process.Produce#addBinding(org.mindswap.owls.process.variable.Output, org.mindswap.owls.process.Perform, org.mindswap.owls.process.variable.Local) */
	public void addBinding(final Output output, final Loc local)
	{
		addBinding(output, createValueOf(local));
	}

	/* @see org.mindswap.owls.process.Produce#addBinding(org.mindswap.owls.process.Output, org.mindswap.owls.process.Perform, org.mindswap.owls.process.Parameter) */
	public void addBinding(final Output output, final Perform perform, final Parameter param)
	{
		addBinding(output, createValueOf(perform, param));
	}

	/* @see org.mindswap.owls.process.Produce#removeBinding(org.mindswap.owls.process.variable.LinkBinding) */
	public void removeBinding(final LinkBinding binding)
	{
		removeProperty(OWLS.Process.producedBinding, binding);
	}

	/* @see org.mindswap.owls.process.Produce#removeBinding(org.mindswap.owls.process.OutputBinding) */
	public void removeBinding(final OutputBinding binding)
	{
		removeProperty(OWLS.Process.producedBinding, binding);
	}

	/* @see org.mindswap.owls.process.Produce#removeBindings() */
	public void removeBindings()
	{
		removeProperty(OWLS.Process.producedBinding, null);
	}

	/* @see org.mindswap.owls.process.ControlConstruct#getConstructs() */
	public OWLIndividualList<ControlConstruct> getConstructs()
	{
		return OWLFactory.emptyIndividualList();
	}

	/* @see org.mindswap.owls.process.ControlConstruct#getAllProcesses(boolean) */
	public OWLIndividualList<Process> getAllProcesses(final boolean recursive)
	{
		return OWLFactory.createIndividualList(); // produce does not has process --> return empty list
	}

	/* @see org.mindswap.owls.process.Produce#getBindingFor(org.mindswap.owls.process.variable.Link) */
	public LinkBinding getBindingFor(final Link link)
	{
		return getBindingFor(getLinkBindings(), link);
	}

	/* @see org.mindswap.owls.process.Produce#getBindingFor(org.mindswap.owls.process.variable.Output) */
	public OutputBinding getBindingFor(final Output output)
	{
		return getBindingFor(getOutputBindings(), output);
	}

	/* @see org.mindswap.owls.process.ControlConstruct#getConstructName() */
	public String getConstructName()
	{
		return Produce.class.getSimpleName();
	}

	/* @see org.mindswap.owls.process.ControlConstruct#execute(org.mindswap.owls.process.execution.ExecutionContext, org.mindswap.owls.process.execution.ExecutionSupport) */
	public <C extends ExecutionContext> void execute(final C context, final ExecutionSupport<C> target)
		throws ExecutionException
	{
		target.executeProduce(thiz, context);
	}

	@Override protected void doPrepare(final ExecutionContext context)
	{
		if (context.isCachingPermitted() && !(thiz instanceof CachedProduceImpl))
		{
			thiz = new CachedProduceImpl(individual).prepare(context);
		}
	}

	static final class CachedProduceImpl extends ProduceImpl
	{
		private OWLIndividualList<LinkBinding> linkBindings;
		private OWLIndividualList<OutputBinding> outputBindings;

		public CachedProduceImpl(final OWLIndividual ind)
		{
			super(ind);
		}

		@Override public void addBinding(final LinkBinding binding)
		{
			linkBindings = null;
			super.addBinding(binding);
		}

		@Override public void addBinding(final OutputBinding binding)
		{
			outputBindings = null;
			super.addBinding(binding);
		}

		@Override public OWLIndividualList<LinkBinding> getLinkBindings()
		{
			if (linkBindings == null) linkBindings = super.getLinkBindings();
			return linkBindings;
		}

		@Override public OWLIndividualList<OutputBinding> getOutputBindings()
		{
			if (outputBindings == null) outputBindings = super.getOutputBindings();
			return outputBindings;
		}

		@Override public void removeBinding(final LinkBinding binding)
		{
			linkBindings = null;
			super.removeBinding(binding);
		}

		@Override public void removeBinding(final OutputBinding binding)
		{
			outputBindings = null;
			super.removeBinding(binding);
		}

		@Override public void removeBindings()
		{
			linkBindings = null;
			outputBindings = null;
			super.removeBindings();
		}

		@Override public ProduceImpl prepare(final ExecutionContext context)
		{
			getLinkBindings();
			getOutputBindings();
			return this;
		}

		@Override protected final void doPrepare(final ExecutionContext context) { /* nothing to do */ }
	}
}
