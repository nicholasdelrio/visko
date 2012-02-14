package edu.utep.trustlab.visko.ontology.service;

import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.RDFNode;
import edu.utep.trustlab.visko.ontology.JenaIndividual;
import edu.utep.trustlab.visko.ontology.model.ViskoModel;
import edu.utep.trustlab.visko.ontology.vocabulary.OWLS_Process;

public class InputBinding extends JenaIndividual {
	private Input inputParameter;
	private String valueData;

	private ObjectProperty toParamProperty;
	private DatatypeProperty valueDataProperty;

	public InputBinding(String baseURL, String name, ViskoModel viskoModel) {
		super(OWLS_Process.CLASS_URI_INPUT_BINDING, baseURL, name, viskoModel);
	}

	public InputBinding(String uri, ViskoModel viskoModel) {
		super(uri, viskoModel);
	}

	public void setInputParameter(Input in) {
		inputParameter = in;
	}

	public Input getInputParameter() {
		return inputParameter;
	}

	public void setValueData(String data) {
		valueData = data;
	}

	public String getValueData() {
		return valueData;
	}

	private void addInputParameterProperty(Individual subjectInd) {
		subjectInd.addProperty(toParamProperty, inputParameter.getURI());
	}

	private void addValueDataProperty(Individual subjectInd) {
		Literal valueDataLit = model.createLiteral(valueData);
		subjectInd.addProperty(valueDataProperty, valueDataLit);
	}

	@Override
	protected Individual createNewIndividual() {
		Individual ind = super.createNewIndividual();
		this.addInputParameterProperty(ind);
		this.addValueDataProperty(ind);
		return ind;
	}

	@Override
	protected void initializeFields() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void populateFieldsWithIndividual(Individual ind) {
		// TODO Auto-generated method stub

		RDFNode inputParameterInd = ind.getPropertyValue(toParamProperty);
		inputParameter = new Input(inputParameterInd.as(Individual.class)
				.getURI(), model);

		RDFNode valueDataInd = ind.getPropertyValue(valueDataProperty);
		valueData = (String) valueDataInd.as(Literal.class).getValue();

	}

	@Override
	protected void setProperties() {
		// TODO Auto-generated method stub
		toParamProperty = model
				.getObjectProperty(OWLS_Process.PROPERTY_URI_TO_PARAM);
		valueDataProperty = model
				.getDatatypeProperty(OWLS_Process.PROPERTY_URI_VALUE_DATA);
	}

	@Override
	protected boolean allFieldsPopulated() {
		// TODO Auto-generated method stub
		return inputParameter != null && valueData != null;
	}
}
