package edu.utep.trustlab.visko.ontology.vocabulary;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class OWL {
	
	private static final String ONTOLOGY_OWL_URI = "http://www.w3.org/2002/07/owl";
	
	public static final String THING = ONTOLOGY_OWL_URI + "#Thing";
	
	// model and ontology
	private static OntModel model;
	
	static {
		model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
		model.read(ONTOLOGY_OWL_URI);
	}
	
	public static OntResource getOWLThing(){
		return model.getOntResource(THING);
	}
}
