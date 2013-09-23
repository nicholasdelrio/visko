package org.openvisko.module;

import vtk.*;

public class VTKSurfaceReconstructionAndContourFilter extends VTKOperator{
	public VTKSurfaceReconstructionAndContourFilter(String polyDataURL){
		super(polyDataURL, "polyData.vtp", false, false, "polyData.vtp");
	}

	public String transform(){
		vtkXMLPolyDataReader dr = new vtkXMLPolyDataReader();
		dr.SetFileName(inputPath);
		dr.Update();
		
		vtkSurfaceReconstructionFilter r = new vtkSurfaceReconstructionFilter();
		r.SetInputConnection(dr.GetOutputPort());
		r.Update();
		
		vtkContourFilter contours = new vtkContourFilter();
		contours.SetInputConnection(r.GetOutputPort());
		contours.SetValue(0, 0.0);
		contours.Update();

		vtkXMLPolyDataWriter polyDataWriter = new vtkXMLPolyDataWriter();
		polyDataWriter.SetFileName(outputPath);
		polyDataWriter.SetDataModeToAscii();
		polyDataWriter.SetInputConnection(contours.GetOutputPort());
		polyDataWriter.Update();
		polyDataWriter.Write();

		dr.Delete();
		r.Delete();
		contours.Delete();
		polyDataWriter.Delete();

		return outputURL;
	}
}
