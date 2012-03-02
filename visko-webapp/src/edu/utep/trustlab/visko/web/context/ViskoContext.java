package edu.utep.trustlab.visko.web.context;

import javax.servlet.http.HttpServlet;

import edu.utep.trustlab.visko.sparql.ViskoTripleStore;

public class ViskoContext {
	public static void setContext(HttpServlet servlet) {
		setViskoJosekiURL(servlet);
	}

	private static void setViskoJosekiURL(HttpServlet servlet) {
		String viskoEndpoint = servlet.getInitParameter("visko-joseki-url");
		ViskoTripleStore.setEndpointURL(viskoEndpoint);

		System.out.println("visko tdb endpoint: "
				+ ViskoTripleStore.getEndpontURL());
	}
}
