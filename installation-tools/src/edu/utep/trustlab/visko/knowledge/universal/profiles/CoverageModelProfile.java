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


package edu.utep.trustlab.visko.knowledge.universal.profiles;

import edu.utep.trustlab.visko.knowledge.vtk.VTKServices;
import edu.utep.trustlab.visko.knowledge.vtk.VTKToolkits;
import edu.utep.trustlab.visko.ontology.service.writer.ToolkitProfileWriter;

public class CoverageModelProfile {
	public static void create() {
		String documentURL;

		/************ profile for velocity ******************************/
		String dataTypeURI = "http://rio.cs.utep.edu/ciserver/ciprojects/HolesCode/HolesCodeSAW3.owl#d7-0";

		ToolkitProfileWriter wtr = new ToolkitProfileWriter("coverageModelProfile");
		wtr.addDataType(dataTypeURI);

		// for vtkImageReader
		wtr.addInputBinding(VTKServices.vtkImageDataReader + "#littleEndian","true");
		wtr.addInputBinding(VTKServices.vtkImageDataReader + "#dim", "3");
		wtr.addInputBinding(VTKServices.vtkImageDataReader + "#dataOrigin", "0/0/0");
		wtr.addInputBinding(VTKServices.vtkImageDataReader + "#dataSpacing", "1/1/1");
		wtr.addInputBinding(VTKServices.vtkImageDataReader + "#dataExtent", "0/229/0/24/0/67");
		wtr.addInputBinding(VTKServices.vtkImageDataReader + "#numScalarComponents", "1");
		wtr.addInputBinding(VTKServices.vtkImageDataReader + "#readLowerLeft","true");

		// for vtkContourFilter
		wtr.addInputBinding(VTKServices.vtkContourFilter + "#numContours", "35");
		wtr.addInputBinding(VTKServices.vtkContourFilter + "#scalarRange", "0.0/9000.0");

		// for vtkPolyDataMapper
		wtr.addInputBinding(VTKServices.vtkPolyDataMapper + "#scalarRange","0.0/9000.0");
		wtr.addInputBinding(VTKServices.vtkPolyDataMapper + "#xRotation", "105");
		wtr.addInputBinding(VTKServices.vtkPolyDataMapper + "#yRotation", "0");
		wtr.addInputBinding(VTKServices.vtkPolyDataMapper + "#zRotation", "0");
		wtr.addInputBinding(VTKServices.vtkPolyDataMapper + "#size", "400/300");
		wtr.addInputBinding(VTKServices.vtkPolyDataMapper + "#backgroundColor", "1/1/1");
		wtr.addInputBinding(VTKServices.vtkPolyDataMapper + "#magnification", "3");

		// for vtkVolume
		wtr.addInputBinding(VTKServices.vtkVolume + "#xRotation", "105");
		wtr.addInputBinding(VTKServices.vtkVolume + "#yRotation", "0");
		wtr.addInputBinding(VTKServices.vtkVolume + "#zRotation", "0");
		wtr.addInputBinding(VTKServices.vtkVolume + "#size", "400/300");
		wtr.addInputBinding(VTKServices.vtkVolume + "#backgroundColor", "0/0/0");
		wtr.addInputBinding(VTKServices.vtkVolume + "#magnification", "3");
		wtr.addInputBinding(VTKServices.vtkVolume + "#colorFunction", "20,1.0,0.0,0.3/80,1.0,0.0,0.3");
		wtr.addInputBinding(VTKServices.vtkVolume + "#opacityFunction", "0,0.0/40,1.0");

		wtr.setSupportingToolkit(VTKToolkits.vtk);
		
		documentURL = wtr.saveDocument();
		System.out.println(documentURL);
		}
}
