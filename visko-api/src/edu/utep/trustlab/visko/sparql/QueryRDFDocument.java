package edu.utep.trustlab.visko.sparql;

import java.io.StringReader;
import java.util.Vector;

import edu.utep.trustlab.visko.util.ResultSetToVector;

import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.query.*;

public class QueryRDFDocument {
	private Model model;
	private QueryExecution qe;

	private static String QUERY_PREFIX = "PREFIX viskoV: <http://trust.utep.edu/visko/ontology/visko-view-v3.owl#> "
			+ "PREFIX viskoO: <http://trust.utep.edu/visko/ontology/visko-operator-v3.owl#> "
			+ "PREFIX viskoS: <http://trust.utep.edu/visko/ontology/visko-service-v3.owl#> "
			+ "PREFIX owlsService: <http://www.daml.org/services/owl-s/1.2/Service.owl#>"
			+ "PREFIX owlsProcess: <http://www.daml.org/services/owl-s/1.2/Process.owl#>"
			+ "PREFIX owl: <http://www.w3.org/2002/07/owl#> "
			+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
			+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> "
			+ "PREFIX pmlp: <http://inference-web.org/2.0/pml-provenance.owl#> "
			+ "PREFIX pmlj: <http://inference-web.org/2.0/pml-justification.owl#> "
			+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> ";

	public static String LOCAL_API_STORE_LOCATION = "C:\\Documents and Settings\\csmaster\\workspace\\ViskoTripleStore\\TDB-VISKO-3";
	public static String TOMCAT_STORE_LOCATION = "/home/users/tomcat/visko-triple-store/TDB-VISKO-3";
	public static String IW_STORE_LOCATION = "/usr/local/trustlab-apps/visko/triple-store/TDB-VISKO-3";
	public static String IW_STORE_LOCATION_FOR_WEBAPP = "/usr/local/trustlab-apps/visko/triple-store-for-webapp/TDB-VISKO-3";

	public QueryRDFDocument() {
		model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
	}

	private void aggregateRDFFile(String rdfContents) {
		StringReader reader = new StringReader(rdfContents);
		model.read(reader, null);
	}

	public void close() {
		model.close();
	}

	public ResultSet getParameterBindingsFromProfileURI(String rdfContents,
			String profileURI) {
		aggregateRDFFile(rdfContents);

		profileURI = "<" + profileURI + ">";
		String stringQuery = QUERY_PREFIX + "SELECT ?param ?value " + "WHERE {"
				+ profileURI + " viskoS:declaresBindings ?binding ."
				+ "?binding owlsProcess:toParam ?param . "
				+ "?binding owlsProcess:valueData ?value . " + "}";

		Query query = QueryFactory.create(stringQuery);
		qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();
		return results;
	}

	public Vector<String[]> getURLFormatAndType(String nodesetContents,
			String nodesetURI) {
		this.aggregateRDFFile(nodesetContents);

		nodesetURI = "<" + nodesetURI + ">";
		String stringQuery = QUERY_PREFIX + "SELECT ?url ?format ?type "
				+ "WHERE {" + nodesetURI + " pmlj:hasConclusion ?conclusion ."
				+ "?conclusion pmlp:hasFormat ?format . "
				+ "?conclusion a ?type . " + "?conclusion pmlp:hasURL ?url . "
				+ "}";

		Query query = QueryFactory.create(stringQuery);
		qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();

		return ResultSetToVector.getVectorTriplesFromResultSet(results, "url",
				"format", "type");
	}
}
