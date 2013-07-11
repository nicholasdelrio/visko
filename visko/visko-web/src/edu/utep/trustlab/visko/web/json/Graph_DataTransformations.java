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

public class Graph_DataTransformations {
	public static HashMap<String, Integer> data;
	public static HashMap<String, String> dataLinks;
	
	private static String jsonGraph;
	private static ViskoTripleStore ts;
	private static ArrayList<JSONObject> linksList;
	private static ArrayList<JSONObject> nodesList;

	
	public static String getDataPathsGraphJSON(){
	
		if(jsonGraph != null)
			return jsonGraph;
		
		ts = new ViskoTripleStore();
		linksList = new ArrayList<JSONObject>();
		nodesList = new ArrayList<JSONObject>();
		data = new HashMap<String, Integer>();
		dataLinks = new HashMap<String, String>();
		
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
		Vector<String[]> data = ResultSetToVector.getVectorPairsFromResultSet(ts.getInputDataParents(), "format", "dataType");
		for(String[] datum : data){
			System.out.println("adding dataNode: " + datum[0] + " - " + datum[1]);
			addNode(datum[0], datum[1]);
		}
	}
		
	private static void processPaths(OperatorPaths paths){
		
		for(OperatorPath path : paths){
			String[] inputData = null;
			String[] outputData = null;
			
			String operatorURI;
			for(int i = 0; i < path.size(); i ++){
				operatorURI = path.get(i);

				if(i == 0){
					inputData = ResultSetToVector.getVectorPairsFromResultSet(ts.getInputData(operatorURI), "format", "dataType").firstElement();
					outputData = ResultSetToVector.getVectorPairsFromResultSet(ts.getOutputData(operatorURI), "format", "dataType").firstElement();
				}
				else{
					inputData = outputData;
					outputData = ResultSetToVector.getVectorPairsFromResultSet(ts.getOutputData(operatorURI), "format", "dataType").firstElement();
				}
					
				addNodesAndLinks(inputData, outputData);
			}
		}
	}
	
	private static void addNodesAndLinks(String[] inputData, String[] outputData){
				
		int source = addNode(inputData[0], inputData[1]);
		int target = addNode(outputData[0], outputData[1]);
		
		if(source != target)		
			addLink(source, target, linksList);
	}
	
	private static void addLink(int source, int target, ArrayList<JSONObject> linksList){
		String link = dataLinks.get(source + "-" + target);
		if(link == null){
		
			dataLinks.put(source + "-" + target, "exists");
			
			try{
				linksList.add(new JSONObject().put("source", source).put("target", target));
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	private static int addNode(String formatURI, String dataTypeURI){
		String key = formatURI + "---" + dataTypeURI;
		Integer index = data.get(key);
		int newIndex;
		boolean isViewable;
		if(index == null){
			isViewable = ts.isAlreadyVisualizableWithViewerSet(formatURI, dataTypeURI);
			newIndex = nodesList.size();
			data.put(key, newIndex);
			try{
				nodesList.add(new JSONObject().put("data", key).put("visualizable", isViewable));
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
		Graph_DataTransformations.processPaths(paths);
	}
}
