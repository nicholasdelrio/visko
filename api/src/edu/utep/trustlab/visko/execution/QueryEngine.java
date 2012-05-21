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

import java.util.HashMap;
import java.util.Vector;

import java.util.Set;

import com.hp.hpl.jena.query.ResultSet;

import edu.utep.trustlab.visko.ontology.model.OWLSModel;
import edu.utep.trustlab.visko.ontology.service.OWLSService;
import edu.utep.trustlab.visko.sparql.QueryRDFDocument;
import edu.utep.trustlab.visko.util.ResultSetToVector;

public class QueryEngine {
	private Query query;
	private PipelineSetBuilder builder;
	private HashMap<String, String> parameterBindings;
	private PipelineSet pipelines;

	public QueryEngine(Query q) {
		query = q;
		builder = new PipelineSetBuilder();
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

		loadParameterBindingsFromExtractor();

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

	public void loadParameterBindingsFromExtractor() {
		ResultSet results = builder.getTripleStore().getExtractorService(
				query.getTypeURI(), query.getFormatURI());
		Vector<String> extractorServices = ResultSetToVector
				.getVectorFromResultSet(results, "service");

		ResultSet profileResults = builder.getTripleStore()
				.getExtractedProfile(query.getTypeURI(), query.getFormatURI());

		if (!profileResults.hasNext())
			return;

		Vector<String> profile = ResultSetToVector.getVectorFromResultSet(
				profileResults, "profile");
		String profileURI = profile.get(0);

		int index = profileURI.indexOf("^^");
		profileURI = profileURI.substring(0, index);
		System.out.println(profileURI);

		if (extractorServices.size() > 0 && query.getArtifactURL() != null) {
			String profileRDFContents = null;
			String serviceURI = extractorServices.get(0);
			OWLSService service = new OWLSService(serviceURI, new OWLSModel());
			ExtractorExecutor executor = new ExtractorExecutor(service);
			try {
				profileRDFContents = executor.executeExtractor(query
						.getArtifactURL());
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (profileRDFContents != null) {

				QueryRDFDocument ts = new QueryRDFDocument();

				ResultSet bindingResults = ts
						.getParameterBindingsFromProfileURI(profileRDFContents,
								profileURI);
				Vector<String[]> bindings = ResultSetToVector
						.getVectorPairsFromResultSet(bindingResults, "param",
								"value");
				ts.close();
				System.out.println("bindings length: " + bindings.size());
				for (String[] binding : bindings) {
					System.out.println("parameterURI: " + binding[0]
							+ ", value: " + binding[1]);

					parameterBindings.put(binding[0], binding[1]);
				}
			}
		}
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
