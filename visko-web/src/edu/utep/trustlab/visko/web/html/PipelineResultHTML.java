package edu.utep.trustlab.visko.web.html;

import java.util.Vector;

import edu.utep.trustlab.visko.execution.PipelineExecutorJob;

public class PipelineResultHTML {
	
	public static String getHTML(PipelineExecutorJob job){
	String resultURL = job.getFinalResultURL();
    	
    	if(resultURL == null)
    		return "No result generated!";
    	
    	String viewerSetURI = job.getPipeline().getViewerSets().firstElement();
 		String endpointURL = job.getPipeline().getViewer().getEndpointURL();
 	   
 		System.out.println("viewerSetURL: " + viewerSetURI);
 		System.out.println("endpointURL: " + endpointURL);
 		
    	String resultMessage = "";

    	if(viewerSetURI.endsWith("mozilla-firefox") || viewerSetURI.endsWith("internet-explorer")){
    		
    		if(resultURL.endsWith(".pdf") || resultURL.endsWith(".PDF"))
        		resultMessage += "<a href=\"" + resultURL + "\">Click to view PDF</a>";
    		else
    			resultMessage += "<img src=\"" + resultURL + "\" />";
    	}
    	
    	else if(viewerSetURI.endsWith("data-driven-documents") && endpointURL != null){
    		endpointURL += "?url=" + resultURL;
    		//resultMessage += "<a href=\"" + endpointURL + "\">Click To Interact with Viewer</a>";
    		resultMessage += "<iframe width=\"1050\" height=\"700\" src=\"" + endpointURL + "\" frameborder=\"1\"></iframe>";
    	}
    	
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
}
