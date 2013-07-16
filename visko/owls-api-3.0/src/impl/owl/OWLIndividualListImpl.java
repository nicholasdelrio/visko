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
 * Created on Dec 27, 2003
 */
package impl.owl;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLIndividualList;

/**
 *
 * @author unascribed
 * @version $Rev: 2317 $; $Author: nick $; $Date: 2011/01/24 06:37:54 $
 */
public class OWLIndividualListImpl<E extends OWLIndividual> implements OWLIndividualList<E>
{
	private final List<E> baseList;

	/**
	 * New empty list using an {@link ArrayList} as the backing data structure.
	 */
	public OWLIndividualListImpl()
	{
		baseList = new ArrayList<E>();
	}

	/**
	 * Constructs a list using the given list as the backing data structure.
	 * In contrast to {@link #OWLIndividualListImpl(Collection)}, the given
	 * list is used as the backing data structure (not copied).
	 *
	 * @param l The list which will be used as the backing list.
	 * @throws AssertionError If <code>l</code> is <code>null</code>.
	 */
	public OWLIndividualListImpl(final List<E> l)
	{
		assert l != null : "Illegal: base list was null.";
		this.baseList = l;
	}

	/**
	 * Constructs a list containing the elements of the given collection, in the
	 * order they are returned by the collection's iterator.
	 *
	 * @param c The collection whose elements are to be copied into this list.
	 * @throws AssertionError If <code>c</code> is <code>null</code>.
	 */
	public OWLIndividualListImpl(final Collection<E> c)
	{
		assert c != null : "Illegal: base collection was null.";
		this.baseList = new ArrayList<E>(c);
	}

	/**
	 * New empty list using an {@link ArrayList} as the backing data structure
	 * and an initial capacity as specified.
	 */
	public OWLIndividualListImpl(final int initialCapacity)
	{
		baseList = new ArrayList<E>(initialCapacity);
	}

	/* @see org.mindswap.owl.OWLIndividualList#addWithoutDuplicate(org.mindswap.owl.OWLIndividual) */
	public boolean addWithoutDuplicate(final E individual)
	{
		if (contains(individual)) return false;
		return add(individual);
	}

	/* @see org.mindswap.owls.process.BindingList#addBindingWithoutDuplicate(org.mindswap.owls.process.BindingList) */
	public boolean addWithoutDuplicate(final List<E> individuals)
	{
		boolean result = false;
		for (final E individual : individuals)
		{
			result |= addWithoutDuplicate(individual);
		}
		return result;
	}

	/* @see org.mindswap.owl.OWLIndividualList#getIndividual(java.net.URI) */
	public E getIndividual(final URI uri)
	{
		for (final E element : baseList)
		{
			if (element.isAnon()) continue;
			if (element.getURI().equals(uri)) return element;
		}
		return null;
	}

	/* @see org.mindswap.owl.OWLIndividualList#getIndividual(java.lang.String) */
	public E getIndividual(final String localName)
	{
		for (final E element : baseList)
		{
			if (element.isAnon()) continue;
			if (element.getLocalName().equals(localName)) return element;
		}
		return null;
	}

	/* @see java.util.List#add(java.lang.Object) */
	public boolean add(final E o)
	{
		return baseList.add(o);
	}

	/* @see java.util.List#add(int, java.lang.Object) */
	public void add(final int index, final E element)
	{
		baseList.add(index, element);
	}

	/* @see java.util.List#addAll(java.util.Collection) */
	public boolean addAll(final Collection<? extends E> c)
	{
		return baseList.addAll(c);
	}

	/* @see java.util.List#addAll(int, java.util.Collection) */
	public boolean addAll(final int index, final Collection<? extends E> c)
	{
		return baseList.addAll(index, c);
	}

	/* @see java.util.List#clear() */
	public void clear()
	{
		baseList.clear();
	}

	/* @see java.util.List#contains(java.lang.Object) */
	public boolean contains(final Object o)
	{
		return baseList.contains(o);
	}

	/* @see java.util.List#containsAll(java.util.Collection) */
	public boolean containsAll(final Collection<?> c)
	{
		return baseList.containsAll(c);
	}

	/* @see java.util.List#get(int) */
	public E get(final int index)
	{
		return baseList.get(index);
	}

	/* @see java.util.List#indexOf(java.lang.Object) */
	public int indexOf(final Object o)
	{
		return baseList.indexOf(o);
	}

	/* @see java.util.List#isEmpty() */
	public boolean isEmpty()
	{
		return baseList.isEmpty();
	}

	/* @see java.util.List#iterator() */
	public Iterator<E> iterator()
	{
		return baseList.iterator();
	}

	/* @see java.util.List#lastIndexOf(java.lang.Object) */
	public int lastIndexOf(final Object o)
	{
		return baseList.lastIndexOf(o);
	}

	/* @see java.util.List#listIterator() */
	public ListIterator<E> listIterator()
	{
		return baseList.listIterator();
	}

	/* @see java.util.List#listIterator(int) */
	public ListIterator<E> listIterator(final int index)
	{
		return baseList.listIterator(index);
	}

	/* @see java.util.List#remove(java.lang.Object) */
	public boolean remove(final Object o)
	{
		return baseList.remove(o);
	}

	/* @see java.util.List#remove(int) */
	public E remove(final int index)
	{
		return baseList.remove(index);
	}

	/* @see java.util.List#removeAll(java.util.Collection) */
	public boolean removeAll(final Collection<?> c)
	{
		return baseList.retainAll(c);
	}

	/* @see java.util.List#retainAll(java.util.Collection) */
	public boolean retainAll(final Collection<?> c)
	{
		return baseList.retainAll(c);
	}

	/* @see java.util.List#set(int, java.lang.Object) */
	public E set(final int index, final E element)
	{
		return baseList.set(index, element);
	}

	/* @see java.util.List#size() */
	public int size()
	{
		return baseList.size();
	}

	/* @see java.util.List#subList(int, int) */
	public List<E> subList(final int fromIndex, final int toIndex)
	{
		return baseList.subList(fromIndex, toIndex);
	}

	/* @see java.util.List#toArray() */
	public Object[] toArray()
	{
		return baseList.toArray();
	}

	/* @see java.util.List#toArray(T[]) */
	public <T> T[] toArray(final T[] a)
	{
		return baseList.toArray(a);
	}

	/* @see java.lang.Object#toString() */
	@Override
	public String toString()
	{
		return baseList.toString();
	}

	protected List<E> getBaseList()
	{
		return baseList;
	}

}
