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

public class FormatAndDataTypeGraphData {
	public static HashMap<String, Integer> formats;

	private static String jsonGraph;
	
	public static String getPathsGraphJSON() {
		
		if(jsonGraph != null)
			return jsonGraph;
		
		formats = new HashMap<String, Integer>();
		ViskoTripleStore ts = new ViskoTripleStore();

		JSONObject pathsGraph = new JSONObject();
		try {
			pathsGraph.put("nodes", getNodes(ts));
			pathsGraph.put("links", getLinks(ts));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		jsonGraph = pathsGraph.toString();
		return jsonGraph;
	}

	private static ArrayList<JSONObject> getNodes(ViskoTripleStore ts) {
		ResultSet formatsAndTypes = ts.getOperatedOnFormatsAndDataTypes();

		ArrayList<JSONObject> nodeList = new ArrayList<JSONObject>();
		QuerySolution solution;
		String formatURI;
		String dataTypeURI;
		boolean isViewable;
		try {
			while (formatsAndTypes.hasNext()) {
				solution = formatsAndTypes.nextSolution();
				formatURI = solution.get("format").toString();
				dataTypeURI = solution.get("dataType").toString();

				isViewable = ts.isFormatAndDataTypeAlreadyVisualizable(formatURI, dataTypeURI);

				if (FormatAndDataTypeGraphData.formats.get(formatURI + "---" + dataTypeURI) == null) {
					FormatAndDataTypeGraphData.formats.put(formatURI + "---" + dataTypeURI, new Integer(nodeList.size()));
					nodeList.add(new JSONObject().put("formatURI", formatURI + "---" + dataTypeURI).put("visualizable", isViewable));
					
					System.out.println("added: " + formatURI + "---" + dataTypeURI);
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
		String inputFormat;
		String outputFormat;
		String inputDataType;
		String outputDataType;
		String operatorURI;
		String operatorLbl;

		int source;
		int target;
		
		
			while (transformerInfo.hasNext()) {
				
				solution = transformerInfo.nextSolution();
				inputFormat = solution.get("inputFormat").toString();
				outputFormat = solution.get("outputFormat").toString();
				inputDataType = solution.get("inputDataType").toString();
				outputDataType = solution.get("outputDataType").toString();
				operatorURI = solution.get("operator").toString();
				operatorLbl = solution.get("lbl").toString();
		
				try
				{
					source = formats.get(inputFormat + "---" + inputDataType);
					target = formats.get(outputFormat + "---" + outputDataType);

					linksList.add(new JSONObject().put("source", source)
							.put("target", target).put("transURI", operatorURI)
							.put("transLbl", operatorLbl));
				
				}
				catch (Exception e) {
					System.out.println("couldn't find: " + inputFormat + "---" + inputDataType);
					System.out.println(" or couldn't find " + outputFormat + "---" + outputDataType);
					
					e.printStackTrace();
				}
			}
		return linksList;
	}
}
