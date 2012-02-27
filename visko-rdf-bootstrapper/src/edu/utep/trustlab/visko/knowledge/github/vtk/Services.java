package edu.utep.trustlab.visko.knowledge.github.vtk;

import edu.utep.trustlab.repository.NickConfigurations;
import edu.utep.trustlab.repository.Repository;
import edu.utep.trustlab.visko.ontology.service.writer.ServiceWriter;

public class Services {
	private static final String wsdlURL = "http://iw.cs.utep.edu:8080/VTK-services/services/VTKHoleTransformersUsingParameters.VTKHoleTransformersUsingParametersPort?wsdl";

	public static void main(String[] args) {
		Repository.setRepository(NickConfigurations.getLocalFileSystem());
		String operationName;

		operationName = "Int2Short";
		ServiceWriter wtr = new ServiceWriter(operationName + "Service");
		wtr.setWSDLURL(wsdlURL);
		wtr.setLabel(operationName);
		wtr.setOperationName(operationName);
		wtr.setConceptualOperator("https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/intToShortIntOperator.owl#intToShortIntOperator");
		wtr.setSupportingToolkit("https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/vtk.owl#vtk");
		wtr.saveDocument();

		operationName = "Float2ShortThr";
		ServiceWriter wtr0 = new ServiceWriter(operationName + "Service");
		wtr0.setWSDLURL(wsdlURL);
		wtr0.setLabel(operationName);
		wtr0.setOperationName(operationName);
		wtr0.setConceptualOperator("https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/floatArrayToShortIntArray.owl#floatArrayToShortIntArray");
		wtr0.setSupportingToolkit("https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/vtk.owl#vtk");
		wtr0.saveDocument();
		
		operationName = "vtkImageDataReader";
		ServiceWriter wtr1 = new ServiceWriter(operationName + "Service");
		wtr1.setWSDLURL(wsdlURL);
		wtr1.setLabel(operationName);
		wtr1.setOperationName(operationName);
		wtr1.setConceptualOperator("https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/shortIntArrayToGriddedData.owl#shortIntArrayToGriddedData");
		wtr1.setSupportingToolkit("https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/vtk.owl#vtk");
		wtr1.saveDocument();
		
		operationName = "vtkContourFilter";
		ServiceWriter wtr3 = new ServiceWriter(operationName + "Service");
		wtr3.setWSDLURL(wsdlURL);
		wtr3.setLabel(operationName);
		wtr3.setOperationName(operationName);
		wtr3.setConceptualOperator("https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/contourer3D.owl#contourer3D");
		wtr3.setSupportingToolkit("https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/vtk.owl#vtk");
		wtr3.saveDocument();
		
		operationName = "vtkPolyDataMapper";
		ServiceWriter wtr5 = new ServiceWriter(operationName + "Service");
		wtr5.setWSDLURL(wsdlURL);
		wtr5.setLabel(operationName);
		wtr5.setOperationName(operationName);
		wtr5.setConceptualOperator("https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/contoursPolyDataToImageOperator.owl#contoursPolyDataToImageOperator");
		wtr5.setSupportingToolkit("https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/vtk.owl#vtk");
		wtr5.saveDocument();
		
		operationName = "vtkVolume";
		ServiceWriter wtr6 = new ServiceWriter(operationName + "Service");
		wtr6.setWSDLURL(wsdlURL);
		wtr6.setLabel(operationName);
		wtr6.setOperationName(operationName);
		wtr6.setConceptualOperator("https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/volume-generator.owl#volume-generator");
		wtr6.setSupportingToolkit("https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/vtk.owl#vtk");
		wtr6.saveDocument();
		
		operationName = "vtkImageDataReaderFloat";
		ServiceWriter wtr7 = new ServiceWriter(operationName + "Service");
		wtr7.setWSDLURL(wsdlURL);
		wtr7.setLabel(operationName);
		wtr7.setOperationName(operationName);
		wtr7.setConceptualOperator("https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/floatArrayToGriddedData.owl#floatArrayToGriddedData");
		wtr7.setSupportingToolkit("https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/vtk.owl#vtk");
		wtr7.saveDocument();
	}
}