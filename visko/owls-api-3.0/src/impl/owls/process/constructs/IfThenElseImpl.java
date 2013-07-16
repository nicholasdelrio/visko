/*
 * Created on Aug 30, 2004
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
import org.mindswap.owls.expression.Condition;
import org.mindswap.owls.process.ControlConstruct;
import org.mindswap.owls.process.ControlConstructVisitor;
import org.mindswap.owls.process.IfThenElse;
import org.mindswap.owls.process.Process;
import org.mindswap.owls.process.execution.ExecutionContext;
import org.mindswap.owls.process.execution.ExecutionSupport;
import org.mindswap.owls.vocabulary.OWLS;

/**
 * @author unascribed
 * @version $Rev: 2319 $; $Author: nick $; $Date: 2011/01/24 06:37:51 $
 */
public class IfThenElseImpl extends ControlConstructImpl<IfThenElseImpl> implements IfThenElse
{
	/**
	 * @see impl.owl.WrappedIndividual#WrappedIndividual(OWLIndividual)
	 */
	public IfThenElseImpl(final OWLIndividual ind)
	{
		super(ind);
		thiz = this;
	}

	/* @see org.mindswap.common.Visitable#accept(org.mindswap.common.Visitor) */
	public void accept(final ControlConstructVisitor visitor)
	{
		visitor.visit(this);
	}

	/* @see org.mindswap.owls.process.IfThenElse#getThen() */
	public ControlConstruct getThen()
	{
		return getPropertyAs(OWLS.Process.thenP, ControlConstruct.class);
	}

	/* @see org.mindswap.owls.process.IfThenElse#getElse() */
	public ControlConstruct getElse()
	{
		return getPropertyAs(OWLS.Process.elseP, ControlConstruct.class);
	}

	/* @see org.mindswap.owls.process.Conditional#getCondition() */
	public Condition<?> getCondition()
	{
		return getPropertyAs(OWLS.Process.ifCondition, Condition.class);
	}

	/* @see org.mindswap.owls.process.Conditional#setCondition(org.mindswap.owls.process.Condition) */
	public void setCondition(final Condition<?> condition)
	{
		setProperty(OWLS.Process.ifCondition, condition);
	}

	/* @see org.mindswap.owls.process.IfThenElse#setThen(org.mindswap.owls.process.ControlConstruct) */
	public void setThen(final ControlConstruct cc)
	{
		setProperty(OWLS.Process.thenP, cc);
	}

	/* @see org.mindswap.owls.process.IfThenElse#setElse(org.mindswap.owls.process.ControlConstruct) */
	public void setElse(final ControlConstruct cc)
	{
		setProperty(OWLS.Process.elseP, cc);
	}

	/* @see org.mindswap.owls.process.ControlConstruct#getConstructs() */
	public OWLIndividualList<ControlConstruct> getConstructs()
	{
		final ControlConstruct thenP = getThen();
		final ControlConstruct elseP = getElse();

		final OWLIndividualList<ControlConstruct> list = OWLFactory.createIndividualList(2);
		list.add(thenP);
		if (elseP != null) list.add(elseP);
		return list;
	}

	/* @see org.mindswap.owls.process.ControlConstruct#getAllProcesses(boolean) */
	public OWLIndividualList<Process> getAllProcesses(final boolean recursive)
	{
		final ControlConstruct thenP = getThen();
		final ControlConstruct elseP = getElse();

		final OWLIndividualList<Process> list = OWLFactory.createIndividualList();
		list.addAll(thenP.getAllProcesses(recursive));
		if (elseP != null) list.addAll(elseP.getAllProcesses(recursive));

		return list;
	}

	/* @see org.mindswap.owls.process.ControlConstruct#getConstructName() */
	public String getConstructName()
	{
		return "If-Then-Else";
	}

	/* @see impl.owls.process.constructs.ControlConstructImpl#removeConstruct(org.mindswap.owls.process.ControlConstruct) */
	@Override
	public boolean removeConstruct(final ControlConstruct CC)
	{
		if (getThen().equals(CC)) setThen(null);
		if (getElse().equals(CC)) setElse(null);
		return true;
	}

	/* @see org.mindswap.owls.process.IfThenElse#removeElse() */
	public void removeElse()
	{
		if (hasProperty(OWLS.Process.elseP)) removeProperty(OWLS.Process.elseP, null);
	}

	/* @see org.mindswap.owls.process.IfThenElse#removeThen() */
	public void removeThen()
	{
		if (hasProperty(OWLS.Process.thenP)) removeProperty(OWLS.Process.thenP, null);
	}

	/* @see org.mindswap.owls.process.ControlConstruct#execute(org.mindswap.owls.process.execution.ExecutionContext, org.mindswap.owls.process.execution.ExecutionSupport) */
	public <C extends ExecutionContext> void execute(final C context, final ExecutionSupport<C> target)
		throws ExecutionException
	{
		target.executeIfThenElse(thiz, context);
	}

	@Override protected void doPrepare(final ExecutionContext context)
	{
		if (context.isCachingPermitted() && !(thiz instanceof CachedIfThenElseImpl))
		{
			thiz = new CachedIfThenElseImpl(individual).prepare(context);
		}
	}

	static final class CachedIfThenElseImpl extends IfThenElseImpl
	{
		private Condition<?> condition;
		private ControlConstruct thenCC;
		private ControlConstruct elseCC;

		public CachedIfThenElseImpl(final OWLIndividual ind)
		{
			super(ind);
		}

		@Override public Condition<?> getCondition()
		{
			if (condition == null) condition = super.getCondition();
			return condition;
		}

		@Override public ControlConstruct getElse()
		{
			if (elseCC == null) elseCC = super.getElse();
			return elseCC;
		}

		@Override public ControlConstruct getThen()
		{
			if (thenCC == null) thenCC = super.getThen();
			return thenCC;
		}

		@Override public boolean removeConstruct(final ControlConstruct cc)
		{
			thenCC = elseCC = null;
			return super.removeConstruct(cc);
		}

		@Override public void removeElse()
		{
			elseCC = null;
			super.removeElse();
		}

		@Override public void removeThen()
		{
			thenCC = null;
			super.removeThen();
		}

		@Override public void setCondition(final Condition<?> condition)
		{
			this.condition = null;
			super.setCondition(condition);
		}

		@Override public void setElse(final ControlConstruct cc)
		{
			elseCC = null;
			super.setElse(cc);
		}

		@Override public void setThen(final ControlConstruct cc)
		{
			thenCC = null;
			super.setThen(cc);
		}

		@Override public IfThenElseImpl prepare(final ExecutionContext context)
		{
			getCondition();
			getThen();
			getElse();
			if (thenCC != null) thenCC = thenCC.prepare(context);
			if (elseCC != null) elseCC = elseCC.prepare(context);
			return this;
		}

		@Override protected final void doPrepare(final ExecutionContext context) { /* nothing to do */ }
	}
}