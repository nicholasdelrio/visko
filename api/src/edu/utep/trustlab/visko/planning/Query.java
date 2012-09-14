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


package edu.utep.trustlab.visko.planning;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import com.hp.hpl.jena.query.ResultSet;

import edu.utep.trustlab.contentManagement.ContentManager;
import edu.utep.trustlab.contentManagement.LocalFileSystem;
import edu.utep.trustlab.visko.execution.PipelineExecutor;
import edu.utep.trustlab.visko.execution.PipelineExecutorJob;
import edu.utep.trustlab.visko.ontology.vocabulary.ViskoV;
import edu.utep.trustlab.visko.planning.Query;
import edu.utep.trustlab.visko.planning.queryParsing.QueryParser;
import edu.utep.trustlab.visko.planning.queryParsing.QueryParserV2;
import edu.utep.trustlab.visko.planning.queryParsing.QueryParserV3;
import edu.utep.trustlab.visko.sparql.QueryRDFDocument;
import edu.utep.trustlab.visko.sparql.SPARQL_EndpointFactory;
import edu.utep.trustlab.visko.sparql.ViskoTripleStore;
import edu.utep.trustlab.visko.util.GetURLContents;
import edu.utep.trustlab.visko.util.ResultSetToVector;

public class Query {
	// required information for VisKo reasoning
	private String formatURI;
	private String viewerSetURI;
	private HashMap<String, String> parameterBindings;
	
	
	private String inputQuery;
	
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

	public static void main(String[] args){
		
		SPARQL_EndpointFactory.setUpEndpointConnection("C:/Users/Public/git/visko/tdb");
		
		LocalFileSystem fs = new LocalFileSystem("http://iw.cs.utep.edu:8080/toolkits/output/", "C:/Users/Public/git/visko/api/output/");
		ContentManager.setWorkspacePath("C:/Users/Public/git/visko/api/output/");
		ContentManager.setProvenanceContentManager(fs);
		
		
		ViskoTripleStore ts = new ViskoTripleStore();
		ResultSet results = ts.getOperators();
		
		Vector<String> stringResults = ResultSetToVector.getVectorFromResultSet(results, "operator");
		for(String result: stringResults){
			System.out.println(result);
		}
		
		
		String url = "http://rio.cs.utep.edu/ciserver/ciprojects/GravityMapProvenance/gravityDataset.txt";
		String formatURI = "https://raw.github.com/nicholasdelrio/visko/master/resources/formats/SPACESEPARATEDVALUES.owl#SPACESEPARATEDVALUES";
		String viewerSetURI = "https://raw.github.com/nicholasdelrio/visko-packages-rdf/master/package_mozilla.owl#mozilla-firefox";
		String typeURI = "http://rio.cs.utep.edu/ciserver/ciprojects/CrustalModeling/CrustalModeling.owl#d19";
		String viewURI = ViskoV.INDIVIDUAL_URI_ContourMap;
		Query query = new Query(url, formatURI, viewerSetURI);
		query.setTypeURI(typeURI);
		query.setViewURI(viewURI);
		
		QueryEngine engine = new QueryEngine(query);
		PipelineSet pipes = engine.getPipelines();
	
		for(Pipeline pipe : pipes){
			System.out.println(pipe);
			System.out.println("generates view: " + pipe.getViewURI());
			
			/*
			PipelineExecutorJob job = new PipelineExecutorJob(pipes.firstElement(), true);
			PipelineExecutor executor = new PipelineExecutor(job);
			executor.process();
			
			while(executor.isAlive()){
			}
			
			System.out.println("Final Result = " + job.getFinalResultURL());*/
		}
	}
	
