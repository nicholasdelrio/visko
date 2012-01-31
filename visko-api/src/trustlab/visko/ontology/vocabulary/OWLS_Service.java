package trustlab.visko.ontology.vocabulary;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.Ontology;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class OWLS_Service {
	public static final String ONTOLOGY_OWLS_SERVICE_URI = "http://www.daml.org/services/owl-s/1.2/Service.owl";

	// OWLS Ontology Concepts
	public static final String CLASS_URI_Service = ONTOLOGY_OWLS_SERVICE_URI
			+ "#Service";

	// model and ontology
	private static OntModel model;
	private static Ontology ontology;

	static {
		model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
		model.read(ONTOLOGY_OWLS_SERVICE_URI + "#");
		ontology = model.getOntology(ONTOLOGY_OWLS_SERVICE_URI);
	}

	public static OntModel getModel() {
		return model;
	}

	public static Ontology getOntology() {
		return ontology;
	}
}
