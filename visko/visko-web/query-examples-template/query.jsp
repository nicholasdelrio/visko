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
<li><a href="#brightness">NASA Goddard MODIS Brightness Visualization Queries</a>
<li><a href="#solar">NASA JPL Solar Image Processing Queries</a>
<li><a href="#polygons">Polygon Data Queries</a>
<li><a href="#visko">Visko Knowledge Base Visualization Queries</a>
<li><a href="#ecology">UTEP Systems Ecology Lab Visualization Queries</a>
<li><a href="#sparql">SPARQL/XML Results Visualization Queries</a>
</ul>

<hr/>
<a name="gravity"/>
<h3>Gravity Data Visualization Queries</h3>
<p>Datasets used in this example set:</p>
<table class="visko" border="1">
	<tr>
		<td><b>Data Type</b></td>
		<td><b>Location</b></td>
		<td><b>Provider</b></td>
	</tr>
	<tr>
		<td>Gravity Dataset</td>
		<td><a href="SERVERBASEURLvisko-web/test-data/gravity/gravityDataset.txt">URL</a></td>
		<td><a href="http://research.utep.edu/Default.aspx?alias=research.utep.edu/paces">UTEP PACES</td>
	</tr>
</table>

<h5>All Possible Visualizations: (AS *)</h5>
<div class="code">
<pre>
PREFIX formats http://openvisko.org/rdf/pml2/formats/
PREFIX types http://rio.cs.utep.edu/ciserver/ciprojects/CrustalModeling/CrustalModeling.owl#
PREFIX visko REPLACE-VISKOmodule_webbrowser.owl#
VISUALIZE SERVERBASEURLvisko-web/test-data/gravity/gravityDataset.txt
AS * IN visko:web-browser
WHERE
	FORMAT = formats:SPACESEPARATEDVALUES.owl#SPACESEPARATEDVALUES
	AND TYPE = types:d19
</pre>
</div>

<table>
<tr>
<td align="center"><a href="http://gmt.soest.hawaii.edu/">GMT</a></td>
<td align="center"><a href="http://www.vtk.org/">VTK</a></td>
<td align="center"><a href="http://www.ncl.ucar.edu/">GMT</a></td>
<td align="center"><a href="http://www.ncl.ucar.edu/">NCL</a></td>
</tr>
<tr>
<td><img src="./visualization-examples/gravity-2d-contourmap-gmt.png" width="250px"></td>
<td><img src="./visualization-examples/gravity-2d-contourmap-vtk.png" width="250px"></td>
<td><img src="./visualization-examples/gravity-3d-barchart-gmt.png" width="250px"></td>
<td><img src="./visualization-examples/gravity-2d-contourmap-ncl.png" width="250px"></td>
</tr>
<tr>
<td align="center"><a href="http://gmt.soest.hawaii.edu/">GMT</a></td>
<td align="center"><a href="http://www.vtk.org/">VTK</a></td>
<td align="center"><a href="http://www.vtk.org/">VTK</a></td>
<td align="center"><a href="http://www.vtk.org/">NCL</a></td>
</tr>
<tr>
<td><img src="./visualization-examples/gravity-2d-pointmap-gmt.png" width="250px"></td>
<td><img src="./visualization-examples/gravity-3d-surfaceplot-vtk.png" width="250px"></td>
<td><img src="./visualization-examples/gravity-3d-isosurfacesrendering-vtk.png" width="250px"></td>
<td><img src="./visualization-examples/gravity-2d-rastermap-ncl.png" width="250px"></td>
</tr>
<tr>
<td align="center"><a href="http://gmt.soest.hawaii.edu/">GMT</a></td>
<td align="center"><a href="http://www.vtk.org/">VTK</a></td>
<td align="center"><a href="http://www.vtk.org/">GMT</a></td>
</tr>
<tr>
<td><img src="./visualization-examples/gravity-2d-rastermap-gmt.png" width="250px"></td>
<td><img src="./visualization-examples/gravity-3d-pointplot-vtk.png" width="250px"></td>
<td><img src="./visualization-examples/gravity-3d-barchart-ungridded-gmt.png" width="250px"></td>
</tr>
</table>

