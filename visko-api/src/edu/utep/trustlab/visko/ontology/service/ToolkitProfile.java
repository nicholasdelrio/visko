package edu.utep.trustlab.visko.ontology.service;

import java.util.Vector;

import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.RDFNode;

import edu.utep.trustlab.visko.ontology.model.ViskoModel;
import edu.utep.trustlab.visko.ontology.vocabulary.XSD;
import edu.utep.trustlab.visko.ontology.JenaIndividual;
import edu.utep.trustlab.visko.ontology.vocabulary.ViskoS;

public class ToolkitProfile extends JenaIndividual {
	private Toolkit toolkit;
	private Vector<String> profiledDataTypes;
	private Vector<InputBinding> inputBindings;

	// properties
	private ObjectProperty basedOnProperty;
	private ObjectProperty declaresBindingsProperty;
	private DatatypeProperty profilesProperty;

	public ToolkitProfile(String baseURL, String name, ViskoModel viskoModel) {
		super(ViskoS.CLASS_URI_TOOLKIT_PROFILE, baseURL, name, viskoModel);
	}

	public ToolkitProfile(String uri, ViskoModel viskoModel) {
		super(uri, viskoModel);
	}

	public void addInputBinding(InputBinding ib) {
		inputBindings.add(ib);
	}

	public void setInputBindings(Vector<InputBinding> ibs) {
		inputBindings = ibs;
	}

	public Vector<InputBinding> getInputBindings() {
		return inputBindings;
	}

	public void setToolkit(Toolkit tk) {
		toolkit = tk;
	}

	public Toolkit getToolkit() {
		return toolkit;
	}

	public void addProfiledDataType(String dataType) {
		profiledDataTypes.add(dataType);
	}

	public Vector<String> getProfiledDataTypes() {
		return profiledDataTypes;
	}

	private void addbasedOnToolkitProperty(Individual subjectInd) {
		subjectInd.addProperty(basedOnProperty, toolkit.getIndividual());
	}

	private void addProfilesProperty(Individual subjectInd) {
		for (String type : profiledDataTypes) {
			System.out.println("adding profiled type....");
			Literal dataType = model.createTypedLiteral(type,
					XSD.TYPE_URI_ANYURI);
			subjectInd.addProperty(profilesProperty, dataType);
		}
	}

	private void addDeclaresBindingsProperty(Individual subjectInd) {
		for (InputBinding ib : inputBindings) {
			subjectInd
					.addProperty(declaresBindingsProperty, ib.getIndividual());
		}
	}

	@Override
	public Individual createNewIndividual() {
		Individual subjectInd = super.createNewIndividual();
		this.addbasedOnToolkitProperty(subjectInd);
		this.addDeclaresBindingsProperty(subjectInd);
		this.addProfilesProperty(subjectInd);

		return subjectInd;
	}

	@Override
	protected void populateFieldsWithIndividual(Individual ind) {
		NodeIterator iterator = ind.listPropertyValues(profilesProperty);
		RDFNode dataType;
		String profiledDataType;
		while (iterator.hasNext()) {
			dataType = iterator.next();
			profiledDataType = (String) dataType.as(Literal.class).getValue();
			profiledDataTypes.add(profiledDataType);
		}

		RDFNode tkInd = ind.getPropertyValue(basedOnProperty);
		toolkit = new Toolkit(tkInd.as(Individual.class).getURI(), model);

		NodeIterator ibInds = ind.listPropertyValues(declaresBindingsProperty);
		String ibURI;
		while (ibInds.hasNext()) {
			ibURI = ibInds.next().as(Individual.class).getURI();
			inputBindings.add(new InputBinding(ibURI, model));
		}
	}

	@Override
	protected void setProperties() {
		basedOnProperty = model.getObjectProperty(ViskoS.PROPERTY_URI_BASED_ON);
		declaresBindingsProperty = model
				.getObjectProperty(ViskoS.PROPERTY_URI_DECLARES_BINDINGS);
		profilesProperty = model
				.getDatatypeProperty(ViskoS.DATATYPE_PROPERTY_URI_PROFILES);
	}

	@Override
	protected void initializeFields() {
		inputBindings = new Vector<InputBinding>();
		profiledDataTypes = new Vector<String>();
	}

	@Override
	protected boolean allFieldsPopulated() {
		return toolkit != null && inputBindings.size() > 0
				&& profiledDataTypes.size() > 0;
	}
}
