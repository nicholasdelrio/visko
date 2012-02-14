package edu.utep.trustlab.visko.knowledge.universal.profiles;


import edu.utep.trustlab.publish.Server;
import edu.utep.trustlab.visko.knowledge.NickCIServer;
import edu.utep.trustlab.visko.ontology.service.writer.ToolkitProfileWriter;

public class BrightnessTemperatureProfile {
	public static void main(String[] args) {
		
		Server.setServer(NickCIServer.getServer());
		String documentURL;
		
		/************ profile for brightness data ******************************/
		String dataTypeURI = "http://giovanni.gsfc.nasa.gov/data/brightness.owl#brightness";

		ToolkitProfileWriter wtr = new ToolkitProfileWriter(
			"brightnessDataProfile");
		wtr.addDataType(dataTypeURI);

		// for netCDFGridContour
		wtr.addInputBinding(
				"http://trust.utep.edu/visko/services/netCDFGridContour.owl#plotVariable",
				"ch4");
		wtr.addInputBinding(
				"http://trust.utep.edu/visko/services/netCDFGridContour.owl#lbOrientation",
				"vertical");
		wtr.addInputBinding(
				"http://trust.utep.edu/visko/services/netCDFGridContour.owl#cnLevelSpacingF",
				"10");
		wtr.addInputBinding(
				"http://trust.utep.edu/visko/services/netCDFGridContour.owl#colorTable",
				"WhiteBlueGreenYellowRed");
		wtr.addInputBinding(
				"http://trust.utep.edu/visko/services/netCDFGridContour.owl#font",
				"helvetica");
		wtr.addInputBinding(
				"http://trust.utep.edu/visko/services/netCDFGridContour.owl#cnFillOn",
				"True");
		wtr.addInputBinding(
				"http://trust.utep.edu/visko/services/netCDFGridContour.owl#cnLinesOn",
				"False");

		// for netCDFGridRaster
		wtr.addInputBinding(
				"http://trust.utep.edu/visko/services/netCDFGridRaster.owl#plotVariable",
				"ch4");
		wtr.addInputBinding(
				"http://trust.utep.edu/visko/services/netCDFGridRaster.owl#lbOrientation",
				"vertical");
		wtr.addInputBinding(
				"http://trust.utep.edu/visko/services/netCDFGridRaster.owl#colorTable",
				"WhiteBlueGreenYellowRed");
		wtr.addInputBinding(
				"http://trust.utep.edu/visko/services/netCDFGridRaster.owl#font",
				"helvetica");

		wtr.setSupportingToolkit("http://rio.cs.utep.edu/ciserver/ciprojects/visko/ncl.owl#ncl");
		documentURL = wtr.saveDocument();
		System.out.println(documentURL);
	}
}
