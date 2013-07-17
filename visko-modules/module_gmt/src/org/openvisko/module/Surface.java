package org.openvisko.module;

import org.openvisko.module.operators.ToolkitOperator;
import org.openvisko.module.util.CommandRunner;
import org.openvisko.module.util.ServerProperties;

public class Surface extends ToolkitOperator
{
	/* ASUMPTION: the input dataset is in tabular space delimited ASCII file */
	
	private static final String SCRIPT_GRAVITY = ServerProperties.getInstance().getScriptsDir().getAbsolutePath() + "/" + "wrapper-surface.sh";

	public Surface(String asciiDataURL){	
		super(asciiDataURL, "xyzData.txt", true, false, "gridded-netcdf-surface.nc");
	}
		
	public String transform(
			String gridSpacing,
			String tension,
			String convergenceLimit,
			String region)
	{
		
		String cmd = 
			SCRIPT_GRAVITY + " "
			+ inputPath + " "
			+ outputPath + " "
			+ gridSpacing + " " 
			+ tension + " "
			+ convergenceLimit + " "
			+ region;
		
		CommandRunner.run(cmd);
		return outputURL;
	}
}
