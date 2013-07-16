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
 * Created on Dec 28, 2004
 */
package impl.owl;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.mindswap.owl.OWLClass;
import org.mindswap.owl.OWLDataProperty;
import org.mindswap.owl.OWLDataValue;
import org.mindswap.owl.OWLEntity;
import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owl.OWLObjectProperty;
import org.mindswap.owl.OWLOntology;
import org.mindswap.owl.OWLProperty;
import org.mindswap.owl.OWLValue;

/**
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:54 $
 */
public abstract class WrappedIndividual extends OWLObjectImpl implements OWLIndividual
{
	protected final OWLIndividual individual;

	public WrappedIndividual(final OWLIndividual ind) {
		individual = ind;

		setNextView(ind.getNextView());
		ind.setNextView(this);
	}

	/* @see org.mindswap.owl.OWLObject#getImplementation() */
	public Object getImplementation() {
		return individual.getImplementation();
	}

	/* @see org.mindswap.owl.OWLIndividual#hasProperty(org.mindswap.owl.OWLProperty) */
	public boolean hasProperty(final OWLProperty prop) {
		return individual.hasProperty(prop);
	}

	/* @see org.mindswap.owl.OWLIndividual#hasProperty(org.mindswap.owl.OWLProperty, org.mindswap.owl.OWLValue) */
	public boolean hasProperty(final OWLProperty prop, final OWLValue value) {
		return individual.hasProperty(prop, value);
	}

	/* @see org.mindswap.owl.OWLIndividual#getProperties() */
	public Map<OWLProperty, List<OWLValue>> getProperties() {
		return individual.getProperties();
	}

	/* @see org.mindswap.owl.OWLIndividual#getProperty(org.mindswap.owl.OWLObjectProperty) */
	public OWLIndividual getProperty(final OWLObjectProperty prop) {
		return individual.getProperty(prop);
	}

	/**
	 * Assuming that the range of values of the given data property can be parsed
	 * to URIs (e.g. typed to <tt>xsd:anyURI</tt>),
	 * this method gets all values of the property in the backing KB and
	 * tries to parse them into a URI. If a value can not be parsed it will be
	 * silently ignored.
	 *
	 * @param prop The data property whose range is expected to be <tt>xsd:anyURI</tt>.
	 * @return The list of all URIs.
	 */
	protected List<URI> getPropertiesAsURI(final OWLDataProperty prop)
	{
		List<OWLDataValue> tmp = individual.getProperties(prop);
		List<URI> uris = new ArrayList<URI>(tmp.size());
		for (OWLDataValue dataValue : tmp)
		{
			final Object value = dataValue.getValue();
			if (value instanceof URI) uris.add((URI) value);
			try
			{
				uris.add(new URI(value.toString()));
			}
			catch (URISyntaxException ignore)
			{
				// we can not recover anyway; we could log something, which would be additional work
			}
		}
		return uris;
	}

	/**
	 * @param <T>
	 * @param prop
	 * @param result
	 * @return
	 */
	protected <T extends OWLIndividual> T getIncomingPropertyAs(final OWLObjectProperty prop, final Class<T> result)
	{
		final OWLIndividual value = individual.getIncomingProperty(prop);
		return (value == null) ? null : value.castTo(result);
	}

	/**
	 * @param <T>
	 * @param prop
	 * @param result
	 * @return
	 */
	protected <T extends OWLIndividual> OWLIndividualList<T> getIncomingPropertiesAs(
		final OWLObjectProperty prop, final Class<T> result)
	{
		return OWLFactory.castList(individual.getIncomingProperties(prop), result);
	}

	/**
	 * @param <T>
	 * @param prop
	 * @param result
	 * @return
	 */
	protected <T extends OWLIndividual> OWLIndividualList<T> getPropertiesAs(final OWLObjectProperty prop,
		final Class<T> result)
	{
		return OWLFactory.castList(individual.getProperties(prop), result);
	}

	/**
	 * @param <T>
	 * @param prop
	 * @param result
	 * @return
	 */
	protected <T extends OWLEntity> T getPropertyAs(final OWLObjectProperty prop, final Class<T> result) {
		final OWLIndividual value = individual.getProperty(prop);
		return (value == null) ? null : value.castTo(result);
	}

	/**
	 * @param <T>
	 * @param prop
	 * @param result
	 * @return
	 */
	protected <T extends OWLDataValue> T getPropertyAs(final OWLDataProperty prop, final Class<T> result) {
		final OWLDataValue value = individual.getProperty(prop);
		return (value == null) ? null : value.castTo(result);
	}

