package edu.utep.trustlab.visko.web.requestHandler;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.utep.trustlab.visko.web.html.Template;

public abstract class RequestHandlerHTML {
	
	public void setHTMLPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/html");
		String html = doGet(request);
		response.getWriter().write(Template.getCompleteHeader() + html + Template.getCompleteFooter());
	}
	
	public abstract String doGet(HttpServletRequest request);
}
