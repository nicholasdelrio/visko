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
<li><a href="#gravity">Gravity Data</a>
  <ol>
    <li><a href="#gravity-ex1">Visualizing gravity data as contour map</a></li>
    <li><a href="#gravity-ex2">Visualizing gravity data as raster map</a></li>
    <li><a href="#gravity-ex3">Visualizing gravity data as raster map with copper tones</a></li>
    <li><a href="#gravity-ex4">Visualizing gravity data as 2D plot</a></li>
    <li><a href="#gravity-ex5">Visualizing gravity data all ways possible (AS *)</a></li>
  </ol>

<li><a href="#holes">Seismic Tomography</a>
  <ol start="6">
    <li><a href="#seis-ex1">Visualizing velocity model as isosurfaces (full disclosure of parameters)</a></li>
    <li><a href="#seis-ex2">Visualizing velocity model as isosurfaces with black background</a></li>
    <li><a href="#seis-ex3">Visualizing velocity model as a volume</a></li>
    <li><a href="#seis-ex4">Visualizing velocity slowness perturbation as isosurfaces</a></li>
    <li><a href="#seis-ex5">Visualizing Gridded Time Field as Isosurfaces</a></li>
    <li><a href="#seis-ex6">Visualizing Gridded Time Field as Isosurfaces with Rotation</a></li>
    <li><a href="#seis-ex7">Visualizing Ray Coverage as Volume</a></li> 
 </ol>
 
<li><a href="#brightness">NASA MODIS Brightness Data</a>
  <ol>
  	<li><a href="#bright-ex1">Visualizing MODIS brightness data as contour map</a></li>
  	<li><a href="#bright-ex2">Visualizing MODIS brightness data as raster map</a></li>
  </ol>

<li><a href="#polygons">Polygon Data</a>
  <ol start="7">
    <li><a href="#cube-ex1">Rendering Cube Polygons</a>
  </ol>
</ul>


<a name="gravity"/>
<h3>Queries Associated with Gravity Data</h3>

<a name="gravity-ex1">
<h5>1. Visualizing gravity data as contour map</h5>
<div class="code">
<pre>
PREFIX formats https://raw.github.com/nicholasdelrio/visko/master/resources/formats/
PREFIX types http://rio.cs.utep.edu/ciserver/ciprojects/CrustalModeling/CrustalModeling.owl#
PREFIX visko REPLACE-VISKOpackage_mozilla.owl#package_mozilla.owl#
PREFIX params REPLACE-VISKOgrdcontour.owl#
VISUALIZE http://rio.cs.utep.edu/ciserver/ciprojects/GravityMapProvenance/gravityDataset.txt
AS https://raw.github.com/nicholasdelrio/visko/master/resources/views/contour-lines.owl#contour-lines IN visko:mozilla-firefox
WHERE
	FORMAT = formats:SPACEDELIMITEDTABULARASCII.owl#SPACEDELIMITEDTABULARASCII
	AND TYPE = types:d19
	AND params:C = 10
	AND params:A = 10
</pre>
</div>
<table>
<tr><td align="center"><a href="http://trust.utep.edu/visko/ql-examples/pipelines/pipeline-gmt-gravity-contourmap.htm">Pipeline</a></td></tr>
<tr><td><img src="http://trust.utep.edu/visko/ql-examples/visualizations/contour-map.png" width="350px"></td></tr>
</table>

<a name="gravity-ex2">
<h5>2. Visualizing gravity data as raster map</h5>
<div class="code">
<pre>
PREFIX formats https://raw.github.com/nicholasdelrio/visko/master/resources/formats/ 
PREFIX types http://rio.cs.utep.edu/ciserver/ciprojects/CrustalModeling/CrustalModeling.owl# 
PREFIX visko REPLACE-VISKOpackage_mozilla.owl#package_mozilla.owl# 
VISUALIZE http://rio.cs.utep.edu/ciserver/ciprojects/GravityMapProvenance/gravityDataset.txt
AS https://raw.github.com/nicholasdelrio/visko/master/resources/views/raster.owl#raster IN visko:mozilla-firefox
WHERE
	FORMAT = formats:SPACEDELIMITEDTABULARASCII.owl#SPACEDELIMITEDTABULARASCII
	AND TYPE = types:d19
