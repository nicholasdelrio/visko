package edu.utep.trustlab.visko.web.context;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import edu.utep.trustlab.contentManagement.VeloClientAdapter;
import edu.utep.trustlab.contentManagement.CIServer;
import edu.utep.trustlab.contentManagement.ContentManager;
import edu.utep.trustlab.contentManagement.LocalFileSystem;
import edu.utep.trustlab.visko.sparql.SPARQL_EndpointFactory;
import edu.utep.trustlab.visko.web.html.Template;

public class ContextListener implements ServletContextListener {

	public static String serverBaseURL;
	
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		ServletContext context = event.getServletContext();
		
		//SPARQL Query Support
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
	
		
		
		String moveData = context.getInitParameter("move-data");
		
		ContentManager manager;
		if(managerType.equals("local"))
			manager = getContentManagerProvenanceLocal(context);
		else if(managerType.equals("ciserver"))
			manager = getContentManagerProvenanceCIServer(context);
		else if(managerType.equals("velo"))
			manager = getContentManagerProvenanceVelo(context);
		else
			manager = getContentManagerProvenanceLocal(context);
	
		String pmlBasePath;
		if(serverBasePath.endsWith("/"))
			pmlBasePath = serverBasePath + "webapps/visko-web/output/";
		else
			pmlBasePath = serverBasePath + "/webapps/visko-web/output/";
		
		System.out.println("workspace path: " + pmlBasePath);
		
		ContentManager.setProvenanceContentManager(manager);
		
		//use same manager and project for data
		if(moveData.equalsIgnoreCase("true"))
			ContentManager.setDataContentManager(manager);
		
		ContentManager.setWorkspacePath(pmlBasePath);
	}

	private static ContentManager getContentManagerProvenanceVelo(ServletContext context){
		String contentManagerURL = context.getInitParameter("content-manager-url");
		String contentManagerUsername = context.getInitParameter("content-manager-username");
		String contentManagerPassword = context.getInitParameter("content-manager-password");
		String contentManagerProject = context.getInitParameter("content-manager-project");
		
		VeloClientAdapter alfresco = new VeloClientAdapter(contentManagerURL, contentManagerUsername, contentManagerPassword);
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
		serverBaseURL = context.getInitParameter("server-url");
		if(!serverBaseURL.endsWith("/"))
			serverBaseURL = serverBaseURL + "/";
		
		String serverBasePath = context.getInitParameter("server-base-path");
		if(!serverBasePath.endsWith("/"))
			serverBasePath = serverBasePath + "/";		
		
		String pmlBaseURL;
		pmlBaseURL = serverBaseURL + "visko-web/output/";
		
		String pmlBasePath;
		pmlBasePath = serverBasePath + "/webapps/visko-web/output/";

		System.out.println("pml dump path: " + pmlBasePath);
		
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
		SPARQL_EndpointFactory.setUpEndpointConnection(tdbFilePath);		
	}
}
