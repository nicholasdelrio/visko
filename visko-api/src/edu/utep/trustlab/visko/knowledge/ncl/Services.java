package edu.utep.trustlab.visko.knowledge.ncl;


import edu.utep.trustlab.repository.Repository;
import edu.utep.trustlab.visko.knowledge.NickConfigurations;
import edu.utep.trustlab.visko.ontology.service.writer.ServiceWriter;

public class Services {
	private static final String wsdlURL = "http://iw.cs.utep.edu:8080/NCL-services/services/NCLTransformers.NCLTransformersPort?wsdl";

	public static void main(String[] args) {
		Repository.setRepository(NickConfigurations.getCIServer());

		String operationName = "esriGridContour";

		/*
		 * ServiceWriter wtr = new ServiceWriter(operationName);
		 * wtr.setLabel(operationName); wtr.setOperationName(operationName);
		 * wtr.setWSDLURL(wsdlURL); wtr.setImplementingOperator(
		 * "http://rio.cs.utep.edu/ciserver/ciprojects/visko/contour1.owl#contour1"
		 * ); wtr.setSupportingToolkit(
		 * "http://rio.cs.utep.edu/ciserver/ciprojects/visko/ncl.owl#ncl");
		 * wtr.dumpViskoDocument();
		 * 
		 * operationName = "esriGridRaster"; ServiceWriter wtr1 = new
		 * ServiceWriter(operationName); wtr1.setLabel(operationName);
		 * wtr1.setOperationName(operationName); wtr1.setWSDLURL(wsdlURL);
		 * wtr1.setImplementingOperator(
		 * "http://rio.cs.utep.edu/ciserver/ciprojects/visko/grid-to-colored-image1.owl#grid-to-colored-image1"
		 * ); wtr1.setSupportingToolkit(
		 * "http://rio.cs.utep.edu/ciserver/ciprojects/visko/ncl.owl#ncl");
		 * wtr1.dumpViskoDocument();
		 * 
		 * operationName = "netCDFGridContour"; ServiceWriter wtr2 = new
		 * ServiceWriter(operationName); wtr2.setLabel(operationName);
		 * wtr2.setOperationName(operationName); wtr2.setWSDLURL(wsdlURL);
		 * wtr2.setImplementingOperator
		 * ("http://rio.cs.utep.edu/ciserver/ciprojects/visko/contour2.owl#contour2"
		 * ); wtr2.setSupportingToolkit(
		 * "http://rio.cs.utep.edu/ciserver/ciprojects/visko/ncl.owl#ncl");
		 * wtr2.dumpViskoDocument();
		 * 
		 * 
		 * operationName = "netCDFGridRaster"; ServiceWriter wtr3 = new
		 * ServiceWriter(operationName); wtr3.setLabel(operationName);
		 * wtr3.setOperationName(operationName); wtr3.setWSDLURL(wsdlURL);
		 * wtr3.setImplementingOperator(
		 * "http://rio.cs.utep.edu/ciserver/ciprojects/visko/rasterMap.owl#rasterMap"
		 * ); wtr3.setSupportingToolkit(
		 * "http://rio.cs.utep.edu/ciserver/ciprojects/visko/ncl.owl#ncl");
		 * wtr3.dumpViskoDocument();
		 */

		operationName = "netCDFTimeSeries";
		ServiceWriter wtr4 = new ServiceWriter(operationName);
		wtr4.setLabel(operationName);
		wtr4.setOperationName(operationName);
		wtr4.setWSDLURL(wsdlURL);
		wtr4.setConceptualOperator("http://rio.cs.utep.edu/ciserver/ciprojects/visko/timeSeriesPlot.owl#timeSeriesPlot");
		wtr4.setSupportingToolkit("http://rio.cs.utep.edu/ciserver/ciprojects/visko/ncl.owl#ncl");
		wtr4.saveDocument();

	}
}
