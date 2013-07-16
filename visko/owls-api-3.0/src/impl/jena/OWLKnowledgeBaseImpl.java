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
 * Created on Dec 12, 2004
 */
package impl.jena;


import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicBoolean;

import org.mindswap.common.Tuple;
import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owl.OWLOntology;
import org.mindswap.owls.OWLSFactory;
import org.mindswap.owls.OWLSVersionTranslator;
import org.mindswap.owls.service.Service;
import org.mindswap.owls.vocabulary.OWLS;
import org.mindswap.utils.QNameProvider;
import org.mindswap.utils.URIUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.reasoner.Reasoner;

/**
 *
 * @author unascribed
 * @version $Rev: 2298 $; $Author: nick $; $Date: 2011/01/24 06:37:51 $
 */
public class OWLKnowledgeBaseImpl extends OWLModelImpl implements OWLKnowledgeBase
{
	private static final Logger logger = LoggerFactory.getLogger(OWLKnowledgeBaseImpl.class);

	private OWLOntologyImpl baseOntology;

	// ontologies that have been loaded, excluding their import closure; baseOntology is also not contained
	private final Set<OWLOntology> ontologies;

	// One entry for every loaded ontology and one for each ontology in its import closure:
	// someOnt.getURI()  |-->  (i, someOnt), whereby i >= 1 is a number that counts
	// how many times someOnt was imported. Example: Let a,b be ontologies that both
	// import c. After a,b were loaded to the KB then i is: i_a=1, i_b=1, i_c=2.
	private final Map<URI, Tuple<Integer, OWLOntology>> uriMap;

	private final QNameProvider qnames;
	private OWLReaderImpl reader; // lazy initialization
	private final AtomicBoolean autoConsistency;
	private final AtomicBoolean strictConversion;

	private OWLSVersionTranslator translator;
	private OWLKnowledgeBase translationSource;

	public OWLKnowledgeBaseImpl()
	{
		super(ModelFactory.createOntologyModel(JenaOWLProvider.createOntSpec()));

		setAutoTranslate(false);
		autoConsistency = new AtomicBoolean(false);
		strictConversion = new AtomicBoolean(true); // Should we set it according to the OntModelSpec?

		translationSource = this;
		ontologies = new HashSet<OWLOntology>();
		uriMap = new HashMap<URI, Tuple<Integer, OWLOntology>>();
		qnames = new QNameProvider();

		baseOntology = createNewBaseOntology();
	}

	/* @see org.mindswap.owl.OWLKnowledgeBase#addAlternativePath(java.net.URI, java.net.URI) */
	public URI addAlternativeLocation(final URI original, final URI copy)
	{
		return JenaOWLProvider.addAlternativeLocation(original, copy);
	}

	/* @see org.mindswap.owl.OWLModel#getBaseModel() */
	public final OWLOntologyImpl getBaseModel()
	{
		return baseOntology;
	}

	/* @see org.mindswap.owl.OWLKnowledgeBase#getOntologies(boolean) */
	public Set<OWLOntology> getOntologies(final boolean all)
	{
		Set<OWLOntology> onts = new HashSet<OWLOntology>(ontologies);
		if (all)
		{
			for (OWLOntology ontology : ontologies)
			{
				onts.addAll(ontology.getImports(false));
			}
		}
		onts.add(baseOntology);
		return onts;
	}

	/* @see org.mindswap.owl.OWLKnowledgeBase#getOntology(java.net.URI) */
	public OWLOntology getOntology(final URI uri)
	{
		final Tuple<Integer, OWLOntology> t = uriMap.get(uri);
		return (t != null)? t.getElement2() : null;
	}

	/* @see org.mindswap.owl.OWLKnowledgeBase#createOntology(java.net.URI) */
	public OWLOntology createOntology(final URI uri)
	{
		return createOntology(uri, null);
	}

	/* @see org.mindswap.owl.OWLKnowledgeBase#createOntology(java.net.URI, java.lang.Object) */
	public OWLOntology createOntology(final URI uri, final Object model)
	{
		return createOntology(uri, (OntModel) model);
	}

	/**
	 * Create an empty ontology with the given URI (logical xml:base or physical)
	 * and from the using the given Jena Model. The ontology will be loaded to
	 * this KB. If this KB already contains a ontology of the given URI the
	 * existing one is returned.
	 *
	 * @param uri Logical URI of the ontology xor the physical URI of the
	 * 	ontology from where it was loaded. Must not be <code>null</code>.
	 * @param model the Jean model to be used.
	 * @return The new ontology representing the given model.
	 */
	OWLOntology createOntology(final URI uri, OntModel model)
	{
		// check if there is an ontology of that URI that is already part of this KB --> if so, return it
		if (uriMap.containsKey(uri)) return getOntology(uri);

		boolean checkConsistency;
		if (model != null) checkConsistency = true; // model may contain statements that cause inconsistency
		else
		{
			model = ModelFactory.createOntologyModel(JenaOWLProvider.createOntSpec());
			checkConsistency = false; // empty model can never cause inconsistency
		}

		final OWLOntology ont = internalLoad(uri, new OWLOntologyImpl(this, uri, model), true);
		if (checkConsistency) return checkConsistency(ont);
		return ont;
	}

