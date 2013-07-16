/*
 * Created on Jan 4, 2005
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
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owls.process.ControlConstruct;
import org.mindswap.owls.process.ControlConstructVisitor;
import org.mindswap.owls.process.ForEach;
import org.mindswap.owls.process.Perform;
import org.mindswap.owls.process.execution.ExecutionContext;
import org.mindswap.owls.process.execution.ExecutionSupport;
import org.mindswap.owls.process.variable.Loc;
import org.mindswap.owls.process.variable.Parameter;
import org.mindswap.owls.process.variable.ValueOf;
import org.mindswap.owls.vocabulary.OWLS;

/**
 * @author unascribed
 * @version $Rev: 2319 $; $Author: nick $; $Date: 2011/01/24 06:37:51 $
 */
public class ForEachImpl extends IterateImpl<ForEachImpl> implements ForEach
{
	public ForEachImpl(final OWLIndividual ind)
	{
		super(ind);
		thiz = this;
	}

	/* @see org.mindswap.common.Visitable#accept(org.mindswap.common.Visitor) */
	public void accept(final ControlConstructVisitor visitor)
	{
		visitor.visit(this);
	}

	/* @see org.mindswap.owls.process.ForEach#getListValue() */
	public ValueOf getListValue()
	{
		return getPropertyAs(OWLS.Process.theList, ValueOf.class);
	}

	/* @see org.mindswap.owls.process.ForEach#setListValue(org.mindswap.owls.process.ValueOf) */
	public void setListValue(final ValueOf value)
	{
		setProperty(OWLS.Process.theList, value);
	}

	/* @see org.mindswap.owls.process.ForEach#getLoopVar() */
	public Loc getLoopVar()
	{
		return getPropertyAs(OWLS.Process.theLoopVar, Loc.class);
	}

	/* @see org.mindswap.owls.process.ForEach#setLoopVar(org.mindswap.owls.process.variable.Loc) */
	public void setLoopVar(final Loc var)
	{
		setProperty(OWLS.Process.theLoopVar, var);
	}

	/* @see org.mindswap.owls.process.Iterate#getComponent() */
	public ControlConstruct getComponent()
	{
		return getPropertyAs(OWLS.Process.iterateBody, ControlConstruct.class);
	}

	/* @see org.mindswap.owls.process.Iterate#setComponent(org.mindswap.owls.process.ControlConstruct) */
	public void setComponent(final ControlConstruct component)
	{
		setProperty(OWLS.Process.iterateBody, component);
	}

	/* @see org.mindswap.owls.process.ForEach#setListValue(org.mindswap.owls.process.Perform, org.mindswap.owls.process.Parameter) */
	public void setListValue(final Perform perform, final Parameter parameter)
	{
		final ValueOf valueOf = getOntology().createValueOf(null);
		valueOf.setSource(parameter, perform);

		setListValue(valueOf);
	}

	/* @see org.mindswap.owls.process.ControlConstruct#getConstructName() */
	public String getConstructName()
	{
		return "For-Each";
	}

	/* @see org.mindswap.owls.process.Iterate#removeComponent() */
	public void removeComponent()
	{
		if (hasProperty(OWLS.Process.iterateBody)) removeProperty(OWLS.Process.iterateBody, null);
	}

	/* @see org.mindswap.owls.process.ControlConstruct#execute(org.mindswap.owls.process.execution.ExecutionContext, org.mindswap.owls.process.execution.ExecutionSupport) */
	public <C extends ExecutionContext> void execute(final C context, final ExecutionSupport<C> target)
		throws ExecutionException
	{
		target.executeForEach(thiz, context);
	}

	@Override protected void doPrepare(final ExecutionContext context)
	{
		if (context.isCachingPermitted() && !(thiz instanceof CachedForEachImpl))
		{
			thiz = new CachedForEachImpl(individual).prepare(context);
		}
	}

	static final class CachedForEachImpl extends ForEachImpl
	{
		private ControlConstruct loopBody;
		private Loc loopVar;
		private ValueOf listValue;

		CachedForEachImpl(final OWLIndividual ind)
		{
			super(ind);
		}

		@Override public ControlConstruct getComponent()
		{
			if (loopBody == null) loopBody = super.getComponent();
			return loopBody;
		}

		@Override public ValueOf getListValue()
		{
			if (listValue == null) listValue = super.getListValue();
			return listValue;
		}

		@Override public Loc getLoopVar()
		{
			if (loopVar == null) loopVar = super.getLoopVar();
			return loopVar;
		}

		@Override public void removeComponent()
		{
			loopBody = null;
			super.removeComponent();
		}

		@Override public boolean removeConstruct(final ControlConstruct cc)
		{
			loopBody = null;
			return super.removeConstruct(cc);
		}

		@Override public void setComponent(final ControlConstruct component)
		{
			loopBody = null;
			super.setComponent(component);
		}

		@Override public void setListValue(final ValueOf value)
		{
			listValue = null;
			super.setListValue(value);
		}

		@Override public void setLoopVar(final Loc var)
		{
			loopVar = null;
			super.setLoopVar(var);
		}

		@Override public ForEachImpl prepare(final ExecutionContext context)
		{
			getListValue();
			getLoopVar();
			loopBody = getComponent().prepare(context);
			return this;
		}

		@Override protected final void doPrepare(final ExecutionContext context) { /* nothing to do */ }
	}
}
