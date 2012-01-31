package trustlab.visko.ontology.vocabulary;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.Ontology;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class ESIPData {

	public static final String ONTOLOGY_ESIP_URI = "http://trust.utep.edu/visko/ontology/esip-data.owl";
	public static final String ONTOLOGY_ESIP_NS = "http://www.ordnancesurvey.co.uk/ontology/Datatypes.owl";

	// Concepts
	public static final String CLASS_ESIP_GEOMETRY = ONTOLOGY_ESIP_NS
			+ "#Geometry";
	public static final String CLASS_ESIP_VOLUME = ONTOLOGY_ESIP_NS + "#Volume";
	public static final String CLASS_ESIP_POINT = ONTOLOGY_ESIP_NS + "#Point";
	public static final String CLASS_ESIP_3DSWATH = ONTOLOGY_ESIP_NS
			+ "#3DSwath";
	public static final String CLASS_ESIP_CURVE = ONTOLOGY_ESIP_NS + "#Curve";
	public static final String CLASS_ESIP_CONTOUR = ONTOLOGY_ESIP_NS
			+ "#Contour";
	public static final String CLASS_ESIP_LINE = ONTOLOGY_ESIP_NS + "#Line";
	public static final String CLASS_ESIP_1DSWATH = ONTOLOGY_ESIP_NS
			+ "#1DSwath";
	public static final String CLASS_ESIP_SURFACE = ONTOLOGY_ESIP_NS
			+ "#Surface";
	public static final String CLASS_ESIP_2DSWATH = ONTOLOGY_ESIP_NS
			+ "#2DSwath";

	/*****************************************************************************************/

	// model and ontology
	private static OntModel model;
	private static Ontology ontology;

	static {
		model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
		model.read(ONTOLOGY_ESIP_URI);
		ontology = model.getOntology(ONTOLOGY_ESIP_URI);
	}

	public static OntModel getModel() {
		return model;
	}

	public static Ontology getOntology() {
		return ontology;
	}
}
