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

package edu.utep.trustlab.visko.ontology.service.writer;
import edu.utep.trustlab.contentManagement.ContentManager;
import edu.utep.trustlab.visko.ontology.model.ViskoModel;
import edu.utep.trustlab.visko.ontology.operator.writer.TransformerWriter;
import edu.utep.trustlab.visko.ontology.service.OWLSService;
import edu.utep.trustlab.visko.ontology.service.Toolkit;
import edu.utep.trustlab.visko.ontology.writer.ViskoWriter;

public class ServiceOperatorWriter extends ViskoWriter {
	
	private Toolkit supportingToolkit;
	private String wsdl;
	private String opName;
	private String label;
	
	private String inputFormatURI;
	private String outputFormatURI;
	private String viewURI;
	
    private OWLSService service;
	private ViskoModel readingModel = new ViskoModel();
	
	public ServiceOperatorWriter(String wsdlURL, String operatorName) {
		wsdl = wsdlURL;
		opName = operatorName;
		service = new OWLSService(ContentManager.getViskoRDFContentManager().getBaseURL(), opName, owlsModel);
	}
	
	public void setInputAndOutputFormatURIs(String inFormatURI, String outFormatURI){
		inputFormatURI = inFormatURI;
		outputFormatURI = outFormatURI;
	}
	
	public void setView(String generatedViewURI){
		viewURI = generatedViewURI;
	}

	public void setLabel(String lbl) {
		label = lbl;
	}

	public void setSupportingToolkit(String toolkitURI) {
		supportingToolkit = new Toolkit(toolkitURI, readingModel);
	}

	public String toRDFString() {
		TransformerWriter wtr;
		
		if(viewURI == null){
			wtr = new TransformerWriter(opName + "Operator", false);
		}
		else{
			wtr = new TransformerWriter(opName + "Operator", true);
			wtr.setMappedToView(viewURI);
		}
		
		wtr.addInputFormat(inputFormatURI);
		wtr.setOutputFormat(outputFormatURI);			
		wtr.setLabel(label);
		wtr.saveDocument();
		String operatorURI = wtr.getURI();
		
		service.setConceptualOperatorURI(operatorURI);
		service.setSupportingToolkit(supportingToolkit);
		service.setWSDLURL(wsdl);
		service.setOperationName(opName);
		service.setLabel(label);

		// adds the individual to the model and returns a reference to that
		// Individual
		service.getIndividual();
		viskoIndividual = service;

		return viskoIndividual.toString();		
	}
}
