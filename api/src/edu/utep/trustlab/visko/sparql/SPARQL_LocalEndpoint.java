package edu.utep.trustlab.visko.sparql;

import org.mindswap.pellet.jena.PelletReasonerFactory;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.tdb.TDBFactory;

public class SPARQL_LocalEndpoint implements SPARQL_Endpoint {
	
	private InfModel model;
	
	public SPARQL_LocalEndpoint(String tdbStorePath){
		Model assertedModel = TDBFactory.createDataset(tdbStorePath).getDefaultModel();
		model = ModelFactory.createInfModel(PelletReasonerFactory.theInstance().create(), assertedModel);
		assertedModel.close();
	}
	
	public ResultSet executeQuery(String queryString){
		  Query query = QueryFactory.create(queryString) ;
		  QueryExecution qexec = QueryExecutionFactory.create(query, model);
		  ResultSet results = qexec.execSelect();
		  return results;
	}
	
	public boolean executeAskQuery(String askQueryString){
		  Query query = QueryFactory.create(askQueryString) ;
		  QueryExecution qexec = QueryExecutionFactory.create(query, model);
		  boolean result = qexec.execAsk();
		  return result;		
	}	
}
