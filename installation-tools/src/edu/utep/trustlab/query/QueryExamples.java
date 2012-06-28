package edu.utep.trustlab.query;

import java.io.File;

import edu.utep.trustlab.visko.util.FileUtils;

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
		String html = FileUtils.readTextFile(templatePath);
		html = html.replaceAll("REPLACE-VISKO", viskoURL);	
		html = html.replaceAll("REPLACE-FORMAT", formatsURL);
		
		String queryFilePath;
		
		if(newDirectory == null)
			queryFilePath = FileUtils.writeTextFile(html, templatePath);
		else
			queryFilePath = FileUtils.writeTextFile(html, newDirectory, templateFile.getName());
		
		return queryFilePath;
	}
	
	public static void main(String[] args){

		System.out.println("generating query examples!");
		QueryExamples qe;
		
		if(args.length == 3)
			qe = new QueryExamples(args[0], args[1], args[2], null);
		else
			qe = new QueryExamples(args[0], args[1], args[2], args[3]);
		
		System.out.println(qe.generateQueryExamples());
	}
}