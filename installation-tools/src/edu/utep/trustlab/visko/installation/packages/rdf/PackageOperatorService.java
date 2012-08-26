package edu.utep.trustlab.visko.installation.packages.rdf;

import java.util.ArrayList;

import org.mindswap.owl.OWLIndividual;

import edu.utep.trustlab.visko.ontology.model.ViskoModel;
import edu.utep.trustlab.visko.ontology.operator.Mapper;
import edu.utep.trustlab.visko.ontology.operator.Transformer;
import edu.utep.trustlab.visko.ontology.pmlp.Format;
import edu.utep.trustlab.visko.ontology.service.OWLSService;
import edu.utep.trustlab.visko.ontology.service.Service;
import edu.utep.trustlab.visko.ontology.service.Toolkit;
import edu.utep.trustlab.visko.ontology.view.View;

public class PackageOperatorService {	
	private String baseURL;
	private String baseFileURL;
	
	private ViskoModel vModel;
		
	private String operationName;
	private String wsdlURL;
	private Toolkit toolkit;
	
	private String label;
	private String comment;
	private Format inputFormat;
	private Format outputFormat;
	private View view;
	
	private OWLSService owlsService;
	private ArrayList<PackageInputParameterBindings> bindingsSet;
	
	private int counter;
	
	protected PackageOperatorService(String name, ViskoModel viskoModel, String bURL, String bFURL){
		vModel = viskoModel;
		baseURL = bURL;
		baseFileURL = bFURL;
		operationName = name;
		bindingsSet = new ArrayList<PackageInputParameterBindings>();
		counter = 0;
	}
	
	public String getName(){
		return operationName;
	}
	
	protected void setToolkit(Toolkit tk){
		toolkit = tk;
	}
	
	public PackageInputParameterBindings createNewInputParameterBindings(){
		addToModel();
		
		ViskoModel paramsModel = new ViskoModel();
			
		for(OWLIndividual input : owlsService.getIndividual().getProfile().getInputs()){
			System.out.println(input.toRDF(true, false));
			paramsModel.addToModel(input.toRDF(true, false));			
		}
		
		String owlsServiceURL = baseURL + owlsService.getFileName();
		String bindingsName = operationName + "-bindings-" + counter++;
		PackageInputParameterBindings bindings = new PackageInputParameterBindings(bindingsName, vModel, baseFileURL, owlsServiceURL, paramsModel);
		bindingsSet.add(bindings);
		return bindings;
	}
	
	public OWLSService getOWLSService(){
		return owlsService;
	}
	
	protected void addToModel() {
		//create operator
		String operatorName = operationName + "-operator";
		Transformer transformer;
		if(view != null){
			Mapper mapper = new Mapper(baseFileURL, operatorName, vModel);
			mapper.setViewToMapTo(view);
			transformer = mapper;
		}
		else
			transformer = new Transformer(baseURL, operatorName, vModel);
		
		transformer.setComment(comment);
		transformer.setLabel(label);
		transformer.setTransformsToFormat(outputFormat);
		transformer.addOperatesOnFormat(inputFormat);
		transformer.setName(operatorName);
		
		//create owlsService
		owlsService = new OWLSService(baseURL, operationName);
		owlsService.setWSDLURL(wsdlURL);
		owlsService.setOperationName(operationName);
		owlsService.setLabel(label);
		
		//create visko service
		Service service = new Service(baseFileURL, operationName, vModel);
		service.setLabel(label);
		service.setSupportingToolkit(toolkit);
		service.setComment(comment);		
		service.setOWLSService(owlsService);		
		service.setConceptualOperator(transformer);
		service.getIndividual();
		
		for(PackageInputParameterBindings bindings : bindingsSet){
			bindings.addToModel();
		}
	}
	
	public void setInputFormat(Format format){
		inputFormat = format;	
	}
	
	public void setOutputFormat(Format format){
		outputFormat = format;
	}
	
	public void setView(View generatedView){
		view = generatedView;
	}
		
	public void setWSDLURL(String url){
		wsdlURL = url;
	}
	
	public void setLabel(String lbl){
		label = lbl;
	}
	
	public void setComment(String com){
		comment = com;
	}
}