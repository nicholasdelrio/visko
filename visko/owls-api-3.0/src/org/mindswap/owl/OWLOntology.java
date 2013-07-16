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

import java.net.URI;
import java.util.Collection;
import java.util.Set;

import org.mindswap.owls.OWLSVersionTranslator;

/**
 * OWL ontologies may contain a set of OWL class, property, and individual
 * axioms, and assertions about individuals. As such, it may not only
 * represent a TBox but can also contain statements that fall into what is
 * commonly defined as the ABox.
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:52 $
 */
public interface OWLOntology extends OWLModel
{
	/**
	 * This prefix is used to create synthetic URIs for ontologies that were
	 * <ul>
	 * 	<li>not read from a (remote) file,</li>
	 * 	<li>read from a input stream or reader whereby no base URI was
	 * 	provided at read time,</li>
	 * 	<li>created ad hoc or derived from other ontologies.
	 * </ul>
	 * I other words, the {@link #getURI() URI} for synthetic named ontologies
	 * starts with this prefix.
	 */
	public static final String SYNTHETIC_ONT_PREFIX = "urn:owl-s-api:";

	/**
	 * Physical or logical URI of this ontology depending on how it was loaded:
	 * <ol>
	 * 	<li>URL if it was loaded from a file respectively a Web resource, or
	 * 	if it was loaded as an import of another ontology.</li>
	 * 	<li>Base URI if it was loaded from an input stream or a reader. In
	 * 	this case it may also be <code>null</code> if no URI was provided at
	 * 	load time.
	 * </ol>
	 *
	 * @return The URI that represents the name of this ontology. May be
	 * 	<code>null</code> as described above.
	 */
	public URI getURI();

//	/**
//	 * @param uri The URI that represents the name of this ontology. May be
//	 * 	<code>null</code> for a kind of anonymous ontology. Use this method
//	 * 	with caution and think twice whether you really want to set the value
//	 * 	to <code>null</code>!
//	 */
//	public void setURI(URI uri);

	/**
	 * Check if this ontology is marked to not allow modifications. This
	 * property is transitive: imports of a read-only ontology (if any)
	 * are also read-only (and vice versa) provided that they were retrieved
	 * using {@link #getImports(boolean)}. However, this property is <em>
	 * local</em> regarding the backing {@link #getKB() KB}: While it is
	 * reflected for this ontology in the KB, it is <strong>not</strong> for
	 * imports of this ontology. This is due to the fact that a imported
	 * ontology may be imported by another ontology as well and we decided
	 * not to <em>globalize</em> this property in this case.
	 * <p>
	 * Any attempt to modify a read-only ontology via one of the addXXX,
	 * createXXX, removeXXX, setXXX methods will result in a
	 * {@link UnsupportedOperationException}.
	 * <p>
	 * Note, however, that when a reasoner gets attached it can, of course,
	 * internally add deduced statements.
	 *
	 * @return <code>true</code> if this ontology cannot be modified.
	 * @see #setReadOnly(boolean)
	 */
	public boolean isReadOnly();

	/**
	 * Allow or disallow modifications to this ontology. Setting this flag is
	 * transitive, i.e., spans over this ontology and its imports (if any).
	 *
	 * @param readOnly If <code>true</code> modifications cannot be made.
	 * @see #isReadOnly()
	 */
	public void setReadOnly(boolean readOnly);

	/**
	 * @return Reference to the backing knowledge base. Every ontology instance
	 * 	belongs to exactly one knowledge base.
	 */
	public OWLKnowledgeBase getKB();

	/**
	 * Get the set of ontologies imported into this ontology.
	 *
	 * @param closure Whether to return only those ontologies that are directly
	 * 	imported (<code>false</code>), or whether to return the transitive
	 * 	imports closure (<code>true</code>).
	 * @return The set of imported ontologies.
	 */
	public Set<OWLOntology> getImports(boolean closure);

	/**
	 * Import the given ontology to this ontology, that is, add all statements
	 * (as a sub model) to this model. Note that it wont be imported if (i) it
	 * is already imported or (ii) if one of the already imported ontologies
	 * (if any) imports the given ontology.
	 * <p>
	 * This will cause the associated inference engine (if any) to update, so
	 * this may be an expensive operation in some cases.
	 *
	 * @param ontology The ontology to import.
	 */
	public void addImport(OWLOntology ontology);

