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

package edu.utep.trustlab.visko.planning.pipelines;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import edu.utep.trustlab.visko.ontology.model.ViskoModel;
import edu.utep.trustlab.visko.ontology.viskoOperator.Viewer;
import edu.utep.trustlab.visko.ontology.viskoService.Service;
import edu.utep.trustlab.visko.sparql.ViskoTripleStore;
import edu.utep.trustlab.visko.util.ResultSetToVector;


public class Pipeline extends Vector<String> {
	private String viewerURI;
	private Vector<String> viewerSets;
	private String viewURI;
	private ViskoModel viskoLoadingModel;
	private PipelineSet parentContainer;

	private ArrayList<String> unboundParameters;
	private ArrayList<String> allParameters;
	
	private ViskoTripleStore ts;
	
	public Pipeline(String aViewerURI, String aViewURI, PipelineSet parent) {
		super();
		
		ts = new ViskoTripleStore();
		
		viskoLoadingModel = new ViskoModel();
		parentContainer = parent;
		viewerURI = aViewerURI;
		viewURI = aViewURI;
		
		setViewerSets(viewerURI);		
	}
	
	public boolean hasInputData(){
		if(getArtifactURL() == null)
			return false;
		return true;
	}
	
	public Vector<String> getViewerSets(){
		return viewerSets;
	}
	
	private void setViewerSets(String viewerURI){
		viewerSets = ResultSetToVector.getVectorFromResultSet(ts.getViewerSetsOfViewer(viewerURI), "viewerSet");
	}

	public String getViewURI(){
		return viewURI;
	}
	
	public Viewer getViewer() {
		return new Viewer(viewerURI, viskoLoadingModel);
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
		return viewerURI;
	}
	
	public HashMap<String, String> getParameterBindings() {
		return parentContainer.getParameterBindings();
	}

	public String getArtifactURL() {
		return parentContainer.getArtifactURL();
	}

	public void addServiceURI(String serviceURI) {
		add(serviceURI);
	}

	public void setServiceURIs(Vector<String> serviceURIs) {
		for (String serviceImplURI : serviceURIs) {
			addServiceURI(serviceImplURI);
		}
	}
	
	public Service getService(int i){
		return new Service(get(i), viskoLoadingModel);
	}

	public boolean requiresInputURL(){
		String firstServiceURI = get(0);
		Vector<String> params = ResultSetToVector.getVectorFromResultSet(ts.getInputParameters(firstServiceURI), "parameter");
		
		for(String parameterURI : params){
			if(parameterURI.contains("url") || parameterURI.contains("URL") || parameterURI.contains("fileLoc"))
				return true;
		}
		
		return false;
	}
	
	private boolean hasAllInputParameters(String serviceURI){		
		String boundedValue;
		boolean allParamsBounded = true;
		Vector<String> params = ResultSetToVector.getVectorFromResultSet(ts.getInputParameters(serviceURI), "parameter");
		
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
	
	public String toString(){
		String stringRepresentation = 
				"Pipeline:\n"
				+ super.toString() + "\n"
				+ "Viewer: " + this.viewerURI;
		return stringRepresentation;
	}
	
	public PipelineSet getParentPipelineSet(){
		return parentContainer;
	}
	
	public String getToolkitThatGeneratesView(){
		String toolkitURI = null;
		if(viewURI != null){
			for(String serviceURI : this){
				if(ts.isImplemenationOfServiceAMapper(serviceURI))
					toolkitURI = ResultSetToVector.getVectorFromResultSet(ts.getToolkitOf(serviceURI), "toolkit").firstElement();
			}
		}
		return toolkitURI;
	}
	
	public String getOutputFormat(){
		String finalServiceURI = this.lastElement();
		return ResultSetToVector.getVectorFromResultSet(ts.getServiceOutputFormat(finalServiceURI), "format").firstElement();
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