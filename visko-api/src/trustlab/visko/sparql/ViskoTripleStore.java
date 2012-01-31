package trustlab.visko.sparql;

import com.hp.hpl.jena.query.*;

public class ViskoTripleStore {
	
	private static String endpointURL;
	
	public static void setEndpointURL(String url){
		endpointURL = url;
	}
	
	public static String getEndpontURL(){
		return endpointURL;
	}
	
	private ViskoSPARQLEndpoint endpoint;
	
	public ViskoTripleStore(){
		endpoint = new ViskoSPARQLEndpoint(endpointURL);
	}
	
	public static String QUERY_PREFIX = "PREFIX viskoV: <http://trust.utep.edu/visko/ontology/visko-view-v3.owl#> "
			+ "PREFIX viskoO: <http://trust.utep.edu/visko/ontology/visko-operator-v3.owl#> "
			+ "PREFIX viskoS: <http://trust.utep.edu/visko/ontology/visko-service-v3.owl#> "
			+ "PREFIX owlsService: <http://www.daml.org/services/owl-s/1.2/Service.owl#> "
			+ "PREFIX owlsProcess: <http://www.daml.org/services/owl-s/1.2/Process.owl#> "
			+ "PREFIX owl: <http://www.w3.org/2002/07/owl#> "
			+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
			+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> "
			+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> ";

	public ResultSet getParameterBindingsQuery(String typeURI) {
		typeURI = "\"" + typeURI + "\"^^xsd:anyURI";
		String stringQuery = QUERY_PREFIX + "SELECT ?param ?value " + "WHERE {"
				+ "?profile viskoS:profiles " + typeURI + " . "
				+ "?profile viskoS:declaresBindings ?binding ."
				+ "?binding owlsProcess:toParam ?param . "
				+ "?binding owlsProcess:valueData ?value . " + "}";

		return endpoint.executeQuery(stringQuery);
	}

	public ResultSet getProfiles(String typeURI) {
		typeURI = "\"" + typeURI + "\"^^xsd:anyURI";

		String stringQuery = QUERY_PREFIX + "SELECT ?profile " + "WHERE {"
				+ "?profile viskoS:profiles " + typeURI + " . " + "}";

		return endpoint.executeQuery(stringQuery);
	}

	public ResultSet getFormatsSupportedByViewer(String viewerURI) {
		viewerURI = "<" + viewerURI + ">";

		String stringQuery = QUERY_PREFIX + "SELECT ?format " + "WHERE {"
				+ viewerURI + " viskoO:operatesOn ?format . " + "}";

		return endpoint.executeQuery(stringQuery);
	}

	public ResultSet getAllParameters() {
		String stringQuery = QUERY_PREFIX + "SELECT ?param " + "WHERE { "
				+ "?service rdf:type owlsService:Service . "
				+ "?service owlsService:describedBy ?process ."
				+ "?process owlsProcess:hasInput ?param ." + "}";
		return endpoint.executeQuery(stringQuery);
	}

	public ResultSet getOWLSServices() {
		String stringQuery = QUERY_PREFIX + "SELECT ?service " + "WHERE { "
				+ "?service rdf:type owlsService:Service . " + "}";

		return endpoint.executeQuery(stringQuery);

	}

	public ResultSet getTransformers() {
		String stringQuery = QUERY_PREFIX + "SELECT ?trans ?transLbl "
				+ "WHERE { " + "?trans rdf:type viskoO:Transformer . "
				+ "?trans rdfs:label ?transLbl . " + "}";

		return endpoint.executeQuery(stringQuery);
	}

	public ResultSet getTransformerInformation() {
		String stringQuery = QUERY_PREFIX
				+ "SELECT ?trans ?transLbl ?input ?output " + "WHERE { "
				+ "?trans rdf:type viskoO:Transformer . "
				+ "?trans rdfs:label ?transLbl . "
				+ "?trans viskoO:operatesOn ?input. "
				+ "?trans viskoO:transformsTo ?output. " + "}";
		return endpoint.executeQuery(stringQuery);
	}

