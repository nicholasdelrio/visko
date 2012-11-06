package edu.utep.trustlab.visko.installation.packages.service.operators.util;

import java.io.*;
import java.util.*;

public class FileUtils{
	
	private static String SERVER;
	private static String OUTPUT;
	private static String SCRIPTS;

	private static final String WEBAPP = "toolkits/";
	private static final String OUTPUT_DIR_NAME = "output/";
	private static final String SCRIPTS_DIR_NAME = "scripts/";
	
	public static void setDeploymentPath(String serverBasePath){
		String basePath;
		
		if(serverBasePath.endsWith("/"))
			basePath = serverBasePath;
		else
			basePath = serverBasePath + "/";
		
		OUTPUT = basePath + "webapps/" + WEBAPP + OUTPUT_DIR_NAME;
		SCRIPTS = basePath + "webapps/" + WEBAPP + SCRIPTS_DIR_NAME;
	}
		
	public static void setServerURL(String url){
		if(url.endsWith("/"))
			SERVER = url;
		else
			SERVER = url + "/";
	}
	
	public static String getLoggingDir(){
		return OUTPUT + "log/";
	}

	public static String getWorkspace(){
		return OUTPUT;
	}
	
	public static String getImageMagickScripts(){
		return SCRIPTS + "scripts-imageMagick/";
	}
		
	public static String getGMTScripts(){
		return SCRIPTS + "scripts-gmt/";
	}
	
	public static String getNCLScripts(){
		return SCRIPTS + "scripts-ncl/";
	}
	
	public static String getNCLOperatorScripts(){
		return getNCLScripts() + "ncl/";
	}
	
	public static String getGravityTrustTableFilePath(){
		return "trust-data/gravity_trust.txt";
	}

	public static String getOutputURLPrefix(){
		return SERVER + WEBAPP + OUTPUT_DIR_NAME;
	}
	
	public static boolean existsOnLocalFileSystem(String datasetURL){
		return datasetURL.startsWith(getOutputURLPrefix());
	}
			
	public static String writeTextFile(String fileContents, String dirName, String fileName){
		File dirFile = new File(dirName);
		dirFile.mkdirs();
		
		String filePath = makeFullPath(dirName, fileName);
		
		try{
			BufferedWriter out = new BufferedWriter(new FileWriter(filePath));
			out.write(fileContents);
			out.close();
			return filePath;
		}
		catch (Exception e){
			e.printStackTrace();
			return e.toString();
		}
	}

	public static boolean exists(String filePath){
		File file = new File(filePath);
		if (file.exists())
			return true;
		return false;
	}

	public static FileOutputStream getLoggingStream(){
		try{
			return new FileOutputStream(getLoggingDir() + "/log.txt");
		}
		catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}

	public static String writeBinaryFile(byte[] fileContents, String dirName, String fileName){
		File dirFile = new File(dirName);
		dirFile.mkdirs();
		
		String filePath = makeFullPath(dirName, fileName);

		try{
			FileOutputStream out = new FileOutputStream(filePath);
			out.write(fileContents);
			out.close();
			return filePath;
		}
		catch (Exception e){
			e.printStackTrace();
			return e.toString();
		}
	}

	public static byte[] readBinaryFile(String fileName){
		try{
			FileInputStream in = new FileInputStream(fileName);
			byte[] fileContents = new byte[fileSize(fileName)];
			in.read(fileContents);
			in.close();
			return fileContents;
		}
		catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}

	public static String readTextFile(String fileName){
		try{
			BufferedReader in = new BufferedReader(new FileReader(fileName));
			String line, fileContents;

			fileContents = null;

			while ((line = in.readLine()) != null){
				if (fileContents == null)
					fileContents = line + "\n";
				else
					fileContents = fileContents + line + "\n";
			}
			
			in.close();
			return fileContents;
		}
		catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}

	public static String getRandomString(){
		// get the current time in milliseconds to use as data source temp file
		// name
		long miliseconds = (new Date()).getTime();
		String localFileName = new String("" + miliseconds);
		return localFileName;
	}

	public static void deleteFile(String fileName){
		File file = new File(fileName);
		file.delete();
	}

	public static void cleanWorkspace(String workspace){
		File file = new File(workspace);
		String[] wsFiles = file.list();
		if (wsFiles != null){
			for (String aFile : wsFiles){
				deleteFile(FileUtils.makeFullPath(workspace, aFile));
			}
		}
	}
	
	public static String getRandomFileNameFromFileName(String fileName){
		if(fileName.contains(".")){
			String[] fileNameParts = fileName.split("\\.");
			String name = fileNameParts[0];
			String extension = fileNameParts[1];
			
			return name + "_" + FileUtils.getRandomString() + "." + extension;
		}
		
		System.out.println("fileName: " + fileName + " doesn't have a file extension...bad move man.");
		return fileName + "_" + FileUtils.getRandomString();
	}

	public static String makePathWellFormedURI(String path){
		try{
			String uri = path.replaceAll("\"", "");
			return uri.replaceAll(" ", "%20");

		}
		catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}

	public static String makeFullPath(String dir, String fileName){
		File directory = new File(dir);
		File file = new File(fileName);
		
		return directory.getAbsolutePath() + File.separator + file.getName();
	}

	public static int fileSize(String fileName){
		File file = new File(fileName);
		return (int) file.length();
	}
	
	    
	  public static String getNameFromFilename(String fileName) {
	    StringTokenizer tokens = new StringTokenizer(fileName, ".");
	    String output = tokens.nextToken();
	    return output;
	  }

	  public static String getNameFromPath(String fileName) {
	    String name = new File(fileName).getName();
	    return name;
	  }

	  public static String getDirFromPath(String fileName) {
	    String path = new File(fileName).getParent();
	    return path;
	  }
}
