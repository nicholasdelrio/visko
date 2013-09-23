package org.openvisko.module;

import vtk.*;

public class VTKImageDataReader3DFloats extends VTKOperator{
	public VTKImageDataReader3DFloats(String coverageModelShortIntURL){
		super(coverageModelShortIntURL, "arrayData.bin", false, false, "imageData.vti");
	}

	public String transform(String littleEndian, String dataOrigin, String dataSpacing, String dataExtent, String numScalarComponents, String readLowerLeft){	
		// Create the reader for the data
		vtkImageReader reader = new vtkImageReader();
		reader.SetFileName(inputPath);
		reader.SetDataScalarTypeToFloat();
		
		//set byte endian
		if(littleEndian.equalsIgnoreCase("true"))
			reader.SetDataByteOrderToLittleEndian();
		else
			reader.SetDataByteOrderToBigEndian();
		
		//set dimension
		reader.SetFileDimensionality(3);
		
		//set data origin indices 
		String[] indices = dataOrigin.split("/");
		int xIndex = Integer.valueOf(indices[0]);
		int yIndex = Integer.valueOf(indices[1]);
		int zIndex = Integer.valueOf(indices[2]);
		reader.SetDataOrigin(xIndex, yIndex, zIndex);
		
		//set data spacing
		String[] spacings = dataSpacing.split("/");
		int xSpacing = Integer.valueOf(spacings[0]);
		int ySpacing = Integer.valueOf(spacings[1]);
		int zSpacing = Integer.valueOf(spacings[2]);
		reader.SetDataSpacing(xSpacing, ySpacing, zSpacing);
		
		//set data extent
		String[] extents = dataExtent.split("/");
		int xStart = Integer.valueOf(extents[0]);
		int xEnd = Integer.valueOf(extents[1]);
		int yStart = Integer.valueOf(extents[2]);
		int yEnd = Integer.valueOf(extents[3]);
		int zStart = Integer.valueOf(extents[4]);
		int zEnd = Integer.valueOf(extents[5]);		                                 
		reader.SetDataExtent(xStart, xEnd, yStart, yEnd, zStart, zEnd);
	
		//set number scalar components
		int numberScalarComponents = Integer.valueOf(numScalarComponents);
		reader.SetNumberOfScalarComponents(numberScalarComponents);
		
		//set file reading location
		if(readLowerLeft.equalsIgnoreCase("true"))
			reader.FileLowerLeftOn();
		else
			reader.FileLowerLeftOff();
		
		reader.Update();
		// reader SetDataMask 0x7fff
		
		vtkXMLImageDataWriter gridWriter = new vtkXMLImageDataWriter();
		gridWriter.SetFileName(outputPath);
		gridWriter.SetDataModeToAscii();
		gridWriter.SetInputConnection(reader.GetOutputPort());
		gridWriter.Update();
		gridWriter.Write();

		reader.Delete();
		gridWriter.Delete();

		return outputURL;
	}
}
