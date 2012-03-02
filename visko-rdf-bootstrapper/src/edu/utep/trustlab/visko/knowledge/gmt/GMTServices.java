package edu.utep.trustlab.visko.knowledge.gmt;

import edu.utep.trustlab.visko.ontology.service.writer.ServiceWriter;

public class GMTServices {
	private static final String wsdlURL = "http://iw.cs.utep.edu:8080/GMT-services/services/GMTGravityTransformersUsingParameters.GMTGravityTransformersUsingParametersPort?wsdl";

	public static void create() {
		String operationName = "ESRIGriddedToContourMapPS";

		ServiceWriter wtr = new ServiceWriter(operationName);
		wtr.setLabel(operationName);
		wtr.setOperationName(operationName);
		wtr.setWSDLURL(wsdlURL);
		wtr.setConceptualOperator("https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/contourer.owl#contourer");
		wtr.setSupportingToolkit("https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/gmt.owl#gmt");
		wtr.saveDocument();

		operationName = "GravityASCIIPointsToMinCurvatureESRIGridded";
		ServiceWriter wtr1 = new ServiceWriter(operationName);
		wtr1.setWSDLURL(wsdlURL);
		wtr1.setLabel(operationName);
		wtr1.setOperationName(operationName);
		wtr1.setConceptualOperator("https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/gridder.owl#gridder");
		wtr1.setSupportingToolkit("https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/gmt.owl#gmt");
		wtr1.saveDocument();

		operationName = "GravityASCIIPointsToNearNeightborESRIGridded";
		ServiceWriter wtr4 = new ServiceWriter(operationName);
		wtr4.setWSDLURL(wsdlURL);
		wtr4.setLabel(operationName);
		wtr4.setOperationName(operationName);
		wtr4.setConceptualOperator("https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/gridder.owl#gridder");
		wtr4.setSupportingToolkit("https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/gmt.owl#gmt");
		wtr4.saveDocument();

		operationName = "GravityASCIIPointsTo2DPlotPS";
		ServiceWriter wtr2 = new ServiceWriter(operationName);
		wtr2.setOperationName(operationName);
		wtr2.setWSDLURL(wsdlURL);
		wtr2.setOperationName(operationName);
		wtr2.setLabel(operationName);
		wtr2.setConceptualOperator("https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/plotter.owl#plotter");
		wtr2.setSupportingToolkit("https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/gmt.owl#gmt");
		wtr2.saveDocument();

		operationName = "ESRIGriddedToColoredImagePS";
		ServiceWriter wtr3 = new ServiceWriter(operationName);
		wtr3.setOperationName(operationName);
		wtr3.setWSDLURL(wsdlURL);
		wtr3.setLabel(operationName);
		wtr3.setConceptualOperator("https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/rasterer.owl#rasterer");
		wtr3.setSupportingToolkit("https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/gmt.owl#gmt");
		wtr3.saveDocument();

		operationName = "CSVToTabularASCII";
		ServiceWriter wtr5 = new ServiceWriter(operationName);
		wtr5.setOperationName(operationName);
		wtr5.setWSDLURL(wsdlURL);
		wtr5.setLabel(operationName);
		wtr5.setConceptualOperator("https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/csv-to-tabular-ascii.owl#csv-to-tabular-ascii");
		wtr5.setSupportingToolkit("https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/gmt.owl#gmt");
		wtr5.saveDocument();
	}
}
