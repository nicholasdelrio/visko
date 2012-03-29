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

import edu.utep.trustlab.contentManagement.Repository;
import edu.utep.trustlab.visko.ontology.service.writer.ServiceWriter;

public class GMTServices {
	private static final String wsdlURL = "http://iw.cs.utep.edu:8080/GMT-services/services/GMTGravityTransformersUsingParameters.GMTGravityTransformersUsingParametersPort?wsdl";

	public static void create() {
		String operationName = "ESRIGriddedToContourMapPS";
		String baseURL = Repository.getRepository().getBaseURL();
		ServiceWriter wtr = new ServiceWriter(operationName);
		wtr.setLabel(operationName);
		wtr.setOperationName(operationName);
		wtr.setWSDLURL(wsdlURL);
		wtr.setConceptualOperator(baseURL + "contourer.owl#contourer");
		wtr.setSupportingToolkit(baseURL + "gmt.owl#gmt");
		wtr.saveDocument();

		operationName = "GravityASCIIPointsToMinCurvatureESRIGridded";
		ServiceWriter wtr1 = new ServiceWriter(operationName);
		wtr1.setWSDLURL(wsdlURL);
		wtr1.setLabel(operationName);
		wtr1.setOperationName(operationName);
		wtr1.setConceptualOperator(baseURL + "gridder.owl#gridder");
		wtr1.setSupportingToolkit(baseURL + "gmt.owl#gmt");
		wtr1.saveDocument();

		operationName = "GravityASCIIPointsToNearNeightborESRIGridded";
		ServiceWriter wtr4 = new ServiceWriter(operationName);
		wtr4.setWSDLURL(wsdlURL);
		wtr4.setLabel(operationName);
		wtr4.setOperationName(operationName);
		wtr4.setConceptualOperator(baseURL + "gridder.owl#gridder");
		wtr4.setSupportingToolkit(baseURL + "gmt.owl#gmt");
		wtr4.saveDocument();

		operationName = "GravityASCIIPointsTo2DPlotPS";
		ServiceWriter wtr2 = new ServiceWriter(operationName);
		wtr2.setOperationName(operationName);
		wtr2.setWSDLURL(wsdlURL);
		wtr2.setOperationName(operationName);
		wtr2.setLabel(operationName);
		wtr2.setConceptualOperator(baseURL + "plotter.owl#plotter");
		wtr2.setSupportingToolkit(baseURL + "gmt.owl#gmt");
		wtr2.saveDocument();

		operationName = "ESRIGriddedToColoredImagePS";
		ServiceWriter wtr3 = new ServiceWriter(operationName);
		wtr3.setOperationName(operationName);
		wtr3.setWSDLURL(wsdlURL);
		wtr3.setLabel(operationName);
		wtr3.setConceptualOperator(baseURL + "rasterer.owl#rasterer");
		wtr3.setSupportingToolkit(baseURL + "gmt.owl#gmt");
		wtr3.saveDocument();

		operationName = "CSVToTabularASCII";
		ServiceWriter wtr5 = new ServiceWriter(operationName);
		wtr5.setOperationName(operationName);
		wtr5.setWSDLURL(wsdlURL);
		wtr5.setLabel(operationName);
		wtr5.setConceptualOperator(baseURL + "csv-to-tabular-ascii.owl#csv-to-tabular-ascii");
		wtr5.setSupportingToolkit(baseURL + "gmt.owl#gmt");
		wtr5.saveDocument();
	}
}
