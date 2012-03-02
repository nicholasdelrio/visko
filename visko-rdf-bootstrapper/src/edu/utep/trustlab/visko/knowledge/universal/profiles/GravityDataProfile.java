package edu.utep.trustlab.visko.knowledge.universal.profiles;

import edu.utep.trustlab.visko.ontology.service.writer.ToolkitProfileWriter;

public class GravityDataProfile {
	public static void create() {
		String documentURL;

		// gravity data
		String dataTypeURI = "http://rio.cs.utep.edu/ciserver/ciprojects/CrustalModeling/CrustalModeling.owl#d19";
		ToolkitProfileWriter wtr2 = new ToolkitProfileWriter(
		"gravityDataProfile");
		wtr2.setSupportingToolkit("https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/gmt.owl#gmt");
		wtr2.addDataType(dataTypeURI);
		// for 2D plotter
		wtr2.addInputBinding(
		"http://trust.utep.edu/visko/services/GravityASCIIPointsTo2DPlotPS.owl#S",
		"c0.04c");
		wtr2.addInputBinding(
		"http://trust.utep.edu/visko/services/GravityASCIIPointsTo2DPlotPS.owl#J",
		"x4c");
		wtr2.addInputBinding(
		"http://trust.utep.edu/visko/services/GravityASCIIPointsTo2DPlotPS.owl#G",
		"blue");
		wtr2.addInputBinding(
		"http://trust.utep.edu/visko/services/GravityASCIIPointsTo2DPlotPS.owl#B",
		"1");
		// for nearneightbor gridding
		wtr2.addInputBinding(
		"http://trust.utep.edu/visko/services/GravityASCIIPointsToNearNeightborESRIGridded.owl#I",
		"0.02");
		wtr2.addInputBinding(
		"http://trust.utep.edu/visko/services/GravityASCIIPointsToNearNeightborESRIGridded.owl#S",
		"0.2");
		// for gridding by surface
		wtr2.addInputBinding(
		"http://trust.utep.edu/visko/services/GravityASCIIPointsToMinCurvatureESRIGridded.owl#I",
		"0.02");
		wtr2.addInputBinding(
		"http://trust.utep.edu/visko/services/GravityASCIIPointsToMinCurvatureESRIGridded.owl#T",
		"0.25");
		wtr2.addInputBinding(
		"http://trust.utep.edu/visko/services/GravityASCIIPointsToMinCurvatureESRIGridded.owl#C",
		"0.1");
		// for grid to colored image
		wtr2.addInputBinding(
		"http://trust.utep.edu/visko/services/ESRIGriddedToColoredImagePS.owl#B",
		"1");
		wtr2.addInputBinding(
		"http://trust.utep.edu/visko/services/ESRIGriddedToColoredImagePS.owl#J",
		"x4c");
		wtr2.addInputBinding(
		"http://trust.utep.edu/visko/services/ESRIGriddedToColoredImagePS.owl#C",
		"hot");
		// for contour map
		wtr2.addInputBinding(
		"http://trust.utep.edu/visko/services/ESRIGriddedToContourMapPS.owl#C",
		"10");
		wtr2.addInputBinding(
		"http://trust.utep.edu/visko/services/ESRIGriddedToContourMapPS.owl#A",
		"20");
		wtr2.addInputBinding(
		"http://trust.utep.edu/visko/services/ESRIGriddedToContourMapPS.owl#B",
		"0.5");
		wtr2.addInputBinding(
		"http://trust.utep.edu/visko/services/ESRIGriddedToContourMapPS.owl#S",
		"5");
		wtr2.addInputBinding(
		"http://trust.utep.edu/visko/services/ESRIGriddedToContourMapPS.owl#J",
		"x4c");
		wtr2.addInputBinding(
		"http://trust.utep.edu/visko/services/ESRIGriddedToContourMapPS.owl#Wc",
		"thinnest,black");
		wtr2.addInputBinding(
		"http://trust.utep.edu/visko/services/ESRIGriddedToContourMapPS.owl#Wa",
		"thinnest,black");

		// for esriGridContour
		wtr2.addInputBinding(
		"http://trust.utep.edu/visko/services/esriGridContour.owl#lbOrientation",
		"vertical");
		wtr2.addInputBinding(
		"http://trust.utep.edu/visko/services/esriGridContour.owl#cnLevelSpacingF",
		"10");
		wtr2.addInputBinding(
		"http://trust.utep.edu/visko/services/esriGridContour.owl#colorTable",
		"WhiteBlueGreenYellowRed");
		wtr2.addInputBinding(
		"http://trust.utep.edu/visko/services/esriGridContour.owl#font",
		"helvetica");
		wtr2.addInputBinding(
		"http://trust.utep.edu/visko/services/esriGridContour.owl#cnFillOn",
		"True");
		wtr2.addInputBinding(
		"http://trust.utep.edu/visko/services/esriGridContour.owl#cnLinesOn",
		"False");

		// for esriGridRaster
		wtr2.addInputBinding(
		"http://trust.utep.edu/visko/services/esriGridRaster.owl#lbOrientation",
		"vertical");
		wtr2.addInputBinding(
		"http://trust.utep.edu/visko/services/esriGridRaster.owl#colorTable",
		"WhiteBlueGreenYellowRed");
		wtr2.addInputBinding(
		"http://trust.utep.edu/visko/services/esriGridRaster.owl#font",
		"helvetica");

		// dump document
		documentURL = wtr2.saveDocument();
		System.out.println(documentURL);
		}
}
