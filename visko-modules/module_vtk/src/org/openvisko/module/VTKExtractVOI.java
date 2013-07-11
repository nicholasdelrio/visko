package org.openvisko.module;

import org.openvisko.module.ModuleRDFRegistration.Resources;
import org.openvisko.module.registration.ModuleOperatorService;
import org.openvisko.module.registration.ModuleWriter;


public class VTKExtractVOI {
	
	public static void populateVTKExtractVOIs(String wsdlURL, ModuleWriter packageWriter){

		ModuleOperatorService service1 = packageWriter.createNewOperatorService(null, ModuleRDFRegistration.Resources.vtkExtractVOI3D);
		service1.setInputFormat(Resources.xml);
		service1.setOutputFormat(Resources.xml);
		service1.setLabel(ModuleRDFRegistration.Resources.vtkExtractVOI3D);
		service1.setComment("Extracts a subset volume");
		service1.setWSDLURL(wsdlURL);
		service1.setInputDataType(Resources.vtkImageData3D);
		service1.setOutputDataType(Resources.vtkImageData3D);
		
		ModuleOperatorService service2 = packageWriter.createNewOperatorService(null, ModuleRDFRegistration.Resources.vtkExtractVOIXYPlane);
		service2.setInputFormat(Resources.xml);
		service2.setOutputFormat(Resources.xml);
		service2.setLabel(ModuleRDFRegistration.Resources.vtkExtractVOIXYPlane);
		service2.setComment("Extracts a subset XY plane by specifying plane bounds and location on Z axix");
		service2.setWSDLURL(wsdlURL);
		service2.setInputDataType(Resources.vtkImageData3D);
		service2.setOutputDataType(Resources.vtkImageData2D);
		service2.setAsDimensionFilter();
		
		ModuleOperatorService service3 = packageWriter.createNewOperatorService(null, ModuleRDFRegistration.Resources.vtkExtractVOIXZPlane);
		service3.setInputFormat(Resources.xml);
		service3.setOutputFormat(Resources.xml);
		service3.setLabel(ModuleRDFRegistration.Resources.vtkExtractVOIXZPlane);
		service3.setComment("Extracts a subset XY plane by specifying plane bounds and location on Z axix");
		service3.setWSDLURL(wsdlURL);
		service3.setInputDataType(Resources.vtkImageData3D);
		service3.setOutputDataType(Resources.vtkImageData2D);
		service3.setAsDimensionFilter();
		
		ModuleOperatorService service4 = packageWriter.createNewOperatorService(null, ModuleRDFRegistration.Resources.vtkExtractVOIYZPlane);
		service4.setInputFormat(Resources.xml);
		service4.setOutputFormat(Resources.xml);
		service4.setLabel(ModuleRDFRegistration.Resources.vtkExtractVOIYZPlane);
		service4.setComment("Extracts a subset XY plane by specifying plane bounds and location on Z axix");
		service4.setWSDLURL(wsdlURL);
		service4.setInputDataType(Resources.vtkImageData3D);
		service4.setOutputDataType(Resources.vtkImageData2D);
		service4.setAsDimensionFilter();
	}
}
