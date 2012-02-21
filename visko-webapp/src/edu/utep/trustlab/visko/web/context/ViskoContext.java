package edu.utep.trustlab.visko.web.context;

import javax.servlet.http.HttpServlet;

import edu.utep.trustlab.repository.CIServer;
import edu.utep.trustlab.repository.LocalFileSystem;
import edu.utep.trustlab.repository.Repository;
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
    		Repository.setRepository(new CIServer(ciServerURL));
    	else
    		Repository.setRepository(new LocalFileSystem(ciServerURL));
    	
    	System.out.println("server-url: " + Repository.getRepository().getBaseURL());
    }
    
    private static void setViskoJosekiURL(HttpServlet servlet)
    {
    	String viskoEndpoint = servlet.getInitParameter("visko-joseki-url");
    	ViskoTripleStore.setEndpointURL(viskoEndpoint);
    	
    	System.out.println("visko tdb endpoint: " + ViskoTripleStore.getEndpontURL());
    } 
}
