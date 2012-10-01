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


package edu.utep.trustlab.visko.planning;

import java.util.Vector;

import edu.utep.trustlab.visko.ontology.vocabulary.supplemental.OWL;
import edu.utep.trustlab.visko.planning.Pipeline;
import edu.utep.trustlab.visko.planning.PipelineSet;
import edu.utep.trustlab.visko.planning.Query;
import edu.utep.trustlab.visko.sparql.ViskoTripleStore;
import edu.utep.trustlab.visko.util.CartesianProduct;
import edu.utep.trustlab.visko.util.ResultSetToVector;

import com.hp.hpl.jena.query.*;

public class PipelineSetBuilder {
	private ViskoTripleStore ts;

	private OperatorPaths operatorPaths;
	private PipelineSet pipelines;
	private Query query;
	
	private String formatURI;
	private String dataTypeURI;
	private Vector<String> viewerURIs;
	private String viewURI;

	private boolean isDataFiltered;
	
	public PipelineSetBuilder(Query drivingQuery) {
		ts = new ViskoTripleStore();
		query = drivingQuery;
		isDataFiltered = query.dataIsFiltered();
		formatURI = query.getFormatURI();
		dataTypeURI = query.getTypeURI();
		viewerURIs = ResultSetToVector.getVectorFromResultSet(ts.getViewersOfViewerSet(query.getViewerSetURI()), "viewer");
		viewURI = query.getViewURI();
	}
	
	public PipelineSetBuilder(String aFormatURI, String aDataTypeURI, Vector<String> viewers, String aViewURI){
		ts = new ViskoTripleStore();
		isDataFiltered = true;
		formatURI = aFormatURI;
		dataTypeURI = aDataTypeURI;
		viewerURIs = viewers;
		viewURI = aViewURI;
	}
	
	public ViskoTripleStore getTripleStore() {
		return ts;
	}

	public boolean isAlreadyVisualizableWithViewerSet() {
		return ts.isAlreadyVisualizableWithViewerSet(query.getFormatURI(), query.getTypeURI(), query.getViewerSetURI());
	}
		
	public OperatorPaths getOperatorPaths() {		
		System.out.println("Finding operator paths...");
		setOperatorPaths();
		
		System.out.println("Number of operator paths: " + operatorPaths.size());
						
		if (viewURI != null) {
			operatorPaths.filterByView(viewURI);
			System.out.println("Number of operator paths after additional View restrictions: " + operatorPaths.size());
		}					
		return operatorPaths;		
	}
	
	public PipelineSet getPipelineSet(){
		if(operatorPaths == null)
			getOperatorPaths();
		
		setOperatorImplementations();
		System.out.println("Number of executable pipelines: " + pipelines.size());
		
		return pipelines;
	}
		
	private void setOperatorImplementations(){
		Vector<Vector<String>> operatorImplSets;
		pipelines = new PipelineSet(query);
		Pipeline pipe;

		for (OperatorPath operatorPath : operatorPaths) {
			pipe = new Pipeline(operatorPath.getViewerURI(), operatorPath.getVisualizationAbstractionGenerated(), pipelines);

			if (operatorPath.isEmpty())
				pipelines.add(pipe);

			else {
				operatorImplSets = new Vector<Vector<String>>();
				for (String operatorURI : operatorPath){
					
					ResultSet operatorImplURIs = ts.getOWLSServiceImplementationsURIs(operatorURI);
					Vector<String> operatorImplURIsVector = ResultSetToVector.getVectorFromResultSet(operatorImplURIs, "opImpl");

					if (operatorImplURIsVector.size() > 0)
						operatorImplSets.add(operatorImplURIsVector);
				}

				if (operatorImplSets.size() == operatorPath.size()) {
					Vector<Vector<String>> cartesianOperatorImpl = CartesianProduct.cartesianProduct(operatorImplSets, 0);

					for (Vector<String> cartesianPath : cartesianOperatorImpl) {
						pipe = new Pipeline(operatorPath.getViewerURI(), operatorPath.getVisualizationAbstractionGenerated(), pipelines);
						pipe.setServiceURIs(cartesianPath);
						pipelines.add(pipe);
					}
				}
			}
		}
	}
		
	private void setOperatorPaths(){
		operatorPaths = new OperatorPaths();
		
		ResultSet operatorResults = ts.getOperatorsThatProcessData(formatURI, dataTypeURI);
		Vector<String> operatorURIs = ResultSetToVector.getVectorFromResultSet(operatorResults, "operator");		
				
		OperatorPath operatorPath;
		for(String operatorURI : operatorURIs){
			operatorPath = new OperatorPath(ts);
			operatorPath.add(operatorURI);
			String transformedDataType = getNextDataType(dataTypeURI, operatorURI);
			constructOperatorPaths(operatorPath, transformedDataType);
		}
	}
	
	private boolean operatorPathViolatesCompositionRules(OperatorPath path){
		return path.violatesRules() || path.violatesRequestedView(viewURI);
	}
	
	private void constructOperatorPaths(OperatorPath operatorPath, String inputTypeURI){
		if(!operatorPathViolatesCompositionRules(operatorPath)){
			ResultSet operatorResults;
			if(inputTypeURI.equals(OWL.CLASS_URI_Thing))
				operatorResults = ts.getAdjacentOperatorsAccordingToFormat(operatorPath.lastElement());
			else if(isDataFiltered)
				operatorResults = ts.getAdjacentOperatorsAccordingToFormatAndDataType(operatorPath.lastElement(), inputTypeURI);
			else
				operatorResults = ts.getAdjacentNonDataFilterOperatorsAccordingToFormatAndDataType(operatorPath.lastElement(), inputTypeURI);
		
			Vector<String> operatorURIs = ResultSetToVector.getVectorFromResultSet(operatorResults, "operator");	
			operatorURIs = operatorPath.filterOperatorsAlreadyInPath(operatorURIs);
	
			if(operatorURIs.size() > 0){
				OperatorPath clonedPath;
				String targetViewerURI = operatorPath.outputCanBeViewedByViewerSet(viewerURIs);

				if(targetViewerURI != null){
					clonedPath = operatorPath.clonePath();
					clonedPath.setViewer(targetViewerURI);
					operatorPaths.add(clonedPath);
				}
			
				for(String nextOperatorURI : operatorURIs){				
					clonedPath = operatorPath.clonePath();
					clonedPath.add(nextOperatorURI);
					String transformedDataType = getNextDataType(inputTypeURI, nextOperatorURI);
					constructOperatorPaths(clonedPath, transformedDataType);
				}
			}
			else {
				String targetViewerURI = operatorPath.outputCanBeViewedByViewerSet(viewerURIs);				
				if(targetViewerURI != null)
					operatorPath.setViewer(targetViewerURI);
					operatorPaths.add(operatorPath);
			}
		}
	}	
	
	private String getNextDataType(String currentDataType, String operatorURI){
		String[] dataURIs = ResultSetToVector.getVectorPairsFromResultSet(ts.getOutputData(operatorURI), "format", "dataType").firstElement();
		String dataTypeURI = dataURIs[1];
		
		if(dataTypeURI.equals(OWL.CLASS_URI_Thing))
			return currentDataType;
		else
			return dataTypeURI;
	}
}
