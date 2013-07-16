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
 * Created on Dec 29, 2003
 */
package org.mindswap.owl;

import impl.owl.CastingList;

import java.net.URI;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.mindswap.common.Parser;
import org.mindswap.common.ServiceFinder;
import org.mindswap.common.Variable;
import org.mindswap.exceptions.CastingException;
import org.mindswap.owls.vocabulary.OWLS;
import org.mindswap.query.ABoxQuery;
import org.mindswap.query.QueryLanguage;

/**
 *
 * @author unascribed
 * @version $Rev: 2317 $; $Author: nick $; $Date: 2011/01/24 06:37:52 $
 */
public class OWLFactory
{
	private static final OWLProvider factory = ServiceFinder.instance().loadImplementation(
		OWLProvider.class, OWLProvider.DEFAULT_OWL_PROVIDER);

	/**
	 * Create a new knowledge base. Initially, its properties are set as follows
	 * <ul>
	 * 	<li>no reasoner is attached, i.e, {@link OWLKnowledgeBase#getReasoner()}
	 * 	returns <code>null</code>,</li>
	 * 	<li>auto consistency is disabled, i.e, {@link OWLKnowledgeBase#isAutoConsistency()}
	 * 	returns <code>false</code>,</li>
	 * 	<li>auto translation is disabled, i.e., {@link OWLKnowledgeBase#isAutoTranslate()}
	 * 	returns <code>false</code>, and</li>
	 * 	<li>strict conversion is enabled, i.e., {@link OWLKnowledgeBase#isStrictConversion()},
	 * 	returns <code>true</code>.</li>
	 * </ul>
	 * @return A new initially empty knowledge base.
	 */
	public static final OWLKnowledgeBase createKB() {
		return factory.createKB();
	}

	/**
	 * Create an empty OWL ontology with the given URI and optionally import the
	 * OWL-S ontologies Service, Profile, Process, and Grounding.
	 * <p>
	 * Attention: Every ontology that is created through an invocation of this
	 * method will be a member of a single (global) backing knowledge base. More
	 * precisely, the knowledge base returned by {@link OWLOntology#getKB()} is
	 * the same among <b>all</b> ontologies returned by this method! If you
	 * need entirely isolated ontologies you need to create a dedicated KB first,
	 * see {@link OWLFactory#createKB()}, and use this KB to create an ontology.
	 *
	 * @param uri The URI for the ontology. Must not be <code>null</code>.
	 * @param importOWLS Whether to import all OWL-S ontologies into the result
	 *        ontology, or not.
	 * @return An newly created ontology.
	 * @deprecated Create a dedicated {@link OWLKnowledgeBase KB} first and then
	 * 	use it to create a new ontology, see {@link #createKB()}.
	 */
	@Deprecated
	public static final OWLOntology createOntology(final URI uri, final boolean importOWLS)
	{
		final OWLOntology ont = factory.createOntology(uri);
		if (importOWLS) OWLS.addOWLSImports(ont);
		return ont;
	}

	/**
	 * @param <T> The type of OWL individuals the list is supposed to contain.
	 * @return A new and empty individuals list.
	 */
	public static final <T extends OWLIndividual> OWLIndividualList<T> createIndividualList() {
		return factory.createIndividualList();
	}

	/**
	 * @param <T> The type of OWL individuals the list is supposed to contain.
	 * @param capacity The initial capacity of the list.
	 * @return A new and empty individuals list with the specified initial capacity.
	 * @exception IllegalArgumentException if the specified initial capacity
	 *            is negative.
	 */
	public static final <T extends OWLIndividual> OWLIndividualList<T> createIndividualList(final int capacity)
	{
		return factory.createIndividualList(capacity);
	}

	/**
	 * This method can be used to resolve the OWL datatype to which a given
	 * Java class can be mapped. This is not a fully fledged and generic
	 * Java-to-OWL type mapping mechanism as it works only for primitive Java
	 * types (<tt>int.class</tt>, ...), their wrapper types (<tt>Integer.class</tt>, ...)
	 * and a few common types, namely {@link URI}, {@link URL}, {@link Date},
	 * and {@link Calendar}.
	 *
	 * @param clazz The Java class.
	 * @return The OWL datatype to which the Java type is mapped, or <code>null</code>
	 * 	if the type can not be directly mapped.
	 */
	public static final OWLDataType getDataType(final Class<?> clazz)
	{
		return factory.getDataType(clazz);
	}

