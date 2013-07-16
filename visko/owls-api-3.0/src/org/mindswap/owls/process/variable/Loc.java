/*
 * Created 03.06.2009
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

import org.mindswap.owl.OWLDataValue;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLValue;


/**
 *
 * @author unascribed
 * @version $Rev: 2289 $; $Author: nick $; $Date: 2011/01/24 06:37:53 $
 */
public interface Loc extends Local
{
	/**
	 * If this local variable has an initial value associated with it (specified
	 * with <tt>process:initialValue</tt> property) return that value. There is no
	 * restriction as to the contents of the value. As such, it may be an untyped
	 * or typed literal, or a XMLLiteral that actually represents a OWL individual.
	 * Internally, the method first checks whether the {@link #getParamType() type}
	 * of this variable is a OWL data type or a OWL class. Depending on the result
	 * the initial value (if any) will then be returned accordingly, possibly
	 * involving parsing the value into a OWL individual if the variable type
	 * is a OWL class and the value represents a serialized form (e.g., a RDF/XML
	 * literal).
	 *
	 * @return The initial value associated with this local variable or
	 * 	<code>null</code> if there is no such value.
	 * @see #getInitialValueAsDataValue()
	 * @see #getInitialValueAsIndividual()
	 */
	public OWLValue getInitialValue();


	/**
	 * If this local variable has an initial value associated with it (specified
	 * with <tt>process:initialValue</tt> property) return that value. There is no
	 * restriction as to the contents of the value. As such, it may be an untyped
	 * or typed literal, or a XMLLiteral.
	 *
	 * @return The initial value associated with this local variable or
	 * 	<code>null</code> if there is no such value.
	 * @see #getInitialValueAsIndividual()
	 */
	public OWLDataValue getInitialValueAsDataValue();

	/**
	 * This method tries to get the initial value (if any) as an OWL individual,
	 * provided that the {@link #getParamType() type} of this variable is a OWL
	 * class and the value syntactically represents a serialized OWL individual
	 * (e.g. RDF/XML literal).
	 * Consequently, some heuristic needs to exist that is able to analyze and
	 * decide whether this is the case and eventually transform the literal into
	 * a individual.
	 *
	 * @return If feasible, the initial value transformed to an OWLIndividual,
	 * 	or	<code>null</code> otherwise.
	 * @see #getInitialValue()
	 */
	public OWLIndividual getInitialValueAsIndividual();

	public void setInitialValue(OWLValue value);

	public void removeInitialValue();

}
