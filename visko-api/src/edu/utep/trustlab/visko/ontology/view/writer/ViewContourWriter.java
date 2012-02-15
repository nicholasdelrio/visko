package edu.utep.trustlab.visko.ontology.view.writer;
import edu.utep.trustlab.publish.Repository;
import edu.utep.trustlab.visko.ontology.view.Contour;
import edu.utep.trustlab.visko.ontology.writer.ViskoWriter;

public class ViewContourWriter extends ViskoWriter {
	Contour view;
	String label;

	public ViewContourWriter(String name) {
		view = new Contour(Repository.getServer().getBaseURL(), name,
				viskoModel);
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
