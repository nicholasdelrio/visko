package edu.utep.trustlab.repository;

import edu.utep.trustlab.visko.util.FileUtilities;

public class LocalFileSystem extends Repository{
	
	private String url;
	private String path;
	
	public LocalFileSystem(String serverURL, String serverPath) {
		url = serverURL;
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