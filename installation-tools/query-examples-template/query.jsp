<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"
	import="edu.utep.trustlab.visko.web.html.Template" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" href="visko-style.css" />
<title>Query Examples</title>
</head>
<body>
<div id="container">

<%= Template.getHeader() %>

<div id="content">

<h2>Visualization Query Examples Supported on this Server</h2>

<p>Visualization Queries Associated with:</p>
<ul>
<li><a href="#gravity">Gravity Data Visualization Queries</a>
<li><a href="#holes">Seismic Tomography Visualization Queries</a> 
<li><a href="#brightness">NASA MODIS Brightness Visualization Queries</a>
<li><a href="#polygons">Polygon Data Queries</a>
<li><a href="#visko">Visko Knowledge Base Visualization Queries</a>
<li><a href="#ecology">UTEP Systems Ecology Lab Visualization Queries</a>
</ul>

<a name="gravity"/>
<h3>Gravity Data Visualization Queries</h3>

<h5>All Possible Visualizations: (AS *)</h5>
<div class="code">
<pre>
PREFIX formats https://raw.github.com/nicholasdelrio/visko/master/resources/formats/
PREFIX types http://rio.cs.utep.edu/ciserver/ciprojects/CrustalModeling/CrustalModeling.owl#
PREFIX visko REPLACE-VISKOpackage_mozilla.owl#
VISUALIZE http://rio.cs.utep.edu/ciserver/ciprojects/GravityMapProvenance/gravityDataset.txt
AS * IN visko:mozilla-firefox
WHERE
	FORMAT = formats:SPACESEPARATEDVALUES.owl#SPACESEPARATEDVALUES
	AND TYPE = types:d19
</pre>
</div>

<table>
<tr>
<td align="center"><a href="http://gmt.soest.hawaii.edu/">GMT</a></td>
<td align="center"><a href="http://www.vtk.org/">VTK</a></td>
<td align="center"><a href="http://www.ncl.ucar.edu/">NCL</a></td>
<td align="center"><a href="http://gmt.soest.hawaii.edu/">GMT</a></td>
</tr>
<tr>
<td><img src="./visualization-examples/gravity-2d-contourmap-gmt.png" width="250px"></td>
<td><img src="./visualization-examples/gravity-2d-contourmap-vtk.png" width="250px"></td>
<td><img src="./visualization-examples/gravity-2d-contourmap-ncl.png" width="250px"></td>
<td><img src="./visualization-examples/gravity-2d-rastermap-gmt.png" width="250px"></td>
</tr>
<tr>
<td align="center"><a href="http://gmt.soest.hawaii.edu/">GMT</a></td>
<td align="center"><a href="http://www.vtk.org/">VTK</a></td>
<td align="center"><a href="http://www.vtk.org/">VTK</a></td>
<td align="center"><a href="http://www.vtk.org/">VTK</a></td>
</tr>
<tr>
<td><img src="./visualization-examples/gravity-2d-pointmap-gmt.png" width="250px"></td>
<td><img src="./visualization-examples/gravity-3d-surfaceplot-vtk.png" width="250px"></td>
<td><img src="./visualization-examples/gravity-3d-isosurfacesrendering-vtk.png" width="250px"></td>
<td><img src="./visualization-examples/gravity-3d-pointplot-vtk.png" width="250px"></td>
</tr>
<tr>
<td align="center"><a href="http://www.ncl.ucar.edu/">NCL</a></td>
</tr>
<tr>
<td><img src="./visualization-examples/gravity-2d-rastermap-ncl.png" width="250px"></td>
</tr>
</table>

