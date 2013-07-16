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

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The interface for OWL individuals.
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:52 $
 */
public interface OWLIndividual extends OWLEntity, OWLValue {
	/**
	 * Return true if a value for the given property exists.
	 *
	 * @param prop
	 * @return
	 */
	public boolean hasProperty(OWLProperty prop);

	/**
	 * Return true if the given value for the property exists.
	 *
	 * @param prop
	 * @param value
	 * @return
	 */
	public boolean hasProperty(OWLProperty prop, OWLValue value);

	/**
	 * Get the value for the given object property. If the resource has more
	 * than one value for this property a random one will be returned.
	 *
	 * @param prop
	 * @return
	 */
	public OWLIndividual getProperty(OWLObjectProperty prop);


	/**
	 * Get all the values for the given object property.
	 *
	 * @param prop
	 * @return
	 */
	public OWLIndividualList<?> getProperties(OWLObjectProperty prop);

	/**
	 * Get the value for the given data type property. If the resource has more
	 * than one value for this property with different language identifiers than
	 * the returned value will be determined according to the settings defined
	 * in {@link org.mindswap.owl.OWLConfig#DEFAULT_LANGS OWLConfig}
	 *
	 * @param prop
	 * @return
	 */
	public OWLDataValue getProperty(OWLDataProperty prop);

	/**
	 * Get the value for the given property URI with the specified language
	 * identifier. If the value for the given language does not exist return
	 * <code>null</code> even if a value is found for another language.
	 * Use {@link #getProperty(OWLDataProperty)} to be more flexible.
	 *
	 * @param prop
	 * @param lang
	 * @return
	 */
	public OWLDataValue getProperty(OWLDataProperty prop, String lang);

	/**
	 * Get all the values for the given data property.
	 *
	 * @param prop
	 * @return
	 */
	public List<OWLDataValue> getProperties(OWLDataProperty prop);

	/**
	 * Get all the properties asserted about this individual.
	 */
	public Map<OWLProperty, List<OWLValue>> getProperties();

	/**
	 * @see OWLModel#getIncomingProperty(OWLObjectProperty, OWLIndividual)
	 */
	public OWLIndividual getIncomingProperty(OWLObjectProperty prop);

	/**
	 * @see OWLModel#getIncomingProperties(OWLObjectProperty, OWLIndividual)
	 */
	public OWLIndividualList<?> getIncomingProperties(OWLObjectProperty prop);

	/**
	 * @see OWLModel#getIncomingProperties(OWLIndividual)
	 */
	public OWLIndividualList<?> getIncomingProperties();

	/**
	 * Set the value for the given data property to the given plain literal
	 * value (no language identifier). All the existing data values (that has
	 * no language identifier) will be removed.
	 *
	 * @param prop
	 * @param value
	 */
	public void setProperty(OWLDataProperty prop, String value);

	/**
	 * Set the value for the given data property to the given literal by
	 * determining the RDF data type from Java class. This function is
	 * equivalent to <code>setProperty(prop, OWLFactory.createDataValue(value))</code>.
	 *
	 * @param prop
	 * @param value
	 */
	public void setProperty(OWLDataProperty prop, Object value);

	/**
	 * Set the value for the given data property. All the existing data values
	 * (that has the same language identifier with the given value) will be removed.
	 *
	 * @param prop
	 * @param value
	 */
	public void setProperty(OWLDataProperty prop, OWLDataValue value);

	public void addProperty(OWLDataProperty prop, OWLDataValue value);

	public void addProperty(OWLDataProperty prop, String value);

	public void addProperty(OWLDataProperty prop, Object value);

	/**
	 * Remove all statements matching (this, p, o) from the backing {@link #getKB()}.
	 *
	 * @param p The target property (predicate).
	 * @param o The value to remove (object). A value of <code>null</code> will
	 * 	remove all statements (this, p, *).
	 */
	public void removeProperty(OWLProperty p, OWLValue o);

	public void addProperty(OWLObjectProperty prop, OWLIndividual value);

	public void setProperty(OWLObjectProperty prop, OWLIndividual value);

	public void addType(OWLClass c);

	public void removeTypes();

	/**
	 * Entirely deletes this individual in the ontology it is part of. More
	 * precisely, it deletes all statements that refer to it, that is, where
	 * it occurs as either statement-subject or statement-object.
	 * <p>
	 * No matter if this individual is anonymous or not, the backing resource
	 * still exists - actually, just its URI respectively anon identifier, but
	 * no statements. As such, it can be still used, for instance, to add or
	 * set properties, which will add this individual back to the ontology
	 * referred to by {@link OWLEntity#getOntology()}.
	 */
	public void delete();

