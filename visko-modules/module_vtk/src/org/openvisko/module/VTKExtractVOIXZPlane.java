package org.openvisko.module;

import vtk.*;

public class VTKExtractVOIXZPlane extends VTKOperator{
	public VTKExtractVOIXZPlane(String imageDataURL){
		super(imageDataURL, "imageData.vti", false, false, "imageData.vti");
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
		int ySlice = Integer.valueOf(extents[2]);
		int zStart = Integer.valueOf(extents[3]);
		int zEnd = Integer.valueOf(extents[4]);
		
        ev.SetVOI(xStart,xEnd,ySlice,ySlice,zStart,zEnd);
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
