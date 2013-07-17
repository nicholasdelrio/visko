package org.openvisko.module;


import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;
import org.openvisko.module.util.FileUtils;
import org.openvisko.module.util.ServerProperties;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFactory;

public class SPARQLResultsToJSONGraph extends org.openvisko.module.operators.ToolkitOperator
{
	private static ArrayList<JSONObject> linksList;
	private static ArrayList<JSONObject> nodesList;

	public static HashMap<String, Integer> resources;
	public static HashMap<String, String> predicates;
	
	public SPARQLResultsToJSONGraph(String sparqlJSONResultsURL){	
		super(sparqlJSONResultsURL, "sparqlResults.xml", true, true, "generalD3Graph.json");
		
		linksList = new ArrayList<JSONObject> ();
		nodesList = new ArrayList<JSONObject> ();
		
		resources = new HashMap<String,Integer> ();
		predicates = new HashMap<String,String> ();
	}
		
	public String transform(String subjectVariable, String predicateVariable, String objectVariable){
		ResultSet results = ResultSetFactory.fromXML(stringData);
		
		String sVariable = "?" + subjectVariable;
		String pVariable = "?" + predicateVariable;
		String oVariable = "?" + objectVariable;
		
		
		QuerySolution solution;
		String subject, predicate, object;
		while(results.hasNext()){
			solution = results.nextSolution();
			
			try{
				subject = solution.get(sVariable).asNode().getURI();
			}
			catch(Exception e){
				subject = solution.get(sVariable).toString();
			}
			
			try{
				predicate = solution.get(pVariable).asNode().getURI();
			}
			catch(Exception e){
				predicate = solution.get(pVariable).toString();
			}
			
			try{
				object = solution.get(oVariable).asNode().getURI();
			}
			catch(Exception e){
				object = solution.get(oVariable).toString();
			}
			addLink(subject, predicate, object);
		}
		
		JSONObject pathsGraph = new JSONObject();
		String jsonGraph;
		try {
			pathsGraph.put("nodes", nodesList);
			pathsGraph.put("links", linksList);

			jsonGraph = pathsGraph.toString();
			FileUtils.writeTextFile(jsonGraph, ServerProperties.getInstance().getOutputDir().getAbsolutePath(), outputFileName);
			return this.outputURL;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private void addLink(String subject, String predicate, String object){
		try{
			int sourceIndex;
			int targetIndex;
			
			// get sourceIndex
			if(resources.get(subject) == null){
				sourceIndex = nodesList.size();
				nodesList.add(new JSONObject().put("operatorURI", subject));
				resources.put(subject, new Integer(sourceIndex));
			}
			else
				sourceIndex = resources.get(subject).intValue();
				
			// get targetIndex
			if(resources.get(object) == null){
				targetIndex = nodesList.size();
				nodesList.add(new JSONObject().put("operatorURI", object));
				resources.put(object, new Integer(targetIndex));
			}
			else
				targetIndex = resources.get(object).intValue();
			
			linksList.add(new JSONObject().put("source", sourceIndex).put("target", targetIndex).put("predicate", predicate));
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}//end class
