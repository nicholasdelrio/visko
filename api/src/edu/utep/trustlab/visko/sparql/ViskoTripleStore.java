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


package edu.utep.trustlab.visko.sparql;

import com.hp.hpl.jena.query.*;

import edu.utep.trustlab.visko.ontology.vocabulary.Visko;
import edu.utep.trustlab.visko.ontology.vocabulary.supplemental.OWLS_Process;
import edu.utep.trustlab.visko.ontology.vocabulary.supplemental.OWLS_Service;

public class ViskoTripleStore {
	
	public static String QUERY_PREFIX = 
			  "PREFIX viskoV: <" + Visko.CORE_VISKO_V + "#> "
			+ "PREFIX viskoO: <" + Visko.CORE_VISKO_O + "#> "
			+ "PREFIX viskoS: <" + Visko.CORE_VISKO_S + "#> "
			+ "PREFIX owlsService: <" + OWLS_Service.ONTOLOGY_OWLS_SERVICE_URI + "#> "
			+ "PREFIX owlsProcess: <" + OWLS_Process.ONTOLOGY_OWLS_PROCESS_URI + "#> "
			+ "PREFIX owl: <http://www.w3.org/2002/07/owl#> "
			+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
			+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> "
			+ "PREFIX pmlp: <http://inference-web.org/2.0/pml-provenance.owl#> "
			+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> ";

	public ResultSet getParameterBindingsQuery(String typeURI) {
		typeURI = "\"" + typeURI + "\"^^xsd:anyURI";
		String stringQuery = QUERY_PREFIX + "SELECT ?param ?value " + "WHERE {"
				+ "?profile viskoS:profiles " + typeURI + " . "
				+ "?profile viskoS:declaresBindings ?binding ."
				+ "?binding owlsProcess:toParam ?param . "
				+ "?binding owlsProcess:valueData ?value . " + "}";

		return SPARQL_EndpointFactory.executeQuery(stringQuery);
	}

	public ResultSet getProfiles(String typeURI) {
		typeURI = "\"" + typeURI + "\"^^xsd:anyURI";

		String stringQuery = QUERY_PREFIX + "SELECT ?profile " + "WHERE {"
				+ "?profile viskoS:profiles " + typeURI + " . " + "}";

		return SPARQL_EndpointFactory.executeQuery(stringQuery);
	}
	
	public ResultSet getInformationSubclasses(){
		String query = QUERY_PREFIX + "SELECT ?subclass ?label WHERE {?subclass rdfs:subClassOf pmlp:Information . ?subclass rdfs:label ?label . } ORDER BY ?label";
		return SPARQL_EndpointFactory.executeQuery(query);
	}

	public ResultSet getFormatsSupportedByViewer(String viewerURI) {
		viewerURI = "<" + viewerURI + ">";

		String stringQuery = QUERY_PREFIX + "SELECT ?format " + "WHERE {"
				+ viewerURI + " viskoO:hasInputFormat ?format . " + "}";

		return SPARQL_EndpointFactory.executeQuery(stringQuery);
	}

	public ResultSet getFormats() {
		String stringQuery = QUERY_PREFIX + "SELECT ?format " + "WHERE {?format a pmlp:Format . }";
		return SPARQL_EndpointFactory.executeQuery(stringQuery);
	}
	
	public ResultSet getAllParameters() {
		String stringQuery = QUERY_PREFIX + "SELECT ?param " + "WHERE { "
				+ "?service rdf:type owlsService:Service . "
				+ "?service owlsService:describedBy ?process ."
				+ "?process owlsProcess:hasInput ?param ." + "} ORDER BY ?param";
		return SPARQL_EndpointFactory.executeQuery(stringQuery);
	}

	public ResultSet getOWLSServices() {
		String stringQuery = QUERY_PREFIX + "SELECT ?service " + "WHERE { "
				+ "?service rdf:type owlsService:Service . " + "}";

		return SPARQL_EndpointFactory.executeQuery(stringQuery);

	}

	public ResultSet getDataTransformers() {
		String stringQuery = QUERY_PREFIX + "SELECT ?operator ?lbl "
				+ "WHERE { " + "?operator rdf:type viskoO:DataTransformer . "
				+ "?operator rdfs:label ?lbl . " + "}";

		return SPARQL_EndpointFactory.executeQuery(stringQuery);
	}
	
