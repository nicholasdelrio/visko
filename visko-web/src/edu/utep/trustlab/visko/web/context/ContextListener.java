package edu.utep.trustlab.visko.web.context;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import edu.utep.trustlab.visko.sparql.ViskoTripleStore;
import edu.utep.trustlab.visko.web.html.Template;
import edu.utep.trustlab.visko.web.requestHandler.sparql.TDBTripleStore;

public class ContextListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		ServletContext context = event.getServletContext();
		setViskoSPARQLEndpointURL(context);
		setViskoTDBFilePath(context);
		setLogo(context);
	}
	
	private static void setLogo(ServletContext context){
		String logoPath = context.getInitParameter("logo-path");
		Template.setLogoPath(logoPath);
	}
	
	private static void setViskoTDBFilePath(ServletContext context){
		String tdbFilePath = context.getInitParameter("visko-tdb-path");
		TDBTripleStore.setTDBStoreFilePath(tdbFilePath);
		
	}
	private static void setViskoSPARQLEndpointURL(ServletContext context) {
		String viskoEndpoint = context.getInitParameter("visko-sparql-endpoint");
		ViskoTripleStore.setEndpointURL(viskoEndpoint);
	}

}
