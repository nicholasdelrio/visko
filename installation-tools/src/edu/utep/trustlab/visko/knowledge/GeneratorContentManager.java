package edu.utep.trustlab.visko.knowledge;

import edu.utep.trustlab.contentManagement.AlfrescoClient;
import edu.utep.trustlab.contentManagement.CIServer;
import edu.utep.trustlab.contentManagement.ContentManager;
import edu.utep.trustlab.contentManagement.LocalFileSystem;


public class GeneratorContentManager {
	public static void main(String[] args){
		
		ContentManager.setContentManager(getContentManager(args));
		ContentManager.setWorkspacePath(getOutputRDFDirectory(args));
		
		Generator1.gen();
		System.out.println("gen1 done");
		Generator2.gen();
		System.out.println("gen2 done");
		Generator3.gen();
		System.out.println("gen3 done");
		Generator4.gen();
		System.out.println("gen4 done");
	}
	
	public static String getOutputRDFDirectory(String[] args){
		return args[5];
	}
	
	public static ContentManager getContentManager(String[] args){
		String contentManager = args[0];
		
		if(contentManager.equalsIgnoreCase("github"))
			return initGitHub(args);
		else if(contentManager.equalsIgnoreCase("cat"))
			return initCAT(args);
		else if(contentManager.equalsIgnoreCase("ciserver"))
			return initCIServer(args);
		
		return null;
	}
	
	public static ContentManager initCIServer(String[] args){
		String serverURL = args[1];
		String projectName = args[2];
		String userName = args[3];
		String password = args[4];
		
		CIServer client =  new CIServer(serverURL, userName, password);
		client.setProjectName(projectName);
		return client;
	}

	public static ContentManager initCAT(String[] args){
		String serverURL = args[1];
		String projectName = args[2];
		String userName = args[3];
		String password = args[4];
		String webdavURL = args[5];

		AlfrescoClient client = new AlfrescoClient(serverURL, userName, password);
		client.setProjectName(projectName);
		if(!webdavURL.equalsIgnoreCase("null"))
			client.setWebDAVURL(webdavURL);
		return client;
	}

	public static ContentManager initGitHub(String[] args){
		String serverURL = args[1];
		String rdfFilePath = args[2];

		LocalFileSystem client =  new LocalFileSystem(serverURL, rdfFilePath);
		return client;		
	}
}
