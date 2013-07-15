package org.openvisko.module;

import org.openvisko.module.operators.ToolkitOperator;
import org.openvisko.module.util.FileUtils;
import org.openvisko.module.util.CommandRunner;

public class Grdimage extends ToolkitOperator{
	
	/*
	 * ASUMPTION: the input netCDF Dataset is 2D grid with the following variable set:
	 * - variable 'x' represents longitude
	 * - variable 'y' represents latitude
	 * - variable 'z' represents data value
	 */

	private static final String SCRIPT_XYZ2IMAGE = FileUtils.getScriptsDir().getAbsolutePath() + "/" + "wrapper-grdimage.sh";

	public Grdimage(String netCDFDataURL){	
		super(netCDFDataURL, "griddedData.nc", false, false, "raster-map.ps");
	}
		
	public String transform(
			String C,
			String J,
			String B,
			String T,
			String R)
	{		
		String command = 
			SCRIPT_XYZ2IMAGE + " "
			+ inputPath + " "
			+ outputPath + " "
			+ T + " "
			+ R + " "
			+ FileUtils.getOutputDir().getAbsolutePath() + " "
			+ C + " "
			+ J + " "
			+ B;
		CommandRunner.run(command);
	    
		return outputURL;
	}
}
