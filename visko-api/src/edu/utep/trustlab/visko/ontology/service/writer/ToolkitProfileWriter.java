package edu.utep.trustlab.visko.ontology.service.writer;

import java.util.Vector;

import edu.utep.trustlab.repository.Repository;
import edu.utep.trustlab.visko.ontology.model.ViskoModel;
import edu.utep.trustlab.visko.ontology.service.Input;
import edu.utep.trustlab.visko.ontology.service.InputBinding;
import edu.utep.trustlab.visko.ontology.service.Toolkit;
import edu.utep.trustlab.visko.ontology.service.ToolkitProfile;
import edu.utep.trustlab.visko.ontology.writer.ViskoWriter;

public class ToolkitProfileWriter extends ViskoWriter {
	private Toolkit tk;
	private Vector<InputBinding> inputBindings;

	private ToolkitProfile profile;
	private ViskoModel loadingModel;
	private Vector<String> dataTypes;
	private int counter;
	private static String BINDING_NAME_PREFIX = "binding";

	public ToolkitProfileWriter(String name) {
		loadingModel = new ViskoModel();
		profile = new ToolkitProfile(Repository.getRepository().getBaseURL(),
				name, viskoModel);
		counter = 0;
		inputBindings = new Vector<InputBinding>();
		dataTypes = new Vector<String>();
	}

	public void addDataType(String uri) {
		dataTypes.add(uri);
	}

	public Vector<String> getDataTypes() {
		return dataTypes;
	}

	public void setSupportingToolkit(String uri) {
		tk = new Toolkit(uri, loadingModel);
	}

	public void addInputBinding(String inputParameterURI, String value) {
		Input input = new Input(inputParameterURI, loadingModel);

		InputBinding ib = new InputBinding(profile.getFullURL(),
				BINDING_NAME_PREFIX + counter++, viskoModel);
		ib.setInputParameter(input);
		ib.setValueData(value);

		inputBindings.add(ib);
	}

	public String toRDFString() {
		profile.setToolkit(tk);
		profile.setInputBindings(inputBindings);
		for (String type : dataTypes)
			profile.addProfiledDataType(type);
		// adds the individual to the model and returns a reference to that
		// Individual
		profile.getIndividual();
		viskoIndividual = profile;

		String viewerRDFString = viskoIndividual.toString();
		return viewerRDFString;
	}

}
