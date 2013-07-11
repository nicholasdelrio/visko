package edu.utep.trustlab.visko.web.html;

import java.io.StringWriter;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

import edu.utep.trustlab.visko.sparql.ViskoTripleStore;

public class ModulesTableHTML {
	private static String html;
	public static String getHTML(){
		
		if(html != null)
			return html;
		
		ViskoTripleStore ts = new ViskoTripleStore();
		
		ResultSet rs = ts.getModuleInformation();
		QuerySolution solution;
		String toolkitLabel;
		String vendorURL;
		String detailsURL;
		String sourceURL;
		String moduleName;
		
		StringWriter writer = new StringWriter();
		writer.write("<table border=\"1\" class=\"visko\">");

		writer.write("<tr>\n");
		writer.write("<td><b>Toolkit</b></td>\n");
		writer.write("<td><b>Vendor</b></td>\n");
		writer.write("<td><b>Module Details</b></td>\n");
		writer.write("<td><b>Module Code</b></td>\n");
		writer.write("</tr>\n");
		
		while(rs.hasNext()){
			solution = rs.next();
			moduleName = solution.get("?moduleName").toString();
			toolkitLabel = solution.get("?toolkitLabel").toString();
			vendorURL = solution.get("?vendorURL").toString();
			detailsURL = "<a href=\"" + solution.get("?detailsURL").toString() + "\">" + moduleName + ".html" + "</a>";
			sourceURL = "<a href=\"" + solution.get("?sourceURL").toString() + "\">ModuleRDFRegistration.java</a>";
		
			writer.write("<tr>\n");
			writer.write("<td>" + toolkitLabel + "</td>\n");
			writer.write("<td>" + vendorURL + "</td>\n");
			writer.write("<td>" + detailsURL + "</td>\n");
			writer.write("<td>" + sourceURL + "</td>\n");
			writer.write("</tr>\n");
		}
		
		writer.write("</table>\n");
		html = writer.toString();
		return html;
	}
}