	/* @see org.mindswap.owl.OWLKnowledgeBase#load(org.mindswap.owl.OWLOntology, boolean) */
	public OWLOntology load(final OWLOntology ont, final boolean withImports)
	{
		// check if there is an ontology of that URI that is already part of this KB --> if so, return it
		final URI uri = ont.getURI();
		if (uriMap.containsKey(uri)) return getOntology(uri);

		OWLOntology loadedOnt = internalLoad(uri, ont, withImports);
		loadedOnt = checkConsistency(loadedOnt);
		return loadedOnt;
	}

	private OWLOntology checkConsistency(OWLOntology loadedOnt)
	{
		if (isAutoConsistency() && !isConsistent())
		{
			logger.warn("Reject loading {} because it causes the KB to be inconsistent!", loadedOnt);
			unload(loadedOnt);
			loadedOnt = null;
		}
		return loadedOnt;
	}

	private OWLOntology internalLoad(final URI uri, final OWLOntology ont, final boolean withImports)
	{
		OWLOntology loadedOnt;

		if (isAutoTranslate()) // translate the ontology if desired
		{
			// initialize dedicated KB that contains all original ontologies in untranslated form
			if (translationSource == this)
			{
				translationSource = OWLFactory.createKB();
				translationSource.setAutoTranslate(false); // since it contains untranslated ontologies
			}

			loadedOnt = translationSource.load(ont, true);
			loadedOnt = translator.translate(loadedOnt, createOntology(uri));
		}
		else // loadedOnt should keep its own KB --> we need to create a new instance that belongs to this KB
		{
			loadedOnt = new OWLOntologyImpl(this, uri, (OntModel) ont.getImplementation(), ont.isReadOnly());
		}

		final OntModel loadedOntModel = (OntModel) loadedOnt.getImplementation();
		if (withImports)
		{
			ontModel.addSubModel(loadedOntModel);

			// add import closure to uriMap so that future loads can check whether
			// an ontology of that URI was already loaded to this KB
			for (OWLOntology imp : loadedOnt.getImports(true))
			{
				addOntology(imp.getURI(), imp);
			}
		}
		else ontModel.addSubModel(loadedOntModel.getBaseModel());

		uriMap.put(uri, new Tuple<Integer, OWLOntology>(1, loadedOnt));
		ontologies.add(loadedOnt);

		return loadedOnt;
	}

	/* @see org.mindswap.owl.OWLKnowledgeBase#unload(java.net.URI) */
	public void unload(URI uri)
	{
		uri = URIUtils.standardURI(uri);
		final OWLOntology ont = getOntology(uri);
		if (ont != null) unload(ont);
		else logger.debug("Could not unload ontology {} because it was not found in this KB.", uri);
	}

	/* @see org.mindswap.owl.OWLKnowledgeBase#unload(org.mindswap.owl.OWLOntology) */
	public void unload(final OWLOntology ontology)
	{
		URI uri = ontology.getURI();
		if (!ontologies.remove(ontology))
		{
			String msg = ontology + " was not directly loaded to this KB";
			if (uri != null && uriMap.containsKey(uri))
				msg += ", but there is a ontology with the same URI that was loaded implicitly as a import";
			msg += "!";
			throw new IllegalArgumentException(msg);
		}

		remove(uri, ontology);

		final Model m = ((OntModel) ontology.getImplementation()).getBaseModel();
		ontModel.removeSubModel(m);
	}

	/* @see org.mindswap.owl.OWLKnowledgeBase#read(java.net.URI) */
	public OWLOntology read(final URI uri) throws IOException {
		return getReader().read(this, uri);
	}

	/* @see org.mindswap.owl.OWLKnowledgeBase#read(java.io.Reader, java.net.URI) */
	public OWLOntology read(final Reader in, final URI baseURI) throws IOException {
		return getReader().read(this, in, baseURI);
	}

	/* @see org.mindswap.owl.OWLKnowledgeBase#read(java.io.InputStream, java.net.URI) */
	public OWLOntology read(final InputStream in, final URI baseURI) throws IOException {
		return getReader().read(this, in, baseURI);
	}

