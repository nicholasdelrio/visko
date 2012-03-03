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


package edu.utep.trustlab.visko.execution;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Vector;

import edu.utep.trustlab.visko.sparql.QueryRDFDocument;
import edu.utep.trustlab.visko.util.GetURLContents;

public class Query {
	// required information for VisKo reasoning
	private String formatURI;
	private String viewerSetURI;
	private HashMap<String, String> parameterBindings;

	// optional information used for filtering paths
	private String typeURI;
	private String viewURI;

	// optional target format
	private String targetFormatURI;

	// the dataset that the query is specifying the visualization for
	private String datasetURL;

	// the nodesetURI
	private String nodesetURI;

	private QueryParser parser;

	public static void main(String[] args) {
		String queryString = "PREFIX ns1 http://test1.owl# PREFIX ns http://test.owl# SELECT ns:contourMap IN-VIEWER ns1:viewer1 FROM LOCAL http://poop.owl FORMAT ns:format1 TYPE ns1:type1 WHERE http://spacing = 1 AND ns:color = marroon AND ns1:bg = black";
		String queryString1 = "PREFIX ns1 http://test1.owl# PREFIX ns http://test.owl# SELECT ns:contourMap IN-VIEWER ns1:viewer1 FROM NODESET http://rio.cs.utep.edu/ciserver/ciprojects/GravityMapProvenance/esriGrid.txt_09509250903594285.owl#answer WHERE http://spacing = 1 AND ns:color = marroon AND ns1:bg = black";
		String queryString2 = "PREFIX ns1 http://test1.owl# PREFIX ns http://test.owl# SELECT ns:contourMap IN-VIEWER ns1:viewer1 FROM http://somedata.txt FORMAT ns:format1 TYPE ns1:type1 WHERE http://spacing = 1 AND ns:color = marroon AND ns1:bg = black";

		Query q = new Query(queryString1);
		System.out.println(q.isValidQuery());

		System.out.println(q);
	}

	public Query(String queryString) {
		parameterBindings = new HashMap<String, String>();
		parser = new QueryParser(queryString);
		parser.parse();

		this.viewerSetURI = parser.getViewerSetURI();
		this.viewURI = parser.getViewURI();
		if (viewURI != null && viewURI.equals("*"))
			viewURI = null;

		this.nodesetURI = parser.getNodesetURI();

		if (nodesetURI != null) {
			try {
				String nodesetContents = GetURLContents
						.downloadText(nodesetURI);
				QueryRDFDocument rdf = new QueryRDFDocument();

				Vector<String[]> results = rdf.getURLFormatAndType(
						nodesetContents, nodesetURI);
				// rdf.close();
				datasetURL = results.firstElement()[0].split("\\^\\^")[0];
				System.out.println("datasetURL " + datasetURL);
				formatURI = results.firstElement()[1];
				typeURI = results.firstElement()[2];

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			this.datasetURL = parser.getContentURL();
			this.formatURI = parser.getFormatURI();
			this.typeURI = parser.getSemanticTypeURI();
		}

		this.setParameterBindings(parser.getParameterBindings());
	}

	public Query(String artifactURL, String fmtURI, String viewerSetURI) {
		this.datasetURL = artifactURL;
		this.formatURI = fmtURI;
		this.viewerSetURI = viewerSetURI;
		parameterBindings = new HashMap<String, String>();
	}

	public void addParameterBinding(String variableURI, String value) {
		parameterBindings.put(variableURI, value);
	}

	public void setParameterBindings(HashMap<String, String> bindings) {
		parameterBindings = bindings;
	}

	public HashMap<String, String> getParameterBindings() {
		return parameterBindings;
	}

	public void setTargetFormatURI(String fmtURI) {
		this.targetFormatURI = fmtURI;
	}

	public String getTargetFormatURI() {
		return targetFormatURI;
	}

	public void setTypeURI(String uri) {
		typeURI = uri;
	}

	public void setViewURI(String uri) {
		viewURI = uri;
	}

	public boolean isValidQuery() {
		return (formatURI != null && (viewerSetURI != null || targetFormatURI != null));
	}

	public boolean hasWarnings() {
		return typeURI == null || viewURI == null || datasetURL == null;
	};

	public String getFormatURI() {
		return this.formatURI;
	}

	public String getNodesetURI() {
		return this.nodesetURI;
	}

	public String getArtifactURL() {
		return datasetURL;
	}

	public String getTypeURI() {
		return this.typeURI;
	}

	public String getViewURI() {
		return this.viewURI;
	}

	public String getViewerSetURI() {
		return this.viewerSetURI;
	}

	public String toString() {
		String[] tokens = parser.getTokens();

		String reconstructedQuery = "";

		for (String token : tokens) {
			if (token.equalsIgnoreCase("PREFIX"))
				reconstructedQuery += "\n";

			else if (token.equalsIgnoreCase("SELECT"))
				reconstructedQuery += "\n";

			else if (token.equalsIgnoreCase("FROM"))
				reconstructedQuery += "\n";

			else if (token.equalsIgnoreCase("FORMAT"))
				reconstructedQuery += "\n\t";

			else if (token.equalsIgnoreCase("TYPE"))
				reconstructedQuery += "\n\t";

			else if (token.equalsIgnoreCase("WHERE"))
				reconstructedQuery += "\n";
			else if (token.equalsIgnoreCase("AND"))
				reconstructedQuery += "\n\t";

			reconstructedQuery += token + " ";
		}

		return reconstructedQuery.trim();
	}
}
