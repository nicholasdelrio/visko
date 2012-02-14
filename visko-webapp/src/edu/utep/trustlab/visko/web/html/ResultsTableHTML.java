package edu.utep.trustlab.visko.web.html;


import edu.utep.trustlab.visko.execution.PipelineSet;
import edu.utep.trustlab.visko.execution.QueryEngine;

public class ResultsTableHTML
{
	public static String getHTML(QueryEngine engine, boolean withProvenance)
	{
		String html = "<table width=\"700\" border=\"1\">";
		
		PipelineSet pipes = engine.getPipelines();
	
		if(pipes.size() > 0 && withProvenance)
		{
			html += "<tr><td><b>Visualizations Bound to ?DATA</b></td><td><b>Visualizations Bound to ?DATA with Provenance</b></td><td><b>How to Generate ?DATA</b></td></tr>";
			html += getVisualizationAndPipelineResultRows(pipes, true);
		}
		else if(pipes.size() > 0 && !withProvenance)
		{
			html += "<tr><td><b>Visualizations Bound to ?DATA</b></td><td><b>How to Generate ?DATA</b></td></tr>";
			html += getVisualizationAndPipelineResultRows(pipes, false);
		}
		else if(!(pipes.size() > 0))
		{
			html += "<tr><td><p>Empty Set</p></td></tr>";
		}
		else if(engine.getQuery().getViewerSetURI() != null && engine.isAlreadyVisualizableWithViewerSet())
		{
			html += "<tr><td><p>Format can already be viewed by ViewerSet.</p></td></tr>";
		}
		else if(engine.getQuery().getTargetFormatURI() != null && engine.getQuery().getTargetFormatURI().equals(engine.getQuery().getFormatURI()))
		{
			html += "<tr><td><p>Format is same as target format.</p></td></tr>";
		}
		html += "</table>";
		return html;
	}
	
	private static String getVisualizationAndPipelineResultRows(PipelineSet pipes, boolean withProvenance)
	{
		String html = "";
		for(int i = 0; i < pipes.size(); i ++)
		{	
			html += "<tr>";
			html += "<td>" + getExecutePipelineLink(i, pipes.getArtifactURL()) + "</td>";
			if(withProvenance)
				html += "<td>" + getExecutePipelineProvenanceLink(i, pipes.getArtifactURL()) + "</td>";
			html += "<td>" + getShowPipelineLink(i) + "</td>";
			html += "</tr>";
		}
		
		return html;
	}
	
	private static String getShowPipelineLink(int index)
	{
		String html = "";
		html = "<a href=\"ShowPipelineServlet?index=" + index;
		html += "\">Pipeline</a>";
		return html;
	}
	
	private static String getExecutePipelineLink(int index, String artifactURL)
	{
		String html = "<p>NULL</p>";
		
		if(artifactURL != null)
		{
			html = "<a href=\"ExecutePipelineServlet?index=" + index;
			html += "\">Visualization</a>";
		}
		return html;
	}
	
	private static String getExecutePipelineProvenanceLink(int index, String artifactURL)
	{
		String html = "<p>NULL</p>";
		
		if(artifactURL != null)
		{
			html = "<a href=\"ExecutePipelineServlet?index=" + index + "&provenance=true";
			html += "\">Visualization and Provenance</a>";
		}
		return html;
	} 
}
