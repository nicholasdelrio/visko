package edu.utep.trustlab.visko.execution;

import java.util.HashMap;
import java.util.Vector;

import java.util.Set;

import com.hp.hpl.jena.query.ResultSet;

import edu.utep.trustlab.repository.Repository;
import edu.utep.trustlab.visko.ontology.model.OWLSModel;
import edu.utep.trustlab.visko.ontology.service.OWLSService;
import edu.utep.trustlab.visko.sparql.UTEPProvenanceRDFStore;
import edu.utep.trustlab.visko.sparql.ViskoTripleStore;
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

	public HashMap<String, String> getParameterBindings() {
		return this.parameterBindings;
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
