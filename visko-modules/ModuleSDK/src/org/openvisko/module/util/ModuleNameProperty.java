package org.openvisko.module.util;

import java.io.InputStream;
import java.util.Properties;

public class ModuleNameProperty {
	
	private static ModuleNameProperty instance;
	
	// required property - if null, must quit execution
	private String moduleName;
	
	public static ModuleNameProperty getInstance(){
		if(instance == null)
			instance = new ModuleNameProperty();
		return instance;
	}
	
	private ModuleNameProperty(){
		try {
			Properties moduleProps = getModuleProperties();
	
			moduleName = moduleProps.getProperty("module.server.webapp.name");
			if(moduleName == null){
				System.out.println("Module name is not set!!! Please set this property in module.properties.");
				System.exit(0);
			}
					
		} catch (Throwable e) {e.printStackTrace();}

	}
	
	public String getName(){
		return moduleName;
	}
	
	public Properties getModuleProperties() throws Exception {
		Properties moduleProps = new Properties();
		InputStream propsFile = FileUtils.class.getResourceAsStream("/module.properties");
		moduleProps.load(propsFile);
		return moduleProps;
	}
}
