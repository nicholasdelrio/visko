/*
Copyright (c) 2012, University of Texas at El Paso
All rights reserved.
Redistribution and use in source and binary forms, with or without modification, are permitted
provided that the following conditions are met:

	-Redistributions of source code must retain the above copyright notice, this list of conditions
	 and the following disclaimer.
	-Redistributions in binary form must reproduce the above copyright notice, this list of conditions
	 and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.*/


package edu.utep.trustlab.visko.knowledge.example;

import edu.utep.trustlab.contentManagement.AlfrescoClient;
import edu.utep.trustlab.contentManagement.ContentManager;
import edu.utep.trustlab.visko.ontology.operator.writer.TransformerWriter;
import edu.utep.trustlab.visko.ontology.service.writer.ServiceWriter;
import edu.utep.trustlab.visko.ontology.service.writer.ToolkitProfileWriter;
import edu.utep.trustlab.visko.ontology.service.writer.ToolkitWriter;

public class SomeOperator {
	
	private String toolkitURI;
	private String operatorURI;
	private String serviceURI;
	
	public void create() {

        String serverURL = "http://localhost:8080/alfresco";
        String projectName = "visko";
        String userName = "admin";
        String password = "password";
        String webdavURL = "http://domain.com/Projects/visko/";

        AlfrescoClient client = new AlfrescoClient(serverURL, userName, password);
        client.setProjectName(projectName);

		ContentManager.setContentManager(client);
		ContentManager.setWorkspacePath("");
        
        if(!webdavURL.equals("${cat-webdav-url}"))
            client.setWebDAVURL(webdavURL);
        
        //create toolkit
        createToolkit();
        
        //create transformer or mapper
        createTransformer();
        createMapper();
        
        //create service
        createService();
        
        //create visualization
        createVisualizationProfile();
	}
	
	public static void main(String[] args){
		SomeOperator populator = new SomeOperator();
		populator.create();
	}
		
	private void createToolkit(){
		String name = "gmt";
		String label = "Generic Mapping Tools";
		
		ToolkitWriter wtr = new ToolkitWriter(name);
		wtr.setLabel(label);
		wtr.saveDocument();
		toolkitURI = wtr.getURI();
	}
	
	private void createTransformer(){
		String name = "gridder";
		String label = "Data Gridder";
		String inputFormatURI = "https://raw.github.com/nicholasdelrio/visko/master/rdf/formats/SPACEDELIMITEDTABULARASCII.owl#SPACEDELIMITEDTABULARASCII";
		String outputFormatURI = "https://raw.github.com/nicholasdelrio/visko/master/rdf/formats/NETCDFGMT.owl#NETCDFGMT";
		
		TransformerWriter wtr = new TransformerWriter(name, false);
		wtr.addInputFormat(inputFormatURI);
		wtr.setOutputFormat(outputFormatURI);
		wtr.setLabel(label);
		wtr.setName(name);
		wtr.saveDocument();
		operatorURI = wtr.getURI();
	}
	
	private void createMapper(){
		String name = "contourer";
		String label = "Contour Line Generator";
		String inputFormatURI = "https://raw.github.com/nicholasdelrio/visko/master/rdf/formats/SPACEDELIMITEDTABULARASCII.owl#SPACEDELIMITEDTABULARASCII";
		String outputFormatURI = "https://raw.github.com/nicholasdelrio/visko/master/rdf/formats/NETCDFGMT.owl#NETCDFGMT";
		String viewURI = "https://raw.github.com/nicholasdelrio/visko/master/rdf/contour-lines.owl#contour-lines";
		
		TransformerWriter wtr = new TransformerWriter(name, true);
		wtr.addInputFormat(inputFormatURI);
		wtr.setOutputFormat(outputFormatURI);
		name = "contour";
		wtr.setLabel(label);
		wtr.setName(name);
		wtr.setMappedToView(viewURI);
		wtr.saveDocument();
		operatorURI = wtr.getURI();
	}
	
	private void createService(){
		String wsdlURL = "";
		String operationName = "grdcontour";
		String label = "Contouring Service";
		
		ServiceWriter wtr = new ServiceWriter(operationName);
		wtr.setLabel(label);
		wtr.setOperationName(operationName);
		wtr.setWSDLURL(wsdlURL);
		wtr.setConceptualOperator(operatorURI);
		wtr.setSupportingToolkit(toolkitURI);
		wtr.saveDocument();
		serviceURI = wtr.getURI();
	}
	
	private void createVisualizationProfile(){
		String dataTypeURI = "http://rio.cs.utep.edu/ciserver/ciprojects/CrustalModeling/CrustalModeling.owl#d19";
		String serviceURL = serviceURI.substring(0, serviceURI.indexOf("#"));
	
		ToolkitProfileWriter wtr = new ToolkitProfileWriter("gravityDataProfile");
		wtr.addDataType(dataTypeURI);
		
		wtr.addInputBinding(serviceURL + "#S", "c0.04c");
		wtr.addInputBinding(serviceURL + "#J", "x4c");
		wtr.addInputBinding(serviceURL + "#G", "blue");
	}
}