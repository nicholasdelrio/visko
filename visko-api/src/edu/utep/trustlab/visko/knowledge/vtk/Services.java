package edu.utep.trustlab.visko.knowledge.vtk;


import edu.utep.trustlab.publish.Server;
import edu.utep.trustlab.visko.knowledge.NickCIServer;
import edu.utep.trustlab.visko.ontology.service.writer.ServiceWriter;

public class Services {
	private static final String wsdlURL = "http://iw.cs.utep.edu:8080/VTK-services/services/VTKHoleTransformersUsingParameters.VTKHoleTransformersUsingParametersPort?wsdl";

	public static void main(String[] args) {
		Server.setServer(NickCIServer.getServer());		String operationName;
		/*
		 * 
		 * operationName = "Int2Short"; ServiceWriter wtr = new
		 * ServiceWriter(operationName + "Service"); wtr.setWSDLURL(wsdlURL);
		 * wtr.setLabel(operationName); wtr.setOperationName(operationName);
		 * wtr.setConceptualOperator(
		 * "http://rio.cs.utep.edu/ciserver/ciprojects/visko/intToShortIntOperator.owl#intToShortIntOperator"
		 * ); wtr.setSupportingToolkit(
		 * "http://rio.cs.utep.edu/ciserver/ciprojects/visko/vtk1.owl#vtk1");
		 * wtr.dumpViskoDocumentOnCIServer();
		 * 
		 * operationName = "Float2ShortThr"; ServiceWriter wtr = new
		 * ServiceWriter(operationName + "Service"); wtr.setWSDLURL(wsdlURL);
		 * wtr.setLabel(operationName); wtr.setOperationName(operationName);
		 * wtr.setConceptualOperator(
		 * "http://rio.cs.utep.edu/ciserver/ciprojects/visko/floatToShortThrOperator.owl#floatToShortThrOperator"
		 * ); wtr.setSupportingToolkit(
		 * "http://rio.cs.utep.edu/ciserver/ciprojects/visko/vtk1.owl#vtk1");
		 * wtr.dumpViskoDocumentOnCIServer();
		 * 
		 * operationName = "vtkImageDataReader"; ServiceWriter wtr1 = new
		 * ServiceWriter(operationName + "Service"); wtr1.setWSDLURL(wsdlURL);
		 * wtr1.setLabel(operationName); wtr1.setOperationName(operationName);
		 * wtr1.setConceptualOperator(
		 * "http://rio.cs.utep.edu/ciserver/ciprojects/visko/shortIntToImageDataOperator.owl#shortIntToImageDataOperator"
		 * ); wtr1.setSupportingToolkit(
		 * "http://rio.cs.utep.edu/ciserver/ciprojects/visko/vtk1.owl#vtk1");
		 * wtr1.dumpViskoDocumentOnCIServer();
		 * 
		 * operationName = "vtkContourFilter"; ServiceWriter wtr3 = new
		 * ServiceWriter(operationName + "Service"); wtr3.setWSDLURL(wsdlURL);
		 * wtr3.setLabel(operationName); wtr3.setOperationName(operationName);
		 * wtr3.setConceptualOperator(
		 * "http://rio.cs.utep.edu/ciserver/ciprojects/visko/imageDataToContoursPolyDataOperator.owl#imageDataToContoursPolyDataOperator"
		 * ); wtr3.setSupportingToolkit(
		 * "http://rio.cs.utep.edu/ciserver/ciprojects/visko/vtk1.owl#vtk1");
		 * wtr3.dumpViskoDocumentOnCIServer();
		 * 
		 * operationName = "vtkPolyDataMapper"; ServiceWriter wtr5 = new
		 * ServiceWriter(operationName + "Service"); wtr5.setWSDLURL(wsdlURL);
		 * wtr5.setLabel(operationName); wtr5.setOperationName(operationName);
		 * wtr5.setConceptualOperator(
		 * "http://rio.cs.utep.edu/ciserver/ciprojects/visko/contoursPolyDataToImageOperator.owl#contoursPolyDataToImageOperator"
		 * ); wtr5.setSupportingToolkit(
		 * "http://rio.cs.utep.edu/ciserver/ciprojects/visko/vtk1.owl#vtk1");
		 * wtr5.dumpViskoDocumentOnCIServer();
		 */

		operationName = "vtkVolume";
		ServiceWriter wtr6 = new ServiceWriter(operationName + "Service");
		wtr6.setWSDLURL(wsdlURL);
		wtr6.setLabel(operationName);
		wtr6.setOperationName(operationName);
		wtr6.setConceptualOperator("http://rio.cs.utep.edu/ciserver/ciprojects/visko/imageDataToVolumeOperator.owl#imageDataToVolumeOperator");
		wtr6.setSupportingToolkit("http://rio.cs.utep.edu/ciserver/ciprojects/visko/vtk1.owl#vtk1");
		wtr6.saveDocument();
	}
}
