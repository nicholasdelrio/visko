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

	public ResultSet getInformationSubclasses(){
		String query =
				QUERY_PREFIX
				+ "SELECT ?subclass ?label WHERE {"
				+ "?subclass rdfs:subClassOf pmlp:Information . "
				+ "?subclass rdfs:label ?label . } ORDER BY ?label";
		return submitQuery(query);
	}
	
	public ResultSet getServiceOutputFormat(String serviceURI){
		serviceURI = "<" + serviceURI + ">";
		
		String stringQuery =
				QUERY_PREFIX
				+ "SELECT ?format where {"
				+ serviceURI + "viskoS:implementsOperator ?operator . "
				+ "?operator viskoO:hasOutputFormat ?format . }";
		return submitQuery(stringQuery);
	}
	
	public ResultSet getToolkitOf(String serviceURI){
		serviceURI = "<" + serviceURI + ">";
		
		String stringQuery =
				QUERY_PREFIX
				+ "SELECT ?toolkit WHERE{"
				+ serviceURI + " viskoS:supportedByToolkit ?toolkit . }";
		
		return submitQuery(stringQuery);
	}
	
	public ResultSet getAllParameters() {
		String stringQuery = QUERY_PREFIX + "SELECT ?param " + "WHERE { "
				+ "?service rdf:type owlsService:Service . "
				+ "?service owlsService:describedBy ?process ."
				+ "?process owlsProcess:hasInput ?param ." + "} ORDER BY ?param";
		
		return submitQuery(stringQuery);
	}

	public ResultSet getOWLSServices() {
		String stringQuery = 
				QUERY_PREFIX 
				+ "SELECT ?service WHERE { "
				+ "?service rdf:type owlsService:Service . }";

		return submitQuery(stringQuery);

	}

	public ResultSet getTransformers() {
		String stringQuery = 
				QUERY_PREFIX 
				+ "SELECT ?operator ?lbl WHERE { " 
				+ "?operator rdf:type viskoO:Transformer . "
				+ "?operator rdfs:label ?lbl . }";

		return submitQuery(stringQuery);
	}
	
	public ResultSet getFilters() {
		String stringQuery = 
				QUERY_PREFIX 
				+ "SELECT ?operator ?lbl WHERE { "
				+ "?operator rdf:type viskoO:Filter . "
				+ "?operator rdfs:label ?lbl . }";

		return submitQuery(stringQuery);
	}

	public ResultSet getFormatConverters() {
		String stringQuery = 
				QUERY_PREFIX 
				+ "SELECT ?operator ?lbl WHERE { "
				+ "?operator rdf:type viskoO:FormatConverter . "
				+ "?operator rdfs:label ?lbl . }";

		return submitQuery(stringQuery);
	}
	
	public ResultSet getDimensionReducers() {
		String stringQuery = 
				QUERY_PREFIX 
				+ "SELECT ?operator ?lbl WHERE { "
				+ "?operator rdf:type viskoO:DimensionReducer . "
				+ "?operator rdfs:label ?lbl . }";

		return submitQuery(stringQuery);
	}
	
	public ResultSet getInterpolators() {
		String stringQuery = 
				QUERY_PREFIX 
				+ "SELECT ?operator ?lbl WHERE { "
				+ "?operator rdf:type viskoO:Interpolator . "
				+ "?operator rdfs:label ?lbl . }";

		return submitQuery(stringQuery);
	}

	public ResultSet getInputData(String operatorURI){
		operatorURI = "<" + operatorURI + ">";
		
		String stringQuery =
				QUERY_PREFIX
				+ "SELECT ?format ?dataType WHERE{"
				+ operatorURI + "viskoO:hasInputFormat ?format . "
				+ operatorURI + "viskoO:hasInputDataType ?dataType . }";
		
		return submitQuery(stringQuery);
	}
	
	public ResultSet getOutputData(String operatorURI){
		operatorURI = "<" + operatorURI + ">";
		
		String stringQuery =
				QUERY_PREFIX
				+ "SELECT DISTINCT ?format ?dataType WHERE{"
				+ operatorURI + "viskoO:hasOutputFormat ?format . "
				+ operatorURI + "viskoO:hasOutputDataType ?dataType . }";
		
		return submitQuery(stringQuery);
	}

	public ResultSet getToolkits() {
		String stringQuery = 
				QUERY_PREFIX 
				+ "SELECT ?toolkit WHERE { "
				+ "?toolkit rdf:type viskoS:Toolkit . }";

		return submitQuery(stringQuery);
	}
		
	public ResultSet getInputDataParents() {
		
		System.out.println("getting data inputs-----------------------------------------------------------------");
		
		String stringQuery = 
				QUERY_PREFIX 
				+ "SELECT DISTINCT ?format ?dataType WHERE { "
				+ "?operator viskoO:hasInputFormat ?format . "
				+ "?operator viskoO:hasInputDataType ?dataType . "
				+ "?operator a viskoO:Operator . }";
		return submitQuery(stringQuery);
	}	
	
	public ResultSet getForrestRootNodeOperators() {
		
		System.out.println("getting operator inputs-----------------------------------------------------------------");
		
		String stringQuery = 
				QUERY_PREFIX 
				+ "SELECT DISTINCT ?operator WHERE { "
				+ "?operator viskoO:hasInputFormat ?format . "
				+ "?operator viskoO:hasInputDataType ?dataType . }";

		return submitQuery(stringQuery);
	}	


	public ResultSet getInputFormats() {
		String stringQuery = 
				QUERY_PREFIX
				+ "SELECT DISTINCT ?inputFormat WHERE {"
				+ "?pipelineComponent viskoO:hasInputFormat ?inputFormat . }"
				+ "ORDER BY ?inputFormat";
		return submitQuery(stringQuery);
	}
	
	public ResultSet getInputDataTypes() {
		String stringQuery = 
				QUERY_PREFIX 
				+ "SELECT DISTINCT ?dataType ?label WHERE {"
				+ "?pipelineComponent viskoO:hasInputDataType ?dataType . "
				+ "OPTIONAL {?dataType rdfs:label ?label}" +
				"}"
				+ "ORDER BY ?dataType";
		return submitQuery(stringQuery);
	}

	public boolean isSourceTypeAndFormatCompatibleWithTargetTypeAndFormat(String sDataTypeURI, String sFormatURI, String tDataTypeURI, String tFormatURI){
		sDataTypeURI = "<" + sDataTypeURI + ">";
		tDataTypeURI = "<" + tDataTypeURI + ">";
		
		String stringQuery = 
				QUERY_PREFIX
				+ "ASK WHERE {"
				+ sDataTypeURI + " rdfs:subClassOf " + tDataTypeURI + " . "
				+ "}";
			
		return (submitAskQuery(stringQuery) || sDataTypeURI.equals(tDataTypeURI)) && sFormatURI.equals(tFormatURI);
	}
	
	public boolean isAlreadyVisualizableWithViewerSet(String formatURI, String dataTypeURI){
		formatURI = "<" + formatURI + ">";
		dataTypeURI = "<" + dataTypeURI + ">";

		String stringQuery = 
				QUERY_PREFIX
				+ "ASK WHERE {{"
				+ "?viewer viskoO:partOfViewerSet ?viewerSet . "
				+ "?viewer viskoO:hasInputFormat " + formatURI + " . "
				+ "?viewer viskoO:hasInputDataType " + dataTypeURI + " . } UNION {"
				+ "?viewer viskoO:partOfViewerSet ?viewerSet . "
				+ "?viewer viskoO:hasInputFormat " + formatURI + " . "
				+ "?viewer viskoO:hasInputDataType ?superDataType . "
				+ dataTypeURI + " rdfs:subClassOf ?superDataType . }}";
				
		return submitAskQuery(stringQuery);
	}
	
	public boolean isAlreadyVisualizableWithViewerSet(String formatURI, String dataTypeURI, String viewerSetURI) {
		formatURI = "<" + formatURI + ">";
		dataTypeURI = "<" + dataTypeURI + ">";
		viewerSetURI = "<" + viewerSetURI + ">";

		String stringQuery = 
				QUERY_PREFIX
				+ "ASK WHERE {{"
				+ "?viewer viskoO:partOfViewerSet " + viewerSetURI + " . "
				+ "?viewer viskoO:hasInputFormat " + formatURI + " . "
				+ "?viewer viskoO:hasInputDataType " + dataTypeURI + " . } UNION {"
				+ "?viewer viskoO:partOfViewerSet " + viewerSetURI + " . "
				+ "?viewer viskoO:hasInputFormat " + formatURI + " . "
				+ "?viewer viskoO:hasInputDataType ?superDataType . "
				+ dataTypeURI + " rdfs:subClassOf ?superDataType . }}";
				
		return submitAskQuery(stringQuery);
	}
	
	public boolean isFormatAndDataTypeAlreadyVisualizable(String formatURI, String dataTypeURI) {

		formatURI = "<" + formatURI + ">";
		dataTypeURI = "<" + dataTypeURI + ">";

		String stringQuery = 
				QUERY_PREFIX
				+ "ASK WHERE { "
				+ "?viewer a viskoO:Viewer. "
				+ "?viewer viskoO:hasInputFormat " + formatURI + " . "
				+ "?viewer viskoO:hasInputDataType " + dataTypeURI + " . }";
		
		return submitAskQuery(stringQuery);
	}

	public ResultSet getViewersOfViewerSet(String viewerSetURI) {
		viewerSetURI = "<" + viewerSetURI + ">";

		String stringQuery = 
				QUERY_PREFIX
				+ "SELECT ?viewer WHERE { "
				+ "?viewer viskoO:partOfViewerSet " + viewerSetURI + " .}";

		return submitQuery(stringQuery);
	}
	
	public ResultSet getViewerSetsOfViewer(String viewerURI) {
		viewerURI = "<" + viewerURI + ">";

		String stringQuery = 
				QUERY_PREFIX
				+ "SELECT ?viewerSet WHERE { " +
				viewerURI + " viskoO:partOfViewerSet ?viewerSet .}";

		return submitQuery(stringQuery);
	}
	
	public ResultSet getVisualizationAbstractionGeneratedByViewer(String viewerURI){
		viewerURI = "<" + viewerURI + ">";

		String stringQuery = 
				QUERY_PREFIX
				+ "SELECT ?visualizationAbstraction WHERE { "
				+ viewerURI + " viskoO:mapsTo ?visualizationAbstraction .}";

		return submitQuery(stringQuery);		
	}
	
	public boolean isMapper(String uri) {
		uri = "<" + uri + ">";

		String stringQuery = QUERY_PREFIX
				+ "ASK "
				+ "WHERE {" + uri + " rdf:type viskoO:Mapper . }";

		return submitAskQuery(stringQuery);
	}
	
	public boolean isViewer(String uri) {
		uri = "<" + uri + ">";

		String stringQuery = QUERY_PREFIX
				+ "ASK "
				+ "WHERE {" + uri + " rdf:type viskoO:Viewer . }";

		return submitAskQuery(stringQuery);
	}
	
	public boolean isFilter(String uri) {
		uri = "<" + uri + ">";

		String stringQuery = QUERY_PREFIX
				+ "ASK "
				+ "WHERE {" + uri + " rdf:type viskoO:Filter . }";

		return submitAskQuery(stringQuery);
	}
	
	public boolean isInterpolator(String uri) {
		uri = "<" + uri + ">";

		String stringQuery = QUERY_PREFIX
				+ "ASK "
				+ "WHERE {" + uri + " rdf:type viskoO:Interpolator . }";

		return submitAskQuery(stringQuery);
	}

	public boolean isDimensionReducer(String uri) {
		uri = "<" + uri + ">";

		String stringQuery = QUERY_PREFIX
				+ "ASK "
				+ "WHERE {" + uri + " rdf:type viskoO:DimensionReducer . }";

		return submitAskQuery(stringQuery);
	}

	public ResultSet getViewGeneratedByViewMapper(String mapperURI) {
		String uri = "<" + mapperURI + ">";

		String stringQuery = QUERY_PREFIX
				+ "SELECT ?view " + "WHERE {" + uri + " viskoO:mapsTo ?view . }";

		return submitQuery(stringQuery);
	}

	public ResultSet getOWLSServiceImplementationsURIs(String operatorURI) {
		operatorURI = "<" + operatorURI + ">";

		String stringQuery = QUERY_PREFIX + "SELECT ?opImpl " + "WHERE {"
				+ "?opImpl viskoS:implementsOperator " + operatorURI + " . "
				+ "}";
		return submitQuery(stringQuery);
	}

	public boolean isImplemenationOfServiceAMapper(String serviceURI) {
		serviceURI = "<" + serviceURI + ">";

		String stringQuery = 
				QUERY_PREFIX
				+ "ASK WHERE {"
				+ serviceURI + " viskoS:implementsOperator ?operator . "
				+ "?operator a viskoO:Mapper . "
				+ "}";
		
		return submitAskQuery(stringQuery);
	}
	
	public ResultSet getImplemenationOf(String serviceURI) {
		serviceURI = "<" + serviceURI + ">";

		String stringQuery = 
				QUERY_PREFIX
				+ "SELECT ?operator WHERE {"
				+ serviceURI + " viskoS:implementsOperator ?operator . "
				+ "}";
		return submitQuery(stringQuery);
	}

	public boolean generatesTargetFormatOfViewerSet(String operatorURI, String viewerSetURI){
		operatorURI = "<" + operatorURI + ">";
		viewerSetURI = "<" + viewerSetURI + ">";
		
		String stringQuery =
				QUERY_PREFIX
				+ "ASK WHERE {{"
				+ operatorURI + " viskoO:hasOutputFormat ?format . "
				+ operatorURI + " viskoO:hasOutputDataType ?dataType . "
				+ "?viewer viskoO:partOfViewerSet " + viewerSetURI + " . "
				+ "?viewer viskoO:hasInputFormat ?format . "
				+ "?viewer viskoO:hasInputDataType ?dataType . } UNION {"
				+ operatorURI + " viskoO:hasOutputFormat ?format . "
				+ operatorURI + " viskoO:hasOutputDataType ?subDataType . "
				+ "?viewer viskoO:partOfViewerSet " + viewerSetURI + " . "
				+ "?viewer viskoO:hasInputFormat ?format . "
				+ "?viewer viskoO:hasInputDataType ?superDataType . "
				+ "?subDataType rdfs:subClassOf ?superDataType . }}";
		
		return submitAskQuery(stringQuery);
	}

	public ResultSet getVisualizationAbstractions() {
		String stringQuery = 
				QUERY_PREFIX
				+ "SELECT DISTINCT ?visualizationAbstraction WHERE {?visualizationAbstraction rdf:type viskoV:VisualizationAbstraction .} ORDER BY ?visualizationAbstraction";
		return submitQuery(stringQuery);
	}

	public ResultSet getOperatorsThatProcessData(String formatURI, String dataTypeURI){
		formatURI = "<" + formatURI + ">";
		dataTypeURI = "<" + dataTypeURI + ">";
		
		String stringQuery = 
				QUERY_PREFIX +
				"SELECT DISTINCT ?operator WHERE {{"
				+ "?operator a viskoO:InputOutputOperator . "
				+ "?operator viskoO:hasInputFormat " + formatURI + " . "
				+ "?operator viskoO:hasInputDataType " + dataTypeURI + " . } UNION {"
				+ "?operator a viskoO:InputOutputOperator . "
				+ "?operator viskoO:hasInputFormat " + formatURI + " . "
				+ "?operator viskoO:hasInputDataType ?superDataType . "
				+ dataTypeURI + " rdfs:subClassOf ?superDataType . }}";
		
		return submitQuery(stringQuery);
	}
	
	// Queries used for my backward chaining reasoning	
	public ResultSet getAdjacentOperatorsAccordingToFormatAndDataType(String operatorURI, String currentTypeURI){
		currentTypeURI = "<" + currentTypeURI + ">";
		operatorURI = "<" + operatorURI + ">";
		
		String stringQuery = 
				QUERY_PREFIX
				+ "SELECT DISTINCT ?operator WHERE {{"
				//+ operatorURI + " viskoO:hasOutputDataType ?dataType . "
				+ operatorURI + " viskoO:hasOutputFormat ?format . "
				+ "?operator a viskoO:InputOutputOperator . "				
				//+ "?operator viskoO:hasInputDataType ?dataType . "
				+ "?operator viskoO:hasInputDataType " + currentTypeURI + " . "
				+ "?operator viskoO:hasInputFormat ?format . } UNION {"				
				//+ operatorURI + " viskoO:hasOutputDataType ?subDataType . "
				+ operatorURI + " viskoO:hasOutputFormat ?format . "
				+ "?operator a viskoO:InputOutputOperator . "
				+ "?operator viskoO:hasInputDataType ?superDataType . "
				+ "?operator viskoO:hasInputFormat ?format . "
				//+ "?subDataType rdfs:subClassOf ?superDataType . }}";
				+ currentTypeURI + " rdfs:subClassOf ?superDataType . }}";
		
		return submitQuery(stringQuery);
	}
	
	public ResultSet getAdjacentNonFilterOperatorsAccordingToFormatAndDataType(String operatorURI, String currentTypeURI){
		currentTypeURI = "<" + currentTypeURI + ">";
		operatorURI = "<" + operatorURI + ">";
		
		String stringQuery = 
				QUERY_PREFIX
				+ "SELECT DISTINCT ?operator WHERE {{"
				+ operatorURI + " viskoO:hasOutputFormat ?format . "
				+ "?operator a viskoO:InputOutputOperator . "				
				+ "?operator viskoO:hasInputDataType " + currentTypeURI + " . "
				+ "?operator viskoO:hasInputFormat ?format . } UNION {"
				+ operatorURI + " viskoO:hasOutputFormat ?format . "
				+ "?operator a viskoO:InputOutputOperator . "
				+ "?operator viskoO:hasInputDataType ?superDataType . "
				+ "?operator viskoO:hasInputFormat ?format . "
				+ currentTypeURI + " rdfs:subClassOf ?superDataType . }"
				+ "FILTER NOTEXISTS {?operator a viskoO:Filter}}";
		
		return submitQuery(stringQuery);
	}
	
	public ResultSet getAdjacentOperatorsAccordingToFormat(String operatorURI){
		operatorURI = "<" + operatorURI + ">";
		
		String stringQuery = 
				QUERY_PREFIX
				+ "SELECT DISTINCT ?operator WHERE {"
				+ operatorURI + " viskoO:hasOutputFormat ?format . "
				+ "?operator a viskoO:InputOutputOperator . "				
				+ "?operator viskoO:hasInputFormat ?format . }";
		
		return submitQuery(stringQuery);
	}
	
	public ResultSet getTargetViewerForOperator(String operatorURI, String viewerSetURI){
		operatorURI = "<" + operatorURI + ">";
		viewerSetURI = "<" + viewerSetURI + ">";
		
		String stringQuery = 
				QUERY_PREFIX
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
		
		return submitQuery(stringQuery);
	}
	
	public boolean outputCanBeViewedByViewer(String operatorURI, String viewerURI){
		operatorURI = "<" + operatorURI + ">";
		viewerURI = "<" + viewerURI + ">";
		
		String stringQuery = 
				QUERY_PREFIX
				+ "ASK WHERE {{"
				+ viewerURI + " a viskoO:Viewer . "
				+ viewerURI + " viskoO:hasInputFormat ?format . "
				+ viewerURI + " viskoO:hasInputDataType ?dataType . "
				+ operatorURI + " viskoO:hasOutputFormat ?format . "
				+ operatorURI + " viskoO:hasOutputDataType ?dataType . }UNION{"
				+ viewerURI + " a viskoO:Viewer . "
				+ viewerURI + " viskoO:hasInputFormat ?format . "
				+ viewerURI + " viskoO:hasInputDataType ?superDataType . "
				+ operatorURI + " viskoO:hasOutputFormat ?format . "
				+ operatorURI + " viskoO:hasOutputDataType ?subDataType ."
				+ "?subDataType rdfs:subClassOf ?superDataType . }}";
				
		return submitAskQuery(stringQuery);
	}
	
	public boolean outputMatchesTargetData(String operatorURI, String targetFormatURI, String targetTypeURI){
		operatorURI = "<" + operatorURI + ">";
		targetFormatURI = "<" + targetFormatURI + ">";
		targetTypeURI = "<" + targetTypeURI + ">";
		
		String stringQuery = 
				QUERY_PREFIX
				+ "ASK WHERE {{"
				+ operatorURI + " viskoO:hasOutputFormat " + targetFormatURI + " . "
				+ operatorURI + " viskoO:hasOutputDataType " + targetTypeURI + " . }UNION{"
				+ operatorURI + " viskoO:hasOutputFormat " + targetFormatURI + " . "
				+ operatorURI + " viskoO:hasOutputDataType ?subDataType ."
				+ "?subDataType rdfs:subClassOf " + targetTypeURI + " . }}";
				
		return submitAskQuery(stringQuery);
	}
	
	public boolean outputCanBeViewedByViewerSet(String operatorURI, String viewerSetURI){
		operatorURI = "<" + operatorURI + ">";
		viewerSetURI = "<" + viewerSetURI + ">";
		
		String stringQuery = 
				QUERY_PREFIX
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
				
		return submitAskQuery(stringQuery);
	}
		
	public ResultSet getViewsGeneratedFrom(String mapperURI) {
		mapperURI = "<" + mapperURI + ">";
		String stringQuery = 
				QUERY_PREFIX
				+ "SELECT ?view WHERE {"
				+ mapperURI + " viskoO:mapsTo ?view . " + "}";

		return submitQuery(stringQuery);
	}
	
	public ResultSet getOperators(){
		String queryString = 
				QUERY_PREFIX
				+ "SELECT ?operator WHERE{"
				+ "?operator a viskoO:Operator . }";
		
		return submitQuery(queryString);
	}

	public ResultSet getViewerSets() {
		String stringQuery = 
				QUERY_PREFIX
				+ "SELECT ?viewerSet WHERE {"
				+ "?viewerSet rdf:type viskoO:ViewerSet . } ORDER BY ?viewerSet";

		return submitQuery(stringQuery);
	}
	
	public ResultSet getModuleInformation(){
		String stringQuery = 
				QUERY_PREFIX
				+ "SELECT ?toolkitLabel ?vendorURL ?detailsURL ?sourceURL ?moduleName WHERE {"
				+ "?toolkit a viskoS:Toolkit . "
				+ "?toolkit rdfs:label ?toolkitLabel . "
				+ "?toolkit viskoS:hasDocumentation ?vendorURL . "
				+ "?module viskoS:wrapsToolkit ?toolkit . "
				+ "?module viskoS:hasDocumentation ?detailsURL . "
				+ "?module rdfs:label ?moduleName . "
				+ "?module viskoS:hasSourceCode ?sourceURL . }";

		return submitQuery(stringQuery);
	}

	public ResultSet getEquivalentSADI(String owlsServiceURI) {
		owlsServiceURI = "<" + owlsServiceURI + ">";
		String stringQuery = 
				QUERY_PREFIX
				+ "SELECT ?sadiService WHERE {"
				+ owlsServiceURI + " <https://raw.github.com/nicholasdelrio/visko-rdf/master/rdf/ontology/visko-service.owl#hasEquivalentSADI> ?sadiService . }";

		return submitQuery(stringQuery);
	}

	public ResultSet getMappers() {
		String stringQuery = 
				QUERY_PREFIX
				+ "SELECT ?mapper " + "WHERE {"
				+ "?mapper rdf:type viskoO:Mapper . }";

		return submitQuery(stringQuery);
	}

	public ResultSet getViewers() {
		String stringQuery = 
				QUERY_PREFIX + "SELECT ?viewer WHERE {"
				+ "?viewer rdf:type viskoO:Viewer . }";

		return submitQuery(stringQuery);
	}

	public ResultSet getParameterBindings(String typeURI) {
		typeURI = "<" + typeURI + ">";
		String stringQuery = 
				QUERY_PREFIX 
				+ "SELECT ?param ?value " + "WHERE {"
				+ "?profile viskoS:profiles " + typeURI + " . "
				+ "?profile viskoS:declaresBindings ?binding ."
				+ "?binding owlsProcess:toParam ?param . "
				+ "?binding owlsProcess:valueData ?value . " + "}";

		return submitQuery(stringQuery);
	}
	
	public ResultSet getInputParameters(String serviceURI) {
		serviceURI = "<" + serviceURI + ">";
		String stringQuery = QUERY_PREFIX + "SELECT ?parameter "
				+ "WHERE {"
				+ serviceURI + " viskoS:supportedByOWLSService ?owlsService . "
				+ "?owlsService owlsService:describedBy ?process . "
				+ "?process owlsProcess:hasInput ?parameter . " + "}";

		return submitQuery(stringQuery);
	}
	
	public boolean submitAskQuery(String query){
		return SPARQL_EndpointFactory.executeAskQuery(query);
	}
	
	public ResultSet submitQuery(String query){
		return SPARQL_EndpointFactory.executeQuery(query);
	}
}
