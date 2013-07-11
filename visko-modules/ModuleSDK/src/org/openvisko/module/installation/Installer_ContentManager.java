package org.openvisko.module.installation;

import java.io.InputStream;
import java.util.Properties;

import edu.utep.trustlab.contentManagement.CIServer;
import edu.utep.trustlab.contentManagement.ContentManager;
import edu.utep.trustlab.contentManagement.LocalFileSystem;
import edu.utep.trustlab.contentManagement.VeloClientAdapter;

public class Installer_ContentManager {
	
	public static void main(String[] args){
	
		if(args.length == 5) {
			
			//package installer based parameters
			String packageFolder = args[0];
			String dataTypesFolder = args[1];
			String wsdlFile = args[2];
			String registryFolder = args[3];
			String viskoServerUrl = args[4];
			
			configureContentManager(viskoServerUrl, registryFolder);
			ModuleInstaller installer = new ModuleInstaller(dataTypesFolder, packageFolder, wsdlFile, ContentManager.getViskoRDFContentManager());
			installer.installPackages();
		}
		else
			System.out.println("missing arguments...");
	}
	
	private static void configureContentManager(String viskoServerUrl, String registryFolderPath) {
	  ContentManager contentMgr = new LocalFileSystem(viskoServerUrl, registryFolderPath);
	  ContentManager.setViskoRDFContentManager(contentMgr);
	}
	
//	private static void configureContentManager(){
//	  // load parameters from registry.properties file 
//	  try {
//	    Properties registryProps = new Properties();
//	    InputStream propsFile = Installer_ContentManager.class.getResourceAsStream("/registry.properties");
//	    registryProps.load(propsFile);
//
//	    String registryType = registryProps.getProperty("registry.type");
//	    ContentManager contentMgr;
//
//	    if(registryType.equals("filesystem")) {
//	      String filePath = registryProps.getProperty("filesystem.registry.location");
//	      contentMgr = new LocalFileSystem("", filePath);
//
//	    } else { // cms
//	      
//	      String contentManagerType = registryProps.getProperty("content.management.server.type");
//	      String serverURL = registryProps.getProperty("content.management.server.url");
//	      String projectName = registryProps.getProperty("content.management.server.project.name");
//	      String username = registryProps.getProperty("content.management.server.username");
//	      String password = registryProps.getProperty("content.management.server.password");
//
//	      if(contentManagerType.equals("velo")){
//	        contentMgr = new VeloClientAdapter(serverURL, username, password);
//	        contentMgr.setProjectName(projectName);
//	      }
//	      else{
//	        contentMgr = new CIServer(serverURL, username, password);
//	        contentMgr.setProjectName(projectName);
//	      }
//	    }
//	    ContentManager.setViskoRDFContentManager(contentMgr);
//
//	    } catch (Throwable e) {
//	      e.printStackTrace();
//	    }
//	}
}