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

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.rdf.model.RDFNode;

import edu.utep.trustlab.visko.ontology.JenaIndividual;
import edu.utep.trustlab.visko.ontology.model.ViskoModel;
import edu.utep.trustlab.visko.ontology.operator.Operator;
import edu.utep.trustlab.visko.ontology.vocabulary.ViskoS;

public class Service extends JenaIndividual {
	
	private OWLSService owlsService;
	private Operator implementedOperator;
	private Toolkit supportingToolkit;
	
	private ObjectProperty implementsOperatorProperty;
	private ObjectProperty supportingToolkitProperty;
	private ObjectProperty supportedByOWLSServiceProperty;
	
	public Service(String baseURL, String name, ViskoModel viskoModel) {
		super(ViskoS.CLASS_URI_SERVICE, baseURL, name, viskoModel);
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
		implementedOperator = op;
	}
	
	public Operator getConceptualOperator() {
		return implementedOperator;
	}

	public void setSupportingToolkit(Toolkit tk) {
		supportingToolkit = tk;
	}

	public Toolkit getSupportingToolkit() {
		return supportingToolkit;
	}
	
	
	private void addImplementsOperatorProperty(Individual subjectInd) {
		if (implementedOperator != null)
			subjectInd.addProperty(implementsOperatorProperty, implementedOperator.getIndividual());
	}

	private void addSupportingToolkitProperty(Individual subjectInd) {
		if(supportingToolkit != null)
			subjectInd.addProperty(supportingToolkitProperty, supportingToolkit.getIndividual());
	}
	
	private void addSupportingOWSServiceProperty(Individual subjectInd) {		
		if(owlsService != null)
			subjectInd.addProperty(supportedByOWLSServiceProperty, owlsService.getIndividual().getURI().toASCIIString());
	}

	@Override
	protected boolean allFieldsPopulated() {
		return implementedOperator != null && supportingToolkit != null && owlsService != null;
	}

	@Override
	protected Individual createNewIndividual() {
		Individual ind = super.createNewIndividual();

		this.addImplementsOperatorProperty(ind);
		this.addSupportingToolkitProperty(ind);
		this.addSupportingOWSServiceProperty(ind);
		
		return ind;
	}

	@Override
	protected void setProperties() {
		implementsOperatorProperty = model.getObjectProperty(ViskoS.PROPERTY_URI_IMPLEMENTS_OPERATOR);
		supportingToolkitProperty = model.getObjectProperty(ViskoS.PROPERTY_URI_SUPPORTED_BY);
		supportedByOWLSServiceProperty = model.getObjectProperty(ViskoS.PROPERTY_URI_SUPPORTED_BY_OWLS);
	}

	@Override
	protected void populateFieldsWithIndividual(Individual ind) {
		
		RDFNode operatorNode = ind.getPropertyValue(implementsOperatorProperty);
		implementedOperator = new Operator(operatorNode.as(Individual.class).getURI(), model);
		
		RDFNode toolkitNode = ind.getPropertyValue(supportingToolkitProperty);
		supportingToolkit = new Toolkit(toolkitNode.as(Individual.class).getURI(), model);
		
		RDFNode serviceNode = ind.getPropertyValue(supportedByOWLSServiceProperty);
		owlsService = new OWLSService(serviceNode.as(Individual.class).getURI());
	}

	@Override
	protected void initializeFields() {

	}
}