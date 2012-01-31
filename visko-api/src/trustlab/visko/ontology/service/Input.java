package trustlab.visko.ontology.service;

import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.RDFNode;

import trustlab.visko.ontology.JenaIndividual;
import trustlab.visko.ontology.model.ViskoModel;
import trustlab.visko.ontology.vocabulary.OWLS_Process;

public class Input extends JenaIndividual {
	private String parameterType;

	private DatatypeProperty parameterTypeProperty;

	public Input(String baseURL, String name, ViskoModel viskoModel) {
		super(OWLS_Process.CLASS_URI_INPUT, baseURL, name, viskoModel);
	}

	public Input(String uri, ViskoModel viskoModel) {
		super(uri, viskoModel);
	}

	@Override
	protected void initializeFields() {
		// TODO Auto-generated method stub
	}

	@Override
	protected void populateFieldsWithIndividual(Individual ind) {
		// TODO Auto-generated method stub
		RDFNode parameterTypeLit = ind.getPropertyValue(parameterTypeProperty);
		parameterType = (String) parameterTypeLit.as(Literal.class).getValue();
	}

	@Override
	protected void setProperties() {
		// TODO Auto-generated method stub
		parameterTypeProperty = model
				.getDatatypeProperty(OWLS_Process.PROPERTY_URI_PARAM_TYPE);
	}

	@Override
	protected boolean allFieldsPopulated() {
		// TODO Auto-generated method stub
		return parameterType != null;
	}
}
