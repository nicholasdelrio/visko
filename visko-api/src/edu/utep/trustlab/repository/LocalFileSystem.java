package edu.utep.trustlab.repository;

import edu.utep.trustlab.visko.util.FileUtilities;

public class LocalFileSystem extends Repository{
	
	public static final String VISKO_GITHUB_RDF_PATH = "../visko-rdf/";
	
	private String url;
	private String path;
	
	public LocalFileSystem(String serverURL, String serverPath) {
		url = serverURL;
		
		if(!url.endsWith("/"))
			url = url + "/";
		
		path = serverPath;
	}
	
	public LocalFileSystem(String serverURL){
		url = serverURL;
	}
	
	public String getBaseURL(){
		return url;
	}
	public String saveDocument(String fileContents, String fileName) {
		FileUtilities.writeTextFile(fileContents, path, fileName);
		return url + fileName;
	}
}