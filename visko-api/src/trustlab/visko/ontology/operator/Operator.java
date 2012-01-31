package trustlab.visko.ontology.operator;

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
import trustlab.visko.ontology.vocabulary.PMLP;
import trustlab.visko.ontology.vocabulary.ViskoO;
import trustlab.visko.ontology.vocabulary.XSD;

public class Operator extends JenaIndividual {
	private Vector<Format> inputFormats;
	private Toolkit toolkit;

	private String name;

	// properties
	private ObjectProperty operatesOnProperty;
	private ObjectProperty supportedByProperty;

	// datatype properties
	private DatatypeProperty hasNameProperty;

	public Operator(String classURI, String baseURL, String name,
			ViskoModel viskoModel) {
		super(classURI, baseURL, name, viskoModel);
	}

	public Operator(String conceptURI, ViskoModel viskoModel) {
		super(conceptURI, viskoModel);
	}

	public void setName(String aName) {
		name = aName;
	}

	public String getName() {
		return name;
	}

	public void setSupportedByToolkit(Toolkit tk) {
		toolkit = tk;
	}

	public Toolkit getSupportedToolkit() {
		return toolkit;
	}

	public void setOperatesOnFormats(Vector<Format> inFormats) {
		inputFormats = inFormats;
	}

	public void addOperatesOnFormat(Format inFormat) {
		inputFormats.add(inFormat);
	}

	public Vector<Format> getOperatesOnFormats() {
		return inputFormats;
	}

	private void addOperatesOnFormatProperties(Individual subjectInd) {
		for (Format format : inputFormats) {
			subjectInd.addProperty(operatesOnProperty, format.getIndividual());
		}
	}

	private void addHasNameProperty(Individual subjectInd) {
		Literal stringName = model
				.createTypedLiteral(name, XSD.TYPE_URI_STRING);
		subjectInd.addProperty(hasNameProperty, stringName);
	}

	private void addSupportedByToolkitProperty(Individual subjectInd) {
		if (toolkit != null) {
			subjectInd
					.addProperty(supportedByProperty, toolkit.getIndividual());
		}
	}

	@Override
	protected boolean allFieldsPopulated() {
		return inputFormats.size() > 0 && name != null;
	}

	@Override
	protected void populateFieldsWithIndividual(Individual ind) {
		NodeIterator fmts = ind.listPropertyValues(operatesOnProperty);
		Format fmt;
		while (fmts.hasNext()) {
			fmt = new Format(fmts.next().as(Individual.class).getURI(), model);
			inputFormats.add(fmt);
		}

		RDFNode supportingTK = ind.getPropertyValue(supportedByProperty);
		if (supportingTK != null)
			toolkit = new Toolkit(supportingTK.as(Individual.class).getURI(),
					model);

		RDFNode theName = ind.getPropertyValue(hasNameProperty);
		if (theName != null)
			name = (String) theName.as(Literal.class).getValue();
	}

	@Override
	protected Individual createNewIndividual() {
		Individual ind = super.createNewIndividual();
		this.addOperatesOnFormatProperties(ind);
		this.addHasNameProperty(ind);
		this.addSupportedByToolkitProperty(ind);
		return ind;
	}

	@Override
	protected void setProperties() {
		operatesOnProperty = model
				.getObjectProperty(ViskoO.PROPERTY_URI_OPERATESON);
		hasNameProperty = model
				.getDatatypeProperty(PMLP.DATATYPE_PROPERTY_URI_PMLP_HASNAME);
		supportedByProperty = model
				.getObjectProperty(ViskoO.PROPERTY_URI_SUPPORTEDBY);
	}

	@Override
	protected void initializeFields() {
		inputFormats = new Vector<Format>();
	}
}
