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
 * Created on Jul 7, 2004
 */
package org.mindswap.owls.process;

import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owls.expression.Expression;
import org.mindswap.owls.process.variable.Loc;
import org.mindswap.owls.process.variable.Output;
import org.mindswap.owls.process.variable.OutputBinding;
import org.mindswap.owls.process.variable.Parameter;
import org.mindswap.owls.process.variable.ParameterValue;
import org.mindswap.owls.process.variable.ResultVar;

/**
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:52 $
 */
public interface Result extends OWLIndividual, MultiConditional
{
	/**
	 * @return The effects defined with process:hasEffect property.
	 */
	public OWLIndividualList<Expression> getEffects();

	/**
	 * @return
	 */
	public Expression<?> getEffect();

	/**
	 * Remove the given effect from this result by breaking the link
	 * <tt>process:hasEffect</tt>. The effect itself remains untouched.
	 *
	 * @param expr The effect expression to remove. A value of <code>null</code>
	 * 	will remove all effect expressions.
	 */
	public void removeEffect(Expression<?> expr);

	/**
	 * @param expr
	 */
	public void addEffect(Expression<?> expr);

	/**
	 * @param binding
	 */
	public void addBinding(OutputBinding binding);

	/**
	 * @param output
	 * @param paramValue
	 */
	public void addBinding(Output output, ParameterValue paramValue);

	/**
	 * @param output
	 * @param perform
	 * @param param
	 */
	public void addBinding(Output output, Perform perform, Parameter param);

	/**
	 * @param output
	 * @param local
	 */
	public void addBinding(Output output, Loc local);

	/**
	 * @return The output bindings defined with process:withOutput property.
	 */
	public OWLIndividualList<OutputBinding> getBindings();

	/**
	 * Remove the given binding from this result by breaking the link
	 * <tt>process:withOutput</tt>. The binding itself remains untouched.
	 *
	 * @param binding The binding to remove. A value of <code>null</code>
	 * 	will remove all bindings.
	 */
	public void removeBinding(OutputBinding binding);

	public void addResultVar(ResultVar resVar);

	public ResultVar getResultVar();

	/**
	 * @return The result variables defined with process:hasResultVar property.
	 */
	public OWLIndividualList<ResultVar> getResultVars();

	/**
	 * Remove the given result variable declaration from this result by breaking
	 * the link <tt>process:hasResultVar</tt>. The result variable declaration
	 * itself remains untouched.
	 *
	 * @param resVar The result variable to remove. A value of <code>null</code>
	 * 	will remove all result variables.
	 */
	public void removeResultVar(ResultVar resVar);

	/**
	 * @return The process that defines this result.
	 */
	public Process getProcess();

	/**
	 * Sets this result to the given process. If this result was already set to
	 * another process this relation will be cut.
	 *
	 * @param process The process which will be extended by this result.
	 */
	public void setProcess(Process process);
}
