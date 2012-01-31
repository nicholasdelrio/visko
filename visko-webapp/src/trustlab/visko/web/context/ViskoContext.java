package trustlab.visko.web.context;

import javax.servlet.http.HttpServlet;

import trustlab.server.CIServer;
import trustlab.server.LocalServer;
import trustlab.server.Server;
import trustlab.visko.sparql.ViskoTripleStore;

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
