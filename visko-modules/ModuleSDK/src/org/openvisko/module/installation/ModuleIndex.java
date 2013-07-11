package org.openvisko.module.installation;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import edu.utep.trustlab.visko.sparql.ViskoTripleStore;

public class ModuleIndex {
	
	private File packageDir;
	private OntModel model;
	
	public ModuleIndex(File packageDirectory){
		packageDir = packageDirectory;
	}
	
	public String getHTMLIndex(){
		aggregateOWLDocuments();

		String indexHTML = "<h2>Toolkit</h2>\n";
		indexHTML += getToolkitHTML();
		
		indexHTML += "<h2>ViewerSets</h2>\n";
		indexHTML += getViewerSetsHTML();
		
		indexHTML += "<h2>Services</h2>\n";
		indexHTML += getServicesHTML();
		
		return 	
			"<html>" +
			"<head>" +
			"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">" +
			"<link rel=\"stylesheet\" type=\"text/css\" href=\"http://openvisko.org/visko-style.css\" />" +
			"<title>Package Contents: " + packageDir.getName() + "</title>" +
			"</head>" +
			"<body>" +
			indexHTML +
			"</body>" +
			"</html>";
	}
	
	private String getToolkitHTML(){
		String queryString = 
				ViskoTripleStore.QUERY_PREFIX
				+ "SELECT ?toolkit ?label WHERE {"
				+ "?toolkit a viskoS:Toolkit . "
				+ "?toolkit rdfs:label ?label . }";
		
		ResultSet results = executeQuery(queryString);
		
		QuerySolution aSolution;
		String[] toolkit = null;
		
		String html = "<p>No Toolkit installed...error!</p>";
		if(results.hasNext()){
			aSolution = results.next();
			toolkit = new String[]{aSolution.get("?toolkit").toString(), aSolution.get("?label").toString()};
			html = "<ul><li>" + toolkit[1] + "</li></ul>";			
		}		
		return html;
	}
	
	private String getViewerSetsHTML(){
		List<String[]> viewerSets = getViewerSets();
		
		String viewerSetsHTML = "<p>No ViewerSets installed.</p>\n";
		if(viewerSets.size() > 0)
		{
			viewerSetsHTML = "<ul>\n";
			for(String[] viewerSet : viewerSets){
				viewerSetsHTML += "\t<li>" + viewerSet[1] + "</li>\n";
				List<String[]> viewers = getViewersForViewerSet(viewerSet[0]);
				String viewersHTML = "\t<ul><li>No Viewers attached</li></ul>\n";
				if(viewers.size() > 0){
					viewersHTML = "\t<ul>\n";
					for(String[] viewer : viewers)
						viewersHTML += "\t\t<li>" + viewer[1] + "</li>\n";
					viewersHTML += "\t</ul>";
				}
				viewerSetsHTML += viewersHTML;
			}
			viewerSetsHTML += "</ul>";
		}
		return viewerSetsHTML;
	}
	
	private String getServicesHTML(){
		String queryString = ViskoTripleStore.QUERY_PREFIX
				+ "SELECT ?service ?label WHERE {"
				+ "?service a viskoS:Service . "
				+ "?service rdfs:label ?label . }";
		
		ResultSet results = executeQuery(queryString);
		
		QuerySolution aSolution;
		String[] service = null;
		
		String html = "<p>No Services installed.</p>\n";
		
		if(results.hasNext()){
			html = "<ul>\n";
			while(results.hasNext()){
				aSolution = results.next();
				service = new String[]{aSolution.get("?service").toString(), aSolution.get("?label").toString()};
				html += "\t<li>" + service[1] + "</li>\n";
			}
			html += "</ul>\n";
		}
		return html;
	}
	
	private List<String[]> getViewerSets(){
		String queryString = ViskoTripleStore.QUERY_PREFIX
				+ "SELECT ?viewerSet ?label WHERE {"
				+ "?viewerSet a viskoO:ViewerSet . "
				+ "?viewerSet rdfs:label ?label . }";

		ResultSet results = executeQuery(queryString);
		
		ArrayList<String[]> viewerSets = new ArrayList<String[]>();
		QuerySolution aSolution;
		while(results.hasNext()){
			aSolution = results.next();
			viewerSets.add(new String[]{aSolution.get("?viewerSet").toString(), aSolution.get("?label").toString()});
		}
		return viewerSets;
	}
	
	private List<String[]> getViewersForViewerSet(String viewerSetURI){
		viewerSetURI = "<" + viewerSetURI + ">";
		
		String queryString = 
				ViskoTripleStore.QUERY_PREFIX
				+ "SELECT ?viewer ?label WHERE "
				+ "{ ?viewer viskoO:partOfViewerSet " + viewerSetURI + " . "
				+ "?viewer rdfs:label ?label . }";
		
		queryString = ViskoTripleStore.QUERY_PREFIX + queryString;
		
		ResultSet results = executeQuery(queryString);
		
		ArrayList<String[]> viewers = new ArrayList<String[]>();
		QuerySolution aSolution;
		while(results.hasNext()){
			aSolution = results.next();
			viewers.add(new String[]{aSolution.get("?viewer").toString(), aSolution.get("?label").toString()});
		}
		return viewers;
	}
	
	private void aggregateOWLDocuments(){
		model = ModelFactory.createOntologyModel();

		for(File aFile :packageDir.listFiles()){
			if(aFile.isFile() && aFile.getName().endsWith(".owl"))
				aggregateOWLDocument(aFile);
		}
	}
	
	private void aggregateOWLDocument(File owlFile){		
		try{
			FileInputStream stream = new FileInputStream(owlFile);
			model.read(stream, null);
		}
		catch(Exception e){
			e.printStackTrace();
		}		
	}
	
	private ResultSet executeQuery(String queryString){
		Query query = QueryFactory.create(queryString) ;
		QueryExecution qexec = QueryExecutionFactory.create(query, model);  
		return qexec.execSelect();
	}
}
