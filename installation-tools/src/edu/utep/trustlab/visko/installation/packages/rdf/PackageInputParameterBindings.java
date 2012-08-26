package edu.utep.trustlab.visko.installation.packages.rdf;

import edu.utep.trustlab.visko.ontology.model.ViskoModel;
import edu.utep.trustlab.visko.ontology.service.Input;
import edu.utep.trustlab.visko.ontology.service.InputBinding;
import edu.utep.trustlab.visko.ontology.service.InputParameterBindings;

public class PackageInputParameterBindings {
	
	private ViskoModel vModel;
	private ViskoModel pModel;
	private InputParameterBindings parameterBindings;

	private String serviceURL;
	private String baseFileURL;

	private int counter;

	private String bindingsName;
	
	protected PackageInputParameterBindings(String name, ViskoModel viskoModel, String bFURL, String owlsServiceURL, ViskoModel paramsModel){
		baseFileURL = bFURL;
		vModel = viskoModel;
		pModel = paramsModel;
		bindingsName= name;
		parameterBindings = new InputParameterBindings(baseFileURL, name, vModel);
		counter = 0;
		
		serviceURL = owlsServiceURL;
	}
	
	public void addDataType(String dataTypeURI){
		parameterBindings.addProfiledDataType(dataTypeURI);
	}
	
	protected void addToModel(){
		parameterBindings.getIndividual();
	}
	
	public void addInputBinding(String parameterName, String value){
		String parameterURI = serviceURL + "#" + parameterName;
		Input input = new Input(parameterURI, pModel);

		String bindingName = bindingsName + "-binding-" +  counter++;
		InputBinding binding = new InputBinding(baseFileURL, bindingName, vModel);
		binding.setInputParameter(input);
		binding.setValueData(value);
		
		parameterBindings.addInputBinding(binding);
	}	
}
