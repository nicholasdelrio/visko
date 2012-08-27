package edu.utep.trustlab.visko.installation.packages.rdf;

import java.util.HashMap;

import edu.utep.trustlab.visko.ontology.model.ViskoModel;
import edu.utep.trustlab.visko.ontology.service.Input;
import edu.utep.trustlab.visko.ontology.service.InputBinding;
import edu.utep.trustlab.visko.ontology.service.InputParameterBindings;

public class PackageInputParameterBindings {
	
	private ViskoModel packageModel;
	private ViskoModel parametersModel;
	private InputParameterBindings parameterBindings;

	private String baseFileURL;

	private int counter;

	private HashMap <String, PackageOperatorService> operatorServices;
	private String bindingsName;
	
	protected PackageInputParameterBindings(String name, ViskoModel viskoModel, String bFURL, ViskoModel paramsModel, HashMap<String, PackageOperatorService> services){
		baseFileURL = bFURL;
		packageModel = viskoModel;
		parametersModel = paramsModel;
		bindingsName= name;
		parameterBindings = new InputParameterBindings(baseFileURL, name, packageModel);
		counter = 0;
		operatorServices = services;
	}
	
	public void addDataType(String dataTypeURI){
		parameterBindings.addProfiledDataType(dataTypeURI);
	}
	
	protected void addToModel(){
		parameterBindings.getIndividual();
	}
	
	public void addInputBinding(String operationName, String parameterName, String value){
		PackageOperatorService service = operatorServices.get(operationName);
		
		String parameterURI = service.getOWLSServiceURL() + "#" + parameterName;
		Input input = new Input(parameterURI, parametersModel);

		String bindingName = bindingsName + "-binding-" +  counter++;
		InputBinding binding = new InputBinding(baseFileURL, bindingName, packageModel);
		binding.setInputParameter(input);
		binding.setValueData(value);
		
		parameterBindings.addInputBinding(binding);
	}	
}
