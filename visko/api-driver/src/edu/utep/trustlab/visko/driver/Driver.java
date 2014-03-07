package edu.utep.trustlab.visko.driver;

import com.hp.hpl.jena.query.ResultSet;

import edu.utep.trustlab.contentManagement.ContentManager;
import edu.utep.trustlab.contentManagement.LocalFileSystem;
import edu.utep.trustlab.visko.execution.PipelineExecutor;
import edu.utep.trustlab.visko.execution.PipelineExecutorJob;
import edu.utep.trustlab.visko.planning.Query;
import edu.utep.trustlab.visko.planning.QueryEngine;
import edu.utep.trustlab.visko.planning.pipelines.PipelineSet;
import edu.utep.trustlab.visko.sparql.SPARQL_Endpoint;
import edu.utep.trustlab.visko.sparql.SPARQL_EndpointFactory;
import edu.utep.trustlab.visko.sparql.ViskoTripleStore;

public class Driver {
	
	public static void main(String[] args){
		SPARQL_EndpointFactory.setUpEndpointConnection("../visko-web/WebContent/registry-tdb/");

		LocalFileSystem fs = new LocalFileSystem("http://iw.cs.utep.edu:8080/toolkits/output/", "C:/Users/Public/workspace-visko/api/output/");
		ContentManager.setWorkspacePath(null);
		ContentManager.setProvenanceContentManager(fs);
		
		ViskoTripleStore ts = new ViskoTripleStore();
		ResultSet results = ts.getInputFormats();
		System.out.println(results.hasNext());

		
		Query query = new Query(getSampleQuery());
		QueryEngine engine = new QueryEngine(query);
		
		PipelineSet pipes = null;
		
		pipes = engine.getPipelines();
		PipelineExecutor executor = new PipelineExecutor();

		if(pipes.size() > 0){
			System.out.println(pipes.firstElement());
				
			PipelineExecutorJob job = new PipelineExecutorJob(pipes.firstElement());
				
			executor.setJob(job);
			executor.process();
		
			while(executor.isAlive()){
			}
				
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
