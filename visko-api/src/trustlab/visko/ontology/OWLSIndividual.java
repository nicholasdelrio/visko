package trustlab.visko.ontology;

import java.io.StringWriter;

import org.mindswap.owl.OWLIndividual;

import trustlab.visko.ontology.model.OWLSModel;

public abstract class OWLSIndividual implements ViskoIndividual {
	private String name;
	protected String uri;
	private String fullURL;
	private String fileName;
	private String baseURL;

	protected boolean isForReading;

	protected OWLSModel model;

	public OWLSIndividual(String baseURL, String name, OWLSModel owlsModel) {
		System.out
				.println("asssuming that the object will be used for writing...");

		setURI(baseURL, name);

		model = owlsModel;
		model.createOntology(fullURL);
		model.addImportsToOntology();

		setProperties();
	}

	public OWLSIndividual(String individualURI, OWLSModel owlsModel) {
		System.out
				.println("asssuming that the object will be used for reading...");
		isForReading = true;
		model = owlsModel;
		uri = individualURI;

		setProperties();

		getIndividual();
	}

	public String toString() {
		StringWriter wtr = new StringWriter();
		model.getOntology().write(wtr, model.getOntology().getURI());
		return wtr.toString();
	}

	public String getFileName() {
		return fileName;
	}

	public String getURI() {
		return uri;
	}

	public abstract OWLIndividual getIndividual();

	protected abstract void setProperties();

	protected abstract boolean allFieldsPopulated();

	protected abstract void initializeFields();

	/********************* PRIVATE METHODS *****************************/
	private void setURI(String base, String individualName) {
		baseURL = "http://trust.utep.edu/visko/services/";
		// baseURL = base;
		name = individualName;
		fileName = name + ".owl";
		fullURL = baseURL + fileName;
		uri = baseURL + fileName + "#" + name;
	}
}
