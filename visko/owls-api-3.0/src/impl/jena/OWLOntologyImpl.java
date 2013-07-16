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
 * Created on Dec 10, 2004
 */
package impl.jena;

import java.net.URI;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owl.OWLOntology;
import org.mindswap.utils.URIUtils;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.Ontology;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

/**
 *
 * @author unascribed
 * @version $Rev: 2274 $; $Author: nick $; $Date: 2011/01/24 06:37:51 $
 */
public class OWLOntologyImpl extends OWLModelImpl implements OWLOntology
{
	private boolean readOnly;
	private final OWLKnowledgeBaseImpl kb;
	private final URI uri;
	/**
	 * If this ontology represents some OWL-S ontology that was translated from
	 * an earlier OWL-S version this field references that source ontology,
	 * otherwise, it references <code>this</code>.
	 */
	private OWLOntology sourceOntology;

	public OWLOntologyImpl(final OWLKnowledgeBaseImpl kb, final URI uri, final OntModel jenaModel)
	{
		this(kb, uri, jenaModel, false);
	}

	public OWLOntologyImpl(final OWLKnowledgeBaseImpl kb, final URI uri, final OntModel jenaModel,
		final boolean readOnly)
	{
		super(jenaModel);
		assert (uri != null && kb != null) : "Ontology URI and/or KB parameter null";
		this.uri = uri;
		this.kb = kb;
		this.readOnly = readOnly;
		this.sourceOntology = this;
	}

//	/**
//	 * Test whether the given object is a ontology that is equal to this ontology,
//	 * which is <code>true</code> iff the underlying graphs are identical Java
//	 * objects. This is not the same test as comparing whether two ontologies have
//	 * the same structure (i.e. contain the same set of statements).
//	 *
//	 * @see java.lang.Object#equals(java.lang.Object)
//	 * @return <code>true</code> iff the underlying graphs are identical Java objects.
//	 */
//	@Override
//	public boolean equals(final Object obj)
//	{
//		if (obj instanceof OWLOntologyImpl) return ontModel.equals(((OWLOntologyImpl) obj).ontModel);
//		return false;
//	}
//
//	/* @see java.lang.Object#hashCode() */
//	@Override
//	public int hashCode()
//	{
//		return ontModel.hashCode();
//	}

	/* @see org.mindswap.owl.OWLModel#getBaseModel() */
	public OWLOntologyImpl getBaseModel()
	{
		final OntModel model = ModelFactory.createOntologyModel(ontModel.getSpecification());
		model.add(ontModel.getBaseModel());
		return new OWLOntologyImpl(kb, uri, model, readOnly);
	}

	/* @see org.mindswap.owl.OWLOntology#getKB() */
	public OWLKnowledgeBaseImpl getKB()
	{
		return kb;
	}

	/* @see org.mindswap.owl.OWLOntology#getImports(boolean) */
	public Set<OWLOntology> getImports(final boolean closure)
	{
		final Set<OWLOntology> imports = new HashSet<OWLOntology>((int) (ontModel.countSubModels() * 1.25));
		final Set<String> importURIs = ontModel.listImportedOntologyURIs(closure);
		for (final String importURI : importURIs)
		{
			final OntModel importModel = ontModel.getImportedModel(importURI);
			imports.add(new OWLOntologyImpl(kb, URIUtils.createURI(importURI), importModel, readOnly));
		}
		return imports;
	}

	/* @see org.mindswap.owl.OWLOntology#addImport(org.mindswap.owl.OWLOntology) */
	public void addImport(final OWLOntology ontology)
	{
		checkNotReadOnly();
		addImport0(ontology);
		refresh();
	}

	/* @see org.mindswap.owl.OWLOntology#addImports(java.util.Collection) */
	public void addImports(final Collection<OWLOntology> theImports)
	{
		checkNotReadOnly();
		startBulkUpdate();
		final boolean refresh = (theImports.size() > 0);
		for (final OWLOntology aOnt : theImports)
		{
			addImport0(aOnt);
		}
		endBulkUpdate(refresh);
	}

	/* @see org.mindswap.owl.OWLOntology#removeImport(org.mindswap.owl.OWLOntology) */
	public void removeImport(final OWLOntology ontology)
	{
		checkNotReadOnly();
		removeImport0(ontology);
		refresh();
	}

	/* @see org.mindswap.owl.OWLOntology#removeImports(java.util.Set) */
	public void removeImports(final Set<OWLOntology> theImports)
	{
		checkNotReadOnly();
		startBulkUpdate();
		final boolean refresh = (theImports.size() > 0);
		for (final OWLOntology ont : theImports)
		{
			removeImport0(ont);
		}
		endBulkUpdate(refresh);
	}

	/* @see org.mindswap.owl.OWLOntology#getURI() */
	public URI getURI() {
		return uri;
	}

	/* @see java.lang.Object#toString() */
	@Override
	public String toString() {
		return "Ontology(" + uri + ")";
	}

	/* @see org.mindswap.owl.OWLModel#refresh() */
	public void refresh()
	{
		if (isBulkUpdate()) return; // skip repeated refreshing while a bulk update is not finished yet
		kb.refresh();
	}

	/* @see org.mindswap.owl.OWLOntology#getTranslationSource() */
	public OWLOntology getTranslationSource() {
		return sourceOntology;
	}

	/* @see org.mindswap.owl.OWLOntology#setTranslationSource(org.mindswap.owl.OWLOntology) */
	public void setTranslationSource(final OWLOntology ontology) {
		sourceOntology = ontology;
	}