</pre>
</div>
<table>
<tr><td align="center"><a href="http://trust.utep.edu/visko/ql-examples/pipelines/pipeline-gmt-gravity-rastermap.htm">Pipeline</a></td><td align="center"><a href="http://trust.utep.edu/visko/ql-examples/pipelines/pipeline-ncl-gravity-rastermap.htm">Pipeline</a></td></tr>
<tr><td><img src="http://trust.utep.edu/visko/ql-examples/visualizations/gravity-raster.png" width="350px"></a></td><td><img src="http://trust.utep.edu/visko/ql-examples/visualizations/gravity-raster1.png"  width="350px"></td></tr>
</table>

<a name="gravity-ex3">
<h5>3. Visualizing gravity data as raster map with copper tones</h5>
<div class="code">
<pre>
PREFIX formats https://raw.github.com/nicholasdelrio/visko/master/resources/formats/
PREFIX types http://rio.cs.utep.edu/ciserver/ciprojects/CrustalModeling/CrustalModeling.owl# 
PREFIX visko REPLACE-VISKOpackage_mozilla.owl# 
PREFIX params REPLACE-VISKOgrdimage.owl# 
VISUALIZE http://rio.cs.utep.edu/ciserver/ciprojects/GravityMapProvenance/gravityDataset.txt
AS https://raw.github.com/nicholasdelrio/visko/master/resources/views/raster.owl#raster IN visko:mozilla-firefox
WHERE
	FORMAT = formats:SPACEDELIMITEDTABULARASCII.owl#SPACEDELIMITEDTABULARASCII
	AND TYPE = types:d19 
	AND params:C = copper
</pre>
</div>
<table>
<tr><td align="center"><a href="http://trust.utep.edu/visko/ql-examples/pipelines/pipeline-gmt-gravity-rastermap-copper.htm">Pipeline</a></td></tr>
<tr><td><img src="http://trust.utep.edu/visko/ql-examples/visualizations/color-map-copper.png" width="350px"></td></tr>
</table>

<a name="gravity-ex4">
<h5>4. Visualizing gravity data as 2D plot</h5>
<div class="code">
<pre>
PREFIX formats https://raw.github.com/nicholasdelrio/visko/master/resources/formats/ 
PREFIX types http://rio.cs.utep.edu/ciserver/ciprojects/CrustalModeling/CrustalModeling.owl# 
PREFIX visko REPLACE-VISKOpackage_mozilla.owl# 
VISUALIZE http://rio.cs.utep.edu/ciserver/ciprojects/GravityMapProvenance/gravityDataset.txt
AS https://raw.github.com/nicholasdelrio/visko/master/resources/views/plot-2D.owl#plot-2D IN visko:mozilla-firefox
WHERE
	FORMAT = formats:SPACEDELIMITEDTABULARASCII.owl#SPACEDELIMITEDTABULARASCII
	AND TYPE = types:d19
</pre>
</div>
<table>
<tr><td align="center"><a href="http://trust.utep.edu/visko/ql-examples/pipelines/pipeline-gmt-gravity-points.htm">Pipeline</a></td></tr>
<tr><td><img src="http://trust.utep.edu/visko/ql-examples/visualizations/2d-plot.png" width="300px"></td></tr>
</table>

<a name="gravity-ex5">
<h5>5. Visualizing gravity data all ways possible (<b>SELECT *</b>)</h5>
<div class="code">
<pre>
PREFIX formats https://raw.github.com/nicholasdelrio/visko/master/resources/formats/ 
PREFIX types http://rio.cs.utep.edu/ciserver/ciprojects/CrustalModeling/CrustalModeling.owl# 
PREFIX visko REPLACE-VISKOpackage_mozilla.owl# 
VISUALIZE http://rio.cs.utep.edu/ciserver/ciprojects/GravityMapProvenance/gravityDataset.txt
AS * IN visko:mozilla-firefox
WHERE
	FORMAT = FORMAT formats:SPACEDELIMITEDTABULARASCII.owl#SPACEDELIMITEDTABULARASCII 
	AND TYPE = types:d19
