package org.openvisko.module;

import org.openvisko.module.installation.ModuleInstaller;

import edu.utep.trustlab.contentManagement.CIServer;
import edu.utep.trustlab.contentManagement.ContentManager;
import edu.utep.trustlab.contentManagement.LocalFileSystem;
import edu.utep.trustlab.contentManagement.VeloClientAdapter;

public class TestModuleInstaller {
  public static void main(String[] args){
    
      //configureContentManager();
      
      //package installer based parameters
      String packageFolder = "C:\\Eclipse\\Workspaces\\SdmCAT\\module_imagej";
      String dataTypesFolder = "C:\\Eclipse\\Workspaces\\SdmCAT\\ModuleSDK\\dataTypes";
      String wsdlFile = "C:\\Eclipse\\Workspaces\\SdmCAT\\module_imagej\\build\\dist\\ModuleService.wsdl";
      String registryFolder = "C:\\Eclipse\\Workspaces\\SdmCAT\\visko-web\\WebContent\\registry";
      
      LocalFileSystem contentMgr = new LocalFileSystem("http://localhost:8080/visko-web/registry", registryFolder);
      contentMgr.setSaveInWorkspace();
      ContentManager.setViskoRDFContentManager(contentMgr);
      
      ModuleInstaller installer = new ModuleInstaller(dataTypesFolder, packageFolder, wsdlFile, ContentManager.getViskoRDFContentManager());
      installer.installPackages();
   
  }
  
  private static void configureContentManager(){
    //content manager based parameters
    String contentManagerType = "velo";
    String serverURL = "http://sdmcat.pnl.gov/alfresco/";
    String projectName = "visko-modules-rdf";
    String username = "admin";
    String password = "sdmcat";
    
    ContentManager client;
    if(contentManagerType.equals("velo")){
      client = new VeloClientAdapter(serverURL, username, password);
      client.setProjectName(projectName);
    }
    else{
      client = new CIServer(serverURL, username, password);
      client.setProjectName(projectName);
    }
    ContentManager.setViskoRDFContentManager(client);
  }
}
