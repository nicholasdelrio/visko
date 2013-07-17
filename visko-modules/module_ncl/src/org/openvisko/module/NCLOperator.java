package org.openvisko.module;

import org.openvisko.module.operators.ToolkitOperator;
import org.openvisko.module.util.FileUtils;
import org.openvisko.module.util.ServerProperties;


public abstract class NCLOperator extends ToolkitOperator
{
	public NCLOperator(String inputDataURL, String baseInputFileName, boolean isTextualInput, boolean shouldBePersistedInMemory, String baseOutputFileName){
		super(inputDataURL, baseInputFileName, isTextualInput, shouldBePersistedInMemory, baseOutputFileName);
	}

	protected void setUpOutputs(String baseOutputFileName){
		String[] fileNameParts = baseOutputFileName.split("\\.");
		String name = fileNameParts[0];
		String extension = fileNameParts[1];
		
		outputFileName = name + "_" + FileUtils.getRandomString();
		outputPath = FileUtils.makeFullPath(ServerProperties.getInstance().getOutputDir().getAbsolutePath(), outputFileName);
		outputURL = ServerProperties.getInstance().getOutputURLPrefix() + outputFileName + "." + extension;
	}
}
