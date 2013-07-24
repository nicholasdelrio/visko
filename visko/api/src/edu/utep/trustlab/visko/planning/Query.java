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

import edu.utep.trustlab.contentManagement.ContentManager;
import edu.utep.trustlab.contentManagement.LocalFileSystem;
import edu.utep.trustlab.visko.execution.PipelineExecutor;
import edu.utep.trustlab.visko.execution.PipelineExecutorJob;
import edu.utep.trustlab.visko.ontology.vocabulary.ViskoV;
import edu.utep.trustlab.visko.planning.Query;
import edu.utep.trustlab.visko.planning.pipelines.PipelineSet;
import edu.utep.trustlab.visko.planning.queryParsing.QueryParser;
import edu.utep.trustlab.visko.planning.queryParsing.QueryParserV4;
import edu.utep.trustlab.visko.sparql.SPARQL_EndpointFactory;
import edu.utep.trustlab.visko.util.PMLNodesetParser;

public class Query {
	//required for pipeline path search
	private String formatURI;
	private String viewerSetURI;

	//optional pipeline path search restrictions
	private String typeURI;
	private String viewURI;
	private String datasetURL;
	private String targetFormatURI;
	private String targetTypeURI;
	private String nodesetURI;

	//flag to see if a filter operator should be added to pipelines
	private boolean dataIsFiltered;
	
	//stores values that will be fed to services
	private HashMap<String, String> parameterBindings;
	
	//string query parser
	private QueryParser parser;
	
	//string query if provided
	private String stringBasedQuery;
	
	public Query(String queryString) {	
		parameterBindings = new HashMap<String, String>();
		
		stringBasedQuery = queryString;
		
		parser = new QueryParserV4(queryString);
		parser.parse();		
		
		viewerSetURI = parser.getViewerSetURI();
		viewURI = parser.getViewURI();
		targetFormatURI = parser.getTargetFormatURI();
		targetTypeURI = parser.getTargetTypeURI();
		
		if (viewURI != null && viewURI.equals("*"))
			viewURI = null;

		dataIsFiltered = parser.dataIsFiltered();
		nodesetURI = parser.getNodesetURI();

		if (nodesetURI != null)
			populateFromNodeset(nodesetURI);
		else {
			datasetURL = parser.getContentURL();
			formatURI = parser.getFormatURI();
			typeURI = parser.getSemanticTypeURI();
		}

		setParameterBindings(parser.getParameterBindings());
	}

	private void populateFromNodeset(String nodesetURI){
		PMLNodesetParser parser = new PMLNodesetParser(nodesetURI);
		parser.parse();
		datasetURL = parser.getDatasetURL();
		formatURI = parser.getFormatURI();
		typeURI = parser.getTypeURI();
	}
	
	public String getInputStringQuery(){
		return stringBasedQuery;
	}
	
	public boolean hasTargetData(){
		return targetFormatURI != null;
	}
	
	public boolean hasValidDataPointer(){
		return QueryParserV4.isURL(datasetURL);
	}	
	
	public Query(String artifactURL, String fmtURI, String viewersetURI) {
		parameterBindings = new HashMap<String, String>();

		datasetURL = artifactURL;
		formatURI = fmtURI;
		viewerSetURI = viewersetURI;
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
	
	public boolean isExecutableQuery(){
		return this.isValidQuery() && this.hasValidDataPointer();
	}

	public boolean isValidQuery() {
		boolean hasNodeSet = this.getNodesetURI() != null;
		boolean hasFormat = this.getFormatURI() != null;
		boolean hasTargetFormat = this.getTargetFormatURI() != null;
		boolean hasViewerSet = this.getViewerSetURI() != null;
		
		boolean hasStandardQueryElements = hasFormat && hasViewerSet;
		boolean hasNodesetQueryElements = hasNodeSet && hasViewerSet;
		boolean hasTargetFormatQueryElements = (hasNodeSet || hasFormat) && hasTargetFormat;

		return hasStandardQueryElements || hasNodesetQueryElements || hasTargetFormatQueryElements;
	}
	
	public String getSourceMessage(){
		String message = "Data Source was Detected";
		if(this.getFormatURI() == null && this.getNodesetURI() == null)
			message = "Input formatURI or NodeSetURI not specified";
		return message;
	}
	
	public String getTargetMessage(){
		String message = "Target was Detected";
		if(this.getTargetFormatURI() == null && this.getViewerSetURI() == null)
			message = "Query Target (i.e., Target Format or ViewerSet) was not specified";
		return message;
	}

	public boolean hasWarnings() {
		return typeURI == null || viewURI == null || datasetURL == null;
	}

	public boolean includeFilters(){
		return dataIsFiltered;
	}
	
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
		if(typeURI == null)
			return "http://www.w3.org/2002/07/owl#Thing";
		
		return this.typeURI;
	}

