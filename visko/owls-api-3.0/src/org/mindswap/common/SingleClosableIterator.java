/*
 * Created 09.07.2009
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

import java.util.NoSuchElementException;

import org.mindswap.owl.OWLValue;
import org.mindswap.query.ValueMap;

/**
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:53 $
 */
public final class SingleClosableIterator<V extends Variable> implements ClosableIterator<ValueMap<V, OWLValue>>
{
	@SuppressWarnings("unchecked")
	private static final SingleClosableIterator NO_ELEMENT = new SingleClosableIterator(null);

	private ValueMap<V, OWLValue> element;

	private SingleClosableIterator(final ValueMap<V, OWLValue> element)
	{
		this.element = element;
	}

	/* @see org.mindswap.utils.ClosableIterator#close() */
	public final void close()
	{
		element = null;
	}

	/* @see java.util.Iterator#hasNext() */
	public final boolean hasNext()
	{
		return element != null;
	}

	/* @see java.util.Iterator#next() */
	public final ValueMap<V, OWLValue> next()
	{
		if (element != null)
		{
			ValueMap<V, OWLValue> result = element;
			element = null;
			return result;
		}
		throw new NoSuchElementException();
	}

	/* @see java.util.Iterator#remove() */
	public final void remove()
	{
		throw new UnsupportedOperationException("Not supported.");
	}

	public static final <V extends Variable> ClosableIterator<ValueMap<V, OWLValue>> oneElement()
	{
		return new SingleClosableIterator<V>(ValueMap.<V,OWLValue>emptyValueMap());
	}

	@SuppressWarnings("unchecked") // not critical since value map is null
	public static final <V extends Variable> ClosableIterator<ValueMap<V, OWLValue>> noElement()
	{
		return NO_ELEMENT;
	}
}
