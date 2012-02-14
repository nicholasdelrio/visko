package edu.utep.trustlab.visko.ontology.vocabulary;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.Ontology;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class PMLP {
	public static final String ONTOLOGY_PMLP_URI = "http://inference-web.org/2.0/pml-provenance.owl";

	// PMLP Ontology Concepts
	public static final String CLASS_URI_PMLP_FORMAT = ONTOLOGY_PMLP_URI
			+ "#Format";
	public static final String CLASS_URI_PMLP_LANGUAGE = ONTOLOGY_PMLP_URI
			+ "#Language";

	// PMLP Ontology Properties
	public static final String DATATYPE_PROPERTY_URI_PMLP_HASNAME = ONTOLOGY_PMLP_URI
			+ "#hasName";

	// model and ontology
	private static OntModel model;
	private static Ontology ontology;

	static {
		model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
		model.read(ONTOLOGY_PMLP_URI);
		ontology = model.getOntology(ONTOLOGY_PMLP_URI);
	}

	public static OntModel getModel() {
		return model;
	}

	public static Ontology getOntology() {
		return ontology;
	}
}
