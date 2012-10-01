package edu.utep.trustlab.visko.ontology.viskoOperator;

import com.hp.hpl.jena.ontology.Individual;

import edu.utep.trustlab.visko.ontology.model.ViskoModel;
import edu.utep.trustlab.visko.ontology.vocabulary.ViskoO;

public class Filter extends InputOutputOperator {

	public Filter(String baseURL, String name, ViskoModel viskoModel) {
		super(ViskoO.CLASS_URI_Filter, baseURL, name, viskoModel);
	}
		
	public Filter(String uri, ViskoModel viskoModel) {
		super(uri, viskoModel);
	}
	
	@Override
	protected boolean allFieldsPopulated() {
		return super.allFieldsPopulated();
	}

	@Override
	protected Individual createNewIndividual() {
		return super.createNewIndividual();
	}

	@Override
	protected void setProperties() {
		super.setProperties();
	}

	@Override
	protected void populateFieldsWithIndividual(Individual ind) {
		super.populateFieldsWithIndividual(ind);
	}

	@Override
	protected void initializeFields() {
		super.initializeFields();
	}
}
