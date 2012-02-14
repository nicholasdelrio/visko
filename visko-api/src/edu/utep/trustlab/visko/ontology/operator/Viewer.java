package edu.utep.trustlab.visko.ontology.operator;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.rdf.model.NodeIterator;

import edu.utep.trustlab.visko.ontology.model.ViskoModel;
import edu.utep.trustlab.visko.ontology.view.View;
import edu.utep.trustlab.visko.ontology.vocabulary.ViskoO;

import java.util.Vector;

public class Viewer extends Operator {
	View viewToPresent;
	Vector<ViewerSet> belongsToSets;

	ObjectProperty presentsViewProperty;
	ObjectProperty addPartOfViewerSetProperty;

	public Viewer(String baseURL, String name, ViskoModel viskoModel) {
		super(ViskoO.CLASS_URI_VIEWER, baseURL, name, viskoModel);
	}

	public Viewer(String uri, ViskoModel viskoModel) {
		super(uri, viskoModel);
	}

	public void setBelongsToViewerSets(Vector<ViewerSet> sets) {
		belongsToSets = sets;
	}

	public void addBelongsToViewerSet(ViewerSet set) {
		belongsToSets.add(set);
	}

	public Vector<ViewerSet> getBelongsToViewerSets() {
		return belongsToSets;
	}

	public void setViewToPresent(View viewToPresent) {
		this.viewToPresent = viewToPresent;
	}

	private void addPresentsViewProperty(Individual subjectInd) {
		if (viewToPresent != null)
			subjectInd.addProperty(presentsViewProperty,
					viewToPresent.getIndividual());
	}

	private void addPartOfViewerSetProperty(Individual subjectInd) {
		for (ViewerSet set : belongsToSets)
			subjectInd.addProperty(addPartOfViewerSetProperty,
					set.getIndividual());
	}

	@Override
	protected boolean allFieldsPopulated() {
		if (super.allFieldsPopulated() && belongsToSets.size() > 0 /*
																	 * &&
																	 * viewToPresent
																	 * != null
																	 */)
			return true;
		return false;
	}

	@Override
	protected Individual createNewIndividual() {
		Individual ind = super.createNewIndividual();

		this.addPartOfViewerSetProperty(ind);
		this.addPresentsViewProperty(ind);

		return ind;
	}

	@Override
	protected void setProperties() {
		super.setProperties();
		presentsViewProperty = model
				.getObjectProperty(ViskoO.PROPERTY_URI_PRESENTSVIEW);
		addPartOfViewerSetProperty = model
				.getObjectProperty(ViskoO.PROPERTY_URI_PART_OF_VIEWERSET);
	}

	@Override
	protected void populateFieldsWithIndividual(Individual ind) {
		super.populateFieldsWithIndividual(ind);

		NodeIterator viewerSets = ind
				.listPropertyValues(addPartOfViewerSetProperty);
		ViewerSet vSet;
		while (viewerSets.hasNext()) {
			vSet = new ViewerSet(viewerSets.next().as(Individual.class)
					.getURI(), model);
			belongsToSets.add(vSet);
		}
	}

	@Override
	protected void initializeFields() {
		super.initializeFields();
		belongsToSets = new Vector<ViewerSet>();
	}
}
