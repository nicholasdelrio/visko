// The MIT License
//
// Copyright (c) 2004 Evren Sirin
// Copyright (c) 2009 Thorsten Möller - University of Basel Switzerland
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
 * Created on Nov 21, 2004
 */
package org.mindswap.owl;


/**
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:52 $
 */
public interface OWLDataType extends OWLType
{
	/**
	 * Test whether the given string is a legal lexical form of this datatype.
	 *
	 * @throws UnsupportedOperationException If this datatype does not support validation.
	 */
	public boolean isValid(String lexicalForm);

	/**
	 * Test whether the given object is a legal value form of this datatype.
	 *
	 * @throws UnsupportedOperationException If this datatype does not support validation.
	 */
	public boolean isValidValue(Object valueForm);

	/**
	 * Parse a value in lexical form to its corresponding representation as a
	 * Java object, i.e., realizes the mapping from lexical to value space.
	 *
	 * @throws IllegalArgumentException If the lexical form is not legal.
	 * @throws UnsupportedOperationException If this datatype does not support parsing.
	 */
	public Object parse(String lexicalForm);

	/**
	 * Convert a value of this datatype in its Java representation to its
	 * corresponding lexical form, i.e., realizes the mapping from value to
	 * lexical space.
	 *
	 * @throws UnsupportedOperationException If this datatype does not support parsing.
	 */
	public String unparse(Object value);
}
