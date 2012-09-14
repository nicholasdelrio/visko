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

public class FormatGraphData {
	public static HashMap<String, Integer> formats;

	public static String getPathsGraphJSON() {
		formats = new HashMap<String, Integer>();
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
		ResultSet formats = ts.getOperatedOnFormats();

		ArrayList<JSONObject> nodeList = new ArrayList<JSONObject>();
		QuerySolution solution;
		String formatURI;
		boolean isViewable;
		try {
			while (formats.hasNext()) {
				solution = formats.nextSolution();
				formatURI = solution.get("inputFormat").toString();

				isViewable = ts.isAlreadyVisualizable(formatURI);

				if (FormatGraphData.formats.get(formatURI) == null) {
					FormatGraphData.formats.put(formatURI,
							new Integer(nodeList.size()));
					nodeList.add(new JSONObject().put("formatURI", formatURI)
							.put("visualizable", isViewable));
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return nodeList;
	}

	private static ArrayList<JSONObject> getLinks(ViskoTripleStore ts) {
		ResultSet transformerInfo = ts.getOperatorInformation();
		ArrayList<JSONObject> linksList = new ArrayList<JSONObject>();
		QuerySolution solution;
		String input;
		String output;
		String transURI;
		String transLbl;

		int source;
		int target;
		try {
			while (transformerInfo.hasNext()) {
				solution = transformerInfo.nextSolution();
				input = solution.get("input").toString();
				output = solution.get("output").toString();
				transURI = solution.get("trans").toString();
				transLbl = solution.get("transLbl").toString();

				source = formats.get(input);
				target = formats.get(output);

				linksList.add(new JSONObject().put("source", source)
						.put("target", target).put("transURI", transURI)
						.put("transLbl", transLbl));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return linksList;
	}
}
