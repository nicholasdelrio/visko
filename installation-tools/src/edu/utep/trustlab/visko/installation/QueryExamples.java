package edu.utep.trustlab.visko.installation;

import java.io.File;

import edu.utep.trustlab.visko.util.FileUtils;

public class QueryExamples {
	
	private String viskoURL;
	private String templatePath;
	private String newDirectory;
	
	public QueryExamples(String templateFilePath, String viskoRDFBaseURL, String newDirectoryPath){
		templatePath = templateFilePath;
		viskoURL = viskoRDFBaseURL;
		newDirectory = newDirectoryPath;
	}
	
	public String generateQueryExamples(){	
		File templateFile = new File(templatePath);
		String html = FileUtils.readTextFile(templatePath);
		html = html.replaceAll("REPLACE-VISKO", viskoURL);	
		
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
		
		if(args.length == 2)
			qe = new QueryExamples(args[0], args[1], null);
		else
			qe = new QueryExamples(args[0], args[1], args[2]);
		
		System.out.println(qe.generateQueryExamples());
	}
}