	public OWLClass getType();

	public Set<OWLClass> getTypes();

	public boolean isType(OWLClass c);

	/**
	 * Return the RDF/XML representation of this individual. The returned RDF/XML is
	 * supposed to be a nested RDF statement which is the b-node closure of the
	 * individual.
	 *
	 * @param printRDFtag If <code>false</code> the enclosing <tt>&lt;rdf:RDF&gt;</tt>
	 * 	tag will be omitted.
	 * @param inlineNamespaces If <code>true</code> namespaces declared within
	 * 	the <tt>&lt;rdf:RDF&gt;</tt> tag wont get lost but occur in-line in
	 * 	the result (good to quote whole expression). This parameter is only
	 * 	relevant if <code>withRDFtag</code> is <code>false</code>.
	 * @return The RDF/XML representation of this individual.
	 */
	public String toRDF(boolean printRDFtag, boolean inlineNamespaces);

	/**
	 * @return The original OWL ontology this individual comes from. If the
	 * 	OWL-S ontology this individual comes from is the latest version then
	 * 	this method will return the same object as {@link #getOntology()}.
	 * 	If the original ontology was from an older version of OWL-S and
	 * 	translated to the latest version then this function will return a
	 * 	reference to the original ontology. This way the information that
	 * 	might have been lost during translation, e.g. non-OWL-S descriptions
	 * 	in the original file, can still be accessed.
	 */
	public OWLOntology getSourceOntology();

	/**
	 * Add a <tt>owl:sameAs</tt> assertion to this individual in the attached
	 * ontology, thus, assert that the two individuals are the same. Existing
	 * <tt>owl:sameAs</tt> assertion about this individual remain.
	 *
	 * @param other Some individual which shall be the same as this individual.
	 * @see #removeSameAs(OWLIndividual)
	 */
	public void addSameAs(OWLIndividual other);

	/**
	 * @param other Some individual.
	 * @return <code>true</code> if the backing KB entails this individual
	 * 	to be the same as the given individual (according to the semantics of
	 * 	<tt>owl:differentFrom</tt> respectively <tt>owl:sameAs</tt>).
	 */
	public boolean isSameAs(OWLIndividual other);

	/**
	 * Remove a <tt>owl:sameAs</tt> assertion between this and the given
	 * individual in the backing KB. If the parameter is <code>null</code> all
	 * existing <tt>owl:sameAs</tt> assertion about this individual
	 * will be removed in the backing KB.
	 *
	 * @param other Some individual or <code>null</code> for the wildcard.
	 * @see #addSameAs(OWLIndividual)
	 */
	public void removeSameAs(OWLIndividual other);

	/**
	 * @return All individuals which the backing KB entails to be the same
	 * 	(according to the semantics of <tt>owl:sameAs</tt>).
	 */
	public OWLIndividualList<?> getSameIndividuals();

	/**
	 * Add a <tt>owl:differentFrom</tt> assertion to this individual in the
	 * attached ontology, thus, assert that the two individuals are distinct.
	 * Existing <tt>owl:differentFrom</tt> assertion about this individual
	 * remain.
	 *
	 * @param other Some individual which shall be different from this individual.
	 * @see #removeDifferentFrom(OWLIndividual)
	 */
	public void addDifferentFrom(OWLIndividual other);

	/**
	 * @param other Some individual.
	 * @return <code>true</code> if the backing model entails this individual
	 * 	to be different from the given individual (according to the semantics
	 * 	<tt>owl:differentFrom</tt> respectively <tt>owl:sameAs</tt>).
	 */
	public boolean isDifferentFrom(OWLIndividual other);

	/**
	 * Remove a <tt>owl:differentFrom</tt> assertion between this and the given
	 * individual in the backing KB. If the parameter is <code>null</code> all
	 * existing <tt>owl:differentFrom</tt> assertion about this individual
	 * will be removed in the backing KB.
	 *
	 * @param other Some individual or <code>null</code> for the wildcard.
	 * @see #addDifferentFrom(OWLIndividual)
	 */
	public void removeDifferentFrom(OWLIndividual other);

	/**
	 * @return All individuals which the backing model entails to be different
	 * 	(according to the semantics of <tt>owl:differentFrom</tt>).
	 */
	public OWLIndividualList<?> getDifferentIndividuals();

}
