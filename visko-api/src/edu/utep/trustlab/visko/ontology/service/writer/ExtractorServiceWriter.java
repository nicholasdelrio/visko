package edu.utep.trustlab.visko.ontology.service.writer;

import edu.utep.trustlab.publish.Server;
import edu.utep.trustlab.visko.ontology.model.ViskoModel;
import edu.utep.trustlab.visko.ontology.service.Extractor;
import edu.utep.trustlab.visko.ontology.service.OWLSService;
import edu.utep.trustlab.visko.ontology.writer.ViskoWriter;

public class ExtractorServiceWriter extends ViskoWriter {
	Extractor implementingExtractor;
	String wsdlURL;
	String opName;
	String label;

	OWLSService service;

	ViskoModel readingModel = new ViskoModel();

	public ExtractorServiceWriter(String name) {
		service = new OWLSService(Server.getServer().getBaseURL(), name, owlsModel);
	}

	public void setWSDLURL(String wsdlURL) {
		this.wsdlURL = wsdlURL;
	}

	public void setOperationName(String opName) {
		this.opName = opName;
	}

	public void setLabel(String lbl) {
		label = lbl;
	}

	public void setConceptualExtractor(String extractorURI) {
		implementingExtractor = new Extractor(extractorURI, readingModel);
	}

	public String toRDFString() {
		service.setConceptualExtractor(implementingExtractor);
		service.setWSDLURL(wsdlURL);
		service.setOperationName(opName);
		service.setLabel(label);

		// adds the individual to the model and returns a reference to that
		// Individual
		service.getIndividual();
		viskoIndividual = service;

		String viewerRDFString = viskoIndividual.toString();
		return viewerRDFString;
	}
}
