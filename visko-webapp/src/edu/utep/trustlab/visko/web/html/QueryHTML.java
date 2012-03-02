package edu.utep.trustlab.visko.web.html;

import edu.utep.trustlab.visko.execution.Query;

public class QueryHTML {
	public static String getHTML(Query query) {
		String strQuery = query.toString();
		// String[] clauses = strQuery.split("\n");

		String html = "<pre>";
		// for(int i = 0; i < clauses.length; i ++)
		// {
		// if(i == 0)
		// {
		// String[] conjunctions = clauses[0].split("AND");
		// html += "<tr><td>" + conjunctions[0] + "AND</td><td>" +
		// conjunctions[1] + "</td></tr>";
		// }
		// else
		// {
		// html += "<tr><td></td><td>" + clauses[i] + "</td></tr>";
		// }
		// }

		html += strQuery;
		html += "</pre>";
		return html;
	}
}
