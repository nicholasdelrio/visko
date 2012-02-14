package edu.utep.trustlab.visko.web.html;

import edu.utep.trustlab.visko.execution.Query;

public class QueryMessages
{
    public static String getQueryErrorsHTML(Query query)
    {
    	String html = "<table width=\"700\" border=\"1\">";
    	
    	html += "<tr><td colspan=2>Query Errors</td></tr>";
    	html += "<tr><td><b>Variable</b></td><td><b>Message</b></td></tr>";
    	boolean error = false;
    	if(query.getFormatURI() == null)
    	{
    		error = true;
    		html += "<tr><td>FORMAT</td><td>Need to specify FORMAT of input data!</td></tr>";
    	}
    	if(query.getViewerSetURI() == null)
    	{
    		error = true;
    		html += "<tr><td>VIEWERSET</td><td>Need to specify VIEWERSET!</td></tr>";
    	}
    	html += "</table>";
    	
    	if(!error)
    		return null;
    	
    	return html;
    }
    
    public static String getQueryWarningsHTML(Query query)
    {
    	String html = "<table width=\"700\" border=\"1\">";
    	
       	html += "<tr><td colspan=2>Query Warnings</td></tr>";
    	html += "<tr><td><b>Variable</b></td><td><b>Message</b></td></tr>";
    	boolean warn = false;
    	if(query.getTypeURI() == null)
    	{
    		warn = true;
    		html += "<tr><td>TYPE</td><td>TYPE not specified. If all parameter bindings are not present in query then visualization will fail.</td></tr>";
    	}
    	if(query.getViewURI() == null)
    	{
    		warn = true;
    		html += "<tr><td>VIEW</td><td>Wildcard (*) used! May result in many different visualizations.</td></tr>";
    	}
    	if(query.getParameterBindings() == null)
    	{
    		warn = true;
    		html += "<tr><td>?BINDINGS</td><td>?BINDINGS unspecified in the query. System will roll back to defaults.</td></tr>";
    	}
    	if(query.getArtifactURL() == null)
    	{
    		warn = true;
    		html += "<tr><td>?CONTENTURL</td><td>?CONTENTURL unbound. No visualizations will be returned.</td></tr>";
    	}
    	html += "</table>";
    	
    	if(!warn)
    		return null;
    	
    	return html;
    }

}
