package trustlab.visko.knowledge.universal.profiles;


import trustlab.publish.Server;
import trustlab.visko.knowledge.NickCIServer;
import trustlab.visko.ontology.service.writer.ToolkitProfileWriter;

public class GravityDataProfile {
	public static void main(String[] args) {
		Server.setServer(NickCIServer.getServer());
		String documentURL;

		// gravity data
		String dataTypeURI = "http://rio.cs.utep.edu/ciserver/ciprojects/CrustalModeling/CrustalModeling.owl#d19";
		ToolkitProfileWriter wtr2 = new ToolkitProfileWriter(
				"gravityDataProfile");
		wtr2.setSupportingToolkit("http://rio.cs.utep.edu/ciserver/ciprojects/visko/gmt1.owl#gmt1");
		wtr2.addDataType(dataTypeURI);
		// for 2D plotter
		wtr2.addInputBinding(
				"http://trust.utep.edu/visko/services/GravityASCIIPointsTo2DPlotPS1.owl#S",
				"c0.04c");
		wtr2.addInputBinding(
				"http://trust.utep.edu/visko/services/GravityASCIIPointsTo2DPlotPS1.owl#J",
				"x4c");
		wtr2.addInputBinding(
				"http://trust.utep.edu/visko/services/GravityASCIIPointsTo2DPlotPS1.owl#G",
				"blue");
		wtr2.addInputBinding(
				"http://trust.utep.edu/visko/services/GravityASCIIPointsTo2DPlotPS1.owl#B",
				"1");
		// for nearneightbor gridding
		wtr2.addInputBinding(
				"http://trust.utep.edu/visko/services/GravityASCIIPointsToNearNeightborESRIGridded1.owl#I",
				"0.02");
		wtr2.addInputBinding(
				"http://trust.utep.edu/visko/services/GravityASCIIPointsToNearNeightborESRIGridded1.owl#S",
				"0.2");
		// for gridding by surface
		wtr2.addInputBinding(
				"http://trust.utep.edu/visko/services/GravityASCIIPointsToMinCurvatureESRIGridded1.owl#I",
				"0.02");
		wtr2.addInputBinding(
				"http://trust.utep.edu/visko/services/GravityASCIIPointsToMinCurvatureESRIGridded1.owl#T",
				"0.25");
		wtr2.addInputBinding(
				"http://trust.utep.edu/visko/services/GravityASCIIPointsToMinCurvatureESRIGridded1.owl#C",
				"0.1");
		// for grid to colored image
		wtr2.addInputBinding(
				"http://trust.utep.edu/visko/services/ESRIGriddedToColoredImagePS1.owl#B",
				"1");
		wtr2.addInputBinding(
				"http://trust.utep.edu/visko/services/ESRIGriddedToColoredImagePS1.owl#J",
				"x4c");
		wtr2.addInputBinding(
				"http://trust.utep.edu/visko/services/ESRIGriddedToColoredImagePS1.owl#C",
				"hot");
		// for contour map
		wtr2.addInputBinding(
				"http://trust.utep.edu/visko/services/ESRIGriddedToContourMapPS1.owl#C",
				"10");
		wtr2.addInputBinding(
				"http://trust.utep.edu/visko/services/ESRIGriddedToContourMapPS1.owl#A",
				"20");
		wtr2.addInputBinding(
				"http://trust.utep.edu/visko/services/ESRIGriddedToContourMapPS1.owl#B",
				"0.5");
		wtr2.addInputBinding(
				"http://trust.utep.edu/visko/services/ESRIGriddedToContourMapPS1.owl#S",
				"5");
		wtr2.addInputBinding(
				"http://trust.utep.edu/visko/services/ESRIGriddedToContourMapPS1.owl#J",
				"x4c");
		wtr2.addInputBinding(
				"http://trust.utep.edu/visko/services/ESRIGriddedToContourMapPS1.owl#Wc",
				"thinnest,black");
		wtr2.addInputBinding(
				"http://trust.utep.edu/visko/services/ESRIGriddedToContourMapPS1.owl#Wa",
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
