package edu.utep.trustlab.visko.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.utep.trustlab.visko.execution.Pipeline;
import edu.utep.trustlab.visko.execution.QueryEngine;
import edu.utep.trustlab.visko.web.html.PipelineHTML;

/**
 * Servlet implementation class ShowPipelineServlet
 */
public class ShowPipelineServlet extends HttpServlet {
       
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * @see HttpServlet#HttpServlet()
     */
    public ShowPipelineServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String stringIndex = request.getParameter("index");
		int index = Integer.valueOf(stringIndex);
		
		QueryEngine engine = (QueryEngine)request.getSession().getAttribute("engine");
	
		Pipeline pipe = engine.getPipelines().get(index);
		
		String html = "<html><head><title>Visualization Pipeline Details</title></head><body>";
		html += "<h2>Visualization Pipeline:</h2>";
		html += PipelineHTML.getPipelineHTML(pipe);
		
		html += "<h2>Pipeline Output Viewed By:</h2>";
		
		html += PipelineHTML.getViewerHTML(pipe);
		html += "</body></html>";
		
		response.getWriter().write(html);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
