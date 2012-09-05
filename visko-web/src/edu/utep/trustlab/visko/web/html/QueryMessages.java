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


package edu.utep.trustlab.visko.web.html;

import edu.utep.trustlab.visko.planning.Query;

public class QueryMessages {
	public static String getQueryErrorsHTML(Query query) {
		String html = "<table width=\"700\" border=\"1\">";

		html += "<tr><td colspan=2>Query Errors</td></tr>";
		html += "<tr><td><b>Variable</b></td><td><b>Message</b></td></tr>";
		boolean error = false;
		
		if (query.getFormatURI() == null && query.getNodesetURI() == null) {
			error = true;
			html += "<tr><td>PMLP:Format</td><td>Need to specify FORMAT of input data or specify a PML2 NodeSet URI!</td></tr>";
		}
		
		if (query.getViewerSetURI() == null) {
			error = true;
			html += "<tr><td>ViewerSet</td><td>Need to specify VIEWERSET!</td></tr>";
		}
		html += "</table>";

		if (!error)
			return null;

		return html;
	}

	public static String getQueryWarningsHTML(Query query) {
		String html = "<table width=\"700\" border=\"1\">";

		html += "<tr><td colspan=2>Query Warnings</td></tr>";
		html += "<tr><td><b>Variable</b></td><td><b>Message</b></td></tr>";
		boolean warn = false;
		if (query.getTypeURI() == null) {
			warn = true;
			html += "<tr><td>TYPE</td><td>TYPE not specified. If all parameter bindings are not present in query then visualization will fail.</td></tr>";
		}
		if (query.getViewURI() == null) {
			warn = true;
			html += "<tr><td>VIEW</td><td>Wildcard (*) used! May result in many different visualizations.</td></tr>";
		}
		if (query.getParameterBindings() == null) {
			warn = true;
			html += "<tr><td>PARAMETER BINDINGS</td><td>BINDINGS unspecified in the query. System will roll back to defaults.</td></tr>";
		}
		if(!query.hasValidDataPointer()){
			warn = true;
			html += "<tr><td>Input Dataset</td><td>No valid pointer (e.g., a URL) for input data provided. No pipeline will be executable.</td></tr>";
		}
		
		if (query.getArtifactURL() == null) {
			warn = true;
			html += "<tr><td>?CONTENT URL</td><td>?CONTENT URL unbound. No visualizations will be returned.</td></tr>";
		}
		html += "</table>";

		if (!warn)
			return null;

		return html;
	}

}
