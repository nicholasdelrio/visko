package edu.utep.trustlab.visko.packages.manager;

import edu.utep.trustlab.contentManagement.CIServer;
import edu.utep.trustlab.contentManagement.VeloClientAdapter;

public class Installer_ContentManager {
	
	public static void main(String[] args){
	
		if(args.length == 6){
			configureContentManager(args);
			
			//package installer based parameters
			String packagesRootDirectory = args[5];
			
			PackageInstaller installer = new PackageInstaller(packagesRootDirectory);
			installer.installPackages();
		}
		else
			System.out.println("missing arguments...");
	}
	
	private static void configureContentManager(String[] args){
		//content manager based parameters
		String contentManagerType = args[0];
		String serverURL = args[1];
		String projectName = args[2];
		String username = args[3];
		String password = args[4];
		
		if(contentManagerType.equals("velo")){
			VeloClientAdapter client = new VeloClientAdapter(serverURL, username, password);
			client.setProjectName(projectName);
		}
		else{
			CIServer client = new CIServer(serverURL, username, password);
			client.setProjectName(projectName);
		}
	}
}