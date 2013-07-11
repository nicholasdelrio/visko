<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"
	import="edu.utep.trustlab.visko.web.html.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" href="visko-style.css" />
<title>VisKo Locally Available Modules</title>
</head>
<body>
<div id="container">

<%= Template.getHeader() %>

<div id="content">

<h3 style="padding:0px">[VisKo Modules]</h3>

<a name="Installed"></a>
<h2>Installed VisKo Modules</h2>

<%= ModulesTableHTML.getHTML() %>

</div>
</div>

<%= Template.getFooter() %>

</body>
</html>