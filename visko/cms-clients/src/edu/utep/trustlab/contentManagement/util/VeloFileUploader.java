package edu.utep.trustlab.contentManagement.util;

import java.io.File;

import edu.utep.trustlab.contentManagement.VeloClientAdapter;
import edu.utep.trustlab.contentManagement.ContentManager;

public class VeloFileUploader {
	
	public static ContentManager manager;
	
	public static void main(String[] args){
		
		String username = "admin";
		String password = "booger1";
		String serverURL = "http://localhost:8080/alfresco/";
		String projectName = "formats";
		String formats = "C:\\Users\\Public\\git\\visko\\rdf\\formats\\";
		
		manager = new VeloClientAdapter(serverURL, username, password);
		manager.setProjectName(projectName);
		VeloClientAdapter.setWorkspacePath(formats);
		
		File formatDir = new File(formats);
		visitAllDirsAndFiles(formatDir);
	}
	
	public static void visitAllDirsAndFiles(File dir) {
        
		process(dir);
		
		System.out.println(dir.getAbsolutePath());
        
		if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                visitAllDirsAndFiles(new File(dir, children[i]));
            }
        }
    }

	private static void process(File file){
		String fileName = file.getName();
		
		if(fileName.endsWith(".owl"))
		{
			System.out.println("processing file: " + fileName);
			manager.saveDocument(file);
		}
	}
}
