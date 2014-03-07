package edu.utep.trustlab.visko.driver;


import edu.utep.trustlab.visko.execution.PipelineExecutor;
import edu.utep.trustlab.visko.execution.PipelineExecutorJob;
import edu.utep.trustlab.visko.planning.Query;
import edu.utep.trustlab.visko.planning.QueryEngine;
import edu.utep.trustlab.visko.planning.pipelines.PipelineSet;
import edu.utep.trustlab.visko.sparql.SPARQL_EndpointFactory;

public class Execution {
	
	public static void main(String[] args){
		
		//Specify Location of Triple Store (created with visko/visko-build/build with target build-triple-store)
		SPARQL_EndpointFactory.setUpEndpointConnection("../visko-web/WebContent/registry-tdb/");
		
		Query query = new Query(getSampleQuery());
		QueryEngine engine = new QueryEngine(query);
		
		PipelineSet pipes = engine.getPipelines();
		PipelineExecutor executor = new PipelineExecutor();

		if(pipes.size() > 0){
			System.out.println(pipes.firstElement());
				
			PipelineExecutorJob job = new PipelineExecutorJob(pipes.firstElement());
				
			executor.setJob(job);
			
			//fork thread to run service
			executor.process();
		
			while(executor.isAlive()){
				System.out.println("executing serivce with index: " + job.getJobStatus().getCurrentServiceIndex());
				System.out.println("executing: " + job.getJobStatus().getCurrentServiceURI());
				System.out.println(job.getJobStatus().getPipelineState());
			}
			
			System.out.println("Did Job complete normally? " + job.getJobStatus().didJobCompletedNormally());
			System.out.println("Final Result = " + job.getFinalResultURL());	
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