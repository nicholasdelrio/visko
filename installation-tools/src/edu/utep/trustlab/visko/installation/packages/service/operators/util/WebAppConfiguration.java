package edu.utep.trustlab.visko.installation.packages.service.operators.util;

import java.io.File;

public class WebAppConfiguration {
	
	private String webConfigPath;
	private String sBasePath;
	private String sURL;
	private String dumpDir;
	
	public WebAppConfiguration(String webXMLPath, String serverBasePath, String serverURL, String dumpDirectory){
		webConfigPath = webXMLPath;
		sBasePath = serverBasePath;
		sURL = serverURL;
		dumpDir = dumpDirectory;
	}
	
	public String generateWebXML(){
		File webXMLFile = new File(webConfigPath);
		String webXML = FileUtils.readTextFile(webConfigPath);
		webXML = webXML.replaceAll("REPLACE-PATH", sBasePath);	
		webXML = webXML.replaceAll("REPLACE-URL", sURL);
		
		return FileUtils.writeTextFile(webXML, dumpDir, webXMLFile.getName());		
	}
	
	public static void main(String[] args){

		System.out.println("generating web.xml file!");
		WebAppConfiguration qe;
		qe = new WebAppConfiguration(args[0], args[1], args[2], args[3]);
		System.out.println(qe.generateWebXML());
	}
}