<h5>Contour Map Visualization Query</h5>
<div class="code">
<pre>
PREFIX views http://openvisko.org/rdf/ontology/visko-view.owl#
PREFIX formats http://openvisko.org/rdf/pml2/formats/
PREFIX types http://rio.cs.utep.edu/ciserver/ciprojects/CrustalModeling/CrustalModeling.owl#
PREFIX visko REPLACE-VISKOmodule_webbrowser.owl#
PREFIX params REPLACE-VISKOgrdcontour.owl#
VISUALIZE SERVERBASEURLvisko-web/test-data/gravity/gravityDataset.txt
AS views:2D_ContourMap IN visko:web-browser
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
PREFIX views http://openvisko.org/rdf/ontology/visko-view.owl#
PREFIX formats http://openvisko.org/rdf/pml2/formats/ 
PREFIX types http://rio.cs.utep.edu/ciserver/ciprojects/CrustalModeling/CrustalModeling.owl# 
PREFIX visko REPLACE-VISKOmodule_webbrowser.owl# 
VISUALIZE SERVERBASEURLvisko-web/test-data/gravity/gravityDataset.txt
AS views:2D_RasterMap IN visko:web-browser
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
PREFIX views http://openvisko.org/rdf/ontology/visko-view.owl#
PREFIX formats http://openvisko.org/rdf/pml2/formats/ 
PREFIX types http://rio.cs.utep.edu/ciserver/ciprojects/CrustalModeling/CrustalModeling.owl# 
PREFIX visko REPLACE-VISKOmodule_webbrowser.owl# 
VISUALIZE SERVERBASEURLvisko-web/test-data/gravity/gravityDataset.txt
AS views:2D_PointMap IN visko:web-browser
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
PREFIX views http://openvisko.org/rdf/ontology/visko-view.owl#
PREFIX formats http://openvisko.org/rdf/pml2/formats/ 
PREFIX types http://rio.cs.utep.edu/ciserver/ciprojects/CrustalModeling/CrustalModeling.owl# 
PREFIX visko REPLACE-VISKOmodule_webbrowser.owl# 
VISUALIZE SERVERBASEURLvisko-web/test-data/gravity/gravityDataset.txt
AS views:3D_PointPlot IN visko:web-browser
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
PREFIX views http://openvisko.org/rdf/ontology/visko-view.owl#
PREFIX formats http://openvisko.org/rdf/pml2/formats/ 
PREFIX types http://rio.cs.utep.edu/ciserver/ciprojects/CrustalModeling/CrustalModeling.owl# 
PREFIX visko REPLACE-VISKOmodule_webbrowser.owl# 
VISUALIZE SERVERBASEURLvisko-web/test-data/gravity/gravityDataset.txt
AS views:3D_IsoSurfacesRendering IN visko:web-browser
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


<h5>Isosurfaces Rendering Query in ParaView</h5>
<div class="code">
<pre>
PREFIX views http://openvisko.org/rdf/ontology/visko-view.owl#
PREFIX formats http://openvisko.org/rdf/pml2/formats/ 
PREFIX types http://rio.cs.utep.edu/ciserver/ciprojects/CrustalModeling/CrustalModeling.owl# 
PREFIX visko REPLACE-VISKOmodule_paraview.owl# 
VISUALIZE SERVERBASEURLvisko-web/test-data/gravity/gravityDataset.txt
AS views:3D_IsoSurfacesRendering IN visko:paraview 
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
<td><img src="./visualization-examples/gravity-3d-isosurfacesrendering-paraview.png" width="350px"></td>
</tr>
</table>


