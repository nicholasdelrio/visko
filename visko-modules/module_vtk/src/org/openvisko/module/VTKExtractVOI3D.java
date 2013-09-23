package org.openvisko.module;

import vtk.*;

public class VTKExtractVOI3D extends VTKOperator{
	public VTKExtractVOI3D(String imageDataURL){
		super(imageDataURL, "imageData.vti", false, false, "imageData.vtp");
	}

	public String transform(String dataExtent){

		// Create the reader for the data
		vtkXMLImageDataReader reader = new vtkXMLImageDataReader();
		reader.SetFileName(inputPath);
		reader.Update();
		
        vtkExtractVOI ev = new vtkExtractVOI();
        ev.SetInputConnection(reader.GetOutputPort());
        
    	//set data extent
		String[] extents = dataExtent.split("/");
		int xStart = Integer.valueOf(extents[0]);
		int xEnd = Integer.valueOf(extents[1]);
		int yStart = Integer.valueOf(extents[2]);
		int yEnd = Integer.valueOf(extents[3]);
		int zStart = Integer.valueOf(extents[4]);
		int zEnd = Integer.valueOf(extents[5]);
		
        ev.SetVOI(xStart,xEnd,yStart,yEnd,zStart,zEnd);
        ev.Update();
		
		vtkXMLImageDataWriter gridWriter = new vtkXMLImageDataWriter();
		gridWriter.SetFileName(outputPath);
		gridWriter.SetDataModeToAscii();
		gridWriter.SetInputConnection(ev.GetOutputPort());
		gridWriter.Update();
		gridWriter.Write();

		reader.Delete();
		ev.Delete();
		gridWriter.Delete();

		return outputURL;
	}
}
