package edu.utep.trustlab.publish;

public abstract class Repository {

	private static Repository SERVER;
	
	public static void setServer(Repository server){
		SERVER = server;
	}
	
	public static Repository getServer(){
		return SERVER;
	}
	
	public abstract String saveDocument(String fileContents, String fileName);
	public abstract String getBaseURL();
}
