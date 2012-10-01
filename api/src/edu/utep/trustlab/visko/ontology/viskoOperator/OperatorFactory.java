package edu.utep.trustlab.visko.ontology.viskoOperator;

import com.hp.hpl.jena.ontology.OntResource;
import edu.utep.trustlab.visko.ontology.model.ViskoModel;
import edu.utep.trustlab.visko.ontology.pmlp.Format;
import edu.utep.trustlab.visko.ontology.viskoView.VisualizationAbstraction;
import edu.utep.trustlab.visko.ontology.vocabulary.supplemental.OWL;

public class OperatorFactory {
	
	private String comment;
	private String label;
	
	private ViskoModel viskoModel;

	private OntResource inputDataType;
	private Format inputFormat;
	
	private OntResource outputDataType;
	private Format outputFormat;
	
	private VisualizationAbstraction view;

	private String baseURL;
	
	//this is a hack, we can infer this in the future if we reason with dimensions
	private boolean isDimensionFilter;
	
	//this is a hack, we can infer this in the future based on if the input type is a sub class of non-uniform data
	// the output is a sub class of uniform data
	private boolean isInterpolator;
	
	public void setAsDimensionFilter(){
		isDimensionFilter = true;
	}
	
	public void setAsInterpolator(){
		isInterpolator = true;
	}
	
	public void setInputDataType(OntResource aDataType){
		inputDataType = aDataType;
	}
		
	public void setOutputDataType(OntResource aDataType){
		outputDataType = aDataType;
	}
	
	public void setInputFormat(Format aFormat){
		inputFormat = aFormat;
	}

	public void setOutputFormat(Format aFormat){
		outputFormat = aFormat;
	}
	
	public void setView(VisualizationAbstraction aView){
		view = aView;
	}
	
	public void setBaseURL(String aURL){
		baseURL = aURL;
	}
	
	public void setModel(ViskoModel vModel){
		viskoModel = vModel;
	}
	
	public String getLabel(){
		return label;
	}
	
	public String getComment(){
		return comment;
	}
	
	private void adjustForNullDataTypes(){
		if(inputDataType == null)
			inputDataType = OWL.getOWLThing();
		if(outputDataType == null)
			outputDataType = OWL.getOWLThing();
	}
	
	public Operator createOperator(String operatorName){
		adjustForNullDataTypes();
		Operator theOperator;
		
		if(isViewMapper()){
			ViewMapper viewMapper = new ViewMapper(baseURL, operatorName, viskoModel);
			viewMapper.setView(view);
			theOperator = viewMapper;
		}
		else if(isInterpolator())
			theOperator = new Interpolator(baseURL, operatorName, viskoModel); 
		else if(isDimensionFilter())
			theOperator = new DimensionFilter(baseURL, operatorName, viskoModel);
		else if(isDataFilter())
			theOperator = new DataFilter(baseURL, operatorName, viskoModel);
		else if(isFormatConverter())
			theOperator = new FormatConverter(baseURL, operatorName, viskoModel);
		else
			theOperator = new DataTransformer(baseURL, operatorName, viskoModel);
	
		//add data types
		theOperator.setInputDataType(inputDataType);
		theOperator.setOutputDataType(outputDataType);
	
		//add formats
		theOperator.setInputFormat(inputFormat);
		theOperator.setOutputFormat(outputFormat);
		
		//add label, comments, and name
		theOperator.setLabel(label);
		theOperator.setComment(comment);
		theOperator.setName(operatorName);
		
		return theOperator;
	}
	
	public void setComment(String aComment){
		comment = aComment;
	}
	
	public void setLabel(String aLabel){
		label = aLabel;
	}
	
	public boolean isDataFilter(){
		return areDataTypesEqual() && areFormatsEqual();
	}
	
	public boolean isDimensionFilter(){
		return isDimensionFilter;
	}
	
	public boolean isInterpolator(){
		return isInterpolator;
	}
	
	public boolean isFormatConverter(){
		return areDataTypesEqual() && !areFormatsEqual();
	}
	
	public boolean isViewMapper(){
		return view != null;
	}
	
	private boolean areDataTypesEqual(){
		return inputDataType.getURI().equals(outputDataType.getURI());
	}
	
	private boolean areFormatsEqual(){
		return inputFormat.getURI().equals(outputFormat.getURI());
	}
}
