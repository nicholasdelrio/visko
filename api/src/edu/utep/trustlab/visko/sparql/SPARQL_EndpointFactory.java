package edu.utep.trustlab.visko.sparql;

import com.hp.hpl.jena.query.ResultSet;

public class SPARQL_EndpointFactory{
	
	private static SPARQL_Endpoint endPointConnection;

	public static SPARQL_Endpoint setUpEndpointConnection(String endPointLocation){
		if(endPointConnection == null){
			endPointConnection = createNewEndpointConnection(endPointLocation);
		}
		
		return endPointConnection;
	}
	
	private static SPARQL_Endpoint createNewEndpointConnection(String endPointLocation){
	
		if(endPointLocation.startsWith("http")){
			return new SPARQL_RemoteEndpoint(endPointLocation);
		}
		
		return new SPARQL_LocalEndpoint(endPointLocation);
	}

	
	public static ResultSet executeQuery(String queryString) {
		return endPointConnection.executeQuery(queryString);
	}

	public static boolean executeAskQuery(String queryString) {
		return endPointConnection.executeAskQuery(queryString);
	}
}