<h5>Contour Map Visualization Query</h5>
<div class="code">
<pre>
PREFIX views https://raw.github.com/nicholasdelrio/visko/master/resources/ontology/visko-view.owl#
PREFIX formats https://raw.github.com/nicholasdelrio/visko/master/resources/formats/
PREFIX types http://rio.cs.utep.edu/ciserver/ciprojects/CrustalModeling/CrustalModeling.owl#
PREFIX visko REPLACE-VISKOpackage_mozilla.owl#
PREFIX params REPLACE-VISKOgrdcontour.owl#
VISUALIZE http://rio.cs.utep.edu/ciserver/ciprojects/GravityMapProvenance/gravityDataset.txt
AS views:2D_ContourMap IN visko:mozilla-firefox
WHERE
	FORMAT = formats:SPACESEPARATEDVALUES.owl#SPACESEPARATEDVALUES
	AND TYPE = types:d19
</pre>
</div>

<table>
<tr>
<td align="center"><a href="http://gmt.soest.hawaii.edu/">GMT</a></td>
<td align="center"><a href="http://www.vtk.org/">VTK</a></td>
<td align="center"><a href="http://www.ncl.ucar.edu/">NCL</a></td>

</tr>
<tr>
<td><img src="./visualization-examples/gravity-2d-contourmap-gmt.png" width="350px"></td>
<td><img src="./visualization-examples/gravity-2d-contourmap-vtk.png" width="350px"></td>
<td><img src="./visualization-examples/gravity-2d-contourmap-ncl.png" width="350px"></td>
</tr>
</table>

<h5>Raster Map Query</h5>
<div class="code">
<pre>
PREFIX views https://raw.github.com/nicholasdelrio/visko/master/resources/ontology/visko-view.owl#
PREFIX formats https://raw.github.com/nicholasdelrio/visko/master/resources/formats/ 
PREFIX types http://rio.cs.utep.edu/ciserver/ciprojects/CrustalModeling/CrustalModeling.owl# 
PREFIX visko REPLACE-VISKOpackage_mozilla.owl# 
VISUALIZE http://rio.cs.utep.edu/ciserver/ciprojects/GravityMapProvenance/gravityDataset.txt
AS views:2D_RasterMap IN visko:mozilla-firefox
WHERE
	FORMAT = formats:SPACESEPARATEDVALUES.owl#SPACESEPARATEDVALUES
	AND TYPE = types:d19
</pre>
</div>
<table>
<tr>
<td align="center"><a href="http://gmt.soest.hawaii.edu/">GMT</a></td>
<td align="center"><a href="http://www.ncl.ucar.edu/">NCL</a></td>
</tr>
<tr>
<tr>
<td><img src="./visualization-examples/gravity-2d-rastermap-gmt.png" width="350px"></td>
<td><img src="./visualization-examples/gravity-2d-rastermap-ncl.png" width="350px"></td>
</tr>
</table>

<h5>2D Point Plot Query</h5>
<div class="code">
<pre>
PREFIX views https://raw.github.com/nicholasdelrio/visko/master/resources/ontology/visko-view.owl#
PREFIX formats https://raw.github.com/nicholasdelrio/visko/master/resources/formats/ 
PREFIX types http://rio.cs.utep.edu/ciserver/ciprojects/CrustalModeling/CrustalModeling.owl# 
PREFIX visko REPLACE-VISKOpackage_mozilla.owl# 
VISUALIZE http://rio.cs.utep.edu/ciserver/ciprojects/GravityMapProvenance/gravityDataset.txt
AS views:2D_PointMap IN visko:mozilla-firefox
WHERE
	FORMAT = formats:SPACESEPARATEDVALUES.owl#SPACESEPARATEDVALUES
	AND TYPE = types:d19
</pre>
</div>

<table>
<tr>
<td align="center"><a href="http://gmt.soest.hawaii.edu/">GMT</a></td>
</tr>
<tr>

<tr>
<td><img src="./visualization-examples/gravity-2d-pointmap-gmt.png" width="350px"></td>
</tr>
</table>

