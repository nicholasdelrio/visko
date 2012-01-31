package trustlab.visko.web.context;

import javax.servlet.http.HttpServlet;

import trustlab.ciclient.ViskoCIServerInterface;

public class ViskoContext
{	
	public static String CI_SERVER_CONTEXT;
	public static String VISKO_TRIPLE_STORE_LOCATION;

	
	public static void setContext(HttpServlet servlet)
	{
		setCIServerContext(servlet);
		setViskoTripleStoreLocation(servlet);
	}
	
    private static void setCIServerContext(HttpServlet servlet)
    {
    	CI_SERVER_CONTEXT = servlet.getInitParameter("ci-server-context");
    	ViskoCIServerInterface.setCIServerContext(CI_SERVER_CONTEXT);
    	
    	System.out.println("ci-server context: " + CI_SERVER_CONTEXT);
    }
    
    private static void setViskoTripleStoreLocation(HttpServlet servlet)
    {
    	VISKO_TRIPLE_STORE_LOCATION = servlet.getInitParameter("visko-tdb-path");
    	
    	System.out.println("visko tdb store: " + VISKO_TRIPLE_STORE_LOCATION);
    } 
}
