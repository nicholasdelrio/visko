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
import edu.utep.trustlab.visko.ontology.service.writer.ToolkitProfileWriter;

public class CIIXRAYDataProfile {
	public static void create() {
		String documentURL;

		/************ profile for CII XRAY Data ******************************/
		String dataTypeURI = "http://pnl.gov/cii/xray/data";
	
		ToolkitProfileWriter wtr = new ToolkitProfileWriter("velocityModelProfile");
		wtr.addDataType(dataTypeURI);

		String vtkTIFFReader = VTKServices.vtkTIFFReader.substring(0, VTKServices.vtkTIFFReader.indexOf("#"));
		String vtkPolyDataMapper = VTKServices.vtkPolyDataMapper.substring(0, VTKServices.vtkPolyDataMapper.indexOf("#"));
		String vtkContourFilter = VTKServices.vtkContourFilter.substring(0, VTKServices.vtkContourFilter.indexOf("#"));
		String vtkVolume = VTKServices.vtkVolume.substring(0, VTKServices.vtkVolume.indexOf("#"));

		// for vtkImageReader
		wtr.addInputBinding(vtkTIFFReader + "#littleEndian", "true");

		// for vtkContourFilter
		wtr.addInputBinding(vtkContourFilter + "#numContours", "35");
		wtr.addInputBinding(vtkContourFilter + "#scalarRange", "0.0/9000.0");

		// for vtkPolyDataMapper
		wtr.addInputBinding(vtkPolyDataMapper + "#scalarRange", "0.0/9000.0");
		wtr.addInputBinding(vtkPolyDataMapper + "#xRotation", "105");
		wtr.addInputBinding(vtkPolyDataMapper + "#yRotation", "0");
		wtr.addInputBinding(vtkPolyDataMapper + "#zRotation", "0");
		wtr.addInputBinding(vtkPolyDataMapper + "#size","400/300");
		wtr.addInputBinding(vtkPolyDataMapper + "#backgroundColor", "1/1/1");
		wtr.addInputBinding(vtkPolyDataMapper + "#magnification", "3");

		// for vtkVolume
		wtr.addInputBinding(vtkVolume + "#xRotation", "105");
		wtr.addInputBinding(vtkVolume + "#yRotation", "0");
		wtr.addInputBinding(vtkVolume + "#zRotation", "0");
		wtr.addInputBinding(vtkVolume + "#size", "400/300");
		wtr.addInputBinding(vtkVolume + "#backgroundColor", "1/1/1");
		wtr.addInputBinding(vtkVolume + "#magnification", "3");
		wtr.addInputBinding(vtkVolume + "#colorFunction", "3000,1,1,0/5000,0.5,0.95,0/5600,0,0,1/6500,0.28,0.2,0.5/7000,1,0,0");
		wtr.addInputBinding(vtkVolume + "#opacityFunction", "4000,0.2/8000,0.5");

		documentURL = wtr.saveDocument();
		System.out.println(documentURL);
	}
}
