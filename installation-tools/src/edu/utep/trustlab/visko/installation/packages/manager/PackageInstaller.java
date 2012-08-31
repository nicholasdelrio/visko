package edu.utep.trustlab.visko.installation.packages.manager;

import java.io.File;

import edu.utep.trustlab.contentManagement.ContentManager;
import edu.utep.trustlab.visko.installation.packages.RDFPackage;
import edu.utep.trustlab.visko.installation.packages.rdf.PackageWriter;
import edu.utep.trustlab.visko.util.FileUtils;

public class PackageInstaller {
	
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
			}
		}
	}
	
	private boolean installPackage(File aPackageDirectory){
		String rdfWriterQualifiedClassName = aPackageDirectory.getName() + RDFPackage.PARTIAL_QUALIFIED_NAME;
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
		rdfPackage.setPackageWriter(packageWriter);
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