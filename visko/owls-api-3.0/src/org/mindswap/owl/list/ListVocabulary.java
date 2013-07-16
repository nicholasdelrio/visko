//The MIT License
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
 * Created on Dec 10, 2004
 */
package org.mindswap.owl.list;

import java.net.URI;

import org.mindswap.owl.EntityFactory;
import org.mindswap.owl.OWLClass;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLObjectProperty;
import org.mindswap.owl.OWLOntology;
import org.mindswap.owl.OWLProperty;
import org.mindswap.owl.OWLValue;
import org.mindswap.utils.URIUtils;

/**
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:54 $
 */
public final class ListVocabulary<T extends OWLValue>
{
	private final Class<T> itemAbstraction;

	private final OWLClass List;
	private final OWLProperty first;
	private final OWLObjectProperty rest;
	private final OWLIndividual nil;

	/**
	 * @param baseURI
	 * @deprecated Use {@link #ListVocabulary(OWLClass, OWLProperty, OWLObjectProperty, OWLIndividual, Class)}
	 * 	instead. This constructor can not assert at runtime that the OWL class
	 * 	indirectly referred by <code>baseURI</code> complies with the generic
	 * 	type parameter (if any).
	 */
	@Deprecated
	public ListVocabulary(final URI baseURI, final Class<T> itemAbstr)
	{
		this(URIUtils.createURI(baseURI, "List"), URIUtils.createURI(baseURI, "first"),
			URIUtils.createURI(baseURI, "rest"), URIUtils.createURI(baseURI, "nil"), itemAbstr);
	}

	/**
	 * @param List
	 * @param first
	 * @param rest
	 * @param nil
	 * @deprecated Use {@link #ListVocabulary(OWLClass, OWLProperty, OWLObjectProperty, OWLIndividual, Class)}
	 * 	instead. This constructor can not assert at runtime that the OWL class
	 * 	referred by the <code>List</code> complies with the generic type parameter
	 * 	(if any).
	 */
	@Deprecated
	public ListVocabulary(final URI List, final URI first, final URI rest, final URI nil, final Class<T> itemAbstr)
	{
		this.List = EntityFactory.createClass(List);
		this.itemAbstraction = itemAbstr;
		this.first = EntityFactory.createObjectProperty(first);
		this.rest = EntityFactory.createObjectProperty(rest);
		this.nil = EntityFactory.createInstance(this.List, nil);
	}

	/**
	 * @param baseURI
	 * @param ont
	 * @param itemAbstr
	 */
	public ListVocabulary(final URI baseURI, final OWLOntology ont, final Class<T> itemAbstr)
	{
		this.List = ont.getClass(URIUtils.createURI(baseURI, "List"));
		this.first = ont.getObjectProperty(URIUtils.createURI(baseURI, "first"));
		this.rest = ont.getObjectProperty(URIUtils.createURI(baseURI, "rest"));
		this.nil = ont.getIndividual(URIUtils.createURI(baseURI, "nil"));
		this.itemAbstraction = itemAbstr;
	}

	/**
	 * @param List
	 * @param first
	 * @param rest
	 * @param nil
	 * @param itemAbstr
	 */
	public ListVocabulary(final OWLClass List, final OWLProperty first,
		final OWLObjectProperty rest, final OWLIndividual nil, final Class<T> itemAbstr)
	{
		this.List = List;
		this.first = first;
		this.rest = rest;
		this.nil = nil;
		this.itemAbstraction = itemAbstr;
	}

	/**
	 * @return The Java class or interface representing the abstraction used
	 * 	to represent list items.
	 */
	public final Class<T> itemAbstraction()
	{
		return itemAbstraction;
	}

	/**
	 * @return The OWL class of the list structure represented by this
	 * 	vocabulary.
	 */
	public final OWLClass list()
	{
		return List;
	}

	/**
	 * @return The property used to refer to the head of a list cell, i.e.,
	 * 	the item of a list cell.
	 */
	public final OWLProperty first()
	{
		return first;
	}

	/**
	 * @return The property used to refer to the tail of a list cell, i.e.,
	 * 	the next list cell.
	 */
	public final OWLObjectProperty rest()
	{
		return rest;
	}

	/**
	 * @return The individual representing the empty list.
	 */
	public final OWLIndividual nil()
	{
		return nil;
	}

	/**
	 * Create a more special list vocabulary based on this vocabulary. The
	 * general contract is that <code>listType</code> is a sub class of
	 * {@link #list()} and <code>itemAbstraction</code> is a sub class or sub
	 * interface of {@link #itemAbstraction()}.
	 *
	 * @param listType The OWL class which should be a sub class of the class
	 * 	returned by {@link #list()}.
	 * @param itemAbstr The more special Java class or interface used by
	 * 	the specialized list vocabulary.
	 * @return The more special list vocabulary.
	 */
	public final <U extends T> ListVocabulary<U> specialize(final OWLClass listType, final Class<U> itemAbstr)
	{
		return new ListVocabulary<U>(listType, first, rest, nil, itemAbstr);
	}

}