<h5>3D Point Plot Query</h5>
<div class="code">
<pre>
PREFIX views https://raw.github.com/nicholasdelrio/visko/master/resources/ontology/visko-view.owl#
PREFIX formats https://raw.github.com/nicholasdelrio/visko/master/resources/formats/ 
PREFIX types http://rio.cs.utep.edu/ciserver/ciprojects/CrustalModeling/CrustalModeling.owl# 
PREFIX visko REPLACE-VISKOpackage_mozilla.owl# 
VISUALIZE http://rio.cs.utep.edu/ciserver/ciprojects/GravityMapProvenance/gravityDataset.txt
AS views:3D_PointPlot IN visko:mozilla-firefox
WHERE
	FORMAT = formats:SPACESEPARATEDVALUES.owl#SPACESEPARATEDVALUES
	AND TYPE = types:d19
</pre>
</div>

<table>
<tr>
<td align="center"><a href="http://www.vtk.org/">VTK</a></td>
</tr>
<tr>

<tr>
<td><img src="./visualization-examples/gravity-3d-pointplot-vtk.png" width="350px"></td>
</tr>
</table>


<h5>Isosurfaces Rendering Query</h5>
<div class="code">
<pre>
PREFIX views https://raw.github.com/nicholasdelrio/visko/master/resources/ontology/visko-view.owl#
PREFIX formats https://raw.github.com/nicholasdelrio/visko/master/resources/formats/ 
PREFIX types http://rio.cs.utep.edu/ciserver/ciprojects/CrustalModeling/CrustalModeling.owl# 
PREFIX visko REPLACE-VISKOpackage_mozilla.owl# 
VISUALIZE http://rio.cs.utep.edu/ciserver/ciprojects/GravityMapProvenance/gravityDataset.txt
AS views:3D_IsoSurfacesRendering IN visko:mozilla-firefox
WHERE
	FORMAT = formats:SPACESEPARATEDVALUES.owl#SPACESEPARATEDVALUES
	AND TYPE = types:d19
</pre>
</div>

<table>
<tr>
<td align="center"><a href="http://www.vtk.org/">VTK</a></td>
</tr>
<tr>

<tr>
<td><img src="./visualization-examples/gravity-3d-isosurfacesrendering-vtk.png" width="350px"></td>
</tr>
</table>

<h5>Surface Plot Query</h5>
<div class="code">
<pre>
PREFIX views https://raw.github.com/nicholasdelrio/visko/master/resources/ontology/visko-view.owl#
PREFIX formats https://raw.github.com/nicholasdelrio/visko/master/resources/formats/ 
PREFIX types http://rio.cs.utep.edu/ciserver/ciprojects/CrustalModeling/CrustalModeling.owl# 
PREFIX visko REPLACE-VISKOpackage_mozilla.owl# 
VISUALIZE http://rio.cs.utep.edu/ciserver/ciprojects/GravityMapProvenance/gravityDataset.txt
AS views:3D_SurfacePlot IN visko:mozilla-firefox
WHERE
	FORMAT = formats:SPACESEPARATEDVALUES.owl#SPACESEPARATEDVALUES
	AND TYPE = types:d19
</pre>
</div>

<table>
<tr>
<td align="center"><a href="http://www.vtk.org/">VTK</a></td>
</tr>
<tr>

<tr>
<td><img src="./visualization-examples/gravity-3d-surfaceplot-vtk.png" width="350px"></td>
</tr>
</table>

<a name="holes"/>
<h3>Queries Associated with Seismic Tomography</h3>

<h5>Velocity Model Isosurfaces Rendering Query</h5>
<div class="code">
<pre>
PREFIX views https://raw.github.com/nicholasdelrio/visko/master/resources/ontology/visko-view.owl#
PREFIX formats https://raw.github.com/nicholasdelrio/visko/master/resources/formats/
PREFIX types http://rio.cs.utep.edu/ciserver/ciprojects/HolesCode/HolesCodeWDO.owl# 
PREFIX visko REPLACE-VISKOpackage_mozilla.owl#
VISUALIZE http://rio.cs.utep.edu/ciserver/ciprojects/HolesCodeFullPML/02029349145023569_vel.3d
AS views:3D_IsoSurfacesRendering IN visko:mozilla-firefox
WHERE
	FORMAT = formats:LITTLE-ENDIAN-SEQUENCE.owl#LITTLE-ENDIAN-SEQUENCE
	AND TYPE = types:d2
