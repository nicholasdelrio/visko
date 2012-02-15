package edu.utep.trustlab.visko.ontology.service.writer;

import edu.utep.trustlab.repository.Repository;
import edu.utep.trustlab.visko.ontology.service.Toolkit;
import edu.utep.trustlab.visko.ontology.writer.ViskoWriter;

public class ToolkitWriter extends ViskoWriter {
	Toolkit tk;
	String label;

	public ToolkitWriter(String name) {
		tk = new Toolkit(Repository.getServer().getBaseURL(), name, viskoModel);
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
