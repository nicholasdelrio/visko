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
 * Created on Nov 20, 2004
 */
package org.mindswap.owl;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URI;
import java.util.Set;

import org.mindswap.owls.service.Service;
import org.mindswap.utils.QNameProvider;

/**
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:52 $
 */
public interface OWLKnowledgeBase extends OWLModel
{
	/**
	 * Add an entry for an alternative copy of some OWL ontology with the given
	 * resource URL. This method needs to be invoked <b>before</b> the ontology
	 * is read respectively loaded to this KB.
	 * <p>
	 * Attention! Even though this is an instance method, invoking it has an
	 * global effect, as long as Jena is used as the underlying API! As a
	 * consequence, make sure to invoke this method with caution if multiple
	 * knowledge bases exist or in multithreaded environments in order not to
	 * cause inferences or unexpected behavior.
	 *
	 * @param original The default or main URL of the ontology document.
	 * @param copy A locally resolvable URL where an alternative copy of the
	 * 	ontology document can be found.
	 * @return The previous value of an alternative copy, if known, or
	 * 	<code>original</code> otherwise.
	 */
	public URI addAlternativeLocation(URI original, URI copy);

	/**
	 * Return the set of ontologies loaded to this KB with the option to include/exclude
	 * imported ones. An ontology <tt>ont</tt> is considered to be imported if there
	 * was no explicit method call to load the ontology, i.e, {@link #load(OWLOntology, boolean)}
	 * or {@link #read(URI)}, but <tt>ont</tt> was imported by one of the loaded ontologies.
	 *
	 * @param all If <code>true</code> return all ontologies, otherwise return
	 * 	only the explicitly loaded ones.
	 * @return The set of ontologies.
	 */
	public Set<OWLOntology> getOntologies(boolean all);

	/**
	 * Return the loaded ontology associated with this URI. Returns <code>null</code>
	 * if there is no ontology with this URI.
	 *
	 * @param uri URI of the ontology.
	 * @return Ontology with the given URI, or <code>null</code> if none exists.
	 */
	public OWLOntology getOntology(URI uri);

	/**
	 * Create an empty ontology with the given URI (may represent a logical
	 * or physical URI). The new ontology will belong to this KB.
	 *
	 * @param uri Logical URI of the ontology xor the physical URI of the
	 * 	ontology from where it can be loaded. Must not be <code>null</code>.
	 * @return A new and initially empty ontology.
	 */
	public OWLOntology createOntology(URI uri);

	/**
	 * Create an empty ontology with the given URI (may represent a logical
	 * or physical URI) and an implementation specific data structure (Jena
	 * Model). The new ontology will belong to this KB.
	 *
	 * @param uri Logical URI of the ontology xor the physical URI of the
	 * 	ontology from where it can be loaded. May be <code>null</code>.
	 * @param model The implementation specific data structure.
	 * @return A new ontology representing the given model.
	 * @throws ClassCastException If <code>model</code> is not compatible with
	 * 	the expected implementation specific data structure.
	 */
	public OWLOntology createOntology(URI uri, Object model);

	/**
	 * Load the ontology to the KB. If the ontology is created using another KB a
	 * copy will be created before load. The imports will automatically be loaded
	 * if the second parameter is true. It is guaranteed that {@link OWLOntology#getKB()}
	 * on the returned OWLOntology (and any of its import ontologies) will return
	 * a reference to this KB.
	 * <p>
	 * The ontology will be rejected if it causes this KB to become inconsistent.
	 * This requires a reasoner to be attached to this KB (see {@link #getReasoner()})
	 * and auto consistency to be active (see {@link #isAutoConsistency()}).
	 *
	 * @param ontology The ontology to load or <code>null</code> if it was not loaded.
	 * @param withImports Whether or not to include imported ontologies.
	 * @return The loaded ontology.
	 */
	public OWLOntology load(OWLOntology ontology, boolean withImports);

