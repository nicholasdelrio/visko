package edu.utep.trustlab.visko.installation.packages.manager;

import java.io.File;
import java.io.FileReader;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import edu.utep.trustlab.contentManagement.ContentManager;
import edu.utep.trustlab.visko.installation.packages.RDFPackage;
import edu.utep.trustlab.visko.installation.packages.rdf.PackageWriter;
import edu.utep.trustlab.visko.util.FileUtils;

public class PackageInstaller {
	
	private static String PARTIAL_QUALIFIED_NAME = ".rdfPackage.PackageSource";
	private static String DATA_TYPES_DIRECTORY_NAME = "dataTypes";
	
	private String packagesRoot;
	private ContentManager contentManager;
	
	public PackageInstaller(String packagesRootDirectory, ContentManager manager){
		packagesRoot = packagesRootDirectory;
		contentManager = manager;
	}
	
	public void installPackages(){
		File packagesRootDir = new File(packagesRoot);
		File[] packages;
		boolean installSuccess;
		if(packagesRootDir.isDirectory()){
			packages = packagesRootDir.listFiles();
			
			for(File viskoPackage : packages){
				if(viskoPackage.getName().startsWith("package") && viskoPackage.isDirectory()){
					installSuccess = installPackage(viskoPackage);
					
					if(installSuccess)
						System.out.println("package: " + viskoPackage.getName() + " installed successfully!");
					else
						System.out.println("package: " + viskoPackage.getName() + " failed to install!");
				}
				else
					System.out.println("ignoring non visko-package: " + viskoPackage.getName());
			}
		}
	}
	
	private boolean installPackage(File aPackageDirectory){
		String rdfWriterQualifiedClassName = aPackageDirectory.getName() + PARTIAL_QUALIFIED_NAME;
		System.out.println("Installing package: " + aPackageDirectory.getName());
		
		ContentManager.setWorkspacePath(aPackageDirectory.getAbsolutePath());
		
		Class<?> rdfWriterClass;
		try{
			rdfWriterClass = Class.forName(rdfWriterQualifiedClassName);
			RDFPackage rdfPackage = (RDFPackage)rdfWriterClass.newInstance();
			
			initializeRDFPackage(rdfPackage, aPackageDirectory);
			
			rdfPackage.populateToolkit();
			System.out.println("populated toolkit");

			rdfPackage.populateViewerSets();
			System.out.println("populated viewer sets");
			
			rdfPackage.populateServices();
			System.out.println("populated services");
	
			rdfPackage.populateParameterBindings();
			System.out.println("populated parameter bindings");
			
			finalizeRDFPackage(rdfPackage, aPackageDirectory);
			
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	private void initializeRDFPackage(RDFPackage rdfPackage, File packageDirectory){
		PackageWriter packageWriter = new PackageWriter(contentManager.getBaseURL(), packageDirectory.getName() + ".owl");
		packageWriter.setDataTypesOntModel(getDataTypesFromPackageOntology(packageDirectory));
		rdfPackage.setPackageWriter(packageWriter);
	}
	
	private OntModel getDataTypesFromPackageOntology(File packageDirectory){
		OntModel dataTypesModel = null;
		File[] subpackageDirectories = packageDirectory.listFiles();
		
		for(File subpackageDirectory : subpackageDirectories){
			if(subpackageDirectory.isDirectory() && subpackageDirectory.getName().equals(DATA_TYPES_DIRECTORY_NAME)){
				for(File dataTypeFile : subpackageDirectory.listFiles()){
					dataTypesModel = ModelFactory.createOntologyModel();
					try{dataTypesModel.read(new FileReader(dataTypeFile), null);}
					catch(Exception e){
						e.printStackTrace();
					}
				}
			}
		}
		return dataTypesModel;
	}
	
	private void finalizeRDFPackage(RDFPackage rdfPackage, File packageDirectory){
		ContentManager.setWorkspacePath(packageDirectory.getAbsolutePath());
		rdfPackage.getPackageWriter().dumpPackageRDF(contentManager);
		
		/*
		PackageIndex index = new PackageIndex(packageDirectory);
		String packageHTML = index.getHTMLIndex();
		
		FileUtils.writeTextFile(packageHTML, packageDirectory.getName(), "index" + ".html");*/
	}
}