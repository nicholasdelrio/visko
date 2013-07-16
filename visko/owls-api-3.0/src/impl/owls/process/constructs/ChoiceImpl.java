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
package impl.owls.process.constructs;

import org.mindswap.exceptions.ExecutionException;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owls.process.Choice;
import org.mindswap.owls.process.ControlConstruct;
import org.mindswap.owls.process.ControlConstructVisitor;
import org.mindswap.owls.process.execution.ExecutionContext;
import org.mindswap.owls.process.execution.ExecutionSupport;

/**
 * @author unascribed
 * @version $Rev: 2319 $; $Author: nick $; $Date: 2011/01/24 06:37:51 $
 */
public class ChoiceImpl extends CollectionControlConstructImpl<ChoiceImpl> implements Choice
{
	public ChoiceImpl(final OWLIndividual ind)
	{
		super(ind);
		thiz = this;
	}

	/* @see org.mindswap.common.Visitable#accept(org.mindswap.common.Visitor) */
	public void accept(final ControlConstructVisitor visitor)
	{
		visitor.visit(this);
	}

	/* @see org.mindswap.owls.process.ControlConstruct#getConstructName() */
	public String getConstructName()
	{
		return Choice.class.getSimpleName();
	}

	/* @see org.mindswap.owls.process.ControlConstruct#execute(org.mindswap.owls.process.execution.ExecutionContext, org.mindswap.owls.process.execution.ExecutionSupport) */
	public <C extends ExecutionContext> void execute(final C context, final ExecutionSupport<C> target)
		throws ExecutionException
	{
		target.executeChoice(thiz, context);
	}

	@Override protected void doPrepare(final ExecutionContext context)
	{
		if (context.isCachingPermitted() && !(thiz instanceof CachedChoiceImpl))
		{
			thiz = new CachedChoiceImpl(individual).prepare(context);
		}
	}

	static final class CachedChoiceImpl extends ChoiceImpl
	{
		private OWLIndividualList<ControlConstruct> components;

		CachedChoiceImpl(final OWLIndividual ind)
		{
			super(ind);
		}

		@Override public OWLIndividualList<ControlConstruct> getConstructs()
		{
			if (components == null) components = super.getConstructs();
			return components;
		}

		@Override public void addComponent(final ControlConstruct component)
		{
			components = null;
			super.addComponent(component);
		}

		@Override public void deleteComponents()
		{
			components = null;
			super.deleteComponents();
		}

		@Override public void removeComponents()
		{
			components = null;
			super.removeComponents();
		}

		@Override public boolean removeConstruct(final ControlConstruct cc)
		{
			components = null;
			return super.removeConstruct(cc);
		}

		@Override public ChoiceImpl prepare(final ExecutionContext context)
		{
			components = prepare(getConstructs(), context);
			return this;
		}

		@Override protected final void doPrepare(final ExecutionContext context) { /* nothing to do */ }
	}
}
