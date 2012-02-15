package edu.utep.trustlab.visko.execution;

import java.util.HashMap;
import java.util.Vector;

import java.util.Set;

import com.hp.hpl.jena.query.ResultSet;

import edu.utep.trustlab.publish.Repository;
import edu.utep.trustlab.visko.knowledge.NickCIServer;
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

	public static void main(String[] args) {
		
		Repository.setServer(NickCIServer.getServer());
		ViskoTripleStore.setEndpointURL("http://iw.cs.utep.edu:8080/joseki/visko");
		
		// String viewerSetURI =
		// "http://rio.cs.utep.edu/ciserver/ciprojects/visko-operator/probeit.owl#probeit";
		String viewerSetURI = "http://rio.cs.utep.edu/ciserver/ciprojects/visko/mozilla-firefox1.owl#mozilla-firefox1";
		// String datasetURL =
		// "http://rio.cs.utep.edu/ciserver/ciprojects/GravityMapProvenance/esriGrid.txt";
		// String datasetURL =
		// "http://rio.cs.utep.edu/ciserver/ciprojects/HolesCodeFullPML/05598217390126823_vel.3d";

		// String viewURI =
		// "http://rio.cs.utep.edu/ciserver/ciprojects/visko/XYPlot.owl#XYPlot";

		// String datasetURL =
		// "http://giovanni.gsfc.nasa.gov/session//4FD77C10-9D9F-11E0-8032-A256126F70C8/A838B4D2-9F84-11E0-9248-C3EBB5374347/A83C6D98-9F84-11E0-B803-A51811396DEC///NULL-AERONET_AOD_L2.2-MYD04_L2.051-MIL2ASAE.0022-20112506234225.nc";
		// String formatURI =
		// "http://rio.cs.utep.edu/ciserver/ciprojects/pmlp/NETCDF.owl#NETCDF";
		// String typeURI =
		// "http://giovanni.gsfc.nasa.gov/data/aerosol/timeseries.owl#timeseries";

		/*
		 * String datasetURL =
		 * "http://rio.cs.utep.edu/ciserver/ciprojects/HolesCodeFullPML/01146509090356318_icov.3d"
		 * ; String formatURI =
		 * "http://rio.cs.utep.edu/ciserver/ciprojects/formats/BINARYINTARRAY.owl#BINARYINTARRAY"
		 * ; String typeURI =
		 * "http://rio.cs.utep.edu/ciserver/ciprojects/HolesCode/HolesCodeSAW3.owl#d7-0"
		 * ;
		 */
		String viewURI = "http://rio.cs.utep.edu/ciserver/ciprojects/visko/volume1.owl#volume1";

		String datasetURL = "http://rio.cs.utep.edu/ciserver/ciprojects/HolesCodeFullPML/02029349145023569_vel.3d";
		String formatURI = "http://rio.cs.utep.edu/ciserver/ciprojects/formats/BINARYFLOATARRAY.owl#BINARYFLOATARRAY";
		String typeURI = "http://rio.cs.utep.edu/ciserver/ciprojects/HolesCode/HolesCodeWDO.owl#d2";

		// String nodesetURI =
		// "http://rio.cs.utep.edu/ciserver/ciprojects/HolesCodeProvenance/DVelocityModelProduct_08987690948167472.owl#answer";

		UTEPProvenanceRDFStore s = new UTEPProvenanceRDFStore();
		// String formatURI = s.getFormatFromArtifactURL(datasetURL);
		// String typeURI = s.getTypeFromArtifactURL(datasetURL);

		// String formatURI = s.getFormatFromNodesetURI(nodesetURI);
		// String typeURI = s.getTypeFromNodesetURI(nodesetURI);
		// datasetURL = s.getDatasetURLFromNodesetURI(nodesetURI);

		System.out.println(datasetURL);
		System.out.println(typeURI);
		System.out.println(formatURI);

		Query query = new Query(datasetURL, formatURI, viewerSetURI);
		query.setTypeURI(typeURI);

		// query.addParameterBinding("http://trust.utep.edu/visko/services/ESRIGriddedToColoredImagePS.owl#C",
		// "rainbow");
		query.addParameterBinding(
				"http://trust.utep.edu/visko/services/ESRIGriddedToColoredImagePS.owl#B",
				"0.25");
		// query.setTargetFormatURI(targetFormat);
		query.setViewURI(viewURI);

		QueryEngine engine = new QueryEngine(query);

		PipelineSet pipes = engine.getPipelines();

		for (Pipeline pipe : pipes) {
			System.out.println("Next pipeline targetting: "
					+ pipe.getViewer().getURI());
			String resultURL = pipe.executePath(false);
			System.out.println("resultant artifact: " + resultURL);
		}
	}
}
