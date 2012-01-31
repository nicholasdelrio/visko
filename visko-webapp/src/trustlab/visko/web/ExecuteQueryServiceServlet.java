package trustlab.visko.web;

import trustlab.visko.web.context.ViskoContext;
import trustlab.visko.web.html.QueryMessages;
import trustlab.visko.execution.*;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class GetTransformersServlet
 */
public class ExecuteQueryServiceServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    	
	private Query query;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ExecuteQueryServiceServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		ViskoContext.setContext(this);
		
		String stringQuery = request.getParameter("query");
		String num = request.getParameter("maxResults");
		
		int maxResults = 100;
		
		if(num != null)
			maxResults = Integer.parseInt(num);
		
		String returnMessage;
		
		if(stringQuery != null)
		{
			query = new Query(stringQuery);

			System.out.println(query.getArtifactURL());
			System.out.println(query.getFormatURI());
			System.out.println(query.getTypeURI());
			System.out.println(query.getViewerSetURI());
			System.out.println(query.getViewURI());
			System.out.println(query.getNodesetURI());
		
			QueryEngine engine = new QueryEngine(query);
		
			if(query.isValidQuery())
			{
				PipelineSet pipelines = engine.getPipelines();
				returnMessage = PipelineToXMLVisualizationSet.toXMLFromPipelineSet(pipelines, query.getNodesetURI(), maxResults);
			}
			else
			{
				String errors = QueryMessages.getQueryErrorsHTML(query);
				returnMessage = "<html><body>" + errors + "</body></html>";
				//String warns = QueryMessages.getQueryWarningsHTML(query);
			}
		}
		else
			returnMessage = "<html><body><p>Failed to Specify Query via the query parameter</p></body></html>";
		
		response.getWriter().write(returnMessage);
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
}
