package edu.utep.trustlab.visko.web.context;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import edu.utep.trustlab.contentManagement.AlfrescoClient;
import edu.utep.trustlab.contentManagement.CIServer;
import edu.utep.trustlab.contentManagement.ContentManager;
import edu.utep.trustlab.contentManagement.LocalFileSystem;
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
		
		//SPARQL Query Support
		setViskoSPARQLEndpointURL(context);
		setViskoTDBFilePath(context);
		
		//HTML Template Customization
		setLogo(context);
		setOrganization(context);
		
		//Provenance content manager
		setProvenanceContentManger(context);
	}
	
	private static void setProvenanceContentManger(ServletContext context){
		String managerType = context.getInitParameter("content-manager-type");
		String serverBasePath = context.getInitParameter("server-base-path");
	
		ContentManager manager;
		if(managerType.equals("local"))
			manager = getContentManagerProvenanceLocal(context);
		else if(managerType.equals("ciserver"))
			manager = getContentManagerProvenanceCIServer(context);
		else if(managerType.equals("alfresco"))
			manager = getContentManagerProvenanceAlfresco(context);
		else
			manager = getContentManagerProvenanceLocal(context);
	
		String pmlBasePath;
		if(serverBasePath.endsWith("/"))
			pmlBasePath = serverBasePath + "webapps/visko-web/output/";
		else
			pmlBasePath = serverBasePath + "/webapps/visko-web/output/";

		ContentManager.setProvenanceContentManager(manager);
		ContentManager.setWorkspacePath(pmlBasePath);
	}

	private static ContentManager getContentManagerProvenanceAlfresco(ServletContext context){
		String contentManagerURL = context.getInitParameter("content-manager-url");
		String contentManagerUsername = context.getInitParameter("content-manager-username");
		String contentManagerPassword = context.getInitParameter("content-manager-password");
		String contentManagerProject = context.getInitParameter("content-manager-project");
		
		AlfrescoClient alfresco = new AlfrescoClient(contentManagerURL, contentManagerUsername, contentManagerPassword);
		alfresco.setProjectName(contentManagerProject);
		return alfresco;
	}
	
	private static ContentManager getContentManagerProvenanceCIServer(ServletContext context){
		String contentManagerURL = context.getInitParameter("content-manager-url");
		String contentManagerUsername = context.getInitParameter("content-manager-username");
		String contentManagerPassword = context.getInitParameter("content-manager-password");
		String contentManagerProject = context.getInitParameter("content-manager-project");
		
		CIServer ciServer = new CIServer(contentManagerURL, contentManagerUsername, contentManagerPassword);
		ciServer.setProjectName(contentManagerProject);
		return ciServer;
	}
	
	private static ContentManager getContentManagerProvenanceLocal(ServletContext context){
		String serverURL = context.getInitParameter("server-url");
		String serverBasePath = context.getInitParameter("server-base-path");
		
		String pmlBaseURL;
		if(serverURL.endsWith("/"))
			pmlBaseURL = serverURL + "visko-web/output/";
		else
			pmlBaseURL = serverURL + "/visko-web/output/";
		
		String pmlBasePath;
		if(serverBasePath.endsWith("/"))
			pmlBasePath = serverBasePath + "webapps/visko-web/output/";
		else
			pmlBasePath = serverBasePath + "/webapps/visko-web/output/";
		
		LocalFileSystem fs = new LocalFileSystem(pmlBaseURL, pmlBasePath);
		return fs;
	}

	private static void setOrganization(ServletContext context){
		String organization = context.getInitParameter("hosting-organization");
		Template.setOrganization(organization);
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
