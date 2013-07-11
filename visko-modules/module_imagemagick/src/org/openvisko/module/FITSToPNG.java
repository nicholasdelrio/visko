package org.openvisko.module;

import org.openvisko.module.operators.ToolkitOperator;
import org.openvisko.module.util.CommandRunner;

public class FITSToPNG extends ToolkitOperator{

	private static final String IMAGEMAGICK = "convert";

	public FITSToPNG(String fitsFileURL){	
		super(fitsFileURL, "data.fits", false, false, "image.png");
	}

	public String transform(){
		String command = IMAGEMAGICK + " " + inputPath + " " + outputPath;
		CommandRunner.run(command);
		
		return outputURL;
	}
} 
