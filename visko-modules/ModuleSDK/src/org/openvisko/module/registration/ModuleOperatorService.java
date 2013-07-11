package org.openvisko.module.registration;

import com.hp.hpl.jena.ontology.OntResource;

import edu.utep.trustlab.visko.ontology.model.ViskoModel;
import edu.utep.trustlab.visko.ontology.pmlp.Format;
import edu.utep.trustlab.visko.ontology.viskoOperator.Operator;
import edu.utep.trustlab.visko.ontology.viskoOperator.OperatorFactory;
import edu.utep.trustlab.visko.ontology.viskoService.OWLSService;
import edu.utep.trustlab.visko.ontology.viskoService.Service;
import edu.utep.trustlab.visko.ontology.viskoService.Toolkit;
import edu.utep.trustlab.visko.ontology.viskoView.VisualizationAbstraction;

public class ModuleOperatorService {	
	private String baseURL;
	private String baseFileURL;
	
	private ViskoModel vModel;
		
	private String operationName;
	private String name;
	private String wsdlURL;
	private Toolkit toolkit;
	
	private OperatorFactory operatorFactory;
		
	private OWLSService owlsService;
	
	protected ModuleOperatorService(String name, ViskoModel viskoModel, String bURL, String bFURL){
		vModel = viskoModel;
		baseURL = bURL;
		baseFileURL = bFURL;
		operationName = name;
		
		operatorFactory = new OperatorFactory();
		operatorFactory.setModel(vModel);
		operatorFactory.setBaseURL(baseFileURL);
	}
	
	public String getName(){
		if(name != null)
			return name;
		return operationName;
	}
	
	void setName(String aName){
		name = aName;
	}
	
	protected void setToolkit(Toolkit tk){
		toolkit = tk;
	}
	
	public String getOWLSServiceURL(){
		return baseURL + owlsService.getFileName();
	}
	
	public OWLSService getOWLSService(){
		return owlsService;
	}
	
	protected void addToModel() {
		String operatorPostfix = "-operator";
		String serviceName = name;
		String operatorName = serviceName + operatorPostfix;
			
		//create operator
		Operator operator = operatorFactory.createOperator(operatorName);
				
		//create owlsService
		owlsService = new OWLSService(baseURL, serviceName);
		owlsService.setWSDLURL(wsdlURL);
		owlsService.setOperationName(operationName);
		owlsService.setLabel(operatorFactory.getLabel());
		
		//create visko service
		Service service = new Service(baseFileURL, serviceName, vModel);
		service.setLabel(operatorFactory.getLabel());
		service.setSupportingToolkit(toolkit);
		service.setComment(operatorFactory.getComment());		
		service.setOWLSService(owlsService);		
		service.setConceptualOperator(operator);
		service.getIndividual();
	}
	
	public void setAsDimensionFilter(){
		operatorFactory.setAsDimensionReducer();
	}
	
	public void setAsInterpolator(){
		operatorFactory.setAsInterpolator();
	}
	
	public void setInputDataType(OntResource inDataType){
		operatorFactory.setInputDataType(inDataType);
	}

	public void setOutputDataType(OntResource outDataType){
		operatorFactory.setOutputDataType(outDataType);
	}
	
	public void setInputFormat(Format format){
		operatorFactory.setInputFormat(format);
	}
	
	public void setOutputFormat(Format format){
		operatorFactory.setOutputFormat(format);
	}
	
	public void setView(VisualizationAbstraction generatedView){
		operatorFactory.setView(generatedView);
	}
		
	public void setWSDLURL(String url){
		wsdlURL = url;
	}
	
	public void setLabel(String lbl){
		operatorFactory.setLabel(lbl);
	}
	
	public void setComment(String com){
		operatorFactory.setComment(com);
	}
}