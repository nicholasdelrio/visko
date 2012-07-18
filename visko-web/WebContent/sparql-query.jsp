<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"
	import="edu.utep.trustlab.visko.web.html.Template" %>
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
PREFIX viskoV: &lt;https://raw.github.com/nicholasdelrio/visko-rdf/master/rdf/ontology/visko-view.owl#&gt;
PREFIX viskoO: &lt;https://raw.github.com/nicholasdelrio/visko-rdf/master/rdf/ontology/visko-operator.owl#&gt;
PREFIX viskoS: &lt;https://raw.github.com/nicholasdelrio/visko-rdf/master/rdf/ontology/visko-service.owl#&gt;
PREFIX owlsService: &lt;http://www.daml.org/services/owl-s/1.2/Service.owl#&gt;
PREFIX owlsProcess: &lt;http://www.daml.org/services/owl-s/1.2/Process.owl#&gt;
PREFIX owl: &lt;http://www.w3.org/2002/07/owl#&gt;
PREFIX rdfs: &lt;http://www.w3.org/2000/01/rdf-schema#&gt;
PREFIX xsd: &lt;http://www.w3.org/2001/XMLSchema#&gt;
PREFIX rdf: &lt;http://www.w3.org/1999/02/22-rdf-syntax-ns#&gt;
	</textarea>
	<br>
	<input type="submit" id="submitButton" style="width: 156px">
</form>

<h2>Examples</h2>
<h3>Can VisKo Render a 3D Binary Float Array in a format that Mozilla Firefox can Display?</h3>
<pre>
ASK		
WHERE
{{
?viewer viskoO:partOfViewerSet  .
?viewer viskoO:operatesOn ?format .
 viskoO:canBeTransformedToTransitive ?format . 
}
UNION
{
?viewer viskoO:partOfViewerSet  .
?viewer viskoO:operatesOn ?format .
 viskoO:canBeTransformedTo ?format .
}
UNION
{
?viewer viskoO:partOfViewerSet  .
?viewer viskoO:operatesOn  .
}}
</pre>

<h3>What Formats Does VisKo Work With (i.e., read-in and write-out)</h3>
<pre>
SELECT DISTINCT ?format
WHERE
{{
?operator a viskoO:Operator .
?operator viskoO:operatesOn ?format .
}
UNION
{
?operator a viskoO:Transformer .
?operator viskoO:transformsTo ?format .
}}
</pre>

<h3>What views can VisKo Generate?</h3>
<pre>
select ?view ?label
where
{
?view a viskoV:View.
?view rdfs:label ?label
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