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


package edu.utep.trustlab.visko.web.requestHandler.execution;


//import edu.utep.trustlab.visko.web.html.provenance.DataProvenanceHTML;
//import edu.utep.trustlab.visko.web.html.provenance.VisualizationProvenanceHTML;

import java.io.IOException;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.utep.trustlab.visko.planning.Pipeline;
import edu.utep.trustlab.visko.execution.PipelineExecutor;
import edu.utep.trustlab.visko.planning.QueryEngine;
import edu.utep.trustlab.visko.web.requestHandler.RequestHandlerRedirect;
import edu.utep.trustlab.visko.web.requestHandler.planning.PipelineExecutionStatusBean;

public class ExecutePipelineServlet extends RequestHandlerRedirect {
	
	public static final String JSP_PAGE = "/ExecutePipeline.jsp";

	public void doGet(HttpServletRequest request, HttpServletResponse response, HttpServlet servlet) throws IOException, ServletException {
		String provenance = request.getParameter("provenance");
		String stringIndex = request.getParameter("index");
		
		boolean captureProvenance = false;
		if(provenance != null)
			captureProvenance = true;
		
		int index = Integer.valueOf(stringIndex);
		QueryEngine engine = (QueryEngine) request.getSession().getAttribute("engine");
		Pipeline pipe = engine.getPipelines().get(index);
		
		HttpSession session = request.getSession();
		
		PipelineExecutor runningPipeline = (PipelineExecutor)session.getAttribute("runningPipeline");
		
		if(runningPipeline == null){
			System.out.println("kick off new pipeline executor.");
			runningPipeline = new PipelineExecutor(pipe);
			session.setAttribute("runningPipeline", runningPipeline);
			runningPipeline.process();
			System.out.println("sending redirect-----------------------------------------------------------");
			response.sendRedirect("ViskoServletManager?requestType=execute-pipeline&index=" + index);
		}		
		else{			
	        PipelineExecutionStatusBean statusBean = new PipelineExecutionStatusBean();
	        //
	        // If the ProcessSimulator (sim) object exists, the process is running.
	        //
	        
	        statusBean.setProcessRunning(runningPipeline.isRunning());
	        statusBean.setMessage(runningPipeline.getStatusMessage());

	        System.out.println("is pipeline running: " + runningPipeline.isRunning());
	        System.out.println("what is the message: " + runningPipeline.getStatusMessage());
	            //
	            // Once the process is complete, remove the binding from 
	            // the session.
	            //
	        if(runningPipeline.isComplete()){
	        	
	        	String resultURL = runningPipeline.getResultURL();
	        	String resultURLHTML;
	        	if(
	        			resultURL.endsWith(".jpg") ||
	        			resultURL.endsWith(".JPG") ||
	        			resultURL.endsWith(".png") ||
	        			resultURL.endsWith(".PNG") ||
	        			resultURL.endsWith(".gif") ||
	        			resultURL.endsWith(".GIF"))
	        		resultURLHTML = "<img src=\"" + resultURL + "\" />";
	        	
	        	else if(resultURL.endsWith(".pdf") || resultURL.endsWith(".PDF"))
	        		resultURLHTML = "<a href=\"" + resultURL + "\">Click to view PDF</a>";
	        	else{
	        		Vector<String> viewerSets = runningPipeline.getPipeline().getViewerSets();
	        		resultURLHTML = "<h4>Result</h4>";
	        		resultURLHTML += "<ul>";
	        		resultURLHTML += "<li>Resultant URL: <a href=\"" + resultURL + "\">" + resultURL + "</a></li>";
	        		resultURLHTML += "<li>Viewed Using: " + runningPipeline.getPipeline().getViewerURI();
	        		resultURLHTML += "<li>Using the ViewerSet(s)</li>";
	        		resultURLHTML += "<ul>";
	        		for(String viewerSet : viewerSets)
	        			resultURLHTML += "<li>" + viewerSet + "</li>";
	        		
	        		resultURLHTML += "</ul>";
	        		resultURLHTML += "</ul>";
	        	}

	        	statusBean.setMessage(resultURLHTML);
	        	statusBean.setLinkToQuery();
	        	session.removeAttribute("runningPipeline");
	        }

	        request.setAttribute("statusBean", statusBean);
	        forward(JSP_PAGE, request, response, servlet);
	        return;

			
		}		
	}
	
	 /*
     * private method for forwarding to the view.
     */
    private void forward(String resource, HttpServletRequest  request, HttpServletResponse response, HttpServlet servlet) throws ServletException, IOException{
        servlet.getServletContext().getRequestDispatcher(resource).forward(request, response);
    }
}
