package edu.utep.trustlab.visko.knowledge.github.universal.profiles;

import edu.utep.trustlab.repository.NickConfigurations;
import edu.utep.trustlab.repository.Repository;
import edu.utep.trustlab.visko.ontology.service.writer.ToolkitProfileWriter;

public class DuSumProfile {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Repository.setRepository(NickConfigurations.getLocalFileSystem());
		/************ profile for dusum ******************************/
		String dataTypeURI = "http://rio.cs.utep.edu/ciserver/ciprojects/HolesCode/HolesCodeSAW3.owl#d8-0";
		String dataTypeURI1 = "http://rio.cs.utep.edu/ciserver/ciprojects/HolesCode/HolesCodeWDO.owl#d8";
		ToolkitProfileWriter wtr2 = new ToolkitProfileWriter(
				"slownessPertubationModelProfile");
		wtr2.addDataType(dataTypeURI);
		wtr2.addDataType(dataTypeURI1);

		// for float2shortThr
		wtr2.addInputBinding(
				"http://trust.utep.edu/visko/services/Float2ShortThrService.owl#scalingFactor",
				"1");
		wtr2.addInputBinding(
				"http://trust.utep.edu/visko/services/Float2ShortThrService.owl#offset",
				"10000");

		// for vtkImageReader
		wtr2.addInputBinding(
				"http://trust.utep.edu/visko/services/vtkImageDataReaderService.owl#littleEndian",
				"true");
		wtr2.addInputBinding(
				"http://trust.utep.edu/visko/services/vtkImageDataReaderService.owl#dim",
				"3");
		wtr2.addInputBinding(
				"http://trust.utep.edu/visko/services/vtkImageDataReaderService.owl#dataOrigin",
				"0/0/0");
		wtr2.addInputBinding(
				"http://trust.utep.edu/visko/services/vtkImageDataReaderService.owl#dataSpacing",
				"1/1/1");
		wtr2.addInputBinding(
				"http://trust.utep.edu/visko/services/vtkImageDataReaderService.owl#dataExtent",
				"0/229/0/24/0/67");
		wtr2.addInputBinding(
				"http://trust.utep.edu/visko/services/vtkImageDataReaderService.owl#numScalarComponents",
				"1");
		wtr2.addInputBinding(
				"http://trust.utep.edu/visko/services/vtkImageDataReaderService.owl#readLowerLeft",
				"true");

		// for vtkContourFilter
		wtr2.addInputBinding(
				"http://trust.utep.edu/visko/services/vtkContourFilterService.owl#numContours",
				"35");
		wtr2.addInputBinding(
				"http://trust.utep.edu/visko/services/vtkContourFilterService.owl#scalarRange",
				"9997.0/10014.0");

		// for vtkPolyDataMapper
		wtr2.addInputBinding(
				"http://trust.utep.edu/visko/services/vtkPolyDataMapperService.owl#scalarRange",
				"9997.0/10014.0");
		wtr2.addInputBinding(
				"http://trust.utep.edu/visko/services/vtkPolyDataMapperService.owl#xRotation",
				"90");
		wtr2.addInputBinding(
				"http://trust.utep.edu/visko/services/vtkPolyDataMapperService.owl#yRotation",
				"0");
		wtr2.addInputBinding(
				"http://trust.utep.edu/visko/services/vtkPolyDataMapperService.owl#zRotation",
				"0");
		wtr2.addInputBinding(
				"http://trust.utep.edu/visko/services/vtkPolyDataMapperService.owl#size",
				"400/300");
		wtr2.addInputBinding(
				"http://trust.utep.edu/visko/services/vtkPolyDataMapperService.owl#backgroundColor",
				"1/1/1");
		wtr2.addInputBinding(
				"http://trust.utep.edu/visko/services/vtkPolyDataMapperService.owl#magnification",
				"3");

		// for vtkVolume
		wtr2.addInputBinding(
				"http://trust.utep.edu/visko/services/vtkVolumeService.owl#xRotation",
				"105");
		wtr2.addInputBinding(
				"http://trust.utep.edu/visko/services/vtkVolumeService.owl#yRotation",
				"0");
		wtr2.addInputBinding(
				"http://trust.utep.edu/visko/services/vtkVolumeService.owl#zRotation",
				"0");
		wtr2.addInputBinding(
				"http://trust.utep.edu/visko/services/vtkVolumeService.owl#size",
				"400/300");
		wtr2.addInputBinding(
				"http://trust.utep.edu/visko/services/vtkVolumeService.owl#backgroundColor",
				"1/1/1");
		wtr2.addInputBinding(
				"http://trust.utep.edu/visko/services/vtkVolumeService.owl#magnification",
				"3");
		wtr2.addInputBinding(
				"http://trust.utep.edu/visko/services/vtkVolumeService.owl#colorFunction",
				"0.0,0.0,0.0,0.0/1000.0,1.0,0.0,0.0/3000.0,0.0,0.0,1.0/5000.0,0.0,1.0,0.0/7000.0,0.0,0.2,0.0");
		wtr2.addInputBinding(
				"http://trust.utep.edu/visko/services/vtkVolumeService.owl#opacityFunction",
				"20,0.0/255,0.2");

		wtr2.setSupportingToolkit("https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/vtk.owl#vtk");
		String documentURL = wtr2.saveDocument();
		System.out.println(documentURL);

	}

}
