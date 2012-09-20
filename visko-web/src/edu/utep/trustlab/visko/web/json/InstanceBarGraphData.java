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

public class InstanceBarGraphData {
	public static String getBarGraph() {
		ViskoTripleStore ts = new ViskoTripleStore();

		int paramCount = numResults(ts.getAllParameters());
		int viewerSetCount = numResults(ts.getViewerSets());
		int viewerCount = numResults(ts.getViewers());
		int transformerCount = numResults(ts.getOperators());
		int toolkitCount = numResults(ts.getToolkits());
		int mapperCount = numResults(ts.getMappers());
		int formatCount = numResults(ts.getOperatedOnFormats());
		int serviceCount = numResults(ts.getOWLSServices());
		int viewCount = numResults(ts.getViews());

		JSONObject jsonGraphData = new JSONObject();

		try {
			ArrayList<JSONObject> data = new ArrayList<JSONObject>();

			data.add(new JSONObject().put("viskoType", "Viewer Sets").put("count", viewerSetCount));
			data.add(new JSONObject().put("viskoType", "Views").put("count", viewCount));
			data.add(new JSONObject().put("viskoType", "Viewers").put("count", viewerCount));
			data.add(new JSONObject().put("viskoType", "Transformers").put("count", transformerCount));
			data.add(new JSONObject().put("viskoType", "Parameters").put("count", paramCount));
			data.add(new JSONObject().put("viskoType", "Toolkits").put("count",	toolkitCount));
			data.add(new JSONObject().put("viskoType", "Mappers").put("count", mapperCount));
			data.add(new JSONObject().put("viskoType", "Used Formats").put("count", formatCount));
			data.add(new JSONObject().put("viskoType", "Services").put("count", serviceCount));

			jsonGraphData.put("instanceGraphData", data);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return jsonGraphData.toString();
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