	/**
	 * Unload the ontology from the KB. The imports of the ontology will
	 * automatically be unloaded if there are no other ontologies importing them.
	 * Note that ontologies loaded explicitly using {@link #load(OWLOntology, boolean)}
	 * will not be unloaded. This method does nothing if the ontology is not
	 * loaded in this KB.
	 *
	 * @param ontology A ontology to unload.
	 */
	public void unload(OWLOntology ontology);

	/**
	 * Unload the ontology from the KB. The imports of the ontology will
	 * automatically be unloaded if there are no other ontologies importing them.
	 * Note that ontologies loaded explicitly using {@link #load(OWLOntology, boolean)}
	 * will not be unloaded. This method does nothing if the ontology is not
	 * loaded in this KB.
	 *
	 * @param uri The URI of the ontology to unload.
	 */
	public void unload(URI uri);

	/**
	 * @return The reader associated with this KB.
	 */
	public OWLReader getReader();

//	/**
//	 * @param reader The new reader to be associated with this KB.
//	 * @return The previous reader (if any).
//	 */
//	public OWLReader setReader(OWLReader reader);

	/**
	 * Convenience method to read an ontology into this KB from the given
	 * physical <tt>URI</tt>. This method delegates to the corresponding
	 * method of the associated {@link #getReader() reader}.
	 * <p>
	 * In order to load local files you may use <code>kb.read(file.toURI())</code>.
	 *
	 * @see OWLReader#read(OWLKnowledgeBase, URI)
	 */
	public OWLOntology read(URI uri) throws IOException;

	/**
	 * Convenience method to read an ontology into this KB from the given
	 * <tt>Reader</tt>. This method delegates to the corresponding
	 * method of the associated {@link #getReader() reader}.
	 *
	 * @see OWLReader#read(OWLKnowledgeBase, Reader, URI)
	 */
	public OWLOntology read(Reader in, URI baseURI) throws IOException;

	/**
	 * Convenience method to read an ontology into this KB from the given
	 * <tt>InputStream</tt>. This method delegates to the corresponding
	 * method of the associated {@link #getReader() reader}.
	 *
	 * @see OWLReader#read(OWLKnowledgeBase, InputStream, URI)
	 */
	public OWLOntology read(InputStream in, URI baseURI) throws IOException;

	/**
	 * @return Utility class to resolve qualified names in this KB.
	 */
	public QNameProvider getQNames();

	/**
	 * @return <code>true</code> if this KB checks consistency automatically
	 * 	after each ontology is loaded and rejects to load the ontology if
	 * 	it causes inconsistency.
	 */
	public boolean isAutoConsistency();

	/**
	 * Set/clear the behavior to check consistency automatically after each
	 * ontology is loaded. When auto consistency is turned on an ontology that
	 * causes inconsistency will not be loaded. If turned off no consistency
	 * check will be done automatically.
	 *
	 * @return The previous value.
	 */
	public boolean setAutoConsistency(boolean auto);

	public boolean isAutoTranslate();
	public boolean setAutoTranslate(boolean auto);

	/**
	 * @param strict Whether to use strict conversion for OWL objects in this KB,
	 * 	that is, any OWL object contained in any ontology part of this KB.
	 * @return The previous value.
	 */
	public boolean setStrictConversion(final boolean strict);

	/**
	 * @return <code>true</code> if strict conversion is used globally for this
	 * 	KB, that is, for all ontologies contained in this KB.
	 */
	public boolean isStrictConversion();


	/**
	 * @return A special purpose KB that contains the original ontologies in
	 * 	untranslated form that were loaded into this KB. Unless at least one
	 * 	ontology was translated this method returns <code>this</code>.
	 */
	public OWLKnowledgeBase getTranslationSource();

