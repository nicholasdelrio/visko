<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="trustlab.visko.web.html.SelectionOptionsHTML,
				trustlab.visko.web.ExecuteQueryServlet,
				trustlab.visko.web.context.ViskoContext" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Build VisKo Query</title>
<script type="text/javascript">

var defaultValue = "<%=SelectionOptionsHTML.DEFAULT%>";
var format;
var type;
var view;
var viewerSet;
var artifactURL;
var parameter;
var parameterValue;
var bindings = new Array();

function parameterBinding(parameter,parameterValue)
{
	this.parameter=parameter;
	this.parameterValue=parameterValue;
}

function trim(stringToTrim)
{
	return stringToTrim.replace(/^\s+|\s+$/g,"");
}

function reset()
{
	format = defaultValue;
	type = defaultValue;
	view = defaultValue;
	viewerSet = defaultValue;
	artifactURL = defaultValue;
	
	document.getElementById("formatURIs").disabled = false;
	document.getElementById("paramURIs").disabled = true;
	document.getElementById("paramValue").disabled = true;
	document.getElementById("bindButton").disabled = true;
	document.getElementById("typeURIs").disabled = true;
	document.getElementById("viewURIs").disabled = true;
	document.getElementById("viewerSetURIs").disabled = true;
	document.getElementById("artifactURL").disabled = true;
	document.getElementById("queryText").disabled = false;
	//document.getElementById("submitButton").disabled = true;
	
	document.getElementById("formatURIs").selectedIndex = 0;
	document.getElementById("typeURIs").selectedIndex = 0;
	document.getElementById("paramURIs").selectedIndex = 0;
	document.getElementById("paramValue").value = "";
	document.getElementById("viewURIs").selectedIndex = 0;
	document.getElementById("viewerSetURIs").selectedIndex = 0;
	document.getElementById("artifactURL").value = "";
	//document.getElementById("queryText").value = "";
	
	bindings = new Array();
}

function setQueryFields()
{	
	document.getElementById("typeURIs").disabled = false;
	document.getElementById("paramURIs").disabled = false;
	document.getElementById("paramValue").disabled = false;
	document.getElementById("bindButton").disabled = false;
	document.getElementById("viewURIs").disabled = false;
	document.getElementById("viewerSetURIs").disabled = false;
	document.getElementById("artifactURL").disabled = false;
	document.getElementById("queryText").disabled = false;
	document.getElementById("submitButton").disabled = false;
	
	format = document.getElementById("formatURIs").value;
	type = document.getElementById("typeURIs").value;
	view = document.getElementById("viewURIs").value;
	viewerSet = document.getElementById("viewerSetURIs").value;
	artifactURL = trim(document.getElementById("artifactURL").value);
	
	parameter = document.getElementById("paramURIs").options[document.getElementById("paramURIs").selectedIndex].value;
	parameterValue = trim(document.getElementById("paramValue").value);
	
	if(parameter != defaultValue && parameterValue != "" && parameterValue != null)
	{
		var paramExists = false;
		for(var i = 0; i < bindings.length; i = i + 1)
		{
			var binding = bindings[i];
			if(binding.parameter == parameter)
			{
				binding.parameterValue = parameterValue;
				paramExists = true;
			}
		}
		
		if(!paramExists)
		{
			bindings.push(new parameterBinding(parameter, parameterValue));
		}
	}

	if(viewerSet == defaultValue)
	{
		document.getElementById("viewerSetURIs").value = "http://rio.cs.utep.edu/ciserver/ciprojects/visko/mozilla-firefox1.owl#mozilla-firefox1";
		viewerSet = "http://rio.cs.utep.edu/ciserver/ciprojects/visko/mozilla-firefox1.owl#mozilla-firefox1";
	}
}
	
function clearParameterValue()
{
	document.getElementById("paramValue").value = "";
	writeQuery();
}

