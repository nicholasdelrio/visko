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
import com.hp.hpl.jena.ontology.OntResource;

import edu.utep.trustlab.visko.ontology.model.ViskoModel;
import edu.utep.trustlab.visko.ontology.pmlp.Format;
import edu.utep.trustlab.visko.ontology.vocabulary.OWL;
import edu.utep.trustlab.visko.ontology.vocabulary.ViskoO;

public class Transformer extends Operator {
	private Format outputFormat;
	private OntResource outputDataType;

	private ObjectProperty transformsToFormatProperty;
	private ObjectProperty transformsToDataTypeProperty;

	public Transformer(String classURI, String baseURL, String name,
			ViskoModel viskoModel) {
		super(classURI, baseURL, name, viskoModel);
	}

	public Transformer(String baseURL, String name, ViskoModel viskoModel) {
		super(ViskoO.CLASS_URI_TRANSFORMER, baseURL, name, viskoModel);
	}

	public Transformer(String uri, ViskoModel viskoModel) {
		super(uri, viskoModel);
	}

	public void setTransformsToFormat(Format outFormat) {
		outputFormat = outFormat;
	}

	public Format getTransformsToFormat() {
		return outputFormat;
	}
	
	public void setTransformsToDataType(OntResource outDataType){
		outputDataType = outDataType;
	}
	
	public OntResource getTransformsToDataType(){
		return outputDataType;
	}

	private void addTransformsToFormatProperty(Individual subjectInd) {
		subjectInd.addProperty(transformsToFormatProperty,
				outputFormat.getIndividual());
	}
	
	private void addTransformsToDataTypeProperty(Individual subjectInd) {
		
		if(outputDataType == null)
			subjectInd.addProperty(transformsToDataTypeProperty, OWL.getOWLThing());
		else
			subjectInd.addProperty(transformsToDataTypeProperty, outputDataType);
	}

	@Override
	protected boolean allFieldsPopulated() {
		if (super.allFieldsPopulated() && outputFormat != null)
			return true;
		return false;
	}

	@Override
	protected Individual createNewIndividual() {
		Individual ind = super.createNewIndividual();
		this.addTransformsToFormatProperty(ind);
		this.addTransformsToDataTypeProperty(ind);
		return ind;
	}

	@Override
	protected void setProperties() {
		super.setProperties();
		transformsToFormatProperty = model.getObjectProperty(ViskoO.PROPERTY_URI_TRANSFORMS_TO);
		transformsToDataTypeProperty = model.getObjectProperty(ViskoO.PROPERTY_URI_TRANSFORMS_TO_DATATYPE);
	}

	@Override
	protected void populateFieldsWithIndividual(Individual ind) {
		super.populateFieldsWithIndividual(ind);
		
		Format fmt = new Format(ind.getPropertyValue(transformsToFormatProperty).as(Individual.class).getURI(), model);
		outputFormat = fmt;
		
		outputDataType = ind.getPropertyValue(transformsToDataTypeProperty).as(Individual.class);		
	}

	@Override
	protected void initializeFields() {
		super.initializeFields();
	}
}
