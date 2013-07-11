package edu.utep.trustlab.visko.web.requestHandler.execution;

import edu.utep.trustlab.visko.execution.PipelineExecutorJob;
import edu.utep.trustlab.visko.execution.PipelineExecutorJobStatus;
import edu.utep.trustlab.visko.web.html.PipelineResultHTML;

public class ExecutePipelineStatusBean{

	private static final int refreshRate = 2;
	
    private PipelineExecutorJob job;
    
    public ExecutePipelineStatusBean(PipelineExecutorJob executorJob){
    	job = executorJob;
    }
    
    public String getCancelButton(){
    	String formHTML = "<form name=\"input\" action=\"ViskoServletManager\" method=\"get\">";
    	formHTML += "<input type=\"hidden\" name=\"requestType\" value=\"cancel-pipeline-execution\">";  	    		    	
    	formHTML += "<input type=\"submit\" value=\"Cancel Pipeline Execution\" />";
    	formHTML += "</form>";
    	
    	if(!job.getJobStatus().isJobCompleted())
    		return formHTML;
    	
    	return "";
    }
    
    public String getMessage(){
    	String message;
    	switch(job.getJobStatus().getPipelineState()){
    		case NODATA:
    			message = job.getJobStatus().toString();
    			break;
    		case EMPTYPIPELINE:
    			message = job.getJobStatus().toString();
    			break;
    		case ERROR:
    			message = job.getJobStatus().toString();
    			break;
    		case INTERRUPTED:
    			message = job.getJobStatus().toString();
    			break;
    		case RUNNING:
    			message = job.getJobStatus().toString();
    			break;
    		case COMPLETE:
    			message = getCompletedMessage();
    			break;
    		default:
    			message = job.getJobStatus().toString();
    			break;
    	}
    	
    	return message;
    }
    
    public String getCompletedMessage(){
    	return PipelineResultHTML.getHTML(job);
    }
       
    public int getRefreshRate(){
        return refreshRate;
    }

    public String getRefreshTag(){
    	PipelineExecutorJobStatus status = job.getJobStatus();
    	if(!status.isJobCompleted())
            return "<meta http-equiv=\"refresh\""
                   + " content=\""
                   + getRefreshRate()
                   + "\";URL=\"ViskoServletManager?requestType=execute-pipeline\">";
    	return "";
    }
}