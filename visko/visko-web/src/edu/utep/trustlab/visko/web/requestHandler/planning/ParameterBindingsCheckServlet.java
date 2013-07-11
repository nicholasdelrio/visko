/*
Copyright (c) 2012, University of Texas at El Paso
All rights reserved.
Redistribution and use in source and binary forms, with or without modification, are permitted
provided that the following conditions are met:

	-Redistributions of source code must retain the above copyright notice, this list of conditions
	 and the following disclaimer.
	-Redistributions in binary form must reproduce the above copyright notice, this list of conditions
	 and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.*/


package edu.utep.trustlab.visko.web.requestHandler.planning;


//import edu.utep.trustlab.visko.web.html.provenance.DataProvenanceHTML;
//import edu.utep.trustlab.visko.web.html.provenance.VisualizationProvenanceHTML;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.utep.trustlab.visko.planning.QueryEngine;
import edu.utep.trustlab.visko.planning.pipelines.Pipeline;
import edu.utep.trustlab.visko.web.context.ViskoWebSession;
import edu.utep.trustlab.visko.web.html.Template;
import edu.utep.trustlab.visko.web.requestHandler.RequestHandlerRedirect;

public class ParameterBindingsCheckServlet extends RequestHandlerRedirect {

	public void doGet(HttpServletRequest request, HttpServletResponse response, HttpServlet servlet) throws IOException {
		
		String captureProvenance = request.getParameter("provenance");
		String stringIndex = request.getParameter("index");
		String useURLBindings = request.getParameter("use-bindings-in-url");

		boolean useBindingsInURL = false;
		
		if(useURLBindings != null && useURLBindings.equalsIgnoreCase("true"))
			useBindingsInURL = true;
		
		int index = Integer.valueOf(stringIndex);
		ViskoWebSession session = (ViskoWebSession) request.getSession().getAttribute(ViskoWebSession.SESSION_ID);
		QueryEngine engine = session.getQueryEngine();
		
		Pipeline pipe = engine.getPipelines().get(index);

		if(useBindingsInURL){	
			System.out.println("updating bindings............................................................................");
			updateParameterBindingsFromURL(pipe, request);
		
			if(!pipe.hasAllInputParameters()){
				String errorHTML = "<h2>Cannot Execute Query, not all parameters bound!</h2>";
			
				errorHTML += "<ul>\n";
				for(String paramURI : pipe.getUnboundParameters()){
					errorHTML += "<li>" + paramURI + "</li>\n";
				}
				
				errorHTML += "</ul>";
				errorHTML = Template.getCompleteHeader() + errorHTML + Template.getCompleteFooter();
				response.getWriter().write(errorHTML);
				return;
			}
		}
		
		if(captureProvenance != null)
			response.sendRedirect("ViskoServletManager?requestType=execute-pipeline&index=" + stringIndex + "&provenance=" + captureProvenance);
		else
			response.sendRedirect("ViskoServletManager?requestType=execute-pipeline&index=" + stringIndex);
	}
	
	private static void updateParameterBindingsFromURL(Pipeline pipeline, HttpServletRequest request){
		
		String boundValue;
		List<String> parameters = pipeline.getAllParameters();
		System.out.println("number of parameters=====================================================" + parameters.size());
		for(String paramURI : parameters){
			System.out.println("attempting to bind: " + paramURI);
			boundValue = request.getParameter(paramURI);
			if(boundValue == null || boundValue.isEmpty()){
				System.out.println("couldn't find value bound to param: " + paramURI + " to " + boundValue);
			}
			else{
				System.out.println("found value to bind: " + boundValue);
				pipeline.getParameterBindings().put(paramURI, boundValue);
			}
		}
	}	
}
