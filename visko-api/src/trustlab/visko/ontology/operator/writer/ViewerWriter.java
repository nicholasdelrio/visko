package trustlab.visko.ontology.operator.writer;

import java.util.Vector;

import trustlab.server.Server;
import trustlab.visko.ontology.model.ViskoModel;
import trustlab.visko.ontology.operator.*;
import trustlab.visko.ontology.pmlp.Format;
import trustlab.visko.ontology.writer.ViskoWriter;

public class ViewerWriter extends ViskoWriter {
	String visualizationParameterComment;
	String visualizationParameterLabel;
	Vector<Format> formats;
	Vector<ViewerSet> partOfSets;
	Viewer viskoViewer;

	ViskoModel readingModel = new ViskoModel();

	public ViewerWriter(String name) {
		viskoViewer = new Viewer(Server.getServer().getBaseURL(), name, viskoModel);
		formats = new Vector<Format>();
		partOfSets = new Vector<ViewerSet>();
	}

	public void addPartOfSetURI(String setURI) {
		ViewerSet set = new ViewerSet(setURI, readingModel);
		partOfSets.add(set);
	}

	public void setLabel(String label) {
		this.visualizationParameterLabel = label;
	}

	public void setViewerComment(String comment) {
		this.visualizationParameterComment = comment;
	}

	public void addFormatURI(String formatURI) {
		Format fmt = new Format(formatURI, readingModel);
		formats.add(fmt);
	}

	public String toRDFString() {
		viskoViewer.setBelongsToViewerSets(partOfSets);
		viskoViewer.setLabel(visualizationParameterLabel);
		viskoViewer.setComment(visualizationParameterComment);
		viskoViewer.setOperatesOnFormats(formats);

		// adds the individual to the model and returns a reference to that
		// Individual
		viskoViewer.getIndividual();
		this.viskoIndividual = viskoViewer;

		String viewerRDFString = viskoIndividual.toString();
		return viewerRDFString;
	}
}
