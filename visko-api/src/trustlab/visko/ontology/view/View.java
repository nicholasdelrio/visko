package trustlab.visko.ontology.view;

import com.hp.hpl.jena.ontology.Individual;

import trustlab.visko.ontology.JenaIndividual;
import trustlab.visko.ontology.model.ViskoModel;
import trustlab.visko.ontology.vocabulary.ViskoV;

public class View extends JenaIndividual {
	public View(String baseURL, String name, ViskoModel viskoModel) {
		super(ViskoV.CLASS_URI_VIEW, baseURL, name, viskoModel);
	}

	public View(String classURI, String baseURL, String name,
			ViskoModel viskoModel) {
		super(classURI, baseURL, name, viskoModel);
	}

	public View(String uri, ViskoModel viskoModel) {
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
