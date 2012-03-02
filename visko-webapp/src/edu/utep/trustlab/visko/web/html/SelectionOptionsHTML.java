package edu.utep.trustlab.visko.web.html;

import java.net.URI;

import edu.utep.trustlab.visko.sparql.ViskoTripleStore;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFactory;

import edu.utep.trust.provenance.*;

public class SelectionOptionsHTML {
	public static final String DEFAULT = "default";
	private ViskoTripleStore viskoStore;
	private RDFStore store;

	public SelectionOptionsHTML() {
		viskoStore = new ViskoTripleStore();
		setUp();
	}

	private void setUp() {
		if (store == null) {
			RDFStore_Service service = new RDFStore_Service();
			store = service.getRDFStoreHttpPort();
		}
	}

	private static String getURIFragment(String uri) {
		try {
			return new URI(uri).getFragment();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getFormats() {
		String formatsXML = store.getFormats();
		ResultSet formats = ResultSetFactory.fromXML(formatsXML);

		String options = "<option value=\"" + DEFAULT
				+ "\">-- Choose Format --</option>";
		String formatURI;
		String option = "";
		while (formats.hasNext()) {
			formatURI = formats.nextSolution().get("?format").toString();
			option = "<option title=\"" + formatURI + "\" value=\"" + formatURI
					+ "\">" + getURIFragment(formatURI) + "</option>";
			options += option;
		}

		return options;
	}

	public String getViewerSets() {
		ResultSet viewerSets = viskoStore.getViewerSets();

		String options = "<option value=\"" + DEFAULT
				+ "\">-- Choose ViewerSet --</option>";
		String viewerSetURI;
		String option = "";
		while (viewerSets.hasNext()) {
			viewerSetURI = viewerSets.nextSolution().get("?viewerSet")
					.toString();
			option = "<option title=\"" + viewerSetURI + "\" value=\""
					+ viewerSetURI + "\">" + getURIFragment(viewerSetURI)
					+ "</option>";
			options += option;
		}

		return options;
	}

	public String getTypes() {
		String typesXML = store.getInformationSubclasses();
		ResultSet types = ResultSetFactory.fromXML(typesXML);
		String options = "<option value=\"" + DEFAULT
				+ "\">-- Choose Type --</option>";
		String typeURI;
		String label;
		String option = "";
		while (types.hasNext()) {
			QuerySolution solution = types.nextSolution();
			typeURI = solution.get("?informationSubclass").toString();
			label = solution.get("?subclassLabel").toString();
			option = "<option title=\"" + typeURI + "\" value=\"" + typeURI
					+ "\">" + label + "</option>";
			options += option;
		}

		return options;
	}

	public String getParameters() {
		ResultSet parameters = viskoStore.getAllParameters();
		String paramURI;
		String options = "<option value=\"" + DEFAULT
				+ "\">-- Choose Parameter --</option>";
		String option = "";
		while (parameters.hasNext()) {
			paramURI = parameters.nextSolution().get("?param").toString();

			if (!(paramURI.contains("url") || paramURI.contains("datasetURL"))) {
				option = "<option title=\"" + paramURI + "\" value=\""
						+ paramURI + "\">" + getURIFragment(paramURI)
						+ "</option>";
				options += option;
			}
		}

		return options;
	}

	public String getViskoViews() {
		ResultSet views = viskoStore.getViews();
		String viewURI;
		String options = "<option value=\"" + DEFAULT
				+ "\">-- Choose View --</option>";
		String option = "";
		while (views.hasNext()) {
			viewURI = views.nextSolution().get("?view").toString();
			option = "<option title=\"" + viewURI + "\" value=\"" + viewURI
					+ "\">" + getURIFragment(viewURI) + "</option>";
			options += option;
		}

		return options;
	}
}
