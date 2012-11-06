package edu.utep.trustlab.visko.installation.packages.service.operators.types;

import edu.utep.trustlab.visko.installation.packages.service.operators.util.FileUtils;


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
		outputPath = FileUtils.makeFullPath(FileUtils.getWorkspace(), outputFileName);
		outputURL = FileUtils.getOutputURLPrefix() + outputFileName + "." + extension;
	}
}