<h5>Surface Plot Query</h5>
<div class="code">
<pre>
PREFIX views http://openvisko.org/rdf/ontology/visko-view.owl#
PREFIX formats http://openvisko.org/rdf/pml2/formats/ 
PREFIX types http://rio.cs.utep.edu/ciserver/ciprojects/CrustalModeling/CrustalModeling.owl# 
PREFIX visko REPLACE-VISKOmodule_webbrowser.owl# 
VISUALIZE SERVERBASEURLvisko-web/test-data/gravity/gravityDataset.txt
AS views:3D_SurfacePlot IN visko:web-browser
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

<hr/>
<a name="holes"/>
<h3>Queries Associated with Seismic Tomography</h3>

<p>Datasets used in this example set:</p>
<table class="visko" border="1">
	<tr>
		<td><b>Data Type</b></td>
		<td><b>Location</b></td>
		<td><b>Provider</b></td>
	</tr>
	<tr>
		<td>Velocity Model</td>
		<td><a href="SERVERBASEURLvisko-web/test-data/holes/velocity-data.3d">URL</a></td>
		<td><a href="http://cybershare.utep.edu/">Cyber-ShARE Center</a></td>
	</tr>
	<tr>
		<td>Slowness Perturbation</td>
		<td><a href="SERVERBASEURLvisko-web/test-data/holes/dusum.3d">URL</a></td>
		<td><a href="http://cybershare.utep.edu/">Cyber-ShARE Center</a></td>
	</tr>
	<tr>
		<td>Arrival Time Field</td>
		<td><a href="SERVERBASEURLvisko-web/test-data/holes/time-data.3d">URL</a></td>
		<td><a href="http://cybershare.utep.edu/">Cyber-ShARE Center</a></td>
	</tr>
	<tr>
		<td>Ray Coverage</td>
		<td><a href="SERVERBASEURLvisko-web/test-data/holes/icov-data.3d">URL</a></td>
		<td><a href="http://cybershare.utep.edu/">Cyber-ShARE Center</a></td>
	</tr>
</table>


<h5>Velocity Model Isosurfaces Rendering Query</h5>
<div class="code">
<pre>
PREFIX views http://openvisko.org/rdf/ontology/visko-view.owl#
PREFIX formats http://openvisko.org/rdf/pml2/formats/
PREFIX types http://rio.cs.utep.edu/ciserver/ciprojects/HolesCode/HolesCodeWDO.owl# 
PREFIX visko REPLACE-VISKOmodule_webbrowser.owl#
VISUALIZE SERVERBASEURLvisko-web/test-data/holes/velocity-data.3d
AS views:3D_IsoSurfacesRendering IN visko:web-browser
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
PREFIX views http://openvisko.org/rdf/ontology/visko-view.owl#
PREFIX formats http://openvisko.org/rdf/pml2/formats/
PREFIX types http://rio.cs.utep.edu/ciserver/ciprojects/HolesCode/HolesCodeWDO.owl# 
PREFIX visko REPLACE-VISKOmodule_webbrowser.owl#
VISUALIZE SERVERBASEURLvisko-web/test-data/holes/velocity-data.3d
AS views:2D_ContourMap IN visko:web-browser
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
PREFIX views http://openvisko.org/rdf/ontology/visko-view.owl#
PREFIX formats http://openvisko.org/rdf/pml2/formats/
PREFIX types http://rio.cs.utep.edu/ciserver/ciprojects/HolesCode/HolesCodeWDO.owl# 
PREFIX visko REPLACE-VISKOmodule_webbrowser.owl#
VISUALIZE SERVERBASEURLvisko-web/test-data/holes/velocity-data.3d
AS views:3D_VolumeRendering IN visko:web-browser
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
PREFIX views http://openvisko.org/rdf/ontology/visko-view.owl#
PREFIX formats http://openvisko.org/rdf/pml2/formats/
PREFIX types http://rio.cs.utep.edu/ciserver/ciprojects/HolesCode/HolesCodeWDO.owl# 
PREFIX visko REPLACE-VISKOmodule_webbrowser.owl#
VISUALIZE SERVERBASEURLvisko-web/test-data/holes/dusum.3d
AS views:3D_IsoSurfacesRendering IN visko:web-browser
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
PREFIX views http://openvisko.org/rdf/ontology/visko-view.owl#
PREFIX formats http://openvisko.org/rdf/pml2/formats/
PREFIX types http://rio.cs.utep.edu/ciserver/ciprojects/HolesCode/HolesCodeWDO.owl# 
PREFIX visko REPLACE-VISKOmodule_webbrowser.owl#
VISUALIZE SERVERBASEURLvisko-web/test-data/holes/time-data.3d
AS views:3D_IsoSurfacesRendering IN visko:web-browser
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
PREFIX views http://openvisko.org/rdf/ontology/visko-view.owl#
PREFIX formats http://openvisko.org/rdf/pml2/formats/
PREFIX types http://rio.cs.utep.edu/ciserver/ciprojects/HolesCode/HolesCodeSAW3.owl# 
PREFIX visko REPLACE-VISKOmodule_webbrowser.owl#
VISUALIZE SERVERBASEURLvisko-web/test-data/holes/icov-data.3d
AS views:3D_VolumeRendering IN visko:web-browser
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

