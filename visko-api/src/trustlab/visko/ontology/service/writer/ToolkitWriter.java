package trustlab.visko.ontology.service.writer;

import trustlab.publish.Server;
import trustlab.visko.ontology.service.Toolkit;
import trustlab.visko.ontology.writer.ViskoWriter;

public class ToolkitWriter extends ViskoWriter {
	Toolkit tk;
	String label;

	public ToolkitWriter(String name) {
		tk = new Toolkit(Server.getServer().getBaseURL(), name, viskoModel);
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String toRDFString() {
		tk.setLabel(label);
		// adds the individual to the model and returns a reference to that
		tk.getIndividual();
		viskoIndividual = tk;

		String viewerRDFString = viskoIndividual.toString();
		return viewerRDFString;
	}
}
