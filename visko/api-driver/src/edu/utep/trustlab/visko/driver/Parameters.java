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
		
		Query query = new Query(Execution.getSampleQuery());
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
}
