package edu.utep.trustlab.visko.web;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ViskoServletManager
 */
public class ViskoServletManager extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ViskoServletManager() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String requestType = request.getParameter("requestType");
		
		if(requestType.equalsIgnoreCase("execute-pipeline"))
			new ExecutePipelineServlet().doGet(request, response);
		else if(requestType.equalsIgnoreCase("execute-query-service"))
			new ExecuteQueryServiceServlet().doGet(request, response);
		else if(requestType.equalsIgnoreCase("execute-query"))
			new ExecuteQueryServlet().doGet(request, response);
		else if(requestType.equalsIgnoreCase("knowledge-base-info"))
			new KnowledgeBaseInformationServlet().doGet(request, response);
		else if(requestType.equalsIgnoreCase("show-pipeline"))
			new ShowPipelineServlet().doGet(request, response);
		else
			response.getOutputStream().println("<html><body><p>Invalid argument specified for <b>requestType</b></body></html>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