	/**
	 * @return A list of unique names of reasoners provided by the
	 * 	backing implementation.
	 */
	public static final List<String> getReasonerNames()
	{
		return factory.getReasonerNames();
	}

	/**
	 * Create a new reasoner instance. Available reasoner names can be retrieved
	 * by invoking {@link #getReasonerNames()}.
	 *
	 * @param reasonerName The unique name of the reasoner.
	 * @return The reasoner of that name, or <code>null</code> this name is unknown.
	 */
	public static final Object createReasoner(final String reasonerName)
	{
		return factory.createReasoner(reasonerName);
	}

	/**
	 * Create a parser that can convert queries of the given query language into
	 * ABox queries. The details (formal semantics) of such conversions are up
	 * to be defined elsewhere.
	 *
	 * @param model The model to be queried by the returned ABox query.
	 * @param lang The language/syntax of the query.
	 * @return A new ABox query parser.
	 */
	public static final Parser<String, ABoxQuery<Variable>> createABoxQueryParser(
		final OWLModel model, final QueryLanguage lang)
	{
		return factory.createABoxQueryParser(model, lang);
	}

	/**
	 * Cast all elements of the given list to the target OWL concept and
	 * return them in a new list. It can be used whenever it is known that
	 * all source elements also represent the target OWL concept.
	 * <p>
	 * The order of elements is preserved.
	 * <p>
	 * In contrast to {@link #wrapList(List, Class)} this method
	 * reports failed attempts to cast some element into the target concept
	 * immediately by throwing a {@link CastingException}. For the former
	 * throwing this exception is delayed until the first access of some
	 * element in the list.
	 *
	 * @param list The list of elements to be casted.
	 * @param castTarget The target OWL concept into which to cast each element.
	 * @return The list of elements, all casted to the target OWL concept.
	 * @throws AssertionError If <tt>list</tt> and/or <tt>castTarget</tt>
	 * 	is <code>null</code>.
	 * @throws CastingException If some element does not represent an OWL
	 * 	individual (implicitly or explicitly) of the target concept.
	 */
	public static final <T extends OWLIndividual> OWLIndividualList<T> castList(
		final List<? extends OWLIndividual> list, final Class<T> castTarget)
	{
		return factory.castList(list, castTarget);
	}

	/**
	 * Wrap all elements of the given list by a list that allows dynamic casting
	 * of elements into the target OWL concept, only if they are accessed in the
	 * returned list. It can be used whenever it is known that all source elements
	 * also represent the target OWL concept.
	 * <p>
	 * The order of elements is preserved.
	 * <p>
	 * In contrast to {@link #castList(List, Class)} this method
	 * reports failed attempts to cast some element into the target concept
	 * only on first access from the returned list (by throwing a
	 * {@link CastingException}), whereas the former would throw this
	 * exception immediately when elements in the source list are casted and
	 * added to the parameterized list.
	 *
	 * @param list The list of elements to be wrapped.
	 * @param castTarget The target OWL concept into which to cast each element.
	 * @return A list of all elements that will be casted dynamically into the
	 * 	target OWL concept whenever accessed.
	 * @throws AssertionError If <tt>list</tt> and/or <tt>castTarget</tt> is
	 * 	<code>null</code>.
	 */
	public static final <T extends OWLIndividual> CastingList<T> wrapList(
		final List<? extends OWLIndividual> list, final Class<T> castTarget)
	{
		return factory.wrapList(list, castTarget);
	}

	/**
	 * @return The empty OWL individual list (immutable).
	 */
	public static final <T extends OWLIndividual> OWLIndividualList<T> emptyIndividualList()
	{
		return factory.emptyIndividualList();
	}

	/**
    * Returns an unmodifiable view of the specified list.  This method allows
    * to provide users with "read-only" access to lists.  Query operations on
    * the returned list "read through" to the specified list, and attempts to
    * modify the returned list, whether direct or via its iterator, result in
    * an <tt>UnsupportedOperationException</tt>.<p>
    *
    * @param  list The list for which an unmodifiable view is to be returned.
    * @return An unmodifiable view of the specified list.
    */
	public static final <T extends OWLIndividual> OWLIndividualList<T> unmodifiableIndividualList(
		final List<T> list)
	{
		return factory.unmodifiableIndividualList(list);
	}

}