<hr/>
<a name="brightness"/>
<h3>NASA MODIS Brightness Visualization</h3>

<p>Datasets used in this example set:</p>
<table class="visko" border="1">
	<tr>
		<td><b>Data Type</b></td>
		<td><b>Location</b></td>
		<td><b>Provider</b></td>
	</tr>
	<tr>
		<td>MODIS Brightness Temperature</td>
		<td><a href="SERVERBASEURLvisko-web/test-data/giovanni/SUBSET_mergedIR_ch4_2008-05-31.nc">URL</a></td>
		<td><a href="http://daac.gsfc.nasa.gov/">NASA Goddard Earth Sciences (GES) Data and Information Services Center (DISC)</td>
	</tr>
</table>


<h5>Contour Map Query</h5>
<div class="code" style="overflow:scroll;">
<pre>
PREFIX views http://openvisko.org/rdf/ontology/visko-view.owl#
PREFIX formats http://openvisko.org/rdf/pml2/formats/ 
PREFIX visko REPLACE-VISKOmodule_webbrowser.owl# 
PREFIX params REPLACE-VISKOgsn_csm_contour_map.owl# 
VISUALIZE SERVERBASEURLvisko-web/test-data/giovanni/SUBSET_mergedIR_ch4_2008-05-31.nc
AS views:2D_ContourMap IN visko:web-browser
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
PREFIX views http://openvisko.org/rdf/ontology/visko-view.owl#
PREFIX formats http://openvisko.org/rdf/pml2/formats/ 
PREFIX visko REPLACE-VISKOmodule_webbrowser.owl# 
PREFIX params REPLACE-VISKOgsn_csm_contour_map_raster.owl# 
VISUALIZE SERVERBASEURLvisko-web/test-data/giovanni/SUBSET_mergedIR_ch4_2008-05-31.nc
AS views:2D_RasterMap IN visko:web-browser
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

<hr/>
<a name="solar"/>
<h3>NASA JPL Solar Image Processing Queries</h3>

<p>Datasets used in this example set:</p>
<table class="visko" border="1">
	<tr>
		<td><b>Data Type</b></td>
		<td><b>Location</b></td>
		<td><b>Provider</b></td>
	</tr>
	<tr>
		<td>Solar Image Unwrapped Sphere</td>
		<td><a href="SERVERBASEURLvisko-web/test-data/jpl/20120717_120115_304_color.png">URL</a></td>
		<td><a href="http://www.jpl.nasa.gov/">NASA JPL</td>
	</tr>
		<tr>
		<td>Solar Image FITS</td>
		<td><a href="SERVERBASEURLvisko-web/test-data/jpl/AIAsynoptic0304.fits">URL</a></td>
		<td><a href="http://www.jpl.nasa.gov/">NASA JPL</td>
	</tr>
	
