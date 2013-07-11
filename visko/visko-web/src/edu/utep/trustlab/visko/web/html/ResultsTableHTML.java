/*
Copyright (c) 2012, University of Texas at El Paso
All rights reserved.
Redistribution and use in source and binary forms, with or without modification, are permitted
provided that the following conditions are met:

	-Redistributions of source code must retain the above copyright notice, this list of conditions
	 and the following disclaimer.
	-Redistributions in binary form must reproduce the above copyright notice, this list of conditions
	 and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.*/


package edu.utep.trustlab.visko.web.html;
import edu.utep.trustlab.visko.planning.QueryEngine;
import edu.utep.trustlab.visko.planning.pipelines.Pipeline;
import edu.utep.trustlab.visko.planning.pipelines.PipelineSet;

public class ResultsTableHTML {
	public static String getHTML(QueryEngine engine, boolean withProvenance) {
		String html = "<table width=\"1000\" border=\"1\" class=\"visko\">";

		PipelineSet pipes = engine.getPipelines();

		if (pipes.size() > 0 && withProvenance) {	
			html += 
					"<tr>" +
					"<td><b>ID</b></td>" +
					"<td><b>Run</b></td>" +
					"<td><b>Run with Provenance</b></td>" +
					"<td><b>Configure</b></td>" +
					"<td><b>Abstraction </b></td>" +
					"<td><b>Primary Toolkit</b></td>" +
					"<td><b>Format</b></td>" +
					"<td><b>Description</b></td>" +
					"</tr>";
			html += getVisualizationAndPipelineResultRows(pipes, engine.getQuery().hasValidDataPointer());
		}	
		else if (!(pipes.size() > 0)) {
			html += "<tr><td><p>Empty Set</p></td></tr>";
		}
		else if (engine.getQuery().getViewerSetURI() != null && engine.isAlreadyVisualizableWithViewerSet()) {
			html += "<tr><td><p>Format can already be viewed by ViewerSet.</p></td></tr>";
		}
		else if (engine.getQuery().getTargetFormatURI() != null && engine.getQuery().getTargetFormatURI()
						.equals(engine.getQuery().getFormatURI())) {
			html += "<tr><td><p>Format is same as target format.</p></td></tr>";
		}
		html += "</table>";		
		return html;
	}

	private static String getVisualizationAndPipelineResultRows(PipelineSet pipes, boolean validDatasetReference) {
		String html = "";
		for (int i = 0; i < pipes.size(); i++) {
			html += "<tr>";
			html += "<td>" + i + "</td>";
			html += "<td>" + getExecutePipelineLink(i, pipes.getArtifactURL(), pipes.get(i).requiresInputURL(), pipes.get(i).hasAllInputParameters(), validDatasetReference, false) + "</td>";
			html += "<td>" + getExecutePipelineLink(i, pipes.getArtifactURL(), pipes.get(i).requiresInputURL(), pipes.get(i).hasAllInputParameters(), validDatasetReference, true) + "</td>";
			html += "<td>" + getEditParametersLink(i);
			html += "<td>" + getAbstractionLink(pipes.get(i)) + "</td>";
			html += "<td>" + getToolkitOfAbstractionLink(pipes.get(i)) + "</td>";
			html += "<td>" + getOutputFormatLink(pipes.get(i)) + "</td>";
			html += "<td>" + getShowPipelineLink(i) + "</td>";
			html += "</tr>";
		}

		return html;
	}
	
	private static String getEditParametersLink(int index){
		return "<a href=\"ViskoServletManager?requestType=edit-parameters&index=" + index + "\">Edit parameters</a>";
	}
	
	private static String getURIFragment(String uri){
		return uri.substring(uri.indexOf("#") + 1);
	}
	
	private static String getAbstractionLink(Pipeline pipe){	
		String viewURI = pipe.getViewURI();
		if(viewURI != null){
			String html = "<a href=\"" + viewURI + "\">" + 	getURIFragment(viewURI) +  "</a>";
			return html;
		}
		else
			return "Unknown";			
	}
	
	private static String getToolkitOfAbstractionLink(Pipeline pipe){	
		String toolkitURI = pipe.getToolkitThatGeneratesView();
		if(toolkitURI != null){
			String html = "<a href=\"" + toolkitURI + "\">" + 	getURIFragment(toolkitURI) +  "</a>";
			return html;
		}
		else
			return "Unknown";			
	}

	private static String getOutputFormatLink(Pipeline pipe){	
		String formatURI = pipe.getOutputFormat();
		if(formatURI != null){
			String html = "<a href=\"" + formatURI + "\">" + 	getURIFragment(formatURI) +  "</a>";
			return html;
		}
		else
			return "Unknown";			
	}

	private static String getShowPipelineLink(int index) {
		String html = "<a href=\"ViskoServletManager?requestType=show-pipeline&index=" + index;
		html += "\">Text</a> / <a href=\"Graph_Pipeline.html?index=" + index;
		html += "\">Graph</a>";
		return html;
	}

	private static String getExecutePipelineLink(int index, String artifactURL, boolean requiresInput, boolean hasAllParametersBound, boolean hasValidDataInput, boolean withProvenance) {
		String html = "<p>NULL</p>";
		String provenanceParameter = "";
		
		if(withProvenance)
			provenanceParameter = "provenance=true&";
			
		if (artifactURL != null && hasValidDataInput) {
			if(hasAllParametersBound)
				html = "<a href=\"ViskoServletManager?" + provenanceParameter + "requestType=execute-pipeline&index=" + index + "\">Execute</a>";
			else
				html = "<a href=\"ViskoServletManager?" + provenanceParameter + "requestType=edit-parameters&index=" + index + "\">Set parameters!</a>";
		}
		else if(!requiresInput){
			if(hasAllParametersBound)
				html = "<a href=\"ViskoServletManager?" + provenanceParameter + "requestType=execute-pipeline&index=" + index + "\">Execute</a>";
			else
				html = "<a href=\"ViskoServletManager?" + provenanceParameter + "&requestType=edit-parameters&index=" + index + "\">Set parameters!</a>";
		}		
		else
			html = "<b>Not a valid dataset reference in query: requires URL.</b>";
		
		return html;
	}
}
