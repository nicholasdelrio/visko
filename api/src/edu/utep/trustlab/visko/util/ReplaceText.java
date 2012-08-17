package edu.utep.trustlab.visko.util;

import java.io.File;

import edu.utep.trustlab.visko.util.FileUtils;

public class ReplaceText {
	
	private String directory;
	private String oldString;
	private String newString;
	
	public static void main(String[] args){
		String directory = args[0];
		String oldString = args[1];
		String newString = args[2];
		
		ReplaceText replace = new ReplaceText(directory, oldString, newString);
		replace.replace();
	}
	
	public ReplaceText(String dir, String oldText, String newText){
		directory = dir;
		oldString = oldText;
		newString = newText;
	}
	
	public void replace(){
		// Iterate through all instance data rdf files and load any pml data found
		File rdf = new File(directory);
		for(File aFile : rdf.listFiles()){
			if(aFile.getName().toLowerCase().endsWith(".owl")){
				System.out.println("processing: " + aFile.getAbsolutePath());
				String contents = FileUtils.readTextFile(aFile.getAbsolutePath());
				String updatedContents = contents.replaceAll(oldString, newString);
				FileUtils.writeTextFile(updatedContents, aFile.getAbsolutePath());
			}
		}
	}	
}