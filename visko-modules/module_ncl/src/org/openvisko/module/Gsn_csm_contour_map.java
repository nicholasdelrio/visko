package org.openvisko.module;

import org.openvisko.module.util.CommandRunner;
import org.openvisko.module.util.PropertyDependentPaths;

public class Gsn_csm_contour_map extends NCLOperator{
	private static final String SCRIPT_CONTOUR = PropertyDependentPaths.getInstance().getScriptsDir().getAbsolutePath() + "/" + "gsn_csm_contour_map.sh ";
	
	public Gsn_csm_contour_map(String netCDFURL){	
		super(netCDFURL, "griddedData.nc", false, false, "contourMap.ps");
	}
	
	public String transform(
			String plotVariable,
			String font,
			String lbOrientation,
			String cnLevelSpacingF,
			String colorTable,
			String cnLinesOn,
			String cnFillOn,
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
		" " + cnLevelSpacingF +
		" " + colorTable +
		" " + cnLinesOn +
		" " + cnFillOn +
		" " + latVariable +
		" " + lonVariable +
		" " + indexOfX +
		" " + indexOfY +
		" " + indexOfZ +
		" " + PropertyDependentPaths.getInstance().getScriptsDir().getAbsolutePath() + "/ncl/" ;
		
	    CommandRunner.run(cmd);   
		return outputURL;
	}
}