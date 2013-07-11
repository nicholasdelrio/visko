<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"
	import="edu.utep.trustlab.visko.web.html.Template,
			edu.utep.trustlab.visko.ontology.vocabulary.supplemental.*,
	        edu.utep.trustlab.visko.ontology.vocabulary.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" href="visko-style.css" />
<title>VisKo SPARQL Endpoint Interface</title>
</head>
<body>
<div id="container">

<%= Template.getHeader() %>

<div id="content">

<h2>VisKo Knowledge Base SPARQL Submission</h2>
<form action="ViskoServletManager">
    <input type="hidden" name="requestType" value="query-triple-store" /> 
	<textarea style="width: 700px; height: 300px" id="queryText" name="query">
PREFIX viskoV: &lt;<%= Visko.CORE_VISKO_V %>#&gt;
PREFIX viskoO: &lt;<%= Visko.CORE_VISKO_O %>#&gt;
PREFIX viskoS: &lt;<%= Visko.CORE_VISKO_S %>#&gt;
PREFIX owlsService: &lt;<%= OWLS_Service.ONTOLOGY_OWLS_SERVICE_URI %>#&gt;
PREFIX owlsProcess: &lt;<%= OWLS_Process.ONTOLOGY_OWLS_PROCESS_URI %>#&gt;
PREFIX owl: &lt;http://www.w3.org/2002/07/owl#&gt;
PREFIX rdfs: &lt;http://www.w3.org/2000/01/rdf-schema#&gt;
PREFIX xsd: &lt;http://www.w3.org/2001/XMLSchema#&gt;
PREFIX rdf: &lt;http://www.w3.org/1999/02/22-rdf-syntax-ns#&gt;


</textarea>
	<br>
	<input type="submit" id="submitButton" style="width: 156px">
</form>

<h2>Examples</h2>

<h3>What Visualizations can VisKo Generate?</h3>
<pre>
select ?view
where
{
?view a viskoV:VisualizationAbstraction.
} 
</pre>

<h3>What Operators Does VisKo Know About?</h3>
<pre>
select ?operator ?label
where
{
?operator a viskoO:Operator. 
?operator rdfs:label ?label
}
</pre>

<h3>What Toolkits Does VisKo Know About?</h3>
<pre>
select ?toolkit ?label
where
{
?toolkit a viskoS:Toolkit. 
?toolkit rdfs:label ?label.
}
</pre>

</div>
</div>

<%= Template.getFooter() %>

</body>
</html>