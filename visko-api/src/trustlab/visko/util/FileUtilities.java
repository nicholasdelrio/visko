package trustlab.visko.util;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * <p>
 * Title: Util
 * </p>
 * <p>
 * This class supports the writing, reading, downloading
 * <p>
 * and deleting of both text and binary files.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: UTEP
 * </p>
 * 
 * @author Nicholas Del Rio(ndel2@utep.edu)
 */

public class FileUtilities {

	public static byte[] downloadFile(String url) {// download file via http
													// protocol and return
													// contents as byte array

		try {
			ArrayList<Byte> array = new ArrayList<Byte>(); // buffer to hold
															// stream
			byte aByte; // temp variable for reading bytes
			Byte abyte; // temp variable for reading bytes

			// initialize connection
			URL yahoo = new URL(url);
			URLConnection fileLocation = yahoo.openConnection();

			BufferedInputStream bis = new BufferedInputStream(
					fileLocation.getInputStream());
			byte[] buff = new byte[2048];
			int bytesRead;

			// Simple read/write loop
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				for (int i = 0; i < bytesRead; i++) {
					aByte = buff[i];
					array.add(new Byte(aByte));
				}
			}

			// close inputstream
			bis.close();

			// trim array size to match actual content
			array.trimToSize();

			// convert from Byte[] to byte[]
			byte[] fileContents = new byte[array.size()];
			for (int i = 0; i < array.size(); i++) {
				abyte = (Byte) array.get(i);
				fileContents[i] = abyte.byteValue();
			}

			return fileContents;

		} catch (MalformedURLException u) {
			u.printStackTrace();
			return null;

		} catch (IOException i) {

			i.printStackTrace();
			return null;
		}
	}

	public static FileOutputStream getLoggingStream(String filePath) {
		try {
			return new FileOutputStream(filePath);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String writeTextFile(String fileContents, String dirName,
			String fileName) {
		File dirFile = new File(dirName);
		dirFile.mkdirs();
		String filePath = dirFile + File.separator + fileName;

		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(filePath));
			out.write(fileContents);
			out.close();
			return filePath;
		} catch (Exception e) {
			return e.toString();
		}
	}

	public static String writeTextFile(String fileContents, String fullPath) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(fullPath));
			out.write(fileContents);
			out.close();
			return fullPath;
		} catch (Exception e) {
			return e.toString();
		}
	}

	public static String writeBinaryFile(byte[] fileContents, String dirName,
			String fileName) {
		File dirFile = new File(dirName);
		dirFile.mkdirs();
		String filePath = dirFile + File.separator + fileName;

		try {
			FileOutputStream out = new FileOutputStream(filePath);
			out.write(fileContents);
			out.close();
			return filePath;
		} catch (Exception e) {
			return e.toString();
		}
	}

	public static byte[] readBinaryFile(String fileName) {
		try {
			FileInputStream in = new FileInputStream(fileName);
			byte[] fileContents = new byte[fileSize(fileName)];
			in.read(fileContents);
			in.close();
			return fileContents;
		} catch (Exception e) {
			return null;
		}
	}

	public static String readTextFile(String fileName) {
		try {
			BufferedReader in = new BufferedReader(new FileReader(fileName));
			String line, fileContents;

			fileContents = null;

			while ((line = in.readLine()) != null) {
				if (fileContents == null)
					fileContents = line + "\n";
				else
					fileContents = fileContents + line + "\n";
			}
			in.close();
			return fileContents;
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	public static String getRandomFileName() {
		double random = Math.random();
		String randomNumeric = random + "";
		randomNumeric = randomNumeric.replaceAll("\\.", "");
		return randomNumeric.substring(0);
	}

	public static int fileSize(String fileName) {
		File file = new File(fileName);
		return (int) file.length();
	}

	public static void deleteFile(String fileName) {
		File file = new File(fileName);
		file.delete();
	}

	public static boolean fileExists(String fileName) {
		File file = new File(fileName);
		return file.exists();
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
