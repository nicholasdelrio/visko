/*
 * Created on Jul 7, 2004
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
package org.mindswap.owls.process.variable;

import org.mindswap.owl.OWLIndividual;
import org.mindswap.owls.process.Perform;
import org.mindswap.owls.vocabulary.OWLS;

/**
 * Used to describe the source of a value (to be assigned to a Parameter
 * specified by a Binding). The source may refer either to a {@link Parameter}
 * another process (encapsulated by a {@link Perform}) within the same
 * composite process; or to a {@link Loc} within <em>this<em> composite
 * process, i.e., if a {@link Loc} is referenced the {@link #getPerform()
 * perform} <strong>must</strong> be {@link OWLS.Process#ThisPerform}.
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:53 $
 */
public interface ValueOf extends ParameterValue, OWLIndividual
{
	public Perform getPerform();

	public boolean isParameter();
	public boolean isLocal();

	/**
	 * Get the source process variable viewed as a {@link Parameter}. This
	 * method should only be used if it is known that the variable is a
	 * {@link Input} or {@link Output}.
	 *
	 * @return The source process variable viewed as {@link Parameter}.
	 * 	<code>null</code> if this property is missing or the process
	 * 	variable is neither an {@link Input} nor {@link Output}.
	 * @see #getTheVar()
	 */
	public Parameter getParameter();

	/**
	 * Get the source process variable viewed as a {@link Loc}. This method
	 * should only be used if it is known that the variable is a {@link Loc}.
	 *
	 * @return The source process variable viewed as {@link Loc}.
	 * 	<code>null</code> if this property is missing or the process
	 * 	variable is not a {@link Loc}.
	 * @see #getTheVar()
	 */
	public Loc getLocal();

	/**
	 * Get the source process variable. This method should be used if it is
	 * unknown whether the variable is actually a {@link Input}, {@link Output},
	 * or {@link Loc}.
	 *
	 * @return The source process variable viewed as a general {@link ProcessVar}.
	 * 	<code>null</code> if this property is missing.
	 * @see #getParameter()
	 * @see #getLocal()
	 */
	public ProcessVar getTheVar();

	/**
	 * Set the source parameter reference and from which perform it comes.
	 * Removes possibly existing previous source process variable reference,
	 * independent of whether it is a Parameter or Loc reference.
	 *
	 * @param param The source parameter.
	 * @param perform The source perform. Note that the perform must be within
	 * 	the enclosing composite process, i.e., the perform must not be beyond
	 * 	scope.
	 */
	public void setSource(Parameter param, Perform perform);

	/**
	 * Set the source local variable and implicitly set the source perform to
	 * {@link OWLS.Process#ThisPerform}, since only local variables of <em>this
	 * </em> composite process can be referred. (This is implied by the scope of
	 * local variables.)
	 *
	 * @param local The source local variable (declared in the enclosing
	 * 	composite process).
	 */
	public void setSource(Loc local);
}
