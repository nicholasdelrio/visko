package edu.utep.trustlab.visko.ontology.view.writer;
import edu.utep.trustlab.repository.Repository;
import edu.utep.trustlab.visko.ontology.view.Point;
import edu.utep.trustlab.visko.ontology.writer.ViskoWriter;

public class ViewPointsWriter extends ViskoWriter {
	Point view;
	String label;

	public ViewPointsWriter(String name) {
		view = new Point(Repository.getRepository().getBaseURL(), name, viskoModel);
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