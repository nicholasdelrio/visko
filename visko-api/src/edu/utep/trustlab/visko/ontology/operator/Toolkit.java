package edu.utep.trustlab.visko.ontology.operator;

import com.hp.hpl.jena.ontology.Individual;
import edu.utep.trustlab.visko.ontology.JenaIndividual;
import edu.utep.trustlab.visko.ontology.model.ViskoModel;
import edu.utep.trustlab.visko.ontology.vocabulary.ViskoO;

public class Toolkit extends JenaIndividual {
	public Toolkit(String baseURL, String name, ViskoModel viskoModel) {
		super(ViskoO.CLASS_URI_TOOLKIT, baseURL, name, viskoModel);
	}

	public Toolkit(String uri, ViskoModel viskoModel) {
		super(uri, viskoModel);
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
