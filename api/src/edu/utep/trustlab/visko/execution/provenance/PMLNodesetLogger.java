package edu.utep.trustlab.visko.execution.provenance;
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

import java.io.StringWriter;
import java.util.Vector;

import org.inference_web.pml.v2.pmlj.*;
import org.inference_web.pml.v2.pmlp.*;
import org.inference_web.pml.v2.vocabulary.*;
import org.inference_web.pml.v2.pmlj.IWNodeSet;
import org.inference_web.pml.v2.util.PMLObjectManager;
import org.mindswap.owl.OWLValue;
import org.mindswap.owls.process.variable.Input;
import org.mindswap.query.ValueMap;

import edu.utep.trustlab.contentManagement.ContentManager;
import edu.utep.trustlab.visko.ontology.model.ViskoModel;
import edu.utep.trustlab.visko.ontology.operator.Transformer;
import edu.utep.trustlab.visko.ontology.service.Service;
import edu.utep.trustlab.visko.util.FileUtils;

public class PMLNodesetLogger {
	
	private Vector<IWNodeSet> serviceNodesets;
	
	private String url;
	private String baseFileName = "visko-pipeline-provenance";
	private String baseNodesetNameService = "pipeline-step";
	private String baseNodesetNameParameter = "parameter-assertion";
	
	private String fileName;
		
	public PMLNodesetLogger() {
		serviceNodesets = new Vector<IWNodeSet>();
		fileName = baseFileName + "-" + FileUtils.getRandomFileName() + ".owl";
		String baseURL = ContentManager.getProvenanceContentManager().getBaseURL();
		url = baseURL + fileName;
	}

	public void captureInitialDataset(String inDatasetURL, Service ingestingService){
		IWInferenceStep is = (IWInferenceStep) PMLObjectManager.createPMLObject(PMLJ.InferenceStep_lname);
		is.setHasInferenceRule(PMLResourceURI.RULE_DIRECT_ASSERTION);
		is.setHasInferenceEngine(PMLResourceURI.ENGINE_VISKO_WEB_SERVICE);
		
		//set up conclusion
		IWInformation conclusion = (IWInformation) PMLObjectManager.createPMLObject(PMLP.Information_lname);
		String formatURI = ingestingService.getConceptualOperator().getOperatesOnFormats().get(0).getURI();
		conclusion.setHasFormat(formatURI);
		conclusion.setHasURL(inDatasetURL);
		
		//set up nodeset
		String nodesetNameService = baseNodesetNameService + "-" + FileUtils.getRandomFileName();
		IWNodeSet ns = (IWNodeSet) PMLObjectManager.createPMLObject(PMLJ.NodeSet_lname);
		ns.setIdentifier(PMLObjectManager.getObjectID(url + "#" + nodesetNameService));
		ns.setHasConclusion(conclusion);
		ns.addIsConsequentOf(is);
		
		serviceNodesets.add(ns);
	}
	
	public void captureProcessingStep(Service service, String inDatasetURL, String outDatasetURL, ValueMap<Input, OWLValue> inputValueMap) {
		//set up transformer
		Transformer transformer = new Transformer(service.getConceptualOperator().getURI(), new ViskoModel());
		
		//set up inference step
		IWInferenceStep is = (IWInferenceStep) PMLObjectManager.createPMLObject(PMLJ.InferenceStep_lname);
		is.setHasInferenceRule(service.getConceptualOperator().getURI());
		is.setHasInferenceEngine(PMLResourceURI.ENGINE_VISKO_WEB_SERVICE);
		
		//set up conclusion
		IWInformation conclusion = (IWInformation) PMLObjectManager.createPMLObject(PMLP.Information_lname);
		String formatURI = transformer.getTransformsToFormat().getURI();
		conclusion.setHasFormat(formatURI);
		setConclusionURL(conclusion, outDatasetURL);
		
		//set up nodeset
		String nodesetNameService = baseNodesetNameService + "-" + FileUtils.getRandomFileName();
		IWNodeSet ns = (IWNodeSet) PMLObjectManager.createPMLObject(PMLJ.NodeSet_lname);
		ns.setIdentifier(PMLObjectManager.getObjectID(url + "#" + nodesetNameService));
		ns.setHasConclusion(conclusion);
		ns.addIsConsequentOf(is);
		
		for (Input var : inputValueMap.getVariables()) {			
			OWLValue value = inputValueMap.getValue(var);
			String valueString = value.toString();

			if (!valueString.equals(inDatasetURL)) {				
				//set up inference step
				IWInferenceStep paramIS = (IWInferenceStep) PMLObjectManager.createPMLObject(PMLJ.InferenceStep_lname);
				paramIS.setHasInferenceRule(PMLResourceURI.RULE_DIRECT_ASSERTION);
				paramIS.setHasInferenceEngine(PMLResourceURI.ENGINE_VISKO_PARAMETER_BINDER);
				
				//set up conclusion
				IWInformation paramConclusion = (IWInformation) PMLObjectManager.createPMLObject(PMLP.Information_lname);
				paramConclusion.setHasFormat(PMLResourceURI.FORMAT_PLAIN_TEXT);
				paramConclusion.setHasRawString(var.getURI() + " = " + valueString);
				
				//set up nodeset
				String nodesetNameParameter = baseNodesetNameParameter + "-" + FileUtils.getRandomFileName();
				IWNodeSet paramNS = (IWNodeSet) PMLObjectManager.createPMLObject(PMLJ.NodeSet_lname);
				paramNS.setIdentifier(PMLObjectManager.getObjectID(url + "#" + nodesetNameParameter));
				paramNS.setHasConclusion(paramConclusion);
				paramNS.addIsConsequentOf(paramIS);

				is.addAntecedent(paramNS);
			}
		}
		serviceNodesets.add(ns);
	}

	private void setConclusionURL(IWInformation conclusion, String dataURL){
		ContentManager dataManager = ContentManager.getDataContentManager();
		
		String newURL = dataURL;
		
		if(dataManager != null)
			newURL = dataManager.saveDocument(dataURL);
		
		conclusion.setHasURL(newURL);
	}
	
	public String dumpNodesets() {

		IWInferenceStep is;
		for (int i = (serviceNodesets.size() - 1); i > 0; i--){
			is = (IWInferenceStep) serviceNodesets.get(i).getIsConsequentOf().get(0);
			is.addAntecedent(serviceNodesets.get(i - 1));
		}

		StringWriter writer = new StringWriter();
		IWNodeSet rootNodeset = serviceNodesets.get(serviceNodesets.size() - 1);
		PMLObjectManager.getOntModel(rootNodeset).write(writer, "RDF/XML-ABBREV");
		PMLObjectManager.getOntModel(rootNodeset).close();
		
		ContentManager manager = ContentManager.getProvenanceContentManager();
		manager.saveDocument(writer.toString(), fileName);
		
		return rootNodeset.getIdentifier().getURIString();
	}
}