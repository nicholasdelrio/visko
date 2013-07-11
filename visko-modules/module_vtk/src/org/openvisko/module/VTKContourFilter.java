package org.openvisko.module;

import org.openvisko.module.ModuleRDFRegistration.Resources;
import org.openvisko.module.registration.ModuleOperatorService;
import org.openvisko.module.registration.ModuleWriter;

public class VTKContourFilter {
	
	public static void populateVTKContourFilters(String wsdlURL, ModuleWriter packageWriter){
		
		ModuleOperatorService service1 = packageWriter.createNewOperatorService(ModuleRDFRegistration.Resources.vtkContourFilter3D, ModuleRDFRegistration.Resources.vtkContourFilter);
		service1.setInputFormat(Resources.xml);
		service1.setOutputFormat(Resources.xml);
		service1.setLabel(ModuleRDFRegistration.Resources.vtkContourFilter3D);
		service1.setComment("Generates isosurfaces from 3D vtkImageData");
		service1.setWSDLURL(wsdlURL);
		service1.setInputDataType(Resources.vtkImageData3D);
		service1.setOutputDataType(Resources.vtkPolyData);
		service1.setView(ModuleRDFRegistration.Resources.isosurfaces3D);
		
		ModuleOperatorService service2 = packageWriter.createNewOperatorService(ModuleRDFRegistration.Resources.vtkContourFilter2D, ModuleRDFRegistration.Resources.vtkContourFilter);
		service2.setInputFormat(Resources.xml);
		service2.setOutputFormat(Resources.xml);
		service2.setLabel(ModuleRDFRegistration.Resources.vtkContourFilter2D);
		service2.setComment("Generates contour map from 2D vtkImageData");
		service2.setWSDLURL(wsdlURL);
		service2.setInputDataType(Resources.vtkImageData2D);
		service2.setOutputDataType(Resources.vtkPolyData);
		service2.setView(Resources.contourMap2D);
		
	}
}
