package trustlab.visko.web.json;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

import trustlab.visko.sparql.ViskoTripleStore;
import trustlab.visko.web.context.ViskoContext;

public class OperatorGraphData
{
	public static HashMap<String, Integer> operators;
	
	public static String getPathsGraphJSON()
	{
		operators = new HashMap<String, Integer>();
		
		ViskoTripleStore ts = ViskoTripleStore.getInstance(ViskoContext.VISKO_TRIPLE_STORE_LOCATION);
		
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
		ResultSet viewers = ts.getViewers();
		ResultSet transformers = ts.getTransformers();
		
		ArrayList<JSONObject> nodeList = new ArrayList<JSONObject>();
		QuerySolution solution;
		String viewerURI;
		String transformerURI;
		
		try
		{
			while(viewers.hasNext())
			{
				solution = viewers.nextSolution();
				viewerURI = solution.get("viewer").toString();
				
				if(OperatorGraphData.operators.get(viewerURI) == null)
				{
					OperatorGraphData.operators.put(viewerURI, nodeList.size());
					nodeList.add(new JSONObject().put("operatorURI", viewerURI).put("viskoType", "Viewer"));
				}
			}
			
			while(transformers.hasNext())
			{
				solution = transformers.nextSolution();
				transformerURI = solution.get("trans").toString();
				
				if(OperatorGraphData.operators.get(transformerURI) == null)
				{
					OperatorGraphData.operators.put(transformerURI, nodeList.size());
					if(ts.isMapper(transformerURI))
						nodeList.add(new JSONObject().put("operatorURI", transformerURI).put("viskoType", "Mapper"));
					else
						nodeList.add(new JSONObject().put("operatorURI", transformerURI).put("viskoType", "Transformer"));
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
		ResultSet formatsInfo = ts.getTransformedFormats();
		
		ArrayList<JSONObject> linksList = new ArrayList<JSONObject>();
		
		QuerySolution solution;
		String outputOfOperator;
		String inputToOperator;
		String formatURI;
		
		System.out.println("num vars: " + formatsInfo.getResultVars().size());
		
		for(int i = 0; i < formatsInfo.getResultVars().size(); i ++)
			System.out.println(formatsInfo.getResultVars().get(i));
		
		int source;
		int target;
		try
		{
			while(formatsInfo.hasNext())
			{
				solution = formatsInfo.nextSolution();				
				formatURI = solution.get("format").toString();
				outputOfOperator = solution.get("outputOf").toString();
				inputToOperator = solution.get("inputTo").toString();
						
				source = operators.get(outputOfOperator);
				target = operators.get(inputToOperator);
				
				linksList.add(new JSONObject().put("source", source)
						.put("target", target)
						.put("format", formatURI));
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return linksList;
	}
}