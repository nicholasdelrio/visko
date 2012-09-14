package edu.utep.trustlab.visko.sparql;

import com.hp.hpl.jena.query.ResultSet;

public interface SPARQL_Endpoint{
	
	public ResultSet executeQuery(String queryString);
	public boolean executeAskQuery(String queryString);
}