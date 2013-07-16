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
package org.mindswap.owl;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URI;
import java.util.Set;

/**
 * OWL reader realize reading respectively parsing OWL ontologies (into
 * in-memory models).
 * <p>
 * If Jena is used as the backing RDF library then repeated reads of the same
 * ontology (including imported ontologies) are subject to get cached in memory
 * by default. This has an effect for situations when reading some ontology
 * fails (for some I/O related or syntactical reason): Only the first attempt
 * will end by throwing an {@link IOException} then. Subsequent attempts to
 * read the same ontology (even if it is an imported one) will end without an
 * exception and its model will be empty. However, it is possible to clear the
 * internal (but global) cache by invoking {@link #clear()}. After doing so,
 * the next attempt to read the same ontology will try to read the resource
 * from its original location, thus, throwing an exception in case of failures.
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:52 $
 */
public interface OWLReader
{

	/**
	 * Reads a ontology from the given URL (<tt>file:</tt> and <tt>http:</tt>)
	 * into a model. If the ontology imports other ontologies they will be read
	 * into the model as well, provided that they can be located and accessed
	 * given their import URI.
	 *
	 * @param kb The knowledge base into which the ontology will be loaded.
	 * 	May not be <code>null</code>.
	 * @param url The URL of the document to read (provided as a URI).
	 * @return The Ontology model.
	 * @throws IOException In case reading of the referenced ontology failed
	 * 	or was interrupted for some I/O related reason. If
	 * 	{@link #isIgnoreFailedImport()} returns <code>false</code>, this
	 * 	exception may also be thrown if reading of some imported ontology
	 * 	(transitively) failed. However, there is an Jena specific detail
	 * 	caused by the its internal caching mechanism, see class JavaDoc.
	 */
	public OWLOntology read(OWLKnowledgeBase kb, URI url) throws IOException;

	/**
	 * Reads the ontology from the given Reader into a model. If the ontology
	 * imports other ontologies they will be read into the model as well,
	 * provided that they can be located and accessed given their import URI.
	 *
	 * @param kb The knowledge base into which the ontology will be loaded.
	 * 	May not be <code>null</code>.
	 * @param in The reader representing the ontology to read.
	 * @param baseURI Logical base URI to resolve relative URIs. This URI will
	 * 	also be assigned to the ontology. If <code>null</code>, a synthetic
	 * 	URI will be created and assigned.
	 * @return The Ontology model.
	 * @throws IOException In case reading of the referenced ontology failed
	 * 	or was interrupted for some I/O related reason. If
	 * 	{@link #isIgnoreFailedImport()} returns <code>false</code>, this
	 * 	exception may also be thrown if reading of some imported ontology
	 * 	(transitively) failed. However, there is an Jena specific detail
	 * 	caused by the its internal caching mechanism, see class JavaDoc.
	 */
	public OWLOntology read(OWLKnowledgeBase kb, Reader in, URI baseURI) throws IOException;

	/**
	 * Reads the ontology from the given InputStream into a model. If the
	 * ontology imports other ontologies they will be read into the model as
	 * well, provided that they can be located and accessed given their import
	 * URI.
	 *
	 * @param kb The knowledge base into which the ontology will be loaded.
	 * 	May not be <code>null</code>.
	 * @param in The input stream representing the ontology to read.
	 * @param baseURI Logical base URI to to resolve relative URIs. This URI will
	 * 	also be assigned to the ontology. If <code>null</code>, a synthetic
	 * 	URI will be created and assigned.
	 * @return The Ontology model.
	 * @throws IOException In case reading of the referenced ontology failed
	 * 	or was interrupted for some I/O related reason. If
	 * 	{@link #isIgnoreFailedImport()} returns <code>false</code>, this
	 * 	exception may also be thrown if reading of some imported ontology
	 * 	(transitively) failed. However, there is an Jena specific detail
	 * 	caused by the its internal caching mechanism, see class JavaDoc.
	 */
	public OWLOntology read(OWLKnowledgeBase kb, InputStream in, URI baseURI) throws IOException;

	/**
	 * Attention! Even though this is an instance method, the set of ignored
	 * ontologies is global, as long as Jena is used as the underlying API!
	 * As a consequence, make sure to invoke this method with caution in
	 * multithreaded environments in order not to cause concurrent modification
	 * inferences respectively exceptions.
	 *
	 * @return The set of OWL ontologies we ignore when importing.
	 */
	public Set<URI> getIgnoredOntologies();

	/**
	 * Add the given URI to the set of URI's we ignore in import statements.
	 * <p>
	 * Attention! Even though this is an instance method, invoking it has an
	 * global effect, as long as Jena is used as the underlying API! As a
	 * consequence, make sure to invoke this method with caution in
	 * multithreaded environments in order not to cause concurrent modification
	 * inferences respectively exceptions.
	 *
	 * @param uri The URI of an OWL ontology to ignore when importing.
	 */
	public void addIgnoredOntology(URI uri);

	/**
	 * Set an error handler for this reader.
	 *
	 * @param newErrHandler The new error handler.
	 * @return The previous error handler (if any).
	 */
	public OWLErrorHandler setErrorHandler(OWLErrorHandler newErrHandler);

	/**
	 * Clear the internal cache, that is, dispose all references that are kept
	 * in memory from previously read models.
	 * <p>
	 * Attention! Even though this is an instance method, invoking it has a
	 * global effect as long as Jena is used as the underlying API! Use this
	 * method with caution. Incautious invocation may result in undesired
	 * inferences or unexpected behavior, e.g., when multiple reader instances
	 * exist or in multithreaded environments.
	 */
	public void clear();

	/**
	 * Set the way this reader handles the event if some imported ontology can
	 * not be found when reading the ontology that specifies the import.
	 * (Ontologies to import are specified in OWL using the
	 * <code>owl:imports</code> property).
	 *
	 * @param ignore If <code>true</code> the reader will ignore the event when
	 * 	some imported ontology could not be found (for whatever reason). The
	 * 	corresponding read method will then not throw an {@link IOException}
	 * 	for failed attempts to read an imported ontology.
	 */
	public void setIgnoreFailedImport(boolean ignore);

	/**
	 * The way this reader handles the event if some imported ontology can not be
	 * found when reading the ontology that specifies the import. (Ontologies to
	 * import are specified in OWL using the <code>owl:imports</code> property).
	 * <p>
	 * The default value of implementations should be <code>false</code>.
	 *
	 * @return If <code>true</code> the reader will ignore the event when some
	 * 	imported ontology could not be found (for whatever reason). The
	 * 	corresponding read method will then not throw an {@link IOException}
	 * 	for failed attempts to read an imported ontology.
	 */
	public boolean isIgnoreFailedImport();

}
