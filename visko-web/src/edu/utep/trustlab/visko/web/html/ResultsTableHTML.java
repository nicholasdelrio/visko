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

import java.net.URI;

import edu.utep.trustlab.visko.planning.PipelineSet;
import edu.utep.trustlab.visko.planning.QueryEngine;

public class ResultsTableHTML {
	public static String getHTML(QueryEngine engine, boolean withProvenance) {
		String html = "<table width=\"700\" border=\"1\">";

		PipelineSet pipes = engine.getPipelines();

		if (pipes.size() > 0 && withProvenance) {
			html += "<tr><td><b>Index</b></td><td><b>Run</b><td><b>Configure</b></td></td><td><b>Resulting View</b></td><td><b>Run and Capture Provenance</b></td><td><b>Description</b></td></tr>";
			html += getVisualizationAndPipelineResultRows(pipes, true, engine.getQuery().hasValidDataPointer());
		} else if (pipes.size() > 0 && !withProvenance) {
			html += "<tr><td><b>Index</b></td><td><b>Run</b></td><td><b>Configure</b></td><td><b>Resulting View</b></td><td><b>Description</b></td></tr>";
			html += getVisualizationAndPipelineResultRows(pipes, false, engine.getQuery().hasValidDataPointer());
		} else if (!(pipes.size() > 0)) {
			html += "<tr><td><p>Empty Set</p></td></tr>";
		} else if (engine.getQuery().getViewerSetURI() != null && engine.isAlreadyVisualizableWithViewerSet()) {
			html += "<tr><td><p>Format can already be viewed by ViewerSet.</p></td></tr>";
		} else if (engine.getQuery().getTargetFormatURI() != null && engine.getQuery().getTargetFormatURI()
						.equals(engine.getQuery().getFormatURI())) {
			html += "<tr><td><p>Format is same as target format.</p></td></tr>";
		}
		html += "</table>";		
		return html;
	}

	private static String getVisualizationAndPipelineResultRows(PipelineSet pipes, boolean withProvenance, boolean validDatasetReference) {
		String html = "";
		for (int i = 0; i < pipes.size(); i++) {
			html += "<tr>";
			html += "<td>" + i + "</td>";
			html += "<td>" + getExecutePipelineLink(i, pipes.getArtifactURL(), pipes.get(i).requiresInputURL(), pipes.get(i).hasAllInputParameters(), validDatasetReference) + "</td>";
			html += "<td>" + getEditParametersLink(i);
			html += "<td>" + getViewLink(pipes.get(i).getViewURI()) + "</td>";
			if (withProvenance)
				html += "<td>"+ getExecutePipelineLinkWithProvenance(i, pipes.getArtifactURL(), pipes.get(i).requiresInputURL(), pipes.get(i).hasAllInputParameters(), validDatasetReference) + "</td>";
			
			html += "<td>" + getShowPipelineLink(i) + "</td>";
			html += "</tr>";
		}

		return html;
	}
	
	private static String getEditParametersLink(int index){
		return "<a href=\"ViskoServletManager?requestType=edit-parameters&index=" + index + "\">Edit parameters</a>";

	}
	
	private static String getViewLink(String viewURI){
		
		if(viewURI != null){
			try{
				String html = "<a href=\"" + viewURI + "\">" + 	new URI(viewURI).getFragment() +  "</a>";
				return html;
			}
			catch(Exception e){
				e.printStackTrace();
				return e.getMessage();
			}
		}
		else
			return "No View Information";
			
	}

	private static String getShowPipelineLink(int index) {
		String html = "";
		html = "<a href=\"ViskoServletManager?requestType=show-pipeline&index=" + index;
		html += "\">Text</a> / <a href=\"Pipeline.html?index=" + index;
		html += "\">Graph</a>";
		return html;
	}

	private static String getExecutePipelineLink(int index, String artifactURL, boolean requiresInput, boolean hasAllParametersBound, boolean hasValidDataInput) {
		String html = "<p>NULL</p>";

		if (artifactURL != null && hasValidDataInput) {
			
			if(hasAllParametersBound){
				html = 	"<a href=\"ViskoServletManager?requestType=execute-pipeline&index=" + index + "\">Run pipeline</a>";
			}
			else
				html = "<a href=\"ViskoServletManager?requestType=edit-parameters&index=" + index + "\">Need to set parameters!</a>";
		}
		else if(!requiresInput){
			if(hasAllParametersBound){
				html = 	"<a href=\"ViskoServletManager?requestType=execute-pipeline&index=" + index + "\">Run pipeline</a>";
			}
			else
				html = "<a href=\"ViskoServletManager?requestType=edit-parameters&index=" + index + "\">Need to set parameters!</a>";
			
		}		
		else
			html = "<b>Not a valid dataset reference in query: requires URL.</b>";
		return html;
	}

	private static String getExecutePipelineLinkWithProvenance(int index, String artifactURL, boolean requiresInput, boolean hasAllParametersBound, boolean hasValidDataInput) {
		String html = "<p>NULL</p>";

		if (artifactURL != null && hasValidDataInput) {
			
			if(hasAllParametersBound){
				html = "<a href=\"ViskoServletManager?provenance=true&requestType=execute-pipeline&index=" + index + "\">Run pipeline with Provenance Capture</a>";
			}
			else
				html = "<a href=\"ViskoServletManager?provenance=true&requestType=edit-parameters&index=" + index + "\">Need to set parameters!</a>";
		}
		else if(!requiresInput){
			if(hasAllParametersBound){
				html = "<a href=\"ViskoServletManager?provenance=true&requestType=execute-pipeline&index=" + index + "\">Run pipeline with Provenance Capture</a>";
			}
			else
				html = "<a href=\"ViskoServletManager?provenance=true&requestType=edit-parameters&index=" + index + "\">Need to set parameters!</a>";
		}		
		else
			html = "<b>Not a valid dataset reference in query: requires URL.</b>";
		return html;
	}
}
