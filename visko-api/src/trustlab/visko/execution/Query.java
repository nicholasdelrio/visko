package trustlab.visko.execution;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Vector;

import trustlab.visko.sparql.QueryRDFDocument;
import trustlab.visko.util.GetURLContents;

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
