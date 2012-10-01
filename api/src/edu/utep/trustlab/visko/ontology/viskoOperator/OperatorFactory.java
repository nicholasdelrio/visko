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
	
	public void setAsDimensionReducer(){
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
		PreViewerOperator preViewerOperator;
		
		if(isMapper()){
			Mapper viewMapper = new Mapper(baseURL, operatorName, viskoModel);
			viewMapper.setView(view);
			preViewerOperator = viewMapper;
		}
		else if(isInterpolator())
			preViewerOperator = new Interpolator(baseURL, operatorName, viskoModel); 
		else if(isDimensionReducer())
			preViewerOperator = new DimensionReducer(baseURL, operatorName, viskoModel);
		else if(isFilter())
			preViewerOperator = new Filter(baseURL, operatorName, viskoModel);
		else if(isConverter())
			preViewerOperator = new Converter(baseURL, operatorName, viskoModel);
		else
			preViewerOperator = new Transformer(baseURL, operatorName, viskoModel);
	
		//add data types
		preViewerOperator.addInputDataType(inputDataType);
		preViewerOperator.setOutputDataType(outputDataType);
	
		//add formats
		preViewerOperator.addInputFormat(inputFormat);
		preViewerOperator.setOutputFormat(outputFormat);
		
		//add label, comments, and name
		preViewerOperator.setLabel(label);
		preViewerOperator.setComment(comment);
		preViewerOperator.setName(operatorName);
		
		return preViewerOperator;
	}
	
	public void setComment(String aComment){
		comment = aComment;
	}
	
	public void setLabel(String aLabel){
		label = aLabel;
	}
	
	public boolean isFilter(){
		return areDataTypesEqual() && areFormatsEqual();
	}
	
	public boolean isDimensionReducer(){
		return isDimensionFilter;
	}
	
	public boolean isInterpolator(){
		return isInterpolator;
	}
	
	public boolean isConverter(){
		return areDataTypesEqual() && !areFormatsEqual();
	}
	
	public boolean isMapper(){
		return view != null;
	}
	
	private boolean areDataTypesEqual(){
		return inputDataType.getURI().equals(outputDataType.getURI());
	}
	
	private boolean areFormatsEqual(){
		return inputFormat.getURI().equals(outputFormat.getURI());
	}
}
