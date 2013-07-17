package org.openvisko.module;

import org.openvisko.module.operators.ToolkitOperator;
import org.openvisko.module.util.FileUtils;
import org.openvisko.module.util.CommandRunner;
import org.openvisko.module.util.ServerProperties;

import gravityMapScenario.gravityDataset.Dataset;

public class Psxy extends ToolkitOperator{
	
	/* ASUMPTION: the input dataset is in tabular space delimited ASCII file */
	
	private static final String PLOTTER_2D = ServerProperties.getInstance().getScriptsDir().getAbsolutePath() + "/" + "wrapper-psxy.sh";

	public Psxy(String asciiDataURL){	
		super(asciiDataURL, "xyData.txt", true, true, "2DPlot.ps");
	}
	
	public String transform(
			String S,
			String J,
			String G,
			String B,
			String R,
			String indexOfX,
			String indexOfY)
	{
		Dataset ds = new Dataset(stringData.trim(), true);
		int[] fieldsOfInterest = new int[] {Integer.valueOf(indexOfX), Integer.valueOf(indexOfY)};
		ds.disableHeader();
		String asciiTrimmed = ds.backToAscii(fieldsOfInterest);
		String asciiDataPath = FileUtils.writeTextFile(asciiTrimmed, ServerProperties.getInstance().getOutputDir().getAbsolutePath(), inputFileName);
		
		String command = 
			PLOTTER_2D + " "
			+ asciiDataPath + " "
			+ outputPath + " "
			+ R + " "
			+ S + " "
			+ J + " "
			+ G + " "
			+ B;
		
		CommandRunner.run(command);
		
		return outputURL;
	}
}