	public ResultSet getDataFilters() {
		String stringQuery = QUERY_PREFIX + "SELECT ?operator ?lbl "
				+ "WHERE { " + "?operator rdf:type viskoO:DataFilter . "
				+ "?operator rdfs:label ?lbl . " + "}";

		return SPARQL_EndpointFactory.executeQuery(stringQuery);
	}

	public ResultSet getFormatConverters() {
		String stringQuery = QUERY_PREFIX + "SELECT ?operator ?lbl "
				+ "WHERE { " + "?operator rdf:type viskoO:FormatConverter . "
				+ "?operator rdfs:label ?lbl . " + "}";

		return SPARQL_EndpointFactory.executeQuery(stringQuery);
	}

	public ResultSet getOperatorInformation() {
		String stringQuery = QUERY_PREFIX
				+ "SELECT ?operator ?lbl ?input ?output " + "WHERE { "
				+ "?operator rdf:type viskoO:Transformer . "
				+ "?operator rdfs:label ?lbl . "
				+ "?operator viskoO:hasInputFormat ?input. "
				+ "?operator viskoO:hasOutputFormat ?output. " + "}";
		return SPARQL_EndpointFactory.executeQuery(stringQuery);
	}

	public ResultSet getToolkits() {
		String stringQuery = QUERY_PREFIX + "SELECT ?toolkit " + "WHERE { "
				+ "?toolkit rdf:type viskoS:Toolkit . " + "}";

		return SPARQL_EndpointFactory.executeQuery(stringQuery);
	}

	public ResultSet getOperatedOnFormats(String viskoOperatorURI) {
		viskoOperatorURI = "<" + viskoOperatorURI + ">";

		String stringQuery = QUERY_PREFIX + "SELECT ?inputFormat " + "WHERE { "
				+ viskoOperatorURI + " viskoO:hasInputFormat ?inputFormat . " + "}";
		return SPARQL_EndpointFactory.executeQuery(stringQuery);
	}

	public ResultSet getOperatedOnFormats() {
		String stringQuery = QUERY_PREFIX + "SELECT DISTINCT ?inputFormat "
				+ "WHERE { " + "?pipelineComponent viskoO:hasInputFormat ?inputFormat . "
				+ "}"
				+ "ORDER BY ?inputFormat";
		return SPARQL_EndpointFactory.executeQuery(stringQuery);
	}
	
	public ResultSet getOperatedOnDataTypes() {
		String stringQuery = QUERY_PREFIX + "SELECT DISTINCT ?dataType "
				+ "WHERE { " + "?pipelineComponent viskoO:hasInputDataType ?dataType . "
				+ "}"
				+ "ORDER BY ?dataType";
		return SPARQL_EndpointFactory.executeQuery(stringQuery);
	}

	public boolean canBeVisualizedWithViewerSet(String formatURI, String viewerSetURI) {
		formatURI = "<" + formatURI + ">";
		viewerSetURI = "<" + viewerSetURI + ">";

		String stringQuery = QUERY_PREFIX + "ASK " + "WHERE {{ "
				+ "?viewer viskoO:partOfViewerSet " + viewerSetURI + ". "
				+ "?viewer viskoO:hasInputFormat ?format . "
				+ formatURI + " viskoO:canBeConvertedToFormatToTransitive ?format . } UNION {"
				+ "?viewer viskoO:partOfViewerSet " + viewerSetURI + ". "
				+ "?viewer viskoO:hasInputFormat ?format . "
				+ formatURI + " viskoO:canBeConvertedToFormat ?format . } UNION { " + "?viewer"
				+ " viskoO:partOfViewerSet " + viewerSetURI + ". "
				+ "?viewer viskoO:hasInputFormat ?format . " + "}}";

		return SPARQL_EndpointFactory.executeAskQuery(stringQuery);
	}