</table>

<h5>Sphere Wrap Query</h5>
<div class="code">
<pre>
PREFIX views http://openvisko.org/rdf/ontology/visko-view.owl#
PREFIX visko REPLACE-VISKOmodule_webbrowser.owl#
VISUALIZE SERVERBASEURLvisko-web/test-data/jpl/20120717_120115_304_color.png
AS views:2D_SpherizedRaster IN visko:web-browser 
WHERE
        FORMAT = http://openvisko.org/rdf/pml2/formats/PNG.owl#PNG
        AND TYPE = http://www.w3.org/2002/07/owl#Thing
</pre>
</div>

<table>
<tr>
<td align="center"><a href="http://www.imagemagick.org/script/index.php">ImageMagick</a></td>
</tr>

<tr>
<td><img src="./visualization-examples/solar-2d-spherizedraster-imageMagick.png" width="450px"></td>
</tr>
</table>

<h5>3D Surface Plot of 2D Image Query</h5>
<div class="code">
<pre>
PREFIX views http://openvisko.org/rdf/ontology/visko-view.owl#
PREFIX visko REPLACE-VISKOmodule_webbrowser.owl#
VISUALIZE SERVERBASEURLvisko-web/test-data/jpl/20120717_120115_304_color.png
AS views:3D_SurfacePlot IN visko:web-browser 
WHERE
        FORMAT = http://openvisko.org/rdf/pml2/formats/PNG.owl#PNG
        AND TYPE = http://www.w3.org/2002/07/owl#Thing
</pre>
</div>

<table>
<tr>
<td align="center"><a href="http://rsb.info.nih.gov/ij/">ImageJ</a></td>
</tr>

<tr>
<td><img src="./visualization-examples/solar-3d-surfaceplot-imageJ.png" width="450px"></td>
</tr>
</table>

<h5>Format Transformation: FITS to PNG</h5>
<div class="code">
<pre>
PREFIX views http://openvisko.org/rdf/ontology/visko-view.owl#
PREFIX visko REPLACE-VISKOmodule_webbrowser.owl#
VISUALIZE SERVERBASEURLvisko-web/test-data/jpl/AIAsynoptic0304.fits 
AS * IN visko:web-browser 
WHERE
        FORMAT = http://openvisko.org/rdf/pml2/formats/FITS.owl#FITS
        AND TYPE = http://www.w3.org/2002/07/owl#Thing
</pre>
</div>

<table>
<tr>
<td align="center"><a href="http://www.imagemagick.org/script/index.php">ImageJ</a></td>
</tr>

<tr>
<td><img src="./visualization-examples/solar-fits2png-imageMagick.png" width="450px"></td>
</tr>
</table>

<h5>3D Surface Plot of Solar FITS</h5>
<div class="code">
<pre>
PREFIX views http://openvisko.org/rdf/ontology/visko-view.owl#
PREFIX visko REPLACE-VISKOmodule_webbrowser.owl#
VISUALIZE SERVERBASEURLvisko-web/test-data/jpl/AIAsynoptic0304.fits
AS views:3D_SurfacePlot IN visko:web-browser 
WHERE
        FORMAT = http://openvisko.org/rdf/pml2/formats/FITS.owl#FITS
        AND TYPE = http://www.w3.org/2002/07/owl#Thing
</pre>
</div>

<table>
<tr>
<td align="center"><a href="http://rsb.info.nih.gov/ij/">ImageJ</a></td>
</tr>

<tr>
<td><img src="./visualization-examples/solar-fits-3d-surfaceplot-imageJ.png" width="450px"></td>
</tr>
</table>


<hr/>
<a name="polygons"/>
<h3>vtkPolyData Queries</h3>

