package org.openvisko.module;

import org.openvisko.module.operators.ToolkitOperator;
import org.openvisko.module.util.FileUtils;
import org.openvisko.module.util.ServerProperties;

import gravityMapScenario.gravityDataset.Dataset;

public class XYZDataFieldFilter extends ToolkitOperator
{
	public XYZDataFieldFilter(String asciiDataURL){	
		super(asciiDataURL, "gravityData.txt", true, true, "xyz.txt");
	}

	public String transform(
			String indexOfX,
			String indexOfY,
			String indexOfZ)
	{
		
		Dataset ds = new Dataset(stringData.trim(), true);
		
		int xIndex = Integer.valueOf(indexOfX);
		int yIndex = Integer.valueOf(indexOfY);
		int zIndex = Integer.valueOf(indexOfZ);
		
		int[] fieldsOfInterest;
		if(zIndex == -1)
			fieldsOfInterest = new int[] {xIndex, yIndex};
		else
			fieldsOfInterest = new int[] {xIndex, yIndex, zIndex};
		
		ds.disableHeader();
		String asciiTrimmed = ds.backToAscii(fieldsOfInterest);
		FileUtils.writeTextFile(asciiTrimmed, ServerProperties.getInstance().getOutputDir().getAbsolutePath(), outputFileName);
		
		return outputURL;
	}
}
