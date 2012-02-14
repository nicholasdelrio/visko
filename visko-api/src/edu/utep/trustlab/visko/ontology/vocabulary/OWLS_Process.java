package edu.utep.trustlab.visko.ontology.vocabulary;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.Ontology;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class OWLS_Process {
	public static final String ONTOLOGY_OWLS_PROCESS_URI = "http://www.daml.org/services/owl-s/1.2/Process.owl";

	// OWLS Ontology Concepts
	public static final String CLASS_URI_INPUT = ONTOLOGY_OWLS_PROCESS_URI
			+ "#Input";
	public static final String CLASS_URI_INPUT_BINDING = ONTOLOGY_OWLS_PROCESS_URI
			+ "#InputBinding";

	// OWLS Properties
	public static final String PROPERTY_URI_VALUE_DATA = ONTOLOGY_OWLS_PROCESS_URI
			+ "#valueData";
	public static final String PROPERTY_URI_TO_PARAM = ONTOLOGY_OWLS_PROCESS_URI
			+ "#toParam";
	public static final String PROPERTY_URI_PARAM_TYPE = ONTOLOGY_OWLS_PROCESS_URI
			+ "#parameterType";

	// model and ontology
	private static OntModel model;
	private static Ontology ontology;

	static {
		model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
		model.read(ONTOLOGY_OWLS_PROCESS_URI + "#");
		ontology = model.getOntology(ONTOLOGY_OWLS_PROCESS_URI);
	}

	public static OntModel getModel() {
		return model;
	}

	public static Ontology getOntology() {
		return ontology;
	}
}
