package org.openvisko.module.operators;

import org.openvisko.module.util.FileUtils;
import org.openvisko.module.util.GetURLContents;

public abstract class ToolkitOperator {
	
	protected String stringData;
	protected byte[] binaryData;
	
	protected String inputPath;
	protected String inputFileName;
	
	protected String outputFileName;
	protected String outputPath;
	protected String outputURL;
	
	protected String inputDatasetURL;
	
	public ToolkitOperator(String datasetURL, String baseInputFileName, boolean textualData, boolean persistInputDataInMemory, String baseOutputFileName){
		inputDatasetURL = datasetURL;
		setUpInputs(datasetURL, baseInputFileName, textualData, persistInputDataInMemory);
		setUpOutputs(baseOutputFileName);
	}
		
	protected void setUpInputs(String datasetURL, String baseInputFileName, boolean textualData, boolean persistInputDataInMemory){
		if(datasetURL != null){
			inputFileName = FileUtils.getInstance().getRandomFileNameFromFileName(baseInputFileName);
			//inputPath = FileUtils.getInstance().getOutputDir().getAbsolutePath() + File.separator + inputFileName;
			
			if(textualData){
				stringData = GetURLContents.downloadText(datasetURL);
				
				if(!persistInputDataInMemory)
					inputPath = FileUtils.getInstance().writeTextFile(stringData, FileUtils.getInstance().getOutputDir().getAbsolutePath(), inputFileName);
			}
			else {
				binaryData = GetURLContents.downloadFile(datasetURL);
				
				if(!persistInputDataInMemory)
					inputPath = FileUtils.getInstance().writeBinaryFile(binaryData, FileUtils.getInstance().getOutputDir().getAbsolutePath(), inputFileName);
			}
			
			System.out.println("saved data locally at: " + inputPath);
		}
	}
	
	protected void setUpOutputs(String baseOutputFileName){		
		outputFileName = FileUtils.getInstance().getRandomFileNameFromFileName(baseOutputFileName);
		outputPath = FileUtils.getInstance().makeFullPath(FileUtils.getInstance().getOutputDir().getAbsolutePath(), outputFileName);
		outputURL = FileUtils.getInstance().getOutputURLPrefix().toString() + outputFileName;
		
		System.out.println("output url will be: " + outputURL);
	}
}