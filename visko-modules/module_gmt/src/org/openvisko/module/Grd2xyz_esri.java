package org.openvisko.module;

import org.openvisko.module.operators.ToolkitOperator;
import org.openvisko.module.util.CommandRunner;
import org.openvisko.module.util.ServerProperties;

public class Grd2xyz_esri extends ToolkitOperator{
	
	/*
	 * ASUMPTION: the input netCDF Dataset is 2D grid with the following variable set:
	 * - variable 'x' represents longitude
	 * - variable 'y' represents latitude
	 * - variable 'z' represents data value
	 */
	private static final String SCRIPT_CONTOUR = ServerProperties.getInstance().getScriptsDir().getAbsolutePath() + "/" + "wrapper-grd2xyz-esri.sh";

	public Grd2xyz_esri(String netCDFDataURL){	
		super(netCDFDataURL, "griddedData.nc", false, false, "xyz.txt");
	}
	
	public String transform(
			String nanReplacement)
	{
		String cmd = SCRIPT_CONTOUR + " "
		+ inputPath + " "
		+ outputPath + " "
		+ nanReplacement;
		
	    CommandRunner.run(cmd);
	    
		return outputURL;
	}
}//end class 