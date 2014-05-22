package org.openvisko.module;

import org.openvisko.module.operators.ToolkitOperator;

import vtk.vtkNativeLibrary;


public abstract class VTKOperator extends ToolkitOperator{
	public VTKOperator(String inputDataURL, String baseInputFileName, boolean isTextualInput, boolean shouldBePersistedInMemory, String baseOutputFileName)	{
		super(inputDataURL, baseInputFileName, isTextualInput, shouldBePersistedInMemory, baseOutputFileName);
		loadVTKNativeLibs();
	}
	
	private void loadVTKNativeLibs(){
		
		if (!vtkNativeLibrary.LoadAllNativeLibraries()) {
            for (vtkNativeLibrary lib : vtkNativeLibrary.values()) {
                if (!lib.IsLoaded()) {
                    System.out.println(lib.GetLibraryName() + " not loaded");
                }
            }
        }
		vtkNativeLibrary.DisableOutputWindow(null);
	}
}
