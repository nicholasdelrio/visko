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

import edu.utep.trustlab.visko.knowledge.universal.ServiceWSDL;
import edu.utep.trustlab.visko.ontology.service.writer.ServiceWriter;

public class VTKServices {
	private static final String wsdlURL = ServiceWSDL.WSDL_URL;
	
	public static String int2short;
	public static String float2shortThr;
	public static String vtkImageDataReader;
	public static String vtkContourFilter;
	public static String vtkPolyDataMapper;
	public static String vtkVolume;
	public static String vtkImageDataReaderFloat;
	
	public static void create() {
		String operationName;
		operationName = "int2Short";
		ServiceWriter wtr = new ServiceWriter(operationName + "Service");
		wtr.setWSDLURL(wsdlURL);
		wtr.setLabel(operationName);
		wtr.setOperationName(operationName);
		wtr.setConceptualOperator(VTKTransformers.int2ShortInt);
		wtr.setSupportingToolkit(VTKToolkits.vtk);
		System.out.println(wtr.saveDocument());
		int2short = wtr.getURI();

		operationName = "float2ShortThr";
		ServiceWriter wtr1 = new ServiceWriter(operationName + "Service");
		wtr1.setWSDLURL(wsdlURL);
		wtr1.setLabel(operationName);
		wtr1.setOperationName(operationName);
		wtr1.setConceptualOperator(VTKTransformers.floatArray2IntArray);
		wtr1.setSupportingToolkit(VTKToolkits.vtk);
		System.out.println(wtr1.saveDocument());
		float2shortThr = wtr1.getURI();
		
		operationName = "vtkImageDataReader";
		ServiceWriter wtr2 = new ServiceWriter(operationName + "Service");
		wtr2.setWSDLURL(wsdlURL);
		wtr2.setLabel(operationName);
		wtr2.setOperationName(operationName);
		wtr2.setConceptualOperator(VTKTransformers.shortIntArray2Gridded);
		wtr2.setSupportingToolkit(VTKToolkits.vtk);
		System.out.println(wtr2.saveDocument());
		vtkImageDataReader = wtr2.getURI();
		
		operationName = "vtkContourFilter";
		ServiceWriter wtr3 = new ServiceWriter(operationName);
		wtr3.setWSDLURL(wsdlURL);
		wtr3.setLabel(operationName);
		wtr3.setOperationName(operationName);
		wtr3.setConceptualOperator(VTKTransformers.contourer3D);
		wtr3.setSupportingToolkit(VTKToolkits.vtk);
		System.out.println(wtr3.saveDocument());
		vtkContourFilter = wtr3.getURI();
		
		operationName = "vtkPolyDataMapper";
		ServiceWriter wtr4 = new ServiceWriter(operationName);
		wtr4.setWSDLURL(wsdlURL);
		wtr4.setLabel(operationName);
		wtr4.setOperationName(operationName);
		wtr4.setConceptualOperator(VTKTransformers.contoursPoly2Image);
		wtr4.setSupportingToolkit(VTKToolkits.vtk);
		System.out.println(wtr4.saveDocument());
		vtkPolyDataMapper = wtr4.getURI();
		
		operationName = "vtkVolume";
		ServiceWriter wtr5 = new ServiceWriter(operationName);
		wtr5.setWSDLURL(wsdlURL);
		wtr5.setLabel(operationName);
		wtr5.setOperationName(operationName);
		wtr5.setConceptualOperator(VTKTransformers.volumeGenerator);
		wtr5.setSupportingToolkit(VTKToolkits.vtk);
		System.out.println(wtr5.saveDocument());
		vtkVolume = wtr5.getURI();
		
		operationName = "vtkImageDataReaderFloat";
		ServiceWriter wtr6 = new ServiceWriter(operationName);
		wtr6.setWSDLURL(wsdlURL);
		wtr6.setLabel(operationName);
		wtr6.setOperationName(operationName);
		wtr6.setConceptualOperator(VTKTransformers.floatArray2Gridded);
		wtr6.setSupportingToolkit(VTKToolkits.vtk);
		System.out.println(wtr6.saveDocument());
		vtkImageDataReaderFloat = wtr6.getURI();
	}
}