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

import java.util.Vector;

import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.RDFNode;

import edu.utep.trustlab.visko.ontology.JenaIndividual;
import edu.utep.trustlab.visko.ontology.model.ViskoModel;
import edu.utep.trustlab.visko.ontology.pmlp.Format;
import edu.utep.trustlab.visko.ontology.vocabulary.OWL;
import edu.utep.trustlab.visko.ontology.vocabulary.PMLP;
import edu.utep.trustlab.visko.ontology.vocabulary.ViskoO;
import edu.utep.trustlab.visko.ontology.vocabulary.XSD;

public class Operator extends JenaIndividual {
	private Vector<Format> inputFormats;
	private Vector<OntResource> inputDataTypes;
	
	private String name;

	// properties
	private ObjectProperty operatesOnProperty;
	private ObjectProperty operatesOnDataTypeProperty;
	
	// datatype properties
	private DatatypeProperty hasNameProperty;

	public Operator(String classURI, String baseURL, String name,
			ViskoModel viskoModel) {
		super(classURI, baseURL, name, viskoModel);
	}

	public Operator(String conceptURI, ViskoModel viskoModel) {
		super(conceptURI, viskoModel);
	}

	public void setName(String aName) {
		name = aName;
	}

	public String getName() {
		return name;
	}

	public void setOperatesOnFormats(Vector<Format> inFormats) {
		inputFormats = inFormats;
	}
	
	public void setOperatesOnDataTypes(Vector<String> inDataTypeURIs) {
		
		inputDataTypes.removeAllElements();
		
		for(String inDataTypeURI : inDataTypeURIs)
			inputDataTypes.add(model.getOntResource(inDataTypeURI));		
	}

	public void addOperatesOnFormat(Format inFormat) {
		inputFormats.add(inFormat);
	}
	
	public void addOperatesOnDataType(String inDataTypeURI) {
		inputDataTypes.add(model.getOntResource(inDataTypeURI));
	}	

	public Vector<Format> getOperatesOnFormats() {
		return inputFormats;
	}

	public Vector<OntResource> getOperatesOnDataTypes() {
		return inputDataTypes;
	}
	
	private void addOperatesOnFormatProperties(Individual subjectInd) {
		for (Format format : inputFormats) {
			subjectInd.addProperty(operatesOnProperty, format.getIndividual());
		}		
	}

	private void addOperatesOnDataTypeProperties(Individual subjectInd) {
		
		if(inputDataTypes == null || inputDataTypes.size() == 0)
			subjectInd.addProperty(operatesOnDataTypeProperty, OWL.getOWLThing());
		else
			for (OntResource dataType : inputDataTypes)
				subjectInd.addProperty(operatesOnDataTypeProperty, dataType);
	}
	
	private void addHasNameProperty(Individual subjectInd) {
		Literal stringName = model.createTypedLiteral(name, XSD.TYPE_URI_STRING);
		subjectInd.addProperty(hasNameProperty, stringName);
	}

	@Override
	protected boolean allFieldsPopulated() {
		return inputFormats.size() > 0 && name != null;
	}

	@Override
	protected void populateFieldsWithIndividual(Individual ind) {
		NodeIterator fmts = ind.listPropertyValues(operatesOnProperty);
		Format fmt;
		while (fmts.hasNext()) {
			fmt = new Format(fmts.next().as(Individual.class).getURI(), model);
			inputFormats.add(fmt);
		}

		System.out.println("finished loading formats...");
		
		NodeIterator dTypes = ind.listPropertyValues(operatesOnDataTypeProperty);
		while (dTypes.hasNext())
			inputDataTypes.add(dTypes.next().as(Individual.class));

		System.out.println("finished loading data types...");
		
		RDFNode theName = ind.getPropertyValue(hasNameProperty);
		if (theName != null)
			name = (String) theName.as(Literal.class).getValue();		
	}

	@Override
	protected Individual createNewIndividual() {
		Individual ind = super.createNewIndividual();
		this.addOperatesOnFormatProperties(ind);
		this.addOperatesOnDataTypeProperties(ind);
		this.addHasNameProperty(ind);
		return ind;
	}

	@Override
	protected void setProperties() {
		operatesOnProperty = model.getObjectProperty(ViskoO.PROPERTY_URI_OPERATESON);
		operatesOnDataTypeProperty = model.getObjectProperty(ViskoO.PROPERTY_URI_OPERATESON_DATATYPE);
		hasNameProperty = model.getDatatypeProperty(PMLP.DATATYPE_PROPERTY_URI_PMLP_HASNAME);
	}

	@Override
	protected void initializeFields() {
		inputFormats = new Vector<Format>();
		inputDataTypes = new Vector<OntResource>();
	}
}
