/*
 * Created 17.05.2009
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
package impl.jena;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.mindswap.exceptions.InvalidListException;
import org.mindswap.owl.OWLValue;
import org.mindswap.owl.list.ListVocabulary;
import org.mindswap.owl.list.OWLList;

import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.shared.PropertyNotFoundException;
import com.hp.hpl.jena.vocabulary.RDF;

/**
 *
 * @author unascribed
 * @version $Rev: 2309 $; $Author: nick $; $Date: 2011/01/24 06:37:51 $
 */
public class OWLListImpl<V extends OWLValue> extends OWLIndividualImpl implements OWLList<V>
{
	protected final ListVocabulary<V> vocabulary;

	public OWLListImpl(final OWLOntologyImpl ontology, final Resource resource,
		final ListVocabulary<V> vocabulary)
	{
		super(ontology, resource);
		this.vocabulary = vocabulary;
	}

	private final class RDFListIterator implements Iterator<V>
	{
		private Resource list;

		RDFListIterator(final Resource list)
		{
			this.list = list;
		}

		/* @see java.util.Iterator#remove() */
		public void remove()
		{
			// I tried to implement it but realized then that removals resulting in
			// the empty list require to reassign OWLListImpl.resource of the enclosing
			// list instance, which is not possible because it is final.
			throw new UnsupportedOperationException("Cannot remove from list iterator.");
		}

		/* @see java.util.Iterator#hasNext() */
		public boolean hasNext()
		{
			return !isEmptyList(list);
		}

		/* @see java.util.Iterator#next() */
		public V next()
		{
			if (isEmptyList(list)) throw new NoSuchElementException();

			final RDFNode tmp = getFirstProperty(list);
			final V next = makeListItem(tmp);

			list = getRestProperty(list);
			return next;
		}
	}

	/* @see org.mindswap.owl.list.OWLList#getRest() */
	public OWLList<V> getRest()
	{
		final Resource rest = getRestProperty(resource);
		return makeList(rest);
	}

	/* @see org.mindswap.owl.list.OWLList#setRest(org.mindswap.owl.list.OWLList) */
	public void setRest(final OWLList<V> rest)
	{
		setRestProperty(resource, (Resource) rest.getImplementation());
	}

	/* @see org.mindswap.owl.list.OWLList#getFirst() */
	public V getFirst()
	{
		checkNotNil("Tried to get head of empty (nil) list.");
		final RDFNode first = getFirstProperty(resource);
		return makeListItem(first);
	}

	/* @see org.mindswap.owl.list.OWLList#setFirst(org.mindswap.owl.OWLValue) */
	public void setFirst(final V value)
	{
		checkNotNil("Tried to set the head of empty (nil) list.");
		setFirstProperty(resource, (RDFNode) value.getImplementation());
	}

	/* @see org.mindswap.owl.list.OWLList#getAllValues() */
	public List<V> toList()
	{
		final List<V> result = new ArrayList<V>();
		for (V item : this)
		{
			result.add(item);
		}
		return result;
	}

	/* @see org.mindswap.owl.list.OWLList#get(int) */
	public V get(final int index)
	{
		if (index == 0) return getFirst();
		if (index < 0 || isEmpty()) throw new IndexOutOfBoundsException("Index less than zero or empty list.");

		try
		{
			final Resource list = getListAt(resource, index);
			final RDFNode value = getFirstProperty(list);
			return makeListItem(value);
		}
		catch (PropertyNotFoundException e) // this happens if index >= size
		{
			throw new IndexOutOfBoundsException("Invalid list structure or index larger than list size.");
		}
	}

	/* @see org.mindswap.owl.list.OWLList#with(org.mindswap.owl.OWLValue) */
	public OWLList<V> with(final V item)
	{
		if (isEmpty()) return makeList(cons(resource, (RDFNode) item.getImplementation()));

		Resource seen = resource, rest = getRestProperty(resource);
		while (!isEmptyList(rest))
		{
			seen = rest;
			rest = getRestProperty(rest);
		}
		setRestProperty(seen, cons(rest, (RDFNode) item.getImplementation()));
		return this;
	}

	/* @see org.mindswap.owl.list.OWLList#cons(org.mindswap.owl.OWLValue) */
	public OWLList<V> cons(final V value)
	{
		return makeList(cons(resource, (RDFNode) value.getImplementation()));
	}

	/* @see org.mindswap.owl.list.OWLList#indexOf(org.mindswap.owl.OWLValue) */
	public int indexOf(final V value)
	{
		int index = -1;
		Resource list = resource;
		while (!isEmptyList(list))
		{
			index++;
			if (value.getImplementation().equals(getFirstProperty(list))) return index;
			list = getRestProperty(list);
		}
		return -1;
	}

