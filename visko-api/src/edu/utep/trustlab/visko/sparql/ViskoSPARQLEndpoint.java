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


package edu.utep.trustlab.visko.sparql;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFactory;
import com.hp.hpl.jena.query.ResultSetFormatter;

public class ViskoSPARQLEndpoint {
	
	private String endpointURL;
	
	public ViskoSPARQLEndpoint(String sparqlEndpointURL){
		endpointURL = sparqlEndpointURL;
	}
	
	public boolean executeAskQuery(String query) {
		String results = execute(query);

		if (results.contains("true"))
			return true;

		return false;
	}

	public ResultSet executeQuery(String query) {
		String results = execute(query);
		return ResultSetFactory.fromXML(results);
	}

	private String execute(String query) {
		try {
			String requestURL = endpointURL + "?query=" + URLEncoder.encode(query, "utf-8");

			URL someURL = new URL(requestURL);
			URLConnection urlc = someURL.openConnection();
			urlc.setRequestProperty("Accept", "application/xml");
			urlc.connect();

			Object resp = urlc.getContent();
			InputStream body = (InputStream) resp;
			InputStreamReader isr = new InputStreamReader(body);
			LineNumberReader lr = new LineNumberReader(isr);
			StringBuffer ret = new StringBuffer();

			while (true) {
				String line = lr.readLine();
				if (line == null) {
					break;
				}
				ret.append(line).append("\n");
			}

			return ret.toString();

		} catch (MalformedURLException u) {
			u.printStackTrace();
			return null;

		} catch (IOException i) {

			i.printStackTrace();
			return null;
		}
	}
}
