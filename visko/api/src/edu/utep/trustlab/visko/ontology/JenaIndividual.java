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


package edu.utep.trustlab.visko.ontology;

import com.hp.hpl.jena.ontology.Individual;

import edu.utep.trustlab.visko.ontology.model.ViskoModel;
import edu.utep.trustlab.visko.ontology.vocabulary.supplemental.RDFS;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Property;

public abstract class JenaIndividual implements ViskoIndividual {
	private String uri;
	private String fullURL;
	private String fileName;
	private String baseURL;
	private OntClass conceptClass;
	private Individual ind;

	protected ViskoModel model;

	// name, label and comment
	private String name;
	private String label;
	private String comment;

	// common properties for all visko individuals
	private Property commentProperty;
	private Property labelProperty;

	public JenaIndividual(String classURI, String baseURL, String name, ViskoModel viskoModel) {

		// the model that will be used to house this new individual
		model = viskoModel;

		// construct the uri using the baseURL (ci-server) and some name
		setURI(baseURL, name);

		// initialize the OntClass that the individual will be a type of
		setOntClass(classURI);

		initializeFields();
		setCommonProperties();
		setProperties();
	}

	public JenaIndividual(String individualURI, ViskoModel viskoModel) {

		model = viskoModel;
		uri = individualURI;

		ind = model.getIndividual(uri);

		initializeFields();
		setCommonProperties();
		setProperties();

		populateCommonFields(ind);
		populateFieldsWithIndividual(ind);
	}

	public String getFileName() {
		return fileName;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getComment() {
		return comment;
	}

	public String getURI() {
		return uri;
	}

	public String getFullURL() {
		return fullURL;
	}

	public String toString() {
		return model.getModelAsRDFString();
	}

	public void close() {
		model.closeModel();
	}

	public Individual getIndividual() {
		if (ind != null)
			return ind;

		else if (allFieldsPopulated()) {
			ind = createNewIndividual();
			return ind;
		}

		else {
			System.out.println("please populate the necessary fields of a new object set as a writer...");
			return null;
		}
	}

	/********************* PRIVATE METHODS *****************************/
	private void setURI(String base, String individualName) {
		baseURL = base;
		name = individualName;

		if (baseURL.endsWith(".owl")) {
			fileName = baseURL.substring(baseURL.lastIndexOf("/") + 1);
			fullURL = baseURL;
			uri = fullURL + "#" + name;
		}
		else {
			fileName = makeFileName(name);
			fullURL = baseURL + fileName;
			uri = fullURL + "#" + name;
		}
		
		uri = uri.replaceAll("\r", "");
	}
	
	public static String  makeFileName(String name){
		return name + ".owl";
	}

	private void setOntClass(String classURI) {
		conceptClass = model.getOntClass(classURI);
	}

	private void setCommonProperties() {
		commentProperty = model.getProperty(RDFS.PROPERTY_URI_comment);
		labelProperty = model.getProperty(RDFS.PROPERTY_URI_label);
	}

	private void populateCommonFields(Individual ind) {
		boolean commentPropertyAsserted = ind.getPropertyValue(commentProperty) != null;
		boolean labelPropertyAsserted = ind.getPropertyValue(labelProperty) != null;

		if (commentPropertyAsserted)
			comment = (String) ind.getPropertyValue(commentProperty).as(Literal.class).getValue();

		if (labelPropertyAsserted)
			label = (String) ind.getPropertyValue(labelProperty).as(Literal.class).getValue();
	}

	private void addCommentProperty(Individual subjectInd) {
		if (comment != null) {
			Literal commentLiteral = model.createLiteral(comment);
			subjectInd.addProperty(commentProperty, commentLiteral);
		}
	}

	private void addLabelProperty(Individual subjectInd) {
		if (label != null) {
			Literal labelLiteral = model.createLiteral(label);
			subjectInd.addProperty(labelProperty, labelLiteral);
		}
	}

	/****************** PROTECTED METHODS *******************************/
	protected Individual createNewIndividual() {
		Individual ind = model.createIndividual(uri, conceptClass);

		addCommentProperty(ind);
		addLabelProperty(ind);

		return ind;
	}

	protected abstract void initializeFields();

	protected abstract void populateFieldsWithIndividual(Individual ind);

	protected abstract void setProperties();

	protected abstract boolean allFieldsPopulated();
}
