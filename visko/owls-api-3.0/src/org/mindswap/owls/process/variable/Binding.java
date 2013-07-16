/*
 * Created on Jul 7, 2004
 *
 * (c) 2008 Thorsten Möller - University of Basel Switzerland
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
package org.mindswap.owls.process.variable;

import org.mindswap.owl.OWLIndividual;

/**
 * Corresponding OWL-S concept: {@link org.mindswap.owls.vocabulary.OWLS.Process#Binding}.
 * <p>
 * The OWL-S property <tt>process:valueType</tt> is currently not accessible
 * by this interface because we think that it is redundant to the type of the
 * target process variable that can be accessed via
 * <code>someBinding.getProcessVar().getParamType()</code> anyway.
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:53 $
 */
public interface Binding<V extends ProcessVar> extends OWLIndividual {

	/* This interface makes use of Java generic type parameter to parameterize
	 * the ProcessVar. We know that this may yield unchecked conversion compiler
	 * warnings and make the general OWLObject casting machinery more difficult. */

	/**
	 * @return The process variable that is <em>bound</em> by the value
	 * 	specification of this binding.
	 */
	public V getProcessVar();

	/**
	 * Set the the process variable that is <em>bound</em> by the value
	 * specification of this binding.
	 *
	 * @param procVar The target process variable to set.
	 * @return The previous variable that was set (if any).
	 * @throws NullPointerException If <code>procVar</code> is <code>null</code>.
	 */
	public V setProcessVar(V procVar);

	/**
	 * @return Object that specifies <em>how<em> the target process variable
	 * 	is bound to the actual value.
	 */
	public ParameterValue getValue();

	/**
	 * @return <code>true</code> if the {@link #getProcessVar() target variable}
	 * 	is bound to a {@link ValueConstant constant value}.
	 */
	public boolean isValueConstantBinding();

	/**
	 * @return <code>true</code> if the {@link #getProcessVar() target variable}
	 * 	is bound to a {@link ValueOf} description, i.e, the value is taken
	 * 	from another variable of the enclosing composite process, or a Perform
	 * 	in the scope of the enclosing process.
	 */
	public boolean isValueOfBinding();

	/**
	 * @return <code>true</code> if the {@link #getProcessVar() target variable}
	 * 	is bound to a {@link ValueForm value form} description.
	 */
	public boolean isValueFormBinding();

	/**
	 * @return <code>true</code> if the {@link #getProcessVar() target variable}
	 * 	is bound to a {@link ValueFunction value function} description.
	 */
	public boolean isValueFunctionBinding();

	/**
	 * @param value The value to bind with the {@link #getProcessVar() target variable}.
	 * @throws NullPointerException if <code>value</code> is <code>null</code>.
	 */
	public void setValue(ParameterValue value);

}
