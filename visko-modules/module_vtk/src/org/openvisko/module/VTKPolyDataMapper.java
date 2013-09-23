package org.openvisko.module;

import vtk.*;

public class VTKPolyDataMapper extends VTKOperator{

	public VTKPolyDataMapper(String velocityContoursPolyDataURL) {
		super(velocityContoursPolyDataURL, "polyData.vtp", true, false, "image.jpg");
	}

	public String transform(
			String scalarRange,
			String xRotation,
			String yRotation,
			String zRotation,
			String size,
			String backgroundColor,
			String magnification)
	{
		vtkXMLPolyDataReader dr = new vtkXMLPolyDataReader();
		dr.SetFileName(inputPath);
		dr.Update();

		vtkPolyDataMapper contMapper = new vtkPolyDataMapper();
		contMapper.SetInput(dr.GetOutput());
		
		//set scalar range
		String[] sRange = scalarRange.split("/");
		double min = Double.valueOf(sRange[0]);
		double max = Double.valueOf(sRange[1]);
		contMapper.SetScalarRange(min, max);

		vtkActor contActor = new vtkActor();
		contActor.SetMapper(contMapper);

		// We'll put a simple outline around the data.
		vtkOutlineFilter outline = new vtkOutlineFilter();
		outline.SetInput(dr.GetOutput());

		vtkPolyDataMapper outlineMapper = new vtkPolyDataMapper();
		outlineMapper.SetInput(outline.GetOutput());

		vtkActor outlineActor = new vtkActor();
		outlineActor.SetMapper(outlineMapper);
		outlineActor.GetProperty().SetColor(0,0,0);

		//set rotations
		double xRot = Double.valueOf(xRotation);
		double yRot = Double.valueOf(yRotation);
		double zRot = Double.valueOf(zRotation);
		
		//rotate isosurfaces
		contActor.RotateX(xRot);
		contActor.RotateY(yRot);
		contActor.RotateZ(zRot);
		
		//rotate the wire outline accordingly
		outlineActor.RotateX(xRot);
		outlineActor.RotateY(yRot);
		outlineActor.RotateZ(zRot);

		vtkRenderer ren1 = new vtkRenderer();
		ren1.AddActor(contActor);
		ren1.AddActor(outlineActor);
		
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

		//Maginfiy the image? How much? 
		vtkRenderLargeImage renderLarge = new vtkRenderLargeImage();
		renderLarge.SetInput(ren1); 
		
		//set magnification
		int mag = Integer.valueOf(magnification);
		renderLarge.SetMagnification(mag); 

		renWin.Render();

		vtkJPEGWriter image = new vtkJPEGWriter();
		image.SetInputConnection(renderLarge.GetOutputPort());
		image.SetFileName(outputPath);
		image.SetQuality(100);
		image.Write();

		dr.Delete();
		contMapper.Delete();
		contActor.Delete();
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
