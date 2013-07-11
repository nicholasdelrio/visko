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

import org.mindswap.owls.service.Service;
import edu.utep.trustlab.visko.ontology.OWLSIndividual;
import edu.utep.trustlab.visko.ontology.model.OWLSModel;
import edu.utep.trustlab.visko.util.wsdl.WSDLTranslatorBuilder;

public class OWLSService extends OWLSIndividual {

	private String wsdlURL;
	private String operationName;
	private String label;

	public OWLSService(String uri) {// for reading
		super(uri, new OWLSModel());
	}

	public OWLSService(String baseURI, String name) {// for writing
		super(baseURI, name, new OWLSModel());
	}

	public void setLabel(String lbl) {
		label = lbl;
	}

	public void setWSDLURL(String url) {
		wsdlURL = url;
	}

	public void setOperationName(String opName) {
		operationName = opName;
	}

	@Override
	public Service getIndividual() {
		Service service = null;
		if (allFieldsPopulated() && !isForReading) {
			// create service
			WSDLTranslatorBuilder builder = new WSDLTranslatorBuilder(model.getOntology(), operationName, wsdlURL);
			service = builder.getTranslator().getService();
			service.setLabel(label, null);		
		}
		else if (!allFieldsPopulated() && !isForReading) 
			System.out.println("all fields not populated....");
	
		else
			// read service and populate service object
			service = model.readService(uri);
			
		return service;
	}

	@Override
	protected void setProperties() {
	}

	@Override
	protected boolean allFieldsPopulated() {
		return
				wsdlURL != null
				&& operationName != null
				&& label != null;		
	}

	@Override
	protected void initializeFields() {
		// TODO Auto-generated method stub
	}
}