	/* @see org.mindswap.owl.OWLKnowledgeBase#getReader() */
	public OWLReaderImpl getReader()
	{
		if (reader == null) reader = new OWLReaderImpl();
		return reader;
	}

	/* @see org.mindswap.owl.OWLKnowledgeBase#getQNames() */
	public QNameProvider getQNames() {
		return qnames;
	}

//	/* @see org.mindswap.owl.OWLKnowledgeBase#setReader(org.mindswap.owl.OWLReader) */
//	public OWLReader setReader(final OWLReader newReader) {
//		OWLReader old = this.reader;
//		this.reader = newReader;
//		return old;
//	}

	/* @see org.mindswap.owl.OWLModel#refresh() */
	public void refresh()
	{
		if (isBulkUpdate()) return; // skip repeated work while a bulk update is not yet finished
		ontModel.rebind();
	}

	/* @see org.mindswap.owl.OWLKnowledgeBase#isAutoConsistency() */
	public boolean isAutoConsistency()
	{
		return autoConsistency.get();
	}

	/* @see org.mindswap.owl.OWLKnowledgeBase#setAutoConsistency(boolean) */
	public boolean setAutoConsistency(final boolean auto)
	{
		return autoConsistency.getAndSet(auto);
	}

	/* @see org.mindswap.owl.OWLKnowledgeBase#isAutoTranslate() */
	public boolean isAutoTranslate() {
		return (translator != null);
	}

	/* @see org.mindswap.owl.OWLKnowledgeBase#setAutoTranslate(boolean) */
	public boolean setAutoTranslate(final boolean auto)
	{
		boolean previous = (translator == null)? false : true;
		translator = auto? OWLSFactory.createVersionTranslator() : null;
		return previous;
	}

	/* @see org.mindswap.owl.OWLKnowledgeBase#isStrictConversion() */
	public boolean isStrictConversion()
	{
		return strictConversion.get();
	}

	/* @see org.mindswap.owl.OWLKnowledgeBase#setStrictConversion(boolean) */
	public boolean setStrictConversion(final boolean strict)
	{
		return strictConversion.getAndSet(strict);
	}

	/* @see org.mindswap.owl.OWLKnowledgeBase#getTranslationSource() */
	public OWLKnowledgeBase getTranslationSource() {
		return translationSource;
	}

	/* @see org.mindswap.owl.OWLKnowledgeBase#readService(java.net.URI) */
	public Service readService(final URI uri) throws IOException
	{
		final OWLOntology ont = read(uri);
		return getCorrectService(ont, uri);
	}

	/* @see org.mindswap.owl.OWLKnowledgeBase#readService(java.io.Reader, java.net.URI) */
	public Service readService(final Reader in, final URI baseURI) throws IOException
	{
		final OWLOntology ont = read(in, baseURI);
		return getCorrectService(ont, baseURI);
	}

	/* @see org.mindswap.owl.OWLKnowledgeBase#readService(java.io.InputStream, java.net.URI) */
	public Service readService(final InputStream in, final URI baseURI) throws IOException
	{
		final OWLOntology ont = read(in, baseURI);
		return getCorrectService(ont, baseURI);
	}

	/* @see org.mindswap.owl.OWLKnowledgeBase#readAllServices(java.net.URI) */
	public OWLIndividualList<Service> readAllServices(final URI uri) throws IOException
	{
		final OWLOntology ont = read(uri);
		return (ont == null)? null : ont.getServices(false);
	}

	/* @see org.mindswap.owl.OWLKnowledgeBase#readAllServices(java.io.Reader, java.net.URI) */
	public OWLIndividualList<Service> readAllServices(final Reader in, final URI baseURI) throws IOException
	{
		final OWLOntology ont = read(in, baseURI);
		return (ont == null)? null : ont.getServices(false);
	}

	/* @see org.mindswap.owl.OWLKnowledgeBase#readAllServices(java.io.InputStream, java.net.URI) */
	public OWLIndividualList<Service> readAllServices(final InputStream in, final URI baseURI) throws IOException
	{
		final OWLOntology ont = read(in, baseURI);
		return (ont == null)? null : ont.getServices(false);
	}

