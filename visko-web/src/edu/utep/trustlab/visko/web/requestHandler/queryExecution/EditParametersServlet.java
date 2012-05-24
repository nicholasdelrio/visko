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


package edu.utep.trustlab.visko.web.requestHandler.queryExecution;

import javax.servlet.http.HttpServletRequest;

import edu.utep.trustlab.visko.execution.Pipeline;
import edu.utep.trustlab.visko.execution.QueryEngine;
import edu.utep.trustlab.visko.web.html.ParameterBindingsHTML;
import edu.utep.trustlab.visko.web.requestHandler.RequestHandlerHTML;
public class EditParametersServlet extends RequestHandlerHTML{

	public String doGet(HttpServletRequest request){

		String stringIndex = request.getParameter("index");
		int index = Integer.valueOf(stringIndex);

		QueryEngine engine = (QueryEngine) request.getSession().getAttribute("engine");

		Pipeline pipe = engine.getPipelines().get(index);

		String html = "<h2>Edit Parameter Values</h2>";
		String paramsList = ParameterBindingsHTML.getParameterBindingsList(pipe);
		
		html = "<form action=\"ViskoServletManager\"><input type=\"hidden\" name=\"index\" value=\"" + index + "\" /><input type=\"hidden\" name=\"requestType\" value=\"execute-pipeline\" />" + paramsList + "<input name\"Execute Pipeline\" type=\"submit\" id=\"submitButton\" style=\"width: 156px\"></form>";
		return html;
	}
}
