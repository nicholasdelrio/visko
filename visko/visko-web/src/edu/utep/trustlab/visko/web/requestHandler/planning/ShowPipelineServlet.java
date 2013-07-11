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

import javax.servlet.http.HttpServletRequest;

import edu.utep.trustlab.visko.planning.QueryEngine;
import edu.utep.trustlab.visko.planning.pipelines.Pipeline;
import edu.utep.trustlab.visko.web.context.ViskoWebSession;
import edu.utep.trustlab.visko.web.html.PipelineHTML;
import edu.utep.trustlab.visko.web.requestHandler.RequestHandlerHTML;
public class ShowPipelineServlet extends RequestHandlerHTML{

	public String doGet(HttpServletRequest request){

		String stringIndex = request.getParameter("index");
		int index = Integer.valueOf(stringIndex);

		ViskoWebSession session = (ViskoWebSession) request.getSession().getAttribute(ViskoWebSession.SESSION_ID);
		QueryEngine engine = session.getQueryEngine();

		Pipeline pipe = engine.getPipelines().get(index);

		String html = "<h2>Visualization Pipeline:</h2>";
		html += PipelineHTML.getPipelineHTML(pipe);

		html += "<h2>Pipeline Output Viewed By:</h2>";

		html += PipelineHTML.getViewerHTML(pipe);

		return html;
	}
}
