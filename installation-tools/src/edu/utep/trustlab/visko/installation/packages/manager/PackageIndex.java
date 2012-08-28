package edu.utep.trustlab.visko.installation.packages.manager;

import java.io.File;
import java.io.FileReader;
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

public class PackageIndex {
	
	private File packageDir;
	private OntModel model;
	
	public PackageIndex(File packageDirectory){
		packageDir = packageDirectory;
	}
	
	public String getHTMLIndex(){
		aggregateOWLDocuments();

		String indexHTML = "<h1>Toolkit</h1>";
		indexHTML += getToolkitHTML();
		
		indexHTML += "<h1>ViewerSets</h1>\n";
		indexHTML += getViewerSetsHTML();
		
		indexHTML += "<h1>Services</h1>";
		indexHTML += getServicesHTML();
		
		return indexHTML;
	}
	
	private String getToolkitHTML(){
		String queryString = "SELECT ?toolkit WHERE { ?toolkit a viskoS:Toolkit . ?toolkit rdfs:label ?label . }";
		ResultSet results = executeQuery(queryString);
		
		QuerySolution aSolution;
		String[] toolkit = null;
		
		String html = "<p>No Toolkit installed...error!</p>";
		if(results.hasNext()){
			aSolution = results.next();
			toolkit = new String[]{aSolution.get("?toolkit").toString(), aSolution.get("?toolkit").toString()};
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
		}
		return viewerSetsHTML;
	}
	
	private String getServicesHTML(){
		String queryString = "SELECT ? WHERE { ?service a viskoS:Service . ?service rdfs:label ?label . }";
		ResultSet results = executeQuery(queryString);
		
		QuerySolution aSolution;
		String[] service = null;
		
		String html = "<p>No Services installed.</p>\n";
		
		if(results.hasNext()){
			html = "<ul>\n";
			while(results.hasNext()){
				aSolution = results.next();
				service = new String[]{aSolution.get("?toolkit").toString(), aSolution.get("?toolkit").toString()};
				html += "\t<li>" + service[1] + "</li>\n";
			}
			html += "</ul>\n";
		}
		return html;
	}
	
	private List<String[]> getViewerSets(){
		String queryString = "SELECT ?viewerSet WHERE { ?viewerSet a viskoO:ViewerSet . ?viewerSet rdfs:label ?label . }";
		ResultSet results = executeQuery(queryString);
		
		ArrayList<String[]> viewerSets = new ArrayList<String[]>();
		QuerySolution aSolution;
		while(results.hasNext()){
			aSolution = results.next();
			viewerSets.add(new String[]{aSolution.get("?viewer").toString(), aSolution.get("?label").toString()});
		}
		return viewerSets;
	}
	
	private List<String[]> getViewersForViewerSet(String viewerSetURI){
		viewerSetURI = "<" + viewerSetURI + ">";
		
		String queryString = 
				"SELECT ?viewer ?label WHERE " +
				"{ ?viewer viskoO:partOfViewerSet " + viewerSetURI + " . " +
				"?viewer rdfs:label ?label . }";
		
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
		for(File aFile :packageDir.listFiles()){
			if(aFile.isFile() && aFile.getName().endsWith(".owl"))
				aggregateOWLDocument(aFile);
		}
	}
	
	private void aggregateOWLDocument(File owlFile){
		model = ModelFactory.createOntologyModel();
		
		try{
			FileReader reader = new FileReader(owlFile);
			model.read(reader, null, " RDF/XML");
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
