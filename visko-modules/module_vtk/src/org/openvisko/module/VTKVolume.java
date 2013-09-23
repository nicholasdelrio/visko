package org.openvisko.module;

import vtk.*;

public class VTKVolume extends VTKOperator{
	public VTKVolume(String velocityImageData3DURL){
		super(velocityImageData3DURL, "imageData.vti", true, false, "image.jpg");
	}
	public String transform(
			String xRotation,
			String yRotation,
			String zRotation,
			String size,
			String backgroundColor,
			String magnification,
			String opacityFunction,
			String colorFunction){  
		// Create the reader for the data
		vtkXMLImageDataReader reader = new vtkXMLImageDataReader();
		reader.SetFileName(inputPath);
		reader.Update();
		
		// Create transfer mapping scalar value to opacity
		vtkPiecewiseFunction opacityTransferFunction = new vtkPiecewiseFunction();
		
		if(opacityFunction != null && !opacityFunction.isEmpty())
		{
			String[] points = opacityFunction.split("/");
			String[] components;
			double x,y;
			for(String point : points)
			{
				point = point.trim();
				components = point.split(",");
				x = Double.valueOf(components[0].trim());
				y = Double.valueOf(components[1].trim());
				
				opacityTransferFunction.AddPoint(x, y);
			}
		}
		
		// Create transfer mapping scalar value to color
		vtkColorTransferFunction colorTransferFunction = new vtkColorTransferFunction();
		
		if(colorFunction != null && !colorFunction.isEmpty())
		{
			String[] points = colorFunction.split("/");
			String[] components;
			double x,r,g,b;
			for(String point : points)
			{
				point = point.trim();
				components = point.split(",");
				x = Double.valueOf(components[0].trim());
				r = Double.valueOf(components[1].trim());
				g = Double.valueOf(components[2].trim());
				b = Double.valueOf(components[3].trim());
				
				colorTransferFunction.AddRGBPoint(x, r, g, b);
			}
		}

		// The property describes how the data will look
		vtkVolumeProperty volumeProperty = new vtkVolumeProperty();
		volumeProperty.SetColor(colorTransferFunction);
		volumeProperty.SetScalarOpacity(opacityTransferFunction);
		volumeProperty.ShadeOn();
		volumeProperty.SetInterpolationTypeToLinear();

		// The mapper ray cast function know how to render the data
		vtkVolumeRayCastCompositeFunction compositeFunction = new vtkVolumeRayCastCompositeFunction();
		vtkVolumeRayCastMapper volumeMapper = new vtkVolumeRayCastMapper();
		volumeMapper.SetVolumeRayCastFunction(compositeFunction);
		volumeMapper.SetInputConnection(reader.GetOutputPort());

		// The volume holds the mapper and the property and can be used to
		// position/orient the volume
		vtkVolume volume = new vtkVolume();
		volume.SetMapper(volumeMapper);
		volume.SetProperty(volumeProperty);

		// We'll put a simple outline around the data.
		vtkOutlineFilter outline = new vtkOutlineFilter();
		outline.SetInput(reader.GetOutput());

		vtkPolyDataMapper outlineMapper = new vtkPolyDataMapper();
		outlineMapper.SetInput(outline.GetOutput());

		// Create an outline to generate a bounding box.
		vtkActor outlineActor = new vtkActor();
		outlineActor.SetMapper(outlineMapper);
		outlineActor.GetProperty().SetColor(0, 0, 0);
		
		//set rotations
		double xRot = Double.valueOf(xRotation);
		double yRot = Double.valueOf(yRotation);
		double zRot = Double.valueOf(zRotation);
		
		//rotate isosurfaces
		volume.RotateX(xRot);
		volume.RotateY(yRot);
		volume.RotateZ(zRot);
		
		//rotate the wire outline accordingly
		outlineActor.RotateX(xRot);
		outlineActor.RotateY(yRot);
		outlineActor.RotateZ(zRot);

		// a renderer for the data
		vtkRenderer ren1 = new vtkRenderer();
		ren1.AddVolume(volume);
		ren1.AddActor2D(outlineActor);
		
		//set background color
		String[] colorValues = backgroundColor.split("/");
		double red = Double.valueOf(colorValues[0]);
		double green = Double.valueOf(colorValues[1]);
		double blue = Double.valueOf(colorValues[2]);
		ren1.SetBackground(red, green, blue);

		// a render window to display the contents
		vtkRenderWindow renWin = new vtkRenderWindow();
		renWin.SetOffScreenRendering(1);
		renWin.AddRenderer(ren1);

		//set size
		String[] dim = size.split("/");
		int width = Integer.valueOf(dim[0]);
		int height = Integer.valueOf(dim[1]);
		renWin.SetSize(width, height);

		// Maginfiy the image? How much?
		vtkRenderLargeImage renderLarge = new vtkRenderLargeImage();
		renderLarge.SetInput(ren1);

		//set magnification
		int mag = Integer.valueOf(magnification);
		renderLarge.SetMagnification(mag); 

		System.out.println("about to render");
		renWin.Render();

		vtkJPEGWriter image = new vtkJPEGWriter();
		image.SetInputConnection(renderLarge.GetOutputPort());
		image.SetFileName(outputPath);
		image.SetQuality(100);

		System.out.println("about to write");
		image.Write();
		
		reader.Delete();
		opacityTransferFunction.Delete();
		colorTransferFunction.Delete();
		volumeProperty.Delete();
		volumeMapper.Delete();
		compositeFunction.Delete();
		volume.Delete();
		outline.Delete();
		outlineMapper.Delete();
		outlineActor.Delete();
		renWin.Delete();
		ren1.Delete();
		image.Delete();
		renderLarge.Delete();
		
		return outputURL;
  }
}