package edu.utep.trustlab.visko.ontology.view;

import com.hp.hpl.jena.ontology.Individual;

import edu.utep.trustlab.visko.ontology.model.ViskoModel;
import edu.utep.trustlab.visko.ontology.vocabulary.ViskoV;

public class Network extends AtomicView {
	public Network(String baseURL, String name, ViskoModel viskoModel) {
		super(ViskoV.CLASS_URI_NETWORK, baseURL, name, viskoModel);
	}

	public Network(String classURI, String baseURL, String name,
			ViskoModel viskoModel) {
		super(classURI, baseURL, name, viskoModel);
	}

	public Network(String uri, ViskoModel viskoModel) {
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
