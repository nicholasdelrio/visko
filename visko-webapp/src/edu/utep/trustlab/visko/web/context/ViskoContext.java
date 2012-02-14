package edu.utep.trustlab.visko.web.context;

import javax.servlet.http.HttpServlet;

import edu.utep.trustlab.publish.CIServer;
import edu.utep.trustlab.publish.LocalServer;
import edu.utep.trustlab.publish.Server;
import edu.utep.trustlab.visko.sparql.ViskoTripleStore;

public class ViskoContext
{
	public static void setContext(HttpServlet servlet)
	{
		setServer(servlet);
		setViskoJosekiURL(servlet);
	}
	
    private static void setServer(HttpServlet servlet)
    {
    	String ciServerURL = servlet.getInitParameter("server-url");
    	
    	if(ciServerURL.contains("ciserver"))
    		Server.setServer(new CIServer(ciServerURL));
    	else
    		Server.setServer(new LocalServer(ciServerURL));
    	
    	System.out.println("server-url: " + Server.getServer().getBaseURL());
    }
    
    private static void setViskoJosekiURL(HttpServlet servlet)
    {
    	String viskoEndpoint = servlet.getInitParameter("visko-joseki-url");
    	ViskoTripleStore.setEndpointURL(viskoEndpoint);
    	
    	System.out.println("visko tdb endpoint: " + ViskoTripleStore.getEndpontURL());
    } 
}
