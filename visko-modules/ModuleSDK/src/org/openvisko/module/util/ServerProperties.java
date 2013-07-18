package org.openvisko.module.util;

import java.io.File;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.UUID;

public class ServerProperties {
	
	private static ServerProperties instance;
	
	private String SERVER_URL;

	private File WEBAPP_DIR;
	private File OUTPUT_DIR;
	private File SCRIPTS_DIR;

	private final String OUTPUT_DIR_NAME = "output";
	private final String SCRIPTS_DIR_NAME = "scripts";
	
	public static ServerProperties getInstance(){
		if(instance == null)
			instance = new ServerProperties();
		
		return instance;
	}
	
	private ServerProperties(){
		try{

			Properties serverProps = getServerProperties();
			
			File tomcatHomePath = ModuleProperties.getInstance().getTomcatHomePath();
			if(tomcatHomePath == null){
				String stringPath = serverProps.getProperty("server-base-path");
				tomcatHomePath = new File(stringPath);
			}
			
			
			URL moduleServerURL = ModuleProperties.getInstance().getHostingServerURL();
			if(moduleServerURL == null){
				String urlString = serverProps.getProperty("server-url");
				if(!urlString.endsWith("/"))
					urlString += "/";
				SERVER_URL = urlString;
			}
			else
				SERVER_URL = moduleServerURL.toString();

			File webappsDir = new File(tomcatHomePath, "webapps");		
			WEBAPP_DIR = new File(webappsDir, ModuleNameProperty.getInstance().getName());
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
		String stringURL = getServerBaseURL().toString() + ModuleNameProperty.getInstance().getName() + "/" + ModuleNameProperty.getInstance().getName() + ".html";
		try
		{return new URL(stringURL);}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	public URL getModuleSourceCode(){
		String stringURL = getServerBaseURL().toString() + ModuleNameProperty.getInstance().getName() + "/org/openvisko/module/ModuleRDFRegistration.java";
		try
		{return new URL(stringURL);}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public URL getOutputURLPrefix(){
		try
		{return new URL(SERVER_URL + ModuleNameProperty.getInstance().getName() + "/" + OUTPUT_DIR_NAME + "/");}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
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
				if(serverPropsFile == null){
					System.out.println("still null");
					serverPropsFile = FileUtils.class.getResourceAsStream("/server." + host.toUpperCase() + ".properties");
				}
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
		return SERVER_URL + ModuleNameProperty.getInstance().getName() + "/" + OUTPUT_DIR_NAME + "/" + temporaryOutputDir.getName();
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