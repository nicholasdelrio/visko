/*
 * Created on Aug 30, 2004
 *
 * The MIT License
 *
 * (c) 2008 Thorsten Möller - University of Basel Switzerland
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
import org.mindswap.owls.expression.Condition;
import org.mindswap.owls.process.ControlConstruct;
import org.mindswap.owls.process.ControlConstructVisitor;
import org.mindswap.owls.process.RepeatWhile;
import org.mindswap.owls.process.execution.ExecutionContext;
import org.mindswap.owls.process.execution.ExecutionSupport;
import org.mindswap.owls.vocabulary.OWLS;

/**
 * @author unascribed
 * @version $Rev: 2319 $; $Author: nick $; $Date: 2011/01/24 06:37:51 $
 */
public class RepeatWhileImpl extends IterateImpl<RepeatWhileImpl> implements RepeatWhile
{
	public RepeatWhileImpl(final OWLIndividual ind)
	{
		super(ind);
		thiz = this;
	}

	/* @see org.mindswap.common.Visitable#accept(org.mindswap.common.Visitor) */
	public void accept(final ControlConstructVisitor visitor)
	{
		visitor.visit(this);
	}

	/* @see org.mindswap.owls.process.Conditional#getCondition() */
	public Condition<?> getCondition()
	{
		return getPropertyAs(OWLS.Process.whileCondition, Condition.class);
	}

	/* @see org.mindswap.owls.process.Conditional#setCondition(org.mindswap.owls.process.Condition) */
	public void setCondition(final Condition<?> condition)
	{
		setProperty(OWLS.Process.whileCondition, condition);
	}

	/* @see org.mindswap.owls.process.Iterate#getComponent() */
	public ControlConstruct getComponent()
	{
		return getPropertyAs(OWLS.Process.whileProcess, ControlConstruct.class);
	}

	/* @see org.mindswap.owls.process.Iterate#setComponent(org.mindswap.owls.process.ControlConstruct) */
	public void setComponent(final ControlConstruct component)
	{
		setProperty(OWLS.Process.whileProcess, component);
	}

	/* @see org.mindswap.owls.process.ControlConstruct#getConstructName() */
	public String getConstructName()
	{
		return "Repeat-While";
	}

	/* @see org.mindswap.owls.process.Iterate#removeComponent() */
	public void removeComponent()
	{
		if (hasProperty(OWLS.Process.whileProcess)) removeProperty(OWLS.Process.whileProcess, null);
	}

	/* @see org.mindswap.owls.process.ControlConstruct#execute(org.mindswap.owls.process.execution.ExecutionContext, org.mindswap.owls.process.execution.ExecutionSupport) */
	public <C extends ExecutionContext> void execute(final C context, final ExecutionSupport<C> target)
		throws ExecutionException
	{
		target.executeRepeatWhile(thiz, context);
	}

	@Override protected void doPrepare(final ExecutionContext context)
	{
		if (context.isCachingPermitted() && !(thiz instanceof CachedRepeatWhileImpl))
		{
			thiz = new CachedRepeatWhileImpl(individual).prepare(context);
		}
	}

	static final class CachedRepeatWhileImpl extends RepeatWhileImpl
	{
		private Condition<?> condition;
		private ControlConstruct loopBody;

		public CachedRepeatWhileImpl(final OWLIndividual ind)
		{
			super(ind);
		}

		@Override public ControlConstruct getComponent()
		{
			if (loopBody == null) loopBody = super.getComponent();
			return loopBody;
		}

		@Override public Condition<?> getCondition()
		{
			if (condition == null) condition = super.getCondition();
			return condition;
		}

		@Override public void removeComponent()
		{
			loopBody = null;
			super.removeComponent();
		}

		@Override public void setComponent(final ControlConstruct component)
		{
			loopBody = null;
			super.setComponent(component);
		}

		@Override public void setCondition(final Condition<?> condition)
		{
			this.condition = null;
			super.setCondition(condition);
		}

		@Override public boolean removeConstruct(final ControlConstruct cc)
		{
			loopBody = null;
			return super.removeConstruct(cc);
		}

		@Override public RepeatWhileImpl prepare(final ExecutionContext context)
		{
			getCondition();
			loopBody = getComponent().prepare(context);
			return this;
		}

		@Override protected final void doPrepare(final ExecutionContext context) { /* nothing to do */ }
	}
}
