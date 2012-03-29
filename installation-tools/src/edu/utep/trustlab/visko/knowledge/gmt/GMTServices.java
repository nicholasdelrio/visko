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


package edu.utep.trustlab.visko.knowledge.gmt;

import edu.utep.trustlab.visko.ontology.service.writer.ServiceWriter;

public class GMTServices {
	private static final String wsdlURL = "http://iw.cs.utep.edu:8080/GMT-services/services/GMTGravityTransformersUsingParameters.GMTGravityTransformersUsingParametersPort?wsdl";
	public static String gridded2ContourMap;
	public static String points2MinCurvatureGridded;
	public static String points2NearNeighborGridded;
	public static String points22DPlot;
	public static String gridded2Raster;
	public static String csv2Tabular;
	
	public static void create() {
		
		String operationName = "ESRIGriddedToContourMapPS";
		ServiceWriter wtr = new ServiceWriter(operationName);
		wtr.setLabel(operationName);
		wtr.setOperationName(operationName);
		wtr.setWSDLURL(wsdlURL);
		wtr.setConceptualOperator(GMTTransformers.contourer);
		wtr.setSupportingToolkit(GMTToolkits.gmt);
		System.out.println(wtr.saveDocument());
		gridded2ContourMap = wtr.getURI();
		
		operationName = "GravityASCIIPointsToMinCurvatureESRIGridded";
		ServiceWriter wtr1 = new ServiceWriter(operationName);
		wtr1.setWSDLURL(wsdlURL);
		wtr1.setLabel(operationName);
		wtr1.setOperationName(operationName);
		wtr1.setConceptualOperator(GMTTransformers.gridder);
		wtr1.setSupportingToolkit(GMTToolkits.gmt);
		System.out.println(wtr1.saveDocument());
		points2MinCurvatureGridded = wtr1.getURI();
		
		operationName = "GravityASCIIPointsToNearNeightborESRIGridded";
		ServiceWriter wtr2 = new ServiceWriter(operationName);
		wtr2.setWSDLURL(wsdlURL);
		wtr2.setLabel(operationName);
		wtr2.setOperationName(operationName);
		wtr2.setConceptualOperator(GMTTransformers.gridder);
		wtr2.setSupportingToolkit(GMTToolkits.gmt);
		System.out.println(wtr2.saveDocument());
		points2NearNeighborGridded = wtr2.getURI();
		
		operationName = "GravityASCIIPointsTo2DPlotPS";
		ServiceWriter wtr3 = new ServiceWriter(operationName);
		wtr3.setOperationName(operationName);
		wtr3.setWSDLURL(wsdlURL);
		wtr3.setOperationName(operationName);
		wtr3.setLabel(operationName);
		wtr3.setConceptualOperator(GMTTransformers.plotter);
		wtr3.setSupportingToolkit(GMTToolkits.gmt);
		System.out.println(wtr3.saveDocument());
		points22DPlot = wtr3.getURI();
		
		operationName = "ESRIGriddedToColoredImagePS";
		ServiceWriter wtr4 = new ServiceWriter(operationName);
		wtr4.setOperationName(operationName);
		wtr4.setWSDLURL(wsdlURL);
		wtr4.setLabel(operationName);
		wtr4.setConceptualOperator(GMTTransformers.rasterer);
		wtr4.setSupportingToolkit(GMTToolkits.gmt);
		System.out.println(wtr4.saveDocument());
		gridded2Raster = wtr4.getURI();
		
		operationName = "CSVToTabularASCII";
		ServiceWriter wtr5 = new ServiceWriter(operationName);
		wtr5.setOperationName(operationName);
		wtr5.setWSDLURL(wsdlURL);
		wtr5.setLabel(operationName);
		wtr5.setConceptualOperator(GMTTransformers.csv2tabular);
		wtr5.setSupportingToolkit(GMTToolkits.gmt);
		System.out.println(wtr5.saveDocument());
		csv2Tabular = wtr5.getURI();
	}
}