<p>Datasets used in this example set:</p>
<table class="visko" border="1">
	<tr>
		<td><b>Data Type</b></td>
		<td><b>Location</b></td>
		<td><b>Provider</b></td>
	</tr>
	<tr>
		<td>VTK PolyData</td>
		<td><a href="SERVERBASEURLvisko-web/test-data/vtk/Cubes.xml">URL</a></td>
		<td><a href="http://cybershare.utep.edu/">Cyber-ShARE Center</td>
	</tr>
</table>

<h5>Cube Query</h5>
<div class="code">
<pre>
PREFIX vtkTypes http://www.vtk.org/vtk-data.owl#
PREFIX formats http://openvisko.org/rdf/pml2/formats/ 
PREFIX types http://rio.cs.utep.edu/ciserver/ciprojects/HolesCode/HolesCodeWDO.owl# 
PREFIX visko REPLACE-VISKOmodule_webbrowser.owl# 
PREFIX renderParams REPLACE-VISKOvtkPolyDataMapper.owl# 
VISUALIZE SERVERBASEURLvisko-web/test-data/vtk/Cubes.xml
AS * IN visko:web-browser
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

<hr/>
<a name="visko">
<h4>VisKo Queries</h4>

<p>Datasets used in this example set:</p>
<table class="visko" border="1">
	<tr>
		<td><b>Data Type</b></td>
		<td><b>Location</b></td>
		<td><b>Provider</b></td>
	</tr>
	<tr>
		<td>VisKo Knowledge Base</td>
		<td><a href="https://github.com/nicholasdelrio/visko-packages-rdf">URL</a></td>
		<td><a href="http://trust.utep.edu/members/nick/">Nicholas Del Rio</td>
	</tr>
</table>

<h5>Operator Paths Force Graph</h5>
<div class="code">
<pre>
PREFIX views http://openvisko.org/rdf/ontology/visko-view.owl#
PREFIX formats http://openvisko.org/rdf/pml2/formats/
PREFIX visko SERVERBASEURLvisko-web/registry/module_d3.owl#
PREFIX types http://openvisko.org/rdf/ontology/visko.owl#
VISUALIZE SERVERBASEURLvisko-web/registry
AS views:2D_VisKo_OperatorPaths_ForceGraph IN visko:d3-viewer-set
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
PREFIX views http://openvisko.org/rdf/ontology/visko-view.owl#
PREFIX formats http://openvisko.org/rdf/pml2/formats/
PREFIX visko SERVERBASEURLvisko-web/registry/module_d3.owl#
PREFIX types http://openvisko.org/rdf/ontology/visko.owl#
VISUALIZE SERVERBASEURLvisko-web/registry
AS views:2D_VisKo_DataTransformations_ForceGraph IN visko:d3-viewer-set
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
PREFIX views http://openvisko.org/rdf/ontology/visko-view.owl#
PREFIX formats http://openvisko.org/rdf/pml2/formats/
PREFIX visko SERVERBASEURLvisko-web/registry/module_d3.owl#
PREFIX types http://openvisko.org/rdf/ontology/visko.owl#
VISUALIZE SERVERBASEURLvisko-web/registry
AS views:2D_VisKo_Instances_BarChart IN visko:d3-viewer-set
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

<hr/>
<a name="ecology">
<h4>UTEP Systems Ecology Lab Visualization Queries</h4>

<p>Datasets used in this example set:</p>
<table class="visko" border="1">
	<tr>
		<td><b>Data Type</b></td>
		<td><b>Location</b></td>
		<td><b>Provider</b></td>
	</tr>
	<tr>
		<td>Time Series for Ecology Data</td>
		<td><a href="SERVERBASEURLvisko-web/test-data/ecology/average_data.nc">URL</a></td>
		<td><a href="http://cybershare.utep.edu/">Cyber-ShARE Center</td>
	</tr>
</table>