function writeQuery()
{	
	setQueryFields();
	var query = "(AND\t";
	
	if(format == defaultValue)
	{reset();}
	else
	{
		query = query + "(hasFormat ?DATA " + format + ")";
	
		if(type != defaultValue)
		{query = query + "\n\t(hasType ?DATA " + type + ")";}

		if(view != defaultValue)
		{query = query + "\n\t(hasView ?DATA " + view + ")";}
		
		if(artifactURL != null && artifactURL != "")
		{query = query + "\n\t(hasContent ?DATA " + artifactURL + ")";}

		for(var i = 0; i < bindings.length; i = i + 1)
		{
			var binding = bindings[i];
			var parameter = binding.parameter;
			var parameterValue = binding.parameterValue;
			
			query = query + "\n\t(hasValue " + parameter + " " + parameterValue + ")";
		}
		
		query = query + "\n\t(viewedBy ?DATA " + viewerSet + "))";
	
		document.getElementById("queryText").value = query;
	}
}
</script>
</head>

<body onLoad = "reset()">

<% SelectionOptionsHTML o = new SelectionOptionsHTML(ViskoContext.VISKO_TRIPLE_STORE_LOCATION); %>

<table style="height: 118px; width: 1049px">
	<tr><td colspan="2" align="center"><b>Complete Form to Compose Query</b></td></tr>
	<tr>
		<td style="width: 176px; ">Select Format</td>
		<td align="right"><select style="width: 780px"
			name="formatURI" id="formatURIs" onchange="writeQuery()"><%=o.getFormats()%></select></td>
	</tr>
	<tr>
		<td style="width: 182px; ">Select Type (optional)</td>
		<td align="right"><select style="width: 780px" name="typeURI"
			onchange="writeQuery()" disabled="disabled" id="typeURIs"><%=o.getTypes()%></select></td>
	</tr>
	<tr>
		<td>Select View (optional)</td>
		<td align="right"><select style="width: 780px" name="viewURI"
			onchange="writeQuery()" disabled="disabled" id="viewURIs"><%=o.getViskoViews() %></select></td>
	</tr>
	<tr>
		<td>Select ViewerSet (optional)</td>
		<td align="right"><select style="width: 780px" name="viewerSetURI"
			onchange="writeQuery()" disabled="disabled" id="viewerSetURIs"><%=o.getViewerSets()%></select></td>
	</tr>
	<tr>
		<td>Set Artifact URL (optional)</td>
		<td align="right"><input style="width: 776px" id="artifactURL" disabled="disabled" onchange="writeQuery()"></td>
	</tr>
	
	<tr>
		<td>Set Bindings(optional)</td>
		<td align="right">
			<table width="780">
				<tr>
					<td style="width: 131px; ">SelectParameter</td>
					<td align="right" colspan="2"><select style="width: 600px" name="paramURI"
					disabled="disabled" id="paramURIs" onchange="clearParameterValue()"><%=o.getParameters()%></select></td>
				</tr>
				<tr>
					<td>Specify Value</td>
					<td align="right"><input style="width: 428px; " id="paramValue" disabled="disabled"></td>
					<td align="right"><input id="bindButton" type="submit" value="Add Binding" style="width: 150px; " disabled="disabled" onclick="writeQuery()"></td>
				</tr>
			</table>
		</td>
	</tr>
	</table>
	<hr>
	<form action="ExecuteQueryServlet">
	<table style="width: 1023px; ">
	<tr><td colspan="2" align="center"><b>VisKo  Query</b></td></tr>
	<tr>
		<td style="width: 996px; " colspan="2" align="right"><textarea style="width: 989px; height: 152px" id="queryText" name="query"></textarea></td>
	</tr>
	<tr>
		<td></td>
		<td align="right">
		<table>
			<tr>
				<td><p onclick="reset()">Reset</p></td>
				<td><input type="submit" id="submitButton" style="width: 156px"></td>
			</tr>
		</table>
		</td>
	</tr>
</table>
</form>
</body>
</html>