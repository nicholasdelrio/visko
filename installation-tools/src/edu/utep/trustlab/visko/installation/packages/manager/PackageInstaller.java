package edu.utep.trustlab.visko.installation.packages.manager;

import java.io.File;

import edu.utep.trustlab.contentManagement.ContentManager;
import edu.utep.trustlab.visko.installation.packages.RDFPackage;
import edu.utep.trustlab.visko.installation.packages.rdf.PackageWriter;

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
				if(viskoPackage.getName().startsWith("package")){
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
			
			System.out.println("created toolkit");
			rdfPackage.populateToolkit();
			
			System.out.println("created viewer sets");
			rdfPackage.populateViewerSets();
			
			System.out.println("created services");
			rdfPackage.populateServices();
			
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
	}
}