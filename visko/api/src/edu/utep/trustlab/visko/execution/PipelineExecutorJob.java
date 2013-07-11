package edu.utep.trustlab.visko.execution;

import java.net.URI;

import edu.utep.trustlab.visko.planning.pipelines.*;

public class PipelineExecutorJob {
	
	private Pipeline pipe;
	private boolean provenance;
	private boolean simulated;
	
	private String finalResultURL;
	private URI pmlQueryURI;
	private URI provQueryURI;
	
	private PipelineExecutorJobStatus status;
	
	public PipelineExecutorJob(Pipeline pipelineToExecute){
		pipe = pipelineToExecute;
		status = new PipelineExecutorJobStatus();
	}
	
	public void setAsSimulatedJob(){
		simulated = true;
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
    
    public boolean isSimulated(){
    	return simulated;
    }
    
    public void setFinalResultURL(String resultURL){
    	finalResultURL = resultURL;
    }
    
    public String getFinalResultURL(){
    	return finalResultURL;
    }
        
    public void setPMLQueryURI(URI queryURI){
    	pmlQueryURI = queryURI;
    }
    
    public URI getPMLQueryURI(){
    	return pmlQueryURI;
    }
    
    public void setProvQueryURI(URI queryURI){
    	provQueryURI = queryURI;
    }
    
    public URI getProvQueryURI(){
    	return provQueryURI;
    }
}
