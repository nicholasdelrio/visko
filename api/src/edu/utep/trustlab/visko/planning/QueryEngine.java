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

import edu.utep.trustlab.visko.planning.PipelineSet;
import edu.utep.trustlab.visko.planning.PipelineSetBuilder;
import edu.utep.trustlab.visko.planning.Query;
import edu.utep.trustlab.visko.util.ResultSetToVector;

public class QueryEngine {
	private Query query;
	private PipelineSetBuilder builder;
	private HashMap<String, String> parameterBindings;
	private PipelineSet pipelines;

	public QueryEngine(Query q) {
		query = q;
		builder = new PipelineSetBuilder(query);
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

	private void createPipelines() {
		String formatURI = query.getFormatURI();
		String viewerSetURI = query.getViewerSetURI();
		String typeConstraintURI = query.getTypeURI();
		String viewConstraintURI = query.getViewURI();
		String targetFormatURI = query.getTargetFormatURI();

		if (typeConstraintURI != null) {
			loadParameterBindingsFromProfile(typeConstraintURI);
		}

		// this one might replace bindings in the profile, but that is good
		// since query bindings take precedence
		if (query.getParameterBindings().size() > 0) {
			loadParameterBindingsFromQuery();
		}

		if (targetFormatURI != null)
			builder.setPipelinesUsingTargetFormat(formatURI, targetFormatURI,
					viewConstraintURI);
		else
			builder.setPipelines(formatURI, viewerSetURI, viewConstraintURI);

		pipelines = builder.getPipelines();
		pipelines.setParameterBindings(parameterBindings);
		pipelines.setArtifactURL(query.getArtifactURL());
	}

	public boolean isAlreadyVisualizableWithViewerSet() {
		return builder.isAlreadyVisualizableWithViewerSet(query.getFormatURI(),
				query.getViewerSetURI());
	}

	public boolean canBeVisualizedWithViewerSet() {
		return builder.formatPathExistsForViewerSet(query.getFormatURI(),
				query.getViewerSetURI());
	}

	public boolean canBeVisualizedWithTargetFormat() {
		return builder.formatPathExistsForTargetFormat(query.getFormatURI(),
				query.getTargetFormatURI());
	}
	
	public void updatePipelinesWithNewParameterBindings(){
		pipelines.setParameterBindings(parameterBindings);
	}

	public void loadParameterBindingsFromProfile(String dataTypeURI) {
		ResultSet results = builder.getTripleStore().getParameterBindings(
				dataTypeURI);
		Vector<String[]> bindings = ResultSetToVector
				.getVectorPairsFromResultSet(results, "param", "value");

		for (String[] binding : bindings) {
			parameterBindings.put(binding[0], binding[1]);
		}
	}

	public void loadParameterBindingsFromQuery() {
		Set<String> keySet = query.getParameterBindings().keySet();
		for (String key : keySet) {
			parameterBindings.put(key, query.getParameterBindings().get(key));
		}
	}
}