	public boolean isFormatAlreadyVisualizableWithViewerSet(String formatURI, String viewerSetURI) {
		formatURI = "<" + formatURI + ">";
		viewerSetURI = "<" + viewerSetURI + ">";

		String stringQuery = QUERY_PREFIX
				+ "ASK WHERE {"
				+ "?viewer viskoO:partOfViewerSet " + viewerSetURI + " . "
				+ "?viewer viskoO:hasInputFormat " + formatURI + " . }";
		
		return SPARQL_EndpointFactory.executeAskQuery(stringQuery);
	}

	public boolean isDataTypeAlreadyVisualizableWithViewerSet(String dataTypeURI, String viewerSetURI) {
		dataTypeURI = "<" + dataTypeURI + ">";
		viewerSetURI = "<" + viewerSetURI + ">";

		String stringQuery = QUERY_PREFIX
				+ "ASK WHERE {{"
				+ "?viewer viskoO:partOfViewerSet " + viewerSetURI + " . "
				+ "?viewer viskoO:hasInputDataType " + dataTypeURI + " . } UNION {"
				+ "?viewer viskoO:partOfViewerSet " + viewerSetURI + " . "
				+ "?viewer viskoO:hasInputDataType ?superDataType . "
				+ dataTypeURI + " rdfs:subClassOf ?superDataType . }}";
	
		return SPARQL_EndpointFactory.executeAskQuery(stringQuery);
	}

	public boolean isSubClassOfDataTypeAlreadyVisualizableWithViewerSet(String dataTypeURI, String viewerSetURI) {

		dataTypeURI = "<" + dataTypeURI + ">";
		viewerSetURI = "<" + viewerSetURI + ">";

		String stringQuery = QUERY_PREFIX + "ASK " + "WHERE { " + "?viewer"
				+ " viskoO:partOfViewerSet " + viewerSetURI + ". "
				+ "?viewer viskoO:hasInputDataType ?superClassDataType . "
				+ dataTypeURI + " rdfs:subClassOf ?superClassDataType . }";
		return SPARQL_EndpointFactory.executeAskQuery(stringQuery);
	}
	
	public boolean isAlreadyVisualizable(String formatURI) {

		formatURI = "<" + formatURI + ">";

		String stringQuery = QUERY_PREFIX + "ASK " + "WHERE { "
				+ "?viewer a viskoO:Viewer. " + "?viewer viskoO:hasInputFormat "
				+ formatURI + " . }";
		return SPARQL_EndpointFactory.executeAskQuery(stringQuery);
	}

	public ResultSet getViewerSetsOfViewer(String viewerURI) {
		viewerURI = "<" + viewerURI + ">";

		String stringQuery = QUERY_PREFIX + "SELECT ?viewerSet " + "WHERE { " +
				viewerURI + " viskoO:partOfViewerSet ?viewerSet .}";

		return SPARQL_EndpointFactory.executeQuery(stringQuery);
	}
	
	public ResultSet getTargetViewerOfViewerSet(String formatURI,
			String viewerSetURI) {

		formatURI = "<" + formatURI + ">";
		viewerSetURI = "<" + viewerSetURI + ">";

		String stringQuery = QUERY_PREFIX + "SELECT ?viewer " + "WHERE { "
				+ "?viewer" + " viskoO:partOfViewerSet " + viewerSetURI + ". "
				+ "?viewer viskoO:hasInputFormat " + formatURI + " . }";

		return SPARQL_EndpointFactory.executeQuery(stringQuery);
	}

	public boolean isTransformer(String uri) {
		uri = "<" + uri + ">";

		String stringQuery = QUERY_PREFIX
				+ "ASK "
				+ "WHERE { "
				+ uri
				+ " rdf:type viskoO:Transformer> . }";

		return SPARQL_EndpointFactory.executeAskQuery(stringQuery);
	}

	public boolean isMapper(String uri) {
		uri = "<" + uri + ">";

		String stringQuery = QUERY_PREFIX
				+ "ASK "
				+ "WHERE {" + uri + " rdf:type viskoO:ViewMapper . }";

		return SPARQL_EndpointFactory.executeAskQuery(stringQuery);
	}

	public ResultSet getViewGeneratedByMapper(String mapperURI) {
		String uri = "<" + mapperURI + ">";

		String stringQuery = QUERY_PREFIX
				+ "SELECT ?view " + "WHERE {" + uri + " viskoO:mapsToView ?view . }";

		return SPARQL_EndpointFactory.executeQuery(stringQuery);
	}
	
