package edu.utep.trustlab.visko.web.json;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

import edu.utep.trustlab.visko.sparql.ViskoTripleStore;

public class FormatGraphData
{
	public static HashMap<String, Integer> formats;
	
	public static String getPathsGraphJSON()
	{
		formats = new HashMap<String,Integer>();
		ViskoTripleStore ts = new ViskoTripleStore();
		
		JSONObject pathsGraph = new JSONObject();
		try
		{
			pathsGraph.put("nodes", getNodes(ts));
			pathsGraph.put("links", getLinks(ts));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return pathsGraph.toString();
	}

	private static ArrayList<JSONObject> getNodes(ViskoTripleStore ts)
	{
		ResultSet formats = ts.getOperatedOnFormats();
		
		ArrayList<JSONObject> nodeList = new ArrayList<JSONObject>();
		QuerySolution solution;
		String formatURI;
		boolean isViewable;
		try
		{
			while(formats.hasNext())
			{
				solution = formats.nextSolution();
				formatURI = solution.get("inputFormat").toString();
				
				isViewable = ts.isAlreadyVisualizable(formatURI);

				if(FormatGraphData.formats.get(formatURI) == null)
				{
					FormatGraphData.formats.put(formatURI, new Integer(nodeList.size()));
					nodeList.add(new JSONObject().put("formatURI", formatURI).put("visualizable", isViewable));
				}
				
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return nodeList;
	}
	
	private static ArrayList<JSONObject> getLinks(ViskoTripleStore ts)
	{
		ResultSet transformerInfo = ts.getTransformerInformation();
		ArrayList<JSONObject> linksList = new ArrayList<JSONObject>();
		QuerySolution solution;
		String input;
		String output;
		String transURI;
		String transLbl;

		int source;
		int target;
		try
		{
			while(transformerInfo.hasNext())
			{
				solution = transformerInfo.nextSolution();
				input = solution.get("input").toString();
				output = solution.get("output").toString();
				transURI = solution.get("trans").toString();
				transLbl = solution.get("transLbl").toString();
								
				source = formats.get(input);
				target = formats.get(output);
				
				linksList.add(new JSONObject().put("source", source)
						.put("target", target)
						.put("transURI", transURI)
						.put("transLbl", transLbl));
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return linksList;
	}
}
