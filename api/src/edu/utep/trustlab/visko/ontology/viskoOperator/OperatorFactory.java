package edu.utep.trustlab.visko.ontology.viskoOperator;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;

import edu.utep.trustlab.visko.ontology.model.ViskoModel;
import edu.utep.trustlab.visko.ontology.pmlp.Format;
import edu.utep.trustlab.visko.ontology.viskoView.View;
import edu.utep.trustlab.visko.ontology.vocabulary.supplemental.OWL;
import edu.utep.trustlab.visko.sparql.ViskoTripleStore;

public class OperatorFactory {
	
	private String comment;
	private String label;
	
	private ViskoModel viskoModel;

	private OntResource inputDataType;
	private Format inputFormat;
	
	private OntResource outputDataType;
	private Format outputFormat;
	
	private View view;

	private String baseURL;
	
	private OntModel dataTypes;
	
	public void setInputDataType(OntResource aDataType){
		inputDataType = aDataType;
	}
	
	public void setDataTypesModel(OntModel dataTypesModel){
		dataTypes = dataTypesModel;
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
	
	public void setView(View aView){
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
		
		if(view != null){
			ViewMapper viewMapper = new ViewMapper(baseURL, operatorName, viskoModel);
			viewMapper.setView(view);
			theOperator = viewMapper;
		}
		else if(isFilter())
			theOperator = new Filter(baseURL, operatorName, viskoModel);
		else
			theOperator = new Operator(baseURL, operatorName, viskoModel);
	
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
	
	public boolean isFilter(){
		boolean dataTypesComply = isOutputDataTypeSubtypeOfInput() || areDataTypesEqual();
		boolean formatsComply = inputFormat.getURI().equals(outputFormat.getURI());
		return dataTypesComply && formatsComply;
	}
	
	private boolean areDataTypesEqual(){
		return inputDataType.equals(outputDataType);
	}
	
	private boolean isOutputDataTypeSubtypeOfInput(){
		String inputDataTypeURI = "<" + inputDataType.getURI() + ">";
		String outputDataTypeURI = "<" + outputDataType.getURI() + ">";
		
		String queryString = 
				ViskoTripleStore.QUERY_PREFIX
				+ "ASK WHERE {" + outputDataTypeURI + " rdfs:subClassOf " + inputDataTypeURI + " . }";
		
		return executeAskQuery(queryString);
	}
			
	private boolean executeAskQuery(String askQueryString){
		  Query query = QueryFactory.create(askQueryString) ;
		  QueryExecution qexec = QueryExecutionFactory.create(query, dataTypes);
		  boolean result = qexec.execAsk();
		  return result;		
	}	
}
