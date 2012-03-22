package edu.utep.trustlab.query;

import java.io.File;

import edu.utep.trustlab.visko.util.FileUtilities;

public class QueryExamples {
	
	private String viskoURL;
	private String formatsURL;
	private String templatePath;
	private String newDirectory;
	
	public QueryExamples(String templateFilePath, String viskoRDFBaseURL, String formatsBaseURL, String newDirectoryPath){
		templatePath = templateFilePath;
		viskoURL = viskoRDFBaseURL;
		formatsURL = formatsBaseURL;
		newDirectory = newDirectoryPath;
	}
	
	public String generateQueryExamples(){	
		File templateFile = new File(templatePath);
		String html = FileUtilities.readTextFile(templatePath);
		html = html.replaceAll("REPLACE-VISKO", viskoURL);	
		html = html.replaceAll("REPLACE-FORMAT", formatsURL);
		
		String queryFilePath;
		
		if(newDirectory == null)
			queryFilePath = FileUtilities.writeTextFile(html, templatePath);
		else
			queryFilePath = FileUtilities.writeTextFile(html, newDirectory, templateFile.getName());
		
		return queryFilePath;
	}
	
	public static void main(String[] args){
		
	}

}
