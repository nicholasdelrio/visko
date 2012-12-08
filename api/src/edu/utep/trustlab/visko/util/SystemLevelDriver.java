package edu.utep.trustlab.visko.util;

import edu.utep.trustlab.contentManagement.ContentManager;
import edu.utep.trustlab.contentManagement.LocalFileSystem;
import edu.utep.trustlab.visko.execution.PipelineExecutor;
import edu.utep.trustlab.visko.execution.PipelineExecutorJob;
import edu.utep.trustlab.visko.ontology.vocabulary.ViskoV;
import edu.utep.trustlab.visko.planning.Pipeline;
import edu.utep.trustlab.visko.planning.PipelineSet;
import edu.utep.trustlab.visko.planning.Query;
import edu.utep.trustlab.visko.planning.QueryEngine;
import edu.utep.trustlab.visko.sparql.SPARQL_EndpointFactory;

public class SystemLevelDriver {
	
	public static void main(String[] args){
		
		SPARQL_EndpointFactory.setUpEndpointConnection("C:/Users/Public/git/visko/tdb");
		
		LocalFileSystem fs = new LocalFileSystem("file:///Users/Public/git/visko/api/output/", "C:/Users/Public/git/visko/api/output/");
		ContentManager.setWorkspacePath("C:/Users/Public/git/visko/api/output/");
		ContentManager.setProvenanceContentManager(fs);
		
		String url = "http://rio.cs.utep.edu/ciserver/ciprojects/GravityMapProvenance/gravityDataset.txt";
		String formatURI = "https://raw.github.com/nicholasdelrio/visko/master/resources/formats/SPACESEPARATEDVALUES.owl#SPACESEPARATEDVALUES";
		String viewerSetURI = "https://raw.github.com/nicholasdelrio/visko-packages-rdf/master/package_mozilla.owl#mozilla-firefox";
		String typeURI = "http://rio.cs.utep.edu/ciserver/ciprojects/CrustalModeling/CrustalModeling.owl#d19";
		String viewURI = ViskoV.INDIVIDUAL_URI_2D_ContourMap;
		
		/*
		String url = "http://testerion.owl";
		String formatURI = "https://raw.github.com/nicholasdelrio/visko/master/resources/formats/RDFXML.owl#RDFXML";
		String viewerSetURI = "https://raw.github.com/nicholasdelrio/visko-packages-rdf/master/package_custom.owl#data-driven-documents";
		String typeURI = "https://raw.github.com/nicholasdelrio/visko/master/resources/ontology/visko.owl#VisKo_KnowledgeBase";
		String viewURI = ViskoV.INDIVIDUAL_URI_2D_VisKo_DataTransformations_ForceGraph;
		*/
		Query query = new Query(url, formatURI, viewerSetURI);
		query.setTypeURI(typeURI);
		query.setViewURI(null);
		
		QueryEngine engine = new QueryEngine(query);
		PipelineSet pipes = engine.getPipelines();
	
		for(Pipeline pipe : pipes)
			System.out.println(pipe);
		
		PipelineExecutor executor = new PipelineExecutor();

		for(int i = 0; i < pipes.size() && i < 2; i ++){
			System.out.println(pipes.firstElement());
			
			PipelineExecutorJob job = new PipelineExecutorJob(pipes.get(i), true);
			job.setAsSimulatedJob();
			
			executor.setJob(job);
			
			executor.process();
			
			while(executor.isAlive()){
			}
			
			System.out.println("Final Result = " + job.getFinalResultURL());
		}
		
		System.out.println("dumping provenance");
		String queryURI = executor.dumpProvenance();
		System.out.println("Query URI: " + queryURI);
	}
}
