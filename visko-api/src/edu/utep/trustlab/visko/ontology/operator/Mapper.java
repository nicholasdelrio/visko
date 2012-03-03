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

import edu.utep.trustlab.visko.ontology.model.ViskoModel;
import edu.utep.trustlab.visko.ontology.view.View;
import edu.utep.trustlab.visko.ontology.vocabulary.ViskoO;

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
