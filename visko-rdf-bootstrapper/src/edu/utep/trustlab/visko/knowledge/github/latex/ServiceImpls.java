package edu.utep.trustlab.visko.knowledge.github.latex;

import edu.utep.trustlab.repository.Repository;
import edu.utep.trustlab.visko.knowledge.NickConfigurations;
import edu.utep.trustlab.visko.ontology.service.writer.ServiceWriter;


public class ServiceImpls {
	private final static String wsdlURL = "http://minas.cs.utep.edu:8080/LatexServices/services/LatexTransformers?wsdl";

	public static void main(String[] args) {
		
		Repository.setRepository(NickConfigurations.getCIServer());
		
		ServiceWriter wtr = new ServiceWriter("tex-to-pdf-service");
		wtr.setWSDLURL(wsdlURL);
		wtr.setLabel("Latex Document to Portable Document Format (PDF)");
		wtr.setConceptualOperator("http://rio.cs.utep.edu/ciserver/ciprojects/viskoOperator/tex-to-pdf.owl#tex-to-pdf");
		wtr.setSupportingToolkit("http://rio.cs.utep.edu/ciserver/ciprojects/viskoOperatorImpl/latex-suite.owl#latex-suite");
		wtr.saveDocument();
	}
}
