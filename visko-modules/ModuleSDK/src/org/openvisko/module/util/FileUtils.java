package org.openvisko.module.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import java.util.StringTokenizer;


public class FileUtils{
	public static final int BUFFER_SIZE = 4096;
	
	/**
	 * Copy the contents of the given InputStream to the given OutputStream.
	 * Closes both streams when done.
	 * @param in the stream to copy from
	 * @param out the stream to copy to
	 * @return the number of bytes copied
	 * @throws IOException in case of I/O errors
	 */
	public static int copy(InputStream in, OutputStream out) throws IOException {

		try {
			int byteCount = 0;
			byte[] buffer = new byte[BUFFER_SIZE];
			int bytesRead = -1;
			while ((bytesRead = in.read(buffer)) != -1) {
				out.write(buffer, 0, bytesRead);
				byteCount += bytesRead;
			}
			out.flush();
			return byteCount;
		}
		finally {
			try {
				in.close();
			}
			catch (IOException ex) {
			}
			try {
				out.close();
			}
			catch (IOException ex) {
			}
		}
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
			return new FileOutputStream(ServerProperties.getInstance().getLoggingDir() + "/log.txt");
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


	public static String getFileName(String fileUrl) {
		// Get the file name
		if(fileUrl.endsWith("/")) {
			fileUrl = fileUrl.substring(0, fileUrl.length() - 1);
		}
		int lastSlash = fileUrl.lastIndexOf('/');
		String fileName = fileUrl.substring(lastSlash + 1);

		return fileName;
	}
}