	public ResultSet getToolkits() {
		String stringQuery = QUERY_PREFIX + "SELECT ?toolkit " + "WHERE { "
				+ "?toolkit rdf:type viskoS:Toolkit . " + "}";

		return endpoint.executeQuery(stringQuery);
	}

	public ResultSet getOperatedOnFormats(String viskoOperatorURI) {
		viskoOperatorURI = "<" + viskoOperatorURI + ">";

		String stringQuery = QUERY_PREFIX + "SELECT ?inputFormat " + "WHERE { "
				+ viskoOperatorURI + " viskoO:operatesOn ?inputFormat . " + "}";
		return endpoint.executeQuery(stringQuery);
	}

	public ResultSet getOperatedOnFormats() {
		String stringQuery = QUERY_PREFIX + "SELECT DISTINCT ?inputFormat "
				+ "WHERE { " + "?operator viskoO:operatesOn ?inputFormat . "
				+ "}";
		return endpoint.executeQuery(stringQuery);
	}

	public boolean canBeVisualizedWithViewerSet(String formatURI,
			String viewerSetURI) {
		formatURI = "<" + formatURI + ">";
		viewerSetURI = "<" + viewerSetURI + ">";

		String stringQuery = QUERY_PREFIX + "ASK " + "WHERE {{ " + "?viewer"
				+ " viskoO:partOfViewerSet " + viewerSetURI + ". "
				+ "?viewer viskoO:operatesOn ?format . " + formatURI
				+ " viskoO:canBeTransformedToTransitive ?format . } UNION { "
				+ "?viewer" + " viskoO:partOfViewerSet " + viewerSetURI + ". "
				+ "?viewer viskoO:operatesOn ?format . " + formatURI
				+ " viskoO:canBeTransformedTo ?format . } UNION { " + "?viewer"
				+ " viskoO:partOfViewerSet " + viewerSetURI + ". "
				+ "?viewer viskoO:operatesOn ?format . " + "}}";

		return endpoint.executeAskQuery(stringQuery);
	}

	public boolean isAlreadyVisualizableWithViewerSet(String formatURI,
			String viewerSetURI) {

		formatURI = "<" + formatURI + ">";
		viewerSetURI = "<" + viewerSetURI + ">";

		String stringQuery = QUERY_PREFIX + "ASK " + "WHERE { " + "?viewer"
				+ " viskoO:partOfViewerSet " + viewerSetURI + ". "
				+ "?viewer viskoO:operatesOn " + formatURI + " . }";
		return endpoint.executeAskQuery(stringQuery);
	}

	public ResultSet isAlreadyVisualizable(String formatURI) {

		formatURI = "<" + formatURI + ">";

		String stringQuery = QUERY_PREFIX + "ASK " + "WHERE { "
				+ "?viewer a viskoO:Viewer. " + "?viewer viskoO:operatesOn "
				+ formatURI + " . }";
		return endpoint.executeQuery(stringQuery);
	}

	public ResultSet getTargetViewerOfViewerSet(String formatURI,
			String viewerSetURI) {

		formatURI = "<" + formatURI + ">";
		viewerSetURI = "<" + viewerSetURI + ">";

		String stringQuery = QUERY_PREFIX + "SELECT ?viewer " + "WHERE { "
				+ "?viewer" + " viskoO:partOfViewerSet " + viewerSetURI + ". "
				+ "?viewer viskoO:operatesOn " + formatURI + " . }";

		return endpoint.executeQuery(stringQuery);
	}

	public ResultSet isTransformer(String uri) {
		uri = "<" + uri + ">";

		String stringQuery = QUERY_PREFIX
				+ "ASK "
				+ "WHERE { "
				+ uri
				+ " rdf:type <http://trust.utep.edu/visko/ontology/visko-operator-v3.owl#Transformer> . }";

		return endpoint.executeQuery(stringQuery);
	}

