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

import org.json.JSONException;
import org.json.JSONObject;

import edu.utep.trustlab.visko.planning.pipelines.Pipeline;
import edu.utep.trustlab.visko.sparql.ViskoTripleStore;
import edu.utep.trustlab.visko.util.ResultSetToVector;

public class Graph_Pipeline {
	public static HashMap<String, Integer> operators;
	
	private ArrayList<JSONObject> nodesList;
	private ArrayList<JSONObject> linksList;
	
	public String getPipelineJSON(Pipeline pipe) {
		operators = new HashMap<String, Integer>();
		ViskoTripleStore ts = new ViskoTripleStore();
		JSONObject pathsGraph = new JSONObject();
		
		try {
			setNodesAndLinks(pipe, ts);
			pathsGraph.put("nodes", nodesList);
			pathsGraph.put("links", linksList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pathsGraph.toString();
	}

	private void setNodesAndLinks(Pipeline pipe, ViskoTripleStore ts) throws JSONException {

		nodesList = new ArrayList<JSONObject>();
		linksList = new ArrayList<JSONObject>();

		int nodeCount = 0;
		int parameterCount = 0;
		for(String viskoServiceURI : pipe){

			//add pipeline operators to list of nodes
			Vector<String> implOf = ResultSetToVector.getVectorFromResultSet(ts.getImplemenationOf(viskoServiceURI), "operator");
			
			if(implOf != null && ts.isMapper(implOf.firstElement()))
				nodesList.add(new JSONObject().put("instanceURI", viskoServiceURI).put("viskoType", "Mapper"));
			else
				nodesList.add(new JSONObject().put("instanceURI", viskoServiceURI).put("viskoType", "Transformer"));
			nodeCount++;
			
			if(nodeCount > 1){
				linksList.add(new JSONObject().put("source", nodeCount  - parameterCount - 2).put("target", nodeCount - 1).put("linkType", "nextService"));
			}
			
			Vector<String>params = ResultSetToVector.getVectorFromResultSet(ts.getInputParameters(viskoServiceURI), "parameter");
			
		
			String boundValue;
			parameterCount = 0;
			for(String paramURI : params){
				boundValue = pipe.getParameterBindings().get(paramURI);
				
				if(!paramURI.contains("url") && !paramURI.contains("URL") && !paramURI.contains("fileLoc")){
					
					if(boundValue != null){
						nodesList.add(new JSONObject().put("instanceURI", paramURI).put("viskoType", "Parameter").put("value", boundValue));
					}
					else
						nodesList.add(new JSONObject().put("instanceURI", paramURI).put("viskoType", "Parameter").put("value", "null"));
				
					parameterCount++;
					linksList.add(new JSONObject().put("source", nodeCount + parameterCount - 1).put("target", nodeCount - 1).put("linkType", "hasInput"));
				}
			}
			
			nodeCount += parameterCount;
		}
		
		nodesList.add(new JSONObject().put("instanceURI", pipe.getViewerURI()).put("viskoType", "Viewer"));
		nodeCount ++;
		linksList.add(new JSONObject().put("target", nodeCount - 1).put("source", nodeCount - parameterCount - 2).put("linkType", "nextService"));
	}
}