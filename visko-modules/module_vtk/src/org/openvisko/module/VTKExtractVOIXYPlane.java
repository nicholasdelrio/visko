package org.openvisko.module;

import vtk.*;

public class VTKExtractVOIXYPlane extends VTKOperator{
	public VTKExtractVOIXYPlane(String imageDataURL){
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
		int yStart = Integer.valueOf(extents[2]);
		int yEnd = Integer.valueOf(extents[3]);
		int zSlice = Integer.valueOf(extents[4]);
		
        ev.SetVOI(xStart,xEnd,yStart,yEnd,zSlice,zSlice);
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
