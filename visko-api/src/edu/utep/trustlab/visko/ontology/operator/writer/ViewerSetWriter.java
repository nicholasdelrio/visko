package edu.utep.trustlab.visko.ontology.operator.writer;

import edu.utep.trustlab.publish.Repository;
import edu.utep.trustlab.visko.ontology.operator.ViewerSet;
import edu.utep.trustlab.visko.ontology.writer.ViskoWriter;

public class ViewerSetWriter extends ViskoWriter {
	String label;
	ViewerSet viskoVS;

	public ViewerSetWriter(String name) {
		viskoVS = new ViewerSet(Repository.getServer().getBaseURL(), name, viskoModel);
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String toRDFString() {
		viskoVS.setLabel(label);

		// adds the individual to the model and returns a reference to that
		// Individual
		viskoVS.getIndividual();
		viskoIndividual = viskoVS;

		String viewerRDFString = viskoIndividual.toString();
		return viewerRDFString;
	}
}
