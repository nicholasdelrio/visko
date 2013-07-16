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

import impl.owl.CastingList;
import impl.owl.OWLIndividualListImpl;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.mindswap.common.Parser;
import org.mindswap.common.Variable;
import org.mindswap.owl.OWLDataType;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owl.OWLModel;
import org.mindswap.owl.OWLObjectConverterRegistry;
import org.mindswap.owl.OWLOntology;
import org.mindswap.owl.OWLProvider;
import org.mindswap.pellet.PelletOptions;
import org.mindswap.pellet.jena.PelletReasonerFactory;
import org.mindswap.query.ABoxQuery;
import org.mindswap.query.QueryLanguage;
import org.mindswap.utils.URIUtils;

import com.hp.hpl.jena.ontology.OntDocumentManager;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.rulesys.OWLFBRuleReasonerFactory;
import com.hp.hpl.jena.reasoner.rulesys.OWLMicroReasonerFactory;
import com.hp.hpl.jena.reasoner.rulesys.OWLMiniReasonerFactory;
import com.hp.hpl.jena.reasoner.rulesys.RDFSRuleReasonerFactory;
import com.hp.hpl.jena.reasoner.transitiveReasoner.TransitiveReasonerFactory;

/**
 *
 * @author unascribed
 * @version $Rev: 2317 $; $Author: nick $; $Date: 2011/01/24 06:37:51 $
 */
public class JenaOWLProvider implements OWLProvider
{
	// Implementation note: This class should *not* maintain any state between
	// the registerConverters(..) method derived from OWLObjectConverterProvider
	// and methods directly derived from OWLProvider! The reason is that two
	// instances of this class will be created; one used by the OWLObjectConverterRegistry
	// to register all converters and one hold by the OWLFactory.

	private static final OWLIndividualList<OWLIndividual> EMPTY_LIST =
		new OWLIndividualListImpl<OWLIndividual>(Collections.<OWLIndividual>emptyList());

	private OWLKnowledgeBase kb; // lazy initialization

	private enum Reasoners { Pellet, OWL, OWLMini, OWLMicro, RDFS, Transitive }

//	static
//	{
//		// The global document manager does not use the global file manager by default. The following
//		// statements need to be done because we want the global doc manager to use the global file manager.
//
//		// make all alternative locations global before we set global file manager on global doc manager
//		OntDocumentManager globalDM = OntDocumentManager.getInstance();
//		FileManager globalFM = FileManager.get();
//		LocationMapper lm = globalDM.getFileManager().getLocationMapper();
//		Iterator<String> it = lm.listAltEntries();
//		while (it.hasNext())
//		{
//			String altURI = it.next();
//			globalFM.getLocationMapper().addAltEntry(altURI, lm.getAltEntry(altURI));
//		}
//
//		// make sure that caching policy is applied to global file manager
//		boolean cacheModels = globalDM.getCacheModels();
////		globalFM.setModelCaching(cacheModels);
//
//		// make sure that the doc manager sees updates to the location mappings held by the global file manager
//		globalDM.setFileManager(globalFM);
////		odm.setProcessImports(false);
//	}

	/**
	 *
	 */
	public JenaOWLProvider()
	{
		super();
	}

	/* @see org.mindswap.owl.OWLProvider#createReasoner(java.lang.String) */
	public Reasoner createReasoner(final String reasonerName)
	{
		// Pellet reasoner (first, because it is most likely used)
		if (Reasoners.Pellet.name().equals(reasonerName))
		{
			PelletOptions.USE_CLASSIFICATION_MONITOR = PelletOptions.MonitorType.NONE; // mute console outputs
			return PelletReasonerFactory.theInstance().create();
		}
		// Jena built-in reasoners
		else if (Reasoners.OWL.name().equals(reasonerName))
		{
			return OWLFBRuleReasonerFactory.theInstance().create(null);
		}
		else if (Reasoners.OWLMini.name().equals(reasonerName))
		{
			return OWLMiniReasonerFactory.theInstance().create(null);
		}
		else if (Reasoners.OWLMicro.name().equals(reasonerName))
		{
			return OWLMicroReasonerFactory.theInstance().create(null);
		}
		else if (Reasoners.RDFS.name().equals(reasonerName))
		{
			return RDFSRuleReasonerFactory.theInstance().create(null);
		}
		else if (Reasoners.Transitive.name().equals(reasonerName))
		{
			return TransitiveReasonerFactory.theInstance().create(null);
		}

		return null;
	}

	/* @see org.mindswap.owl.OWLProvider#getReasonerNames() */
	public List<String> getReasonerNames()
	{
		List<String> reasoners = new ArrayList<String>();
		for (Reasoners r : Reasoners.values())
		{
			reasoners.add(r.name());
		}
		return reasoners;
	}

	private OWLKnowledgeBase getKB() {
		if (kb == null) {
			kb = createKB();
			kb.setAutoTranslate(false);
		}
		return kb;
	}