	/**
	 * Convenience method to read an ontology into this KB from the given
	 * physical <tt>URI</tt> and to query for a OWL-S service individual.
	 * More precisely, the method searches the ontology resource for a service
	 * individual whose URI partially matches the given URI. For instance,
	 * if the given URI would be <tt>http://mydomain/foo.owl</tt> and the
	 * ontology contains a service <tt>http://mydomain/foo.owl#MyService</tt>
	 * the method would return this service because both URIs partially match
	 * (except for the fragment). If several instances exist it picks one
	 * arbitrarily.
	 * <p>
	 * This method delegates to the corresponding method of the associated
	 * {@link #getReader() reader}.
	 *
	 * @see OWLReader#read(OWLKnowledgeBase, URI)
	 */
	public Service readService(URI uri) throws IOException;

	/**
	 * Convenience method to read an ontology into this KB from the given
	 * <tt>Reader</tt> and to query for a OWL-S service individual.
	 * More precisely, the method searches the ontology resource for a service
	 * individual whose URI partially matches the given baseURI. For instance,
	 * if the base URI would be <tt>http://mydomain/foo.owl</tt> and the
	 * ontology contains a service <tt>http://mydomain/foo.owl#MyService</tt>
	 * the method would return this service because both URIs partially match
	 * (except for the fragment). If several instances exist it picks one
	 * arbitrarily.
	 * <p>
	 * This method delegates to the corresponding method of the associated
	 * {@link #getReader() reader}.
	 *
	 * @see OWLReader#read(OWLKnowledgeBase, Reader, URI)
	 */
	public Service readService(Reader in, URI baseURI) throws IOException;

	/**
	 * Convenience method to read an ontology into this KB from the given
	 * <tt>InputStream</tt> and to query for an OWL-S service individual.
	 * More precisely, the method searches the ontology resource for a service
	 * individual whose URI partially matches the given baseURI. For instance,
	 * if the base URI would be <tt>http://mydomain/foo.owl</tt> and the
	 * ontology contains a service <tt>http://mydomain/foo.owl#MyService</tt>
	 * the method would return this service because both URIs partially match
	 * (except for the fragment). If several instances exist it picks one
	 * arbitrarily.
	 * <p>
	 * This method delegates to the corresponding method of the associated
	 * {@link #getReader() reader}.
	 *
	 * @see OWLReader#read(OWLKnowledgeBase, Reader, URI)
	 */
	public Service readService(InputStream in, URI baseURI) throws IOException;

	/**
	 * Convenience method to read an ontology into this KB from the given
	 * physical <tt>URI</tt> and to query for OWL-S service individuals.
	 * More precisely, the method searches the ontology resource including
	 * imported ontologies (if any) for OWL-S Service individuals.
	 * <p>
	 * This method delegates to the corresponding method of the associated
	 * {@link #getReader() reader}.
	 *
	 * @see OWLReader#read(OWLKnowledgeBase, URI)
	 */
	public OWLIndividualList<Service> readAllServices(URI uri) throws IOException;

	/**
	 * Convenience method to read an ontology into this KB from the given
	 * <tt>Reader</tt> and to query for OWL-S service individuals.
	 * More precisely, the method searches the ontology resource including
	 * imported ontologies (if any) for OWL-S Service individuals.
	 * <p>
	 * This method delegates to the corresponding method of the associated
	 * {@link #getReader() reader}.
	 *
	 * @see OWLReader#read(OWLKnowledgeBase, Reader, URI)
	 */
	public OWLIndividualList<Service> readAllServices(Reader in, URI baseURI) throws IOException;

	/**
	 * Convenience method to read an ontology into this KB from the given
	 * <tt>InputStream</tt> and to query for OWL-S service individuals.
	 * More precisely, the method searches the ontology resource including
	 * imported ontologies (if any) for OWL-S Service individuals.
	 * <p>
	 * This method delegates to the corresponding method of the associated
	 * {@link #getReader() reader}.
	 *
	 * @see OWLReader#read(OWLKnowledgeBase, InputStream, URI)
	 */
	public OWLIndividualList<Service> readAllServices(InputStream in, URI baseURI) throws IOException;
}
