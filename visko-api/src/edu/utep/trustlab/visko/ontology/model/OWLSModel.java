package edu.utep.trustlab.visko.ontology.model;

import java.net.URI;

import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owl.OWLObjectProperty;
import org.mindswap.owl.OWLOntology;
import org.mindswap.owls.service.Service;
import org.mindswap.owls.vocabulary.OWLS;

//import edu.utep.trustlab.visko.ontology.vocabulary.ViskoO;
import edu.utep.trustlab.repository.Repository;
import edu.utep.trustlab.visko.ontology.JenaIndividual;
import edu.utep.trustlab.visko.ontology.vocabulary.ViskoS;
//import edu.utep.trustlab.visko.ontology.vocabulary.ViskoV;
import edu.utep.trustlab.visko.util.GetURLContents;
import edu.utep.trustlab.visko.util.RedirectURI;

public class OWLSModel {
	private OWLKnowledgeBase kb;
	private OWLOntology ontology;
	
	public OWLSModel() {
		loadKB();
	}
		public OWLKnowledgeBase getOWLKnowledgeBase() {
		return kb;
	}

	public Service readService(String serviceURI) {
		Service service = null;
		
		if(Repository.getServer() != null)
			serviceURI = RedirectURI.redirectHack(serviceURI, Repository.getServer().getBaseURL());
		
		URI uri = GetURLContents.getURI(serviceURI);
		try {
			service = kb.readService(uri);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return service;
	}

	public void createOntology(String uri) {
		URI ontologyURI = GetURLContents.getURI(uri);
		ontology = kb.createOntology(ontologyURI);
	}

	public void addImportsToOntology() {
		ontology.getWriter().setNsPrefix("viskoS",
				ViskoS.ONTOLOGY_VISKO_S_URI + "#");
		OWLS.addOWLSImports(ontology);
	}

	public OWLObjectProperty getObjectProperty(String uri) {
		URI propertyURI = GetURLContents.getURI(uri);
		try {
			kb.read(propertyURI);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return kb.getObjectProperty(propertyURI);
	}

	public OWLOntology getOntology() {
		return ontology;
	}

	public OWLIndividual convertJenaToOWLIndividual(JenaIndividual ind) {
		URI uri = GetURLContents.getURI(ind.getURI());
		try {
			kb.read(uri);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return kb.getIndividual(uri);
	}

	private void loadKB() {
		kb = OWLFactory.createKB();
	}
}
