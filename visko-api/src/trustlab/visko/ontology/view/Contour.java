package trustlab.visko.ontology.view;

import com.hp.hpl.jena.ontology.Individual;

import trustlab.visko.ontology.model.ViskoModel;
import trustlab.visko.ontology.vocabulary.ESIPData;

public class Contour extends Curve {
	public Contour(String uri, ViskoModel viskoModel) {
		super(uri, viskoModel);
	}

	public Contour(String baseURL, String name, ViskoModel viskoModel) {
		super(ESIPData.CLASS_ESIP_CONTOUR, baseURL, name, viskoModel);
		// TODO Auto-generated constructor stub
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
