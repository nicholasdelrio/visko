package trustlab.visko.ontology.view.writer;
import trustlab.publish.Server;
import trustlab.visko.ontology.view.Graph;
import trustlab.visko.ontology.writer.ViskoWriter;

public class ViewGraphWriter extends ViskoWriter {
	Graph view;
	String label;

	public ViewGraphWriter(String name) {
		view = new Graph(Server.getServer().getBaseURL(), name, viskoModel);
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