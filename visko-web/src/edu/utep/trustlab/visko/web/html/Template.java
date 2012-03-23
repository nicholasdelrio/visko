package edu.utep.trustlab.visko.web.html;

public class Template {
	
	public static String getHeader(){
		return "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">" +
				"<html>" +
				"<head>" +
				"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">" +
				"<link rel=\"stylesheet\" type=\"text/css\" href=\"visko-style.css\" />" +
				"<title>Submit a Query</title>" +
				"</head>" +
				"<body>" +
				"<div id=\"container\">" +
				"<div id=\"header\">" +
				"    <img src=\"visko-just-logo.png\" alt=\"VisKo Logo\" style=\"padding:5px;\" />" +
				"    <h1 style=\"padding-top:0px;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;A VisKo Instance</h1>" +
				"</div>" +
				"<div id=\"menu\">" +
				"	<a href=\"http://trust.utep.edu/visko\">VisKo Home</a>" +
				"    <a href=\".\">Instance Home</a>" +
				"    <a href=\"query.html\">Submit a VisKo Query</a>" +
				"    <a href=\".knowledge-base.html/\">Knowledge Base</a>" +
				"</div>" +
				"<div id=\"content\">";
	}
	
	public static String getFooter(){
		return 
				"</div>" +
				"</div>" +
				"<font color=\"white\"><i><b>Copyright @2012 CyberShARE Center, The University of Texas at El Paso</b></i></font>" +
				"<p>&nbsp;</p>" +
				"<p>&nbsp;</p>" +
				"<p>&nbsp;</p>" +
				"<p>&nbsp;</p>" +
				"<p>&nbsp;</p>" +
				"<p>&nbsp;</p>" +
				"<p>&nbsp;</p>" +
				"<p>&nbsp;</p>" +
				"<p>&nbsp;</p>" +
				"</body>" +
				"</html>";
	}
}
