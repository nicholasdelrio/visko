<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"
	import="edu.utep.trustlab.visko.web.html.Template" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" href="visko-style.css" />
<title>Knowledge Base</title>
</head>
<body>
<div id="container">

<%= Template.getHeader() %>

<div id="content">

<h3 style="padding:0px">[VisKo Knowledge Base]</h3>

<table style="float:left">
<tr>
<td>
<div class="toc">
<h4 style="padding:0px">Contents</h4>
<dl>
<dd><a href="#Overview">1 Overview</a></dd>
<dd><a href="#Browsing">2 Capability Graphs</a></dd>
<dd><a href="#Querying">3 Capability Listing by Querying</a></dd>

</dl>
</div>
</td>
</tr>

</table>
<br>&nbsp;
<br>&nbsp;
<br>&nbsp;
<br>&nbsp;
<br>&nbsp;

<a name="Overview"></a>
<h2>Overview</h2>
<p>This page provides both visual and textual querying techniques for ascertaining the fitness of your knowledge base.</p>

<a name="Browsing"></a>
<h2>Class Instances</h2>
<iframe width="1050" height="350" src="Bars_Instances.html" frameborder="1"></iframe>

<h2>Format/Data Type Transformation Paths:</h2>
<iframe width="1050" height="700" src="Graph_DataTransformations.html" frameborder="1"></iframe>

<h2>Operator Pipeline Paths:</h2>
<iframe width="1050" height="700" src="Graph_OperatorPaths.html" frameborder="1"></iframe>

<a name="Querying"></a>
<h2>Querying your Knowledge Base</h2>
<p>You can submit SPARQL queries against the VisKo knowledge base.</p>
<ul><li><a href="sparql-query.jsp">Your SPARQL Query Endpoint</a></li></ul>

</div>
</div>

<%= Template.getFooter() %>

</body>
</html>