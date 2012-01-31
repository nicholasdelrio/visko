package trustlab.visko.web.context;

import javax.servlet.http.HttpServlet;

import trustlab.server.CIServer;
import trustlab.server.Server;
import trustlab.visko.sparql.ViskoTripleStore;

public class ViskoContext
{
	public static void setContext(HttpServlet servlet)
	{
		setCIServerContext(servlet);
		setViskoTripleStoreLocation(servlet);
	}
	
    private static void setCIServerContext(HttpServlet servlet)
    {
    	String ciServerURL = servlet.getInitParameter("ci-server-url");
    	Server.setServer(new CIServer(ciServerURL));
    	
    	System.out.println("ci-server-url: " + Server.getServer());
    }
    
    private static void setViskoTripleStoreLocation(HttpServlet servlet)
    {
    	String viskoEndpoint = servlet.getInitParameter("visko-tdb-path");
    	ViskoTripleStore.setEndpointURL(viskoEndpoint);
    	
    	System.out.println("visko tdb endpoint: " + ViskoTripleStore.getEndpontURL());
    } 
}
