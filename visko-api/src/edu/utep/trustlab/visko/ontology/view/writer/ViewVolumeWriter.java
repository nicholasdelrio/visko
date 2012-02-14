package edu.utep.trustlab.visko.ontology.view.writer;

import edu.utep.trustlab.publish.Server;
import edu.utep.trustlab.visko.ontology.view.Volume;
import edu.utep.trustlab.visko.ontology.writer.ViskoWriter;

public class ViewVolumeWriter extends ViskoWriter {
	Volume view;
	String label;

	public ViewVolumeWriter(String name) {
		view = new Volume(Server.getServer().getBaseURL(), name, viskoModel);
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String toRDFString() {
		view.setLabel(label);
		// adds the individual to the model and returns a reference to that
		// Individual
		view.getIndividual();
		viskoIndividual = view;

		String viewerRDFString = viskoIndividual.toString();
		return viewerRDFString;
	}
}
