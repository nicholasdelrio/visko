package edu.utep.trustlab.repository;

public abstract class Repository {

	private static Repository SERVER;
	
	public static void setRepository(Repository server){
		SERVER = server;
	}
	
	public static Repository getRepository(){
		return SERVER;
	}
	
	public abstract String saveDocument(String fileContents, String fileName);
	public abstract String getBaseURL();
}
