package org.openvisko.module.util;

import java.io.File;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.UUID;

public class PropertyDependentPaths {
	
	private static PropertyDependentPaths instance;
	
	private String SERVER_URL;
	private String WEBAPP_NAME;
	private File WEBAPP_DIR;
	private File OUTPUT_DIR;
	private File SCRIPTS_DIR;

	private final String OUTPUT_DIR_NAME = "output";
	private final String SCRIPTS_DIR_NAME = "scripts";
	
	public static PropertyDependentPaths getInstance(){
		if(instance == null)
			instance = new PropertyDependentPaths();
		
		return instance;
	}
	
	private PropertyDependentPaths(){
		try {
			// Initialize properties
			Properties moduleProps = getModuleProperties();
			SERVER_URL = moduleProps.getProperty("module.server.url");

			if(!SERVER_URL.endsWith("/")) {
				SERVER_URL += "/";
			}
			WEBAPP_NAME = moduleProps.getProperty("module.server.webapp.name");

			// module specific tomcat server
			String tomcatHomePath = moduleProps.getProperty("module.server.tomcat.home");
			if(tomcatHomePath == null) {
				// If no specific module tomcat provided, then we are deploying to same
				// server as visko-web, so use that location instead
				Properties serverProps = getServerProperties();
				tomcatHomePath = serverProps.getProperty("server-base-path");
				
				String override = serverProps.getProperty("override-module-url");
				
				if(override != null){
					SERVER_URL = serverProps.getProperty("server-url");

					if(!SERVER_URL.endsWith("/"))
						SERVER_URL += "/";
				}
			}
			
			System.out.println("tomcat home Path: " + tomcatHomePath);
			
			File tomcatHome = new File(tomcatHomePath);
			File webappsDir = new File(tomcatHome, "webapps");
			WEBAPP_DIR = new File(webappsDir, WEBAPP_NAME);
			OUTPUT_DIR = new File(WEBAPP_DIR, OUTPUT_DIR_NAME);
			SCRIPTS_DIR = new File(WEBAPP_DIR, SCRIPTS_DIR_NAME);

			setScriptsToExecutable();
			
		} catch (Throwable e) {e.printStackTrace();}
	}
	
	private void setScriptsToExecutable(){
		if(SCRIPTS_DIR != null){
		
			File[] scripts = SCRIPTS_DIR.listFiles();
		
			if(scripts != null){
				for(File script : scripts){

					if(script.isFile()){
						//covert from dos2unix
						String contents = FileUtils.readTextFile(script.getAbsolutePath()).replaceAll("\r\n", "\n");
						FileUtils.writeTextFile(contents, SCRIPTS_DIR.getAbsolutePath(), script.getName());
						script.setExecutable(true, false);
					}
				}
			}
		}
	}
	
	
	public URL getServerBaseURL(){
		try
		{return new URL(SERVER_URL);}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}	  
	}
	
	public URL getModuleHTMLDescription(){
		String stringURL = getServerBaseURL().toString() + getWebappName() + "/" + getWebappName() + ".html";
		try
		{return new URL(stringURL);}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	public URL getModuleSourceCode(){
		String stringURL = getServerBaseURL().toString() + getWebappName() + "/org/openvisko/module/ModuleRDFRegistration.java";
		try
		{return new URL(stringURL);}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public URL getOutputURLPrefix(){
		try
		{return new URL(SERVER_URL + WEBAPP_NAME + "/" + OUTPUT_DIR_NAME + "/");}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public String getWebappName(){
		return WEBAPP_NAME;
	}
	
	public Properties getModuleProperties() throws Exception {
		Properties moduleProps = new Properties();
		InputStream propsFile = FileUtils.class.getResourceAsStream("/module.properties");
		moduleProps.load(propsFile);
		return moduleProps;
	}

	public Properties getServerProperties() throws Exception {
		Properties serverProps = new Properties();    
		InputStream serverPropsFile = null;

		// first try to get machine-specific or user-specific server properties
		String username = System.getProperty("user.name");
		System.out.println("looking for server." + username + ".properties");
		if(username != null) {
			serverPropsFile = FileUtils.class.getResourceAsStream("/server." + username + ".properties");
		}
		if(serverPropsFile == null) {
			String host = getHostName();
			System.out.println("hostname is: " + host);
			if(host != null) {
				serverPropsFile = FileUtils.class.getResourceAsStream("/server." + host + ".properties");
				if(serverPropsFile == null)
					serverPropsFile = FileUtils.class.getResourceAsStream("/server." + host.toUpperCase() + ".properties");
			}
		}
		if(serverPropsFile == null) {
			serverPropsFile = FileUtils.class.getResourceAsStream("/server.properties");
		}    

		if(serverPropsFile != null) {
			serverProps.load(serverPropsFile);
		}
		return serverProps;
	}
	
	private static String getHostName() {
		String host = null;
		try {
			InetAddress addr = InetAddress.getLocalHost();
			host = addr.getHostName();

		} catch (UnknownHostException e) { 
			System.err.println(e); 
		}
		return host;
	}
	
	public File getLoggingDir(){
		return new File(WEBAPP_DIR, "log");
	}

	public File getOutputDir() {
		return OUTPUT_DIR;
	}

	public File getWebappDir() {
		return WEBAPP_DIR;
	}

	public File getScriptsDir() {
		return SCRIPTS_DIR;
	}
	
	public String getOutputURLPrefix(File temporaryOutputDir){
		return SERVER_URL + WEBAPP_NAME + "/" + OUTPUT_DIR_NAME + "/" + temporaryOutputDir.getName();
	}
	
	/**
	 * Modules can use this method to create a unique output directory
	 * for an invocation of their service
	 * @return
	 */
	public File getTemporaryOutputDirectory() {
		// TODO: use date and time instead of uuid
		String uuid = UUID.randomUUID().toString();
		File tempDir = new File(OUTPUT_DIR, uuid);
		tempDir.mkdir();
		return tempDir;
	}
	
	/**
	 * Download the input file from remote location or copy it from
	 * local file system, depending on format of url.
	 * @param fileUrl
	 * @param destFolder
	 */
	public File getInputFile(String fileUrl, File destFolder) throws Exception { 
		File inputFile = null;

		if(fileUrl.startsWith("file:")) {
			// this is a local file url
			URI uri = new URL(fileUrl).toURI();
			inputFile = new File(uri);

		} else { // http url 

			// see if url points to file on same web server
			if(fileUrl.startsWith(SERVER_URL)) {
				URL url = new URL(fileUrl);
				String localFilePath = OUTPUT_DIR.getAbsolutePath() + url.getFile();
				inputFile = new File(localFilePath);
			}

			if(inputFile == null || !inputFile.exists()) {
				System.out.println("file doesn't exist on file system of server...need to http get it");
				inputFile = GetURLContents.getFile(fileUrl, destFolder);

			}
		}

		return inputFile;
	}
	
	public boolean existsOnLocalFileSystem(String datasetURL){
		return datasetURL.startsWith(SERVER_URL);
	}

	public File getOutputFile(String outputFileName){
		File outputFile = new File (OUTPUT_DIR, outputFileName);
		return outputFile;
	}
}