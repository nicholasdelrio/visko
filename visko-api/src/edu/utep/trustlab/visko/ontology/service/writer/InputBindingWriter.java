package edu.utep.trustlab.visko.ontology.service.writer;
import edu.utep.trustlab.repository.Repository;
import edu.utep.trustlab.visko.ontology.model.ViskoModel;
import edu.utep.trustlab.visko.ontology.service.Input;
import edu.utep.trustlab.visko.ontology.service.InputBinding;
import edu.utep.trustlab.visko.ontology.writer.ViskoWriter;

public class InputBindingWriter extends ViskoWriter {
	private Input inputParameter;
	private String valueData;

	private InputBinding inBinding;
	private ViskoModel readingModel;

	public InputBindingWriter(String name) {
		inBinding = new InputBinding(Repository.getServer().getBaseURL(),
				name, viskoModel);
	}

	public void setInputParameter(String uri) {
		inputParameter = new Input(uri, readingModel);
	}

	public void setValueData(String value) {
		valueData = value;
	}

	public String toRDFString() {
		inBinding.setInputParameter(inputParameter);
		inBinding.setValueData(valueData);
		// adds the individual to the model and returns a reference to that
		// Individual
		inBinding.getIndividual();
		viskoIndividual = inBinding;

		String viewerRDFString = viskoIndividual.toString();
		return viewerRDFString;
	}
}