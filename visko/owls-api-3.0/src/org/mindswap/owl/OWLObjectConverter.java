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
 * Created on Mar 17, 2004
 */
package org.mindswap.owl;

import org.mindswap.exceptions.CastingException;

/**
 * OWL object converter implement the functionality required to determine
 * if a given object can be casted to (viewed as) another object type and
 * also implement the cast operation itself.
 *
 * @param <T> The type of OWL object this converter can cast objects to.
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:52 $
 */
public interface OWLObjectConverter<T extends OWLObject>
{
	/**
	 * Check if the given source object instance can be casted to (viewed as)
	 * the target object type represented by this converter.
	 *
	 * @param source The OWL object subject to be checked.
	 * @param strictConversion Whether to use strict OWL object conversion
	 * 	(<code>true</code>) or not (<code>false</code>).
	 * @see OWLObject#canCastTo(Class)
	 */
	public boolean canCast(OWLObject source, boolean strictConversion);

	/**
	 * Try to cast (convert) the given object to the target object type
	 * represented by this converter.
	 *
	 * @param source The OWL object subject to be checked.
	 * @param strictConversion Whether to use strict OWL object conversion
	 * 	(<code>true</code>) or not (<code>false</code>).
	 * @see OWLObject#castTo(Class)
	 * @throws CastingException In case the source object instance cannot be
	 * 	casted to (viewed as) the target object type represented by this
	 * 	converter.
	 */
	public T cast(OWLObject source, boolean strictConversion);
}