</pre>
</div>

<table>
<tr>
<td align="center"><a href="http://www.vtk.org/">VTK</a></td>
</tr>
<tr>

<tr>
<td><img src="./visualization-examples/velocity-3d-isosurfacesrendering-vtk.png" width="450px"></td>
</tr>
</table>



<h5>Velocity Model Isosurfaces Rendering Query</h5>
<div class="code">
<pre>
PREFIX views https://raw.github.com/nicholasdelrio/visko/master/resources/ontology/visko-view.owl#
PREFIX formats https://raw.github.com/nicholasdelrio/visko/master/resources/formats/
PREFIX types http://rio.cs.utep.edu/ciserver/ciprojects/HolesCode/HolesCodeWDO.owl# 
PREFIX visko REPLACE-VISKOpackage_mozilla.owl#
VISUALIZE http://rio.cs.utep.edu/ciserver/ciprojects/HolesCodeFullPML/02029349145023569_vel.3d
AS views:2D_ContourMap IN visko:mozilla-firefox
WHERE
	FORMAT = formats:LITTLE-ENDIAN-SEQUENCE.owl#LITTLE-ENDIAN-SEQUENCE
	AND TYPE = types:d2
</pre>
</div>

<table>
<tr>
<td align="center"><a href="http://www.vtk.org/">VTK</a></td>
</tr>
<tr>

<tr>
<td><img src="./visualization-examples/velocity-2d-contourmap-vtk.png" width="450px"></td>
</tr>
</table>



<a name="seis-ex3">
<h5>Velocity Model Volume Query</h5>
<div class="code">
<pre>
PREFIX views https://raw.github.com/nicholasdelrio/visko/master/resources/ontology/visko-view.owl#
PREFIX formats https://raw.github.com/nicholasdelrio/visko/master/resources/formats/
PREFIX types http://rio.cs.utep.edu/ciserver/ciprojects/HolesCode/HolesCodeWDO.owl# 
PREFIX visko REPLACE-VISKOpackage_mozilla.owl#
VISUALIZE http://rio.cs.utep.edu/ciserver/ciprojects/HolesCodeFullPML/02029349145023569_vel.3d
AS views:3D_VolumeRendering IN visko:mozilla-firefox
WHERE
	FORMAT = formats:LITTLE-ENDIAN-SEQUENCE.owl#LITTLE-ENDIAN-SEQUENCE
	AND TYPE = types:d2
</pre>
</div>

<table>
<tr>
<td align="center"><a href="http://www.vtk.org/">VTK</a></td>
</tr>

<tr>
<td><img src="./visualization-examples/velocity-3d-volumerendering-vtk.png" width="450px"></td>
</tr>
</table>

<h5>Slowness Perturbation Isosurfaces Rendering Query</h5>
<div class="code">
<pre>
PREFIX views https://raw.github.com/nicholasdelrio/visko/master/resources/ontology/visko-view.owl#
PREFIX formats https://raw.github.com/nicholasdelrio/visko/master/resources/formats/
PREFIX types http://rio.cs.utep.edu/ciserver/ciprojects/HolesCode/HolesCodeWDO.owl# 
PREFIX visko REPLACE-VISKOpackage_mozilla.owl#
VISUALIZE http://rio.cs.utep.edu/ciserver/ciprojects/HolesCodeFullPML/032289675474805557_dusum.3d
AS views:3D_IsoSurfacesRendering IN visko:mozilla-firefox
WHERE
	FORMAT = formats:LITTLE-ENDIAN-SEQUENCE.owl#LITTLE-ENDIAN-SEQUENCE
	AND TYPE = types:d8
</pre>
</div>

<table>
<tr>
<td align="center"><a href="http://www.vtk.org/">VTK</a></td>
</tr>

