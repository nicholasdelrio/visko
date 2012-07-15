package edu.utep.trustlab.visko.execution;

import edu.utep.trustlab.visko.planning.Pipeline;

public class PipelineExecutorJob {
	
	private Pipeline pipe;
	private boolean provenance;
	
	private String finalResultURL;
	private String pmlNodesetURI;
	private String pmlQueryURI;
	
	private PipelineExecutorJobStatus status;
	
	public PipelineExecutorJob(Pipeline pipelineToExecute, boolean logProvenance){
		pipe = pipelineToExecute;
		provenance = logProvenance;
		status = new PipelineExecutorJobStatus();
	}
    
	public PipelineExecutorJobStatus getJobStatus(){
		return status;
	}
	
    public void setPipeline(Pipeline pipeline){
    	pipe = pipeline;
    }
    public Pipeline getPipeline(){
    	return pipe;
    }
    
    public void setProvenanceLogging(boolean logProvenance){
    	provenance = logProvenance;
    }
    
    public boolean getProvenanceLogging(){
    	return provenance;
    }
    
    public void setFinalResultURL(String resultURL){
    	finalResultURL = resultURL;
    }
    
    public String getFinalResultURL(){
    	return finalResultURL;
    }
    
    public void setPMLNodesetURI(String nodesetURI){
    	pmlNodesetURI = nodesetURI;
    }
    
    public String getPMLNodesetURI(){
    	return pmlNodesetURI;
    }
    
    public void setPMLQueryURI(String queryURI){
    	pmlQueryURI = queryURI;
    }
    
    public String getPMLQueryURI(){
    	return pmlQueryURI;
    }
}
