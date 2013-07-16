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
package impl.jena;

import impl.owl.OWLIndividualListImpl;
import impl.owl.OWLObjectImpl;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.mindswap.owl.OWLDataValue;
import org.mindswap.owl.OWLEntity;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLIndividualList;
import org.mindswap.utils.URIUtils;

import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDFS;

/**
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:51 $
 */
public abstract class OWLEntityImpl<R extends Resource> extends OWLObjectImpl implements OWLEntity
{
	protected final R resource;
	protected final OWLOntologyImpl ontology;

	public OWLEntityImpl(final OWLOntologyImpl ontology, final R resource)
	{
		this.resource = resource;
		this.ontology = ontology;
	}

	/* @see org.mindswap.owl.OWLEntity#getKB() */
	public OWLKnowledgeBaseImpl getKB()
	{
		return ontology.getKB();
	}

	/* @see org.mindswap.owl.OWLEntity#getOntology() */
	public OWLOntologyImpl getOntology()
	{
		return ontology;
	}

//	public OWLEntity inModel(final OWLModelImpl model)
//	{
//		if (model instanceof OWLOntology) return inOntology((OWLOntology) model);
//
//		// otherwise, model is a OWLKnowledgeBase
//		return inOntology(((OWLKnowledgeBaseImpl) model).getBaseOntology());
//	}

	/* @see org.mindswap.owl.OWLEntity#isAnon() */
	public boolean isAnon()
	{
		return resource.isAnon();
	}

	/* @see org.mindswap.owl.OWLEntity#getURI() */
	public URI getURI()
	{
		return (resource.isAnon())? null : URI.create(resource.getURI());
	}

	/* @see org.mindswap.owl.OWLEntity#getLocalName() */
	public String getLocalName()
	{
		return (resource.isAnon())? null : URIUtils.getLocalName(resource.getURI());
	}

	/* @see org.mindswap.owl.OWLEntity#getQName() */
	public String getQName()
	{
		return (resource.isAnon())? null : getKB().getQNames().shortForm(resource.getURI());
	}

	/* @see org.mindswap.owl.OWLEntity#getNamespace() */
	public String getNamespace() {
		return resource.getNameSpace();
	}

	/* @see org.mindswap.owl.OWLEntity#getLabel(java.lang.String) */
	public String getLabel(final String lang)
	{
		final Literal value = getDataValue(RDFS.label, lang);
		return (value == null) ? null : value.getLexicalForm();
	}

	/* @see org.mindswap.owl.OWLEntity#getLabels() */
	public List<String> getLabels()
	{
		return getLiterals(RDFS.label);
	}

	/* @see org.mindswap.owl.OWLEntity#setLabel(java.lang.String, java.lang.String) */
	public void setLabel(final String label, final String lang)
	{
		removeDataValues(RDFS.label, lang);
		ontology.ontModel.add(resource, RDFS.label, label, lang);
	}

	/* @see org.mindswap.owl.OWLEntity#getPropertiesAsDataValue(java.net.URI) */
	public List<OWLDataValue> getPropertiesAsDataValue(final URI propURI) {
		final Property prop = ResourceFactory.createProperty(propURI.toString());
		return getDataValues(prop);
	}

	/* @see org.mindswap.owl.OWLEntity#getPropertiesAsIndividual(java.net.URI) */
	public OWLIndividualList<?> getPropertiesAsIndividual(final URI propURI)
	{
		final Property prop = ResourceFactory.createProperty(propURI.toString());
		return getIndividuals(prop);
	}

	/* @see org.mindswap.owl.OWLEntity#getPropertyAsDataValue(java.net.URI, java.lang.String) */
	public OWLDataValue getPropertyAsDataValue(final URI propURI, final String lang)
	{
		final Property prop = ResourceFactory.createProperty(propURI.toString());
		final Literal lit = getDataValue(prop, lang);
		return (lit == null)? null : new OWLDataValueImpl(lit);
	}

	/* @see org.mindswap.owl.OWLEntity#getPropertyAsIndividual(java.net.URI) */
	public OWLIndividual getPropertyAsIndividual(final URI propURI)
	{
		final Property prop = ResourceFactory.createProperty(propURI.toString());
		final Statement s = ontology.getKB().ontModel.getProperty(resource, prop);
		if (s != null)
		{
			final RDFNode n = s.getObject();
			if (n.isResource()) return new OWLIndividualImpl(ontology, n.as(Resource.class));
		}
		return null;
	}

	/* @see org.mindswap.owl.OWLEntity#addProperty(java.net.URI, org.mindswap.owl.OWLDataValue) */
	public void addProperty(final URI propURI, final OWLDataValue value)
	{
		final Property prop = ResourceFactory.createProperty(propURI.toString());
		ontology.ontModel.add(resource, prop, (RDFNode) value.getImplementation());
	}

	/* @see org.mindswap.owl.OWLEntity#addProperty(java.net.URI, org.mindswap.owl.OWLEntity) */
	public void addProperty(final URI propURI, final OWLEntity value)
	{
		final Property prop = ResourceFactory.createProperty(propURI.toString());
		ontology.ontModel.add(resource, prop, (RDFNode) value.getImplementation());
	}

