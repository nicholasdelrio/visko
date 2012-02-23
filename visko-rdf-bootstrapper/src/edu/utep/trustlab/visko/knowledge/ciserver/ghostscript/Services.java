package edu.utep.trustlab.visko.knowledge.ciserver.ghostscript;

import edu.utep.trustlab.repository.NickConfigurations;
import edu.utep.trustlab.repository.Repository;
import edu.utep.trustlab.visko.ontology.service.writer.ServiceWriter;

public class Services {
	private static final String wsdlURL = "http://iw.cs.utep.edu:8080/GMT-services/services/GMTGravityTransformersUsingParameters.GMTGravityTransformersUsingParametersPort?wsdl";

	public static void main(String[] args) {
		
		Repository.setRepository(NickConfigurations.getCIServer());
		
		String operationName = "PSToPDF";
		ServiceWriter wtr1 = new ServiceWriter(operationName + 1);
		wtr1.setWSDLURL(wsdlURL);
		wtr1.setOperationName(operationName);
		wtr1.setLabel("Adobe PostScript to Portable Document Format");
		wtr1.setConceptualOperator("http://rio.cs.utep.edu/ciserver/ciprojects/visko/ps-to-pdf1.owl#ps-to-pdf1");
		wtr1.setSupportingToolkit("http://rio.cs.utep.edu/ciserver/ciprojects/visko/ghostscript1.owl#ghostscript1");
		wtr1.saveDocument();

		operationName = "PSFToPNG";
		ServiceWriter wtr2 = new ServiceWriter(operationName + 1);
		wtr2.setWSDLURL(wsdlURL);
		wtr2.setOperationName(operationName);
		wtr2.setLabel("Adobe PostScript to Portable Network Graphic");
		wtr2.setConceptualOperator("http://rio.cs.utep.edu/ciserver/ciprojects/visko/ps-to-png1.owl#ps-to-png1");
		wtr2.setSupportingToolkit("http://rio.cs.utep.edu/ciserver/ciprojects/visko/ghostscript1.owl#ghostscript1");
		wtr2.saveDocument();

		operationName = "PDFToPNG";
		ServiceWriter wtr3 = new ServiceWriter(operationName + 1);
		wtr3.setWSDLURL(wsdlURL);
		wtr3.setOperationName(operationName);
		wtr3.setLabel("Adobe Portable Document Format to Portable Network Graphic");
		wtr3.setConceptualOperator("http://rio.cs.utep.edu/ciserver/ciprojects/visko/pdf-to-png1.owl#pdf-to-png1");
		wtr3.setSupportingToolkit("http://rio.cs.utep.edu/ciserver/ciprojects/visko/ghostscript1.owl#ghostscript1");
		wtr3.saveDocument();
	}
}
