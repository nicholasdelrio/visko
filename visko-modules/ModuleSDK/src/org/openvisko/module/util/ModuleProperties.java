package org.openvisko.module.util;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

public class ModuleProperties {
	
	private static ModuleProperties instance;

	// optional properties - if null that is fine
	private URL hostingServerURL;
	private File tomcatHomePath;

	static ModuleProperties getInstance(){
		if(instance == null)
			instance = new ModuleProperties();
		
		return instance;
	}
	
	private ModuleProperties(){
		try {
			Properties moduleProps = getModuleProperties();
						
			// set server url
			String urlString = moduleProps.getProperty("module.server.url");
			if(urlString != null){
				if(!urlString.endsWith("/"))
					urlString += "/";
				hostingServerURL = new URL(urlString);
			}
						
			// set tomcat home path
			String pathString = moduleProps.getProperty("module.server.tomcat.home");
			if(pathString != null)
				tomcatHomePath = new File(pathString);
			
		} catch (Throwable e) {e.printStackTrace();}
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