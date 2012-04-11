<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"
	import="edu.utep.trustlab.visko.web.html.Template" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" href="visko-style.css" />
<title>Your Knowledge Base Instance</title>
</head>
<body>
<div id="container">

<%= Template.getHeader() %>

<div id="content">

<h3 style="padding:0px">[VisKo Knowledge Base Instance]</h3>

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
<h2>Format Transformation Paths:</h2>
<iframe width="900" height="500" src="FormatTransformationPaths.html" frameborder="1"></iframe>

<h2>Operator Pipelines</h2>
<iframe width="900" height="500" src="OperatorPipelines.html" frameborder="1"></iframe>

<a name="Querying"></a>
<h2>Querying your Knowledge Base</h2>
<p>You will use SPARQL to directly interface with your knowledge base instance.</p> 
<p>Questions/Answers</p>

<ul>
<li><a href="ViskoServletManager?requestType=query-triple-store&query=PREFIX+viskoV%3A+%3Chttp%3A%2F%2Ftrust.utep.edu%2Fvisko%2Fontology%2Fvisko-view-v3.owl%23%3E%0D%0APREFIX+viskoO%3A+%3Chttp%3A%2F%2Ftrust.utep.edu%2Fvisko%2Fontology%2Fvisko-operator-v3.owl%23%3E%0D%0APREFIX+viskoS%3A+%3Chttp%3A%2F%2Ftrust.utep.edu%2Fvisko%2Fontology%2Fvisko-service-v3.owl%23%3E%0D%0APREFIX+owlsService%3A+%3Chttp%3A%2F%2Fwww.daml.org%2Fservices%2Fowl-s%2F1.2%2FService.owl%23%3E%0D%0APREFIX+owlsProcess%3A+%3Chttp%3A%2F%2Fwww.daml.org%2Fservices%2Fowl-s%2F1.2%2FProcess.owl%23%3E%0D%0APREFIX+owl%3A+%3Chttp%3A%2F%2Fwww.w3.org%2F2002%2F07%2Fowl%23%3E%0D%0APREFIX+rdfs%3A+%3Chttp%3A%2F%2Fwww.w3.org%2F2000%2F01%2Frdf-schema%23%3E%0D%0APREFIX+xsd%3A+%3Chttp%3A%2F%2Fwww.w3.org%2F2001%2FXMLSchema%23%3E%0D%0APREFIX+rdf%3A+%3Chttp%3A%2F%2Fwww.w3.org%2F1999%2F02%2F22-rdf-syntax-ns%23%3E%0D%0A%0D%0AASK%0D%0A%09%09%09%0D%0AWHERE%0D%0A%7B%7B%0D%0A%3Fviewer+viskoO%3ApartOfViewerSet+%3Chttps%3A%2F%2Fraw.github.com%2Fnicholasdelrio%2Fvisko%2Fmaster%2Frdf%2Fmozilla-firefox.owl%23mozilla-firefox%3E+.%0D%0A%3Fviewer+viskoO%3AoperatesOn+%3Fformat+.%0D%0A%3Chttps%3A%2F%2Fraw.github.com%2Fnicholasdelrio%2Fvisko%2Fmaster%2Frdf%2Fformats%2FBINARYFLOATARRAYLENDIAN.owl%23BINARYFLOATARRAYLENDIAN%3E+viskoO%3AcanBeTransformedToTransitive+%3Fformat+.+%0D%0A%7D+UNION+%7B%0D%0A%0D%0A%3Fviewer+viskoO%3ApartOfViewerSet+%3Chttps%3A%2F%2Fraw.github.com%2Fnicholasdelrio%2Fvisko%2Fmaster%2Frdf%2Fmozilla-firefox.owl%23mozilla-firefox%3E+.%0D%0A%3Fviewer+viskoO%3AoperatesOn+%3Fformat+.%0D%0A%3Chttps%3A%2F%2Fraw.github.com%2Fnicholasdelrio%2Fvisko%2Fmaster%2Frdf%2Fformats%2FBINARYFLOATARRAYLENDIAN.owl%23BINARYFLOATARRAYLENDIAN%3E+viskoO%3AcanBeTransformedTo+%3Fformat+.%0D%0A%7D+UNION+%7B%0D%0A%0D%0A%3Fviewer+viskoO%3ApartOfViewerSet+%3Chttps%3A%2F%2Fraw.github.com%2Fnicholasdelrio%2Fvisko%2Fmaster%2Frdf%2Fmozilla-firefox.owl%23mozilla-firefox%3E+.%0D%0A%3Fviewer+viskoO%3AoperatesOn+%3Chttps%3A%2F%2Fraw.github.com%2Fnicholasdelrio%2Fvisko%2Fmaster%2Frdf%2Fformats%2FBINARYFLOATARRAYLENDIAN.">Can VisKo Render a 3D Binary Float Array in a format that Mozilla Firefox can Display?</a></li>
<li><a href="ViskoServletManager?requestType=query-triple-store&query=PREFIX+viskoV%3A+%3Chttp%3A%2F%2Ftrust.utep.edu%2Fvisko%2Fontology%2Fvisko-view-v3.owl%23%3E%0D%0APREFIX+viskoO%3A+%3Chttp%3A%2F%2Ftrust.utep.edu%2Fvisko%2Fontology%2Fvisko-operator-v3.owl%23%3E%0D%0APREFIX+viskoS%3A+%3Chttp%3A%2F%2Ftrust.utep.edu%2Fvisko%2Fontology%2Fvisko-service-v3.owl%23%3E%0D%0APREFIX+owlsService%3A+%3Chttp%3A%2F%2Fwww.daml.org%2Fservices%2Fowl-s%2F1.2%2FService.owl%23%3E%0D%0APREFIX+owlsProcess%3A+%3Chttp%3A%2F%2Fwww.daml.org%2Fservices%2Fowl-s%2F1.2%2FProcess.owl%23%3E%0D%0APREFIX+owl%3A+%3Chttp%3A%2F%2Fwww.w3.org%2F2002%2F07%2Fowl%23%3E%0D%0APREFIX+rdfs%3A+%3Chttp%3A%2F%2Fwww.w3.org%2F2000%2F01%2Frdf-schema%23%3E%0D%0APREFIX+xsd%3A+%3Chttp%3A%2F%2Fwww.w3.org%2F2001%2FXMLSchema%23%3E%0D%0APREFIX+rdf%3A+%3Chttp%3A%2F%2Fwww.w3.org%2F1999%2F02%2F22-rdf-syntax-ns%23%3E%0D%0A%0D%0ASELECT+DISTINCT+%3Fformat%0D%0AWHERE%0D%0A%7B%7B%0D%0A%3Foperator+a+viskoO%3AOperator+.%0D%0A%3Foperator+viskoO%3AoperatesOn+%3Fformat+.%0D%0A%7D%0D%0AUNION%0D%0A%7B%0D%0A%3Foperator+a+viskoO%3ATransformer+.%0D%0A%3Foperator+viskoO%3AtransformsTo+%3Fformat+.%0D%0A%7D%7D">What Formats Does VisKo Work With (i.e., read-in and write-out)</a></li>
<li><a href="ViskoServletManager?requestType=query-triple-store&query=PREFIX+viskoV%3A+%3Chttp%3A%2F%2Ftrust.utep.edu%2Fvisko%2Fontology%2Fvisko-view-v3.owl%23%3E%0D%0APREFIX+viskoO%3A+%3Chttp%3A%2F%2Ftrust.utep.edu%2Fvisko%2Fontology%2Fvisko-operator-v3.owl%23%3E%0D%0APREFIX+viskoS%3A+%3Chttp%3A%2F%2Ftrust.utep.edu%2Fvisko%2Fontology%2Fvisko-service-v3.owl%23%3E%0D%0APREFIX+owlsService%3A+%3Chttp%3A%2F%2Fwww.daml.org%2Fservices%2Fowl-s%2F1.2%2FService.owl%23%3E%0D%0APREFIX+owlsProcess%3A+%3Chttp%3A%2F%2Fwww.daml.org%2Fservices%2Fowl-s%2F1.2%2FProcess.owl%23%3E%0D%0APREFIX+owl%3A+%3Chttp%3A%2F%2Fwww.w3.org%2F2002%2F07%2Fowl%23%3E%0D%0APREFIX+rdfs%3A+%3Chttp%3A%2F%2Fwww.w3.org%2F2000%2F01%2Frdf-schema%23%3E%0D%0APREFIX+xsd%3A+%3Chttp%3A%2F%2Fwww.w3.org%2F2001%2FXMLSchema%23%3E%0D%0APREFIX+rdf%3A+%3Chttp%3A%2F%2Fwww.w3.org%2F1999%2F02%2F22-rdf-syntax-ns%23%3E%0D%0A%0D%0Aselect+%3Fview+%3Flabel%0D%0Awhere%0D%0A%7B%0D%0A%3Fview+a+viskoV%3AView.%0D%0A%3Fview+rdfs%3Alabel+%3Flabel%0D%0A%7D+">What views can VisKo Generate?</a>
<li><a href="ViskoServletManager?requestType=query-triple-store&query=PREFIX+viskoV%3A+%3Chttp%3A%2F%2Ftrust.utep.edu%2Fvisko%2Fontology%2Fvisko-view-v3.owl%23%3E%0D%0APREFIX+viskoO%3A+%3Chttp%3A%2F%2Ftrust.utep.edu%2Fvisko%2Fontology%2Fvisko-operator-v3.owl%23%3E%0D%0APREFIX+viskoS%3A+%3Chttp%3A%2F%2Ftrust.utep.edu%2Fvisko%2Fontology%2Fvisko-service-v3.owl%23%3E%0D%0APREFIX+owlsService%3A+%3Chttp%3A%2F%2Fwww.daml.org%2Fservices%2Fowl-s%2F1.2%2FService.owl%23%3E%0D%0APREFIX+owlsProcess%3A+%3Chttp%3A%2F%2Fwww.daml.org%2Fservices%2Fowl-s%2F1.2%2FProcess.owl%23%3E%0D%0APREFIX+owl%3A+%3Chttp%3A%2F%2Fwww.w3.org%2F2002%2F07%2Fowl%23%3E%0D%0APREFIX+rdfs%3A+%3Chttp%3A%2F%2Fwww.w3.org%2F2000%2F01%2Frdf-schema%23%3E%0D%0APREFIX+xsd%3A+%3Chttp%3A%2F%2Fwww.w3.org%2F2001%2FXMLSchema%23%3E%0D%0APREFIX+rdf%3A+%3Chttp%3A%2F%2Fwww.w3.org%2F1999%2F02%2F22-rdf-syntax-ns%23%3E%0D%0A%0D%0Aselect+%3Foperator+%3Flabel%0D%0Awhere%0D%0A%7B%0D%0A%3Foperator+a+viskoO%3AOperator.+%0D%0A%3Foperator+rdfs%3Alabel+%3Flabel%0D%0A%7D">What Operators Does VisKo Know About?</a>
<li><a href="ViskoServletManager?requestType=query-triple-store&query=PREFIX+viskoV%3A+%3Chttp%3A%2F%2Ftrust.utep.edu%2Fvisko%2Fontology%2Fvisko-view-v3.owl%23%3E%0D%0APREFIX+viskoO%3A+%3Chttp%3A%2F%2Ftrust.utep.edu%2Fvisko%2Fontology%2Fvisko-operator-v3.owl%23%3E%0D%0APREFIX+viskoS%3A+%3Chttp%3A%2F%2Ftrust.utep.edu%2Fvisko%2Fontology%2Fvisko-service-v3.owl%23%3E%0D%0APREFIX+owlsService%3A+%3Chttp%3A%2F%2Fwww.daml.org%2Fservices%2Fowl-s%2F1.2%2FService.owl%23%3E%0D%0APREFIX+owlsProcess%3A+%3Chttp%3A%2F%2Fwww.daml.org%2Fservices%2Fowl-s%2F1.2%2FProcess.owl%23%3E%0D%0APREFIX+owl%3A+%3Chttp%3A%2F%2Fwww.w3.org%2F2002%2F07%2Fowl%23%3E%0D%0APREFIX+rdfs%3A+%3Chttp%3A%2F%2Fwww.w3.org%2F2000%2F01%2Frdf-schema%23%3E%0D%0APREFIX+xsd%3A+%3Chttp%3A%2F%2Fwww.w3.org%2F2001%2FXMLSchema%23%3E%0D%0APREFIX+rdf%3A+%3Chttp%3A%2F%2Fwww.w3.org%2F1999%2F02%2F22-rdf-syntax-ns%23%3E%0D%0A%0D%0Aselect+%3Ftoolkit+%3Flabel%0D%0Awhere%0D%0A%7B%0D%0A%3Ftoolkit+a+viskoS%3AToolkit.+%0D%0A%3Ftoolkit+rdfs%3Alabel+%3Flabel.%0D%0A%7D">What Toolkits Does VisKo Know About?</a>
</ul>

<br>

<p>Submit Your Own Questions in SPARQL</p>
<ul><li><a href="sparql-query.html">Your SPARQL Query Endpoint</a></li></ul>

</div>
</div>

<%= Template.getFooter() %>

</body>
</html>