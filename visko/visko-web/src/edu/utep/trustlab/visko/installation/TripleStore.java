/*
Copyright (c) 2012, University of Texas at El Paso
All rights reserved.
Redistribution and use in source and binary forms, with or without modification, are permitted
provided that the following conditions are met:

	-Redistributions of source code must retain the above copyright notice, this list of conditions
	 and the following disclaimer.
	-Redistributions in binary form must reproduce the above copyright notice, this list of conditions
	 and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.*/


package edu.utep.trustlab.visko.installation;
	
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.velo.java.client.Path;
import org.velo.java.client.VeloJavaClient;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.tdb.TDBFactory;

import edu.utep.trustlab.visko.util.FileUtils;

	public class TripleStore {

	private String packageDir;
	private String resourcesDir;
	private String dataTypesDir;
	private String tripleStoreDir;
	private Model model;

	private String serverUrl;
	private String serverType;
	private String serverUsername;
	private String serverPassword;
	private String serverProject;
	
	public TripleStore(String packageDirectory, String resourcesDirectory, String dataTypesDirectory, String tripleStoreDirectory){
		packageDir = packageDirectory;
		resourcesDir = resourcesDirectory;
		tripleStoreDir = tripleStoreDirectory;
		dataTypesDir = dataTypesDirectory;
		
		if(!tripleStoreDir.endsWith("/"))
			tripleStoreDir += "/";
		
		try {
			Properties props = new Properties();
			InputStream propsFile = getClass().getResourceAsStream("/registry.properties");
			props.load(propsFile);
			serverType = props.getProperty("content.management.server.type");
			serverUrl = props.getProperty("content.management.server.url");
			serverUsername = props.getProperty("content.management.server.username");
			serverPassword = props.getProperty("content.management.server.password");
			serverProject = props.getProperty("content.management.server.project.name");

			// Load RDF 
			// TODO: support other content management servers besides velo
			if(serverType != null && serverType.equals("velo")) {
				loadRDFFromVelo();
			}
      
		}
		catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 */
	private void loadRDFFromVelo() {
	 
	  VeloJavaClient client = new VeloJavaClient();
	  client.setBaseUrl(serverUrl);
	  client.setAuthentication(serverUsername, serverPassword);
    
	  // download all files from the server project to the packageDir directory
	  File packageDirectory = new File(packageDir);

	  List<org.velo.java.client.Resource> children = client.getCollection("/Projects/" + serverProject);
	  for (org.velo.java.client.Resource child : children) {
		  System.out.println("child webdav path = " + child.getWebdavPath());
		  String fileName = new Path(child.getWebdavPath()).getFileName();
		  client.getFile("/Projects/" + serverProject + "/" + fileName, packageDirectory.getAbsolutePath());
	  }
	}

	public String create(){
		
		File storesDirectory = new File(tripleStoreDir);

		TripleStore.clean(storesDirectory);
		Dataset ds = TDBFactory.createDataset(tripleStoreDir);
		model = ds.getDefaultModel();
		
		File packageDirectory = new File(packageDir);
		aggregate(packageDirectory);
		aggregateResources();
		aggregateDataTypes();
				
		System.out.println("Model now has " + model.size() + " statements.");
		model.close();
		
		return storesDirectory.getAbsolutePath();
	}
	
	private void aggregateResources(){
		File resourcesDirectory = new File(resourcesDir);
		
		if(resourcesDirectory.isDirectory()){
			File[] resourceDirectories = resourcesDirectory.listFiles();
			
			for(File resourceDirectory : resourceDirectories){
				if(isValidResourceDirectory(resourceDirectory)){
					aggregate(resourceDirectory);
				}
			}
		}
	}
	
	private boolean isValidResourceDirectory(File resourceDirectory){
		String dirName = resourceDirectory.getName();
		String[] legalDirs = new String[]{"ontology"};
		
		boolean isLegal = false;
		for(String legalDir : legalDirs){
			if(dirName.equals(legalDir))
				isLegal = true;
		}
		
		return isLegal && resourceDirectory.isDirectory();
	}
	
	private void aggregateDataTypes(){
		File dataTypesDir = new File(this.dataTypesDir);
		if(dataTypesDir.exists())
			this.aggregate(dataTypesDir);
	}
	
	private void aggregatePackages(){
		File packageDirectory = new File(packageDir);
		if(packageDirectory.isDirectory()){
			File[] packages = packageDirectory.listFiles();
			for(File aPackage : packages){
				if(aPackage.getName().startsWith("package") && aPackage.isDirectory()){
					aggregate(aPackage);
				}
			}
		}
		else
			System.out.println("The package directory: " + packageDir + " does not point to a directory!");
	}
	
	private static void clean(File storesDirectory){
		// If it exists then clean it, else create it.
		if (storesDirectory.exists()){
			for(File aFile : storesDirectory.listFiles()){
				aFile.delete();
			}
		}
		else
			storesDirectory.mkdir();
	}
	
	
	private void cleanModuleRDF(File moduleFile){
		String contents = FileUtils.readTextFile(moduleFile.getAbsolutePath());
		contents = contents.replaceAll("file.*visko/visko-modules", "http://visko.cybershare.utep.edu:5080");
		contents = contents.replaceAll("build/dist/ModuleService.wsdl", "services/ModuleService?wsdl");
		FileUtils.writeTextFile(contents, moduleFile.getAbsolutePath());
	}
	
	private void aggregate(File directory){
		// Iterate through all ontology files and load any pml data found
		System.out.println("Adding OWL/RDF files in directory: " + directory.getAbsolutePath());
		
		for(File aFile : directory.listFiles()){
			if((aFile.getName().endsWith(".owl") || aFile.getName().endsWith(".rdf")) && isAcceptedFile(aFile)){
				System.out.println("aggregating file: " + aFile.getName());
				cleanModuleRDF(aFile);
				try{model.read(new FileInputStream(aFile), null);}
				catch(Exception e){
					System.out.println("error: " + e.getMessage());
				}
			}
		}
	}
	
	public boolean isAcceptedFile(File file){
		String[] badFiles = new String[]{"cf-obj.owl"};
		for(String badFileName : badFiles){
			if(badFileName.equals(file.getName()))
			{
				System.out.println("ignoring file: " + file.getName());
				return false;
			}
		}
		return true;
	}
	
	public static void main(String[] args){
		if(args.length != 4){
			System.out.println("incorrect number of args specified; required: 4");
			return;
		}
		
		String moduleRegistryDirectory = args[0];
		String viskoResourcesDirectory = args[1];
		String dataTypesDirectory = args[2];
		String tripleStoreDirectory = args[3];
		
		System.out.println("viskoResourcesDirectory: " + viskoResourcesDirectory);
		
		TripleStore ts = new TripleStore(moduleRegistryDirectory, viskoResourcesDirectory, dataTypesDirectory, tripleStoreDirectory);
		System.out.println(ts.create());
	}
}
