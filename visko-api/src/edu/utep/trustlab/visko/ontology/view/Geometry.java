package edu.utep.trustlab.visko.ontology.view;

import com.hp.hpl.jena.ontology.Individual;

import edu.utep.trustlab.visko.ontology.model.ViskoModel;
import edu.utep.trustlab.visko.ontology.vocabulary.ESIPData;

public class Geometry extends AtomicView {
	public Geometry(String classURI, String baseURL, String name,
			ViskoModel viskoModel) {
		super(classURI, baseURL, name, viskoModel);
	}

	public Geometry(String baseURL, String name, ViskoModel viskoModel) {
		super(ESIPData.CLASS_ESIP_GEOMETRY, baseURL, name, viskoModel);
	}

	public Geometry(String uri, ViskoModel viskoModel) {
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
