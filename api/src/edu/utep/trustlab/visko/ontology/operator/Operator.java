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
import edu.utep.trustlab.visko.ontology.vocabulary.ViskoO;
import edu.utep.trustlab.visko.ontology.vocabulary.supplemental.OWL;
import edu.utep.trustlab.visko.ontology.vocabulary.supplemental.PMLP;
import edu.utep.trustlab.visko.ontology.vocabulary.supplemental.XSD;

public class Operator extends JenaIndividual {
	
	private Format inputFormat;
	private Format outputFormat;
	
	private OntResource inputDataType;
	private OntResource outputDataType;
	
	private String name;

	// Object Properties
	private ObjectProperty hasInputFormat;
	private ObjectProperty hasOutputFormat;
	
	private ObjectProperty hasInputDataType;
	private ObjectProperty hasOutputDataType;
	
	// DataType properties
	private DatatypeProperty hasName;

	public Operator(String baseURL, String name, ViskoModel viskoModel) {
		super(ViskoO.CLASS_URI_Operator, baseURL, name, viskoModel);
	}
	
	public Operator(String classURI, String baseURL, String name, ViskoModel viskoModel) {
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

	public void setInputFormat(Format inFormat) {
		inputFormat = inFormat;
	}
	
	public void setOutputFormat(Format outFormat){
		outputFormat = outFormat;
	}
	
	public Format getInputFormat(){
		return inputFormat;
	}
	
	public Format getOutputFormat(){
		return outputFormat;
	}
	
	public void setInputDataType(OntResource inDataType) {
		inputDataType = inDataType;
	}	
	
	public void setOutputDataType(OntResource outDataType){
		outputDataType = outDataType;
	}
	
	public String getInputDataTypeURI(){
		return inputDataType.getURI();
	}
	
	public String getOutputDataTypeURI(){
		return outputDataType.getURI();
	}
	
	private void addHasInputFormat(Individual subjectInd) {
		subjectInd.addProperty(hasInputFormat, inputFormat.getIndividual());		
	}

	private void addHasOutputFormat(Individual subjectInd) {
		subjectInd.addProperty(hasOutputFormat, outputFormat.getIndividual());		
	}
	
	private void addHasInputDataType(Individual subjectInd) {
		
		if(inputDataType == null)
			subjectInd.addProperty(hasInputDataType, OWL.getOWLThing());
		else
			subjectInd.addProperty(hasInputDataType, inputDataType);
	}

	private void addHasOutputDataType(Individual subjectInd) {
		
		if(outputDataType == null)
			subjectInd.addProperty(hasOutputDataType, OWL.getOWLThing());
		else
			subjectInd.addProperty(hasOutputDataType, inputDataType);
	}
	
	private void addHasName(Individual subjectInd) {
		Literal literalName = model.createTypedLiteral(name, XSD.DATATYPE_URI_STRING);
		subjectInd.addProperty(hasName, literalName);
	}

	@Override
	protected boolean allFieldsPopulated() {
		boolean hasInputOutputFormats = inputFormat != null && outputFormat != null;
		boolean hasInputOutputDataTypes = inputDataType != null && outputDataType != null;
		return hasInputOutputFormats && hasInputOutputDataTypes && name != null;
	}

	@Override
	protected void populateFieldsWithIndividual(Individual ind) {
		
		// populate input format
		NodeIterator inFormat = ind.listPropertyValues(hasInputFormat);
		inputFormat = new Format(inFormat.next().as(Individual.class).getURI(), model);

		// populate output format
		NodeIterator outFormat = ind.listPropertyValues(hasOutputFormat);
		outputFormat = new Format(outFormat.next().as(Individual.class).getURI(), model);
		
		// populate input data type
		NodeIterator inDataType = ind.listPropertyValues(hasInputDataType);
		inputDataType = inDataType.next().as(Individual.class);

		// populate input data type
		NodeIterator outDataType = ind.listPropertyValues(hasOutputDataType);
		outputDataType = outDataType.next().as(Individual.class);
				
		RDFNode theName = ind.getPropertyValue(hasName);
		name = (String) theName.as(Literal.class).getValue();		
	}

	@Override
	protected Individual createNewIndividual() {
		Individual ind = super.createNewIndividual();
		
		// add input/output formats
		this.addHasInputFormat(ind);
		this.addHasOutputFormat(ind);
		
		// add input/output data types
		this.addHasInputDataType(ind);
		this.addHasOutputDataType(ind);

		// add name
		this.addHasName(ind);
		
		return ind;
	}

	@Override
	protected void setProperties() {
		hasInputFormat = model.getObjectProperty(ViskoO.PROPERTY_URI_hasInputFormat);
		hasOutputFormat = model.getObjectProperty(ViskoO.PROPERTY_URI_hasOutputFormat);
		
		hasInputDataType = model.getObjectProperty(ViskoO.PROPERTY_URI_hasInputDataType);
		hasOutputDataType = model.getObjectProperty(ViskoO.PROPERTY_URI_hasOutputDataType);
	
		hasName = model.getDatatypeProperty(PMLP.DATATYPE_PROPERTY_URI_hasName);
	}

	@Override
	protected void initializeFields() {
	}
}