</pre>
</div>
<table>
<tr>
    <td align="center"><a href="http://trust.utep.edu/visko/ql-examples/pipelines/pipeline-gmt-gravity-contourmap.htm">Pipeline</a></td>
    <td align="center"><a href="http://trust.utep.edu/visko/ql-examples/pipelines/pipeline-ncl-gravity-rastermap.htm">Pipeline</a></td>
    <td align="center"><a href="http://trust.utep.edu/visko/ql-examples/pipelines/pipeline-gmt-gravity-points.htm">Pipeline</a></td>
</tr>
<tr>
    <td><img src="http://trust.utep.edu/visko/ql-examples/visualizations/contour-map.png"  width="350px"></td>
    <td><img src="http://trust.utep.edu/visko/ql-examples/visualizations/gravity-raster1.png" width="350px"></td>
    <td><img src="http://trust.utep.edu/visko/ql-examples/visualizations/2d-plot.png" width="300px"></td>
</tr>
</table>

<a name="holes"/>
<h3>Queries Associated with Seismic Tomography</h3>

<a name="seis-ex1">
<h5>6. Visualizing velocity model as isosurfaces (full disclosure of parameter bindings)</h5>
<div class="code">
<pre>
PREFIX formats https://raw.github.com/nicholasdelrio/visko/master/resources/formats/
PREFIX types http://rio.cs.utep.edu/ciserver/ciprojects/HolesCode/HolesCodeWDO.owl# 
PREFIX visko REPLACE-VISKOpackage_mozilla.owl#
PREFIX dataParams REPLACE-VISKOvtkImageDataReader.owl#
PREFIX contourParams REPLACE-VISKOvtkContourFilter.owl#
PREFIX renderParams REPLACE-VISKOvtkPolyDataMapper.owl#
VISUALIZE http://rio.cs.utep.edu/ciserver/ciprojects/HolesCodeFullPML/02029349145023569_vel.3d
AS https://raw.github.com/nicholasdelrio/visko/master/resources/views/iso-surfaces.owl#iso-surfaces IN visko:mozilla-firefox
WHERE
	FORMAT = formats:BINARYFLOATARRAYLENDIAN.owl#BINARYFLOATARRAYLENDIAN 
	AND TYPE = types:d2
	AND dataParams:dataSpacing = 1/1/1
	AND dataParams:dataExtent = 0/230/0/25/0/68
	AND dataParams:littleEndian = true
	AND dataParams:dataOrigin = 0/0/0
	AND dataParams:numScalarComponents = 1
	AND contourParams:scalarRange = 0.0/9000.0
	AND contourParams:numContours = 35
	AND renderParams:backgroundColor = 1/1/1
	AND renderParams:magnification = 3
	AND renderParams:xRotation = 105
	AND renderParams:yRotation = 0
	AND renderParams:zRotation = 0
</pre>
</div>
<table>
<tr><td align="center"><a href="http://trust.utep.edu/visko/ql-examples/pipelines/pipeline-vtk-velocity-isosurfaces.htm">Pipeline</a></td></tr>
<tr><td><img src="http://trust.utep.edu/visko/ql-examples/visualizations/velocityContours.jpg"  width="450px"></td></tr>
</table>

<a name="seis-ex2">
<h5>7. Visualizing velocity model as isosurfaces with black background</h5>
<div class="code">
<pre>
PREFIX formats https://raw.github.com/nicholasdelrio/visko/master/resources/formats/ 
PREFIX types http://rio.cs.utep.edu/ciserver/ciprojects/HolesCode/HolesCodeWDO.owl# 
PREFIX visko REPLACE-VISKOpackage_mozilla.owl# 
PREFIX params REPLACE-VISKOvtkPolyDataMapper.owl# 
VISUALIZE http://rio.cs.utep.edu/ciserver/ciprojects/HolesCodeFullPML/02029349145023569_vel.3d
AS https://raw.github.com/nicholasdelrio/visko/master/resources/views/iso-surfaces.owl#iso-surfaces IN visko:mozilla-firefox
WHERE
	FORMAT = formats:BINARYFLOATARRAYLENDIAN.owl#BINARYFLOATARRAYLENDIAN 
	AND TYPE = types:d2 
	AND params:backgroundColor = 0/0/0