<tr>
<td><img src="./visualization-examples/slowness-3d-isosurfacesrendering-vtk.png" width="450px"></td>
</tr>
</table>

<h5>Time Field Isosurfaces Rendering Query</h5>
<div class="code">
<pre>
PREFIX views https://raw.github.com/nicholasdelrio/visko/master/resources/ontology/visko-view.owl#
PREFIX formats https://raw.github.com/nicholasdelrio/visko/master/resources/formats/
PREFIX types http://rio.cs.utep.edu/ciserver/ciprojects/HolesCode/HolesCodeWDO.owl# 
PREFIX visko REPLACE-VISKOpackage_mozilla.owl#
VISUALIZE http://rio.cs.utep.edu/ciserver/ciprojects/HolesCodeFullPML/05729227976475819_time.3d
AS views:3D_IsoSurfacesRendering IN visko:mozilla-firefox
WHERE
	FORMAT = formats:LITTLE-ENDIAN-SEQUENCE.owl#LITTLE-ENDIAN-SEQUENCE
	AND TYPE = types:d4
</pre>
</div>

<table>
<tr>
<td align="center"><a href="http://www.vtk.org/">VTK</a></td>
</tr>

<tr>
<td><img src="./visualization-examples/time-3d-isosurfacesrendering-vtk.png" width="450px"></td>
</tr>
</table>

<h5>Ray Coverage Volume Rendering Query</h5>
<div class="code">
<pre>
PREFIX views https://raw.github.com/nicholasdelrio/visko/master/resources/ontology/visko-view.owl#
PREFIX formats https://raw.github.com/nicholasdelrio/visko/master/resources/formats/
PREFIX types http://rio.cs.utep.edu/ciserver/ciprojects/HolesCode/HolesCodeSAW3.owl# 
PREFIX visko REPLACE-VISKOpackage_mozilla.owl#
VISUALIZE http://rio.cs.utep.edu/ciserver/ciprojects/HolesCodeFullPML/01146509090356318_icov.3d
AS views:3D_VolumeRendering IN visko:mozilla-firefox
WHERE
	FORMAT = formats:LITTLE-ENDIAN-SEQUENCE.owl#LITTLE-ENDIAN-SEQUENCE
	AND TYPE = types:d7-0
</pre>
</div>

<table>
<tr>
<td align="center"><a href="http://www.vtk.org/">VTK</a></td>
</tr>

<tr>
<td><img src="./visualization-examples/coverage-3d-volumerendering-vtk.png" width="450px"></td>
</tr>
</table>

<a name="brightness"/>
<h3>NASA MODIS Brightness Visualization</h3>

<h5>Contour Map Query</h5>
<div class="code" style="overflow:scroll;">
<pre>
PREFIX views https://raw.github.com/nicholasdelrio/visko/master/resources/ontology/visko-view.owl#
PREFIX formats https://raw.github.com/nicholasdelrio/visko/master/resources/formats/ 
PREFIX visko REPLACE-VISKOpackage_mozilla.owl# 
PREFIX params REPLACE-VISKOgsn_csm_contour_map.owl# 
VISUALIZE http://iw.cs.utep.edu/visko-web/test-data/giovanni/SUBSET_mergedIR_ch4_2008-05-31.nc
AS views:2D_ContourMap IN visko:mozilla-firefox
WHERE
	FORMAT = formats:NETCDF.owl#NETCDF 
	AND TYPE = http://giovanni.gsfc.nasa.gov/giovanni-data.owl#BrightnessTemperature
	AND params:cnFillOn = True
	AND params:cnLinesOn = True
	AND params:cnLevelSpacingF = 20 
	AND params:font = helvetica 
	AND params:lbOrientation = vertical 
	AND params:colorTable = rainbow
</pre>
</div>

<table>
<tr>
<td align="center"><a href="http://www.ncl.ucar.edu/">NCL</a></td>
</tr>

<tr>
<td><img src="./visualization-examples/brightness-2d-contourmap-ncl.png" width="450px"></td>
</tr>
</table>

