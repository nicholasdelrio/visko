/*
Copyright (c) 2012, University of Texas at El Paso
All rights reserved.
Redistribution and use in source and binary forms, with or without modification, are permitted
provided that the following conditions are met:

	-Redistributions of source code must retain the above copyright notice, this list of conditions
	 and the following disclaimer.
	-Redistributions in binary form must reproduce the above copyright notice, this list of conditions
	 and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.*/


package edu.utep.trustlab.visko.knowledge.universal.profiles;

import edu.utep.trustlab.visko.knowledge.gmt.GMTServices;
import edu.utep.trustlab.visko.knowledge.gmt.GMTToolkits;
import edu.utep.trustlab.visko.knowledge.ncl.NCLServices;
import edu.utep.trustlab.visko.ontology.service.writer.ToolkitProfileWriter;

public class GravityDataProfile {
	public static void create() {
		String documentURL;

		// gravity data
		String dataTypeURI = "http://rio.cs.utep.edu/ciserver/ciprojects/CrustalModeling/CrustalModeling.owl#d19";
		ToolkitProfileWriter wtr2 = new ToolkitProfileWriter(
		"gravityDataProfile");
		wtr2.setSupportingToolkit(GMTToolkits.gmt);
		wtr2.addDataType(dataTypeURI);

		String points22DPlot = GMTServices.points22DPlot.substring(0, GMTServices.points22DPlot.indexOf("#"));
		String points2NearNeighborGridded = GMTServices.points2NearNeighborGridded.substring(0, GMTServices.points2NearNeighborGridded.indexOf("#"));
		String points2MinCurvatureGridded = GMTServices.points2MinCurvatureGridded.substring(0, GMTServices.points2MinCurvatureGridded.indexOf("#"));
		String gridded2Raster = GMTServices.gridded2Raster.substring(0, GMTServices.gridded2Raster.indexOf("#"));
		String gridded2ContourMap = GMTServices.gridded2ContourMap.substring(0, GMTServices.gridded2ContourMap.indexOf("#"));
		String esriGridRaster = NCLServices.esriGridRaster.substring(0, NCLServices.esriGridRaster.indexOf("#"));
		String esriGridContour = NCLServices.esriGridContour.substring(0, NCLServices.esriGridContour.indexOf("#"));
		
		// for 2D plotter 
		wtr2.addInputBinding(
		points22DPlot + "#S",
		"c0.04c");
		wtr2.addInputBinding(
		points22DPlot + "#J",
		"x4c");
		wtr2.addInputBinding(
		points22DPlot + "#G",
		"blue");
		wtr2.addInputBinding(
		points22DPlot + "#B",
		"1");
		// for nearneightbor gridding
		wtr2.addInputBinding(
		points2NearNeighborGridded + "#I",
		"0.02");
		wtr2.addInputBinding(
		points2NearNeighborGridded + "#S",
		"0.2");
		// for gridding by surface
		wtr2.addInputBinding(
		points2MinCurvatureGridded + "#I",
		"0.02");
		wtr2.addInputBinding(
		points2MinCurvatureGridded + "#T",
		"0.25");
		wtr2.addInputBinding(
		points2MinCurvatureGridded + "#C",
		"0.1");
		// for grid to colored image
		wtr2.addInputBinding(
		gridded2Raster + "#B",
		"1");
		wtr2.addInputBinding(
		gridded2Raster + "#J",
		"x4c");
		wtr2.addInputBinding(
		gridded2Raster + "#C",
		"hot");
		// for contour map
		wtr2.addInputBinding(
		gridded2ContourMap + "#C",
		"10");
		wtr2.addInputBinding(
		gridded2ContourMap + "#A",
		"20");
		wtr2.addInputBinding(
		gridded2ContourMap + "#B",
		"0.5");
		wtr2.addInputBinding(
		gridded2ContourMap + "#S",
		"5");
		wtr2.addInputBinding(
		gridded2ContourMap + "#J",
		"x4c");
		wtr2.addInputBinding(
		gridded2ContourMap + "#Wc",
		"thinnest,black");
		wtr2.addInputBinding(
		gridded2ContourMap + "#Wa",
		"thinnest,black");

		// for esriGridContour
		wtr2.addInputBinding(
		esriGridContour + "#lbOrientation",
		"vertical");
		wtr2.addInputBinding(
		esriGridContour + "#cnLevelSpacingF",
		"10");
		wtr2.addInputBinding(
		esriGridContour + "#colorTable",
		"WhiteBlueGreenYellowRed");
		wtr2.addInputBinding(
		esriGridContour + "#font",
		"helvetica");
		wtr2.addInputBinding(
		esriGridContour + "#cnFillOn",
		"True");
		wtr2.addInputBinding(
		esriGridContour + "#cnLinesOn",
		"False");

		// for esriGridRaster
		wtr2.addInputBinding(
		esriGridRaster + "lbOrientation",
		"vertical");
		wtr2.addInputBinding(
		esriGridRaster + "colorTable",
		"WhiteBlueGreenYellowRed");
		wtr2.addInputBinding(
		esriGridRaster + "font",
		"helvetica");

		// dump document
		documentURL = wtr2.saveDocument();
		System.out.println(documentURL);
		}
}