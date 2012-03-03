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


package edu.utep.trustlab.visko.ontology.service;

import java.util.Vector;

import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.RDFNode;

import edu.utep.trustlab.visko.ontology.model.ViskoModel;
import edu.utep.trustlab.visko.ontology.vocabulary.XSD;
import edu.utep.trustlab.visko.ontology.JenaIndividual;
import edu.utep.trustlab.visko.ontology.vocabulary.ViskoS;

public class ToolkitProfile extends JenaIndividual {
	private Toolkit toolkit;
	private Vector<String> profiledDataTypes;
	private Vector<InputBinding> inputBindings;

	// properties
	private ObjectProperty basedOnProperty;
	private ObjectProperty declaresBindingsProperty;
	private DatatypeProperty profilesProperty;

	public ToolkitProfile(String baseURL, String name, ViskoModel viskoModel) {
		super(ViskoS.CLASS_URI_TOOLKIT_PROFILE, baseURL, name, viskoModel);
	}

	public ToolkitProfile(String uri, ViskoModel viskoModel) {
		super(uri, viskoModel);
	}

	public void addInputBinding(InputBinding ib) {
		inputBindings.add(ib);
	}

	public void setInputBindings(Vector<InputBinding> ibs) {
		inputBindings = ibs;
	}

	public Vector<InputBinding> getInputBindings() {
		return inputBindings;
	}

	public void setToolkit(Toolkit tk) {
		toolkit = tk;
	}

	public Toolkit getToolkit() {
		return toolkit;
	}

	public void addProfiledDataType(String dataType) {
		profiledDataTypes.add(dataType);
	}

	public Vector<String> getProfiledDataTypes() {
		return profiledDataTypes;
	}

	private void addbasedOnToolkitProperty(Individual subjectInd) {
		subjectInd.addProperty(basedOnProperty, toolkit.getIndividual());
	}

	private void addProfilesProperty(Individual subjectInd) {
		for (String type : profiledDataTypes) {
			System.out.println("adding profiled type....");
			Literal dataType = model.createTypedLiteral(type,
					XSD.TYPE_URI_ANYURI);
			subjectInd.addProperty(profilesProperty, dataType);
		}
	}

	private void addDeclaresBindingsProperty(Individual subjectInd) {
		for (InputBinding ib : inputBindings) {
			subjectInd
					.addProperty(declaresBindingsProperty, ib.getIndividual());
		}
	}

	@Override
	public Individual createNewIndividual() {
		Individual subjectInd = super.createNewIndividual();
		this.addbasedOnToolkitProperty(subjectInd);
		this.addDeclaresBindingsProperty(subjectInd);
		this.addProfilesProperty(subjectInd);

		return subjectInd;
	}

	@Override
	protected void populateFieldsWithIndividual(Individual ind) {
		NodeIterator iterator = ind.listPropertyValues(profilesProperty);
		RDFNode dataType;
		String profiledDataType;
		while (iterator.hasNext()) {
			dataType = iterator.next();
			profiledDataType = (String) dataType.as(Literal.class).getValue();
			profiledDataTypes.add(profiledDataType);
		}

		RDFNode tkInd = ind.getPropertyValue(basedOnProperty);
		toolkit = new Toolkit(tkInd.as(Individual.class).getURI(), model);

		NodeIterator ibInds = ind.listPropertyValues(declaresBindingsProperty);
		String ibURI;
		while (ibInds.hasNext()) {
			ibURI = ibInds.next().as(Individual.class).getURI();
			inputBindings.add(new InputBinding(ibURI, model));
		}
	}

	@Override
	protected void setProperties() {
		basedOnProperty = model.getObjectProperty(ViskoS.PROPERTY_URI_BASED_ON);
		declaresBindingsProperty = model
				.getObjectProperty(ViskoS.PROPERTY_URI_DECLARES_BINDINGS);
		profilesProperty = model
				.getDatatypeProperty(ViskoS.DATATYPE_PROPERTY_URI_PROFILES);
	}

	@Override
	protected void initializeFields() {
		inputBindings = new Vector<InputBinding>();
		profiledDataTypes = new Vector<String>();
	}

	@Override
	protected boolean allFieldsPopulated() {
		return toolkit != null && inputBindings.size() > 0
				&& profiledDataTypes.size() > 0;
	}
}
