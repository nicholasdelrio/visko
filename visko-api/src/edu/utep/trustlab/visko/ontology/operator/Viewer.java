/*
Copyright (c) 2012, University of Texas at El Paso
All rights reserved.
Redistribution and use in source and binary forms, with or without modification, are permitted
provided that the following conditions are met:

	-Redistributions of source code must retain the above copyright notice, this list of conditions
	 and the following disclaimer.
	-Redistributions in binary form must reproduce the above copyright notice, this list of conditions
	 and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.*/


/*
Copyright (c) 2012, University of Texas at El Paso
All rights reserved.
Redistribution and use in source and binary forms, with or without modification, are permitted
provided that the following conditions are met:

	-Redistributions of source code must retain the above copyright notice, this list of conditions
	 and the following disclaimer.
	-Redistributions in binary form must reproduce the above copyright notice, this list of conditions
	 and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.*/


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
