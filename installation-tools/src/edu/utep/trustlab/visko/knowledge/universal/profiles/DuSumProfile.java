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

public class DuSumProfile {
	public static void create() {

		
		/************ profile for dusum ******************************/
		String dataTypeURI = "http://rio.cs.utep.edu/ciserver/ciprojects/HolesCode/HolesCodeSAW3.owl#d8-0";
		String dataTypeURI1 = "http://rio.cs.utep.edu/ciserver/ciprojects/HolesCode/HolesCodeWDO.owl#d8";
		
		ToolkitProfileWriter wtr2 = new ToolkitProfileWriter(
		"slownessPertubationModelProfile");
		wtr2.addDataType(dataTypeURI);
		wtr2.addDataType(dataTypeURI1);

		String float2shortThr = VTKServices.float2shortThr.substring(0, VTKServices.float2shortThr.indexOf("#"));
		String vtkImageDataReader = VTKServices.vtkImageDataReader.substring(0, VTKServices.vtkImageDataReader.indexOf("#"));
		String vtkContourFilter = VTKServices.vtkContourFilter.substring(0, VTKServices.vtkContourFilter.indexOf("#"));
		String vtkPolyDataMapper = VTKServices.vtkPolyDataMapper.substring(0, VTKServices.vtkPolyDataMapper.indexOf("#"));
		String vtkVolume = VTKServices.vtkVolume.substring(0, VTKServices.vtkVolume.indexOf("#"));
		
		// for float2shortThr
		wtr2.addInputBinding(
		float2shortThr + "#scalingFactor",
		"1");
		wtr2.addInputBinding(
		float2shortThr + "#offset",
		"10000");

		// for vtkImageReader
		wtr2.addInputBinding(
		vtkImageDataReader + "#littleEndian",
		"true");
		wtr2.addInputBinding(
		vtkImageDataReader + "#dim",
		"3");
		wtr2.addInputBinding(
		vtkImageDataReader + "#dataOrigin",
		"0/0/0");
		wtr2.addInputBinding(
		vtkImageDataReader + "#dataSpacing",
		"1/1/1");
		wtr2.addInputBinding(
		vtkImageDataReader + "#dataExtent",
		"0/229/0/24/0/67");
		wtr2.addInputBinding(
		vtkImageDataReader + "#numScalarComponents",
		"1");
		wtr2.addInputBinding(
		vtkImageDataReader + "#readLowerLeft",
		"true");

		// for vtkContourFilter
		wtr2.addInputBinding(
		vtkContourFilter + "#numContours",
		"35");
		wtr2.addInputBinding(
		vtkContourFilter + "#scalarRange",
		"9997.0/10014.0");

		// for vtkPolyDataMapper
		wtr2.addInputBinding(
		vtkPolyDataMapper + "#scalarRange",
		"9997.0/10014.0");
		wtr2.addInputBinding(
		vtkPolyDataMapper + "#xRotation",
		"90");
		wtr2.addInputBinding(
		vtkPolyDataMapper + "#yRotation",
		"0");
		wtr2.addInputBinding(
		vtkPolyDataMapper + "#zRotation",
		"0");
		wtr2.addInputBinding(
		vtkPolyDataMapper + "#size",
		"400/300");
		wtr2.addInputBinding(
		vtkPolyDataMapper + "#backgroundColor",
		"1/1/1");
		wtr2.addInputBinding(
		vtkPolyDataMapper + "#magnification",
		"3");

		// for vtkVolume
		wtr2.addInputBinding(
		vtkVolume + "#xRotation",
		"105");
		wtr2.addInputBinding(
		vtkVolume + "#yRotation",
		"0");
		wtr2.addInputBinding(
		vtkVolume + "#zRotation",
		"0");
		wtr2.addInputBinding(
		vtkVolume + "#size",
		"400/300");
		wtr2.addInputBinding(
		vtkVolume + "#backgroundColor",
		"1/1/1");
		wtr2.addInputBinding(
		vtkVolume + "#magnification",
		"3");
		wtr2.addInputBinding(
		vtkVolume + "#colorFunction",
		"0.0,0.0,0.0,0.0/1000.0,1.0,0.0,0.0/3000.0,0.0,0.0,1.0/5000.0,0.0,1.0,0.0/7000.0,0.0,0.2,0.0");
		wtr2.addInputBinding(
		vtkVolume + "#opacityFunction",
		"20,0.0/255,0.2");

		String documentURL = wtr2.saveDocument();
		System.out.println(documentURL);

		}
}
