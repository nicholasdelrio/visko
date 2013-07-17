package org.openvisko.module;

import org.openvisko.module.operators.ToolkitOperator;
import org.openvisko.module.util.CommandRunner;
import org.openvisko.module.util.ServerProperties;

public class Matlab2DRaster extends ToolkitOperator{

	private static final String SCRIPT_2D_RASTER = ServerProperties.getInstance().getScriptsDir().getAbsolutePath() + "/" + "matlab2DRaster.sh";
	
	public Matlab2DRaster(String inputFileURL){	
			super(inputFileURL, "originalImage.mat", false, false, "2DRaster.tif");
			
			//set script permission to executable
			String command = "chmod 755 " + SCRIPT_2D_RASTER;
			CommandRunner.run(command);			
	}
			
	public String transform(String inputFile, String outputFile, String selectedColor){
			String command;
			
			command = SCRIPT_2D_RASTER + " " + inputPath + " " + outputPath + " " +
					ServerProperties.getInstance().getWebappDir().getAbsolutePath() + " " + selectedColor;
			CommandRunner.run(command);
			
			System.out.println("Output URL: " + outputURL);
			return outputURL;
	}
} 
