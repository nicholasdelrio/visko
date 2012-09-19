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

import edu.utep.trustlab.visko.planning.Pipeline;
import edu.utep.trustlab.visko.planning.PipelineSet;
import edu.utep.trustlab.visko.planning.Query;
import edu.utep.trustlab.visko.sparql.SPARQL_EndpointFactory;
import edu.utep.trustlab.visko.sparql.ViskoTripleStore;
import edu.utep.trustlab.visko.util.CartesianProduct;
import edu.utep.trustlab.visko.util.ResultSetToVector;

import com.hp.hpl.jena.query.*;

public class PipelineSetBuilder {
	private ViskoTripleStore ts;

	private OperatorPaths operatorPaths;
	private PipelineSet pipelines;
	private Query query;

	public PipelineSetBuilder(Query drivingQuery) {
		ts = new ViskoTripleStore();
		query = drivingQuery;
	}

	public PipelineSet getPipelines() {
		return pipelines;
	}

	public ViskoTripleStore getTripleStore() {
		return ts;
	}

	public boolean isAlreadyVisualizableWithViewerSet() {
		boolean formatCheck = ts.isFormatAlreadyVisualizableWithViewerSet(query.getFormatURI(), query.getViewerSetURI());
		boolean typeCheck = ts.isDataTypeAlreadyVisualizableWithViewerSet(query.getTypeURI(), query.getViewerSetURI()) || ts.isSubClassOfDataTypeAlreadyVisualizableWithViewerSet(query.getTypeURI(), query.getViewerSetURI());
		
		return formatCheck && typeCheck;
	}
	
	public void setPipelines() {	
		System.out.println("Finding operator paths...");
		setOperatorPaths();
		
		System.out.println("Number of operator paths: " + operatorPaths.size());
				
		operatorPaths.filterByViewerSet(query.getViewerSetURI());
		System.out.println("Number of operator paths after additional ViewerSet restriction: " + operatorPaths.size());
		
		if (query.getViewURI() != null) {
			operatorPaths.filterByView(query.getViewURI());
			System.out.println("Number of operator paths after additional View restrictions: " + operatorPaths.size());
		}				
		
		setOperatorImplementations();
		System.out.println("Number of executable pipelines: " + pipelines.size());
		
	}
	
	private void setOperatorImplementations(){
		Vector<Vector<String>> operatorImplSets;
		pipelines = new PipelineSet(query);
		Pipeline pipe;

		for (OperatorPath operatorPath : operatorPaths) {
			pipe = new Pipeline(operatorPath.getViewerURI(), operatorPath.getViewGenerated(), pipelines);

			if (operatorPath.isEmpty()) {
				pipelines.add(pipe);
			}

			else {
				operatorImplSets = new Vector<Vector<String>>();

				for (String operatorURI : operatorPath){
					
					ResultSet operatorImplURIs = ts.getOWLSServiceImplementationsURIs(operatorURI);
					Vector<String> operatorImplURIsVector = ResultSetToVector.getVectorFromResultSet(operatorImplURIs, "opImpl");

					if (operatorImplURIsVector.size() > 0)
						operatorImplSets.add(operatorImplURIsVector);
				}

				if (operatorImplSets.size() == operatorPath.size()) {
					Vector<Vector<String>> cartesianOperatorImpl = CartesianProduct
							.cartesianProduct(operatorImplSets, 0);

					for (Vector<String> cartesianPath : cartesianOperatorImpl) {
						pipe = new Pipeline(operatorPath.getViewerURI(), operatorPath.getViewGenerated(), pipelines);
						pipe.setServiceURIs(cartesianPath);
						pipelines.add(pipe);
					}
				}
			}
		}
	}
		
	private void setOperatorPaths(){
		operatorPaths = new OperatorPaths();
		
		// get a listing of all operators that process the inputFormat
		// these operators will be the starting point for our search algorithm
		ResultSet operatorResults = ts.getOperatorsThatProcessData(query.getFormatURI(), query.getTypeURI());
		Vector<String> operatorURIs = ResultSetToVector.getVectorFromResultSet(operatorResults, "operator");		
		
		System.out.println("input operators...");
		for(String operatorURI : operatorURIs)
			System.out.println(operatorURI);
		
		// for each root operatorURI, start a new path and populate it
		OperatorPath operatorPath;
		for(String operatorURI : operatorURIs){
			//if(!operatorURI.equals("https://raw.github.com/nicholasdelrio/visko-packages-rdf/master/package_vtk.owl#vtkDataObjectToDataSetFilter3DGravityData-operator")){
				operatorPath = new OperatorPath(ts);
				operatorPath.add(operatorURI);
				constructOperatorPaths(operatorPath);
			//}
		}
	}
	
	private void constructOperatorPaths(OperatorPath operatorPath){

		if(operatorPath.violatesSingleFilterRule() || operatorPath.violatesSingleMapperRule())
			return;
		
		if(query.getViewURI() != null && operatorPath.violatesRequestedView(query.getViewURI())){
			System.out.println("operator violated view: ");
			System.out.println(operatorPath);
			return;
		}
				
		// get a listing of all operators that can process input operator
		ResultSet operatorResults = ts.getAdjacentOperatorsAccordingToFormatAndDataType(operatorPath.lastElement());
		Vector<String> operatorURIs = ResultSetToVector.getVectorFromResultSet(operatorResults, "operator");
		
		//System.out.println("an Operator path: " + operatorPath);
		
		// filter out any operators that are already in this path		
		operatorURIs = operatorPath.filterOperatorsAlreadyInPath(operatorURIs);
		
		// recursive base case, we hit the end of an operator path
		if(operatorURIs.size() == 0){
			operatorPaths.add(operatorPath);
			return;
		}

		// recursive case, there are more operators to add
		else{
			OperatorPath clonedPath;
			
			if(ts.generatesTargetFormatOfViewerSet(operatorPath.lastElement(), query.getViewerSetURI())){
				clonedPath = operatorPath.clonePath();
				operatorPaths.add(clonedPath);
			}
			
			for(String nextOperatorURI : operatorURIs){
				clonedPath = operatorPath.clonePath();
				clonedPath.add(nextOperatorURI);
				constructOperatorPaths(clonedPath);
			}
		}
	}	
}
