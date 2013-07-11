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
import edu.utep.trustlab.visko.sparql.ViskoTripleStore;
import edu.utep.trustlab.visko.util.ResultSetToVector;

import com.hp.hpl.jena.query.*;

public class PartialPathsBuilder extends OperatorPathsBuilder {
	private ViskoTripleStore ts;
	private OperatorPaths operatorPaths;

	public PartialPathsBuilder() {
		ts = new ViskoTripleStore();
		operatorPaths = new OperatorPaths();
	}
				
	public OperatorPaths getOperatorPaths() {		
		System.out.println("Finding operator paths...");
		setOperatorPaths();
		System.out.println("Number of operator paths: " + operatorPaths.size());		
		return operatorPaths;		
	}
						
	protected void setOperatorPaths(){
		ResultSet operatorResults = ts.getForrestRootNodeOperators();
		Vector<String> operatorURIs = ResultSetToVector.getVectorFromResultSet(operatorResults, "operator");		
		
		OperatorPath operatorPath;
		for(String operatorURI : operatorURIs){
			operatorPath = new OperatorPath(ts);
			operatorPath.add(operatorURI);
			
			Vector<String[]> inputData = ResultSetToVector.getVectorPairsFromResultSet(ts.getInputData(operatorURI), "format", "dataType");
			
			String transformedDataType = getNextDataType(inputData.firstElement()[1], operatorURI);
			constructOperatorPaths(operatorPath, transformedDataType);
		}
	}
	
	private Vector<String> getNextOperators(OperatorPath operatorPath, String inputTypeURI){
		ResultSet operatorResults;

		if(inputTypeURI == null)
			return new Vector<String>();
		
		if(inputTypeURI.equals(OWL.CLASS_URI_Thing))
			operatorResults = ts.getAdjacentOperatorsAccordingToFormat(operatorPath.lastElement());
		else
			operatorResults = ts.getAdjacentNonFilterOperatorsAccordingToFormatAndDataType(operatorPath.lastElement(), inputTypeURI);
	
		Vector<String> operatorURIs = ResultSetToVector.getVectorFromResultSet(operatorResults, "operator");	
		operatorURIs = operatorPath.filterOperatorsAlreadyInPath(operatorURIs);
		return operatorURIs;
	}
	
	private void addPartialPipelines(OperatorPath operatorPath){
		OperatorPath clonedPath;
		clonedPath = operatorPath.clonePath();
		operatorPaths.add(clonedPath);			
	}
		
	private void constructOperatorPaths(OperatorPath operatorPath, String inputTypeURI){
		Vector<String> nextOperatorURIs = getNextOperators(operatorPath, inputTypeURI);
		if(nextOperatorURIs.size() > 0  && inputTypeURI != null){				
				for(String nextOperatorURI : nextOperatorURIs){
					OperatorPath clonedPath = operatorPath.clonePath();
					clonedPath.add(nextOperatorURI);
					String transformedDataType = getNextDataType(inputTypeURI, nextOperatorURI);
					constructOperatorPaths(clonedPath, transformedDataType);
				}
			}
		else {addPartialPipelines(operatorPath);}	
	}	
		
	private String getNextDataType(String currentDataType, String operatorURI){
		System.out.println("getting next data type for: " + operatorURI);
		Vector<String[]> nextDataURIs = ResultSetToVector.getVectorPairsFromResultSet(ts.getOutputData(operatorURI), "format", "dataType");
		
		if(nextDataURIs.size() > 0){
		
			String[] dataURIs = nextDataURIs.firstElement();
			String dataTypeURI = dataURIs[1];
		
			if(dataTypeURI.equals(OWL.CLASS_URI_Thing))
				return currentDataType;
			else
				return dataTypeURI;
		}
		else
			return null;
	}
}
