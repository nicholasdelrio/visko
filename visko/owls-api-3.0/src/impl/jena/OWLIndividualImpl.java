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
package impl.jena;

import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.mindswap.owl.OWLClass;
import org.mindswap.owl.OWLDataProperty;
import org.mindswap.owl.OWLDataValue;
import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owl.OWLObjectProperty;
import org.mindswap.owl.OWLOntology;
import org.mindswap.owl.OWLProperty;
import org.mindswap.owl.OWLValue;
import org.mindswap.owl.OWLWriter;
import org.mindswap.utils.RDFUtils;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelExtract;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StatementBoundaryBase;
import com.hp.hpl.jena.vocabulary.OWL;

/**
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:51 $
 */
public class OWLIndividualImpl extends OWLEntityImpl<Resource> implements OWLIndividual
{
	public OWLIndividualImpl(final OWLOntologyImpl ontology, final Resource resource)
	{
		super(ontology, resource);
	}

	/* @see org.mindswap.owl.OWLIndividual#hasProperty(org.mindswap.owl.OWLProperty) */
	public boolean hasProperty(final OWLProperty prop) {
		return ontology.getKB().contains(this, prop, null);
	}

	/* @see org.mindswap.owl.OWLIndividual#getProperty(org.mindswap.owl.OWLObjectProperty) */
	public OWLIndividual getProperty(final OWLObjectProperty prop) {
		return ontology.getKB().getProperty(this, prop);
	}

	/* @see org.mindswap.owl.OWLIndividual#getProperties(org.mindswap.owl.OWLObjectProperty) */
	public OWLIndividualList<?> getProperties(final OWLObjectProperty prop) {
		return ontology.getKB().getProperties(this, prop);
	}

	/* @see org.mindswap.owl.OWLIndividual#getProperties() */
	public Map<OWLProperty, List<OWLValue>> getProperties() {
		return ontology.getKB().getProperties(this);
	}

	/* @see org.mindswap.owl.OWLIndividual#getProperty(org.mindswap.owl.OWLDataProperty) */
	public OWLDataValue getProperty(final OWLDataProperty prop) {
		return ontology.getKB().getProperty(this, prop);
	}

	/* @see org.mindswap.owl.OWLIndividual#getProperty(org.mindswap.owl.OWLDataProperty, java.lang.String) */
	public OWLDataValue getProperty(final OWLDataProperty prop, final String lang) {
		return ontology.getKB().getProperty(this, prop, lang);
	}

	/* @see org.mindswap.owl.OWLIndividual#getProperties(org.mindswap.owl.OWLDataProperty) */
	public List<OWLDataValue> getProperties(final OWLDataProperty prop) {
		return ontology.getKB().getProperties(this, prop);
	}

	/* @see org.mindswap.owl.OWLIndividual#getIncomingProperty(org.mindswap.owl.OWLObjectProperty) */
	public OWLIndividual getIncomingProperty(final OWLObjectProperty prop) {
		return ontology.getKB().getIncomingProperty(prop, this);
	}

	/* @see org.mindswap.owl.OWLIndividual#getIncomingProperties(org.mindswap.owl.OWLObjectProperty) */
	public OWLIndividualList<?> getIncomingProperties(final OWLObjectProperty prop) {
		return ontology.getKB().getIncomingProperties(prop, this);
	}

	/* @see org.mindswap.owl.OWLIndividual#getIncomingProperties() */
	public OWLIndividualList<?> getIncomingProperties() {
		return ontology.getKB().getIncomingProperties(this);
	}

	/* @see org.mindswap.owl.OWLIndividual#setProperty(org.mindswap.owl.OWLDataProperty, java.lang.String) */
	public void setProperty(final OWLDataProperty prop, final String value) {
		ontology.getKB().removeProperty(this, prop, null);
		ontology.addProperty(this, prop, value);
	}

