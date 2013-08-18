package org.openvisko.module.util;

import java.io.File;
import java.io.FileOutputStream;


public class CommandRunner
{
	
	public static int run(String cmd, String workingDirectory){
		System.out.println("running commmand...");
		System.out.println(cmd);
		int exitValue = -1;
		try{
			Process proc = Runtime.getRuntime().exec(cmd, null, new File(workingDirectory));
			// any error message?
			StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream(), "ERROR");
			FileOutputStream fos = FileUtils.getLoggingStream();
			// any output?
			StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream(), "OUTPUT", fos);

			// kick them off
			errorGobbler.start();
			outputGobbler.start();

			exitValue = proc.waitFor();

			fos.flush();
			fos.close();
    
		} catch(Exception e){e.printStackTrace();}
		
		return exitValue;
	}	

	public static int run(String cmd){
		
		System.out.println("running commmand...");
		System.out.println(cmd);
		int exitValue = -1;
		try{
			Process proc = Runtime.getRuntime().exec(cmd);
			// any error message?
			StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream(), "ERROR");
			FileOutputStream fos = FileUtils.getLoggingStream();
			// any output?
			StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream(), "OUTPUT", fos);

			// kick them off
			errorGobbler.start();
			outputGobbler.start();

			exitValue = proc.waitFor();

			fos.flush();
			fos.close();
		} catch (Exception e){e.printStackTrace();}
		
		return exitValue;
	}
}