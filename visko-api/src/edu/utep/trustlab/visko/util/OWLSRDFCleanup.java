package edu.utep.trustlab.visko.util;

public class OWLSRDFCleanup {
	
	public static String fixURIForImplementsOperator(String owlsRDF, String baseURI){
		return owlsRDF.replace("<viskoS:implementsOperator rdf:resource=\"",  "<viskoS:implementsOperator rdf:resource=\"" + baseURI);
	}

	public static String fixURIForSupportedByToolkit(String owlsRDF, String baseURI){
		return owlsRDF.replace("<viskoS:supportedBy rdf:resource=\"",  "<viskoS:supportedBy rdf:resource=\"" + baseURI);
	}
}
