package edu.utep.trustlab.visko.knowledge.gmt;

import edu.utep.trustlab.repository.Repository;
import edu.utep.trustlab.visko.knowledge.NickCIServer;
import edu.utep.trustlab.visko.ontology.service.writer.ServiceWriter;

public class Services {
	private static final String wsdlURL = "http://iw.cs.utep.edu:8080/GMT-services/services/GMTGravityTransformersUsingParameters.GMTGravityTransformersUsingParametersPort?wsdl";

	public static void main(String[] args) {
		Repository.setServer(NickCIServer.getServer());

		String operationName = "ESRIGriddedToContourMapPS";

		/*
		 * ServiceWriter wtr = new ServiceWriter(operationName + 1);
		 * wtr.setLabel(operationName); wtr.setOperationName(operationName);
		 * wtr.setWSDLURL(wsdlURL); wtr.setConceptualOperator(
		 * "http://rio.cs.utep.edu/ciserver/ciprojects/visko/contour1.owl#contour1"
		 * ); wtr.setSupportingToolkit(
		 * "http://rio.cs.utep.edu/ciserver/ciprojects/visko/gmt1.owl#gmt1");
		 * wtr.dumpViskoDocumentOnCIServer();
		 * 
		 * operationName = "GravityASCIIPointsToMinCurvatureESRIGridded";
		 * ServiceWriter wtr1 = new ServiceWriter(operationName + 1);
		 * wtr1.setWSDLURL(wsdlURL); wtr1.setLabel(operationName);
		 * wtr1.setOperationName(operationName); wtr1.setConceptualOperator(
		 * "http://rio.cs.utep.edu/ciserver/ciprojects/visko/grid-by-surface1.owl#grid-by-surface1"
		 * ); wtr1.setSupportingToolkit(
		 * "http://rio.cs.utep.edu/ciserver/ciprojects/visko/gmt1.owl#gmt1");
		 * wtr1.dumpViskoDocumentOnCIServer();
		 * 
		 * operationName = "GravityASCIIPointsToNearNeightborESRIGridded";
		 * ServiceWriter wtr4 = new ServiceWriter(operationName + 1);
		 * wtr4.setWSDLURL(wsdlURL); wtr4.setLabel(operationName);
		 * wtr4.setOperationName(operationName); wtr4.setConceptualOperator(
		 * "http://rio.cs.utep.edu/ciserver/ciprojects/visko/grid-by-near-neighbor1.owl#grid-by-near-neighbor1"
		 * ); wtr4.setSupportingToolkit(
		 * "http://rio.cs.utep.edu/ciserver/ciprojects/visko/gmt1.owl#gmt1");
		 * wtr4.dumpViskoDocumentOnCIServer();
		 * 
		 * operationName = "GravityASCIIPointsTo2DPlotPS"; ServiceWriter wtr2 =
		 * new ServiceWriter(operationName + 1);
		 * wtr2.setOperationName(operationName); wtr2.setWSDLURL(wsdlURL);
		 * wtr2.setOperationName(operationName); wtr2.setLabel(operationName);
		 * wtr2.setConceptualOperator(
		 * "http://rio.cs.utep.edu/ciserver/ciprojects/visko/plotter1.owl#plotter1"
		 * ); wtr2.setSupportingToolkit(
		 * "http://rio.cs.utep.edu/ciserver/ciprojects/visko/gmt1.owl#gmt1");
		 * wtr2.dumpViskoDocumentOnCIServer();
		 * 
		 * operationName = "ESRIGriddedToColoredImagePS"; ServiceWriter wtr3 =
		 * new ServiceWriter(operationName + 1);
		 * wtr3.setOperationName(operationName); wtr3.setWSDLURL(wsdlURL);
		 * wtr3.setLabel(operationName); wtr3.setConceptualOperator(
		 * "http://rio.cs.utep.edu/ciserver/ciprojects/visko/grid-to-colored-image1.owl#grid-to-colored-image1"
		 * ); wtr3.setSupportingToolkit(
		 * "http://rio.cs.utep.edu/ciserver/ciprojects/visko/gmt1.owl#gmt1");
		 * wtr3.dumpViskoDocumentOnCIServer();
		 */
		operationName = "CSVToTabularASCII";
		ServiceWriter wtr5 = new ServiceWriter(operationName);
		wtr5.setOperationName(operationName);
		wtr5.setWSDLURL(wsdlURL);
		wtr5.setLabel(operationName);
		wtr5.setConceptualOperator("http://rio.cs.utep.edu/ciserver/ciprojects/visko/csv-to-tabular-ascii.owl#csv-to-tabular-ascii");
		wtr5.setSupportingToolkit("http://rio.cs.utep.edu/ciserver/ciprojects/visko/gmt1.owl#gmt1");
		wtr5.saveDocument();
	}
}