	/* @see org.mindswap.owl.OWLEntity#addProperty(java.net.URI, java.lang.String, java.lang.String) */
	public void addProperty(final URI propURI, final String value, final String lang) {
		final Property prop = ResourceFactory.createProperty(propURI.toString());
		ontology.ontModel.add(resource, prop, value, lang);
	}

	/* @see org.mindswap.owl.OWLEntity#setProperty(java.net.URI, org.mindswap.owl.OWLValue) */
	public void setProperty(final URI propURI, final OWLDataValue value)
	{
		final Property prop = ResourceFactory.createProperty(propURI.toString());
		removeDataValues(prop, value.getLanguage());
		ontology.ontModel.add(resource, prop, (RDFNode) value.getImplementation());
	}

	/* @see org.mindswap.owl.OWLEntity#setProperty(java.net.URI, org.mindswap.owl.OWLEntity) */
	public void setProperty(final URI propURI, final OWLEntity value)
	{
		final Property prop = ResourceFactory.createProperty(propURI.toString());
		ontology.getKB().removeAll(resource, prop, null);
		ontology.ontModel.add(resource, prop, (RDFNode) value.getImplementation());
	}

	/* @see org.mindswap.owl.OWLEntity#setProperty(java.net.URI, java.lang.String, java.lang.String) */
	public void setProperty(final URI propURI, final String value, final String lang)
	{
		final Property prop = ResourceFactory.createProperty(propURI.toString());
		removeDataValues(prop, lang);
		ontology.ontModel.add(resource, prop, value, lang);
	}

	protected List<String> getLiterals(final Property prop)
	{
		final List<String> list = new ArrayList<String>();
		final NodeIterator it = ontology.getKB().ontModel.listObjectsOfProperty(resource, prop);
		while (it.hasNext())
		{
			final RDFNode node = it.next();
			if (node.isLiteral())
			{
				list.add(node.as(Literal.class).getString());
			}
		}
		it.close();
		return list;
	}

	protected List<OWLDataValue> getDataValues(final Property prop)
	{
		final List<OWLDataValue> list = new ArrayList<OWLDataValue>();
		final NodeIterator it = ontology.getKB().ontModel.listObjectsOfProperty(resource, prop);
		while (it.hasNext())
		{
			final RDFNode node = it.next();
			if (node.isLiteral())
			{
				list.add(new OWLDataValueImpl(node.as(Literal.class)));
			}
		}
		it.close();
		return list;
	}

	protected OWLIndividualList<?> getIndividuals(final Property prop)
	{
		final OWLIndividualList<OWLIndividual> list = new OWLIndividualListImpl<OWLIndividual>();
		final NodeIterator it = ontology.getKB().ontModel.listObjectsOfProperty(resource, prop);
		while (it.hasNext())
		{
			final RDFNode node = it.next();
			if (node.isResource())
			{
				list.add(new OWLIndividualImpl(ontology, node.as(Resource.class)));
			}
		}
		it.close();
		return list;
	}

	protected Literal getDataValue(final Property prop, final String lang) {
		Literal found = null;
		final NodeIterator it = ontology.getKB().ontModel.listObjectsOfProperty(resource, prop);
		while (it.hasNext())
		{
			final RDFNode node = it.next();
			if (node.isLiteral())
			{
				final Literal l = node.as(Literal.class);
				String lLang = l.getLanguage();

				if (lang == null || lang.equalsIgnoreCase(lLang)) // don't care about language || exact match
				{
					found = l;
					break;
				}
            else if (lLang != null && lLang.length() > 1 && lang.equalsIgnoreCase(lLang.substring(0, 2)))
            {
                // partial match - e.g. want EN, found EN-GB
                // keep searching in case there's a better
                found = l;
            }
            else if (found == null && lLang == null)
            {
                // found a string with no (i.e. default) language - keep this unless we've got something better
                found = l;
            }
			}
		}
		it.close();
		return found;
	}

	protected void removeDataValues(final Property prop, final String lang)
	{
		if (lang == null)
		{
			ontology.getKB().removeAll(resource, prop, null);
		}
		else
		{
			final StmtIterator it = ontology.getKB().ontModel.listStatements(resource, prop, null, lang);
			while (it.hasNext())
			{
				ontology.getKB().removeAll(resource, prop, it.next().getObject());
			}
			it.close();
		}
	}

	/* @see impl.owl.OWLObjectImpl#toString() */
	@Override
	public String toString()
	{
		return (resource.isAnon())? "Anonymous (" + resource.getId() + ")" : resource.getURI();
	}

	/* @see org.mindswap.owl.OWLEntity#toPrettyString() */
	public String toPrettyString()
	{
		String value = getLabel(null);
		if (value == null)
		{
			if (isAnon()) value = "Anonymous (" + resource.getId() + ")";
			else value = ontology.getKB().getQNames().shortForm(resource.getURI());
		}
		return value;
	}

	/* @see org.mindswap.owl.OWLEntity#removeProperties(java.net.URI) */
	public void removeProperties(final URI propURI) {
		final Property prop = ResourceFactory.createProperty(propURI.toString());
		ontology.getKB().removeAll(resource, prop, null);
	}

	/* @see org.mindswap.owl.OWLObject#getImplementation() */
	public R getImplementation() {
		return resource;
	}

	/* @see org.mindswap.owl.OWLEntity#getAnonID() */
	public String getAnonID()
	{
		if (isAnon()) return resource.getId().getLabelString();
		return null;
	}

}