	public ResultSet isMapper(String uri) {
		uri = "<" + uri + ">";

		String stringQuery = QUERY_PREFIX
				+ "ASK "
				+ "WHERE { "
				+ uri
				+ " rdf:type <http://trust.utep.edu/visko/ontology/visko-operator-v3.owl#Mapper> . }";

		return endpoint.executeQuery(stringQuery);
	}

	public boolean canBeVisualizedWithTargetFormat(String formatURI,
			String targetFormatURI) {

		formatURI = "<" + formatURI + ">";
		targetFormatURI = "<" + targetFormatURI + ">";

		String stringQuery = QUERY_PREFIX + "ASK " + "WHERE {{ " + formatURI
				+ " viskoO:canBeTransformedToTransitive " + targetFormatURI
				+ " . " + "      } UNION {" + formatURI
				+ " viskoO:canBeTransformedTo " + targetFormatURI + " . "
				+ "}}";

		return endpoint.executeAskQuery(stringQuery);
	}

	public ResultSet getTargetFormatsFromViewerSet(String formatURI,
			String viewerSetURI) {
		formatURI = "<" + formatURI + ">";
		viewerSetURI = "<" + viewerSetURI + ">";

		String stringQuery = QUERY_PREFIX + "SELECT ?format ?viewer "
				+ "WHERE {{ " + "?viewer viskoO:partOfViewerSet "
				+ viewerSetURI + " . " + "?viewer viskoO:operatesOn ?format . "
				+ formatURI + " viskoO:canBeTransformedToTransitive ?format . "
				+ "} UNION { " + "?viewer viskoO:partOfViewerSet "
				+ viewerSetURI + " . " + "?viewer viskoO:operatesOn ?format . "
				+ formatURI + "viskoO:canBeTransformedTo ?format . " + "}}";

		return endpoint.executeQuery(stringQuery);
	}

	public ResultSet getNextFormats(String formatURI, String targetFormat) {
		formatURI = "<" + formatURI + ">";
		targetFormat = "<" + targetFormat + ">";

		String stringQuery = QUERY_PREFIX + "SELECT ?format " + "WHERE {{ "
				+ formatURI + " viskoO:canBeTransformedTo ?format . "
				+ "} UNION {" + formatURI
				+ " viskoO:canBeTransformedTo ?format . }}";

		return endpoint.executeQuery(stringQuery);
	}

	public ResultSet getTransformers(String inFormatURI, String outFormatURI) {
		inFormatURI = "<" + inFormatURI + ">";
		outFormatURI = "<" + outFormatURI + ">";

		String stringQuery = QUERY_PREFIX + "SELECT ?transformer " + "WHERE { "
				+ "?transformer viskoO:operatesOn " + inFormatURI + " . "
				+ "?transformer viskoO:transformsTo " + outFormatURI + " . "
				+ " }";

		return endpoint.executeQuery(stringQuery);
	}

	public ResultSet getOWLSServiceImplementationsURIs(String operatorURI) {
		operatorURI = "<" + operatorURI + ">";

		String stringQuery = QUERY_PREFIX + "SELECT ?opImpl " + "WHERE {"
				+ "?opImpl viskoS:implementsOperator " + operatorURI + " . "
				+ "}";
		return endpoint.executeQuery(stringQuery);
	}

	public ResultSet getOutputFormatsOfTransformer(String transformerURI) {
		transformerURI = "<" + transformerURI + ">";

		String stringQuery = QUERY_PREFIX + "SELECT ?outputFormat "
				+ "WHERE { " + transformerURI
				+ " viskoO:transformsTo ?outputFormat . " + "      }";

		return endpoint.executeQuery(stringQuery);
	}

	public ResultSet getTransformedFormats() {
		String stringQuery = QUERY_PREFIX
				+ "SELECT ?format ?outputOf ?inputTo " + "WHERE {"
				+ "?outputOf viskoO:transformsTo ?format . "
				+ "?inputTo viskoO:operatesOn ?format . " + "}";
		return endpoint.executeQuery(stringQuery);
	}

