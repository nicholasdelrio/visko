package edu.utep.trustlab.visko.ontology.vocabulary.supplemental;


import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class OWL {
	
	private static final String ONTOLOGY_OWL_URI = "http://www.w3.org/2002/07/owl";
	
	// Classes
	public static final String CLASS_URI_Thing = ONTOLOGY_OWL_URI + "#Thing";
	
	private static OntModel model;
	
	static {
		model = ModelFactory.createOntologyModel();
		model.read(ONTOLOGY_OWL_URI);
	}
	
	public static OntResource getOWLThing(){
		return model.getOntResource(CLASS_URI_Thing);
	}
}