	/* @see org.mindswap.owl.OWLProvider#createOntology(java.net.URI) */
	public OWLOntology createOntology(final URI uri) {
		return getKB().createOntology(uri);
	}

	/* @see org.mindswap.owl.OWLProvider#createKB() */
	public OWLKnowledgeBase createKB() {
		return new OWLKnowledgeBaseImpl();
	}

	/* @see org.mindswap.owl.OWLProvider#createIndividualList() */
	public <T extends OWLIndividual> OWLIndividualList<T> createIndividualList() {
		return new OWLIndividualListImpl<T>();
	}

	/* @see org.mindswap.owl.OWLProvider#createIndividualList(int) */
	public <T extends OWLIndividual> OWLIndividualList<T> createIndividualList(final int capacity)
	{
		return new OWLIndividualListImpl<T>(capacity);
	}

	/* @see org.mindswap.owl.OWLProvider#createABoxQueryParser(org.mindswap.owl.OWLModel, org.mindswap.query.QueryLanguage) */
	public Parser<String, ABoxQuery<Variable>> createABoxQueryParser(final OWLModel model, final QueryLanguage lang)
	{
		return new ABoxQueryParser(model, lang);
	}

	/* @see org.mindswap.owl.OWLProvider#getDataType(java.lang.Class) */
	public OWLDataType getDataType(final Class<?> clazz)
	{
		return getKB().getDataType(clazz);
	}

	/* @see org.mindswap.owl.OWLProvider#castList(java.util.List, java.lang.Class) */
	public <T extends OWLIndividual> OWLIndividualList<T> castList(final List<? extends OWLIndividual> list,
		final Class<T> castTarget)
	{
		assert (list != null && castTarget != null) : "Illegal: list and/or cast target parameter was null.";

		final OWLIndividualList<T> result = createIndividualList();
		T element;
		for (final OWLIndividual individual : list)
		{
			element = individual.castTo(castTarget);
			result.add(element);
		}
		return result;
	}

	/* @see org.mindswap.owl.OWLProvider#wrapList(java.util.List, java.lang.Class) */
	public <T extends OWLIndividual> CastingList<T> wrapList(final List<? extends OWLIndividual> list,
		final Class<T> castTarget)
	{
		// assertions as specified by interface JavaDoc are implemented in constructor
		return new CastingList<T>(list, castTarget);
	}

	/* @see org.mindswap.owl.OWLProvider#emptyIndividualList() */
	@SuppressWarnings("unchecked") // cast in method is not critical since EMPTY_LIST is empty and immutable
	public <T extends OWLIndividual> OWLIndividualList<T> emptyIndividualList()
	{
		return (OWLIndividualList<T>) EMPTY_LIST;
	}

	/* @see org.mindswap.owl.OWLProvider#unmodifiableIndividualList(java.util.List) */
	public <T extends OWLIndividual> OWLIndividualList<T> unmodifiableIndividualList(final List<T> list)
	{
		return new OWLIndividualListImpl<T>(Collections.unmodifiableList(list));
	}

	/* @see org.mindswap.owl.OWLObjectConverterProvider#registerConverters(org.mindswap.owl.OWLObjectConverterRegistry) */
	public void registerConverters(final OWLObjectConverterRegistry registry)
	{
		OWLConverters.registerConverters(registry);
	}

	/**
	 * This method implements {@link OWLKnowledgeBase#addAlternativeLocation(URI, URI)}.
	 */
	static URI addAlternativeLocation(final URI original, final URI copy)
	{
		String originalAsString = original.toString();
		String prevAltnURI = OntDocumentManager.getInstance().doAltURLMapping(originalAsString);
		OntDocumentManager.getInstance().addAltEntry(originalAsString, copy.toString());
		return URIUtils.createURI(prevAltnURI);
	}

	/**
	 * Utility method that can be used by classes in this package. It creates
	 * a new specification for OWL DL models that are stored in memory and do no
	 * additional entailment reasoning, i.e., the reasoner is initially set
	 * <code>null</code>.
	 * <p>
	 * Furthermore, the model specification delegates to the global <tt>
	 * OntDocumentManager</tt> provided by Jena. Consequently, all the global
	 * settings are used, such as alternative paths, prefix mappings, ignored
	 * ontologies, and the global cache (if not disabled). Note that modifying
	 * the internal state of the document manager may cause inferences in
	 * multithreaded environments, e.g., modifying the set of ignored ontologies
	 * by one thread while another thread reads the set at the same time.
	 *
	 * @return A new ontology model specification.
	 */
	static OntModelSpec createOntSpec()
	{
		OntModelSpec spec = new OntModelSpec(OntModelSpec.OWL_DL_MEM);
		spec.setDocumentManager(OntDocumentManager.getInstance());
		return spec;
	}
}
