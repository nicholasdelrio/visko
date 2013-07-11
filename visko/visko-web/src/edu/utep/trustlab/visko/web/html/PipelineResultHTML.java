package edu.utep.trustlab.visko.web.html;

import java.net.URI;
import java.util.Vector;

import edu.utep.trustlab.visko.execution.PipelineExecutorJob;
import edu.utep.trustlab.visko.ontology.model.ViskoModel;
import edu.utep.trustlab.visko.ontology.viskoOperator.InputOutputOperator;

public class PipelineResultHTML {
	
	public static String getHTML(PipelineExecutorJob job){
		String html = "<h2>Query</h2>";
		html += "<a href=\"ViskoServletManager?requestType=get-query\">Click to export query</a>";
		
		html += "<h2>Provenance</h2>";
		html += getProvenanceHTML(job);
		html += getPipelineResultsHTML(job);
		
		return html;
	}
	
	public static String getPipelineResultsHTML(PipelineExecutorJob job){
		String resultURL = job.getFinalResultURL();

		if(resultURL != null){
	
			String operatorURI = job.getPipeline().getService(job.getPipeline().size() - 1).getConceptualOperator().getURI();
			InputOutputOperator operator = new InputOutputOperator(operatorURI, new ViskoModel());
	
			String outputFormatURI = operator.getOutputFormat().getURI();
	    	
			Vector<String> viewerSets = job.getPipeline().getViewerSets();
			String viewerSetURI = "";
			
			if(viewerSets != null && viewerSets.size() > 0)
				viewerSetURI = job.getPipeline().getViewerSets().firstElement();

			String endpointURL = null;
			if(job.getPipeline().getViewerURI() != null)
				endpointURL = job.getPipeline().getViewer().getEndpointURL();
 	   
			System.out.println("viewerSetURL: " + viewerSetURI);
			System.out.println("endpointURL: " + endpointURL);
 		
			String resultMessage = "";

			if(viewerSetURI.endsWith("mozilla-firefox") || viewerSetURI.endsWith("internet-explorer") || viewerSetURI.endsWith("web-browser")){
    		
				if(outputFormatURI.endsWith("PDF"))
					resultMessage += "<a href=\"" + resultURL + "\">Click to view PDF</a>";
				else if(outputFormatURI.endsWith("HTML"))
					resultMessage += "<iframe width=\"1050\" height=\"700\" src=\"" + resultURL + "\" frameborder=\"1\"></iframe>";
				else
					resultMessage += "<img src=\"" + resultURL + "\" />";
			}
    	
			else if(endpointURL != null){
				endpointURL += "?url=" + resultURL;
				resultMessage += "<iframe width=\"1050\" height=\"700\" src=\"" + endpointURL + "\" frameborder=\"1\"></iframe>";
			}
			else {
				viewerSets = job.getPipeline().getViewerSets();
				resultMessage += "<h4>Result</h4>";
				resultMessage += "<ul>";
				resultMessage += "<li>Resultant URL: <a href=\"" + resultURL + "\">" + resultURL + "</a></li>";
				
				if(viewerSets != null && viewerSets.size() > 0)
					resultMessage += "<li>Viewed Using: " + job.getPipeline().getViewerURI() + "</li>";
				else
					resultMessage += "<li>Viewed Using: Unknown</li>";
				resultMessage += "<li>Using the ViewerSet(s)</li>";
				resultMessage += "<ul>";
    		
				if(viewerSets != null && viewerSets.size() > 0)
					for(String viewerSet : viewerSets)
						resultMessage += "<li>" + viewerSet + "</li>";
				else
					resultMessage += "<li>No ViewerSets specified in query</li>";
				
				resultMessage += "</ul>";
				resultMessage += "</ul>";
			}
			return resultMessage;
    	}
		else
			return "Result was not generated";
	}
	
	private static String getProvenanceHTML(PipelineExecutorJob job){
		String html;
		if(!job.getProvenanceLogging())
			html = "<p>Provenance logging not enabled for this run.</p>";
				
		else{
			html = "<table width=\"1000\" border=\"1\" class=\"visko\">";
			html += "<tr><td><b>Language</b></td><td><b>URI</b></td></tr>";

			URI pml2URI = job.getPMLQueryURI();
			URI provURI = job.getProvQueryURI();
			
			if(pml2URI != null)
				html += "<tr><td>PML2</td><td><a href=\"" + pml2URI.toASCIIString() + "\">" + pml2URI.toASCIIString() + "</a></td></tr>";
			else
				html += "<tr><td>PML2</td><td>Error recording provenance</td></tr>";

			if(provURI != null)
				html += "<tr><td>Prov</td><td><a href=\"" + provURI.toASCIIString() + "\">" + provURI.toASCIIString() + "</a></td></tr>";
			else
				html += "<tr><td>Prov/PML2</td><td>Error recording provenance</td></tr>";			
		}
		
		html += "</table>";
		return html;
	}
}
