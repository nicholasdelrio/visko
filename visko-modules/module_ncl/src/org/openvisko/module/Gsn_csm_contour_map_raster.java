package org.openvisko.module;

import org.openvisko.module.util.CommandRunner;
import org.openvisko.module.util.ServerProperties;

public class Gsn_csm_contour_map_raster extends NCLOperator{

	private static final String SCRIPT_CONTOUR = ServerProperties.getInstance().getScriptsDir().getAbsolutePath() + "/"  +  "gsn_csm_contour_map_raster.sh ";
	
	public Gsn_csm_contour_map_raster(String netCDFURL){	
		super(netCDFURL, "griddedData.nc", false, false, "rasterMap.ps");
	}
	
	public String transform(
			String plotVariable,
			String font,
			String lbOrientation,
			String colorTable,
			String latVariable,
			String lonVariable,
			String indexOfX,
			String indexOfY,
			String indexOfZ
		){
		
		String cmd = SCRIPT_CONTOUR + 
		inputPath + 
		" " + outputPath +
		" " + plotVariable +
		" " + font +
		" " + lbOrientation +
		" " + colorTable +
		" " + latVariable +
		" " + lonVariable +
		" " + indexOfX +
		" " + indexOfY +
		" " + indexOfZ +
		" " + ServerProperties.getInstance().getScriptsDir().getAbsolutePath() + "/ncl/" ;
		
	    CommandRunner.run(cmd);   
		return outputURL;
	}
}