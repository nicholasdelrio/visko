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

package edu.utep.trustlab.visko.ontology.viskoService;

import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.RDFNode;
import edu.utep.trustlab.visko.ontology.JenaIndividual;
import edu.utep.trustlab.visko.ontology.model.ViskoModel;
import edu.utep.trustlab.visko.ontology.vocabulary.supplemental.OWLS_Process;

public class InputBinding extends JenaIndividual {
	private Input parameter;
	private String value;

	private ObjectProperty toParam;
	private DatatypeProperty valueData;

	public InputBinding(String baseURL, String name, ViskoModel viskoModel) {
		super(OWLS_Process.CLASS_URI_InputBinding, baseURL, name, viskoModel);
	}

	public InputBinding(String uri, ViskoModel viskoModel) {
		super(uri, viskoModel);
	}

	public void setInputParameter(Input in) {
		parameter = in;
	}

	public Input getInputParameter() {
		return parameter;
	}

	public void setValueData(String data) {
		value = data;
	}

	public String getValueData() {
		return value;
	}

	private void addInputParameter(Individual subjectInd) {
		subjectInd.addProperty(toParam, parameter.getURI());
	}

	private void addValueData(Individual subjectInd) {
		Literal valueDataLit = model.createLiteral(value);
		subjectInd.addProperty(valueData, valueDataLit);
	}

	@Override
	protected Individual createNewIndividual() {
		Individual ind = super.createNewIndividual();
		this.addInputParameter(ind);
		this.addValueData(ind);
		return ind;
	}

	@Override
	protected void initializeFields() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void populateFieldsWithIndividual(Individual ind) {
		// TODO Auto-generated method stub

		// populate parameter
		RDFNode inputParameterInd = ind.getPropertyValue(toParam);
		parameter = new Input(inputParameterInd.as(Individual.class).getURI(), model);

		// populate value
		RDFNode valueDataInd = ind.getPropertyValue(valueData);
		value = (String) valueDataInd.as(Literal.class).getValue();

	}

	@Override
	protected void setProperties() {
		// TODO Auto-generated method stub
		toParam = model.getObjectProperty(OWLS_Process.PROPERTY_URI_toParam);
		valueData = model.getDatatypeProperty(OWLS_Process.PROPERTY_URI_valueData);
	}

	@Override
	protected boolean allFieldsPopulated() {
		// TODO Auto-generated method stub
		return parameter != null && value != null;
	}
}