	/* @see org.mindswap.owl.OWLIndividual#setProperty(org.mindswap.owl.OWLDataProperty, java.lang.Object) */
	public void setProperty(final OWLDataProperty prop, final Object value) {
		ontology.getKB().removeProperty(this, prop, null);
		ontology.addProperty(this, prop, value);
	}

	/* @see org.mindswap.owl.OWLIndividual#setProperty(org.mindswap.owl.OWLDataProperty, org.mindswap.owl.OWLDataValue) */
	public void setProperty(final OWLDataProperty prop, final OWLDataValue value) {
		ontology.getKB().removeProperty(this, prop, null);
		ontology.addProperty(this, prop, value);
	}

	/* @see org.mindswap.owl.OWLIndividual#addProperty(org.mindswap.owl.OWLDataProperty, org.mindswap.owl.OWLDataValue) */
	public void addProperty(final OWLDataProperty prop, final OWLDataValue value) {
		ontology.addProperty(this, prop, value);
	}

	/* @see org.mindswap.owl.OWLIndividual#addProperty(org.mindswap.owl.OWLDataProperty, java.lang.String) */
	public void addProperty(final OWLDataProperty prop, final String value) {
		ontology.addProperty(this, prop, value);
	}

	/* @see org.mindswap.owl.OWLIndividual#addProperty(org.mindswap.owl.OWLDataProperty, java.lang.Object) */
	public void addProperty(final OWLDataProperty prop, final Object value) {
		ontology.addProperty(this, prop, value);
	}

	/* @see org.mindswap.owl.OWLIndividual#removeProperty(org.mindswap.owl.OWLDataProperty, org.mindswap.owl.OWLDataValue) */
	public void removeProperty(final OWLProperty theProp, final OWLValue theValue) {
		ontology.getKB().removeProperty(this, theProp, theValue);
	}

	/* @see org.mindswap.owl.OWLIndividual#addProperty(org.mindswap.owl.OWLObjectProperty, org.mindswap.owl.OWLIndividual) */
	public void addProperty(final OWLObjectProperty prop, final OWLIndividual value) {
		ontology.addProperty(this, prop, value);
	}

	/* @see org.mindswap.owl.OWLIndividual#setProperty(org.mindswap.owl.OWLObjectProperty, org.mindswap.owl.OWLIndividual) */
	public void setProperty(final OWLObjectProperty prop, final OWLIndividual value) {
		ontology.getKB().removeProperty(this, prop, null);
		ontology.addProperty(this, prop, value);
	}

	/* @see org.mindswap.owl.OWLIndividual#getTypes() */
	public Set<OWLClass> getTypes() {
		return ontology.getKB().getTypes(this);
	}

	/* @see org.mindswap.owl.OWLIndividual#hasProperty(org.mindswap.owl.OWLProperty, org.mindswap.owl.OWLValue) */
	public boolean hasProperty(final OWLProperty prop, final OWLValue value) {
		return ontology.getKB().contains(this, prop, value);
	}

	/* @see org.mindswap.owl.OWLIndividual#getType() */
	public OWLClass getType() {
		return ontology.getKB().getType(this);
	}

	/* @see org.mindswap.owl.OWLIndividual#isType(org.mindswap.owl.OWLClass) */
	public boolean isType(final OWLClass c)
	{
		return ontology.getKB().isType(this, c);
	}

	/* @see org.mindswap.owl.OWLValue#isDataValue() */
	public final boolean isDataValue() {
		return false;
	}

	/* @see org.mindswap.owl.OWLValue#isIndividual() */
	public final boolean isIndividual() {
		return true;
	}

	/* @see org.mindswap.owl.OWLIndividual#addType(org.mindswap.owl.OWLClass) */
	public void addType(final OWLClass c) {
		ontology.addType(this, c);
	}

	/* @see org.mindswap.owl.OWLIndividual#removeType(org.mindswap.owl.OWLClass) */
	public void removeTypes() {
		ontology.getKB().removeTypes(this);
	}

