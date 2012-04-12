package edu.utep.trustlab.contentManagement;

import java.io.File;

import edu.utep.trustlab.visko.util.FileUtilities;

public class ReplaceText {
	public static void main(String[] args){
		String rdfDirectory = args[0];
		String github_username = args[1];
		String github_replace = args[2];
		
		// Iterate through all instance data rdf files and load any pml data found
		File rdf = new File(rdfDirectory);
		for(File aFile : rdf.listFiles()){
			if(aFile.getName().toLowerCase().endsWith(".owl")){
				System.out.println("processing: " + aFile.getAbsolutePath());
				String contents = FileUtilities.readTextFile(aFile.getAbsolutePath());
				String updatedContents = contents.replaceAll(github_replace, github_username);
				FileUtilities.writeTextFile(updatedContents, aFile.getAbsolutePath());
			}
		}
	}
}