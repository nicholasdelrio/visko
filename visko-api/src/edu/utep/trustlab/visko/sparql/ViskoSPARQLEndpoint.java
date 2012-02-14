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