package edu.utep.trustlab.visko.installation.packages.manager;

import java.io.File;

import edu.utep.trustlab.visko.installation.packages.RDFPackage;

public class PackageInstaller {
	
	private static final String PARTIAL_QUALIFIED_NAME = ".rdfPackage.RDFPackage";
	
	private String packagesRoot;
	
	public PackageInstaller(String packagesRootDirectory){
		packagesRoot = packagesRootDirectory;
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
		String rdfWriterQualifiedClassName = aPackageDirectory.getName() + PARTIAL_QUALIFIED_NAME;
		System.out.println("Installing package: " + aPackageDirectory.getName());
		Class<?> rdfWriterClass;
		try{
			rdfWriterClass = Class.forName(rdfWriterQualifiedClassName);
			RDFPackage writer = (RDFPackage)rdfWriterClass.newInstance();

			System.out.println("created toolkit");
			writer.populateToolkit();
			
			System.out.println("created viewer sets");
			writer.populateViewerSets();
			
			System.out.println("created services");
			writer.populateServices();
			
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
}