package org.openvisko.module.operators;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.openvisko.module.util.FileUtils;
import org.openvisko.module.util.ServerProperties;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public abstract class SimulationOperator {

	protected HashMap<String,String> datasetPaths;
		
	protected String outputFileName;
	protected String outputPath;
	protected String outputURL;
	
	public SimulationOperator(String contextURL, String outputFileName){
		datasetPaths = new HashMap<String,String>();
		
		if(contextURL != null)
			extractInputDatasetPaths(contextURL);
		
		setUpOutputs(outputFileName);
	}
		
	protected void extractInputDatasetPaths(String contextURL){
		String inputFileName = contextURL.substring(contextURL.lastIndexOf("/") + 1);
		String inputPath;
		System.out.println(inputFileName);
		
		if(ServerProperties.getInstance().existsOnLocalFileSystem(contextURL)){
			inputPath = ServerProperties.getInstance().getOutputDir().getAbsolutePath() + inputFileName;			
			setInputDatasets(inputPath);	
		}
		else{
			setInputDatasets(contextURL);
		}
	}
	
	private void setInputDatasets(String contextXMLPath){
		try{
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(contextXMLPath);
			
			doc.getDocumentElement().normalize();
			
			NodeList fileElements = doc.getElementsByTagName("File");
			
			for (int i = 0; i < fileElements.getLength(); i ++) {
				Node fileElement = fileElements.item(i);
				   
				if (fileElement.getNodeType() == Node.ELEMENT_NODE) {
		 
				      Element file = (Element) fileElement;
				      String fileType = file.getAttribute("fileType");
				      String filePath = file.getAttribute("filePath");
				      
				      datasetPaths.put(fileType,  filePath);
				   }
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	protected void addOutputFile(String fileType, String filePath){
		datasetPaths.put(fileType, filePath);
	}
	
	protected String getInputFile(String fileType){
		return datasetPaths.get(fileType);
	}
	
	protected void setUpOutputs(String outputFileName){
		this.outputFileName = "outputFile_" + FileUtils.getRandomString() + "_" + outputFileName;
		outputPath = FileUtils.makeFullPath(ServerProperties.getInstance().getOutputDir().getAbsolutePath(), outputFileName);
		outputURL = ServerProperties.getInstance().getOutputURLPrefix().toString() + this.outputFileName;
	}
	
	protected String writeOutputContextXML(){

		Set<String> keys = datasetPaths.keySet();
		Iterator<String> fileTypes = keys.iterator();
		String fileType;
		String filePath;

		String xmlOutput = "<Files>\n";

		while(fileTypes.hasNext()){
			fileType = fileTypes.next();
			filePath = datasetPaths.get(fileType);
			
			xmlOutput += "\t<File fileType=\"" + fileType + "\" filePath=\"" + filePath + "\" />\n";
		}
		
		xmlOutput += "</Files>";
		
		FileUtils.writeTextFile(xmlOutput, ServerProperties.getInstance().getOutputDir().toString(), outputFileName);
		return outputURL;
	}
}