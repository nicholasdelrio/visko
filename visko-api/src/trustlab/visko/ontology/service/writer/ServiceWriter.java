package trustlab.visko.ontology.service.writer;
import trustlab.server.Server;
import trustlab.visko.ontology.model.ViskoModel;
import trustlab.visko.ontology.operator.Operator;
import trustlab.visko.ontology.service.OWLSService;
import trustlab.visko.ontology.service.Toolkit;
import trustlab.visko.ontology.writer.ViskoWriter;

public class ServiceWriter extends ViskoWriter {
	Operator implementingOperator;
	Toolkit supportingToolkit;
	String wsdlURL;
	String opName;
	String label;

	OWLSService service;

	ViskoModel readingModel = new ViskoModel();

	public ServiceWriter(String name) {
		service = new OWLSService(Server.getServer().getBaseURL(),name, owlsModel);
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

	public void setConceptualOperator(String operatorURI) {
		implementingOperator = new Operator(operatorURI, readingModel);
	}

	public void setSupportingToolkit(String toolkitURI) {
		supportingToolkit = new Toolkit(toolkitURI, readingModel);
	}

	public String toRDFString() {
		service.setConceptualOperator(implementingOperator);
		service.setSupportingToolkit(supportingToolkit);
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