	/* @see org.mindswap.owl.list.OWLList#insert(int, org.mindswap.owl.OWLValue) */
	public OWLList<V> insert(final int index, final V value)
	{
		if (index < 0) throw new IndexOutOfBoundsException("Index less than zero.");
		if (index == 0) return cons(value);

		try
		{
			Resource seen = resource, rest = getRestProperty(resource);
			for (int i = 1; i < index; i++)
			{
				seen = rest;
				rest = getRestProperty(rest);
			}
			setRestProperty(seen, cons(rest, (RDFNode) value.getImplementation()));
		}
		catch (PropertyNotFoundException e) // this happens if index > size
		{
			throw new IndexOutOfBoundsException("Invalid list structure or index larger than list size.");
		}
		return this;
	}

	/* @see org.mindswap.owl.list.OWLList#remove(org.mindswap.owl.OWLValue) */
	public OWLList<V> remove(final V value)
	{
		if (value == null || isEmpty()) return this;

		int index = 0;
		Resource list = resource, rest;
		RDFNode first;
		try
		{
			while (!isEmptyList(list))
			{
				first = getFirstProperty(list);
				if (first.equals(value.getImplementation()))
				{
					rest = remove0(list);

					// removal at beginning of list --> list was deleted and rest becomes the new start
					if (index == 0) return makeList(rest);

					// otherwise, the previous list element needs to be relinked to refer to rest
					list = list.getModel().listResourcesWithProperty(
						(Property) vocabulary.rest().getImplementation(), list).next();
					setRestProperty(list, rest);
					return this;
				}

				list = getRestProperty(list); // new list = rest of current list
				rest = getRestProperty(list); // new rest = rest of new list
				index++;
			}
		}
		catch (PropertyNotFoundException e)
		{
			// only if not end of list was reached we do propagate exception
			// because structure of list seems to be mixed up
			if (!isEmptyList(list)) throw new InvalidListException("Invalid list structure: Details: " + e);
		}
		return this;
	}

	/* @see org.mindswap.owl.list.OWLList#remove(int) */
	public OWLList<V> remove(final int index)
	{
		if (index < 0 || isEmpty()) throw new IndexOutOfBoundsException("Index less than zero or empty list.");

		try
		{
			Resource list = getListAt(resource, index);
			final Resource rest = remove0(list);

			// removal at beginning of list --> list was deleted and rest becomes the new start
			if (index == 0) return makeList(rest);

			// otherwise, the previous list element needs to be relinked to refer to rest
			list = list.getModel().listResourcesWithProperty(
				(Property) vocabulary.rest().getImplementation(), list).next();
			setRestProperty(list, rest);
			return this;
		}
		catch (PropertyNotFoundException e) // this happens if index >= size
		{
			throw new IndexOutOfBoundsException("Invalid list structure or index larger than list size.");
		}
	}

	/* @see org.mindswap.owl.list.OWLList#clear() */
	public OWLList<V> clear()
	{
		Resource r = resource;
		while (!isEmptyList(r))
		{
			r = remove0(r);
		}
		return makeList(r); // r is now the empty list
	}

	/* @see impl.owl.WrappedIndividual#delete() */
	@Override
	public void delete()
	{
		OWLList<V> empty = clear();
		ontology.remove(empty, false);
	}

	/* @see org.mindswap.owl.list.OWLList#set(int, org.mindswap.owl.OWLValue) */
	public V set(final int index, final V value)
	{
		checkNotNil("Cannot set value in empty (nil) list. Use add instead.");

		if (index < 0 || index >= size())
		{
			throw new IndexOutOfBoundsException("Index less than zero or not less than list size.");
		}

		final Resource list = getListAt(resource, index);
		final RDFNode oldValue = getFirstProperty(list);
		setFirstProperty(list, (RDFNode) value.getImplementation());
		return makeListItem(oldValue);
	}

	/* @see java.lang.Iterable#iterator() */
	public Iterator<V> iterator()
	{
		return new RDFListIterator(resource);
	}

	/* @see org.mindswap.owl.list.OWLList#size() */
	public final int size()
	{
		// do not use recursive approach for counting size as it may crash for large lists (stack overflow)
		int size = 0;
		Resource list = resource;
		while(!isEmptyList(list))
		{
			list = getRestProperty(list);
			size++;
		}
		return size;
	}

	/* @see org.mindswap.owl.list.OWLList#isEmpty() */
	public boolean isEmpty()
	{
		return isEmptyList(resource);
	}

	/* @see org.mindswap.owl.list.OWLList#getVocabulary() */
	public ListVocabulary<V> getVocabulary()
	{
		return vocabulary;
	}

	/* @see org.mindswap.owl.list.OWLList#specialize(java.lang.Class) */
	@SuppressWarnings("unchecked")
	public <W extends V> OWLList<W> specialize(final Class<W> itemAbstr)
	{
		// shortcut - in case this list is already so special as intended
		if (vocabulary.itemAbstraction().equals(itemAbstr))
			return (OWLList<W>) this; // cast is not critical because of the equals check (V equals W)

		ListVocabulary<W> specializedVocabulary = vocabulary.specialize(vocabulary.list(), itemAbstr);
		return new OWLListImpl<W>(ontology, resource, specializedVocabulary);
	}

