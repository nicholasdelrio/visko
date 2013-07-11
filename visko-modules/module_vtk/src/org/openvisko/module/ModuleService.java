package org.openvisko.module;

import javax.jws.WebParam;

public class ModuleService {

	public String vtkDataObjectToDataSetFilter3DGravityData(@WebParam(name = "url") String url){
		VTKDataObjectToDataSetFilter3DGravityData transformer = new VTKDataObjectToDataSetFilter3DGravityData(url);
		return transformer.transform();
	}
	
	public String vtkExtractVOIXZPlane(
			@WebParam(name = "url") String url,
			@WebParam(name="dataExtent") String dataExtent
			){
		VTKExtractVOIXZPlane transformer = new VTKExtractVOIXZPlane(url);
		return transformer.transform(dataExtent);
	}
	
	public String vtkExtractVOIYZPlane(
			@WebParam(name = "url") String url,
			@WebParam(name="dataExtent") String dataExtent
			){
		VTKExtractVOIYZPlane transformer = new VTKExtractVOIYZPlane(url);
		return transformer.transform(dataExtent);
	}

	public String vtkExtractVOIXYPlane(
			@WebParam(name = "url") String url,
			@WebParam(name="dataExtent") String dataExtent
			){
		VTKExtractVOIXYPlane transformer = new VTKExtractVOIXYPlane(url);
		return transformer.transform(dataExtent);
	}

	
	public String vtkExtractVOI3D(
			@WebParam(name = "url") String url,
			@WebParam(name="dataExtent") String dataExtent
			){
		VTKExtractVOI3D transformer = new VTKExtractVOI3D(url);
		return transformer.transform(dataExtent);
	}
	
	public String vtkShepardMethod(
			@WebParam(name = "url") String url,
			@WebParam(name="sampleDimensions") String sampleDimensions,
			@WebParam(name="maximumDistance") String maximumDistance
			){
		VTKShepardMethod transformer = new VTKShepardMethod(url);
		return transformer.transform(sampleDimensions, maximumDistance);
	}
	
	public String vtkGlyph3DSphereSource(
			@WebParam(name = "url") String url,
			@WebParam(name="radius") String radius,
			@WebParam(name="scaleFactor") String scaleFactor
			){
		VTKGlyph3DSphereSource transformer = new VTKGlyph3DSphereSource(url);
		return transformer.transform(radius, scaleFactor);
	}
	
	public String vtkSurfaceReconstructionAndContourFilter(@WebParam(name = "url") String url){
		VTKSurfaceReconstructionAndContourFilter transformer = new VTKSurfaceReconstructionAndContourFilter(url);
		return transformer.transform();
	}
	
	public String vtkVolume(
			@WebParam(name="url") String url,
			@WebParam(name="xRotation") String xRotation,
			@WebParam(name="yRotation") String yRotation,
			@WebParam(name="zRotation") String zRotation,
			@WebParam(name="size") String size,
			@WebParam(name="backgroundColor") String backgroundColor,
			@WebParam(name="magnification")  String magnification,
			@WebParam(name="opacityFunction") String opacityFunction,
			@WebParam(name="colorFunction") String colorFunction)
	{
		VTKVolume transformer = new VTKVolume(url);
		return transformer.transform(
				xRotation,
				yRotation,
				zRotation,
				size,
				backgroundColor,
				magnification,
				opacityFunction,
				colorFunction);
	}
	
	public String vtkPolyDataMapper(
			@WebParam(name="url") String url,
			@WebParam(name="scalarRange") String scalarRange,
			@WebParam(name="xRotation") String xRotation,
			@WebParam(name="yRotation") String yRotation,
			@WebParam(name="zRotation") String zRotation,
			@WebParam(name="size") String size,
			@WebParam(name="backgroundColor") String backgroundColor,
			@WebParam(name="magnification")  String magnification)
	{
		VTKPolyDataMapper transformer = new VTKPolyDataMapper(url);
		return transformer.transform(
				scalarRange,
				xRotation,
				yRotation,
				zRotation,
				size,
				backgroundColor,
				magnification);
	}
		
	public String vtkContourFilter(
			@WebParam(name="url") String url,
			@WebParam(name="numContours") String numContours,
			@WebParam(name="scalarRange") String scalarRange)
	{
		VTKContourDataFilter transformer = new VTKContourDataFilter(url);
		return transformer.transform(numContours, scalarRange);
	}
	
	public String vtkImageDataReader3DIntegers(
			@WebParam(name="url") String url,
			@WebParam(name="littleEndian") String littleEndian,
			@WebParam(name="dataOrigin") String dataOrigin,
			@WebParam(name="dataSpacing") String dataSpacing,
			@WebParam(name="dataExtent") String dataExtent,
			@WebParam(name="numScalarComponents") String numScalarComponents,
			@WebParam(name="readLowerLeft") String readLowerLeft)
	{
		VTKImageDataReader3DIntegers transformer = new VTKImageDataReader3DIntegers(url);
		return transformer.transform(littleEndian, dataOrigin, dataSpacing, dataExtent, numScalarComponents, readLowerLeft);
	}
	
	public String vtkImageDataReader3DUnsignedShortIntegers(
			@WebParam(name="url") String url,
			@WebParam(name="littleEndian") String littleEndian,
			@WebParam(name="dataOrigin") String dataOrigin,
			@WebParam(name="dataSpacing") String dataSpacing,
			@WebParam(name="dataExtent") String dataExtent,
			@WebParam(name="numScalarComponents") String numScalarComponents,
			@WebParam(name="readLowerLeft") String readLowerLeft)
	{
		VTKImageDataReader3DUnsignedShortIntegers transformer = new VTKImageDataReader3DUnsignedShortIntegers(url);
		return transformer.transform(littleEndian, dataOrigin, dataSpacing, dataExtent, numScalarComponents, readLowerLeft);
	}
	
	public String vtkImageDataReader3DFloats(
			@WebParam(name="url") String url,
			@WebParam(name="littleEndian") String littleEndian,
			@WebParam(name="dataOrigin") String dataOrigin,
			@WebParam(name="dataSpacing") String dataSpacing,
			@WebParam(name="dataExtent") String dataExtent,
			@WebParam(name="numScalarComponents") String numScalarComponents,
			@WebParam(name="readLowerLeft") String readLowerLeft)
	{
		VTKImageDataReader3DFloats transformer = new VTKImageDataReader3DFloats(url);
		return transformer.transform(littleEndian, dataOrigin, dataSpacing, dataExtent, numScalarComponents, readLowerLeft);
	}
	
}
