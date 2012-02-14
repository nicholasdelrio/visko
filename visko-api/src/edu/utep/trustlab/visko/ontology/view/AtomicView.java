package edu.utep.trustlab.visko.ontology.view;

import com.hp.hpl.jena.ontology.Individual;

import edu.utep.trustlab.visko.ontology.model.ViskoModel;
import edu.utep.trustlab.visko.ontology.vocabulary.ViskoV;

public class AtomicView extends View {
	public AtomicView(String baseURL, String name, ViskoModel viskoModel) {
		super(ViskoV.CLASS_URI_ATOMIC_VIEW, baseURL, name, viskoModel);
	}

	public AtomicView(String classURI, String baseURL, String name,
			ViskoModel viskoModel) {
		super(classURI, baseURL, name, viskoModel);
	}

	public AtomicView(String uri, ViskoModel viskoModel) {
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
