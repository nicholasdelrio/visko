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

import org.json.JSONObject;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

import edu.utep.trustlab.visko.sparql.ViskoTripleStore;

public class OperatorGraphData {
	public static HashMap<String, Integer> operators;

	public static String getPathsGraphJSON() {
		operators = new HashMap<String, Integer>();

		ViskoTripleStore ts = new ViskoTripleStore();

		JSONObject pathsGraph = new JSONObject();
		try {
			pathsGraph.put("nodes", getNodes(ts));
			pathsGraph.put("links", getLinks(ts));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pathsGraph.toString();
	}

	private static ArrayList<JSONObject> getNodes(ViskoTripleStore ts) {
		ResultSet viewers = ts.getViewers();
		ResultSet transformers = ts.getTransformers();

		System.out.println(transformers.hasNext());
		
		ArrayList<JSONObject> nodeList = new ArrayList<JSONObject>();
		QuerySolution solution;
		String viewerURI;
		String transformerURI;

		try {
			while (viewers.hasNext()) {
				solution = viewers.nextSolution();
				viewerURI = solution.get("viewer").toString();

				if (OperatorGraphData.operators.get(viewerURI) == null) {
					OperatorGraphData.operators.put(viewerURI, nodeList.size());
					nodeList.add(new JSONObject().put("operatorURI", viewerURI)
							.put("viskoType", "Viewer"));
				}
			}

			while (transformers.hasNext()) {
				solution = transformers.nextSolution();
				transformerURI = solution.get("trans").toString();

				if (OperatorGraphData.operators.get(transformerURI) == null) {
					OperatorGraphData.operators.put(transformerURI,
							nodeList.size());
					if (ts.isMapper(transformerURI))
						nodeList.add(new JSONObject().put("operatorURI",
								transformerURI).put("viskoType", "Mapper"));
					else
						nodeList.add(new JSONObject().put("operatorURI",
								transformerURI).put("viskoType", "Transformer"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return nodeList;
	}

	private static ArrayList<JSONObject> getLinks(ViskoTripleStore ts) {
		ResultSet formatsInfo = ts.getTransformedFormats();

		ArrayList<JSONObject> linksList = new ArrayList<JSONObject>();

		QuerySolution solution;
		String outputOfOperator;
		String inputToOperator;
		String formatURI;

		System.out.println("num vars: " + formatsInfo.getResultVars().size());

		for (int i = 0; i < formatsInfo.getResultVars().size(); i++)
			System.out.println(formatsInfo.getResultVars().get(i));

		int source;
		int target;
		try {
			while (formatsInfo.hasNext()) {
				solution = formatsInfo.nextSolution();
				formatURI = solution.get("format").toString();
				outputOfOperator = solution.get("outputOf").toString();
				inputToOperator = solution.get("inputTo").toString();

				source = operators.get(outputOfOperator);
				target = operators.get(inputToOperator);

				linksList.add(new JSONObject().put("source", source)
						.put("target", target).put("format", formatURI));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return linksList;
	}
}
