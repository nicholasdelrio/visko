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

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.utep.trustlab.visko.execution.PipelineExecutor;
import edu.utep.trustlab.visko.execution.PipelineExecutorJob;
import edu.utep.trustlab.visko.planning.QueryEngine;
import edu.utep.trustlab.visko.planning.pipelines.Pipeline;
import edu.utep.trustlab.visko.web.context.ViskoWebSession;
import edu.utep.trustlab.visko.web.requestHandler.RequestHandlerRedirect;

public class ExecutePipelineServlet extends RequestHandlerRedirect {
	
	public static final String JSP_PAGE = "/ExecutePipelineStatus.jsp";

	public void doGet(HttpServletRequest request, HttpServletResponse response, HttpServlet servlet) throws IOException, ServletException {
		String provenance = request.getParameter("provenance");
		String stringIndex = request.getParameter("index");

		boolean captureProvenance = false;
		if(provenance != null)
			captureProvenance = true;
		
		ViskoWebSession session = (ViskoWebSession) request.getSession().getAttribute(ViskoWebSession.SESSION_ID);
		ExecutePipelineStatusBean statusBean;
				
		if(!session.hasPipelineExecutor()){
			int index = Integer.valueOf(stringIndex);
			
			System.out.println("Kicking off new pipeline executor...");
			QueryEngine engine = session.getQueryEngine();
			Pipeline pipe = engine.getPipelines().get(index);
						
			PipelineExecutorJob job = new PipelineExecutorJob(pipe);
			job.setProvenanceLogging(captureProvenance);
			
			PipelineExecutor runningPipeline = new PipelineExecutor();
			runningPipeline.setJob(job);
			
			//add the running pipeline to the session object
			session.setPipelineExecutor(runningPipeline);
			runningPipeline.process();
			
			System.out.println("Redirecting to self...");
			if(captureProvenance)
				response.sendRedirect("ViskoServletManager?provenance=true&requestType=execute-pipeline&index=" + index);
			else
				response.sendRedirect("ViskoServletManager?provenance=true&requestType=execute-pipeline&index=" + index);
		}	
		else{		
	        statusBean = new ExecutePipelineStatusBean(session.getPipelineExecutor().getJob());
	        
	        if(!session.getPipelineExecutor().isAlive() || session.getPipelineExecutor().getJob().getJobStatus().isJobCompleted()){
	        	session.removePipelineExecutor();
	        }
	        
	        request.setAttribute("statusBean", statusBean);
	        forward(JSP_PAGE, request, response, servlet);
		}		
	}
	
	 /*
     * private method for forwarding to the view.
     */
    private void forward(String resource, HttpServletRequest  request, HttpServletResponse response, HttpServlet servlet) throws ServletException, IOException{
        servlet.getServletContext().getRequestDispatcher(resource).forward(request, response);
    }
}