	/* @see impl.owl.WrappedIndividual#toString() */
	@Override
	public String toString()
	{
		final StringBuilder str = new StringBuilder("[");
		boolean notFirst = false;
		for (V item : this)
		{
			if (notFirst) str.append(", ");
			else notFirst = true;

			str.append(item.toString());
		}
		str.append("]");
		return str.toString();
	}

	/**
	 * Helper method for optimization purposes to prevent the need to cast an
	 * individual to an OWLList in order to be able to get the value associated
	 * to the {@link ListVocabulary#first()} property.
	 *
	 * @param list The OWLList for which to return the value associated with
	 * 	the {@link ListVocabulary#first()} property.
	 * @return The actual value.
	 */
	RDFNode getFirstProperty(final Resource list)
	{
		return list.getRequiredProperty((Property) vocabulary.first().getImplementation()).getObject();
	}

	/**
	 * Helper method for optimization purposes, for instance, to iterate through
	 * a list while it is not required to cast every time to a OWLList object.
	 * In other words, this method is functionally equivalent to {@link #getRest()}
	 * except that it does not try to cast the rest list to an instance of OWLList.
	 *
	 * @param list The OWLList for which to return the value of its
	 * 	{@link ListVocabulary#rest() rest property}.
	 * @return The tail list.
	 */
	Resource getRestProperty(final Resource list)
	{
		return list.getRequiredProperty((Property) vocabulary.rest().getImplementation()).getResource();
	}

	boolean isEmptyList(final Resource list)
	{
		return list.equals(vocabulary.nil().getImplementation());
	}

	/**
	 * Wrap a list item value as the specific list item type.
	 *
	 * @param item An RDF node whose <tt>rdf:type</tt> is equal to
	 * 	the RDFS/OWL class this specific list is supposed to handle.
	 * @return The given individual wrapped as the specific list item object.
	 */
	V makeListItem(final RDFNode item)
	{
		final OWLValue listItem;
		if (item.isLiteral())
		{
			listItem = new OWLDataValueImpl(item.as(Literal.class));
		}
		else
		{
			listItem = new OWLIndividualImpl(ontology, item.as(Resource.class));
		}
		return listItem.castTo(vocabulary.itemAbstraction());
	}

	/**
	 * Wrap a list instance as the specific list type.
	 *
	 * @param listResource An OWL individual whose <tt>rdf:type</tt> is equal to
	 * 	<code>this.vocabulary.List()</code>.
	 * @return The given individual wrapped as the specific list object.
	 */
	OWLListImpl<V> makeList(final Resource listResource)
	{
		return new OWLListImpl<V>(ontology, listResource, vocabulary);
	}

	/**
    * Check that this list is not the nil list, and throw an invalid list
    * exception if it is.
    *
    * @param msg The context message for the empty list exception.
    * @throws InvalidListException If this list is the nil list.
    */
   private void checkNotNil(final String msg)
   {
       if (isEmpty()) throw new InvalidListException(msg);
   }

	private Resource cons(final Resource rest, final RDFNode value)
	{
		final Resource newList = createNewList();
		newList.addProperty((Property) vocabulary.first().getImplementation(), value);
		newList.addProperty((Property) vocabulary.rest().getImplementation(), rest);
		return newList;
	}

	private Resource createNewList()
	{
		return ontology.ontModel.createResource((Resource) vocabulary.list().getImplementation());
	}

	private Resource getListAt(Resource list, final int index)
	{
		for (int i = 0; i < index; i++)
		{
			list = getRestProperty(list);
		}
		return list;
	}

	Resource remove0(final Resource r)
	{
		final Resource rest = getRestProperty(r);

		ontology.ontModel.removeAll(r, (Property) vocabulary.first().getImplementation(), null);
		ontology.ontModel.removeAll(r, (Property) vocabulary.rest().getImplementation(), null);
		// we should not simply remove all types because resource may be subject of other statements
		ontology.ontModel.remove(r, RDF.type, (Resource) vocabulary.list().getImplementation());

		return rest;
	}

	private void setFirstProperty(final Resource list, final RDFNode value)
	{
		// first remove the existing head
		ontology.ontModel.removeAll(list, (Property) vocabulary.first().getImplementation(), null);

      // now add the new head value
		list.addProperty((Property) vocabulary.first().getImplementation(), value);
	}

	void setRestProperty(final Resource list, final Resource rest)
	{
		// first remove the existing tail
		ontology.ontModel.removeAll(list, (Property) vocabulary.rest().getImplementation(), null);

		// now add the new tail
		list.addProperty((Property) vocabulary.rest().getImplementation(), rest);
	}
}