	/**
	 * Method queries this KB for a service that is declared in the given
	 * ontology and has the given URI. If the URI contains a fragment identifier,
	 * only that service matching the full URI (including the fragment) is
	 * returned. If the given URI does not contain a fragment identifier, the
	 * first service found that matches the URI is returned. If the URI does
	 * not match any of the services found or if it is <code>null</code> the
	 * first one found is returned.
	 *
	 * @param ont The ontology acting as a filter to constrain the scope of
	 * 	relevant services.
	 * @param uri The URI of the service to return or <code>null</code> to
	 * 	pick one arbitrarily.
	 * @return  The service (partially) matching the given URI, or
	 * 	<code>null</code> if no service exists in the given ontology.
	 */
	private Service getCorrectService(final OWLOntology ont, final URI uri)
	{
		final OWLIndividualList<?> services = getInstances(
			OWLS.Service.Service, false, (OntModel) ont.getImplementation());
		if (services == null || services.isEmpty()) return null;

		// no URI given --> we can do no better than picking one service arbitrarily
		if (uri == null) return services.get(0).castTo(Service.class);

		final String stdURI = URIUtils.standardURI(uri).toString();

		// search now the list
		OWLIndividual secondChoice = null;
		for (final OWLIndividual service : services)
		{
			final URI serviceURI = service.getURI();
			if (uri.equals(serviceURI)) return service.castTo(Service.class);
			if (serviceURI.toString().startsWith(stdURI)) secondChoice = service;
		}

		// All services in the list neither match full nor standard URI --> this situation
		// usually happens when loading a remote or local file, whereby its physical URI
		// does not match the base URI in the ontology.
		// --> we can not do better than arbitrarily returning the first service found
		if (secondChoice == null && services.size() > 0) secondChoice = services.get(0);

		return (secondChoice == null)? null : secondChoice.castTo(Service.class);
	}

	/* @see impl.jena.OWLModelImpl#getOntology() */
	@Override
	protected final OWLOntologyImpl getOntology()
	{
		return baseOntology;
	}

	/* @see impl.jena.OWLModelImpl#reasonerSet(com.hp.hpl.jena.reasoner.Reasoner) */
	@Override
	protected void reasonerSet(final Reasoner oldReasoner)
	{
		// we need to reassign baseOntology since ontModel is a new instance
		baseOntology = createNewBaseOntology();
	}

	/* @see impl.jena.OWLModelImpl#removeAll(com.hp.hpl.jena.rdf.model.Resource, com.hp.hpl.jena.rdf.model.Property, com.hp.hpl.jena.rdf.model.RDFNode) */
	@Override
	protected void removeAll(final Resource s, final Property p, final RDFNode o)
	{
		startBulkUpdate();
		ontModel.removeAll(s, p, o);
		for (Entry<URI, Tuple<Integer, OWLOntology>> e : uriMap.entrySet())
		{
			final OWLOntology ont = e.getValue().getElement2();
			if (ont.isReadOnly()) continue;
			((OWLOntologyImpl) ont).ontModel.removeAll(s, p, o);
		}
		endBulkUpdate(true);
	}

	void add(final URI ontURI, final OWLOntology ont)
	{
		addOntology(ontURI, ont);
		for (OWLOntology imp : ont.getImports(true))
		{
			addOntology(imp.getURI(), imp);
		}
	}

	void remove(final URI ontURI, final OWLOntology ont)
	{
		removeOntology(ontURI);
		for (OWLOntology imp : ont.getImports(true))
		{
			removeOntology(imp.getURI());
		}
	}

	private final OWLOntologyImpl createNewBaseOntology()
	{
		OWLOntologyImpl newBaseOnt = new OWLOntologyImpl(
			this, URI.create(OWLOntology.SYNTHETIC_ONT_PREFIX + "baseOntology:" + System.nanoTime()), ontModel);

		// keep translation source ontology if necessary
		if (baseOntology != null && baseOntology.getTranslationSource() != baseOntology)
		{
			newBaseOnt.setTranslationSource(baseOntology.getTranslationSource());
		}

		return newBaseOnt;
	}

	private void addOntology(final URI ontURI, OWLOntology ont)
	{
		final Tuple<Integer, OWLOntology> t = uriMap.get(ontURI);
		if (t != null) t.setElements(t.getElement1() + 1, t.getElement2());
		else
		{
			if (ont.getKB() != this) // may be true if this method is invoked transitively by OWLOntology.addImport(s)
			{
				ont = new OWLOntologyImpl(this, ontURI, (OntModel) ont.getImplementation(), ont.isReadOnly());
			}

			// make built-in ontologies read-only by default <-- fixed knowledge that should
			// not be modifiable by default
			String ns = ontURI.toString();
			ns = (ns.endsWith("#"))? ns : ns + "#";
			if (isInLanguageNamespace(ns)) ont.setReadOnly(true);

			uriMap.put(ontURI, new Tuple<Integer, OWLOntology>(1, ont));
		}
	}

	private void removeOntology(final URI ontURI)
	{
		final Tuple<Integer, OWLOntology> t = uriMap.get(ontURI);
		if (t != null)
		{
			if (t.getElement1().intValue() == 1) uriMap.remove(ontURI);
			else t.setElements(t.getElement1() - 1, t.getElement2());
		}
	}

}
