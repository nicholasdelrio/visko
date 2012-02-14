package edu.utep.trustlab.visko.ontology.vocabulary;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.Ontology;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class ViskoV {
	public static final String ONTOLOGY_VISKO_V_URI = "http://trust.utep.edu/visko/ontology/visko-view-v3.owl";

	// Concepts
	public static final String CLASS_URI_VIEW = ONTOLOGY_VISKO_V_URI + "#View";
	public static final String CLASS_URI_COMPOSITE_VIEW = ONTOLOGY_VISKO_V_URI
			+ "#CompositeView";
	public static final String CLASS_URI_ATOMIC_VIEW = ONTOLOGY_VISKO_V_URI
			+ "#AtomicView";
	public static final String CLASS_URI_NETWORK = ONTOLOGY_VISKO_V_URI
			+ "#Network";
	public static final String CLASS_URI_GRAPH = ONTOLOGY_VISKO_V_URI
			+ "#Graph";
	public static final String CLASS_URI_TREE = ONTOLOGY_VISKO_V_URI + "#Tree";
	public static final String CLASS_URI_RASTER = ONTOLOGY_VISKO_V_URI
			+ "#Raster";

	/*****************************************************************************************/

	// Properties
	public static final String PROPERTY_URI_INCLUDES_VIEW = ONTOLOGY_VISKO_V_URI
			+ "#includesView";

	// model and ontology
	private static OntModel model;
	private static Ontology ontology;

	static {
		model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
		model.read(ONTOLOGY_VISKO_V_URI);
		ontology = model.getOntology(ONTOLOGY_VISKO_V_URI);
	}

	public static OntModel getModel() {
		return model;
	}

	public static Ontology getOntology() {
		return ontology;
	}
}
