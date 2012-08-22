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

import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLObjectProperty;
import org.mindswap.owls.service.Service;
import edu.utep.trustlab.visko.ontology.OWLSIndividual;
import edu.utep.trustlab.visko.ontology.model.OWLSModel;
import edu.utep.trustlab.visko.ontology.model.ViskoModel;
import edu.utep.trustlab.visko.ontology.operator.Operator;
import edu.utep.trustlab.visko.ontology.vocabulary.ViskoS;
import edu.utep.trustlab.visko.util.OWLSRDFCleanup;
import edu.utep.trustlab.visko.util.wsdl.WSDLTranslatorBuilder;

public class OWLSService extends OWLSIndividual {

	private String wsdlURL;
	private String operationName;
	private String label;

	private Operator implementedOperator;
	
	private Extractor implementedExtractor;

	private Toolkit supportingToolkit;

	private OWLObjectProperty implementsOperatorProperty;
	private OWLObjectProperty supportingToolkitProperty;
	private OWLObjectProperty implementsExtractorProperty;

	private boolean isOperatorService;
	private boolean isExtractorService;

	public OWLSService(String uri, OWLSModel owlsModel) {// for reading
		super(uri, owlsModel);
	}

	public OWLSService(String baseURI, String name, OWLSModel owlsModel) {// for writing
		super(baseURI, name, owlsModel);
	}

	public void setLabel(String lbl) {
		label = lbl;
	}

	public void setConceptualOperator(Operator op) {
		implementedOperator = op;
	}
	
	public Operator getConceptualOperator() {
		return implementedOperator;
	}

	public void setConceptualExtractor(Extractor ex) {
		implementedExtractor = ex;
	}

	public Extractor getConceptualExtractor() {
		return implementedExtractor;
	}

	public void setSupportingToolkit(Toolkit tk) {
		supportingToolkit = tk;
	}

	public Toolkit getSupportingToolkit() {
		return supportingToolkit;
	}

	public void setWSDLURL(String url) {
		wsdlURL = url;
	}

	public void setOperationName(String opName) {
		operationName = opName;
	}

	private OWLIndividual getToolkitAsOWLIndividual() {
		return model.convertJenaToOWLIndividual(supportingToolkit);
	}

	private OWLIndividual getOperatorAsOWLIndividual() {
		return model.convertJenaToOWLIndividual(implementedOperator);
	}

	private OWLIndividual getExtractorAsOWLIndividual() {
		return model.convertJenaToOWLIndividual(implementedExtractor);
	}

	@Override
	public Service getIndividual() {
		Service service = null;
		if (allFieldsPopulated() && !isForReading) {// create service
			WSDLTranslatorBuilder builder = new WSDLTranslatorBuilder(model.getOntology(), operationName, wsdlURL);
			service = builder.getTranslator().getService();
			service.setLabel(label, null);

			if (isOperatorService) {
				service.addProperty(supportingToolkitProperty, getToolkitAsOWLIndividual());
				service.addProperty(implementsOperatorProperty, getOperatorAsOWLIndividual());
			}
			
			else
				service.addProperty(implementsExtractorProperty, getExtractorAsOWLIndividual());
		
		} else if (!allFieldsPopulated() && !isForReading) {
			System.out.println("all fields not populated....");
		
		} else {// read service and populate service object
			service = model.readService(uri);

			OWLIndividual operator = service.getProperty(implementsOperatorProperty);
			OWLIndividual toolkit = service.getProperty(supportingToolkitProperty);
			OWLIndividual extractor = service.getProperty(implementsExtractorProperty);

			ViskoModel viskoModel = new ViskoModel();

			if (operator != null && toolkit != null) {
				supportingToolkit = new Toolkit(toolkit.getURI().toASCIIString(), viskoModel);
				implementedOperator = new Operator(operator.getURI().toASCIIString(), viskoModel);
			}

			if (extractor != null)
				implementedExtractor = new Extractor(extractor.getURI().toASCIIString(), viskoModel);
		}

		return service;
	}

	@Override
	protected void setProperties() {
		implementsOperatorProperty = model.getObjectProperty(ViskoS.PROPERTY_URI_IMPLEMENTS_OPERATOR);
		implementsExtractorProperty = model.getObjectProperty(ViskoS.PROPERTY_URI_IMPLEMENTS_EXTRACTOR);
		supportingToolkitProperty = model.getObjectProperty(ViskoS.PROPERTY_URI_SUPPORTED_BY);
	}

	@Override
	protected boolean allFieldsPopulated() {
		isOperatorService = 
				wsdlURL != null
				&& operationName != null
				&& implementedOperator != null
				&& this.supportingToolkit != null
				&& label != null;
		
		isExtractorService = 
				wsdlURL != null
				&& operationName != null
				&& implementedExtractor != null
				&& label != null;

		return isOperatorService || isExtractorService;
	}

	@Override
	protected void initializeFields() {
		// TODO Auto-generated method stub
	}
	
	public String toString(){
		String rdfString = super.toString();
		rdfString = OWLSRDFCleanup.fixURIForImplementsOperator(rdfString, implementedOperator.getURI());
		rdfString = OWLSRDFCleanup.fixURIForSupportedByToolkit(rdfString, supportingToolkit.getURI());
		return rdfString;
	}
}