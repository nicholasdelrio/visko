package edu.utep.trustlab.repository;

import java.io.File;

import edu.utep.trustlab.visko.util.FileUtilities;

public class SwitchRepositoryURL {
	private static String nicholasdelrio = "nicholasdelrio";
	public static void main(String[] args){
		String rdfDirectory = args[0];
		String github_username = args[1];
		
		// Iterate through all instance data rdf files and load any pml data found
		File rdf = new File(rdfDirectory);
		for(File aFile : rdf.listFiles()){
			if(aFile.getName().toLowerCase().endsWith(".owl")){
				System.out.println("processing: " + aFile.getAbsolutePath());
				String contents = FileUtilities.readTextFile(aFile.getAbsolutePath());
				String updatedContents = contents.replaceAll(nicholasdelrio, github_username);
				FileUtilities.writeTextFile(updatedContents, aFile.getAbsolutePath());
			}
		}
	}
}