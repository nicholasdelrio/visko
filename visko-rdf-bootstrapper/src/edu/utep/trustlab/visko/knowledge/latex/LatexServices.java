package edu.utep.trustlab.visko.knowledge.latex;
import edu.utep.trustlab.visko.ontology.service.writer.ServiceWriter;


public class LatexServices {
	private final static String wsdlURL = "http://minas.cs.utep.edu:8080/LatexServices/services/LatexTransformers?wsdl";

	public static void create() {
		ServiceWriter wtr = new ServiceWriter("tex-to-pdf-service");
		wtr.setWSDLURL(wsdlURL);
		wtr.setLabel("Latex Document to Portable Document Format (PDF)");
		wtr.setConceptualOperator("https://raw.github.com/nicholasdelrio/visko/master/visko-rdfOperator/tex-to-pdf.owl#tex-to-pdf");
		wtr.setSupportingToolkit("https://raw.github.com/nicholasdelrio/visko/master/visko-rdfOperatorImpl/latex-suite.owl#latex-suite");
		wtr.saveDocument();
	}
}
