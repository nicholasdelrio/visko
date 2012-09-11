package edu.utep.trustlab.visko.installation.packages.rdf;

import edu.utep.trustlab.visko.ontology.model.ViskoModel;
import edu.utep.trustlab.visko.ontology.operator.ViewMapper;
import edu.utep.trustlab.visko.ontology.operator.DataTransformer;
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
	
	private String inputDataTypeURI;
	private String outputDataTypeURI;
	
	private OWLSService owlsService;
	
	protected PackageOperatorService(String name, ViskoModel viskoModel, String bURL, String bFURL){
		vModel = viskoModel;
		baseURL = bURL;
		baseFileURL = bFURL;
		operationName = name;
	}
	
	public String getName(){
		return operationName;
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
		//create operator
		String operatorName = operationName + "-operator";
		DataTransformer transformer;
		if(view != null){
			ViewMapper mapper = new ViewMapper(baseFileURL, operatorName, vModel);
			mapper.setViewToMapTo(view);
			transformer = mapper;
		}
		else
			transformer = new DataTransformer(baseFileURL, operatorName, vModel);
		
		transformer.setComment(comment);
		transformer.setLabel(label);
		transformer.setTransformsToFormat(outputFormat);
		transformer.addOperatesOnFormat(inputFormat);
		transformer.setName(operatorName);
		
		if(inputDataTypeURI != null)
			transformer.addOperatesOnDataType(inputDataTypeURI);
		if(outputDataTypeURI != null)
			transformer.setTransformsToDataType(outputDataTypeURI);
		
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
	}
	
	public void setInputDataType(String inDataTypeURI){
		inputDataTypeURI = inDataTypeURI;
	}

	public void setOutputDataType(String outDataTypeURI){
		outputDataTypeURI = outDataTypeURI;
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