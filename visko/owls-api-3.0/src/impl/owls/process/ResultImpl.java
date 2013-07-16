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
 * Created on Aug 30, 2004
 */
package impl.owls.process;

import impl.owl.WrappedIndividual;

import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owls.expression.Condition;
import org.mindswap.owls.expression.Expression;
import org.mindswap.owls.process.Perform;
import org.mindswap.owls.process.Process;
import org.mindswap.owls.process.Result;
import org.mindswap.owls.process.variable.Loc;
import org.mindswap.owls.process.variable.Output;
import org.mindswap.owls.process.variable.OutputBinding;
import org.mindswap.owls.process.variable.Parameter;
import org.mindswap.owls.process.variable.ParameterValue;
import org.mindswap.owls.process.variable.ResultVar;
import org.mindswap.owls.process.variable.ValueOf;
import org.mindswap.owls.vocabulary.OWLS;

/**
 *
 * @author unascribed
 * @version $Rev: 2323 $; $Author: nick $; $Date: 2011/01/24 06:37:54 $
 */
public class ResultImpl extends WrappedIndividual implements Result {
	public ResultImpl(final OWLIndividual ind) {
		super(ind);
	}

	/* @see org.mindswap.owls.process.Result#getEffect() */
	public Expression<?> getEffect() {
		return getPropertyAs(OWLS.Process.hasEffect, Expression.class);
	}

	/* @see org.mindswap.owls.process.Result#getEffects() */
	public OWLIndividualList<Expression> getEffects() {
		return getPropertiesAs(OWLS.Process.hasEffect, Expression.class);
	}

	/* @see org.mindswap.owls.process.Result#addEffect(org.mindswap.owls.generic.expression.Expression) */
	public void addEffect(final Expression<?> effect) {
		addProperty(OWLS.Process.hasEffect, effect);
	}

	/* @see org.mindswap.owls.process.Result#addBinding(org.mindswap.owls.process.Output, org.mindswap.owls.process.ParameterValue) */
	public void addBinding(final Output output, final ParameterValue paramValue) {
		final OutputBinding binding = getOntology().createOutputBinding(null);
		binding.setProcessVar(output);
		binding.setValue(paramValue);

		addBinding(binding);
	}

	/* @see org.mindswap.owls.process.Result#addBinding(org.mindswap.owls.process.Output, org.mindswap.owls.process.Perform, org.mindswap.owls.process.Parameter) */
	public void addBinding(final Output output, final Perform perform, final Parameter param) {
		final ValueOf valueOf = getOntology().createValueOf(null);
		valueOf.setSource(param, perform);

		addBinding(output, valueOf);
	}

	/* @see org.mindswap.owls.process.Result#addBinding(org.mindswap.owls.process.variable.Output, org.mindswap.owls.process.variable.Loc) */
	public void addBinding(final Output output, final Loc local)
	{
		final ValueOf valueOf = getOntology().createValueOf(null);
		valueOf.setSource(local);

		addBinding(output, valueOf);
	}

	/* @see org.mindswap.owls.process.Result#addBinding(org.mindswap.owls.process.OutputBinding) */
	public void addBinding(final OutputBinding binding) {
		addProperty(OWLS.Process.withOutput, binding);
	}

	/* @see org.mindswap.owls.process.Result#getBindings() */
	public OWLIndividualList<OutputBinding> getBindings()
	{
		return getPropertiesAs(OWLS.Process.withOutput, OutputBinding.class);
	}

	/* @see org.mindswap.owls.process.Result#getParameters() */
	public OWLIndividualList<ResultVar> getResultVars()
	{
		return getPropertiesAs(OWLS.Process.hasResultVar, ResultVar.class);
	}

	/* @see org.mindswap.owls.process.MultiConditional#getConditions() */
	public OWLIndividualList<Condition> getConditions()
	{
		return getPropertiesAs(OWLS.Process.inCondition, Condition.class);
	}

	/* @see org.mindswap.owls.process.MultiConditional#addCondition(org.mindswap.owls.process.Condition) */
	public void addCondition(final Condition<?> condition) {
		addProperty(OWLS.Process.inCondition, condition);
	}

	/* @see org.mindswap.owls.process.MultiConditional#removeCondition(org.mindswap.owls.expression.Condition) */
	public void removeCondition(final Condition<?> condition)
	{
		removeProperty(OWLS.Process.inCondition, condition);
	}

	/* @see org.mindswap.owls.process.Result#addResultVar(org.mindswap.owls.process.variable.ResultVar) */
	public void addResultVar(final ResultVar resVar)
	{
		addProperty(OWLS.Process.hasResultVar, resVar);
	}

	/* @see org.mindswap.owls.process.Result#getProcess() */
	public Process getProcess()
	{
		return getIncomingPropertyAs(OWLS.Process.hasResult, Process.class);
	}

	/* @see org.mindswap.owls.process.Result#getResultVar() */
	public ResultVar getResultVar()
	{
		return getPropertyAs(OWLS.Process.hasResultVar, ResultVar.class);
	}

	/* @see org.mindswap.owls.process.Result#removeBinding(org.mindswap.owls.process.OutputBinding) */
	public void removeBinding(final OutputBinding binding)
	{
		removeProperty(OWLS.Process.withOutput, binding);
	}

	/* @see org.mindswap.owls.process.Result#removeEffect(org.mindswap.owls.generic.expression.Expression) */
	public void removeEffect(final Expression<?> expr)
	{
		removeProperty(OWLS.Process.hasEffect, expr);
	}

	/* @see org.mindswap.owls.process.Result#removeResultVar(org.mindswap.owls.process.variable.ResultVar) */
	public void removeResultVar(final ResultVar resVar)
	{
		removeProperty(OWLS.Process.hasEffect, resVar);
	}

	/* @see org.mindswap.owls.process.Result#setProcess(org.mindswap.owls.process.Process) */
	public void setProcess(final Process process)
	{
		final OWLIndividualList<Process> ps = getIncomingPropertiesAs(OWLS.Process.hasResult, Process.class);
		for (final Process p : ps) // should be at most one
		{
			p.removeResult(this);
		}
		process.addResult(this);
	}
}

