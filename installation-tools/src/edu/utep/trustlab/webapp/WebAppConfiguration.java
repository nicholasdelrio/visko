package edu.utep.trustlab.webapp;

import java.io.File;

import edu.utep.trustlab.visko.util.FileUtils;

public class WebAppConfiguration {
	
	private String webConfigPath;
	private String headerLogoPath;
	private String tripleStorePath;
	private String sparqlURL;
	private String dumpDirPath;
	private String org;
	
	public WebAppConfiguration(String webXMLPath, String logoPath, String sparqlEndpoint, String tdbPath, String organization, String newDirectoryPath){
		webConfigPath = webXMLPath;
		headerLogoPath = logoPath;
		tripleStorePath = tdbPath;
		dumpDirPath = newDirectoryPath;
		sparqlURL = sparqlEndpoint;
		org = organization;
	}
	
	public String generateWebXML(){
		File webXMLFile = new File(webConfigPath);
		String webXML = FileUtils.readTextFile(webConfigPath);
		webXML = webXML.replaceAll("REPLACE-LOGO-PATH", headerLogoPath);	
		webXML = webXML.replaceAll("REPLACE-ENDPOINT-URL", sparqlURL);
		webXML = webXML.replaceAll("REPLACE-TDB-PATH", tripleStorePath);
		webXML = webXML.replaceAll("REPLACE-ORGANIZATION", org);
		
		String webXMLPath;
		
		if(dumpDirPath == null)
			webXMLPath = FileUtils.writeTextFile(webXML, webConfigPath);
		else
			webXMLPath = FileUtils.writeTextFile(webXML, dumpDirPath, webXMLFile.getName());
		
		return webXMLPath;
	}
	
	public static void main(String[] args){

		System.out.println("generating web.xml file!");
		WebAppConfiguration qe;
		
		if(args.length == 4)
			qe = new WebAppConfiguration(args[0], args[1], args[2], args[3], args[4], null);
		else
			qe = new WebAppConfiguration(args[0], args[1], args[2], args[3], args[4], args[5]);
		
		System.out.println(qe.generateWebXML());
	}
}
