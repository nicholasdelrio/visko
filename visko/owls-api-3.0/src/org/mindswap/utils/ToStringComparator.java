/*
 * Created on 13.01.2009
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
package org.mindswap.utils;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Compares two object instances just by invoking {@link Object#toString()} on
 * them and then compares the strings.
 * <p>
 * There is no need to create dedicated instances of this class. Just use the
 * existing {@link #INSTANCE}.
 *
 * @author unascribed
 * @version $Rev:$; $Author: nick $; $Date: 2011/01/24 06:37:53 $
 */
public final class ToStringComparator implements Comparator<Object>, Serializable
{
	/**
	 * To be used whenever a ToStringComparator is required. There is no need
	 * to create dedicated instances.
	 */
	public static final Comparator<Object> INSTANCE = new ToStringComparator();

	private static final long serialVersionUID = 200833317882878761L;

	/* @see java.util.Comparator#compare(java.lang.Object, java.lang.Object) */
	public int compare(final Object o1, final Object o2)
	{
		return o1.toString().compareTo(o2.toString());
	}
}