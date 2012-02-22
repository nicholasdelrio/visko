package edu.utep.trustlab.visko.knowledge.github.universal.profiles;


import edu.utep.trustlab.repository.Repository;
import edu.utep.trustlab.visko.knowledge.NickConfigurations;
import edu.utep.trustlab.visko.ontology.service.writer.ToolkitProfileWriter;

public class CoverageModelProfile {
	public static void main(String[] args) {
		Repository.setRepository(NickConfigurations.getCIServer());
		String documentURL;

		/************ profile for velocity ******************************/
		String dataTypeURI = "http://rio.cs.utep.edu/ciserver/ciprojects/HolesCode/HolesCodeSAW3.owl#d7-0";

		ToolkitProfileWriter wtr = new ToolkitProfileWriter(
				"coverageModelProfile");
		wtr.addDataType(dataTypeURI);

		// for vtkImageReader
		wtr.addInputBinding(
				"http://trust.utep.edu/visko/services/vtkImageDataReaderService.owl#littleEndian",
				"true");
		wtr.addInputBinding(
				"http://trust.utep.edu/visko/services/vtkImageDataReaderService.owl#dim",
				"3");
		wtr.addInputBinding(
				"http://trust.utep.edu/visko/services/vtkImageDataReaderService.owl#dataOrigin",
				"0/0/0");
		wtr.addInputBinding(
				"http://trust.utep.edu/visko/services/vtkImageDataReaderService.owl#dataSpacing",
				"1/1/1");
		wtr.addInputBinding(
				"http://trust.utep.edu/visko/services/vtkImageDataReaderService.owl#dataExtent",
				"0/229/0/24/0/67");
		wtr.addInputBinding(
				"http://trust.utep.edu/visko/services/vtkImageDataReaderService.owl#numScalarComponents",
				"1");
		wtr.addInputBinding(
				"http://trust.utep.edu/visko/services/vtkImageDataReaderService.owl#readLowerLeft",
				"true");

		// for vtkContourFilter
		wtr.addInputBinding(
				"http://trust.utep.edu/visko/services/vtkContourFilterService.owl#numContours",
				"35");
		wtr.addInputBinding(
				"http://trust.utep.edu/visko/services/vtkContourFilterService.owl#scalarRange",
				"0.0/9000.0");

		// for vtkPolyDataMapper
		wtr.addInputBinding(
				"http://trust.utep.edu/visko/services/vtkPolyDataMapperService.owl#scalarRange",
				"0.0/9000.0");
		wtr.addInputBinding(
				"http://trust.utep.edu/visko/services/vtkPolyDataMapperService.owl#xRotation",
				"105");
		wtr.addInputBinding(
				"http://trust.utep.edu/visko/services/vtkPolyDataMapperService.owl#yRotation",
				"0");
		wtr.addInputBinding(
				"http://trust.utep.edu/visko/services/vtkPolyDataMapperService.owl#zRotation",
				"0");
		wtr.addInputBinding(
				"http://trust.utep.edu/visko/services/vtkPolyDataMapperService.owl#size",
				"400/300");
		wtr.addInputBinding(
				"http://trust.utep.edu/visko/services/vtkPolyDataMapperService.owl#backgroundColor",
				"1/1/1");
		wtr.addInputBinding(
				"http://trust.utep.edu/visko/services/vtkPolyDataMapperService.owl#magnification",
				"3");

		// for vtkVolume
		wtr.addInputBinding(
				"http://trust.utep.edu/visko/services/vtkVolumeService.owl#xRotation",
				"105");
		wtr.addInputBinding(
				"http://trust.utep.edu/visko/services/vtkVolumeService.owl#yRotation",
				"0");
		wtr.addInputBinding(
				"http://trust.utep.edu/visko/services/vtkVolumeService.owl#zRotation",
				"0");
		wtr.addInputBinding(
				"http://trust.utep.edu/visko/services/vtkVolumeService.owl#size",
				"400/300");
		wtr.addInputBinding(
				"http://trust.utep.edu/visko/services/vtkVolumeService.owl#backgroundColor",
				"0/0/0");
		wtr.addInputBinding(
				"http://trust.utep.edu/visko/services/vtkVolumeService.owl#magnification",
				"3");
		wtr.addInputBinding(
				"http://trust.utep.edu/visko/services/vtkVolumeService.owl#colorFunction",
				"20,1.0,0.0,0.3/80,1.0,0.0,0.3");
		wtr.addInputBinding(
				"http://trust.utep.edu/visko/services/vtkVolumeService.owl#opacityFunction",
				"0,0.0/40,1.0");

		wtr.setSupportingToolkit("http://rio.cs.utep.edu/ciserver/ciprojects/visko/vtk1.owl#vtk1");
		documentURL = wtr.saveDocument();
		System.out.println(documentURL);
	}
}