	/**
	 * Import the given ontologies to this ontology, that is, add all statements
	 * (as a sub model) of each ontology to this model. Note that an ontology
	 * <tt>o</tt> in the given collection wont be imported if (i) it is already
	 * imported or (ii) if one of the already imported ontologies (if any)
	 * imports <tt>o</tt>. Therefore, the order of the given collection may
	 * have an impact on which ontologies will actually be added as imports.
	 * <p>
	 * This will cause the associated inference engine (if any) to update, so
	 * this may be an expensive operation in some cases.
	 *
	 * @param theImports The ontologies to import.
	 */
	public void addImports(final Collection<OWLOntology> theImports);

	/**
	 * Enforce that this model no longer imports the given ontology.
	 * <p>
	 * This will cause the associated inference engine (if any) to update, so
	 * this may be an expensive operation in some cases.
	 *
	 * @param ontology The ontology to remove as import.
	 */
	public void removeImport(OWLOntology ontology);

	/**
	 * Enforce that this model no longer imports the given ontologies.
	 * <p>
	 * This will cause the associated inference engine (if any) to update, so
	 * this may be an expensive operation in some cases.
	 *
	 * @param theImports The ontologies to remove as imports.
	 */
	public void removeImports(final Set<OWLOntology> theImports);

	/**
	 * If this OWL-S ontology was translated from an older version of OWL-S
	 * (using {@link OWLSVersionTranslator}) then this method will return a
	 * reference to the original ontology. This way, the information that
	 * might have been lost during translation, e.g. non-OWL-S descriptions
	 * in the original file, can still be accessed. If the ontology
	 * originally belongs to the latest version then this function will
	 * return a reference to itself.
	 *
	 * @return The source ontology, or <code>this</code> if this ontology
	 * 	was not translated from an earlier OWLS-S version.
	 */
	public OWLOntology getTranslationSource();

	/**
	 * @deprecated This method is for internal use only and should never be used!
	 */
	@Deprecated
	public void setTranslationSource(OWLOntology ontology); // REFACTOR remove this method

	/**
	 * Create a new, independent, ontology containing all the statements in
	 * this ontology which are not in another. Neither of the ontologies is changed.
	 * <p>
	 * Note that the ontology returned does not belong to the KB of this ontology
	 * nor to the KB of the other, i.e., it belongs to a newly created one.
	 *
	 * @param ont The other ontology whose statements are to be excluded.
	 * @return A new ontology containing all the statements in this ontology
	 * 	that are not in the given ontology.
	 */
	public OWLOntology difference(OWLOntology ont);

	/**
	 * Create a new, independent, ontology containing all the statements which
	 * are in both this ontology and another. As ontologies are sets of statements,
	 * a statement contained in both ontologies will only appear once in the
	 * resulting model. Neither of the ontologies is changed.
	 * <p>
	 * Note that the ontology returned does not belong to the KB of this ontology
	 * nor to the KB of the other, i.e., it belongs to a newly created one.
	 *
	 * @param ont The other ontology.
	 * @return A new ontology containing all the statements that are in both ontologies.
	 */
	public OWLOntology intersection(OWLOntology ont);

	/**
	 * Merge the contents of this ontology with <code>ont</code> and return the
	 * merged ontology. Neither of the ontologies is changed.
	 * <p>
	 * Note that the ontology returned does not belong to the KB of this ontology
	 * nor to the KB of the other, i.e., it belongs to a newly created one.
	 *
	 * @param ont The other ontology to merge.
	 * @return The union of this and the other ontology.
	 */
	public OWLOntology union(OWLOntology ont);

	/**
	 * Add all the statements of <code>ont</code> to this ontology. Note that
	 * this is not equivalent to importing another ontology, see
	 * {@link #addImport(OWLOntology)}.
	 *
	 * @param ont The ontology to add.
	 */
	public void add(OWLOntology ont);

	/**
	 * Remove all the statements from this ontology. This operation will also be
	 * reflected in the associated {@link OWLOntology#getKB() knowledge base}.
	 *
	 * @param includingImports Whether to remove all statements from all
	 * 	imported ontologies as well.
	 */
	public void removeAll(boolean includingImports);

}
