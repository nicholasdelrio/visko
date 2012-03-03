package edu.utep.trustlab.visko.web;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.utep.trustlab.visko.web.json.FormatGraphData;
import edu.utep.trustlab.visko.web.json.OperatorGraphData;
import edu.utep.trustlab.visko.web.json.InstanceBarGraphData;
import edu.utep.trustlab.visko.web.context.ViskoContext;

/**
 * Servlet implementation class KnowledgeBaseIndividuals
 */
public class KnowledgeBaseInformationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public KnowledgeBaseInformationServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		ViskoContext.setContext(this);
		String infoType = request.getParameter("info");

		if (infoType.equals("graph")) {
			response.getWriter().write(InstanceBarGraphData.getBarGraph());
		} else if (infoType.equals("formatPaths")) {
			response.getWriter().write(FormatGraphData.getPathsGraphJSON());
		} else if (infoType.equals("pipelines")) {
			response.getWriter().write(OperatorGraphData.getPathsGraphJSON());
		}

		else
			response.getWriter().write("{}");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
