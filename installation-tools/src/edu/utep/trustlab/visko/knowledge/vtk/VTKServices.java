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


package edu.utep.trustlab.visko.knowledge.vtk;

import edu.utep.trustlab.contentManagement.Repository;
import edu.utep.trustlab.visko.ontology.service.writer.ServiceWriter;

public class VTKServices {
	private static final String wsdlURL = "http://iw.cs.utep.edu:8080/VTK-services/services/VTKHoleTransformersUsingParameters.VTKHoleTransformersUsingParametersPort?wsdl";

	public static void create() {
		String operationName;
		String baseURL = Repository.getRepository().getBaseURL();
		operationName = "Int2Short";
		ServiceWriter wtr = new ServiceWriter(operationName + "Service");
		wtr.setWSDLURL(wsdlURL);
		wtr.setLabel(operationName);
		wtr.setOperationName(operationName);
		wtr.setConceptualOperator(baseURL + "intToShortIntOperator.owl#intToShortIntOperator");
		wtr.setSupportingToolkit(baseURL + "vtk.owl#vtk");
		wtr.saveDocument();

		operationName = "Float2ShortThr";
		ServiceWriter wtr0 = new ServiceWriter(operationName + "Service");
		wtr0.setWSDLURL(wsdlURL);
		wtr0.setLabel(operationName);
		wtr0.setOperationName(operationName);
		wtr0.setConceptualOperator(baseURL + "floatArrayToShortIntArray.owl#floatArrayToShortIntArray");
		wtr0.setSupportingToolkit(baseURL + "vtk.owl#vtk");
		wtr0.saveDocument();
		
		operationName = "vtkImageDataReader";
		ServiceWriter wtr1 = new ServiceWriter(operationName + "Service");
		wtr1.setWSDLURL(wsdlURL);
		wtr1.setLabel(operationName);
		wtr1.setOperationName(operationName);
		wtr1.setConceptualOperator(baseURL + "shortIntArrayToGriddedData.owl#shortIntArrayToGriddedData");
		wtr1.setSupportingToolkit(baseURL + "vtk.owl#vtk");
		wtr1.saveDocument();
		
		operationName = "vtkContourFilter";
		ServiceWriter wtr3 = new ServiceWriter(operationName + "Service");
		wtr3.setWSDLURL(wsdlURL);
		wtr3.setLabel(operationName);
		wtr3.setOperationName(operationName);
		wtr3.setConceptualOperator(baseURL + "contourer3D.owl#contourer3D");
		wtr3.setSupportingToolkit(baseURL + "vtk.owl#vtk");
		wtr3.saveDocument();
		
		operationName = "vtkPolyDataMapper";
		ServiceWriter wtr5 = new ServiceWriter(operationName + "Service");
		wtr5.setWSDLURL(wsdlURL);
		wtr5.setLabel(operationName);
		wtr5.setOperationName(operationName);
		wtr5.setConceptualOperator(baseURL + "contoursPolyDataToImageOperator.owl#contoursPolyDataToImageOperator");
		wtr5.setSupportingToolkit(baseURL + "vtk.owl#vtk");
		wtr5.saveDocument();
		
		operationName = "vtkVolume";
		ServiceWriter wtr6 = new ServiceWriter(operationName + "Service");
		wtr6.setWSDLURL(wsdlURL);
		wtr6.setLabel(operationName);
		wtr6.setOperationName(operationName);
		wtr6.setConceptualOperator(baseURL + "volume-generator.owl#volume-generator");
		wtr6.setSupportingToolkit(baseURL + "vtk.owl#vtk");
		wtr6.saveDocument();
		
		operationName = "vtkImageDataReaderFloat";
		ServiceWriter wtr7 = new ServiceWriter(operationName + "Service");
		wtr7.setWSDLURL(wsdlURL);
		wtr7.setLabel(operationName);
		wtr7.setOperationName(operationName);
		wtr7.setConceptualOperator(baseURL + "floatArrayToGriddedData.owl#floatArrayToGriddedData");
		wtr7.setSupportingToolkit(baseURL + "vtk.owl#vtk");
		wtr7.saveDocument();
	}
}
