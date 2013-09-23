package org.openvisko.module;

import vtk.*;

public class VTKShepardMethod extends VTKOperator{

	public VTKShepardMethod(String polyDataURL){
		super(polyDataURL, "polyData.vtp", false, false, "imageData.vti");
	}

	public String transform(String sampleDimensions, String maximumDistance){	
		// Create the reader for the data
		vtkXMLPolyDataReader dr = new vtkXMLPolyDataReader();
		dr.SetFileName(inputPath);
		dr.Update();
		
		vtkShepardMethod sm = new vtkShepardMethod();
		sm.DebugOn();
        sm.SetInputConnection(dr.GetOutputPort());
        
        String[] sampleDims = sampleDimensions.trim().split("/");
        int xDim = Integer.valueOf(sampleDims[0]);
        int yDim = Integer.valueOf(sampleDims[1]);
        int zDim = Integer.valueOf(sampleDims[2]);
        
        sm.SetSampleDimensions(xDim, yDim, zDim);
        sm.SetMaximumDistance(1);
        
        double[] bounds = dr.GetOutput().GetBounds();
        sm.SetModelBounds(bounds[0],bounds[1],bounds[2],bounds[3],bounds[4],bounds[5]);
        sm.Update();
		
		vtkXMLImageDataWriter gridWriter = new vtkXMLImageDataWriter();
		gridWriter.SetFileName(outputPath);
		gridWriter.SetDataModeToAscii();
		gridWriter.SetInputConnection(sm.GetOutputPort());
		gridWriter.Update();
		gridWriter.Write();

		dr.Delete();
		sm.Delete();
		gridWriter.Delete();

		return outputURL;
	}
}