<h5>Raster Map Query</h5>
<div class="code"  style="overflow:scroll;">
<pre>
PREFIX views https://raw.github.com/nicholasdelrio/visko/master/resources/ontology/visko-view.owl#
PREFIX formats https://raw.github.com/nicholasdelrio/visko/master/resources/formats/ 
PREFIX visko REPLACE-VISKOpackage_mozilla.owl# 
PREFIX params REPLACE-VISKOgsn_csm_contour_map_raster.owl# 
VISUALIZE http://iw.cs.utep.edu/visko-web/test-data/giovanni/SUBSET_mergedIR_ch4_2008-05-31.nc
AS views:2D_RasterMap IN visko:mozilla-firefox
WHERE
	FORMAT = formats:NETCDF.owl#NETCDF
	AND TYPE = http://giovanni.gsfc.nasa.gov/giovanni-data.owl#BrightnessTemperature
	AND params:cnFillOn = True
	AND params:cnLinesOn = True 
	AND params:cnLevelSpacingF = 20 
	AND params:font = helvetica 
	AND params:lbOrientation = vertical
	AND params:colorTable = rainbow
</pre>
</div>

<table>
<tr>
<td align="center"><a href="http://www.ncl.ucar.edu/">NCL</a></td>
</tr>

<tr>
<td><img src="./visualization-examples/brightness-2d-contourmap-ncl.png" width="450px"></td>
</tr>
</table>

<a name="polygons">
<h4>vtkPolyData Queries</h4>

<h5>Cube Query</h5>
<div class="code">
<pre>
PREFIX vtkTypes http://www.vtk.org/vtk-data.owl#
PREFIX formats https://raw.github.com/nicholasdelrio/visko/master/resources/formats/ 
PREFIX types http://rio.cs.utep.edu/ciserver/ciprojects/HolesCode/HolesCodeWDO.owl# 
PREFIX visko REPLACE-VISKOpackage_mozilla.owl# 
PREFIX renderParams REPLACE-VISKOvtkPolyDataMapper.owl# 
VISUALIZE http://rio.cs.utep.edu/ciserver/ciprojects/sdata/Cubes.xml
AS * IN visko:mozilla-firefox
WHERE
	FORMAT = formats:XML.owl#XML
	AND TYPE = vtkTypes:vtkPolyData
	AND renderParams:backgroundColor = 0/1/0 
	AND renderParams:magnification = 3 
	AND renderParams:xRotation = 15 
	AND renderParams:yRotation = 30 
	AND renderParams:zRotation = 0
</pre>
</div>

<table>
<tr>
<td align="center"><a href="http://www.vtk.org/">VTK</a></td>
</tr>

<tr>
<td><img src="./visualization-examples/cube-3d-polydata-vtk.png" width="350px"></td>
</tr>
</table>

<a name="visko">
<h4>VisKo Queries</h4>

<h5>Operator Paths Force Graph</h5>
<div class="code">
<pre>
PREFIX views https://raw.github.com/nicholasdelrio/visko/master/resources/ontology/visko-view.owl#
PREFIX formats https://raw.github.com/nicholasdelrio/visko/master/resources/formats/
PREFIX visko https://raw.github.com/nicholasdelrio/visko-packages-rdf/master/package_custom.owl#
PREFIX types https://raw.github.com/nicholasdelrio/visko/master/resources/ontology/visko.owl#
VISUALIZE https://github.com/nicholasdelrio/visko-packages-rdf
AS views:2D_VisKo_OperatorPaths_ForceGraph IN visko:data-driven-documents
WHERE
	FORMAT = formats:RDFXML.owl#RDFXML
	AND TYPE = types:VisKo_KnowledgeBase
</pre>
</div>

<table>
<tr>
<td align="center"><a href="http://d3js.org/">D3</a></td>
</tr>

<tr>
<td><img src="./visualization-examples/operatorpaths-2d-forcegraph-d3.png" width="450px"></td>
</tr>
</table>

