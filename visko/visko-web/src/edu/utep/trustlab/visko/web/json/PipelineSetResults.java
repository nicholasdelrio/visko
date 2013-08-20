package edu.utep.trustlab.visko.web.json;

import java.util.ArrayList;

import org.json.JSONObject;

import edu.utep.trustlab.visko.execution.PipelineExecutor;
import edu.utep.trustlab.visko.execution.PipelineExecutorJob;
import edu.utep.trustlab.visko.planning.pipelines.Pipeline;
import edu.utep.trustlab.visko.planning.pipelines.PipelineSet;


public class PipelineSetResults {
	
	public static final String NULL_VIEW = "NULL-VIEW";

	private boolean provenance;
	private PipelineSet pipelines;
	private String viewCriteria;
	private String toolkitCriteria;
	private String outputFormatCriteria;
	private int maxResults;
		
	public PipelineSetResults() {
		// TODO Auto-generated constructor stub
		setMaxResults();
	}
		
	private void setMaxResults(){
		this.maxResults = -1;
	}
	
	public void setProvenanceRecording(boolean provenanceRecording){
		provenance = provenanceRecording;
	}
	
	public void setMaxResults(int maxResults){
		this.maxResults = maxResults;
	}

	public void setPipelineSet(PipelineSet pipelines){
		this.pipelines = pipelines;
	}
	
	public void setViewCriteria(String viewURI){
		viewCriteria = viewURI;
	}
	
	public void setToolkitCriteria(String toolkitURI){
		toolkitCriteria = toolkitURI;
	}
	
	public void setOutputFormatCriteria(String formatURI){
		outputFormatCriteria = formatURI;
	}
	
	private JSONObject getResultSetTemplate(){
		JSONObject resultSet = new JSONObject();
		try{
			resultSet
			//.put("query", pipelines.getQuery().toString())
			.put("inputURL", pipelines.getArtifactURL());
		}catch(Exception e){e.printStackTrace();}
		
		return resultSet;
	}
	
	public String getEmptyResults(){
		return getResultSetTemplate().toString();
	}

	//trivial result occurs when the input data is already in a form that can be viewed
	// or matches the required input type/format requirements
	private ArrayList<JSONObject> getTrivialResult(){
		ArrayList<JSONObject> results = new ArrayList<JSONObject>();
		results.add(getResult(pipelines.getArtifactURL(), null));
		return results;
	}
	
	private ArrayList<JSONObject> getResults(){
		PipelineExecutor executor = new PipelineExecutor();
		
		ArrayList<JSONObject> results = new ArrayList<JSONObject>();
		Pipeline aPipeline;
		for(int i = 0; i < pipelines.size() && i < maxResults; i ++){
			aPipeline = pipelines.get(i);
			if(targetPipeline(aPipeline))
				results.add(getResult(executor, aPipeline));
		}
		return results;
	}

	
	public String toString(){
		ArrayList<JSONObject> results = getResults();
		JSONObject resultSet = getResultSetTemplate();
		try{
			resultSet.put("results", results);
			return resultSet.toString(4);
		}
		catch(Exception e){e.printStackTrace();}
		
		return resultSet.toString();
	}
	
	public String getTrivialResultsString(){
		ArrayList<JSONObject> results = getTrivialResult();
		JSONObject resultSet = getResultSetTemplate();
		try{
			resultSet.put("results", results);
			return resultSet.toString(4);
		}
		catch(Exception e){e.printStackTrace();}
		
		return resultSet.toString();
	}
	
	private boolean targetPipeline(Pipeline aPipeline){
		return 
				this.doesFormatCheckOut(aPipeline) &&
				this.doesToolkitCheckOut(aPipeline) &&
				this.doesViewCheckOut(aPipeline);
	}
	
	private boolean doesFormatCheckOut(Pipeline aPipeline){
		boolean formatChecksOut = false;
		
		if(outputFormatCriteria == null)
			formatChecksOut = true;
		
		else if(aPipeline.getOutputFormat() != null && aPipeline.getOutputFormat().equals(outputFormatCriteria))
			formatChecksOut = true;
		
		return formatChecksOut;
	}
	
	private boolean doesToolkitCheckOut(Pipeline aPipeline){
		boolean toolkitChecksOut = false;
		
		if(toolkitCriteria == null)
			toolkitChecksOut = true;
		
		else if(aPipeline.getToolkitThatGeneratesView() != null && aPipeline.getToolkitThatGeneratesView().equals(toolkitCriteria))
			toolkitChecksOut = true;
		
		return toolkitChecksOut;
	}
	
	private boolean doesViewCheckOut(Pipeline aPipeline){
		boolean viewChecksOut = false;
		if(viewCriteria == null)
			viewChecksOut = true;
		
		else if(viewCriteria.equals(NULL_VIEW) && aPipeline.getViewURI() == null)
			viewChecksOut = true;
		
		else if(viewCriteria.equals(aPipeline.getViewURI()))
			viewChecksOut = true;
		
		return viewChecksOut;
	}
	
	private JSONObject getResult(PipelineExecutor executor, Pipeline aPipeline){
		PipelineExecutorJob job = new PipelineExecutorJob(aPipeline);
		job.setProvenanceLogging(provenance);
		executor.setJob(job);
		executor.run();
		
		String viewerURI = aPipeline.getViewerURI();		
		String outputURL = job.getFinalResultURL();
		
		JSONObject aRecord = getResult(outputURL, viewerURI);
		try{
			if(job.getProvenanceLogging())
				aRecord.put("provenance", job.getProvQueryURI());
		}catch(Exception e){e.printStackTrace();}
		
		return aRecord;
	}
	
	private JSONObject getResult(String outputURL, String viewerURI){
		JSONObject aRecord = new JSONObject();
		if(viewerURI == null)
			viewerURI = "no viewer";
		try{
			aRecord
			.put("outputURL", outputURL)
			.put("viewerURI", viewerURI);			
		}catch(Exception e){e.printStackTrace();}
		return aRecord;
	}
}