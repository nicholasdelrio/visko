package edu.utep.trustlab.visko.installation;

import java.io.File;

import edu.utep.trustlab.visko.util.FileUtils;

public class WebAppConfiguration {
	
	//path to template web.xml
	private static String webConfigPath;
	
	//path to dump dir of complete web.xml
	private static String dumpDirPath;
	
	//server info
	private static String server_url;
	private static String server_base_path;
	
	//HTML header and logo
	private static String logo_path;
	private static String hosting_organization;
	
	//SPARQL endpoint and TDB path
	private static String visko_sparql_endpoint;
	private static String visko_tdb_path;
	
	//move data
	private static String move_data;
	
	//content manager info
	private static String content_manager_type;
	private static String content_manager_url;
	private static String content_manager_username;
	private static String content_manager_password;
	private static String content_manager_project;
	
	public String generateWebXML(){
		File webXMLFile = new File(WebAppConfiguration.webConfigPath);
		String webXML = FileUtils.readTextFile(WebAppConfiguration.webConfigPath);

		//server info
		webXML = webXML.replace("REPLACE-SERVER-URL", WebAppConfiguration.server_url);
		webXML = webXML.replace("REPLACE-SERVER-BASE-PATH", WebAppConfiguration.server_base_path);
		
		//HTML header and logo
		webXML = webXML.replace("REPLACE-ORGANIZATION", WebAppConfiguration.logo_path);
		webXML = webXML.replace("REPLACE-LOGO-PATH", WebAppConfiguration.hosting_organization);
		
		//TDB path
		webXML = webXML.replace("REPLACE-TDB-PATH", WebAppConfiguration.visko_tdb_path);
		
		//move data
		webXML = webXML.replace("REPLACE-MOVE-DATA", WebAppConfiguration.move_data);
		
		//content manager info
		webXML = webXML.replace("REPLACE-CONTENT-MANAGER-TYPE", WebAppConfiguration.content_manager_type);
		webXML = webXML.replace("REPLACE-CONTENT-MANAGER-URL", WebAppConfiguration.content_manager_url);
		webXML = webXML.replace("REPLACE-CONTENT-MANAGER-USERNAME", WebAppConfiguration.content_manager_username);
		webXML = webXML.replace("REPLACE-CONTENT-MANAGER-PASSWORD", WebAppConfiguration.content_manager_password);
		webXML = webXML.replace("REPLACE-CONTENT-MANAGER-PROJECT", WebAppConfiguration.content_manager_project);
		
		String webXMLPath;
		webXMLPath = FileUtils.writeTextFile(webXML, WebAppConfiguration.dumpDirPath, webXMLFile.getName());
		
		return webXMLPath;
	}
	
	public static void main(String[] args){

		System.out.println("generating web.xml file!");
		
		//path to template web.xml
		WebAppConfiguration.webConfigPath = args[0];
		
		//path to dump dir of complete web.xml
		WebAppConfiguration.dumpDirPath = args[1];
		
		//server info
		WebAppConfiguration.server_url = args[2];
		WebAppConfiguration.server_base_path = args[3];
		
		//HTML header and logo
		WebAppConfiguration.logo_path = args[4];
		WebAppConfiguration.hosting_organization = args[5];
		
		//SPARQL endpoint and TDB path
		WebAppConfiguration.visko_tdb_path = args[6];
		
		//content manager info
		WebAppConfiguration.content_manager_type = args[7];
		WebAppConfiguration.content_manager_url = args[8];
		WebAppConfiguration.content_manager_username = args[9];
		WebAppConfiguration.content_manager_password = args[10];
		WebAppConfiguration.content_manager_project = args[11];
		
		WebAppConfiguration.move_data = args[12];
		
		WebAppConfiguration webXMLGenerator = new WebAppConfiguration();
		String deployedPath = webXMLGenerator.generateWebXML();
		
		System.out.println("web.xml deployment path: " + deployedPath);
	}
}
