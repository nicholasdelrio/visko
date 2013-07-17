package org.openvisko.module;

import org.openvisko.module.operators.ToolkitOperator;
import org.openvisko.module.util.CommandRunner;
import org.openvisko.module.util.ServerProperties;

public class Spherize extends ToolkitOperator{

	private static final String SCRIPT_SPHERIZE = ServerProperties.getInstance().getScriptsDir().getAbsolutePath() + "/" + "spherize.sh";

	public Spherize(String fitsFileURL){	
		super(fitsFileURL, "image.png", false, false, "spherizedImage.png");
	}

	public String transform(){
		String command = SCRIPT_SPHERIZE + " " + inputPath + " " + outputPath;
		CommandRunner.run(command);
		
		return outputURL;
	}
} 