</pre>
</div>
<table>
<tr><td align="center"><a href="http://trust.utep.edu/visko/ql-examples/pipelines/pipeline-vtk-velocity-isosurfaces-black.htm">Pipeline</a></td></tr>
<tr><td><img src="http://trust.utep.edu/visko/ql-examples/visualizations/velocityContours-black.jpg" width="450px"></td></tr>
</table>

<a name="seis-ex3">
<h5>8. Visualizing velocity model as a volume</h5>
<div class="code">
<pre>
PREFIX formats https://raw.github.com/nicholasdelrio/visko/master/resources/formats/
PREFIX types http://rio.cs.utep.edu/ciserver/ciprojects/HolesCode/HolesCodeWDO.owl# 
PREFIX visko REPLACE-VISKOpackage_mozilla.owl# 
PREFIX params REPLACE-VISKOvtkVolume.owl# 
VISUALIZE http://rio.cs.utep.edu/ciserver/ciprojects/HolesCodeFullPML/02029349145023569_vel.3d
AS https://raw.github.com/nicholasdelrio/visko/master/resources/views/volume.owl#volume IN visko:mozilla-firefox
WHERE
	FORMAT = formats:BINARYFLOATARRAYLENDIAN.owl#BINARYFLOATARRAYLENDIAN 
	AND TYPE = types:d2 
	AND params:colorFunction = 3000,1,1,0/5000,0.5,0.95,0/5600,0,0,1/6500,0.28,0.2,0.5/7000,1,0,0
	AND params:opacityFunction = 4000,0.2/8000,0.5
</pre>
</div>

<table>
<tr><td align="center"><a href="http://trust.utep.edu/visko/ql-examples/pipelines/pipeline-vtk-velocity-volume.htm">Pipeline</a></td></tr>
<tr><td><img src="http://trust.utep.edu/visko/ql-examples/visualizations/velocityVolume.png" width="450px"></td></tr>
</table>

<a name="seis-ex4">
<h5>9. Visualizing velocity slowness perturbation as isosurfaces</h5>
<div class="code">
<pre>
PREFIX formats https://raw.github.com/nicholasdelrio/visko/master/resources/formats/ 
PREFIX types http://rio.cs.utep.edu/ciserver/ciprojects/HolesCode/HolesCodeWDO.owl# 
PREFIX visko REPLACE-VISKOpackage_mozilla.owl# 
VISUALIZE http://rio.cs.utep.edu/ciserver/ciprojects/HolesCodeFullPML/032289675474805557_dusum.3d
AS https://raw.github.com/nicholasdelrio/visko/master/resources/views/iso-surfaces.owl#iso-surfaces IN visko:mozilla-firefox
WHERE
	FORMAT = formats:BINARYFLOATARRAYLENDIAN.owl#BINARYFLOATARRAYLENDIAN
	AND TYPE = types:d8
</pre>
</div>

<table>
<tr><td align="center"><a href="http://trust.utep.edu/visko/ql-examples/pipelines/pipeline-vtk-dusum-isosurfaces.htm">Pipeline</a></td></tr>
<tr><td><img src="http://trust.utep.edu/visko/ql-examples/visualizations/dusumContours.jpg" width="450px"></td></tr>
</table>

<a name="seis-ex5">
<h5>10. Visualizing Gridded Time Field as Isosurfaces</h5>
<div class="code">
<pre>
PREFIX formats https://raw.github.com/nicholasdelrio/visko/master/resources/formats/ 
PREFIX types http://rio.cs.utep.edu/ciserver/ciprojects/HolesCode/HolesCodeWDO.owl# 
PREFIX visko REPLACE-VISKOpackage_mozilla.owl# 
VISUALIZE http://rio.cs.utep.edu/ciserver/ciprojects/HolesCodeFullPML/05729227976475819_time.3d
AS https://raw.github.com/nicholasdelrio/visko/master/resources/views/iso-surfaces.owl#iso-surfaces IN visko:mozilla-firefox
WHERE
	FORMAT = formats:BINARYFLOATARRAYLENDIAN.owl#BINARYFLOATARRAYLENDIAN 
	AND TYPE = types:d4
</pre>
</div>

