/*
 * Created 30.07.2009
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
package org.mindswap.common;

/**
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:53 $
 */
public class OrdinaryVariable implements Variable
{
	private final String name;

	/**
	 * @param name Desired variable name. Prefix character '?' and '$'
	 * 	respectively will be removed.
	 * @throws IllegalArgumentException If <code>name</code> is
	 * 	<code>null</code> or empty.
	 */
	public OrdinaryVariable(final String name)
	{
		if (name == null || name.length() == 0) throw new IllegalArgumentException("Name empty or null");
		this.name = (name.startsWith("?") || name.startsWith("$"))? name.substring(1) : name;
	}

	/* @see org.mindswap.common.Variable#getName() */
	public String getName()
	{
		return name;
	}

	/* @see java.lang.Object#hashCode() */
	@Override
	public int hashCode()
	{
		return name.hashCode();
	}

	/* @see java.lang.Object#equals(java.lang.Object) */
	@Override
	public boolean equals(final Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		OrdinaryVariable other = (OrdinaryVariable) obj;
		return (name.equals(other.name));
	}

	/* @see java.lang.Object#toString() */
	@Override
	public String toString()
	{
		return "?" + name;
	}

}
