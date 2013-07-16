/*
 * Created 20.07.2009
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
public class Tuple<T1, T2>
{
	private T1 element1;
	private T2 element2;

	public Tuple(final T1 element1, final T2 element2)
	{
		this.element1 = element1;
		this.element2 = element2;
	}

	/**
	 * @return The first element.
	 */
	public T1 getElement1()
	{
		return element1;
	}

	/**
	 * @return The second element.
	 */
	public T2 getElement2()
	{
		return element2;
	}

	/**
	 * @param element1 The first element to set.
	 * @param element2 The second element to set.
	 */
	public void setElements(final T1 element1, final T2 element2)
	{
		this.element1 = element1;
		this.element2 = element2;
	}

	/* @see java.lang.Object#equals(java.lang.Object) */
	@Override
	public boolean equals(final Object other)
	{
		if ((other == null) || (other.getClass() != this.getClass())) return false;
		return ((element1 != null)? element1.equals(((Tuple<?,?>) other).element1) :
			((Tuple<?,?>) other).element1 == null) &&	((element2 != null)?
				element2.equals(((Tuple<?,?>) other).element2) : ((Tuple<?,?>) other).element2 == null);
	}

	/* @see java.lang.Object#hashCode() */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((element1 == null)? 0 : element1.hashCode());
		result = prime * result + ((element2 == null)? 0 : element2.hashCode());
		return result;
	}

	/* @see java.lang.Object#toString() */
	@Override
	public String toString()
	{
		return "(" + (element1 != null? element1.toString() : null) + ", " +
			(element2 != null? element2.toString() : null) + ")";
	}

}
