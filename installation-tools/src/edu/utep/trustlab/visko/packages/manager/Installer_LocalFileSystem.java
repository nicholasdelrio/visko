package edu.utep.trustlab.visko.packages.manager;

import edu.utep.trustlab.contentManagement.LocalFileSystem;

public class Installer_LocalFileSystem {
	
	public static void main(String[] args){
		
		if(args.length == 4){
			configureContentManager(args);
			
			//package installer based parameters
			String packagesRootDirectory = args[3];
			
			PackageInstaller installer = new PackageInstaller(packagesRootDirectory);
			installer.installPackages();
		}
		else
			System.out.println("missing arguments...");
	}
	
	private static void configureContentManager(String[] args){
		//content manager based parameters
		String serverURL = args[0];
		String serverFilePath = args[1];
		String projectName = args[2];
		
		LocalFileSystem client = new LocalFileSystem(serverURL, serverFilePath);
		client.setProjectName(projectName);
	}
}
