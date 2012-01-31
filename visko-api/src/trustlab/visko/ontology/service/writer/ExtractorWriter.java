package trustlab.visko.ontology.service.writer;

import java.util.Vector;

import trustlab.server.Server;
import trustlab.visko.ontology.model.ViskoModel;
import trustlab.visko.ontology.pmlp.Format;
import trustlab.visko.ontology.service.Extractor;
import trustlab.visko.ontology.writer.ViskoWriter;

public class ExtractorWriter extends ViskoWriter {
	private Extractor extractor;
	private Vector<String> dataTypes;
	private Format format;
	private String profileURI;
	private ViskoModel loadingModel;

	public ExtractorWriter(String name) {
		loadingModel = new ViskoModel();

		extractor = new Extractor(Server.getServer().getBaseURL(),
				name, viskoModel);
		dataTypes = new Vector<String>();
	}

	public void setFormat(String formatURI) {
		format = new Format(formatURI, loadingModel);
	}

	public void setProfileURI(String profileURI) {
		this.profileURI = profileURI;
	}

	public void addDataType(String uri) {
		dataTypes.add(uri);
	}

	public Vector<String> getDataTypes() {
		return dataTypes;
	}

	public String toRDFString() {
		for (String type : dataTypes)
			extractor.addDataTypeToExtractFrom(type);

		extractor.setFormat(format);
		extractor.setProfileURI(profileURI);
		// adds the individual to the model and returns a reference to that
		// Individual
		extractor.getIndividual();
		viskoIndividual = extractor;

		String viewerRDFString = viskoIndividual.toString();
		return viewerRDFString;
	}
}
