package edu.utep.trustlab.visko.web.requestHandler;

import java.io.IOException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.utep.trustlab.visko.web.html.Template;

public abstract class RequestHandlerURL {
	
	public void setURL(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/html");
		URL resultURL = doGet(request);
		response.getWriter().write(resultURL.toString());
	}
	
	public abstract URL doGet(HttpServletRequest request);
}
