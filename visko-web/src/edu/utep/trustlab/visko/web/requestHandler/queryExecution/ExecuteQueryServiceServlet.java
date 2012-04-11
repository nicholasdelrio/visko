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

import edu.utep.trustlab.visko.web.html.QueryMessages;
import edu.utep.trustlab.visko.web.requestHandler.RequestHandlerHTML;
import edu.utep.trustlab.visko.execution.*;

public class ExecuteQueryServiceServlet  extends RequestHandlerHTML {
	private Query query;

	public String doGet(HttpServletRequest request){
		// TODO Auto-generated method stub

		String stringQuery = request.getParameter("query");
		String num = request.getParameter("maxResults");

		int maxResults = 100;

		if (num != null)
			maxResults = Integer.parseInt(num);

		String returnMessage;

		if (stringQuery != null) {
			query = new Query(stringQuery);

			System.out.println(query.getArtifactURL());
			System.out.println(query.getFormatURI());
			System.out.println(query.getTypeURI());
			System.out.println(query.getViewerSetURI());
			System.out.println(query.getViewURI());
			System.out.println(query.getNodesetURI());

			QueryEngine engine = new QueryEngine(query);

			if (query.isValidQuery()) {
				PipelineSet pipelines = engine.getPipelines();
				returnMessage = PipelineToXMLVisualizationSet.toXMLFromPipelineSet(pipelines, query.getNodesetURI(), maxResults);
			} else {
				String errors = QueryMessages.getQueryErrorsHTML(query);
				returnMessage = "<html><body>" + errors + "</body></html>";
				// String warns = QueryMessages.getQueryWarningsHTML(query);
			}
		} else
			returnMessage = "<html><body><p>Failed to Specify Query via the query parameter</p></body></html>";

		return returnMessage;
	}

}