	public boolean canBeVisualizedWithTargetFormat(String formatURI,
			String targetFormatURI) {

		formatURI = "<" + formatURI + ">";
		targetFormatURI = "<" + targetFormatURI + ">";

		String stringQuery = QUERY_PREFIX + "ASK " + "WHERE {{ " + formatURI
				+ " viskoO:canBeConvertedToFormatTransitive " + targetFormatURI
				+ " . " + "      } UNION {" + formatURI
				+ " viskoO:canBeConvertedToFormat " + targetFormatURI + " . "
				+ "}}";

		return SPARQL_EndpointFactory.executeAskQuery(stringQuery);
	}

	public ResultSet getTargetFormatsFromViewerSet(String formatURI, String viewerSetURI) {
		formatURI = "<" + formatURI + ">";
		viewerSetURI = "<" + viewerSetURI + ">";

		String stringQuery = 
				QUERY_PREFIX + "SELECT ?format ?viewer "
				+ "WHERE {{"
				+ "?viewer viskoO:partOfViewerSet " + viewerSetURI + " . "
				+ "?viewer viskoO:hasInputFormat ?format . "
				+ formatURI + " viskoO:canBeConvertedToFormatTransitive ?format . } UNION { "
				+ "?viewer viskoO:partOfViewerSet " + viewerSetURI + " . "
				+ "?viewer viskoO:hasInputFormat ?format . "
				+ formatURI + "viskoO:canBeConvertedToFormat ?format . " + "}}";

		return SPARQL_EndpointFactory.executeQuery(stringQuery);
	}
	
	public ResultSet getFormatsFromViewerSet(String viewerSetURI) {
		viewerSetURI = "<" + viewerSetURI + ">";

		String stringQuery = 
				QUERY_PREFIX
				+ "SELECT ?format WHERE {"
				+ "?viewer viskoO:partOfViewerSet " + viewerSetURI + " . "
				+ "?viewer viskoO:hasInputFormat ?format . }";

		return SPARQL_EndpointFactory.executeQuery(stringQuery);
	}


	public ResultSet getNextFormats(String formatURI, String targetFormat) {
		formatURI = "<" + formatURI + ">";
		targetFormat = "<" + targetFormat + ">";

		String stringQuery = QUERY_PREFIX + "SELECT ?format " + "WHERE {{ "
				+ formatURI + " viskoO:canBeConvertedToFormat ?format . "
				+ "} UNION {" + formatURI
				+ " viskoO:canBeConvertedToFormat ?format . }}";

		return SPARQL_EndpointFactory.executeQuery(stringQuery);
	}

	public ResultSet getTransformers(String inFormatURI, String outFormatURI) {
		inFormatURI = "<" + inFormatURI + ">";
		outFormatURI = "<" + outFormatURI + ">";

		String stringQuery = QUERY_PREFIX + "SELECT ?transformer " + "WHERE { "
				+ "?transformer viskoO:hasInputFormat " + inFormatURI + " . "
				+ "?transformer viskoO:hasOutputFormat " + outFormatURI + " . "
				+ " }";

		return SPARQL_EndpointFactory.executeQuery(stringQuery);
	}

	public ResultSet getOWLSServiceImplementationsURIs(String operatorURI) {
		operatorURI = "<" + operatorURI + ">";

		String stringQuery = QUERY_PREFIX + "SELECT ?opImpl " + "WHERE {"
				+ "?opImpl viskoS:implementsOperator " + operatorURI + " . "
				+ "}";
		return SPARQL_EndpointFactory.executeQuery(stringQuery);
	}
	
	public ResultSet getImplemenationOf(String serviceURI) {
		serviceURI = "<" + serviceURI + ">";

		String stringQuery = QUERY_PREFIX + "SELECT ?operator " + "WHERE {"
				+ serviceURI + " viskoS:implementsOperator ?operator . "
				+ "}";
		return SPARQL_EndpointFactory.executeQuery(stringQuery);
	}

	public ResultSet getOutputFormatsOfOperator(String operatorURI) {
		operatorURI = "<" + operatorURI + ">";

		String stringQuery = 
				QUERY_PREFIX
				+ "SELECT ?format WHERE {"
				+ operatorURI + " viskoO:hasOutputFormat ?format . }";

		return SPARQL_EndpointFactory.executeQuery(stringQuery);
	}

