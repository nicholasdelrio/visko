package org.openvisko.module.operators;

import org.openvisko.module.util.FileUtils;
import org.openvisko.module.util.GetURLContents;
import org.openvisko.module.util.ServerProperties;

public abstract class ToolkitOperator {
	
	protected String stringData;
	protected byte[] binaryData;
	
	protected String inputPath;
	protected String inputFileName;
	
	protected String outputFileName;
	protected String outputPath;
	protected String outputURL;
	
	protected String inputDatasetURL;

	//constructor used when you do want the module to download the input data specified by the url
	public ToolkitOperator(String datasetURL, String baseInputFileName, boolean textualData, boolean persistInputDataInMemory, String baseOutputFileName){
		inputDatasetURL = datasetURL;
		setUpInputs(datasetURL, baseInputFileName, textualData, persistInputDataInMemory);
		setUpOutputs(baseOutputFileName);
	}
	
	//constructor used when you don't want the module to download the input data specified by the url
	public ToolkitOperator(String datasetURL, String baseOutputFileName){
		inputDatasetURL = datasetURL;
		setUpOutputs(baseOutputFileName);
	}
		
	protected void setUpInputs(String datasetURL, String baseInputFileName, boolean textualData, boolean persistInputDataInMemory){
		if(datasetURL != null){
			inputFileName = FileUtils.getRandomFileNameFromFileName(baseInputFileName);
			//inputPath = FileUtils.getOutputDir().getAbsolutePath() + File.separator + inputFileName;
			
			if(textualData){
				stringData = GetURLContents.downloadText(datasetURL);
				
				if(!persistInputDataInMemory)
					inputPath = FileUtils.writeTextFile(stringData, ServerProperties.getInstance().getOutputDir().getAbsolutePath(), inputFileName);
			}
			else {
				binaryData = GetURLContents.downloadFile(datasetURL);
				
				if(!persistInputDataInMemory)
					inputPath = FileUtils.writeBinaryFile(binaryData, ServerProperties.getInstance().getOutputDir().getAbsolutePath(), inputFileName);
			}
			
			System.out.println("saved data locally at: " + inputPath);
		}
	}
	
	protected void setUpOutputs(String baseOutputFileName){		
		outputFileName = FileUtils.getRandomFileNameFromFileName(baseOutputFileName);
		outputPath = FileUtils.makeFullPath(ServerProperties.getInstance().getOutputDir().getAbsolutePath(), outputFileName);
		outputURL = ServerProperties.getInstance().getOutputURLPrefix().toString() + outputFileName;
		
		System.out.println("output url will be: " + outputURL);
	}
}