package edu.utep.trustlab.visko.knowledge.ghostscript;

import edu.utep.trustlab.visko.ontology.service.writer.ServiceWriter;

public class GSServices {
	private static final String wsdlURL = "http://iw.cs.utep.edu:8080/GMT-services/services/GMTGravityTransformersUsingParameters.GMTGravityTransformersUsingParametersPort?wsdl";

	public static void create() {
		String operationName = "PSToPDF";
		
		/*
		ServiceWriter wtr1 = new ServiceWriter(operationName);
		wtr1.setWSDLURL(wsdlURL);
		wtr1.setOperationName(operationName);
		wtr1.setLabel("Adobe PostScript to Portable Document Format");
		wtr1.setConceptualOperator("https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/ps-to-pdf.owl#ps-to-pdf");
		wtr1.setSupportingToolkit("https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/ghostscript.owl#ghostscript");
		wtr1.saveDocument();

		operationName = "PSFToPNG";
		ServiceWriter wtr2 = new ServiceWriter(operationName);
		wtr2.setWSDLURL(wsdlURL);
		wtr2.setOperationName(operationName);
		wtr2.setLabel("Adobe PostScript to Portable Network Graphic");
		wtr2.setConceptualOperator("https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/ps-to-png.owl#ps-to-png");
		wtr2.setSupportingToolkit("https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/ghostscript.owl#ghostscript");
		wtr2.saveDocument();*/

		operationName = "PDFToPNG";
		ServiceWriter wtr3 = new ServiceWriter(operationName);
		wtr3.setWSDLURL(wsdlURL);
		wtr3.setOperationName(operationName);
		wtr3.setLabel("Adobe Portable Document Format to Portable Network Graphic");
		wtr3.setConceptualOperator("https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/pdf-to-png.owl#pdf-to-png");
		wtr3.setSupportingToolkit("https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/ghostscript.owl#ghostscript");
		System.out.println(wtr3.saveDocument());
	}
}