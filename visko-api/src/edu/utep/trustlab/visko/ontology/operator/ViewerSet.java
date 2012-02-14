package edu.utep.trustlab.visko.ontology.operator;

import edu.utep.trustlab.visko.ontology.JenaIndividual;
import edu.utep.trustlab.visko.ontology.model.ViskoModel;
import edu.utep.trustlab.visko.ontology.vocabulary.ViskoO;

import com.hp.hpl.jena.ontology.Individual;

public class ViewerSet extends JenaIndividual {
	public ViewerSet(String baseURL, String name, ViskoModel viskoModel) {
		super(ViskoO.CLASS_URI_VIEWERSET, baseURL, name, viskoModel);
	}

	public ViewerSet(String uri, ViskoModel viskoModel) {
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