<h5>H20 Time Series</h5>
<div class="code">
<pre>
PREFIX timeSeries https://raw.github.com/nicholasdelrio/visko-packages-rdf/master/gsn_csm_xy2_time_series.owl#
VISUALIZE SERVERBASEURLvisko-web/test-data/ecology/average_data.nc 
AS http://openvisko.org/rdf/ontology/visko-view.owl#2D_TimeSeriesPlot 
IN SERVERBASEURLvisko-web/registry/module_webbrowser.owl#web-browser 
WHERE
	FORMAT = http://openvisko.org/rdf/pml2/formats/NETCDF.owl#NETCDF
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

<hr>

<a name="sparql"/>
<h4>SPARQL/XML Results Visualization Queries</h4>
<p>The visualizations below are generated from SPARQL query result data, encoded in SPARQL+XML.</p>

<p>SPARQLEndpoint used in this example set:</p>
<table class="visko" border="1">
	<tr>
		<td><b>Endpoint URL</b></td>
		<td><b>Contents</b></td>
		<td><b>Provider</b></td>
	</tr>
	<tr>
		<td><a href="SERVERBASEURLvisko-web/sparql-query.jsp">URL</a></td>
		<td>VisKo Knowledge Base</td>
		<td><a href="http://cybershare.utep.edu/">Cyber-ShARE Center</td>
	</tr>
</table>


<h5>What VisualizationAbstractions Can Visko Generate?</h5>
<p>Visualization Query (has SPARQL query encoded within)</p>
<div class="code" style="overflow:scroll;">
<pre>
PREFIX views http://openvisko.org/rdf/ontology/visko-view.owl#
PREFIX formats http://openvisko.org/rdf/pml2/formats/
PREFIX visko SERVERBASEURLvisko-web/registry/module_d3.owl#
PREFIX types http://openvisko.org/rdf/ontology/visko.owl#
VISUALIZE SERVERBASEURLvisko-web/ViskoServletManager?requestType=query-triple-store&query=PREFIX+viskoV%3A+%3Chttp%3A%2F%2Fopenvisko.org%2Frdf%2Fontology%2Fvisko-view.owl%23%3E%0D%0APREFIX+rdf%3A+%3Chttp%3A%2F%2Fwww.w3.org%2F1999%2F02%2F22-rdf-syntax-ns%23%3E%0D%0ASELECT+%0D%0A%3Fs%0D%0A%28COALESCE%28%3Fprd%2C+rdf%3Atype%29+AS+%3Fp%29%0D%0A%28COALESCE%28%3Fobj%2C+viskoV%3AVisualizationAbstraction%29+AS+%3Fo%29%0D%0AWHERE%0D%0A%7B%0D%0A%3Fs+a+viskoV%3AVisualizationAbstraction%0D%0A%7D
AS * IN visko:d3-viewer-set 
WHERE
	FORMAT = http://openvisko.org/rdf/pml2/formats/SPARQLRESULTSXML.owl#SPARQLRESULTSXML
	AND TYPE = http://www.w3.org/2002/07/owl#Thing
</pre>
</div>

<p>Source SPARQL Query:</p>
<div class="code" style="overflow:scroll;">
<pre>
PREFIX viskoV: &lt;http://openvisko.org/rdf/ontology/visko-view.owl#&gt;
PREFIX rdf: &lt;http://www.w3.org/1999/02/22-rdf-syntax-ns#&gt;
SELECT 
?s
(COALESCE(?prd, rdf:type) AS ?p)
(COALESCE(?obj, viskoV:VisualizationAbstraction) AS ?o)
WHERE
{
?s a viskoV:VisualizationAbstraction
}
</pre>
</div>

<table>
<tr>
<td align="center"><a href="http://d3js.org/">D3</a></td>
</tr>

<tr>
<td><img src="./visualization-examples/visualization-abstraction-sparql-2d-forcegraph-d3.png" width="450px"></td>
</tr>
</table>

</div>
</div>

<%= Template.getFooter() %>

</body>
</html>