package edu.utep.trustlab.visko.web.requestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.utep.trustlab.visko.web.html.Template;

public abstract class RequestHandlerHTML {
	
	public String getHTMLPage(HttpServletRequest request, HttpServletResponse response){
		response.setContentType("text/html");
		String html = doGet(request);
		return Template.getCompleteHeader() + html + Template.getCompleteFooter();
		
	}
	public abstract String doGet(HttpServletRequest request);
}
