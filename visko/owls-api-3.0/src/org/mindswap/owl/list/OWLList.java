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
 * Created on Dec 23, 2004
 */
package org.mindswap.owl.list;

import java.util.List;

import org.mindswap.exceptions.InvalidListException;
import org.mindswap.owl.OWLDataValue;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLValue;

/**
 * This interface is intended to provide an OWL-DL compatible version of the
 * <tt>rdf:List</tt> construct. As such, it may store either OWL
 * {@link OWLDataValue data values} or {@link OWLIndividual individuals} (but
 * not mixed together in one instance).
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:54 $
 */
public interface OWLList<V extends OWLValue> extends OWLIndividual, Iterable<V>
{
	/**
	 * Get the index of the first occurrence of the given value in this list,
	 * or <tt>-1</tt> if the value is not in the list.
	 *
	 * @param value The value to search for.
	 * @return The index of the first occurrence of value in this list, or
	 * 	<tt>-1</tt> if not found.
	 */
	public int indexOf(V value);

	public V getFirst();

	public void setFirst(V first);

	public OWLList<V> getRest();

	public void setRest(OWLList<V> rest);

	public ListVocabulary<V> getVocabulary();

	public V get(int index);

	/**
    * Returns the list that is this list with the given value added to the end
    * of the list. This operation will always work, even on an empty list, but
    * the return value is the updated list.  Specifically, in the case of
    * adding a value to the empty list, the returned list will not be the same
    * as this list.
    *
    * @param item A value to add to the end of this list.
    * @return The list that results from adding a value to the end of this list.
    */
	public OWLList<V> with(V item);

	/**
	 * Return a reference to a new list cell whose first is <code>item</code>
	 * and whose rest is this list.
	 *
	 * @param item A new value to add to the head of the list.
	 * @return The new list, whose head is <code>item</code>.
	 */
	public OWLList<V> cons(V item);

	public OWLList<V> insert(int index, V item);

	/**
    * Set/replace the value at the i'th position in the list with the given
    * value.
    *
    * @param index The index into the list, from 0.
    * @param value The new value to associate with the i'th list element.
    * @return The value that was previously at position i in the list.
    * @throws IndexOutOfBoundsException If the list has fewer than (i + 1) elements.
    * @throws InvalidListException If this list is the empty list (list:nil).
    */
	public V set(int index, V value);

	public int size();

	public boolean isEmpty();

	public OWLList<V> remove(V value);

	public OWLList<V> remove(int index);

	public OWLList<V> clear();

	public List<V> toList();

	/**
	 * This method is a convenience tool to simplify list handling when it
	 * is known that the items in the list can be viewed as a more special
	 * OWL type. For instance, suppose there is a list of which it is known
	 * that items can be viewed as {@link org.mindswap.swrl.Atom atoms}. The
	 * following code illustrates this.
	 *
	 * <pre>
	 * // general variant
	 * OWLList&lt;OWLValue&gt; list = ... ;
	 * for (OWLValue item : list)
	 * {
	 * 	Atom atom = item.castTo(Atom.class);
	 * 	// do something with atom
	 * }
	 *
	 * // specialized and more simple variant
	 * OWLList&lt;OWLValue&gt; list = ... ;
	 * OWLList&lt;Atom&gt; atomList = list.specialize(Atom.class);
	 * for (Atom atom : atomList)
	 * {
	 * 	// do something with atom
	 * }
	 * </pre>
	 *
	 * Note that the returned list operates on the same backing model, thus,
	 * changes made to one list are reflected by the other. Consequently,
	 * make sure not to interfere access if both list objects are used.
	 *
	 * @param itemAbstraction The more special class representing the item type.
	 * @return A more special list according to the given item abstraction.
	 */
	public <W extends V> OWLList<W> specialize(Class<W> itemAbstraction);

}