	/* @see org.mindswap.owl.OWLIndividual#toRDF(boolean, boolean) */
	public String toRDF(final boolean printRDFTag, final boolean inlineNamespaces)
	{
		// extract the blank node closure rooted at this' resource from the associated KB model
		OntModel closureModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
		new ModelExtract(new BNodeBoundary()).extractInto(closureModel, resource, ontology.getKB().ontModel);

		final OWLOntology closureOnt = OWLFactory.createKB().createOntology(ontology.getURI(), closureModel);

		final StringWriter sw = new StringWriter();
		final OWLWriter writer = closureOnt.getWriter();
		writer.setShowXmlDeclaration(false);

		// no base URI on purpose: resources must use absolute URIs because top level
		// RDF tag may be removed, which includes xml:base
		closureOnt.write(sw, null);

		final String rdf = sw.toString();
		if (printRDFTag) return rdf;
		return RDFUtils.removeRDFTag(rdf, inlineNamespaces);
	}

	/* @see org.mindswap.owl.OWLIndividual#getSourceOntology() */
	public OWLOntology getSourceOntology() {
		return ontology.getTranslationSource();
	}

	/* @see org.mindswap.owl.OWLIndividual#addSameAs(org.mindswap.owl.OWLIndividual) */
	public void addSameAs(final OWLIndividual other)
	{
		ontology.ontModel.add(resource, OWL.sameAs, (Resource) other.getImplementation());
	}

	/* @see org.mindswap.owl.OWLIndividual#isSameAs(org.mindswap.owl.OWLIndividual) */
	public boolean isSameAs(final OWLIndividual other) {
		return ontology.getKB().isSameAs(this, other);
	}

	/* @see org.mindswap.owl.OWLIndividual#removeSameAs(org.mindswap.owl.OWLIndividual) */
	public void removeSameAs(final OWLIndividual other)
	{
		ontology.getKB().removeAll(
			resource, OWL.sameAs, (other == null)? null : (Resource) other.getImplementation());
	}

	/* @see org.mindswap.owl.OWLIndividual#getSameIndividuals() */
	public OWLIndividualList<?> getSameIndividuals() {
		return ontology.getKB().getSameIndividuals(this);
	}

	/* @see org.mindswap.owl.OWLIndividual#addDifferentFrom(org.mindswap.owl.OWLIndividual) */
	public void addDifferentFrom(final OWLIndividual other)
	{
		ontology.ontModel.add(
			resource, OWL.differentFrom, (other == null)? null : (Resource) other.getImplementation());
	}

	/* @see org.mindswap.owl.OWLIndividual#isDifferentFrom(org.mindswap.owl.OWLIndividual) */
	public boolean isDifferentFrom(final OWLIndividual other) {
		return ontology.getKB().isDifferentFrom(this, other);
	}

	/* @see org.mindswap.owl.OWLIndividual#removeDifferentFrom(org.mindswap.owl.OWLIndividual) */
	public void removeDifferentFrom(final OWLIndividual other)
	{
		ontology.getKB().removeAll(resource, OWL.differentFrom, (Resource) other.getImplementation());
	}

	/* @see org.mindswap.owl.OWLIndividual#getDifferentIndividuals() */
	public OWLIndividualList<?> getDifferentIndividuals() {
		return ontology.getKB().getDifferentIndividuals(this);
	}

	/* @see org.mindswap.owl.OWLIndividual#delete() */
	public void delete() {
		ontology.getKB().remove(this, false);
	}

	/**
	 * Stops if it finds a named Resource (i.e. one that has a URI) or a Literal
	 * as the object of a statement, i.e., blank nodes in the object pass through.
	 */
	static final class BNodeBoundary extends StatementBoundaryBase
	{
		/* @see com.hp.hpl.jena.rdf.model.StatementBoundaryBase#stopAt(com.hp.hpl.jena.rdf.model.Statement) */
		@Override
		public boolean stopAt(final Statement s)
		{
			RDFNode stmtObject = s.getObject();
			return (stmtObject.isURIResource() || stmtObject.isLiteral())? true : false;
		}
	}

}
