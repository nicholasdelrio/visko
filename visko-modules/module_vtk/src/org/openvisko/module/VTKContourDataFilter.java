package org.openvisko.module;

import vtk.*;

public class VTKContourDataFilter extends VTKOperator{

	public VTKContourDataFilter(String velocityImageData3DURL){
		super(velocityImageData3DURL, "imageData.vti", false, false, "polyData.vtp");
	}
	
	public String transform (String numContours, String scalarRange){  
		// Create the reader for the data
		vtkXMLImageDataReader reader = new vtkXMLImageDataReader();
		reader.SetFileName(inputPath);
		reader.Update();
		
		vtkContourFilter contours = new vtkContourFilter();
		contours.SetInput(reader.GetOutput());
		
		int numberOfContours = Integer.valueOf(numContours);
		String[] range = scalarRange.split("/");
		double min = Double.valueOf(range[0]);
		double max = Double.valueOf(range[1]);
		contours.GenerateValues(numberOfContours, min, max);
		
		vtkXMLPolyDataWriter polyDataWriter = new vtkXMLPolyDataWriter();
		polyDataWriter.SetFileName(outputPath);
		polyDataWriter.SetDataModeToAscii();
		polyDataWriter.SetInputConnection(contours.GetOutputPort());
		polyDataWriter.Update();
		polyDataWriter.Write();
		
		reader.Delete();
		contours.Delete();
		polyDataWriter.Delete();
		
		return outputURL;
  }
}
