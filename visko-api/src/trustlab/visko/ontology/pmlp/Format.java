package trustlab.visko.ontology.pmlp;

import com.hp.hpl.jena.ontology.Individual;

import trustlab.visko.ontology.JenaIndividual;
import trustlab.visko.ontology.model.ViskoModel;
import trustlab.visko.ontology.vocabulary.PMLP;

public class Format extends JenaIndividual {
	public Format(String baseURL, String name, ViskoModel viskoModel) {
		super(PMLP.CLASS_URI_PMLP_FORMAT, baseURL, name, viskoModel);
	}

	public Format(String uri, ViskoModel viskoModel) {
		super(uri, viskoModel);
	}

	public String getURI(String uri) {
		return uri;
	}

	@Override
	protected Individual createNewIndividual() {
		// TODO Auto-generated method stub
		return null;
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
