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


package edu.utep.trustlab.visko.ontology.service;

import java.util.Vector;

import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.NodeIterator;

import edu.utep.trustlab.visko.ontology.model.ViskoModel;
import edu.utep.trustlab.visko.ontology.JenaIndividual;
import edu.utep.trustlab.visko.ontology.vocabulary.ViskoS;
import edu.utep.trustlab.visko.ontology.vocabulary.supplemental.XSD;

public class InputParameterBindings extends JenaIndividual {
	private Vector<String> types;
	private Vector<InputBinding> bindings;

	private ObjectProperty declaresBindings;
	private DatatypeProperty profiles;

	public InputParameterBindings(String baseURL, String name, ViskoModel viskoModel) {
		super(ViskoS.CLASS_URI_InputParameterBindings, baseURL, name, viskoModel);
	}

	public InputParameterBindings(String uri, ViskoModel viskoModel) {
		super(uri, viskoModel);
	}

	public void addInputBinding(InputBinding ib) {
		bindings.add(ib);
	}

	public void setInputBindings(Vector<InputBinding> ibs) {
		bindings = ibs;
	}

	public Vector<InputBinding> getInputBindings() {
		return bindings;
	}

	public void setProfiledTypes(Vector<String> someTypes){
		types = someTypes;
	}
	
	public void addProfileType(String dataType) {
		types.add(dataType);
	}

	public Vector<String> getProfiledTypes() {
		return types;
	}

	private void addProfiles(Individual subjectInd) {
		for (String type : types) {
			Literal dataTypeLiteral = model.createTypedLiteral(type, XSD.DATATYPE_URI_ANYURI);
			subjectInd.addProperty(profiles, dataTypeLiteral);
		}
	}

	private void addDeclaresBindings(Individual subjectInd) {
		for (InputBinding ib : bindings) {
			subjectInd.addProperty(declaresBindings, ib.getIndividual());
		}
	}

	@Override
	public Individual createNewIndividual() {
		Individual subjectInd = super.createNewIndividual();
		this.addDeclaresBindings(subjectInd);
		this.addProfiles(subjectInd);

		return subjectInd;
	}

	@Override
	protected void populateFieldsWithIndividual(Individual ind) {
		
		// populate types
		NodeIterator semanticTypes = ind.listPropertyValues(profiles);
		while (semanticTypes.hasNext())
			types.add((String) semanticTypes.next().as(Literal.class).getValue());

		// populate bindings
		NodeIterator inBindings = ind.listPropertyValues(declaresBindings);
		while (inBindings.hasNext())
			bindings.add(new InputBinding(inBindings.next().as(Individual.class).getURI(), model));
	}

	@Override
	protected void setProperties() {
		declaresBindings = model.getObjectProperty(ViskoS.PROPERTY_URI_declaresBindings);
		profiles = model.getDatatypeProperty(ViskoS.DATATYPE_PROPERTY_URI_profiles);
	}

	@Override
	protected void initializeFields() {
		bindings = new Vector<InputBinding>();
		types = new Vector<String>();
	}

	@Override
	protected boolean allFieldsPopulated() {
		return bindings.size() > 0 && types.size() > 0;
	}
}