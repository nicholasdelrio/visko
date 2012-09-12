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

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.rdf.model.RDFNode;

import edu.utep.trustlab.visko.ontology.JenaIndividual;
import edu.utep.trustlab.visko.ontology.model.ViskoModel;
import edu.utep.trustlab.visko.ontology.viskoOperator.Operator;
import edu.utep.trustlab.visko.ontology.vocabulary.ViskoS;

public class Service extends JenaIndividual {
	
	private OWLSService owlsService;
	private Operator operator;
	private Toolkit toolkit;
	
	private ObjectProperty implementsOperator;
	private ObjectProperty supportingToolkit;
	private ObjectProperty supportedByOWLSService;
	
	public Service(String baseURL, String name, ViskoModel viskoModel) {
		super(ViskoS.CLASS_URI_Service, baseURL, name, viskoModel);
	}

	public Service(String uri, ViskoModel viskoModel) {
		super(uri, viskoModel);
	}
	
	public void setOWLSService(OWLSService service){
		owlsService = service;
	}
	
	public OWLSService getOWLSService(){
		return owlsService;
	}
	
	public void setConceptualOperator(Operator op) {
		operator = op;
	}
	
	public Operator getConceptualOperator() {
		return operator;
	}

	public void setSupportingToolkit(Toolkit tk) {
		toolkit = tk;
	}

	public Toolkit getSupportingToolkit() {
		return toolkit;
	}
	
	
	private void addImplementsOperator(Individual subjectInd) {
		if (operator != null)
			subjectInd.addProperty(implementsOperator, operator.getIndividual());
	}

	private void addSupportingToolkit(Individual subjectInd) {
		if(toolkit != null)
			subjectInd.addProperty(supportingToolkit, toolkit.getIndividual());
	}
	
	private void addSupportingOWSService(Individual subjectInd) {		
		Individual owlsServiceIndividual = model.getIndividualFromOWLIndividual(owlsService.getIndividual());
		if(owlsService != null)
			subjectInd.addProperty(supportedByOWLSService, owlsServiceIndividual);
	}

	@Override
	protected boolean allFieldsPopulated() {
		return operator != null && toolkit != null && owlsService != null;
	}

	@Override
	protected Individual createNewIndividual() {
		Individual ind = super.createNewIndividual();

		this.addImplementsOperator(ind);
		this.addSupportingToolkit(ind);
		this.addSupportingOWSService(ind);
		
		return ind;
	}

	@Override
	protected void setProperties() {
		implementsOperator = model.getObjectProperty(ViskoS.PROPERTY_URI_implementsOperator);
		supportingToolkit = model.getObjectProperty(ViskoS.PROPERTY_URI_supportedByToolkit);
		supportedByOWLSService = model.getObjectProperty(ViskoS.PROPERTY_URI_supportedByOWLSService);
	}

	@Override
	protected void populateFieldsWithIndividual(Individual ind) {
		
		// populate operator
		RDFNode operatorNode = ind.getPropertyValue(implementsOperator);
		operator = new Operator(operatorNode.as(Individual.class).getURI(), model);
		
		// populate toolkit
		RDFNode toolkitNode = ind.getPropertyValue(supportingToolkit);
		toolkit = new Toolkit(toolkitNode.as(Individual.class).getURI(), model);
		
		// populate owlsService
		RDFNode serviceNode = ind.getPropertyValue(supportedByOWLSService);
		owlsService = new OWLSService(serviceNode.as(Individual.class).getURI());
	}

	@Override
	protected void initializeFields() {

	}
}