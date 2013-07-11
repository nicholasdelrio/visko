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


package edu.utep.trustlab.visko.web.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import org.json.JSONObject;

import edu.utep.trustlab.visko.planning.operatorPaths.OperatorPath;
import edu.utep.trustlab.visko.planning.operatorPaths.OperatorPaths;
import edu.utep.trustlab.visko.sparql.ViskoTripleStore;
import edu.utep.trustlab.visko.util.ResultSetToVector;

public class Graph_OperatorPaths {	
	public static HashMap<String, Integer> operators;
	public static HashMap<String, String> operatorLinks;
	
	private static String jsonGraph;
	private static ViskoTripleStore ts;
	private static ArrayList<JSONObject> linksList;
	private static ArrayList<JSONObject> nodesList;

	
	public static String getOperatorPathsGraphJSON(){
	
		if(jsonGraph != null)
			return jsonGraph;
		
		ts = new ViskoTripleStore();
		linksList = new ArrayList<JSONObject>();
		nodesList = new ArrayList<JSONObject>();
		operators = new HashMap<String, Integer>();
		operatorLinks = new HashMap<String, String>();
		
		OperatorPathsCache pathsBuilder = new OperatorPathsCache();
		pathsBuilder.start();
		
		while(pathsBuilder.isAlive())
			;
		
		populateNodesAndLinks(pathsBuilder.getOperatorPaths());
		addAllNodes();

		JSONObject pathsGraph = new JSONObject();
		try {
			pathsGraph.put("nodes", nodesList);
			pathsGraph.put("links", linksList);

			jsonGraph = pathsGraph.toString();
			System.out.println(jsonGraph);
			return jsonGraph;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static void addAllNodes(){
		Vector<String> operatorURIs = ResultSetToVector.getVectorFromResultSet(ts.getOperators(), "operator");
		for(String operatorURI : operatorURIs){
			System.out.println("adding operatorNode: " + operatorURI);
			addNode(operatorURI);
		}
	}
	
	private static void processPaths(OperatorPaths paths){
		String operator1 = null;
		String operator2 = null;
		for(OperatorPath path : paths){
			for(int i = 0; i < path.size(); i ++){
				if(i > 0){
					operator1 = path.get(i - 1);
					operator2 = path.get(i);
					addNodesAndLinks(operator1, operator2);
				}			
			}
			addNodesAndLinksFromViewers(path);
		}
	}
	
	private static void addNodesAndLinksFromViewers(OperatorPath path){
		String operatorURI = path.lastElement();
		String viewerURI = path.getViewerURI();		
		addNodesAndLinks(operatorURI, viewerURI);
	}
	
	private static void addNodesAndLinks(String operator1, String operator2){
				
		int source = addNode(operator1);
		int target = addNode(operator2);
		
		if(source != target)		
			addLink(source, target, linksList);
	}
	
	private static void addLink(int source, int target, ArrayList<JSONObject> linksList){
		String link = operatorLinks.get(source + "-" + target);
		if(link == null){
		
			operatorLinks.put(source + "-" + target, "exists");
			
			try{
				linksList.add(new JSONObject().put("source", source).put("target", target));
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	private static int addNode(String operatorURI){
		Integer index = operators.get(operatorURI);
		int newIndex;
		
		if(index == null){
			newIndex = nodesList.size();
			operators.put(operatorURI, newIndex);
			try{
				if(ts.isMapper(operatorURI))
					nodesList.add(new JSONObject().put("operatorURI", operatorURI).put("viskoType", "ViewMapper"));
				else if(ts.isViewer(operatorURI))
					nodesList.add(new JSONObject().put("operatorURI", operatorURI).put("viskoType", "Viewer"));
				else
					nodesList.add(new JSONObject().put("operatorURI", operatorURI).put("viskoType", "Transformer"));
				return newIndex;
			}
			catch(Exception e){
				e.printStackTrace();
				return 0;
			}
		}
		else
			return index.intValue();
	}

	private static void populateNodesAndLinks(OperatorPaths paths) {
		processPaths(paths);
	}
}
