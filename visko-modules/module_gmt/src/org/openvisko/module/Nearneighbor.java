package org.openvisko.module;

import org.openvisko.module.operators.ToolkitOperator;
import org.openvisko.module.util.CommandRunner;
import org.openvisko.module.util.ServerProperties;

public class Nearneighbor extends ToolkitOperator
{
	private static final String SCRIPT_GRAVITY_NN = ServerProperties.getInstance().getScriptsDir().getAbsolutePath() + "/" + "wrapper-nearneighbor.sh";

	public Nearneighbor(String asciiDataURL){	
		super(asciiDataURL, "xyzData.txt", true, false, "gridded-netcdf.nc");
	}

	public String transform(
			String gridSpacing,
			String searchRadius,
			String region)
	{		
		String cmd = 
			SCRIPT_GRAVITY_NN + " "
			+ inputPath + " "
			+ outputPath + " "
			+ gridSpacing + " "
			+ searchRadius + " "
			+ region;
		
		CommandRunner.run(cmd);
		return outputURL;
	}
}
