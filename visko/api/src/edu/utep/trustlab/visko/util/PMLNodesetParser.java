package edu.utep.trustlab.visko.util;

import java.util.Vector;


public class PMLNodesetParser {
	
	private String datasetURL;
	private String formatURI;
	private String typeURI;
	private String nodesetURI;
	
	public PMLNodesetParser(String uri){
		nodesetURI = uri;
	}
	
	public void parse(){
		try {
			String nodesetContents = GetURLContents.downloadText(nodesetURI);
			QueryRDFDocument rdf = new QueryRDFDocument();
			rdf.aggregateRDFFile(nodesetContents);
			Vector<String[]> results = rdf.getURLFormatAndType(nodesetURI);
		
			// rdf.close();
			datasetURL = results.firstElement()[0].split("\\^\\^")[0];
			System.out.println("datasetURL " + datasetURL);
			formatURI = results.firstElement()[1];
			typeURI = results.firstElement()[2];

		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getFormatURI(){
		return formatURI;
	}
	public String getTypeURI(){
		return typeURI;
	}
	public String getDatasetURL(){
		return datasetURL;
	}
}
