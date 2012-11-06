package edu.utep.trustlab.visko.installation.packages.service.operators.types;

import edu.utep.trustlab.visko.installation.packages.service.operators.util.FileUtils;
import edu.utep.trustlab.visko.installation.packages.service.operators.util.GetURLContents;

public abstract class ToolkitOperator {
	
	protected String stringData;
	protected byte[] binaryData;
	
	protected String inputPath;
	protected String inputFileName;
	
	protected String outputFileName;
	protected String outputPath;
	protected String outputURL;
	
	public ToolkitOperator(String datasetURL, String baseInputFileName, boolean textualData, boolean persistInputDataInMemory, String baseOutputFileName){
		setUpInputs(datasetURL, baseInputFileName, textualData, persistInputDataInMemory);
		setUpOutputs(baseOutputFileName);
	}
	
	private static boolean isStaticFileURL(String url){
		if(url.contains("?"))
			return false;
		return true;
	}

	protected void setUpInputs(String datasetURL, String baseInputFileName, boolean textualData, boolean persistInputDataInMemory){
		if(datasetURL != null){
			
			if(isStaticFileURL(datasetURL) && FileUtils.existsOnLocalFileSystem(datasetURL)){
				System.out.println("file exists on file system of server...don't need to http get!");
		
				inputFileName = datasetURL.substring(datasetURL.lastIndexOf("/") + 1);
				System.out.println("input file name on local system is: " + inputFileName);
				
				inputPath = FileUtils.getWorkspace() + inputFileName;
			
				if(persistInputDataInMemory && textualData)
					stringData = FileUtils.readTextFile(inputPath);
				else if(persistInputDataInMemory && !textualData)
					binaryData = FileUtils.readBinaryFile(inputPath);
			
			}
			else{
				System.out.println("file doesn't exist on file system of server...need to http get it");
	
				inputFileName = FileUtils.getRandomFileNameFromFileName(baseInputFileName);
				inputPath = FileUtils.getWorkspace() + inputFileName;
			
				if(textualData){
					stringData = GetURLContents.downloadText(datasetURL);
				
					if(!persistInputDataInMemory)
						FileUtils.writeTextFile(stringData, FileUtils.getWorkspace(), inputFileName);
				}
				else {
					binaryData = GetURLContents.downloadFile(datasetURL);
				
					if(!persistInputDataInMemory)
						FileUtils.writeBinaryFile(binaryData, FileUtils.getWorkspace(), inputFileName);
				}
			}
		}
	}
	
	protected void setUpOutputs(String baseOutputFileName){		
		outputFileName = FileUtils.getRandomFileNameFromFileName(baseOutputFileName);
		outputPath = FileUtils.makeFullPath(FileUtils.getWorkspace(), outputFileName);
		outputURL = FileUtils.getOutputURLPrefix() + outputFileName;
	}
}