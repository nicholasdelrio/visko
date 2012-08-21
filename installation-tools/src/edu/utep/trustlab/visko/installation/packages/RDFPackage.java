package edu.utep.trustlab.visko.installation.packages;

import edu.utep.trustlab.visko.installation.packages.rdf.PackageWriter;

public abstract class RDFPackage {
	
	private PackageWriter packageWriter;
	
	public abstract void populateViewerSets();
	public abstract void populateToolkit();
	public abstract void populateServices();
	
	public void setPackageWriter(PackageWriter writer){
		packageWriter = writer;
	}
	
	public PackageWriter getPackageWriter(){
		return packageWriter;
	}
}
