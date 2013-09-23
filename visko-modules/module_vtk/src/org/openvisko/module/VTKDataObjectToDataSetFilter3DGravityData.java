package org.openvisko.module;

import vtk.*;
import gravityMapScenario.gravityDataset.*;

public class  VTKDataObjectToDataSetFilter3DGravityData extends VTKOperator{

	private vtkProgrammableDataObjectSource dos;
	private String LON_FIELD_NAME = "lon";
	private String LAT_FIELD_NAME = "lat";
	private String ELEVATION_FIELD_NAME = "ele";
	private String GRAVITY_FIELD_NAME = "gravity";

	public  VTKDataObjectToDataSetFilter3DGravityData(String gravityPointsDataURL){
		super(gravityPointsDataURL, "gravityData.txt", true, true, "polyData.vtp");
	}

	public String transform (){  
		// Create the reader for the data
		dos = new vtkProgrammableDataObjectSource();
		dos.DebugOn();
		dos.SetExecuteMethod(this, "loadFieldData");
		dos.Update();

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

		//dump out the poly data points of gravity data
		vtkXMLPolyDataWriter polyDataWriter = new vtkXMLPolyDataWriter();
		polyDataWriter.SetFileName(outputPath);
		polyDataWriter.SetDataModeToAscii();
		polyDataWriter.SetInputConnection(aa.GetOutputPort());
		polyDataWriter.Update();
		polyDataWriter.Write();

		dos.Delete();
		do2ds.Delete();
		rf.Delete();
		aa.Delete();
		polyDataWriter.Delete();

		return outputURL;
	}

	public void loadFieldData()
	{
		String asciiDataset = stringData.trim();
				
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
