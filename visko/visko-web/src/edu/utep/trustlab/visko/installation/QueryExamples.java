package edu.utep.trustlab.visko.installation;

import java.io.File;

import edu.utep.trustlab.visko.util.FileUtils;

public class QueryExamples {
	
	private String registryURL;
	private String templatePath;
	private String serverURL;
	private String newDirectory;	
	
	public QueryExamples(String templateFilePath, String viskoRegistryURL, String viskoBaseServerURL, String newDirectoryPath){
		templatePath = templateFilePath;
		
		if(viskoRegistryURL.endsWith("/"))
			registryURL = viskoRegistryURL;
		else
			registryURL = viskoRegistryURL + "/";

		if(viskoBaseServerURL.endsWith("/"))
			serverURL = viskoBaseServerURL;
		else
			serverURL = viskoBaseServerURL + "/";
		
		newDirectory = newDirectoryPath;
	}
	
	public String generateQueryExamples(){	
		File templateFile = new File(templatePath);
		String html = FileUtils.readTextFile(templatePath);
		
		html = html.replaceAll("REPLACE-VISKO", registryURL);
		html = html.replaceAll("SERVERBASEURL", serverURL);
		
		String queryFilePath;
		
		queryFilePath = FileUtils.writeTextFile(html, newDirectory, templateFile.getName());
		
		return queryFilePath;
	}
	
	public static void main(String[] args){

		System.out.println("generating query examples!");
		QueryExamples qe;
		
		qe = new QueryExamples(args[0], args[1], args[2], args[3]);
		
		System.out.println(qe.generateQueryExamples());
	}
}