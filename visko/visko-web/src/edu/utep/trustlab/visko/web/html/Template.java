package edu.utep.trustlab.visko.web.html;

public class Template {
	
	private static String logoPath;
	private static String org;
	public static void setLogoPath(String path){
		logoPath = path;
	}
	
	public static String getLogoPath(){
		return logoPath;
	}
	
	public static void setOrganization(String organization){
		org = organization;
	}
	
	public static String getOrganization(){
		return org;
	}
	
	public static String getCompleteHeader(){
		return "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">" +
				"<html>" +
				"<head>" +
				"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">" +
				"<link rel=\"stylesheet\" type=\"text/css\" href=\"visko-style.css\" />" +
				"<title>Visko Server</title>" +
				"</head>" +
				"<body>" +
				"<div id=\"container\">" +
				"<div id=\"header\">" +
				"	 <img src=\"" + Template.getLogoPath() + "\" style=\"padding:5px;\" + />" +
				"    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
				"    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
				"    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
				"    <h1 style=\"padding-top:0px;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + Template.getOrganization() + " VisKo Server</h1>" +
				"</div><br>" +
				"<div id=\"menu\">" +
				"    <a href=\".\">Server Home</a>" +
				"    <a href=\"query.jsp\">Query Examples</a>" +
				"    <a href=\"knowledge-base.jsp\">Knowledge Base</a>" +
				"    <a href=\"modules.jsp\">Modules</a>" +
				"	 <a href=\"http://trust.utep.edu/visko\">VisKo Home</a>" +
				"</div>" +
				"<div id=\"content\">";
	}
	
	public static String getHeader(){
		System.out.println("getting header stuff");
		return "<div id=\"header\">" +
				"	 <img src=\"" + Template.getLogoPath() + "\" style=\"padding:5px;\" />" +
				"    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
				"    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
				"    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
				"    <h1 style=\"padding-top:0px;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + Template.getOrganization() + " VisKo Server</h1>" +
				"</div><br>" +
				"<div id=\"menu\">" +
				"    <a href=\".\">Server Home</a>" +
				"    <a href=\"query.jsp\">Query Examples</a>" +
				"    <a href=\"knowledge-base.jsp\">Knowledge Base</a>" +
				"    <a href=\"modules.jsp\">Modules</a>" +
				"	 <a href=\"http://trust.utep.edu/visko\">VisKo Home</a>" +
				"</div>";
	}
	
	public static String getFooter(){
		return 
				"<font color=\"white\"><i><b>Copyright @2012 CyberShARE Center, The University of Texas at El Paso</b></i></font>" +
				"<p>&nbsp;</p>" +
				"<p>&nbsp;</p>" +
				"<p>&nbsp;</p>" +
				"<p>&nbsp;</p>" +
				"<p>&nbsp;</p>" +
				"<p>&nbsp;</p>" +
				"<p>&nbsp;</p>" +
				"<p>&nbsp;</p>" +
				"<p>&nbsp;</p>";
	}
	
	public static String getCompleteFooter(){
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
