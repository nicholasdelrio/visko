package edu.utep.trustlab.visko.driver;

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
		
		Resource format;
		while(inputFormats.hasNext()){
			format = inputFormats.nextSolution().get("?inputFormat").asResource();
			System.out.println(format.getURI());
		}
	}
}
