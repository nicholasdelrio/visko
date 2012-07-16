package edu.utep.trustlab.visko.web.requestHandler.execution;

import java.util.Vector;

import edu.utep.trustlab.visko.execution.PipelineExecutorJob;
import edu.utep.trustlab.visko.execution.PipelineExecutorJobStatus;

public class ExecutePipelineStatusBean{

	private static final int refreshRate = 3;
	
	private String linkToQuery = "";
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
    	String resultURL = job.getFinalResultURL();
    	
    	if(resultURL == null)
    		return "No result generated!";
    	
    	String resultMessage = "";
    	if(
    			resultURL.endsWith(".jpg") ||
    			resultURL.endsWith(".JPG") ||
    			resultURL.endsWith(".png") ||
    			resultURL.endsWith(".PNG") ||
    			resultURL.endsWith(".gif") ||
    			resultURL.endsWith(".GIF"))
    		resultMessage += "<img src=\"" + resultURL + "\" />";
    	
    	else if(resultURL.endsWith(".pdf") || resultURL.endsWith(".PDF"))
    		resultMessage += "<a href=\"" + resultURL + "\">Click to view PDF</a>";
    	
    	else{
    		Vector<String> viewerSets = job.getPipeline().getViewerSets();
    		resultMessage += "<h4>Result</h4>";
    		resultMessage += "<ul>";
    		resultMessage += "<li>Resultant URL: <a href=\"" + resultURL + "\">" + resultURL + "</a></li>";
    		resultMessage += "<li>Viewed Using: " + job.getPipeline().getViewerURI();
    		resultMessage += "<li>Using the ViewerSet(s)</li>";
    		resultMessage += "<ul>";
    		
    		for(String viewerSet : viewerSets)
    			resultMessage += "<li>" + viewerSet + "</li>";
    		
    		resultMessage += "</ul>";
    		resultMessage += "</ul>";
    	}
    	return resultMessage;
    }
   
    public void setLinkToQuery(){
    	String provenanceLink = "Provenance logging not enabled for this run.";
    	if(job.getProvenanceLogging() && job.getPMLQueryURI() != null)
    		provenanceLink = "<a href=\"" + job.getPMLQueryURI() + "\">" + job.getPMLQueryURI() + "</a>";    	
    	
    	linkToQuery =
    			"<br>"
    			+ "<table border=\"1\">"
    			+ "<tr><td><b>Visualization Query</b></td><td><b>PML Query</b></td></tr>"
    			+ "<tr><td> <a href=\"ViskoServletManager?requestType=get-query\">Click to export query</a></td><td>" + provenanceLink + "</td></tr>"
    			+ "</table>";
    }
    
    public String getLinkToQuery(){
    	return linkToQuery;
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