package trustlab.visko.web;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import trustlab.visko.web.html.provenance.DataProvenanceHTML;
import trustlab.visko.web.html.provenance.VisualizationProvenanceHTML;
import trustlab.visko.execution.Pipeline;
import trustlab.visko.execution.QueryEngine;

/**
 * Servlet implementation class ExecutePipelineServlet
 */
public class ExecutePipelineServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ExecutePipelineServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		String captureProvenance = request.getParameter("provenance");
		
		String stringIndex = request.getParameter("index");
		int index = Integer.valueOf(stringIndex);
		
		QueryEngine engine = (QueryEngine)request.getSession().getAttribute("engine");
		
		Pipeline pipe = engine.getPipelines().get(index);
		
		String result;
		String html = "";
		if(captureProvenance != null)
		{
			html +=  "<html><head><title>Resultant Visualization And Provenance</title></head><body>";
			result = pipe.executePath(true);
			
			if(result != null)
			{	
				VisualizationProvenanceHTML prov = new VisualizationProvenanceHTML(result);
				html += "<p>Reading Provenance of: <a href=\"" + result + "\">" + result + "</a>";
				html += prov.getPlotHTML();
				html += "<h3>Visualization Provenance</h3>";
				html += prov.getParameterTable();
				
				DataProvenanceHTML dataProv = new DataProvenanceHTML(result);
				html += "<h3>Data Provenance</h3>";
				html += dataProv.getGiovanniUserSelectionTable();
			}	
			else
				html += "<h1>ERROR: visualization or provenance could not be generated!</h1>";
		}
		
		else
		{
			result = pipe.executePath(false);
			html +=  "<html><head><title>Resultant Visualization</title></head><body>";
			
			if(result.endsWith("pdf") || result.endsWith("PDF"))
				html += "<a href=\"" + result + "\">Resultant PDF Document</a>";
			else
				html += "<img src=\"" + result + "\">";
		}
		
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
