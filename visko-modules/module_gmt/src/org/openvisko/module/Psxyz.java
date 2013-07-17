package org.openvisko.module;

import org.openvisko.module.operators.ToolkitOperator;
import org.openvisko.module.util.CommandRunner;
import org.openvisko.module.util.ServerProperties;

public class Psxyz extends ToolkitOperator{
	
	/* ASUMPTION: the input dataset is in tabular space delimited ASCII file */
	
	private static final String PLOTTER_2D = ServerProperties.getInstance().getScriptsDir().getAbsolutePath() + "/" + "wrapper-psxyz.sh";

	public Psxyz(String asciiDataURL){	
		super(asciiDataURL, "xyzData.txt", true, false, "3Dbarchart.ps");
	}
	
	public String transform(		
			String B,
			String J,
			String JZ,
			String R,
			String E,
			String S,
			String W,
			String G)
	{
		
		String command = 
			PLOTTER_2D + " "
			+ inputPath + " "
			+ outputPath + " "
			+ B + " "
			+ J + " "
			+ JZ + " "
			+ R + " "
			+ E + " "
			+ S + " "
			+ W + " "
			+ G;
		
		CommandRunner.run(command);
		return outputURL;
	}
}