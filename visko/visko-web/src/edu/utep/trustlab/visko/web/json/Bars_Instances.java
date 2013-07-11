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

import com.hp.hpl.jena.query.ResultSet;
import org.json.*;
import edu.utep.trustlab.visko.sparql.ViskoTripleStore;

public class Bars_Instances {
	
	private static String jsonBarGraph;
	private static ViskoTripleStore ts = new ViskoTripleStore();
	
	public static String getBarGraph() {
	
		if(jsonBarGraph != null)
			return jsonBarGraph;
		
		//params stuff
		int paramCount = numResults(ts.getAllParameters());
		
		//viewerset and viewers stuff
		int viewerSetCount = numResults(ts.getViewerSets());
		int viewerCount = numResults(ts.getViewers());

		//operators count
		int transformerCount = numResults(ts.getTransformers());
		int mapperCount = numResults(ts.getMappers());
		int filterCount = numResults(ts.getFilters());
		int dimReducerCount = numResults(ts.getDimensionReducers());
		int interpolatorCount = numResults(ts.getInterpolators());
		
		//toolkits stuff
		int toolkitCount = numResults(ts.getToolkits());

		//formats and types stuff
		int formatCount = numResults(ts.getInputFormats());
		int dataTypeCount = numResults(ts.getInputDataTypes());
		
		//implementing services stuff
		int serviceCount = numResults(ts.getOWLSServices());
		
		//generated views
		int visualizationCount = numResults(ts.getVisualizationAbstractions());

		JSONObject jsonGraphData = new JSONObject();

		try {
			ArrayList<JSONObject> data = new ArrayList<JSONObject>();

			data.add(new JSONObject().put("viskoType", "Toolkit").put("count",	toolkitCount));
			data.add(new JSONObject().put("viskoType", "Service").put("count", serviceCount));
			data.add(new JSONObject().put("viskoType", "Parameter").put("count", paramCount));

			data.add(new JSONObject().put("viskoType", "Format").put("count", formatCount));
			data.add(new JSONObject().put("viskoType", "DataType").put("count", dataTypeCount));
			
			data.add(new JSONObject().put("viskoType", "ViewerSet").put("count", viewerSetCount));
			data.add(new JSONObject().put("viskoType", "Viewer").put("count", viewerCount));
			data.add(new JSONObject().put("viskoType", "Visualization").put("count", visualizationCount));

			data.add(new JSONObject().put("viskoType", "Transformer").put("count", transformerCount));
			data.add(new JSONObject().put("viskoType", "Filter").put("count", filterCount));
			data.add(new JSONObject().put("viskoType", "DimensionReducer").put("count", dimReducerCount));
			data.add(new JSONObject().put("viskoType", "Mapper").put("count", mapperCount));
			data.add(new JSONObject().put("viskoType", "Interpolator").put("count", interpolatorCount));


			jsonGraphData.put("instanceGraphData", data);

		} catch (Exception e) {
			e.printStackTrace();
		}

		jsonBarGraph =jsonGraphData.toString();
		return jsonBarGraph;
	}

	private static int numResults(ResultSet rs) {
		int count = 0;
		while (rs.hasNext()) {
			count++;
			rs.next();
		}
		return count;
	}
}
