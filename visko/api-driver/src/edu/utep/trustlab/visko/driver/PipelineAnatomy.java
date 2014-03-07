package edu.utep.trustlab.visko.driver;

import java.util.Vector;

import com.hp.hpl.jena.ontology.OntResource;

import edu.utep.trustlab.visko.ontology.pmlp.Format;
import edu.utep.trustlab.visko.ontology.viskoOperator.Viewer;
import edu.utep.trustlab.visko.ontology.viskoService.Service;
import edu.utep.trustlab.visko.planning.Query;
import edu.utep.trustlab.visko.planning.QueryEngine;
import edu.utep.trustlab.visko.planning.pipelines.Pipeline;
import edu.utep.trustlab.visko.planning.pipelines.PipelineSet;
import edu.utep.trustlab.visko.sparql.SPARQL_EndpointFactory;

public class PipelineAnatomy {
public static void main(String[] args){
		
		//Specify Location of Triple Store (created with visko/visko-build/build with target build-triple-store)
		SPARQL_EndpointFactory.setUpEndpointConnection("../visko-web/WebContent/registry-tdb/");
		
		Query query = new Query(Execution.getSampleQuery());
		QueryEngine engine = new QueryEngine(query);
		
		PipelineSet pipes = engine.getPipelines();
		Pipeline pipe;
		Service service;
		if(pipes.size() > 0){
			pipe = pipes.firstElement();
		
			for(int i = 0; i < pipe.size(); i ++){
				service = pipe.getService(i);
				
				//get service name and uri
				String name = service.getConceptualOperator().getName();
				String uri = service.getConceptualOperator().getURI();
				
				System.out.println("Service:  " + name);
				System.out.println("Service URI: " + uri);
				
				//get service toolkit
				String toolkitName = service.getSupportingToolkit().getLabel();
				System.out.println("\tService Toolkit: " + toolkitName);
				
				//get service inputs
				Vector<Format> inputFormats = service.getConceptualOperator().getInputFormats();
				Vector<OntResource> inputTypes = service.getConceptualOperator().getInputDataTypes();
				
				for(Format inFormat : inputFormats)
					System.out.println("\t\tInput Format: " + inFormat.getURI());
				
				for(OntResource inType : inputTypes)
					System.out.println("\t\tInput Type: " + inType.getURI());
				
				//There is other stuff related to the RDF describing this service, but I don't think you all need that
			}
			
			//print stuff regarding the viewer
			Viewer viewer = pipe.getViewer();
			System.out.println("Viewer: " + viewer.getName());
			
			Vector<Format> inputFormats = viewer.getInputFormats();
			for(Format inFormat : inputFormats)
				System.out.println("\tInput Format: " + inFormat.getURI());
		}		
	}
}