package edu.utep.trustlab.visko.ontology.view.writer;
import edu.utep.trustlab.publish.Repository;
import edu.utep.trustlab.visko.ontology.view.Graph;
import edu.utep.trustlab.visko.ontology.writer.ViskoWriter;

public class ViewGraphWriter extends ViskoWriter {
	Graph view;
	String label;

	public ViewGraphWriter(String name) {
		view = new Graph(Repository.getServer().getBaseURL(), name, viskoModel);
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