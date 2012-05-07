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

import edu.utep.trustlab.visko.execution.Pipeline;
import edu.utep.trustlab.visko.sparql.ViskoTripleStore;
import edu.utep.trustlab.visko.util.ResultSetToVector;

public class PipelineGraphData {
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
		for(String serviceURI : pipe){

			//add pipeline operators to list of nodes
			if(ts.isMapper(serviceURI))
				nodesList.add(new JSONObject().put("operatorURI", serviceURI).put("viskoType", "mapper"));
			else
				nodesList.add(new JSONObject().put("operatorURI", serviceURI).put("viskoType", "transformer"));
			
			Vector<String>params = ResultSetToVector.getVectorFromResultSet(ts.getInputParameters(serviceURI), "parameter");
			
			nodeCount++;
			
			if(nodeCount > 1){
				linksList.add(new JSONObject().put("source", nodeCount - parameterCount - 1).put("target", nodeCount - 1).put("linkType", "nextService"));
			}
			
			String boundValue;
			parameterCount = 0;
			for(String paramURI : params){
				boundValue = pipe.getParameterBindings().get(paramURI);
				if(boundValue != null){
					nodesList.add(new JSONObject().put("parameterURI", paramURI).put("viskoType", "parameter").put("value", boundValue));
				}
				else
					nodesList.add(new JSONObject().put("parameterURI", paramURI).put("viskoType", "parameter").put("value", "null"));
				
				parameterCount++;
				nodeCount++;
				linksList.add(new JSONObject().put("source", nodeCount - 1).put("target", nodeCount - parameterCount - 1).put("linkType", "hasInput"));
			}
		}
		
		nodesList.add(new JSONObject().put("operatorURI", pipe.getViewURI()).put("viskoType", "viewer"));
		linksList.add(new JSONObject().put("target", nodeCount - 1).put("source", nodeCount - parameterCount - 1).put("linkType", "nextService"));
	}
}