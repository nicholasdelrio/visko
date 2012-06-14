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

package edu.utep.trustlab.visko.execution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import edu.utep.trustlab.visko.ontology.model.OWLSModel;
import edu.utep.trustlab.visko.ontology.model.ViskoModel;
import edu.utep.trustlab.visko.ontology.operator.Viewer;
import edu.utep.trustlab.visko.ontology.service.OWLSService;
import edu.utep.trustlab.visko.sparql.ViskoTripleStore;
import edu.utep.trustlab.visko.util.ResultSetToVector;

public class Pipeline extends Vector<String> {
	private String viewer;
	private Vector<String> viewerSets;
	private String view;
	private OWLSModel owlsLoadingModel;
	private ViskoModel viskoLoadingModel;
	private PipelineSet parentContainer;

	private ArrayList<String> unboundParameters;
	private ArrayList<String> allParameters;
	
	public Pipeline(String viewerURI, String viewURI, PipelineSet parent) {
		super();
		viskoLoadingModel = new ViskoModel();
		owlsLoadingModel = new OWLSModel();
		parentContainer = parent;
		viewer = viewerURI;
		view = viewURI;
		
		setViewerSets(viewerURI);
	}
	
	public Vector<String> getViewerSets(){
		return viewerSets;
	}
	
	private void setViewerSets(String viewerURI){
		viewerSets = ResultSetToVector.getVectorFromResultSet(new ViskoTripleStore().getViewerSetsOfViewer(viewerURI), "viewerSet");
	}

	public String getViewURI(){
		return view;
	}
	
	public Viewer getViewer() {
		return new Viewer(viewer, viskoLoadingModel);
	}

	public List<String> getAllParameters(){
		if(allParameters == null)
			hasAllInputParameters();
		
		return allParameters;
	}
	
	public List<String> getUnboundParameters(){
		if(unboundParameters == null)
			hasAllInputParameters();
		
		return unboundParameters;
	}
	
	public String getViewerURI() {
		return new Viewer(viewer, viskoLoadingModel).getURI();
	}
	
	public HashMap<String, String> getParameterBindings() {
		return parentContainer.getParameterBindings();
	}

	public String getArtifactURL() {
		return parentContainer.getArtifactURL();
	}

	public void addOWLSServiceURI(String serviceURI) {
		add(serviceURI);
	}

	public void setOWLSServiceURIs(Vector<String> serviceURIs) {
		for (String serviceImplURI : serviceURIs) {
			addOWLSServiceURI(serviceImplURI);
		}
	}
	
	public OWLSService getService(int i){
		return new OWLSService(get(i), owlsLoadingModel);
	}
	
	public boolean requiresInputURL(){
		String firstServiceURI = get(0);
		Vector<String> params = ResultSetToVector.getVectorFromResultSet(new ViskoTripleStore().getInputParameters(firstServiceURI), "parameter");
		
		for(String parameterURI : params){
			if(parameterURI.contains("url") || parameterURI.contains("URL") || parameterURI.contains("fileLoc"))
				return true;
		}
		
		return false;
	}
	
	private boolean hasAllInputParameters(String serviceURI){		
		String boundedValue;
		boolean allParamsBounded = true;
		Vector<String> params = ResultSetToVector.getVectorFromResultSet(new ViskoTripleStore().getInputParameters(serviceURI), "parameter");
		
		for (String parameterURI : params) {

			boundedValue = getParameterBindings().get(parameterURI);
			
			if(!parameterURI.contains("url") && !parameterURI.contains("URL") && !parameterURI.contains("fileLoc")){
				allParameters.add(parameterURI);
				
				if(boundedValue == null){
					unboundParameters.add(parameterURI);
					allParamsBounded = false;
				}
			}
		}
		return allParamsBounded;
	}

	public boolean hasAllInputParameters(){
		unboundParameters = new ArrayList<String>();
		allParameters = new ArrayList<String>();

		boolean allParametersBound = true;
		for(String serviceURI : this){
			if(!hasAllInputParameters(serviceURI)){
				allParametersBound = false;
			}
		}
		return allParametersBound;
	}	
}