package org.openvisko.module.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.openvisko.module.util.FileUtils;

import gravityMapScenario.gravityDataset.Dataset;
import gravityMapScenario.gravityDataset.Row;
import vtk.vtkActor;
import vtk.vtkAssignAttribute;
import vtk.vtkDataObjectToDataSetFilter;
import vtk.vtkFloatArray;
import vtk.vtkGlyph3D;
import vtk.vtkJPEGWriter;
import vtk.vtkNativeLibrary;
import vtk.vtkOpenGLRenderWindow;
import vtk.vtkOutlineFilter;
import vtk.vtkPolyDataMapper;
import vtk.vtkProgrammableDataObjectSource;
import vtk.vtkRearrangeFields;
import vtk.vtkRenderLargeImage;
import vtk.vtkRenderWindow;
import vtk.vtkRenderWindowInteractor;
import vtk.vtkRenderer;
import vtk.vtkSphereSource;

public class GravityGlyphs {
	
	 static {
	        if (!vtkNativeLibrary.LoadAllNativeLibraries()) {
	            for (vtkNativeLibrary lib : vtkNativeLibrary.values()) {
	                if (!lib.IsLoaded()) {
	                    System.out.println(lib.GetLibraryName() + " not loaded");
	                }
	            }
	        }
	        vtkNativeLibrary.DisableOutputWindow(null);
	    }
	
	private vtkProgrammableDataObjectSource dos;
	private String LON_FIELD_NAME = "lon";
	private String LAT_FIELD_NAME = "lat";
	private String ELEVATION_FIELD_NAME = "ele";
	private String GRAVITY_FIELD_NAME = "gravity";
	
	private File input;
	private File output;

	public GravityGlyphs(){
		input = new File("./webapp/test-data/gravityDataset.txt");
		output = new File("./webapp/output/glyphs.jpg");
	}
	
	public GravityGlyphs(File input, File output){
		this.input = input;
		this.output = output;
	}
	
	private vtkAssignAttribute getFieldData(){
		
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
		
		return aa;
	}
	
	private vtkGlyph3D getGlyphs(vtkAssignAttribute aa){
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
	    
	    return glyphs;
	}
	
	private vtkRenderer getRenderer(vtkGlyph3D glyphs){
	    // Render Polydata glyphs
	    vtkPolyDataMapper contMapper = new vtkPolyDataMapper();
		contMapper.SetInputData(glyphs.GetOutput());
		
		//set scalar range
		double min = -236;
		double max = -177;
		contMapper.SetScalarRange(min, max);

		vtkActor contActor = new vtkActor();
		contActor.SetMapper(contMapper);

		// We'll put a simple outline around the data.
		vtkOutlineFilter outline = new vtkOutlineFilter();
		outline.SetInputData(glyphs.GetOutput());

		vtkPolyDataMapper outlineMapper = new vtkPolyDataMapper();
		outlineMapper.SetInputData(outline.GetOutput());

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
		
		return ren1;
	}
	
	private vtkOpenGLRenderWindow getRenderWindow(vtkRenderer ren1){
		// a render window to display the contents		
		vtkOpenGLRenderWindow renWin = new vtkOpenGLRenderWindow();
		renWin.AddRenderer(ren1);
		renWin.OffScreenRenderingOn();
		
		//set size
		int width = 1500;
		int height = 1500;
		renWin.SetSize(width, height);
	
		return renWin;
		
		/*
		vtkRenderWindowInteractor iren = new vtkRenderWindowInteractor();
		iren.SetRenderWindow(renWin);
		renWin.Render();
		iren.Start();*/
	}
	
	private vtkRenderLargeImage getMagnification(vtkRenderer ren1){
		//Maginfiy the image? How much? 
		vtkRenderLargeImage renderLarge = new vtkRenderLargeImage();
		renderLarge.SetInput(ren1); 
		
		//set magnification
		int mag = Integer.valueOf(10);
		renderLarge.SetMagnification(mag); 
			
		return renderLarge;
	}
	
	private void dumpJPEG(vtkRenderLargeImage renderLarge){
		vtkJPEGWriter image = new vtkJPEGWriter();
		image.SetInputConnection(renderLarge.GetOutputPort());
		image.SetFileName(output.getAbsoluteFile());
		
		System.out.println("about to write file");
		image.SetQuality(100);
		image.Write();
		System.out.println("wrote file");
	}
	
	private void transform (){
		vtkAssignAttribute aa = this.getFieldData();
		vtkGlyph3D glyphs = this.getGlyphs(aa);
		vtkRenderer ren1 = this.getRenderer(glyphs);
		vtkRenderLargeImage renderLarge = this.getMagnification(ren1);
		vtkOpenGLRenderWindow renWin = this.getRenderWindow(ren1);
		renWin.Render();
		
		this.dumpJPEG(renderLarge);
		
	}
	
	public static String readTextFile(File file){
		try{
			BufferedReader in = new BufferedReader(new FileReader(file));
			String line, fileContents;

			fileContents = null;

			while ((line = in.readLine()) != null){
				if (fileContents == null)
					fileContents = line + "\n";
				else
					fileContents = fileContents + line + "\n";
			}

			in.close();
			return fileContents;
		}
		catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}

	public void loadFieldData()
	{
		String asciiDataset = readTextFile(input).trim();
				
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
	
	public static void main(String[] args){
		GravityGlyphs glyphs;
		
		if(args.length != 2)
			glyphs = new GravityGlyphs();
		else{
			File input,output;
			input = new File(args[0]);
			output = new File(args[1]);
			glyphs = new GravityGlyphs(input, output);
		}
		glyphs.transform();
	}
}