	public String getViewURI() {
		return this.viewURI;
	}

	public String getViewerSetURI() {
		return this.viewerSetURI;
	}
		
	public void setTargetTypeURI(String typeURI){
		this.typeURI = typeURI;
	}
	
	public String getTargetTypeURI(){
		if(this.targetTypeURI == null)
			return "http://www.w3.org/2002/07/owl#Thing";
		
		return this.targetTypeURI;
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
	
	public static void main(String[] args){
		
		SPARQL_EndpointFactory.setUpEndpointConnection("C:/Users/Public/workspace-visko/visko-web/WebContent/registry-tdb");
		
		LocalFileSystem fs = new LocalFileSystem("http://iw.cs.utep.edu:8080/toolkits/output/", "C:/Users/Public/workspace-visko/api/output/");
		ContentManager.setWorkspacePath("C:/Users/Public/workspace-visko/api/output/");
		ContentManager.setProvenanceContentManager(fs);
		
		/*
		String url = "http://rio.cs.utep.edu/ciserver/ciprojects/GravityMapProvenance/gravityDataset.txt";
		String formatURI = "http://openvisko.org/rdf/pml2/formats/SPACESEPARATEDVALUES.owl#SPACESEPARATEDVALUES";
		String viewerSetURI = "http://localhost:8080/visko-web/registry/module_webbrowser.owl#web-browser";
		String typeURI = "http://rio.cs.utep.edu/ciserver/ciprojects/CrustalModeling/CrustalModeling.owl#d19";
		String viewURI = ViskoV.INDIVIDUAL_URI_2D_ContourMap;
		*/
		/*
		String url = "http://testerion.owl";
		String formatURI = "https://raw.github.com/nicholasdelrio/visko/master/resources/formats/RDFXML.owl#RDFXML";
		String viewerSetURI = "https://raw.github.com/nicholasdelrio/visko-packages-rdf/master/package_custom.owl#data-driven-documents";
		String typeURI = "https://raw.github.com/nicholasdelrio/visko/master/resources/ontology/visko.owl#VisKo_KnowledgeBase";
		String viewURI = ViskoV.INDIVIDUAL_URI_2D_VisKo_DataTransformations_ForceGraph;
		*/

		String url = "http://rio.cs.utep.edu/ciserver/ciprojects/GravityMapProvenance/gravityDataset.txt";
		String formatURI = "http://openvisko.org/rdf/pml2/formats/PNG.owl#PNG";
		String viewerSetURI = "http://localhost:8080/visko-web/registry/module_webbrowser.owl#web-browser";
		String targetFormatURI = "http://openvisko.org/rdf/pml2/formats/PNG.owl#PNG";
		//String typeURI = "http://openvisko.org/rdf/ontology/visko.owl#VisKo_KnowledgeBase";
		
		String viewURI = ViskoV.INDIVIDUAL_URI_3D_SurfacePlot;
		
		Query query = new Query(url, formatURI, viewerSetURI);
		//query.setViewURI(viewURI);
		query.setTargetFormatURI(targetFormatURI);
		
		QueryEngine engine = new QueryEngine(query);
		
		PipelineSet pipes = null;
		
		pipes = engine.getPipelines();
		PipelineExecutor executor = new PipelineExecutor();

		if(pipes.size() > 0){
			System.out.println(pipes.firstElement());
				
			PipelineExecutorJob job = new PipelineExecutorJob(pipes.firstElement());
			job.setAsSimulatedJob();
			job.setProvenanceLogging(true);
				
			executor.setJob(job);
			executor.process();
		
			while(executor.isAlive()){
			}
				
			System.out.println("Final Result = " + job.getFinalResultURL());	
			System.out.println("pml provenance = " + job.getPMLQueryURI().toASCIIString());
			System.out.println("pml provenance = " + job.getProvQueryURI().toASCIIString());
		}
	}
}