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

import edu.utep.trustlab.visko.web.context.ViskoWebSession;
import edu.utep.trustlab.visko.web.requestHandler.RequestHandlerRedirect;

public class ExecutePipelineCancelServlet extends RequestHandlerRedirect {

	public void doGet(HttpServletRequest request, HttpServletResponse response, HttpServlet servlet) throws IOException, ServletException {				
		ViskoWebSession session = (ViskoWebSession) request.getSession().getAttribute(ViskoWebSession.SESSION_ID);

		//if the pipeline is running correctly
		if(session.hasPipelineExecutor() && session.getPipelineExecutor().isAlive()){
			//kill and remove it then
			session.getPipelineExecutor().scheduleForTermination();
			session.removePipelineExecutor();
		}
		//if the pipeline has died for some reason before we process this cancellation
		else if(session.hasPipelineExecutor() && !session.getPipelineExecutor().isAlive())
			//just remove it
			session.removePipelineExecutor();
		
		response.sendRedirect("ViskoServletManager?requestType=execute-query&reuse=true");
	}	
}
