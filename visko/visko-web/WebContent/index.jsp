<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="edu.utep.trustlab.visko.web.html.*" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" href="visko-style.css" />
<title>VisKo Server at <%=Template.getOrganization() %></title>
<script type="text/javascript">

var defaultValue = "<%= SelectionOptionsHTML.DEFAULT %>";
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
	
	document.getElementById("formatURIs").disabled = true;
	document.getElementById("paramURIs").disabled = true;
	document.getElementById("paramValue").disabled = true;
	document.getElementById("bindButton").disabled = true;
	document.getElementById("typeURIs").disabled = true;
	document.getElementById("viewURIs").disabled = false;
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
	document.getElementById("formatURIs").disabled = false;
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
}
	
function clearParameterValue()
{
	document.getElementById("paramValue").value = "";
	writeQuery();
}

function writeQuery()
{	
	setQueryFields();
	var query = "";
	
	if(view == defaultValue)
	{reset();}
	else
	{
		if(artifactURL != null && artifactURL != "")
		{query = query + "VISUALIZE " + artifactURL + " \n";}
		else
		{query = query + "VISUALIZE url \n";}
	
		if(view != null && view != defaultValue)
			query = query + "AS " + view + " \n";
		else{query = query + "AS view";}
		
		if(viewerSet !== null && viewerSet != defaultValue)
		{query = query + "IN " + viewerSet + " \n";}
		else
		{query = query + "IN viewer-set \n";}
		
		if(format != null && format != defaultValue)
		{
			query = query + "WHERE\n";
			query = query + "\tFORMAT = " + format + "\n";
		}
		else
		{query = query + "WHERE\n\tFORMAT = format\n";}
		
		if(type != null && type != defaultValue)
		{query = query + "\tAND TYPE = " + type + "\n";}
		else
		{query = query + "\tAND TYPE = type\n";}
		
		for(var i = 0; i < bindings.length; i = i + 1)
		{
			var binding = bindings[i];
			var parameter = binding.parameter;
			var parameterValue = binding.parameterValue;
			
			query = query + "\tAND " + parameter + " = " + parameterValue + "\n";
		
		}
			
		document.getElementById("queryText").value = query;
	}
}
</script>
</head>

<body onLoad = "reset()">
<div id="container">

<%= Template.getHeader() %>

<div id="content">

<% SelectionOptionsHTML o = new SelectionOptionsHTML(); %>

<table style="width: 1049px">
<tr><td>Click <a href="query.jsp">here</a> for a list of query examples or use the form below to construct a new visualization query from scratch.</td></tr>
</table>

<table style="height: 118px; width: 1049px">
	<tr><td colspan="2"><h2>Complete Form to Compose Query</h2></td></tr>
	<tr>
		<td>Select Abstraction</td>
		<td align="right"><select style="width: 780px; background-color: #AFEEEE" name="viewURI"
			onchange="writeQuery()" id="viewURIs"><%=o.getViskoViews() %></select></td>
	</tr>
	<tr>
		<td>Select ViewerSet</td>
		<td align="right"><select style="width: 780px; background-color: #AFEEEE" name="viewerSetURI"
			onchange="writeQuery()" disabled="disabled" id="viewerSetURIs"><%=o.getViewerSets()%></select></td>
	</tr>

	<tr>
		<td>Set Input URL (optional)</td>
		<td align="right"><input style="width: 776px; background-color: #AFEEEE" id="artifactURL" disabled="disabled" onchange="writeQuery()"></td>
	</tr>

	<tr>
		<td style="width: 176px; ">Select Input Format</td>
		<td align="right"><select style="width: 780px; background-color: #AFEEEE"
			name="formatURIs" disabled="disabled" id="formatURIs" onchange="writeQuery()"><%=o.getFormats()%></select></td>
	</tr>
	<tr>
		<td style="width: 182px; ">Select Input Type (optional)</td>
		<td align="right"><select style="width: 780px; background-color: #AFEEEE" name="typeURI"
			onchange="writeQuery()" disabled="disabled" id="typeURIs"><%=o.getTypes()%></select></td>
	</tr>
	
	<tr>
		<td>Set Bindings(optional)</td>
		<td align="right">
			<table width="780">
				<tr>
					<td style="width: 131px; ">SelectParameter</td>
					<td align="right" colspan="2"><select style="width: 600px; background-color: #AFEEEE" name="paramURI"
					disabled="disabled" id="paramURIs" onchange="clearParameterValue()"><%=o.getParameters()%></select></td>
				</tr>
				<tr>
					<td>Specify Value</td>
					<td align="right"><input style="width: 428px; background-color: #AFEEEE" id="paramValue" disabled="disabled"></td>
					<td align="right"><input id="bindButton" type="submit" value="Add Binding" style="width: 150px" disabled="disabled" onclick="writeQuery()"></td>
				</tr>
			</table>
		</td>
	</tr>
	</table>
	<hr>
	<form action="ViskoServletManager">
    <input type="hidden" name="requestType" value="execute-query" /> 	<table style="width: 1023px; ">
	<tr><td colspan="2"><h2>VisKo  Query</h2></td></tr>
	<tr>
		<td style="width: 996px; " colspan="2" align="right"><textarea style="width: 989px; height: 152px; background-color: #AFEEEE" id="queryText" name="query"></textarea></td>
	</tr>
	<tr>
		<td></td>
		<td align="right">
		<table>
			<tr>
				<td><input type="submit" id="submitButton" style="width: 156px"></td>
				<td><button type="button" onclick="reset()">Reset</button></td>
			</tr>
		</table>
		</td>
	</tr>
</table>
</form>
</div>
</div>
</div>
</div>
<%= Template.getFooter() %>
</body>
</html>