package edu.utep.trustlab.visko.installation.packages;

import edu.utep.trustlab.visko.installation.packages.rdf.PackageWriter;

public abstract class RDFPackage {
	
	public static String PARTIAL_QUALIFIED_NAME = ".rdfPackage.PackageSource";
	
	private PackageWriter packageWriter;
	
	public abstract void populateViewerSets();
	public abstract void populateToolkit();
	public abstract void populateServices();
	public abstract void populateParameterBindings();
	public abstract String getWSDLURL();
	
	public void setPackageWriter(PackageWriter writer){
		packageWriter = writer;
	}
	
	public PackageWriter getPackageWriter(){
		return packageWriter;
	}
}
