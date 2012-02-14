package edu.utep.trustlab.visko.ontology.vocabulary;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.Ontology;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class RDFS {
	public static final String ONTOLOGY_RDFS_URI = "http://www.w3.org/2000/01/rdf-schema";

	// RDFS Properties
	public static final String PROPERTY_URI_RDFS_COMMENT = ONTOLOGY_RDFS_URI
			+ "#comment";
	public static final String PROPERTY_URI_RDFS_LABEL = ONTOLOGY_RDFS_URI
			+ "#label";

	// model and ontology
	private static OntModel model;
	private static Ontology ontology;

	static {
		model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
		model.read(ONTOLOGY_RDFS_URI);
		ontology = model.getOntology(ONTOLOGY_RDFS_URI);
	}

	public static OntModel getModel() {
		return model;
	}

	public static Ontology getOntology() {
		return ontology;
	}
}
