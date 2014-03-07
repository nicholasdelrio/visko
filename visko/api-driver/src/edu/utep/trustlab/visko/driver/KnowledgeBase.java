package edu.utep.trustlab.visko.driver;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Resource;

import edu.utep.trustlab.visko.sparql.SPARQL_EndpointFactory;
import edu.utep.trustlab.visko.sparql.ViskoTripleStore;

public class KnowledgeBase {
	
	public static void main(String[] args){
		//Specify Location of Triple Store (created with visko/visko-build/build with target build-triple-store)
		SPARQL_EndpointFactory.setUpEndpointConnection("../visko-web/WebContent/registry-tdb/");
	
		ViskoTripleStore ts = new ViskoTripleStore();
		ResultSet inputFormats = ts.getInputFormats();
	
		System.out.println("List of known formats...");
		
		//print out registered formats
		Resource format;
		while(inputFormats.hasNext()){
			format = inputFormats.nextSolution().get("?inputFormat").asResource();
			System.out.println(format.getURI());
		}
		
		//print out input/output format of some service - you can snag a service from a pipeline
		String operatorURI = "http://visko.cybershare.utep.edu:5080/visko-web/registry/module_gmt.owl#grdcontour-operator";
		
		Resource inputFormat;
		Resource inputType;
		
		System.out.println("\n\nInterface of " + operatorURI);
		
		ResultSet inputData = ts.getInputData(operatorURI);
		while(inputData.hasNext()){
			QuerySolution solution = inputData.nextSolution();
			inputFormat = solution.get("?format").asResource();
			inputType = solution.get("?dataType").asResource();
			System.out.println("Input Format: " + inputFormat.getURI());
			System.out.println("Input Type: " + inputType.getURI());
		}
		
		Resource outputFormat;
		Resource outputType;
		
		ResultSet outputData = ts.getOutputData(operatorURI);
		while(outputData.hasNext()){
			QuerySolution solution = outputData.nextSolution();
			outputFormat = solution.get("?format").asResource();
			outputType = solution.get("?dataType").asResource();
			System.out.println("Output Format: " + outputFormat.getURI());
			System.out.println("Output Type: " + outputType.getURI());
		}

	}
}
