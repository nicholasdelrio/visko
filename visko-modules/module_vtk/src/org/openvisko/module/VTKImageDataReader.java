package org.openvisko.module;

import org.openvisko.module.ModuleRDFRegistration.Resources;
import org.openvisko.module.registration.ModuleOperatorService;
import org.openvisko.module.registration.ModuleWriter;

public class VTKImageDataReader {
	
	public static void populateVTKImageDataReaders(String wsdlURL, ModuleWriter packageWriter){
		
		ModuleOperatorService service1 = packageWriter.createNewOperatorService(null, ModuleRDFRegistration.Resources.vtkImageDataReader3DFloats);
		service1.setInputFormat(ModuleRDFRegistration.Resources.littleEndianSequence);
		service1.setOutputFormat(Resources.xml);
		service1.setLabel(ModuleRDFRegistration.Resources.vtkImageDataReader3DFloats);
		service1.setComment("Converts 1D array of float values into 3D vtkImageData of float values");
		service1.setWSDLURL(wsdlURL);
		service1.setInputDataType(Resources.array1DFloat);
		service1.setOutputDataType(Resources.vtkImageData3DFloats);
		
		ModuleOperatorService service2 = packageWriter.createNewOperatorService(null, ModuleRDFRegistration.Resources.vtkImageDataReader3DIntegers);
		service2.setInputFormat(Resources.littleEndianSequence);
		service2.setOutputFormat(Resources.xml);
		service2.setLabel(ModuleRDFRegistration.Resources.vtkImageDataReader3DIntegers);
		service2.setComment("Convert arrays of integer values into vtkImageData of integer values");
		service2.setWSDLURL(wsdlURL);
		service2.setInputDataType(Resources.array1DInteger);
		service2.setOutputDataType(Resources.vtkImageData3DIntegers);

		ModuleOperatorService service3 = packageWriter.createNewOperatorService(null, ModuleRDFRegistration.Resources.vtkImageDataReader3DUnsignedShortIntegers);
		service3.setInputFormat(Resources.littleEndianSequence);
		service3.setOutputFormat(Resources.xml);
		service3.setLabel(ModuleRDFRegistration.Resources.vtkImageDataReader3DUnsignedShortIntegers);
		service3.setComment("Convert array of unsigned short integers into vtkImageData of unsigned short integers values");
		service3.setWSDLURL(wsdlURL);
		service3.setInputDataType(Resources.array1DUnsignedShortInteger);
		service3.setOutputDataType(Resources.vtkImageData3DUnsignedShortIntegers);		
	}
}
