package edu.utep.trustlab.visko.ontology.view;

import com.hp.hpl.jena.ontology.Individual;

import edu.utep.trustlab.visko.ontology.model.ViskoModel;
import edu.utep.trustlab.visko.ontology.vocabulary.ESIPData;

public class Point extends Geometry {
	public Point(String uri, ViskoModel viskoModel) {
		super(uri, viskoModel);
	}

	public Point(String baseURL, String name, ViskoModel viskoModel) {
		super(ESIPData.CLASS_ESIP_POINT, baseURL, name, viskoModel);
	}

	@Override
	protected void initializeFields() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void populateFieldsWithIndividual(Individual ind) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setProperties() {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean allFieldsPopulated() {
		// TODO Auto-generated method stub
		return true;
	}

}
