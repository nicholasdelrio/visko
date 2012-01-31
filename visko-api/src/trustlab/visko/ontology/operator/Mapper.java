package trustlab.visko.ontology.operator;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;

import trustlab.visko.ontology.model.ViskoModel;
import trustlab.visko.ontology.view.View;
import trustlab.visko.ontology.vocabulary.ViskoO;

public class Mapper extends Transformer {
	private View mappedToView;

	private ObjectProperty mapsToViewProperty;

	public Mapper(String baseURL, String name, ViskoModel viskoModel) {
		super(ViskoO.CLASS_URI_MAPPER, baseURL, name, viskoModel);
	}

	public Mapper(String uri, ViskoModel viskoModel) {
		super(uri, viskoModel);
	}

	public void setViewToMapTo(View mappedToView) {
		this.mappedToView = mappedToView;
	}

	public View getViewToMapTo() {
		return mappedToView;
	}

	private void addMapsToViewProperty(Individual subjectInd) {
		subjectInd
				.addProperty(mapsToViewProperty, mappedToView.getIndividual());
	}

	@Override
	protected boolean allFieldsPopulated() {
		if (super.allFieldsPopulated() && mappedToView != null)
			return true;
		return false;
	}

	@Override
	protected Individual createNewIndividual() {
		Individual ind = super.createNewIndividual();
		addMapsToViewProperty(ind);
		return ind;
	}

	@Override
	protected void setProperties() {
		super.setProperties();
		mapsToViewProperty = model
				.getObjectProperty(ViskoO.PROPERTY_URI_MAPS_TO);
	}

	@Override
	protected void populateFieldsWithIndividual(Individual ind) {
		super.populateFieldsWithIndividual(ind);
		View view = new View(ind.getPropertyValue(mapsToViewProperty)
				.as(Individual.class).getURI(), model);
		mappedToView = view;
	}

	@Override
	protected void initializeFields() {
		super.initializeFields();
	}
}
