package edu.utep.trustlab.visko.installation.packages.service.context;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import edu.utep.trustlab.visko.installation.packages.service.operators.util.FileUtils;

public class ContextListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent event) {
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		ServletContext context = event.getServletContext();
		setServerURL(context);
		setServerBasePath(context);
	}
	
	private static void setServerBasePath(ServletContext context){
		String serverBasePath = context.getInitParameter("server-base-path");
		FileUtils.setDeploymentPath(serverBasePath);
	}
	
	private static void setServerURL(ServletContext context){
		String serverURL = context.getInitParameter("server-url");
		FileUtils.setServerURL(serverURL);		
	}
}