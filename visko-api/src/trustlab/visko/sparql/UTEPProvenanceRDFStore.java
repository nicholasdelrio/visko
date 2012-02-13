package trustlab.visko.sparql;

import edu.utep.trust.provenance.*;
import com.hp.hpl.jena.query.*;

public class UTEPProvenanceRDFStore {
	private RDFStore proxy;

	public UTEPProvenanceRDFStore() {
		RDFStore_Service service = new RDFStore_Service();
		proxy = service.getRDFStoreHttpPort();
	}

	public String getFormatFromArtifactURL(String artifactURL) {
		artifactURL = "\"" + artifactURL + "\"";
		String queryString = "SELECT ?formatURI\n"
				+ "WHERE {?informationURI <http://inference-web.org/2.0/pml-provenance.owl#hasURL> "
				+ artifactURL
				+ "^^<http://www.w3.org/2001/XMLSchema#anyURI> . "
				+ "?informationURI <http://inference-web.org/2.0/pml-provenance.owl#hasFormat> ?formatURI .}";

		String formats = proxy.doQuery(queryString);
		ResultSet results = ResultSetFactory.fromXML(formats);

		String formatInd = null;
		if (results.hasNext()) {
			QuerySolution solution = results.nextSolution();
			formatInd = solution.get("?formatURI").toString();
		}

		return formatInd;
	}

	public String getTypeFromArtifactURL(String artifactURL) {
		artifactURL = "\"" + artifactURL + "\"";
		String queryString = "SELECT ?informationURI ?informationType "
				+ "WHERE { "
				+ "?informationURI <http://inference-web.org/2.0/pml-provenance.owl#hasURL> "
				+ artifactURL
				+ "^^<http://www.w3.org/2001/XMLSchema#anyURI> . "
				+ "?informationURI <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?informationType .}";

		String formats = proxy.doQuery(queryString);
		ResultSet results = ResultSetFactory.fromXML(formats);

		String informationType = null;

		if (results.hasNext()) {
			QuerySolution solution = results.nextSolution();
			informationType = solution.get("?informationType").toString();
			if (!informationType
					.equals("http://inference-web.org/2.0/pml-provenance.owl#Information")) {
				return informationType;
			}
		}
		return informationType;
	}

	/**************************************************************************************/

	public String getFormatFromNodesetURI(String nodesetURI) {
		nodesetURI = "<" + nodesetURI + ">";
		String queryString = "SELECT ?y\n"
				+ "WHERE { "
				+ nodesetURI
				+ " <http://inference-web.org/2.0/pml-justification.owl#hasConclusion> ?x .\n"
				+ "?x <http://inference-web.org/2.0/pml-provenance.owl#hasFormat> ?y .}";

		String formats = proxy.doQuery(queryString);
		ResultSet results = ResultSetFactory.fromXML(formats);

		String formatInd = null;

		if (results.hasNext()) {
			QuerySolution solution = results.nextSolution();
			formatInd = solution.get("?y").toString();
		}

		return formatInd;
	}

	public String getDatasetURLFromNodesetURI(String nodesetURI) {
		nodesetURI = "<" + nodesetURI + ">";
		String queryString = "SELECT ?y "
				+ "WHERE { "
				+ nodesetURI
				+ " <http://inference-web.org/2.0/pml-justification.owl#hasConclusion> ?x . "
				+ "?x <http://inference-web.org/2.0/pml-provenance.owl#hasURL> ?y . }";

		String datasetURLs = proxy.doQuery(queryString);
		ResultSet urls = ResultSetFactory.fromXML(datasetURLs);

		String url = null;

		if (urls.hasNext()) {
			QuerySolution solution = urls.nextSolution();
			url = solution.get("?y").toString();
		}

		if (url != null)
			url = url.substring(0, url.indexOf("^^"));

		return url;
	}

	/*
	 * public String getDatasetURLFromNodesetURI(String nodesetURI) { Loader
	 * loader = new Loader(false); PMLNode ns = loader.loadNode(nodesetURI,
	 * Loader.DEFAULT_ID); PMLConclusion conc = ns.getConclusion();
	 * 
	 * if(conc == null) { System.out.println("conc is null"); return null; }
	 * else { System.out.println("conc is not null"); String url =
	 * conc.getHasURL(); return url; } }
	 */

	/*
	 * public String getDatasetURLFromNodesetURI(String nodesetURI) {
	 * DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	 * DocumentBuilder db = null; try { db = dbf.newDocumentBuilder(); } catch
	 * (ParserConfigurationException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } Document doc = null; try { doc =
	 * db.parse(nodesetURI); } catch (MalformedURLException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } catch (SAXException e)
	 * { // TODO Auto-generated catch block e.printStackTrace(); } catch
	 * (IOException e) { // TODO Auto-generated catch block e.printStackTrace();
	 * } doc.getDocumentElement().normalize(); NodeList hasURLs =
	 * doc.getElementsByTagName("pmlp:hasURL");
	 * 
	 * Node node; String url = null; for(int i = 0; i < hasURLs.getLength(); i
	 * ++) { node = hasURLs.item(i); url =
	 * node.getChildNodes().item(0).getNodeValue(); }
	 * 
	 * return url; }
	 */

	public String getTypeFromNodesetURI(String nodesetURI) {
		nodesetURI = "<" + nodesetURI + ">";
		String queryString = "SELECT ?y\n"
				+ "WHERE { "
				+ nodesetURI
				+ " <http://inference-web.org/2.0/pml-justification.owl#hasConclusion> ?x .\n"
				+ "?x <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?y .}";

		String formats = proxy.doQuery(queryString);
		ResultSet results = ResultSetFactory.fromXML(formats);

		String formatInd = null;
		if (results.hasNext()) {
			QuerySolution solution = results.nextSolution();
			formatInd = solution.get("?y").toString();
		}

		return formatInd;
	}
}