	public ResultSet getTransformedFormats() {
		String stringQuery = QUERY_PREFIX
				+ "SELECT ?format ?outputOf ?inputTo " + "WHERE {"
				+ "?outputOf viskoO:hasOutputFormat ?format . "
				+ "?inputTo viskoO:hasInputFormat ?format . " + "}";
		return SPARQL_EndpointFactory.executeQuery(stringQuery);
	}

	public ResultSet getViews() {
		String stringQuery = QUERY_PREFIX + "SELECT ?view " + "WHERE {"
				+ "?view rdf:type viskoV:View . " + "} ORDER BY ?view";
		return SPARQL_EndpointFactory.executeQuery(stringQuery);
	}

	public boolean canOperateOnDataType(String operatorURI, String dataTypeURI){
		operatorURI = "<" + operatorURI + ">";
		dataTypeURI = "<" + dataTypeURI + ">";
		
		String stringQuery = QUERY_PREFIX +
				"ASK WHERE {" + operatorURI + " viskoO:hasInputDataType " + dataTypeURI + " . }";
		
		return SPARQL_EndpointFactory.executeAskQuery(stringQuery);
	}

	public ResultSet getOperatorsThatProcessFormat(String formatURI){
		formatURI = "<" + formatURI + ">";
		
		String stringQuery = QUERY_PREFIX
				+ "SELECT ?operator WHERE {"
				+ "?operator a viskoO:Operator . "
				+ "?operator viskoO:hasInputFormat " +  formatURI + " . }";
		
		return SPARQL_EndpointFactory.executeQuery(stringQuery);
	}
	
	public boolean generatesView(String operatorURI, String viewURI){
		operatorURI = "<" + operatorURI + ">";
		viewURI = "<" + viewURI + ">";
		
		String stringQuery = QUERY_PREFIX
				+ "ASK WHERE {"
				+ operatorURI + " viskoO:mapsToView " + viewURI + " . }";
		
		return SPARQL_EndpointFactory.executeAskQuery(stringQuery);
	}
	
	public boolean canBeAdjacentOperatorsAccordingToType(String operator1URI, String operator2URI){
		operator1URI = "<" + operator1URI + ">";
		operator2URI = "<" + operator2URI + ">";
		
		String stringQuery = QUERY_PREFIX
				+ "ASK WHERE {{"
				+ operator1URI + " viskoO:hasOutputDataType ?dataType . "
				+ operator2URI + " viskoO:hasInputDataType ?dataType . } UNION {"
				+ operator1URI + " viskoO:hasOutputDataType ?subDataType . "
				+ operator2URI + " viskoO:hasInputDataType ?superDataType ."
				+ "?subDataType rdfs:subClassOf ?superDataType . }}";
		
		return SPARQL_EndpointFactory.executeAskQuery(stringQuery);
	}
	
	public boolean operatorAcceptsInputDataType(String operatorURI, String dataTypeURI){
		dataTypeURI = "<" + dataTypeURI + ">";
		operatorURI = "<" + operatorURI + ">";
		
		String stringQuery = QUERY_PREFIX
				+ "ASK WHERE {{"
				+ operatorURI + " viskoO:hasInputDataType " + dataTypeURI + " . } UNION {"
				+ operatorURI + " viskoO:hasInputDataType ?superDataType . "
				+ dataTypeURI + " rdfs:subClassOf ?superDataType . }}";
		
		return SPARQL_EndpointFactory.executeAskQuery(stringQuery);
	}
	
