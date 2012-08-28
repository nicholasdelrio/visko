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

import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.tdb.*;

	public class TripleStore {

	private String packageDir;
	private String resourcesDir;
	private String tripleStoreDir;
	private Model model;
	
	public TripleStore(String packageDirectory, String resourcesDirectory, String tripleStoreDirectory){
		packageDir = packageDirectory;
		resourcesDir = resourcesDirectory;
		tripleStoreDir = tripleStoreDirectory;
		
		if(!tripleStoreDir.endsWith("/"))
			tripleStoreDir += "/";
	}

	public String create(){
		
		File storesDirectory = new File(tripleStoreDir);

		TripleStore.clean(storesDirectory);
		model = TDBFactory.createModel(tripleStoreDir);
		
		aggregatePackages();
		aggregateResources();
				
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
		String[] illegalDirs = new String[]{"bin","datasets","engines", "parameters", "queries"};
		
		boolean isLegal = true;
		for(String illegalDir : illegalDirs){
			if(dirName.equals(illegalDir))
				isLegal = false;
		}
		return isLegal;
	}
	
	private void aggregatePackages(){
		File packageDirectory = new File(packageDir);
		if(packageDirectory.isDirectory()){
			File[] packages = packageDirectory.listFiles();
			for(File aPackage : packages){
				if(aPackage.getName().startsWith("package")){
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
	
	private void aggregate(File directory){
		// Iterate through all ontology files and load any pml data found
		
		for(File aFile : directory.listFiles()){
			if(aFile.getName().endsWith(".owl") || aFile.getName().endsWith(".rdf"))
			try{
				model.read(new FileInputStream(aFile), null);
			}
			catch(Exception e){
				System.out.println("error: " + e.getMessage());
			}
		}
	}
	
	public static void main(String[] args){
		TripleStore ts = new TripleStore(args[0], args[1], args[2]);
		System.out.println(ts.create());
	}
}
