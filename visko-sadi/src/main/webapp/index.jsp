<%@ page contentType="text/html; charset=iso-8859-1" language="java"  %>
<%
  String hostName = request.getServerName();
%>
<?xml version='1.0' encoding='UTF-8'?>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <title>SADI services at <%=hostName%></title>
    <link rel="icon" href="http://sadiframework.org/favicon.ico" type="image/x-icon">
    <link rel="stylesheet" type="text/css" href="http://sadiframework.org/style/new.css">
  </head>
  <body>
    <div id='outer-frame'>
      <div id='inner-frame'>
        <div id='header'>
          <h1><a href="http://sadiframework.org/">SADI</a> services at <%=hostName%></h1>
        </div>
        <div id='nav'>
          <ul>
            <li class="page_item current_page_item">Services</li>
          </ul>
        </div>
        <div id='content'>
          <h2>SADI Services</h2>
	      <ul>
            <li><a href="./query-planner">query-planner</a></li>
            <li><a href="./planner">planner</a></li>
	      </ul>
        </div> <!-- content -->
        <div id='footer'>
        </div> <!-- footer -->
      </div> <!-- inner-frame -->
    </div> <!-- outer-frame -->
  </body>
</html>