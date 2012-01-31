package trustlab.visko.ontology.operator;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;

import trustlab.visko.ontology.model.ViskoModel;
import trustlab.visko.ontology.pmlp.Format;
import trustlab.visko.ontology.vocabulary.ViskoO;

public class Transformer extends Operator {
	private Format outputFormat;

	private ObjectProperty transformsToFormatProperty;

	public Transformer(String classURI, String baseURL, String name,
			ViskoModel viskoModel) {
		super(classURI, baseURL, name, viskoModel);
	}

	public Transformer(String baseURL, String name, ViskoModel viskoModel) {
		super(ViskoO.CLASS_URI_TRANSFORMER, baseURL, name, viskoModel);
	}

	public Transformer(String uri, ViskoModel viskoModel) {
		super(uri, viskoModel);
	}

	public void setTransformsToFormat(Format outFormat) {
		outputFormat = outFormat;
	}

	public Format getTransformsToFormat() {
		return outputFormat;
	}

	private void addTransformsToFormatProperty(Individual subjectInd) {
		subjectInd.addProperty(transformsToFormatProperty,
				outputFormat.getIndividual());
	}

	@Override
	protected boolean allFieldsPopulated() {
		if (super.allFieldsPopulated() && outputFormat != null)
			return true;
		return false;
	}

	@Override
	protected Individual createNewIndividual() {
		Individual ind = super.createNewIndividual();
		this.addTransformsToFormatProperty(ind);
		return ind;
	}

	@Override
	protected void setProperties() {
		super.setProperties();
		transformsToFormatProperty = model
				.getObjectProperty(ViskoO.PROPERTY_URI_TRANSFORMS_TO);
	}

	@Override
	protected void populateFieldsWithIndividual(Individual ind) {
		super.populateFieldsWithIndividual(ind);
		Format fmt = new Format(ind
				.getPropertyValue(transformsToFormatProperty)
				.as(Individual.class).getURI(), model);
		outputFormat = fmt;
	}

	@Override
	protected void initializeFields() {
		super.initializeFields();
	}
}
