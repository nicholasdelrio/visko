package org.openvisko.module.util;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

public class ModuleProperties {
	
	private static ModuleProperties instance;
	
	private URL hostingServerURL;
	private String webappName;
	private File tomcatHomePath;

	public static ModuleProperties getInstance(){
		if(instance == null)
			instance = new ModuleProperties();
		
		return instance;
	}
	
	private ModuleProperties(){
		try {
			Properties moduleProps = getModuleProperties();
			
			// set server url
			String urlString = moduleProps.getProperty("module.server.url");
			if(!urlString.endsWith("/"))
				urlString += "/";
			hostingServerURL = new URL(urlString);
			
			// set webapp name
			webappName = moduleProps.getProperty("module.server.webapp.name");
			
			// set tomcat home path, if set.  it can be null. it is only set when user wants to deploy module in server different than where visko-web is being hosted
			String pathString = moduleProps.getProperty("module.server.tomcat.home");
			if(pathString != null)
				tomcatHomePath = new File(pathString);
			
		} catch (Throwable e) {e.printStackTrace();}
	}
	
	
	public String getWebappName(){
		return webappName;
	}
	
	URL getHostingServerURL(){
		return hostingServerURL;
	}
	
	File getTomcatHomePath(){
		return tomcatHomePath;
	}
	
	public Properties getModuleProperties() throws Exception {
		Properties moduleProps = new Properties();
		InputStream propsFile = FileUtils.class.getResourceAsStream("/module.properties");
		moduleProps.load(propsFile);
		return moduleProps;
	}
}