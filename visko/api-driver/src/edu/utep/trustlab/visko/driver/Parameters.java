package edu.utep.trustlab.visko.driver;

import edu.utep.trustlab.visko.planning.Query;
import edu.utep.trustlab.visko.planning.QueryEngine;
import edu.utep.trustlab.visko.planning.pipelines.Pipeline;
import edu.utep.trustlab.visko.planning.pipelines.PipelineSet;
import edu.utep.trustlab.visko.sparql.SPARQL_EndpointFactory;

public class Parameters {
	public static void main(String[] args){
		
		//Specify Location of Triple Store (created with visko/visko-build/build with target build-triple-store)
		SPARQL_EndpointFactory.setUpEndpointConnection("../visko-web/WebContent/registry-tdb/");
		
		Query query = new Query(getSampleQuery());
		QueryEngine engine = new QueryEngine(query);
		
		PipelineSet pipes = engine.getPipelines();
		Pipeline pipe;
		if(pipes.size() > 0){
			System.out.println(pipes.firstElement());
			pipe = pipes.firstElement();
			
			//print out unbound parameters
			for(String parameter : pipe.getUnboundParameters())
				System.out.println("An unbound parameter: " + parameter);
			
			//print out bound parameters and their arguments
			for(String parameter : pipe.getAllParameters()){
				String value = pipe.getParameterBindings().get(parameter);
				
				if(value != null)
					System.out.println("Parameter: " + parameter + " is bound to: " + value);
				else
					System.out.println("Parameter: " + parameter + " is unbound");
			}
		}		
	}
	
	private static final String NEWLINE = "\n";
	
	public static String getSampleQuery(){
		String queryString =
				"PREFIX views http://openvisko.org/rdf/ontology/visko-view.owl#" + NEWLINE +
				"PREFIX formats http://openvisko.org/rdf/pml2/formats/" + NEWLINE +
				"PREFIX types http://rio.cs.utep.edu/ciserver/ciprojects/CrustalModeling/CrustalModeling.owl#" + NEWLINE +
				"PREFIX visko http://visko.cybershare.utep.edu:5080/visko-web/registry/module_webbrowser.owl#" + NEWLINE +
				"PREFIX params http://visko.cybershare.utep.edu:5080/visko-web/registry/grdcontour.owl#" + NEWLINE +
				"VISUALIZE http://visko.cybershare.utep.edu:5080/visko-web/test-data/gravity/gravityDataset.txt" + NEWLINE +
				"AS views:2D_ContourMap IN visko:web-browser" + NEWLINE +
				"WHERE" + NEWLINE +
				"FORMAT = formats:SPACESEPARATEDVALUES.owl#SPACESEPARATEDVALUES" + NEWLINE +
				"AND TYPE = types:d19";

		return queryString;	
	}
}
