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

import org.mindswap.owl.OWLDataValue;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owls.vocabulary.OWLS;

/**
 * ValueConstant is for specifying either a constant (XML) data value, xor
 * referring a OWL individual to be bound as the value to a process variable.
 * Basically, it is an abstraction for the two properties
 * {@link OWLS.Process#valueData} and {@link OWLS.Process#valueObject}.
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:53 $
 */
public interface ValueConstant extends ParameterValue
{

	/**
	 * @return The constant data value. If some OWL individual is actually
	 * 	associated then this method returns <code>null</code>;
	 * 	see {@link #getIndividual()}.
	 */
	public OWLDataValue getData();

	/**
	 * @return The constant OWL individual value. If some data value is actually
	 * 	associated then this method returns <code>null</code>;
	 * 	see {@link #getIndividual()}.
	 */
	public OWLIndividual getIndividual();

	/**
	 * @return <code>true</code> if a {@link OWLDataValue} is assigned.
	 */
	public boolean isDataValue();

	/**
	 * @return <code>true</code> if a {@link OWLIndividual} is assigned.
	 */
	public boolean isIndividualValue();
}
