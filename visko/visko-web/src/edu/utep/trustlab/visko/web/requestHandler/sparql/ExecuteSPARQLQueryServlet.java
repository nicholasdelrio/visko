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


package edu.utep.trustlab.visko.web.requestHandler.sparql;

import javax.servlet.http.HttpServletRequest;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;

import edu.utep.trustlab.visko.sparql.SPARQL_EndpointFactory;
import edu.utep.trustlab.visko.web.requestHandler.RequestHandlerSparqlXML;

public class ExecuteSPARQLQueryServlet extends RequestHandlerSparqlXML {

	public String doGet(HttpServletRequest request){
		String query = request.getParameter("query");
	
		ResultSet results;
		if(query.toLowerCase().contains("ask"))
			return "<?xml version=\"1.0\"?><result>" + SPARQL_EndpointFactory.executeAskQuery(query) + "</result>";
		else {
			results = SPARQL_EndpointFactory.executeQuery(query);
			if(results != null){
				String xml = ResultSetFormatter.asXMLString(results);
				return xml;
			}
			else
				return "<?xml version=\"1.0\"?><message>Query Yielded a Null Response.</message>";
		}
	}
}
