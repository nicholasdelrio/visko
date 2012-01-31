package trustlab.visko.knowledge.latex;

import trustlab.server.Server;
import trustlab.visko.knowledge.NickCIServer;
import trustlab.visko.ontology.service.writer.ServiceWriter;


public class ServiceImpls {
	private final static String wsdlURL = "http://minas.cs.utep.edu:8080/LatexServices/services/LatexTransformers?wsdl";

	public static void main(String[] args) {
		
		Server.setServer(NickCIServer.getServer());
		
		ServiceWriter wtr = new ServiceWriter("tex-to-pdf-service");
		wtr.setWSDLURL(wsdlURL);
		wtr.setLabel("Latex Document to Portable Document Format (PDF)");
		wtr.setConceptualOperator("http://rio.cs.utep.edu/ciserver/ciprojects/viskoOperator/tex-to-pdf.owl#tex-to-pdf");
		wtr.setSupportingToolkit("http://rio.cs.utep.edu/ciserver/ciprojects/viskoOperatorImpl/latex-suite.owl#latex-suite");
		wtr.saveDocument();
	}
}
