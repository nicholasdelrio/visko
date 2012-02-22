package edu.utep.trustlab.visko.knowledge.github.ncl;

import edu.utep.trustlab.repository.Repository;
import edu.utep.trustlab.visko.knowledge.NickConfigurations;
import edu.utep.trustlab.visko.ontology.service.writer.ServiceWriter;

public class Services {
	private static final String wsdlURL = "http://iw.cs.utep.edu:8080/NCL-services/services/NCLTransformers.NCLTransformersPort?wsdl";

	public static void main(String[] args) {
		Repository.setRepository(NickConfigurations.getLocalFileSystem());

		String operationName = "esriGridContour";
		ServiceWriter wtr = new ServiceWriter(operationName);
		wtr.setLabel(operationName);
		wtr.setOperationName(operationName);
		wtr.setWSDLURL(wsdlURL);
		wtr.setConceptualOperator("https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/contourer.owl#contourer");
		wtr.setSupportingToolkit("https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/ncl.owl#ncl");
		wtr.saveDocument();

		operationName = "esriGridRaster";
		ServiceWriter wtr1 = new ServiceWriter(operationName);
		wtr1.setLabel(operationName);
		wtr1.setOperationName(operationName);
		wtr1.setWSDLURL(wsdlURL);
		wtr1.setConceptualOperator("https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/rasterer.owl#rasterer");
		wtr1.setSupportingToolkit("https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/ncl.owl#ncl");
		wtr1.saveDocument();

		operationName = "netCDFGridContour";
		ServiceWriter wtr2 = new ServiceWriter(operationName);
		wtr2.setLabel(operationName);
		wtr2.setOperationName(operationName);
		wtr2.setWSDLURL(wsdlURL);
		wtr2.setConceptualOperator("https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/netCDFContourer.owl#netCDFContourer");
		wtr2.setSupportingToolkit("https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/ncl.owl#ncl");
		wtr2.saveDocument();

		operationName = "netCDFGridRaster";
		ServiceWriter wtr3 = new ServiceWriter(operationName);
		wtr3.setLabel(operationName);
		wtr3.setOperationName(operationName);
		wtr3.setWSDLURL(wsdlURL);
		wtr3.setConceptualOperator("https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/netCDFRasterer.owl#netCDFRasterer");
		wtr3.setSupportingToolkit("https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/ncl.owl#ncl");
		wtr3.saveDocument();

		operationName = "netCDFTimeSeries";
		ServiceWriter wtr4 = new ServiceWriter(operationName);
		wtr4.setLabel(operationName);
		wtr4.setOperationName(operationName);
		wtr4.setWSDLURL(wsdlURL);
		wtr4.setConceptualOperator("https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/netCDFTimeSeriesPlotter.owl#netCDFTimeSeriesPlotter");
		wtr4.setSupportingToolkit("https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/ncl.owl#ncl");
		wtr4.saveDocument();
	}
}