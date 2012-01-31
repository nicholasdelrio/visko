<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    import="java.util.*"
    import="trustlab.visko.sparql.*"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Visko Entry Point</title>
</head>
<body>
<%	String artifactURL = (String)request.getParameter("artifactURL");
	String viewerSetURI = "http://rio.cs.utep.edu/ciserver/ciprojects/visko/mozilla-firefox1.owl%23mozilla-firefox1";
	response.sendRedirect("ExecuteVKQLQueryServlet?fromApp=entrypoint&artifactURL=" + artifactURL + "&viewerSetURI=" + viewerSetURI);
%>
</body>
</html>