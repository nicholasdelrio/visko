package trustlab.visko.ontology.service;

import java.util.Vector;

import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.RDFNode;

import trustlab.visko.ontology.JenaIndividual;
import trustlab.visko.ontology.model.ViskoModel;
import trustlab.visko.ontology.pmlp.Format;
import trustlab.visko.ontology.vocabulary.ViskoS;
import trustlab.visko.ontology.vocabulary.XSD;

public class Extractor extends JenaIndividual {

	private String profileURI; // will be used in future when pipeline executor
								// is described in visko ontology
	private Vector<String> dataTypesToExtractFrom;
	private Format format;

	// datatype properties
	private DatatypeProperty extractsFromDataOfTypeProperty;
	private DatatypeProperty createsProfileProperty;
	private ObjectProperty extractsFromFormatProperty;

	public Extractor(String baseURL, String name, ViskoModel viskoModel) {
		super(ViskoS.CLASS_URI_EXTRACTOR, baseURL, name, viskoModel);
	}

	public Extractor(String uri, ViskoModel viskoModel) {
		super(uri, viskoModel);
	}

	public void setDataTypesToExtractFrom(Vector<String> dataTypeURIs) {
		if (dataTypeURIs != null)
			dataTypesToExtractFrom = dataTypeURIs;
	}

	public void setProfileURI(String profileURI) {
		this.profileURI = profileURI;
	}

	public String getProfileURI() {
		return profileURI;
	}

	public void setFormat(Format fmt) {
		format = fmt;
	}

	public void addDataTypeToExtractFrom(String dataTypeURI) {
		if (dataTypeURI != null)
			dataTypesToExtractFrom.add(dataTypeURI);
	}

	public Vector<String> getDataTypesToExtractFrom() {
		return dataTypesToExtractFrom;
	}

	private void addExtractsFromDataOfTypeProperty(Individual subjectInd) {
		for (String type : dataTypesToExtractFrom) {
			Literal dataType = model.createTypedLiteral(type,
					XSD.TYPE_URI_ANYURI);
			subjectInd.addProperty(extractsFromDataOfTypeProperty, dataType);
		}
	}

	private void addCreatesProfileProperty(Individual subjectInd) {
		Literal profile = model.createTypedLiteral(profileURI,
				XSD.TYPE_URI_ANYURI);
		subjectInd.addProperty(createsProfileProperty, profile);
	}

	private void addExtractsFromFormatProperty(Individual subjectInd) {
		subjectInd.addProperty(extractsFromFormatProperty,
				format.getIndividual());
	}

	@Override
	public Individual createNewIndividual() {
		Individual subjectInd = super.createNewIndividual();
		this.addExtractsFromDataOfTypeProperty(subjectInd);
		this.addExtractsFromFormatProperty(subjectInd);
		this.addCreatesProfileProperty(subjectInd);

		return subjectInd;
	}

	@Override
	protected void initializeFields() {
		dataTypesToExtractFrom = new Vector<String>();
	}

	@Override
	protected void populateFieldsWithIndividual(Individual ind) {
		NodeIterator iterator = ind
				.listPropertyValues(extractsFromDataOfTypeProperty);
		String extractedDataType;
		while (iterator.hasNext()) {
			extractedDataType = (String) iterator.next().as(Literal.class)
					.getValue();
			dataTypesToExtractFrom.add(extractedDataType);
		}

		RDFNode profileNode = ind.getPropertyValue(createsProfileProperty);
		profileURI = (String) profileNode.as(Literal.class).getValue();

		RDFNode fmt = ind.getPropertyValue(extractsFromFormatProperty);
		format = new Format(fmt.as(Individual.class).getURI(), model);
	}

	@Override
	protected void setProperties() {
		extractsFromDataOfTypeProperty = model
				.getDatatypeProperty(ViskoS.DATATYPE_PROPERTY_URI_EXTRACTS_DATA_OFTYPE);
		extractsFromFormatProperty = model
				.getObjectProperty(ViskoS.PROPERTY_URI_EXTRACTS_FROM_FORMAT);
		createsProfileProperty = model
				.getDatatypeProperty(ViskoS.DATATYPE_PROPERTY_URI_CREATES_PROFILE);
	}

	@Override
	protected boolean allFieldsPopulated() {
		return dataTypesToExtractFrom.size() > 0
				&& extractsFromFormatProperty != null && profileURI != null;
	}
}
