package org.openvisko.module;

import org.openvisko.module.operators.ToolkitOperator;
import org.openvisko.module.util.CommandRunner;
import org.openvisko.module.util.ServerProperties;

public class Grdview extends ToolkitOperator{
	
	/*
	 * ASUMPTION: the input netCDF Dataset is 2D grid with the following variable set:
	 * - variable 'x' represents longitude
	 * - variable 'y' represents latitude
	 * - variable 'z' represents data value
	 */
	private static final String SCRIPT_CONTOUR = ServerProperties.getInstance().getScriptsDir().getAbsolutePath() + "/" + "wrapper-grdview.sh";

	public Grdview(String netCDFDataURL){	
		super(netCDFDataURL, "griddedData.nc", false, false, "3D-mesh.ps");
	}
	
	public String transform(
			String contourInterval,
			String annotationInterval,
			String projection,
			String smoothing,
			String boundaryAnnotationInterval,
			String annotationPen,
			String contourPen)
	{
		String cmd = SCRIPT_CONTOUR + " "
		+ inputPath + " "
		+ outputPath + " "
		+ contourInterval + " "
		+ annotationInterval + " "
		+ projection + " "
		+ smoothing + " "
		+ boundaryAnnotationInterval + " "
		+ annotationPen + " "
		+ contourPen;
		
	    CommandRunner.run(cmd);
	    
		return outputURL;
	}
}//end class 
