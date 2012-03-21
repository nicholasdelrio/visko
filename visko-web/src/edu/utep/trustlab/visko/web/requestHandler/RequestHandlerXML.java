package edu.utep.trustlab.visko.web.requestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.utep.trustlab.visko.web.html.Template;

public abstract class RequestHandlerXML {
	
	public String getXMLResults(HttpServletRequest request, HttpServletResponse response){
		response.setContentType("application/sparql-results+xml ");
		return doGet(request);
	}
	public abstract String doGet(HttpServletRequest request);
}