<table>
<tr><td align="center"><a href="http://trust.utep.edu/visko/ql-examples/pipelines/pipeline-vtk-time-isosurfaces.htm">Pipeline</a></td></tr>
<tr><td><img src="http://trust.utep.edu/visko/ql-examples/visualizations/timeContours.jpg" width="450px"></td></tr>
</table>

<a name="seis-ex6">
<h5>11. Visualizing Gridded Time Field as Isosurfaces with Rotation</h5>
<div class="code">
<pre>
PREFIX formats https://raw.github.com/nicholasdelrio/visko/master/resources/formats/ 
PREFIX types http://rio.cs.utep.edu/ciserver/ciprojects/HolesCode/HolesCodeWDO.owl# 
PREFIX visko REPLACE-VISKOpackage_mozilla.owl# 
PREFIX params REPLACE-VISKOvtkPolyDataMapper.owl# 
VISUALIZE http://rio.cs.utep.edu/ciserver/ciprojects/HolesCodeFullPML/05729227976475819_time.3d
AS https://raw.github.com/nicholasdelrio/visko/master/resources/views/iso-surfaces.owl#iso-surfaces IN visko:mozilla-firefox
WHERE
	FORMAT = formats:BINARYFLOATARRAYLENDIAN.owl#BINARYFLOATARRAYLENDIAN 
	AND TYPE = types:d4
	AND params:xRotation = 90
	AND params:yRotation = 0
	AND params:zRotation = -45
</pre>
</div>
<table>
<tr><td align="center"><a href="http://trust.utep.edu/visko/ql-examples/pipelines/pipeline-vtk-time-isosurfaces-rotated.htm">Pipeline</a></td></tr>
<tr><td><img src="http://trust.utep.edu/visko/ql-examples/visualizations/time-rotation.png" width="450px"></td></tr>
</table>

<a name="seis-ex7">
<h5>12. Visualizing Ray Coverage as Volume</h5>
<div class="code">
<pre>
PREFIX formats https://raw.github.com/nicholasdelrio/visko/master/resources/formats/ 
PREFIX types http://rio.cs.utep.edu/ciserver/ciprojects/HolesCode/HolesCodeSAW3.owl#
PREFIX visko REPLACE-VISKOpackage_mozilla.owl# 
PREFIX params REPLACE-VISKOvtkVolume.owl# 
VISUALIZE http://rio.cs.utep.edu/ciserver/ciprojects/HolesCodeFullPML/01146509090356318_icov.3d
AS https://raw.github.com/nicholasdelrio/visko/master/resources/views/volume.owl#volume IN visko:mozilla-firefox
WHERE
	FORMAT = formats:BINARYINTARRAYLENDIAN.owl#BINARYINTARRAYLENDIAN 
	AND TYPE = types:d7-0 
	AND params:colorFunction = 20,1.0,0.0,0.3/80,1.0,0.0,0.3
	AND params:opacityFunction = 0,0.0/40,1.0
	AND params:backgroundColor = 0/0/0
</pre>
</div>
<table>
<tr><td align="center"><a href="http://trust.utep.edu/visko/ql-examples/pipelines/pipeline-vtk-coverage-volume.htm">Pipeline</a></td></tr>
<tr><td><img src="http://trust.utep.edu/visko/ql-examples/visualizations/coverage-vtk-volume.png" width="450px"></td></tr>
</table>

<a name="brightness"/>
<h3>Queries Associated with Brightness/Temperature Data</h3>

<a name="bright-ex1">
<h5>13. Visualizing brightness temperature as contour map with: lines, colors, vertical color bar, interval of 10, and rainbow color table</h5>
<div class="code" style="overflow:scroll;">
<pre>
PREFIX formats https://raw.github.com/nicholasdelrio/visko/master/resources/formats/ 
PREFIX visko REPLACE-VISKOpackage_mozilla.owl# 
PREFIX params REPLACE-VISKOgsn_csm_contour_map.owl# 
VISUALIZE http://disc2.nascom.nasa.gov/daac-bin/OTF/HTTP_services.cgi?SERVICE=SUBSET_YOTC_LATS4D&BBOX=-65.390625,7.734375,-10.546875,42.890625&SHORTNAME=mergedIR&VARIABLES=ch4&TIME=2008-05-31T00:00:00
AS https://raw.github.com/nicholasdelrio/visko/master/resources/views/contour-lines.owl#contour-lines IN visko:mozilla-firefox
WHERE
	FORMAT = formats:NETCDF.owl#NETCDF 
	AND TYPE = http://giovanni.gsfc.nasa.gov/data/brightness.owl#brightness 
	AND params:cnFillOn = True
	AND params:cnLinesOn = True
	AND params:cnLevelSpacingF = 20 
	AND params:font = helvetica 
	AND params:lbOrientation = vertical 
	AND params:colorTable = rainbow
