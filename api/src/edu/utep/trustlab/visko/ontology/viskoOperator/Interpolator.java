package edu.utep.trustlab.visko.ontology.viskoOperator;

import com.hp.hpl.jena.ontology.Individual;

import edu.utep.trustlab.visko.ontology.model.ViskoModel;
import edu.utep.trustlab.visko.ontology.vocabulary.ViskoO;

public class Interpolator extends InputOutputOperator {

	public Interpolator(String baseURL, String name, ViskoModel viskoModel) {
		super(ViskoO.CLASS_URI_Interpolator, baseURL, name, viskoModel);
	}
		
	public Interpolator(String uri, ViskoModel viskoModel) {
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
