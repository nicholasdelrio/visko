package edu.utep.trustlab.visko.web.requestHandler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class RequestHandlerRedirect {
	
	public void setRedirection(HttpServletRequest request, HttpServletResponse response, HttpServlet servlet) throws IOException, ServletException {
		doGet(request, response, servlet);		
	}
	public abstract void doGet(HttpServletRequest request, HttpServletResponse response, HttpServlet servlet) throws IOException, ServletException;
}