	public ResultSet getViews() {
		String stringQuery = QUERY_PREFIX + "SELECT ?view " + "WHERE {"
				+ "?view rdf:type viskoV:View . " + "}";
		return endpoint.executeQuery(stringQuery);
	}

	public ResultSet getViewsGeneratedFrom(String mapperURI) {
		mapperURI = "<" + mapperURI + ">";
		String stringQuery = QUERY_PREFIX + "SELECT ?view " + "WHERE {"
				+ mapperURI + " viskoO:mapsTo ?view . " + "}";

		return endpoint.executeQuery(stringQuery);
	}

	public ResultSet getViewerSets() {
		String stringQuery = QUERY_PREFIX + "SELECT ?viewerSet " + "WHERE {"
				+ "      ?viewerSet rdf:type viskoO:ViewerSet . " + "      }";

		return endpoint.executeQuery(stringQuery);
	}

	public ResultSet getViewersOfViewerSet(String viewerSetURI) {
		viewerSetURI = "<" + viewerSetURI + ">";
		String stringQuery = QUERY_PREFIX + "SELECT ?viewer " + "WHERE {"
				+ "      ?viewer viskoO:partOfViewerSet " + viewerSetURI
				+ " . " + "      }";

		return endpoint.executeQuery(stringQuery);
	}

	public ResultSet getMappers() {
		String stringQuery = QUERY_PREFIX + "SELECT ?mapper " + "WHERE {"
				+ "      ?mapper rdf:type viskoO:Mapper . " + "      }";

		return endpoint.executeQuery(stringQuery);
	}

	public ResultSet getViewers() {
		String stringQuery = QUERY_PREFIX + "SELECT ?viewer " + "WHERE {"
				+ "      ?viewer rdf:type viskoO:Viewer . " + "      }";

		return endpoint.executeQuery(stringQuery);
	}

	public ResultSet getExtractedProfile(String typeURI, String formatURI) {
		String dataTypeURI = "\"" + typeURI + "\"^^xsd:anyURI";
		String dataFormatURI = "<" + formatURI + ">";

		String stringQuery = QUERY_PREFIX + "SELECT ?profile " + "WHERE {"
				+ "?extractor viskoS:extractsFromDataOfType " + dataTypeURI
				+ " . " + "?extractor viskoS:extractsFromFormat "
				+ dataFormatURI + " . "
				+ "?extractor viskoS:createsProfile ?profile . " + "}";

		return endpoint.executeQuery(stringQuery);
	}

	public ResultSet getExtractorService(String typeURI, String formatURI) {
		String dataTypeURI = "\"" + typeURI + "\"^^xsd:anyURI";
		String dataFormatURI = "<" + formatURI + ">";

		String stringQuery = QUERY_PREFIX + "SELECT ?service " + "WHERE {"
				+ "?extractor viskoS:extractsFromDataOfType " + dataTypeURI
				+ " . " + "?extractor viskoS:extractsFromFormat "
				+ dataFormatURI + " . "
				+ "?service viskoS:implementsExtractor ?extractor . " + "}";

		return endpoint.executeQuery(stringQuery);
	}

	public ResultSet getParameterBindings(String typeURI) {
		typeURI = "\"" + typeURI + "\"^^xsd:anyURI";
		String stringQuery = QUERY_PREFIX + "SELECT ?param ?value " + "WHERE {"
				+ "?profile viskoS:profiles " + typeURI + " . "
				+ "?profile viskoS:declaresBindings ?binding ."
				+ "?binding owlsProcess:toParam ?param . "
				+ "?binding owlsProcess:valueData ?value . " + "}";

		return endpoint.executeQuery(stringQuery);
	}
	
	public ResultSet submitQuery(String query){
		return endpoint.executeQuery(query);
	}
}