<h5>Operator Paths Force Graph</h5>
<div class="code">
<pre>
PREFIX views https://raw.github.com/nicholasdelrio/visko/master/resources/ontology/visko-view.owl#
PREFIX formats https://raw.github.com/nicholasdelrio/visko/master/resources/formats/
PREFIX visko https://raw.github.com/nicholasdelrio/visko-packages-rdf/master/package_custom.owl#
PREFIX types https://raw.github.com/nicholasdelrio/visko/master/resources/ontology/visko.owl#
VISUALIZE https://github.com/nicholasdelrio/visko-packages-rdf
AS views:2D_VisKo_DataTransformations_ForceGraph IN visko:data-driven-documents
WHERE
	FORMAT = formats:RDFXML.owl#RDFXML
	AND TYPE = types:VisKo_KnowledgeBase
</pre>
</div>

<table>
<tr>
<td align="center"><a href="http://d3js.org/">D3</a></td>
</tr>

<tr>
<td><img src="./visualization-examples/datatransformations-2d-forcegraph-d3.png" width="450px"></td>
</tr>
</table>


<h5>Operator Paths Force Graph</h5>
<div class="code">
<pre>
PREFIX views https://raw.github.com/nicholasdelrio/visko/master/resources/ontology/visko-view.owl#
PREFIX formats https://raw.github.com/nicholasdelrio/visko/master/resources/formats/
PREFIX visko https://raw.github.com/nicholasdelrio/visko-packages-rdf/master/package_custom.owl#
PREFIX types https://raw.github.com/nicholasdelrio/visko/master/resources/ontology/visko.owl#
VISUALIZE https://github.com/nicholasdelrio/visko-packages-rdf
AS views:2D_VisKo_Instances_BarChart IN visko:data-driven-documents
WHERE
	FORMAT = formats:RDFXML.owl#RDFXML
	AND TYPE = types:VisKo_KnowledgeBase
</pre>
</div>

<table>
<tr>
<td align="center"><a href="http://d3js.org/">D3</a></td>
</tr>

<tr>
<td><img src="./visualization-examples/instances-2d-barchart-d3.png" width="450px"></td>
</tr>
</table>


<a name="ecology">
<h4>UTEP Systems Ecology Lab Visualization Queries</h4>

<h5>H20 Time Series</h5>
<div class="code">
<pre>
PREFIX timeSeries https://raw.github.com/nicholasdelrio/visko-packages-rdf/master/gsn_csm_xy2_time_series.owl#
VISUALIZE http://iw.cs.utep.edu/visko-web/test-data/ecology/average_data.nc 
AS https://raw.github.com/nicholasdelrio/visko/master/resources/ontology/visko-view.owl#2D_TimeSeriesPlot 
IN https://raw.github.com/nicholasdelrio/visko-packages-rdf/master/package_mozilla.owl#mozilla-firefox 
WHERE
	FORMAT = https://raw.github.com/nicholasdelrio/visko/master/resources/formats/NETCDF.owl#NETCDF
	AND TYPE = http://iridl.ldeo.columbia.edu/ontologies/cf-obj.owl#Variable_with_Time
	AND timeSeries:title = mean-H2O-and-CO2
	AND timeSeries:xDimName = time
	AND timeSeries:rPlotVariablesList = mean_CO2
	AND timeSeries:xDimSize = 338
	AND timeSeries:yLAxisLabel = mean_H2O_hmp
	AND timeSeries:lPlotVariablesList = mean_H2O_hmp
	AND timeSeries:yRAxisLabel = mean_CO2
</pre>
</div>

<table>
<tr>
<td align="center"><a href="http://www.ncl.ucar.edu/">NCL</a></td>
</tr>

<tr>
<td><img src="./visualization-examples/ecology-2d-timeseries-ncl.png" width="450px"></td>
</tr>
</table>



</div>
</div>

<%= Template.getFooter() %>

</body>
</html>