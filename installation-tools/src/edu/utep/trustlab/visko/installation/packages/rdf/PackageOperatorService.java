package edu.utep.trustlab.visko.installation.packages.rdf;

import edu.utep.trustlab.visko.ontology.model.OWLSModel;
import edu.utep.trustlab.visko.ontology.model.ViskoModel;
import edu.utep.trustlab.visko.ontology.operator.Mapper;
import edu.utep.trustlab.visko.ontology.operator.Transformer;
import edu.utep.trustlab.visko.ontology.pmlp.Format;
import edu.utep.trustlab.visko.ontology.service.OWLSService;
import edu.utep.trustlab.visko.ontology.service.Toolkit;
import edu.utep.trustlab.visko.ontology.view.View;

class PackageOperatorService {	
	private String baseURL;
	
	private ViskoModel vModel;
	private ViskoModel tempReadingModel;
	private OWLSModel oModel;
	
	private String operationName;
	private String wsdlURL;
	private Toolkit toolkit;
	
	private String label;
	private String comment;
	private Format inputFormat;
	private Format outputFormat;
	private View view;
	
	
	protected PackageOperatorService(ViskoModel viskoModel, OWLSModel owlsModel, String url){
		vModel = viskoModel;
		oModel = owlsModel;
		tempReadingModel = new ViskoModel();
		baseURL = url;
	}
	
	protected void setToolkit(Toolkit tk){
		toolkit = tk;
	}
	
	protected void addToModel(){
		//create service
		OWLSService service = new OWLSService(baseURL, operationName, oModel);
		service.setLabel(label);
		service.setOperationName(operationName);
		service.setSupportingToolkit(toolkit);
		service.setWSDLURL(wsdlURL);
		
		//create operator
		String operatorName = operationName + "-operator";
		Transformer transformer;
		if(view != null){
			Mapper mapper = new Mapper(baseURL, operatorName, vModel);
			mapper.setViewToMapTo(view);
			transformer = mapper;
		}
		else
			transformer = new Transformer(baseURL, operatorName, vModel);
		
		transformer.setComment(comment);
		transformer.setLabel(label);
		transformer.setTransformsToFormat(outputFormat);
		transformer.addOperatesOnFormat(inputFormat);
		
		service.setConceptualOperator(transformer);
		service.getIndividual();
		
		vModel.addToModel(service.toString());
	}
	
	public void setInputFormatURI(String uri){
		inputFormat = new Format(uri, tempReadingModel);	
	}
	
	public void setOutputFormatURI(String uri){
		outputFormat = new Format(uri, tempReadingModel);
	}
	
	public void setViewURI(String uri){
		view = new View(uri, tempReadingModel);
	}
	
	public void setOperationName(String opName){
		operationName = opName;
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
