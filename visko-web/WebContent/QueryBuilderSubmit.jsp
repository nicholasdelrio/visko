<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="edu.utep.trustlab.visko.web.html.SelectionOptionsHTML" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Build VisKo Query</title>
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
		query = query + "SELECT " + view + " \n";
		
		if(viewerSet !== null && viewerSet != defaultValue)
		{query = query + "IN-VIEWER " + viewerSet + " \n";}
		else
		{query = query + "IN-VIEWER viewer-set \n";}

		if(artifactURL != null && artifactURL != "")
		{query = query + "FROM " + artifactURL + " \n";}
		else
		{query = query + "FROM url \n";}
		
		if(format != defaultValue)
		{query = query + "FORMAT " + format + " \n";}
		else
		{query = query + "FORMAT format \n";}
		
		if(type != defaultValue)
		{query = query + "TYPE " + type + " \n";}
		else
		{query = query + "TYPE type \n";}

		if(bindings.length > 0)
		{query = query + "WHERE";}
		for(var i = 0; i < bindings.length; i = i + 1)
		{
			var binding = bindings[i];
			var parameter = binding.parameter;
			var parameterValue = binding.parameterValue;
			
			if((bindings.length - i) > 1)
				query = query + "\t" + parameter + " = " + parameterValue + " AND \n";
			else
				query = query + "\t" + parameter + " = " + parameterValue + " \n";	
		}
			
		document.getElementById("queryText").value = query;
	}
}
</script>
</head>

<body onLoad = "reset()">

<% SelectionOptionsHTML o = new SelectionOptionsHTML(); %>

<table style="height: 118px; width: 1049px">
	<tr><td colspan="2" align="center"><b>Complete Form to Compose Query</b></td></tr>
	<tr>
		<td>Select View</td>
		<td align="right"><select style="width: 780px" name="viewURI"
			onchange="writeQuery()" id="viewURIs"><%=o.getViskoViews() %></select></td>
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
		<td style="width: 176px; ">Select Format</td>
		<td align="right"><select style="width: 780px"
			name="formatURIs" disabled="disabled" id="formatURIs" onchange="writeQuery()"><%=o.getFormats()%></select></td>
	</tr>
	<tr>
		<td style="width: 182px; ">Select Type (optional)</td>
		<td align="right"><select style="width: 780px" name="typeURI"
			onchange="writeQuery()" disabled="disabled" id="typeURIs"><%=o.getTypes()%></select></td>
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
	<form action="ViskoServletManager">
    <input type="hidden" name="requestType" value="execute-query" /> 	<table style="width: 1023px; ">
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