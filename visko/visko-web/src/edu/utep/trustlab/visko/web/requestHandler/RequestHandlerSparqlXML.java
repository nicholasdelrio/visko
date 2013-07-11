package edu.utep.trustlab.visko.web.requestHandler;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class RequestHandlerSparqlXML {
	
	public void setSparqlResults(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/plain");
		response.getWriter().write(doGet(request));
	}
	public abstract String doGet(HttpServletRequest request);
}
