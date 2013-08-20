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
import java.util.Vector;
import java.util.Set;

import com.hp.hpl.jena.query.ResultSet;

import edu.utep.trustlab.contentManagement.ContentManager;
import edu.utep.trustlab.contentManagement.LocalFileSystem;
import edu.utep.trustlab.visko.planning.operatorPaths.AllPathsBuilder;
import edu.utep.trustlab.visko.planning.operatorPaths.OperatorPath;
import edu.utep.trustlab.visko.planning.operatorPaths.OperatorPaths;
import edu.utep.trustlab.visko.planning.operatorPaths.OperatorPathsBuilder;
import edu.utep.trustlab.visko.planning.operatorPaths.QueryDrivenBuilder;
import edu.utep.trustlab.visko.planning.pipelines.OperatorPathsToPipelines;
import edu.utep.trustlab.visko.planning.pipelines.PipelineSet;
import edu.utep.trustlab.visko.sparql.SPARQL_EndpointFactory;
import edu.utep.trustlab.visko.sparql.ViskoTripleStore;
import edu.utep.trustlab.visko.util.ResultSetToVector;

public class QueryEngine {
	private Query query;
	private OperatorPathsBuilder builder;
	private HashMap<String, String> parameterBindings;
	private PipelineSet pipelines;
	private OperatorPaths operatorPaths;
	private ViskoTripleStore ts;

	public QueryEngine(Query q) {
		ts = new ViskoTripleStore();
		query = q;
		
		if(q != null)
			builder = new QueryDrivenBuilder(query);
		else
			builder = new AllPathsBuilder();
		
		parameterBindings = new HashMap<String, String>();
	}

	public Query getQuery() {
		return query;
	}

	public PipelineSet getPipelines() {
		if (pipelines == null)
			createPipelines();
		return pipelines;
	}
	
	public OperatorPaths getOperatorPaths(){
		if(operatorPaths == null)
			operatorPaths = builder.getOperatorPaths();
		
		return operatorPaths;
	}

	private void createPipelines() {
		String typeConstraintURI = query.getTypeURI();

		if (typeConstraintURI != null)
			loadParameterBindingsFromProfile(typeConstraintURI);

		// this one might replace bindings in the profile, but that is good since query bindings take precedence
		if (query.getParameterBindings().size() > 0)
			loadParameterBindingsFromQuery();

		operatorPaths = builder.getOperatorPaths();
		pipelines = new OperatorPathsToPipelines(query, operatorPaths).getPipelines();
		pipelines.setParameterBindings(parameterBindings);
		pipelines.setArtifactURL(query.getArtifactURL());
	}
	
	public boolean isAlreadyVisualizableWithViewerSet() {
		return ts.isAlreadyVisualizableWithViewerSet(query.getFormatURI(), query.getTypeURI(), query.getViewerSetURI());
	}
	
	public boolean isAlreadyInGivenTypeAndFormat(String typeURI, String formatURI){
		return ts.isSourceTypeAndFormatCompatibleWithTargetTypeAndFormat(query.getTypeURI(), query.getFormatURI(), typeURI, formatURI);
	}


	public void updatePipelinesWithNewParameterBindings(){
		pipelines.setParameterBindings(parameterBindings);
	}

	public void loadParameterBindingsFromProfile(String dataTypeURI) {
		ResultSet results = ts.getParameterBindings(dataTypeURI);
		Vector<String[]> bindings = ResultSetToVector.getVectorPairsFromResultSet(results, "param", "value");

		for (String[] binding : bindings)
			parameterBindings.put(binding[0], binding[1]);
	}

	public void loadParameterBindingsFromQuery() {
		Set<String> keySet = query.getParameterBindings().keySet();
		for (String key : keySet)
			parameterBindings.put(key, query.getParameterBindings().get(key));
	}
	
	public static void main(String[] args){
		SPARQL_EndpointFactory.setUpEndpointConnection("C:/Users/Public/workspace-visko/visko-web/WebContent/registry-tdb");
		
		LocalFileSystem fs = new LocalFileSystem("http://iw.cs.utep.edu:8080/toolkits/output/", "C:/Users/Public/git/visko/api/output/");
		ContentManager.setWorkspacePath("C:/Users/Public/git/visko/api/output/");
		ContentManager.setProvenanceContentManager(fs);

		QueryEngine engine = new QueryEngine(null);
		OperatorPaths paths = engine.getOperatorPaths();
	
		for(OperatorPath path : paths)
			System.out.println(path);
	}
}