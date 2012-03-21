package edu.utep.trustlab.visko.web.requestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class RequestHandlerJSON {
	
	public String getJSON(HttpServletRequest request, HttpServletResponse response){
		response.setContentType("application/json");
		return doGet(request);
	}
	public abstract String doGet(HttpServletRequest request);
}
