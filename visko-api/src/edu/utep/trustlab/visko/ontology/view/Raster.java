package edu.utep.trustlab.visko.ontology.view;

import com.hp.hpl.jena.ontology.Individual;

import edu.utep.trustlab.visko.ontology.model.ViskoModel;
import edu.utep.trustlab.visko.ontology.vocabulary.ViskoV;

public class Raster extends Network {
	public Raster(String baseURL, String name, ViskoModel viskoModel) {
		super(ViskoV.CLASS_URI_RASTER, baseURL, name, viskoModel);
	}

	public Raster(String uri, ViskoModel viskoModel) {
		super(uri, viskoModel);
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
	protected void initializeFields() {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean allFieldsPopulated() {
		// TODO Auto-generated method stub
		return true;
	}
}