	/**
	 * @param prop
	 * @return
	 */
	protected String getPropertyAsString(final OWLDataProperty prop) {
		final OWLDataValue value = individual.getProperty(prop);
		return (value == null) ? null : value.toString();
	}

	/**
	 * @param prop
	 * @param lang
	 * @return
	 */
	protected String getPropertyAsString(final OWLDataProperty prop, final String lang) {
		final OWLDataValue value = individual.getProperty(prop, lang);
		return (value == null) ? null : value.toString();
	}

	/**
	 * @param prop
	 * @return
	 */
	protected URI getPropertyAsURI(final OWLDataProperty prop)
	{
		final OWLDataValue dv = individual.getProperty(prop);
		if (dv != null)
		{
			final Object value = dv.getValue();
			if (value instanceof URI) return (URI) value;
			try
			{
				return new URI(value.toString().trim());
			}
			catch (NullPointerException e) { /* fall through, we can not recover anyway */ }
			catch (URISyntaxException e) { /* fall through, we can not recover anyway */ }
		}
		return null;
	}

	/**
	 * This method can be used if it is known that the value of the given
	 * property syntactically represents a URL.
	 *
	 * @return The value of the property converted to an URL. Returns <code>null</code>
	 * 	if there is no statement containing this individual and the property, or
	 * 	the value is a malformed URL.
	 */
	protected URL getPropertyAsURL(final OWLDataProperty prop)
	{
		final OWLDataValue dv = individual.getProperty(prop);
		if (dv != null)
		{
			final Object value = dv.getValue();
			try
			{
				if (value instanceof URI) return ((URI) value).toURL();
				return new URL(value.toString().trim());
			}
			catch (NullPointerException e) { /* fall through, we can not recover anyway */ }
			catch (IllegalArgumentException e) { /* fall through, we can not recover anyway */ }
			catch (MalformedURLException ignore) { /* fall through, we can not recover anyway */ }
		}
		return null;
	}

	/* @see org.mindswap.owl.OWLIndividual#getProperties(org.mindswap.owl.OWLObjectProperty) */
	public OWLIndividualList<?> getProperties(final OWLObjectProperty prop) {
		return individual.getProperties(prop);
	}

	/* @see org.mindswap.owl.OWLIndividual#getProperty(org.mindswap.owl.OWLDataProperty) */
	public OWLDataValue getProperty(final OWLDataProperty prop) {
		return individual.getProperty(prop);
	}

	/* @see org.mindswap.owl.OWLIndividual#getProperty(org.mindswap.owl.OWLDataProperty, java.lang.String) */
	public OWLDataValue getProperty(final OWLDataProperty prop, final String lang) {
		return individual.getProperty(prop, lang);
	}

	/* @see org.mindswap.owl.OWLIndividual#getProperties(org.mindswap.owl.OWLDataProperty) */
	public List<OWLDataValue> getProperties(final OWLDataProperty prop) {
		return individual.getProperties(prop);
	}

	/* @see org.mindswap.owl.OWLIndividual#getIncomingProperties() */
	public OWLIndividualList<?> getIncomingProperties() {
		return individual.getIncomingProperties();
	}

	/* @see org.mindswap.owl.OWLIndividual#getIncomingProperty(org.mindswap.owl.OWLObjectProperty) */
	public OWLIndividual getIncomingProperty(final OWLObjectProperty prop) {
		return individual.getIncomingProperty(prop);
	}

	/* @see org.mindswap.owl.OWLIndividual#getIncomingProperties(org.mindswap.owl.OWLObjectProperty) */
	public OWLIndividualList<?> getIncomingProperties(final OWLObjectProperty prop) {
		return individual.getIncomingProperties(prop);
	}

	/* @see org.mindswap.owl.OWLIndividual#setProperty(org.mindswap.owl.OWLDataProperty, java.lang.String) */
	public void setProperty(final OWLDataProperty prop, final String value) {
		individual.setProperty(prop, value);
	}

	/* @see org.mindswap.owl.OWLIndividual#setProperty(org.mindswap.owl.OWLDataProperty, java.lang.Object) */
	public void setProperty(final OWLDataProperty prop, final Object value) {
		individual.setProperty(prop, value);
	}

	/* @see org.mindswap.owl.OWLIndividual#setProperty(org.mindswap.owl.OWLDataProperty, org.mindswap.owl.OWLDataValue) */
	public void setProperty(final OWLDataProperty prop, final OWLDataValue value) {
		individual.setProperty(prop, value);
	}

	/* @see org.mindswap.owl.OWLIndividual#addProperty(org.mindswap.owl.OWLDataProperty, org.mindswap.owl.OWLDataValue) */
	public void addProperty(final OWLDataProperty prop, final OWLDataValue value) {
		individual.addProperty(prop, value);
	}

