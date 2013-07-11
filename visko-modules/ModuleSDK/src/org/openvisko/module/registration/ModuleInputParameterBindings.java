package org.openvisko.module.registration;

import java.util.HashMap;

import com.hp.hpl.jena.ontology.OntResource;

import edu.utep.trustlab.visko.ontology.model.ViskoModel;
import edu.utep.trustlab.visko.ontology.viskoService.Input;
import edu.utep.trustlab.visko.ontology.viskoService.InputBinding;
import edu.utep.trustlab.visko.ontology.viskoService.InputParameterBindings;

public class ModuleInputParameterBindings {
	
	private ViskoModel packageModel;
	private ViskoModel parametersModel;
	private InputParameterBindings parameterBindings;

	private String baseFileURL;

	private int counter;

	private HashMap <String, ModuleOperatorService> operatorServices;
	private String bindingsName;
	
	protected ModuleInputParameterBindings(String name, ViskoModel viskoModel, String bFURL, ViskoModel paramsModel, HashMap<String, ModuleOperatorService> services){
		baseFileURL = bFURL;
		packageModel = viskoModel;
		parametersModel = paramsModel;
		bindingsName= name;
		parameterBindings = new InputParameterBindings(baseFileURL, name, packageModel);
		counter = 0;
		operatorServices = services;
	}
	
	public void addSemanticType(OntResource semanticType){
		parameterBindings.addProfileType(semanticType);
	}
	
	protected void addToModel(){
		parameterBindings.getIndividual();
	}
	
	public void addInputBinding(String operationName, String parameterName, String value){
		ModuleOperatorService service = operatorServices.get(operationName);
		
		String parameterURI = service.getOWLSServiceURL() + "#" + parameterName;
		Input input = new Input(parameterURI, parametersModel);

		String bindingName = bindingsName + "-binding-" +  counter++;
		InputBinding binding = new InputBinding(baseFileURL, bindingName, packageModel);
		binding.setInputParameter(input);
		binding.setValueData(value);
		
		parameterBindings.addInputBinding(binding);
	}	
}