	public Query(String queryString) {
		inputQuery = queryString;
		
		parameterBindings = new HashMap<String, String>();
		
		parser = new QueryParserV3(queryString);
		
		parser.parse();

		if(!parser.isValidQuery()){
			parser = new QueryParserV2(queryString);
			parser.parse();
		}
		
		
		this.viewerSetURI = parser.getViewerSetURI();
		this.viewURI = parser.getViewURI();
		if (viewURI != null && viewURI.equals("*"))
			viewURI = null;

		this.nodesetURI = parser.getNodesetURI();

		if (nodesetURI != null) {
			try {
				String nodesetContents = GetURLContents.downloadText(nodesetURI);
				QueryRDFDocument rdf = new QueryRDFDocument();

				Vector<String[]> results = rdf.getURLFormatAndType(nodesetContents, nodesetURI);
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

	public boolean hasValidDataPointer(){
		return QueryParserV3.isURL(datasetURL);
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
	
	
	public String constructQueryFromVariables(){
		String newViewURI = viewURI;
		if(viewURI == null){
			newViewURI = "*";
		}
		
		String reconstructedQuery = 
				"VISUALIZE " + datasetURL + "\n" +
				"AS " + newViewURI + "\n" +
				"IN " + viewerSetURI + "\n" +
				"WHERE ";

		if(getFormatURI() != null)
				reconstructedQuery += "\n\tFORMAT = " + getFormatURI() + "\n";		
		if(getTypeURI() != null)
				reconstructedQuery += "\tAND TYPE = " + getTypeURI() + "\n";
						
		Set<String> parameterURIs = parameterBindings.keySet();
				
		if(parameterURIs.size() > 0){
			Iterator<String> paramURIs = parameterURIs.iterator();
			String paramURI;
			while(paramURIs.hasNext()){
				paramURI = paramURIs.next();			
				reconstructedQuery = setBinding(reconstructedQuery, paramURI, parameterBindings.get(paramURI));
			}
		}			
		return reconstructedQuery;
	}

	public String getInputQuery(){
		return inputQuery;
	}
	
	public String toString() {
		if(parser == null)
			return constructQueryFromVariables();
					
		String[] tokens = parser.getTokens();
		
		String reconstructedQuery = "";

		for (String token : tokens) {
			if (token.equalsIgnoreCase("PREFIX")){
				token = token.toUpperCase();
				reconstructedQuery += "\n";
			}
			else if (token.equalsIgnoreCase("VISUALIZE")){
				token = token.toUpperCase();
				reconstructedQuery += "\n";
			}
			else if (token.equalsIgnoreCase("AS")){
				token = token.toUpperCase();
				reconstructedQuery += "\n";
			}
			else if (token.equalsIgnoreCase("IN")){
				token = token.toUpperCase();
				reconstructedQuery += "\n";
			}
			else if (token.equalsIgnoreCase("WHERE")){
				token = token.toUpperCase();
				reconstructedQuery += "\n" + token;
				break;
			}

			reconstructedQuery += token + " ";
		}

		if(getFormatURI() != null)
			reconstructedQuery += "\n\tFORMAT = " + getFormatURI() + "\n";
		if(getTypeURI() != null)
			reconstructedQuery += "\tAND TYPE = " + getTypeURI() + "\n";
		
		Set<String> parameterURIs = parameterBindings.keySet();
		
		if(parameterURIs.size() > 0){
			Iterator<String> paramURIs = parameterURIs.iterator();
			String paramURI;
			while(paramURIs.hasNext()){
				paramURI = paramURIs.next();			
				reconstructedQuery = setBinding(reconstructedQuery, paramURI, parameterBindings.get(paramURI));
			}
		}
		return reconstructedQuery;
	}
	
	private String getExistingPrefix(String uri){
		
		if(parser == null)
			return null;
		
		//check from query parser bindings
		Set<String> keys = parser.getPrefixes().keySet();
		for(String key : keys){
			String prefixValue = parser.getPrefixes().get(key);
			if(prefixValue.equalsIgnoreCase(uri + "#"))
				return key;
		}	
		return null;
	}
	
	private String setBinding(String query, String paramURI, String boundValue){
		
		String url = paramURI.substring(0, paramURI.indexOf("#"));
		String fragment = paramURI.substring(paramURI.indexOf("#") + 1);
		
		String prefix = getExistingPrefix(url);

		if(prefix != null){
			query = query + "\tAND " + prefix + ":" + fragment + " = " + boundValue + "\n";
		}
		else
			query = query + "\tAND " + paramURI + " = " + boundValue + "\n";
		
		return query;
	}
}