	/* @see org.mindswap.owl.OWLIndividual#addProperty(org.mindswap.owl.OWLDataProperty, java.lang.Object) */
	public void addProperty(final OWLDataProperty prop, final Object value) {
		individual.addProperty(prop, value);
	}

	/* @see org.mindswap.owl.OWLIndividual#addProperty(org.mindswap.owl.OWLDataProperty, java.lang.String) */
	public void addProperty(final OWLDataProperty prop, final String value) {
		individual.addProperty(prop, value);
	}

	/* @see org.mindswap.owl.OWLIndividual#removeProperty(org.mindswap.owl.OWLProperty, org.mindswap.owl.OWLValue) */
	public void removeProperty(final OWLProperty theProp, final OWLValue theValue) {
		individual.removeProperty(theProp,theValue);
	}

	/* @see org.mindswap.owl.OWLIndividual#addProperty(org.mindswap.owl.OWLObjectProperty, org.mindswap.owl.OWLIndividual) */
	public void addProperty(final OWLObjectProperty prop, final OWLIndividual value) {
		individual.addProperty(prop, value);
	}

	/* @see org.mindswap.owl.OWLIndividual#setProperty(org.mindswap.owl.OWLObjectProperty, org.mindswap.owl.OWLIndividual) */
	public void setProperty(final OWLObjectProperty prop, final OWLIndividual value) {
		individual.setProperty(prop, value);
	}

	/* @see org.mindswap.owl.OWLIndividual#addType(org.mindswap.owl.OWLClass) */
	public void addType(final OWLClass c) {
		individual.addType(c);
	}

	/* @see org.mindswap.owl.OWLIndividual#removeTypes() */
	public void removeTypes() {
		individual.removeTypes();
	}

	/* @see org.mindswap.owl.OWLIndividual#getType() */
	public OWLClass getType() {
		return individual.getType();
	}

	/* @see org.mindswap.owl.OWLIndividual#getTypes() */
	public Set<OWLClass> getTypes() {
		return individual.getTypes();
	}

	/* @see org.mindswap.owl.OWLIndividual#isType(org.mindswap.owl.OWLClass) */
	public boolean isType(final OWLClass c) {
		return individual.isType(c);
	}

	/* @see org.mindswap.owl.OWLEntity#isAnon() */
	public boolean isAnon() {
		return individual.isAnon();
	}

	/* @see org.mindswap.owl.OWLObject#getURI() */
	public URI getURI() {
		return individual.getURI();
	}

	/* @see org.mindswap.owl.OWLEntity#getAnonID() */
	public String getAnonID() {
		return individual.getAnonID();
	}

	/* @see org.mindswap.owl.OWLEntity#getLabel() */
	public String getLabel(final String lang) {
		return individual.getLabel(null);
	}

	/* @see org.mindswap.owl.OWLEntity#getLabels() */
	public List<String> getLabels() {
		return individual.getLabels();
	}

	/* @see org.mindswap.owl.OWLEntity#setLabel(java.lang.String) */
	public void setLabel(final String label, final String lang) {
		individual.setLabel(label, null);
	}

	/* @see org.mindswap.owl.OWLEntity#getPropertyAsDataValue(java.net.URI, java.lang.String) */
	public OWLDataValue getPropertyAsDataValue(final URI prop, final String lang) {
		return individual.getPropertyAsDataValue(prop, lang);
	}

	/* @see org.mindswap.owl.OWLEntity#getPropertyAsIndividual(java.net.URI) */
	public OWLIndividual getPropertyAsIndividual(final URI prop)
	{
		return individual.getPropertyAsIndividual(prop);
	}

	/* @see org.mindswap.owl.OWLEntity#getPropertiesAsDataValue(java.net.URI) */
	public List<OWLDataValue> getPropertiesAsDataValue(final URI prop) {
		return individual.getPropertiesAsDataValue(prop);
	}

	/* @see org.mindswap.owl.OWLEntity#getPropertiesAsIndividual(java.net.URI) */
	public OWLIndividualList<?> getPropertiesAsIndividual(final URI prop)
	{
		return individual.getPropertiesAsIndividual(prop);
	}

	/* @see org.mindswap.owl.OWLEntity#addProperty(java.net.URI, org.mindswap.owl.OWLDataValue) */
	public void addProperty(final URI prop, final OWLDataValue value) {
		individual.addProperty(prop, value);
	}

	/* @see org.mindswap.owl.OWLEntity#addProperty(java.net.URI, org.mindswap.owl.OWLEntity) */
	public void addProperty(final URI p, final OWLEntity o)
	{
		individual.addProperty(p, o);
	}

