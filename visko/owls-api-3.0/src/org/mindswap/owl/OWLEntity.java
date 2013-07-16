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
import java.util.List;

/**
 * Generic interface for OWL {@link OWLClass classes}, {@link OWLProperty properties},
 * and {@link OWLIndividual individuals}.
 *
 * @author unascribed
 * @version $Rev: 2337 $; $Author: nick $; $Date: 2011/01/24 06:37:52 $
 */
public interface OWLEntity extends OWLObject
{
	/**
	 * @return The ontology attached to this OWL entity. Every OWL entity
	 * 	instance has a ontology attached. Usually, this ontology is the
	 * 	model in which it was created, i.e., the model that was subject to
	 * 	be queried when this instance was created.
	 * 	Note that "instance" here refers to the Java object instance. As
	 * 	such, it should not be mixed up with the term individual in the
	 * 	OWL terminology (or resource in RDF) because multiple ontologies
	 * 	may "talk about" the same OWL individual (resource), hence, it may
	 * 	be part of multiple ontologies.
	 */
	public OWLOntology getOntology();

	/**
	 * @return The knowledge base model that contains the
	 * 	{@link #getOntology() ontology model} of this OWL entity. Every
	 * 	ontology model is part of a knowledge base model.
	 */
	public OWLKnowledgeBase getKB();

	/**
	 * @return <code>true</code> if this entity is anonymous (a blank node in
	 * 	RDF terminology).
	 */
	public boolean isAnon();

	/**
	 * @return The URI of this resource. Returns <code>null</code> if this
	 * 	entity {@link #isAnon() is anonymous}.
	 */
	public URI getURI();

	/**
	 * @return The local name part within its {@link #getURI() full name},
	 * 	which is basically the fragment identifier of the URI. Returns
	 * 	<code>null</code> if this entity {@link #isAnon() is anonymous}.
	 */
	public String getLocalName();

	/**
	 * @return
	 */
	public String getQName();

	/**
	 * @return The namespace part within its {@link #getURI() full name}.
	 */
	public String getNamespace();

	/**
	 * @return A label string if this is an anonymous (blank) entity. Usually,
	 * 	labels are unique for the lifetime of the backing {@link #getKB() KB}.
	 * 	Details may vary depending on which RDF/OWL framework is used.
	 */
	public String getAnonID();

	/**
	 * Get the <tt>rdfs:label</tt> for this resource. This function will look
	 * for labels with different language codes according to the parameter. If
	 * there is more than one such resource, an arbitrary selection is made.
	 *
	 * @param lang The language attribute for the desired label (EN, FR, etc) or
	 * 	<code>null</code> for don't care. Will attempt to retrieve the most
	 * 	specific label matching the given language.
	 * @return A label string matching the given language, or <code>null</code>
	 * 	if there is no matching label, i.e., if the label for the given
	 * 	language does not exist return <code>null</code> even if a label is
	 * 	found for another language.
	 */
	public String getLabel(String lang);

	/**
	 * Return all labels written in any language.
	 *
	 * @return List of label strings or an empty list if non exist.
	 */
	public List<String> getLabels();

	/**
	 * Set the value of <tt>rdfs:label</tt> for this resource. Any existing
	 * statements for <tt>rdfs:label</tt> will be removed.
	 *
	 * @param label The label for this entity.
	 * @param lang The language attribute for this label (EN, FR, etc) or
	 * <code>null</code> if not specified.
	 */
	public void setLabel(String label, String lang);

	/**
	 * @param prop
	 * @return
	 */
	public OWLIndividual getPropertyAsIndividual(URI prop);

	/**
	 * @param prop
	 * @param lang
	 * @return
	 */
	public OWLDataValue getPropertyAsDataValue(URI prop, String lang);

	/**
	 * @param prop
	 * @return
	 */
	public OWLIndividualList<?> getPropertiesAsIndividual(URI prop);

	/**
	 * @param prop
	 * @return
	 */
	public List<OWLDataValue> getPropertiesAsDataValue(URI prop);

	/**
	 * @param p
	 * @param o
	 */
	public void addProperty(URI p, OWLDataValue o);

	/**
	 * @param p
	 * @param o
	 */
	public void addProperty(URI p, OWLEntity o);

	/**
	 * @param p
	 * @param o
	 * @param lang
	 */
	public void addProperty(URI p, String o, String lang);

	/**
	 * @param p
	 * @param o
	 */
	public void setProperty(URI p, OWLDataValue o);

	/**
	 * @param p
	 * @param o
	 */
	public void setProperty(URI p, OWLEntity o);

	/**
	 * @param p
	 * @param o
	 * @param lang
	 */
	public void setProperty(URI p, String o, String lang);

	/**
	 * @param prop
	 */
	public void removeProperties(URI prop);

	/**
	 * @return A (more or less) user-friendly string representation. Tries
	 * 	{@link #getLabel(String)} first and falls back to {@link #toString()} if
	 * 	there is no label.
	 */
	public String toPrettyString();
}
