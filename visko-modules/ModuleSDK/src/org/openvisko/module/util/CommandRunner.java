package org.openvisko.module.util;

import java.io.File;
import java.io.FileOutputStream;


public class CommandRunner
{
	
	public static void run(String cmd, String workingDirectory){
		System.out.println("running commmand...");
		System.out.println(cmd);
		try{
			Process proc = Runtime.getRuntime().exec(cmd, null, new File(workingDirectory));
			// any error message?
			StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream(), "ERROR");
			FileOutputStream fos = FileUtils.getInstance().getLoggingStream();
			// any output?
			StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream(), "OUTPUT", fos);

			// kick them off
			errorGobbler.start();
			outputGobbler.start();

			proc.waitFor();

			fos.flush();
			fos.close();
    
		} catch (Throwable e){
      if(e instanceof RuntimeException) {
        throw (RuntimeException)e;
      } else {
        throw new RuntimeException(e);
      }
    }
	}	

	public static void run(String cmd){
		
		System.out.println("running commmand...");
		System.out.println(cmd);
		try{
			Process proc = Runtime.getRuntime().exec(cmd);
			// any error message?
			StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream(), "ERROR");
			FileOutputStream fos = FileUtils.getInstance().getLoggingStream();
			// any output?
			StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream(), "OUTPUT", fos);

			// kick them off
			errorGobbler.start();
			outputGobbler.start();

			proc.waitFor();

			fos.flush();
			fos.close();
		} catch (Throwable e){
		  if(e instanceof RuntimeException) {
		    throw (RuntimeException)e;
		  } else {
		    throw new RuntimeException(e);
		  }
		}
	}
}