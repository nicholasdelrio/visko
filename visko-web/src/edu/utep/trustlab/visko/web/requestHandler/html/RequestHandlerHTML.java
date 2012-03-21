package edu.utep.trustlab.visko.web.requestHandler.html;

import javax.servlet.http.HttpServletRequest;

import edu.utep.trustlab.visko.web.html.Template;

public abstract class RequestHandlerHTML {
	
	public String getHTMLPage(HttpServletRequest request){
		String html = doGet(request);
		return Template.getHeader() + html + Template.getFooter();
		
	}
	public abstract String doGet(HttpServletRequest request);
}
