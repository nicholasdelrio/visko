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

import edu.utep.trustlab.visko.execution.PipelineSet;
import edu.utep.trustlab.visko.execution.QueryEngine;

public class ResultsTableHTML {
	public static String getHTML(QueryEngine engine, boolean withProvenance) {
		String html = "<table width=\"700\" border=\"1\">";

		PipelineSet pipes = engine.getPipelines();

		if (pipes.size() > 0 && withProvenance) {
			html += "<tr><td><b>Visualization</b></td><td><b>Type</b></td><td><b>Visualization with Provenance</b></td><td><b>Pipeline</b></td></tr>";
			html += getVisualizationAndPipelineResultRows(pipes, true);
		} else if (pipes.size() > 0 && !withProvenance) {
			html += "<tr><td><b>Visualization</b></td><td><b>Type</b></td><td><b>Pipeline</b></td></tr>";
			html += getVisualizationAndPipelineResultRows(pipes, false);
		} else if (!(pipes.size() > 0)) {
			html += "<tr><td><p>Empty Set</p></td></tr>";
		} else if (engine.getQuery().getViewerSetURI() != null
				&& engine.isAlreadyVisualizableWithViewerSet()) {
			html += "<tr><td><p>Format can already be viewed by ViewerSet.</p></td></tr>";
		} else if (engine.getQuery().getTargetFormatURI() != null
				&& engine.getQuery().getTargetFormatURI()
						.equals(engine.getQuery().getFormatURI())) {
			html += "<tr><td><p>Format is same as target format.</p></td></tr>";
		}
		html += "</table>";
		return html;
	}

	private static String getVisualizationAndPipelineResultRows(PipelineSet pipes, boolean withProvenance) {
		String html = "";
		
		for (int i = 0; i < pipes.size(); i++) {
			html += "<tr>";
			html += "<td>" + getExecutePipelineLink(i, pipes.getArtifactURL(), pipes.get(i).hasAllInputParameters()) + "</td>";			
			html += "<td>" + getViewLink(pipes.get(i).getView().getURI() + "</td>");
			if (withProvenance)
				html += "<td>"+ getExecutePipelineProvenanceLink(i, pipes.getArtifactURL()) + "</td>";
			
			html += "<td>" + getShowPipelineLink(i) + "</td>";
			html += "</tr>";
		}

		return html;
	}
	
	private static String getViewLink(String viewURI){
		try{
			String html = "<a href=\"" + viewURI + "\">" + 	new URI(viewURI).getFragment() +  "</a>";
			return html;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	private static String getShowPipelineLink(int index) {
		String html = "";
		html = "<a href=\"ViskoServletManager?requestType=show-pipeline&index=" + index;
		html += "\">Pipeline</a>";
		return html;
	}

	private static String getExecutePipelineLink(int index, String artifactURL, boolean hasAllParametersBound) {
		String html = "<p>NULL</p>";

		if (artifactURL != null) {
			
			if(hasAllParametersBound){
				html = "<a href=\"ViskoServletManager?requestType=execute-pipeline&index=" + index;
				html += "\">Visualization</a>";
			}
			else
				html = "<b>Not all pipeline parameters bound!</b>";
		}
		return html;
	}

	private static String getExecutePipelineProvenanceLink(int index,
			String artifactURL) {
		String html = "<p>NULL</p>";

		if (artifactURL != null) {
			html = "<a href=\"ExecutePipelineServlet?index=" + index
					+ "&provenance=true";
			html += "\">Visualization and Provenance</a>";
		}
		return html;
	}
}
