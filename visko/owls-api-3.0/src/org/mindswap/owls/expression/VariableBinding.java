/*
 * Created 02.07.2009
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
package org.mindswap.owls.expression;

import org.mindswap.exceptions.CastingException;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owls.expression.Expression.QuotedExpression;
import org.mindswap.owls.process.variable.ProcessVar;
import org.mindswap.owls.vocabulary.OWLS;

/**
 * Used to define a correspondence between a variable mentioned in a quoted
 * expression ({@link OWLS.Expression#theVariable}) and a OWL individual
 * ({@link OWLS.Expression#theObject}). Expression languages for which
 * variables can't be identified by URIs need to make use of variable bindings
 * in order to establish such correspondences. For instance, SPARQL variables
 * can be identified only by means of their name.
 * <p>
 * When used to refer a OWL-S process variable, however, the value of the object
 * property must be an instance of {@link ProcessVar}, or one of its sub
 * classes.
 * <p>
 * Corresponding OWL-S concept: {@link OWLS.Expression#VariableBinding}.
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:54 $
 */
public interface VariableBinding extends OWLIndividual
{
	/**
	 * @return The unquoted expression that uses/refers to this variable binding.
	 * 	If it is referred to by multiple expressions one of them is picked arbitrarily.
	 */
	public QuotedExpression<?> getExpression();

	public String getExpressionVariable();

	public OWLIndividual getTheObject();

	/**
	 * Get the value of the object property and try to view it as a process
	 * variable. This is equivalent to <code>getTheObject().castTo(ProcessVar.class)</code>.
	 *
	 * @return The object viewed as a variable.
	 * @throws CastingException If the object individual can not be viewed as
	 * 	process variable.
	 */
	public ProcessVar getTheObjectAsVar();

	public void setExpressionVariable(String variable);

	public void setTheObject(OWLIndividual theObject);

}