	public ResultSet getTargetViewerForOperator(String operatorURI, String viewerSetURI){
		operatorURI = "<" + operatorURI + ">";
		viewerSetURI = "<" + viewerSetURI + ">";
		
		String stringQuery = QUERY_PREFIX
				+ "SELECT DISTINCT ?viewer WHERE {{"
				+ "?viewer viskoO:partOfViewerSet " + viewerSetURI + " . "
				+ "?viewer viskoO:hasInputFormat ?format . "
				+ "?viewer viskoO:hasInputDataType ?dataType . "
				+ operatorURI + " viskoO:hasOutputFormat ?format . "
				+ operatorURI + " viskoO:hasOutputDataType ?dataType . } UNION {"
				+ "?viewer viskoO:partOfViewerSet " + viewerSetURI + " . "
				+ "?viewer viskoO:hasInputFormat ?format . "
				+ "?viewer viskoO:hasInputDataType ?superDataType . "
				+ operatorURI + " viskoO:hasOutputFormat ?format . "
				+ operatorURI + " viskoO:hasOutputDataType ?subDataType ."
				+ "?subDataType rdfs:subClassOf ?superDataType . }}";
		
		return SPARQL_EndpointFactory.executeQuery(stringQuery);
	}
	
	public boolean outputCanBeViewedByViewerSet(String operatorURI, String viewerSetURI){
		operatorURI = "<" + operatorURI + ">";
		viewerSetURI = "<" + viewerSetURI + ">";
		
		String stringQuery = QUERY_PREFIX
				+ "ASK WHERE {{"
				+ "?viewer viskoO:partOfViewerSet " + viewerSetURI + " . "
				+ "?viewer viskoO:hasInputFormat ?format . "
				+ "?viewer viskoO:hasInputDataType ?dataType . "
				+ operatorURI + " viskoO:hasOutputFormat ?format . "
				+ operatorURI + " viskoO:hasOutputDataType ?dataType . }UNION{"
				+ "?viewer viskoO:partOfViewerSet " + viewerSetURI + " . "
				+ "?viewer viskoO:hasInputFormat ?format . "
				+ "?viewer viskoO:hasInputDataType ?superDataType . "
				+ operatorURI + " viskoO:hasOutputFormat ?format . "
				+ operatorURI + " viskoO:hasOutputDataType ?subDataType ."
				+ "?subDataType rdfs:subClassOf ?superDataType . }}";
				
		return SPARQL_EndpointFactory.executeAskQuery(stringQuery);
	}
	
	public ResultSet getNextOperatorWithCommonFormat(String operatorURI){
		operatorURI = "<" + operatorURI + ">";
		
		String stringQuery = QUERY_PREFIX
				+ "SELECT ?operator WHERE {"
				+ operatorURI + " viskoO:hasOutputFormat ?format . "
				+ "?format viskoO:isInputFormatTo ?operator . "
				+ "?operator a viskoO:Operator . }";
		
		return SPARQL_EndpointFactory.executeQuery(stringQuery);
	}
	
	public ResultSet getTransformedToDataType(String operatorURI){
		operatorURI = "<" + operatorURI + ">";
		
		String stringQuery = QUERY_PREFIX +
				"SELECT ?dataType WHERE { " + operatorURI + " viskoO:hasOutputDataType ?dataType . }";
		
		return SPARQL_EndpointFactory.executeQuery(stringQuery);
	}
	
	public boolean canOperateOnSuperTypeOfDataType(String operatorURI, String dataTypeURI){
		operatorURI = "<" + operatorURI + ">";
		dataTypeURI = "<" + dataTypeURI + ">";
		
		String stringQuery = QUERY_PREFIX +
				"ASK WHERE {" + operatorURI + " viskoO:hasInputDataType ?superType . "
				+ dataTypeURI + " rdfs:subClassOf ?superType . }";
		
		return SPARQL_EndpointFactory.executeAskQuery(stringQuery);
	}
	
	public ResultSet getViewsGeneratedFrom(String mapperURI) {
		mapperURI = "<" + mapperURI + ">";
		String stringQuery = QUERY_PREFIX + "SELECT ?view " + "WHERE {"
				+ mapperURI + " viskoO:mapsToView ?view . " + "}";

		return SPARQL_EndpointFactory.executeQuery(stringQuery);
	}
	
	public ResultSet getOperators(){
		String queryString = QUERY_PREFIX + "SELECT ?operator WHERE{"
				+ "?operator a viskoO:Operator . }";
		
		return SPARQL_EndpointFactory.executeQuery(queryString);
	}

	public ResultSet getViewerSets() {
		String stringQuery = QUERY_PREFIX + "SELECT ?viewerSet " + "WHERE {"
				+ "      ?viewerSet rdf:type viskoO:ViewerSet . " + " } ORDER BY ?viewerSet";

		return SPARQL_EndpointFactory.executeQuery(stringQuery);
	}

