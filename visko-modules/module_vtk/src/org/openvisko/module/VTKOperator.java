package org.openvisko.module;

import org.openvisko.module.operators.ToolkitOperator;


public abstract class VTKOperator extends ToolkitOperator
{
	public VTKOperator(String inputDataURL, String baseInputFileName, boolean isTextualInput, boolean shouldBePersistedInMemory, String baseOutputFileName)
	{
		super(inputDataURL, baseInputFileName, isTextualInput, shouldBePersistedInMemory, baseOutputFileName);
		loadVTKNativeLibs();
	}
	
	private void loadVTKNativeLibs()
	{
		System.out.println("loading vtk jni libs...");
		System.loadLibrary("vtkFilteringJava");
		System.loadLibrary("vtkCommonJava");
		System.loadLibrary("vtkIOJava");
		System.loadLibrary("vtkHybridJava");
		System.loadLibrary("vtkImagingJava");
		System.loadLibrary("vtkGraphicsJava");
		System.loadLibrary("vtkRenderingJava");
		System.loadLibrary("vtkVolumeRenderingJava");
	}
}
