package edu.utep.trustlab.visko.knowledge.universal.profiles;

import edu.utep.trustlab.visko.ontology.service.writer.ToolkitProfileWriter;

public class VelocityModelProfile {
	public static void create() {
		String documentURL;

		/************ profile for velocity ******************************/
		String dataTypeURI = "http://rio.cs.utep.edu/ciserver/ciprojects/HolesCode/HolesCodeSAW3.owl#d14-0";
		String dataTypeURI1 = "http://rio.cs.utep.edu/ciserver/ciprojects/HolesCode/HolesCodeWDO.owl#d2";
		String dataTypeURI2 = "http://rio.cs.utep.edu/ciserver/ciprojects/HolesCode/HolesCodeSAW3.owl#d2-1";
		ToolkitProfileWriter wtr = new ToolkitProfileWriter(
		"velocityModelProfile");
		wtr.addDataType(dataTypeURI);
		wtr.addDataType(dataTypeURI1);
		wtr.addDataType(dataTypeURI2);

		// for float2shortThr
		wtr.addInputBinding(
		"https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/Float2ShortThrService.owl#scalingFactor",
		"1000");
		wtr.addInputBinding(
		"https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/Float2ShortThrService.owl#offset",
		"0");

		// for vtkImageReader
		wtr.addInputBinding(
		"https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/vtkImageDataReaderService.owl#littleEndian",
		"true");
		wtr.addInputBinding(
		"https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/vtkImageDataReaderService.owl#dim",
		"3");
		wtr.addInputBinding(
		"https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/vtkImageDataReaderService.owl#dataOrigin",
		"0/0/0");
		wtr.addInputBinding(
		"https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/vtkImageDataReaderService.owl#dataSpacing",
		"1/1/1");
		wtr.addInputBinding(
		"https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/vtkImageDataReaderService.owl#dataExtent",
		"0/230/0/25/0/68");
		wtr.addInputBinding(
		"https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/vtkImageDataReaderService.owl#numScalarComponents",
		"1");
		wtr.addInputBinding(
		"https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/vtkImageDataReaderService.owl#readLowerLeft",
		"true");

		// for vtkContourFilter
		wtr.addInputBinding(
		"https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/vtkContourFilterService.owl#numContours",
		"35");
		wtr.addInputBinding(
		"https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/vtkContourFilterService.owl#scalarRange",
		"0.0/9000.0");

		// for vtkPolyDataMapper
		wtr.addInputBinding(
		"https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/vtkPolyDataMapperService.owl#scalarRange",
		"0.0/9000.0");
		wtr.addInputBinding(
		"https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/vtkPolyDataMapperService.owl#xRotation",
		"105");
		wtr.addInputBinding(
		"https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/vtkPolyDataMapperService.owl#yRotation",
		"0");
		wtr.addInputBinding(
		"https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/vtkPolyDataMapperService.owl#zRotation",
		"0");
		wtr.addInputBinding(
		"https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/vtkPolyDataMapperService.owl#size",
		"400/300");
		wtr.addInputBinding(
		"https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/vtkPolyDataMapperService.owl#backgroundColor",
		"1/1/1");
		wtr.addInputBinding(
		"https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/vtkPolyDataMapperService.owl#magnification",
		"3");

		// for vtkVolume
		wtr.addInputBinding(
		"https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/vtkVolumeService.owl#xRotation",
		"105");
		wtr.addInputBinding(
		"https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/vtkVolumeService.owl#yRotation",
		"0");
		wtr.addInputBinding(
		"https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/vtkVolumeService.owl#zRotation",
		"0");
		wtr.addInputBinding(
		"https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/vtkVolumeService.owl#size",
		"400/300");
		wtr.addInputBinding(
		"https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/vtkVolumeService.owl#backgroundColor",
		"1/1/1");
		wtr.addInputBinding(
		"https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/vtkVolumeService.owl#magnification",
		"3");
		wtr.addInputBinding(
		"https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/vtkVolumeService.owl#colorFunction",
		"3000,1,1,0/5000,0.5,0.95,0/5600,0,0,1/6500,0.28,0.2,0.5/7000,1,0,0");
		wtr.addInputBinding(
		"https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/vtkVolumeService.owl#opacityFunction",
		"4000,0.2/8000,0.5");

		wtr.setSupportingToolkit("https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/vtk.owl#vtk");
		documentURL = wtr.saveDocument();
		System.out.println(documentURL);
		}
}