</pre>
</div>
<table>
<tr><td align="center"><a href="http://trust.utep.edu/visko/ql-examples/pipelines/pipeline-ncl-brightness-contourmap.htm">Pipeline</a></td></tr>
<tr><td><img src="http://trust.utep.edu/visko/ql-examples/visualizations/brightness-contour.png" width="450px"></td></tr>
</table>

<a name="bright-ex2">
<h5>14. Visualizing Brightness Temperature as Raster Image with: Annotations in Helvetica, Horizontal Color Bar, and Rainbow Color Table</h5>
<div class="code"  style="overflow:scroll;">
<pre>
PREFIX formats https://raw.github.com/nicholasdelrio/visko/master/resources/formats/ 
PREFIX visko REPLACE-VISKOpackage_mozilla.owl# 
PREFIX params REPLACE-VISKOgsn_csm_contour_map_raster.owl# 
VISUALIZE http://disc2.nascom.nasa.gov/daac-bin/OTF/HTTP_services.cgi?SERVICE=SUBSET_YOTC_LATS4D&BBOX=-65.390625,7.734375,-10.546875,42.890625&SHORTNAME=mergedIR&VARIABLES=ch4&TIME=2008-05-31T00:00:00
AS https://raw.github.com/nicholasdelrio/visko/master/resources/views/raster.owl#raster IN visko:mozilla-firefox
WHERE
	FORMAT = formats:NETCDF.owl#NETCDF
	AND TYPE = http://giovanni.gsfc.nasa.gov/data/brightness.owl#brightness 
	AND params:cnFillOn = True
	AND params:cnLinesOn = True 
	AND params:cnLevelSpacingF = 20 
	AND params:font = helvetica 
	AND params:lbOrientation = vertical
	AND params:colorTable = rainbow
</pre>
</div>
<table>
<tr><td align="center"><a href="http://trust.utep.edu/visko/ql-examples/pipelines/pipeline-ncl-brightness-rastermap.htm">Pipeline</a></td></tr>
<tr><td><img src="http://trust.utep.edu/visko/ql-examples/visualizations/brightness-raster.png" width="450px"></td></tr>
</table>

<a name="polygons">
<h4>Queries Associated with Polygon Data</h4>

<a name="cube-ex1">
<h5>15. VTK Polygon Cube</h5>
<div class="code">
<pre>
PREFIX formats https://raw.github.com/nicholasdelrio/visko/master/resources/formats/ 
PREFIX types http://rio.cs.utep.edu/ciserver/ciprojects/HolesCode/HolesCodeWDO.owl# 
PREFIX visko REPLACE-VISKOpackage_mozilla.owl# 
PREFIX renderParams REPLACE-VISKOvtkPolyDataMapper.owl# 
VISUALIZE http://rio.cs.utep.edu/ciserver/ciprojects/HolesCode/HolesCodeWDO.owl#
AS * IN visko:mozilla-firefox
WHERE
	FORMAT = formats:VTKPOLYDATA.owl#VTKPOLYDATA 
	AND TYPE = types:d2 
	AND renderParams:backgroundColor = 0/1/0 
	AND renderParams:magnification = 3 
	AND renderParams:xRotation = 15 
	AND renderParams:yRotation = 30 
	AND renderParams:zRotation = 0
</pre>
</div>
<table>
<tr><td align="center"><a href="http://trust.utep.edu/visko/ql-examples/pipelines/pipeline-vtk-cube.htm">Pipeline</a></td></tr>
<tr><td><img src="http://trust.utep.edu/visko/ql-examples/visualizations/cube-vtk.png" width="450px"></td></tr>
</table>


</div>
</div>

<%= Template.getFooter() %>

</body>
</html>