	/* @see org.mindswap.owl.OWLOntology#difference(org.mindswap.owl.OWLOntology) */
	public OWLOntology difference(final OWLOntology ont)
	{
		final OntModel diff = ModelFactory.createOntologyModel(
			ontModel.getSpecification(), ontModel.difference(((OWLOntologyImpl) ont).ontModel));

		final OWLKnowledgeBase newKB = OWLFactory.createKB();
		return newKB.createOntology(URI.create(
			OWLOntology.SYNTHETIC_ONT_PREFIX + "difference:Ontology" + System.nanoTime()), diff);
	}

	/* @see org.mindswap.owl.OWLOntology#intersection(org.mindswap.owl.OWLOntology) */
	public OWLOntology intersection(final OWLOntology ont)
	{
		final OntModel intersection = ModelFactory.createOntologyModel(
			ontModel.getSpecification(), ontModel.intersection(((OWLOntologyImpl) ont).ontModel));

		final OWLKnowledgeBase newKB = OWLFactory.createKB();
		return newKB.createOntology(URI.create(
			OWLOntology.SYNTHETIC_ONT_PREFIX + "intersection:Ontology" + System.nanoTime()), intersection);
	}

	/* @see org.mindswap.owl.OWLOntology#union(org.mindswap.owl.OWLOntology) */
	public OWLOntology union(final OWLOntology  ont)
	{
		final OntModel union = ModelFactory.createOntologyModel(
			ontModel.getSpecification(), ontModel.union(((OWLOntologyImpl) ont).ontModel));

		final OWLKnowledgeBase newKB = OWLFactory.createKB();
		return newKB.createOntology(URI.create(
			OWLOntology.SYNTHETIC_ONT_PREFIX + "union:Ontology" + System.nanoTime()), union);
	}

	/* @see org.mindswap.owl.OWLOntology#add(org.mindswap.owl.OWLOntology) */
	public void add(final OWLOntology  ont)
	{
		checkNotReadOnly();
		final Model m = ((OWLOntologyImpl) ont).ontModel.getBaseModel();
		ontModel.getBaseModel().add(m);
		kb.refresh();
	}

	/* @see org.mindswap.owl.OWLOntology#isReadOnly() */
	public boolean isReadOnly()
	{
		return readOnly;
	}

	/* @see org.mindswap.owl.OWLOntology#setReadOnly(boolean) */
	public void setReadOnly(final boolean readOnly)
	{
		this.readOnly = readOnly;
	}

	/* @see org.mindswap.owl.OWLOntology#removeAll(boolean) */
	public void removeAll(final boolean includingImports)
	{
		checkNotReadOnly();
		if (includingImports) removeImports(getImports(false));
		ontModel.getBaseModel().removeAll();
		ontModel.rebind();
	}

	/** Throw an {@link UnsupportedOperationException} if this ontology is in read-only state. */
	protected final void checkNotReadOnly()
	{
		if (readOnly) throw new UnsupportedOperationException("Read-only ontology cannot be modified.");
	}

	/* @see impl.jena.OWLModelImpl#getOntology() */
	@Override
	protected final OWLOntologyImpl getOntology()
	{
		return this;
	}

	/* @see impl.jena.OWLModelImpl#reasonerSet(com.hp.hpl.jena.reasoner.Reasoner) */
	@Override
	protected void reasonerSet(final Reasoner oldReasoner)
	{
		// currently, we do not react to this event
	}

	/* @see impl.jena.OWLModelImpl#removeAll(com.hp.hpl.jena.rdf.model.Resource, com.hp.hpl.jena.rdf.model.Property, com.hp.hpl.jena.rdf.model.RDFNode) */
	@Override
	protected void removeAll(final Resource s, final Property p, final RDFNode o)
	{
		checkNotReadOnly();
		ontModel.removeAll(s, p, o);
	}

	private void addImport0(final OWLOntology importOnt)
	{
		final URI impURI = importOnt.getURI();
		final String impURIasString = impURI.toString();
		if (ontModel.hasLoadedImport(impURIasString)) return;

		// 1.) Load other ontology to this ontology.
		ontModel.getDocumentManager().loadImport(ontModel, impURIasString);

		// 2.) Add import statement to owl:Ontology.
		// 2.a) Do we have an owl:Ontology individual of our URI? If so, reuse it.
		Ontology o = ontModel.getOntology(uri.toString());
		if (o == null)
		{
			// 2.b) Do we have any other owl:Ontology individual? If so, use the first one found.
			final OntModel baseModel = ModelFactory.createOntologyModel(
				ontModel.getSpecification(), ontModel.getBaseModel());
			final ExtendedIterator<Ontology> it = baseModel.listOntologies();
			if (it.hasNext()) o = it.next();
			it.close();

			if (o == null) // 2.c) Still nothing found? Create an owl:Ontology individual that we can use.
			{
				o = ontModel.createOntology(uri.toString());
			}
		}
		o.addImport(ontModel.createResource(impURIasString));

		// 3.) Tell our KB about imported ontology so that it can update itself
		kb.add(impURI, importOnt);
	}

	private void removeImport0(final OWLOntology ontology)
	{
		final URI ontURI = ontology.getURI();
		final String impURIasString = ontURI.toString();
		if (!ontModel.hasLoadedImport(impURIasString)) return;
		ontModel.getDocumentManager().unloadImport(ontModel, impURIasString);

		// Remove import statement; we must NOT remove owl:Ontology individual itself because
		// it may be the subject of other statements, e.g., owl:versionInfo, ...
		final Ontology o = ontModel.getOntology(uri.toString());
		if (o != null) o.removeImport(ontModel.createResource(impURIasString));

		// Tell our KB about removal so that it can update itself
		kb.remove(ontURI, ontology);
	}

}
