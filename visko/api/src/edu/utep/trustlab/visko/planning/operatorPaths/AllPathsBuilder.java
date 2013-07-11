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


package edu.utep.trustlab.visko.planning.operatorPaths;

import java.util.Vector;

import edu.utep.trustlab.visko.ontology.vocabulary.supplemental.OWL;
import edu.utep.trustlab.visko.planning.Query;
import edu.utep.trustlab.visko.sparql.ViskoTripleStore;
import edu.utep.trustlab.visko.util.ResultSetToVector;

import com.hp.hpl.jena.query.*;

public class AllPathsBuilder extends OperatorPathsBuilder {
	private ViskoTripleStore ts;
	private OperatorPaths operatorPaths;
	
	private Vector<String> viewerURIs;
	private String viewURI;

	private boolean isDataFiltered;
		
	public AllPathsBuilder() {
		ts = new ViskoTripleStore();
		operatorPaths = new OperatorPaths();
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
					
	protected void setOperatorPaths(){
		
		ResultSet formatsAndDataTypes = ts.getInputDataParents();
		String formatURI;
		String dataTypeURI;
		QuerySolution solution;
		Query query;
		
		while(formatsAndDataTypes.hasNext()){
			solution = formatsAndDataTypes.next();
			formatURI = solution.get("format").toString();
			dataTypeURI = solution.get("dataType").toString();

			System.out.println("Finding operator paths for format: " + formatURI + " and dataTypeURI: " + dataTypeURI);
			
			viewerURIs = ResultSetToVector.getVectorFromResultSet(ts.getViewers(), "viewer");
			
			query = new Query(null, formatURI, null);
			query.setTypeURI(dataTypeURI);

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
	}
	
	private Vector<String> getNextOperators(OperatorPath operatorPath, String inputTypeURI){
		
		ResultSet operatorResults;
		if(inputTypeURI.equals(OWL.CLASS_URI_Thing))
			operatorResults = ts.getAdjacentOperatorsAccordingToFormat(operatorPath.lastElement());
		else if(isDataFiltered)
			operatorResults = ts.getAdjacentOperatorsAccordingToFormatAndDataType(operatorPath.lastElement(), inputTypeURI);
		else
			operatorResults = ts.getAdjacentNonFilterOperatorsAccordingToFormatAndDataType(operatorPath.lastElement(), inputTypeURI);
	
		Vector<String> operatorURIs = ResultSetToVector.getVectorFromResultSet(operatorResults, "operator");	
		operatorURIs = operatorPath.filterOperatorsAlreadyInPath(operatorURIs);
		return operatorURIs;
	}
	
	private void addValidPipelines(OperatorPath operatorPath){
		Vector<String> targetViewerURIs = operatorPath.outputCanBeViewedByViewerSet(viewerURIs);
		OperatorPath clonedPath;
		
		for(String targetViewerURI : targetViewerURIs){
			clonedPath = operatorPath.clonePath();
			clonedPath.setViewer(targetViewerURI);
			operatorPaths.add(clonedPath);			
		}
	}
	
	private boolean operatorPathViolatesCompositionRules(OperatorPath path){
		return path.violatesRules() || path.violatesRequestedView(viewURI);
	}
	
	private void constructOperatorPaths(OperatorPath operatorPath, String inputTypeURI){
		if(!operatorPathViolatesCompositionRules(operatorPath)){
			Vector<String> nextOperatorURIs = getNextOperators(operatorPath, inputTypeURI);
			if(nextOperatorURIs.size() > 0 && inputTypeURI != null){
				
				addValidPipelines(operatorPath);
			
				for(String nextOperatorURI : nextOperatorURIs){
					OperatorPath clonedPath = operatorPath.clonePath();
					clonedPath.add(nextOperatorURI);
					String transformedDataType = getNextDataType(inputTypeURI, nextOperatorURI);
					constructOperatorPaths(clonedPath, transformedDataType);
				}
			}
			else {addValidPipelines(operatorPath);}	
		}
	}	
		
	private String getNextDataType(String currentDataType, String operatorURI){
		Vector<String[]> data = ResultSetToVector.getVectorPairsFromResultSet(ts.getOutputData(operatorURI), "format", "dataType");
		
		String[] dataURIs = data.firstElement();
		String dataTypeURI = dataURIs[1];
		
		if(dataTypeURI.equals(OWL.CLASS_URI_Thing))
			return currentDataType;
		else
			return dataTypeURI;
	}
}