	/* @see org.mindswap.owl.OWLEntity#addProperty(java.net.URI, java.lang.String, java.lang.String) */
	public void addProperty(final URI prop, final String value, final String lang) {
		individual.addProperty(prop, value, lang);
	}

	/* @see org.mindswap.owl.OWLEntity#setProperty(java.net.URI, org.mindswap.owl.OWLDataValue) */
	public void setProperty(final URI prop, final OWLDataValue value) {
		individual.setProperty(prop, value);
	}

	/* @see org.mindswap.owl.OWLEntity#setProperty(java.net.URI, org.mindswap.owl.OWLEntity) */
	public void setProperty(final URI p, final OWLEntity o)
	{
		individual.setProperty(p, o);
	}

	/* @see org.mindswap.owl.OWLEntity#setProperty(java.net.URI, java.lang.String, java.lang.String) */
	public void setProperty(final URI prop, final String value, final String lang) {
		individual.setProperty(prop, value, lang);
	}

	/* @see org.mindswap.owl.OWLEntity#removeProperties(java.net.URI) */
	public void removeProperties(final URI prop) {
		individual.removeProperties(prop);
	}

	/* @see org.mindswap.owl.OWLObject#getModel() */
	public OWLKnowledgeBase getKB() {
		return individual.getKB();
	}

	/* @see org.mindswap.owl.OWLValue#isDataValue() */
	public final boolean isDataValue() {
		return false;
	}

	/* @see org.mindswap.owl.OWLValue#isIndividual() */
	public final boolean isIndividual() {
		return true;
	}

	/* @see org.mindswap.owl.OWLObject#getOntology() */
	public OWLOntology getOntology() {
		return individual.getOntology();
	}

	/* @see org.mindswap.owl.OWLEntity#getLocalName() */
	public String getLocalName() {
		return individual.getLocalName();
	}

	/* @see org.mindswap.owl.OWLEntity#getQName() */
	public String getQName() {
		return individual.getQName();
	}

	/* @see org.mindswap.owl.OWLIndividual#toRDF(boolean, boolean) */
	public String toRDF(final boolean withRDFTag, final boolean keepNamespaces) {
		return individual.toRDF(withRDFTag, keepNamespaces);
	}

	/* @see org.mindswap.owl.OWLIndividual#getSourceOntology() */
	public OWLOntology getSourceOntology() {
		return individual.getSourceOntology();
	}

	/* @see org.mindswap.owl.OWLIndividual#addSameAs(org.mindswap.owl.OWLIndividual) */
	public void addSameAs(final OWLIndividual other)
	{
		individual.addSameAs(other);
	}

	/* @see org.mindswap.owl.OWLIndividual#isSameAs(org.mindswap.owl.OWLIndividual) */
	public boolean isSameAs(final OWLIndividual other) {
		return individual.isSameAs( other );
	}

	/* @see org.mindswap.owl.OWLIndividual#getSameIndividuals() */
	public OWLIndividualList<?> getSameIndividuals() {
		return individual.getSameIndividuals();
	}

	/* @see org.mindswap.owl.OWLIndividual#removeSameAs(org.mindswap.owl.OWLIndividual) */
	public void removeSameAs(final OWLIndividual other)
	{
		individual.removeSameAs(other);
	}

	/* @see org.mindswap.owl.OWLIndividual#addDifferentFrom(org.mindswap.owl.OWLIndividual) */
	public void addDifferentFrom(final OWLIndividual other)
	{
		individual.addDifferentFrom(other);
	}

	/* @see org.mindswap.owl.OWLIndividual#isDifferentFrom(org.mindswap.owl.OWLIndividual) */
	public boolean isDifferentFrom(final OWLIndividual other) {
		return individual.isDifferentFrom( other );
	}

	/* @see org.mindswap.owl.OWLIndividual#getDifferentIndividuals() */
	public OWLIndividualList<?> getDifferentIndividuals() {
		return individual.getDifferentIndividuals();
	}

	/* @see org.mindswap.owl.OWLIndividual#removeDifferentFrom(org.mindswap.owl.OWLIndividual) */
	public void removeDifferentFrom(final OWLIndividual other)
	{
		individual.removeDifferentFrom(other);
	}

	/* @see org.mindswap.owl.OWLEntity#toPrettyString() */
	public String toPrettyString() {
		return individual.toPrettyString();
	}

	/* @see org.mindswap.owl.OWLIndividual#delete() */
	public void delete() {
		individual.delete();
	}

	/* @see org.mindswap.owl.OWLEntity#getNamespace() */
	public String getNamespace() {
		return individual.getNamespace();
	}
}
