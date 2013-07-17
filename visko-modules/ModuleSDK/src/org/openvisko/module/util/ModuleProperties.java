package org.openvisko.module.util;

import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

public class ModuleProperties {
	
	private static ModuleProperties instance;
	
	private URL SERVER_URL;
	private String WEBAPP_NAME;

	public static ModuleProperties getInstance(){
		if(instance == null)
			instance = new ModuleProperties();
		
		return instance;
	}
	
	private ModuleProperties(){
		try {
			Properties moduleProps = getModuleProperties();
			String urlString = moduleProps.getProperty("module.server.url");
			
			if(!urlString.endsWith("/"))
				urlString += "/";
			
			SERVER_URL = new URL(urlString);
			
			WEBAPP_NAME = moduleProps.getProperty("module.server.webapp.name");												
		} catch (Throwable e) {e.printStackTrace();}
	}
	
	
	public String getWebappName(){
		return WEBAPP_NAME;
	}
	
	public URL getServer_URL(){
		return SERVER_URL;
	}
	
	public Properties getModuleProperties() throws Exception {
		Properties moduleProps = new Properties();
		InputStream propsFile = FileUtils.class.getResourceAsStream("/module.properties");
		moduleProps.load(propsFile);
		return moduleProps;
	}
}