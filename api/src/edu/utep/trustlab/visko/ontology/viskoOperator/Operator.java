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

package edu.utep.trustlab.visko.ontology.viskoOperator;

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
import edu.utep.trustlab.visko.ontology.viskoView.VisualizationAbstraction;
import edu.utep.trustlab.visko.ontology.vocabulary.ViskoO;
import edu.utep.trustlab.visko.ontology.vocabulary.supplemental.OWL;
import edu.utep.trustlab.visko.ontology.vocabulary.supplemental.PMLP;
import edu.utep.trustlab.visko.ontology.vocabulary.supplemental.XSD;

public class Operator extends JenaIndividual {
	
	private Vector<Format> inputFormats;
	private Vector<OntResource> inputDataTypes;	
	protected VisualizationAbstraction visualizationAbstraction;
	private String name;

	// Object Properties
	private ObjectProperty hasInputFormat;	
	private ObjectProperty hasInputDataType;	
	private ObjectProperty mapsTo;
	
	// DataType properties
	private DatatypeProperty hasName;
	
	public Operator(String classURI, String baseURL, String name, ViskoModel viskoModel) {
		super(classURI, baseURL, name, viskoModel);
	}

	public Operator(String conceptURI, ViskoModel viskoModel) {
		super(conceptURI, viskoModel);
	}
	
	public void setVisualizationAbstraction(VisualizationAbstraction aView) {
		visualizationAbstraction = aView;
	}

	public VisualizationAbstraction getVisualizationAbstraction() {
		return visualizationAbstraction;
	}

	public void setName(String aName) {
		name = aName;
	}

	public String getName() {
		return name;
	}
	
	public void addInputFormat(Format inFormat) {
		inputFormats.add(inFormat);
	}
		
	public Vector<Format> getInputFormats(){
		return inputFormats;
	}
		
	public void addInputDataType(OntResource inDataType) {
		inputDataTypes.add(inDataType);
	}	
		
	public Vector<OntResource> getInputDataTypes(){
		return inputDataTypes;
	}
	
	private void addMapsToView(Individual subjectInd) {
		if(visualizationAbstraction != null)
			subjectInd.addProperty(mapsTo, visualizationAbstraction.getIndividual());
	}
	
	private void addHasInputFormat(Individual subjectInd) {
		for(Format inputFormat : inputFormats){
			subjectInd.addProperty(hasInputFormat, inputFormat.getIndividual());
		}
	}
	
	private void addHasInputDataType(Individual subjectInd) {
		if(inputDataTypes.size() == 0)
			subjectInd.addProperty(hasInputDataType, OWL.getOWLThing());
		else
			for(OntResource inputDataType : inputDataTypes)
				subjectInd.addProperty(hasInputDataType, inputDataType);
	}
	
	private void addHasName(Individual subjectInd) {
		Literal literalName = model.createTypedLiteral(name, XSD.DATATYPE_URI_STRING);
		subjectInd.addProperty(hasName, literalName);
	}

	@Override
	protected boolean allFieldsPopulated() {
		boolean hasInputFormats = inputFormats.size() > 0;
		return hasInputFormats && name != null;
	}

	@Override
	protected void populateFieldsWithIndividual(Individual ind) {
		// populate input formats
		NodeIterator inFormats = ind.listPropertyValues(hasInputFormat);
		while(inFormats.hasNext())
			inputFormats.add(new Format(inFormats.next().as(Individual.class).getURI(), model));
	
		// populate input data type
		NodeIterator inDataTypes = ind.listPropertyValues(hasInputDataType);
		while(inDataTypes.hasNext())
			inputDataTypes.add(inDataTypes.next().as(OntResource.class));

		// populate visualization abstraction generated
		RDFNode visualizationAbstractionNode = ind.getPropertyValue(mapsTo);
		if(visualizationAbstractionNode != null)
			visualizationAbstraction = new VisualizationAbstraction(visualizationAbstractionNode.as(Individual.class).getURI(), model);

		
		RDFNode theName = ind.getPropertyValue(hasName);
		name = (String) theName.as(Literal.class).getValue();		
	}

	@Override
	protected Individual createNewIndividual() {
		Individual ind = super.createNewIndividual();
		
		// add input/output formats
		this.addHasInputFormat(ind);
		
		// add input/output data types
		this.addHasInputDataType(ind);

		// add visualization abstraction
		this.addMapsToView(ind);
		
		// add name
		this.addHasName(ind);
		
		return ind;
	}

	@Override
	protected void setProperties() {
		hasInputFormat = model.getObjectProperty(ViskoO.PROPERTY_URI_hasInputFormat);		
		hasInputDataType = model.getObjectProperty(ViskoO.PROPERTY_URI_hasInputDataType);
		hasName = model.getDatatypeProperty(PMLP.DATATYPE_PROPERTY_URI_hasName);
		mapsTo = model.getObjectProperty(ViskoO.PROPERTY_URI_mapsTo);
	}

	@Override
	protected void initializeFields() {
		inputDataTypes = new Vector<OntResource>();
		inputFormats = new Vector<Format>();
	}
}
