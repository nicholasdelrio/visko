package edu.utep.trustlab.visko.installation.packages.manager;

import edu.utep.trustlab.contentManagement.ContentManager;
import edu.utep.trustlab.contentManagement.LocalFileSystem;

public class Installer_LocalFileSystem {
	
	public static void main(String[] args){
		
		if(args.length == 3){
			configureContentManager(args);
			
			//package installer based parameters
			String packagesRootDirectory = args[2];
			
			PackageInstaller installer = new PackageInstaller(packagesRootDirectory, ContentManager.getViskoRDFContentManager());
			installer.installPackages();
		}
		else
			System.out.println("missing arguments...");
	}
	
	private static void configureContentManager(String[] args){
		//content manager based parameters
		String serverURL = args[0];
		String serverFilePath = args[1];
		
		LocalFileSystem client = new LocalFileSystem(serverURL, serverFilePath);
		client.setSaveInWorkspace();
		ContentManager.setViskoRDFContentManager(client);
	}
}
