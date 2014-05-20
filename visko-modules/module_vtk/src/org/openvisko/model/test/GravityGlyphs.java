package org.openvisko.model.test;

import org.openvisko.module.util.FileUtils;

import gravityMapScenario.gravityDataset.Dataset;
import gravityMapScenario.gravityDataset.Row;
import vtk.vtkActor;
import vtk.vtkAssignAttribute;
import vtk.vtkDataObjectToDataSetFilter;
import vtk.vtkFloatArray;
import vtk.vtkGlyph3D;
import vtk.vtkJPEGWriter;
import vtk.vtkOutlineFilter;
import vtk.vtkPolyDataMapper;
import vtk.vtkProgrammableDataObjectSource;
import vtk.vtkRearrangeFields;
import vtk.vtkRenderLargeImage;
import vtk.vtkRenderWindow;
import vtk.vtkRenderer;
import vtk.vtkSphereSource;

public class GravityGlyphs {
	private vtkProgrammableDataObjectSource dos;
	private String LON_FIELD_NAME = "lon";
	private String LAT_FIELD_NAME = "lat";
	private String ELEVATION_FIELD_NAME = "ele";
	private String GRAVITY_FIELD_NAME = "gravity";

	public  GravityGlyphs(String gravityPointsDataURL){
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
	public void transform (){  
		// Create the reader for the data
		dos = new vtkProgrammableDataObjectSource();
		dos.DebugOn();
		dos.SetExecuteMethod(this, "loadFieldData");
		dos.Update();

		// Read in data as field data
		vtkDataObjectToDataSetFilter do2ds = new vtkDataObjectToDataSetFilter();
		do2ds.SetInputConnection(dos.GetOutputPort());
		do2ds.SetDataSetTypeToPolyData();
		do2ds.SetPointComponent(0, LON_FIELD_NAME, 0);
		do2ds.SetPointComponent(1, LAT_FIELD_NAME, 0);
		do2ds.SetPointComponent(2, ELEVATION_FIELD_NAME, 0);
		do2ds.Update();

		vtkRearrangeFields rf = new vtkRearrangeFields();
		rf.SetInputConnection(do2ds.GetOutputPort());
		rf.AddOperation("MOVE", GRAVITY_FIELD_NAME, "DATA_OBJECT", "POINT_DATA");
		rf.Update();

		vtkAssignAttribute aa = new vtkAssignAttribute();
		aa.DebugOn();
		aa.SetInputConnection(rf.GetOutputPort());
		aa.Assign(GRAVITY_FIELD_NAME, "SCALARS", "POINT_DATA");
		aa.Update();

		//add points/glyphs
		vtkSphereSource sphereSource = new vtkSphereSource();
	    double doubleRadius = Double.valueOf(0.06);
	    sphereSource.SetRadius(doubleRadius);

	    vtkGlyph3D glyphs = new vtkGlyph3D();
	    glyphs.SetInputConnection(aa.GetOutputPort());
	    glyphs.SetSourceConnection(sphereSource.GetOutputPort());
	    glyphs.SetVectorModeToUseNormal();
	    glyphs.SetScaleModeToDataScalingOff();
	    double doubleScaleFactor = Double.valueOf(0.25);
	    glyphs.SetScaleFactor(doubleScaleFactor);
	    glyphs.Update();
			    
	    // Render Polydata glyphs
	    vtkPolyDataMapper contMapper = new vtkPolyDataMapper();
		contMapper.SetInput(glyphs.GetOutput());
		
		//set scalar range
		double min = -236;
		double max = -177;
		contMapper.SetScalarRange(min, max);

		vtkActor contActor = new vtkActor();
		contActor.SetMapper(contMapper);

		// We'll put a simple outline around the data.
		vtkOutlineFilter outline = new vtkOutlineFilter();
		outline.SetInput(glyphs.GetOutput());

		vtkPolyDataMapper outlineMapper = new vtkPolyDataMapper();
		outlineMapper.SetInput(outline.GetOutput());

		vtkActor outlineActor = new vtkActor();
		outlineActor.SetMapper(outlineMapper);
		outlineActor.GetProperty().SetColor(0,0,0);

		//set rotations
		double xRot = Double.valueOf(-30);
		double yRot = Double.valueOf(0);
		double zRot = Double.valueOf(0);
		
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
		double red = 0.5;
		double green = 0.5;
		double blue = 0.5;;
		ren1.SetBackground(red, green, blue);

		// a render window to display the contents
		vtkRenderWindow renWin = new vtkRenderWindow();
		renWin.SetOffScreenRendering(1);
		renWin.AddRenderer(ren1);
		
		//set size
		int width = 400;
		int height = 300;
		renWin.SetSize(width, height);

		//Maginfiy the image? How much? 
		vtkRenderLargeImage renderLarge = new vtkRenderLargeImage();
		renderLarge.SetInput(ren1); 
		
		//set magnification
		int mag = Integer.valueOf(3);
		renderLarge.SetMagnification(mag); 

		renWin.Render();

		vtkJPEGWriter image = new vtkJPEGWriter();
		image.SetInputConnection(renderLarge.GetOutputPort());
		image.SetFileName("./webapp/output/glyphs.jpg");
		image.SetQuality(100);
		image.Write();

		contMapper.Delete();
		contActor.Delete();
		outline.Delete();
		outlineMapper.Delete();
		outlineActor.Delete();
		renWin.Delete();
		ren1.Delete();
		image.Delete();
		renderLarge.Delete();

		// Clean up resources
		dos.Delete();
		do2ds.Delete();
		rf.Delete();
		aa.Delete();
		
		sphereSource.Delete();
		glyphs.Delete();
				
		contMapper.Delete();
		contActor.Delete();
		outline.Delete();
		outlineMapper.Delete();
		outlineActor.Delete();
		renWin.Delete();
		ren1.Delete();
		image.Delete();
		renderLarge.Delete();
	}

	public void loadFieldData()
	{
		String asciiDataset = FileUtils.readTextFile("./webapp/test-data/gravityDataset.txt").trim();
				
		Dataset ds = new Dataset(asciiDataset, true);
		Row[] dataset = ds.getDataset();

		vtkFloatArray lon = new vtkFloatArray();
		lon.SetName(LON_FIELD_NAME);

		vtkFloatArray lat = new vtkFloatArray();
		lat.SetName(LAT_FIELD_NAME);

		vtkFloatArray ele = new vtkFloatArray();
		ele.SetName(ELEVATION_FIELD_NAME);

		vtkFloatArray grav = new vtkFloatArray();
		grav.SetName(GRAVITY_FIELD_NAME);

		for (int i = 0; i < ds.getNumRecords(); i++) {
			Row aRow = dataset[i];
			for (int j = 0; j < 4; j++) {

				double value = 0;
				try{
					value = Double.valueOf(aRow.getValue(j));
				}catch(Exception e){
					e.printStackTrace();
					System.out.println("details, i=" + i + ", j=" + j);
				}

				if (j == 0)
					lon.InsertNextValue(value);
				else if (j == 1)
					lat.InsertNextValue(value);
				else if (j == 2)
					grav.InsertNextValue(value);
				else
					//try to normalize heights as mucha as possible
					ele.InsertNextValue(value/1000);
			}
		}

		dos.GetOutput().GetFieldData().AllocateArrays(4);
		dos.GetOutput().GetFieldData().AddArray(lon);
		dos.GetOutput().GetFieldData().AddArray(lat);
		dos.GetOutput().GetFieldData().AddArray(ele);
		dos.GetOutput().GetFieldData().AddArray(grav);
	}	
}
