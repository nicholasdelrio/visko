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
 * Created on Dec 21, 2004
 */
package impl.owl;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.mindswap.owl.OWLIndividual;

/**
 * Implementation of a list with dynamic cast behavior. Instances will be created
 * by specifying i.) the target OWL individual type to which all elements of this
 * list will tried to cast on read access via getter or iterator. In other words,
 * casting lists are not compile-time type safe and may throw
 * {@link org.mindswap.exceptions.CastingException casting exceptions} at
 * runtime. Optionally, instances can also be created by injecting an existing
 * list that may contain subclasses of {@link OWLIndividual}. If the elements of
 * this list can be casted to the target OWL individual type everything is fine.
 * Otherwise, clients will experience above-mentioned exceptions.
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:54 $
 */
public class CastingList<E extends OWLIndividual> extends OWLIndividualListImpl<E>
{
	protected final Class<E> castTarget;

	/**
	 * @param castTarget
	 * @throws AssertionError If <code>castTarget</code> is <code>null</code>.
	 */
	public CastingList(final Class<E> castTarget)
	{
		this(new ArrayList<E>(), castTarget);
	}

	/**
	 * @param list
	 * @param castTarget
	 * @throws AssertionError If <code>castTarget</code> is <code>null</code>.
	 */
	public CastingList(final List<? extends OWLIndividual> list, final Class<E> castTarget)
	{
		// Unchecked conversion warning by compiler is not critical since elements of list
		// are casted to E for every access method in this class.
		super(list instanceof CastingList? ((CastingList) list).getBaseList() : list);
		assert castTarget != null : "Illegal: cast target class was null.";
		this.castTarget = castTarget;
	}

	/* @see impl.owl.OWLIndividualListImpl#get(int) */
	@Override
	public E get(final int index)
	{
		return super.get(index).castTo(castTarget);
	}

	/* @see impl.owl.OWLIndividualListImpl#iterator() */
	@Override
	public Iterator<E> iterator()
	{
		return listIterator(0);
	}

	/* @see impl.owl.OWLIndividualListImpl#listIterator() */
	@Override
	public ListIterator<E> listIterator()
	{
		return listIterator(0);
	}

	/* @see impl.owl.OWLIndividualListImpl#listIterator(int) */
	@Override
	public ListIterator<E> listIterator(final int index)
	{
		return new ListIterator<E>() {
			ListIterator<E> i = CastingList.this.getListIterator(index);

			public boolean hasNext()
			{
				return i.hasNext();
			}

			public E next()
			{
				return i.next().castTo(castTarget);
			}

			public boolean hasPrevious()
			{
				return i.hasPrevious();
			}

			public E previous()
			{
				return i.previous().castTo(castTarget);
			}

			public int nextIndex()
			{
				return i.nextIndex();
			}

			public int previousIndex()
			{
				return i.previousIndex();
			}

			public void remove()
			{
				i.remove();
			}

			public void set(final E o)
			{
				i.set(o);
			}

			public void add(final E o)
			{
				i.add(o);
			}
		};
	}

	ListIterator<E> getListIterator(final int index)
	{
		return super.listIterator(index);
	}

	/* @see impl.owl.OWLIndividualListImpl#remove(int) */
	@Override
	public E remove(final int index)
	{
		return super.remove(index).castTo(castTarget);
	}

	/* @see impl.owl.OWLIndividualListImpl#set(int, org.mindswap.owl.OWLIndividual) */
	@Override
	public E set(final int index, final E element)
	{
		return super.set(index, element).castTo(castTarget);
	}

	/* @see impl.owl.OWLIndividualListImpl#subList(int, int) */
	@Override
	public List<E> subList(final int fromIndex, final int toIndex)
	{
		return new CastingList<E>(super.subList(fromIndex, toIndex), castTarget);
	}

	/* @see impl.owl.OWLIndividualListImpl#toArray() */
	@Override
	public Object[] toArray()
	{
		final int size = size();
		final Object[] result = new Object[size];

		for (int i = 0; i < size; i++)
			result[i] = get(i);

		return result;
	}

	/* @see impl.owl.OWLIndividualListImpl#toArray(T[]) */
	@Override
	public <T> T[] toArray(T a[])
	{
		final int size = size();
		if (a.length < size) a = (T[]) Array.newInstance(a.getClass().getComponentType(), size);

		final Object[] result = a;
		for (int i = 0; i < size; i++)
			result[i] = get(i);

		if (a.length > size) a[size] = null;

		return a;
	}
}