	public ResultSet getViewersOfViewerSet(String viewerSetURI) {
		viewerSetURI = "<" + viewerSetURI + ">";
		String stringQuery = QUERY_PREFIX + "SELECT ?viewer " + "WHERE {"
				+ "      ?viewer viskoO:partOfViewerSet " + viewerSetURI
				+ " . " + "      }";

		return SPARQL_EndpointFactory.executeQuery(stringQuery);
	}
	
	public ResultSet getEquivalentSADI(String owlsServiceURI) {
		owlsServiceURI = "<" + owlsServiceURI + ">";
		String stringQuery = QUERY_PREFIX + "SELECT ?sadiService " 
		+ "WHERE {" + owlsServiceURI + " <https://raw.github.com/nicholasdelrio/visko-rdf/master/rdf/ontology/visko-service.owl#hasEquivalentSADI> ?sadiService . }";

		return SPARQL_EndpointFactory.executeQuery(stringQuery);
	}


	public ResultSet getMappers() {
		String stringQuery = QUERY_PREFIX + "SELECT ?mapper " + "WHERE {"
				+ "      ?mapper rdf:type viskoO:Mapper . " + "      }";

		return SPARQL_EndpointFactory.executeQuery(stringQuery);
	}

	public ResultSet getViewers() {
		String stringQuery = QUERY_PREFIX + "SELECT ?viewer " + "WHERE {"
				+ "      ?viewer rdf:type viskoO:Viewer . " + "      }";

		return SPARQL_EndpointFactory.executeQuery(stringQuery);
	}

	public ResultSet getExtractedProfile(String typeURI, String formatURI) {
		String dataTypeURI = "\"" + typeURI + "\"^^xsd:anyURI";
		String dataFormatURI = "<" + formatURI + ">";

		String stringQuery = QUERY_PREFIX + "SELECT ?profile " + "WHERE {"
				+ "?extractor viskoS:extractsFromDataOfType " + dataTypeURI
				+ " . " + "?extractor viskoS:extractsFromFormat "
				+ dataFormatURI + " . "
				+ "?extractor viskoS:createsProfile ?profile . " + "}";

		return SPARQL_EndpointFactory.executeQuery(stringQuery);
	}

	public ResultSet getExtractorService(String typeURI, String formatURI) {
		String dataTypeURI = "\"" + typeURI + "\"^^xsd:anyURI";
		String dataFormatURI = "<" + formatURI + ">";

		String stringQuery = QUERY_PREFIX + "SELECT ?service " + "WHERE {"
				+ "?extractor viskoS:extractsFromDataOfType " + dataTypeURI
				+ " . " + "?extractor viskoS:extractsFromFormat "
				+ dataFormatURI + " . "
				+ "?service viskoS:implementsExtractor ?extractor . " + "}";

		return SPARQL_EndpointFactory.executeQuery(stringQuery);
	}

	public ResultSet getParameterBindings(String typeURI) {
		typeURI = "\"" + typeURI + "\"^^xsd:anyURI";
		String stringQuery = QUERY_PREFIX + "SELECT ?param ?value " + "WHERE {"
				+ "?profile viskoS:profiles " + typeURI + " . "
				+ "?profile viskoS:declaresBindings ?binding ."
				+ "?binding owlsProcess:toParam ?param . "
				+ "?binding owlsProcess:valueData ?value . " + "}";

		return SPARQL_EndpointFactory.executeQuery(stringQuery);
	}
	
	public ResultSet getInputParameters(String serviceURI) {
		serviceURI = "<" + serviceURI + ">";
		String stringQuery = QUERY_PREFIX + "SELECT ?parameter "
				+ "WHERE {"
				+ serviceURI + " viskoS:supportedByOWLSService ?owlsService . "
				+ "?owlsService owlsService:describedBy ?process . "
				+ "?process owlsProcess:hasInput ?parameter . " + "}";

		return SPARQL_EndpointFactory.executeQuery(stringQuery);
	}
	
	public ResultSet submitQuery(String query){
		return SPARQL_EndpointFactory.executeQuery(query);
	}
}
