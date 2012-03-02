package edu.utep.trustlab.visko.knowledge.universal.profiles;

import edu.utep.trustlab.visko.ontology.service.writer.ToolkitProfileWriter;

public class BrightnessTemperatureProfile {
	public static void create() {
		String documentURL;

		/************ profile for brightness data ******************************/
		String dataTypeURI = "http://giovanni.gsfc.nasa.gov/data/brightness.owl#brightness";

		ToolkitProfileWriter wtr = new ToolkitProfileWriter("brightnessDataProfile");
		wtr.addDataType(dataTypeURI);

		// for netCDFGridContour
		wtr.addInputBinding("https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/netCDFGridContour.owl#plotVariable", "ch4");
		wtr.addInputBinding("https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/netCDFGridContour.owl#lbOrientation", "vertical");
		wtr.addInputBinding("https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/netCDFGridContour.owl#cnLevelSpacingF", "10");
		wtr.addInputBinding("https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/netCDFGridContour.owl#colorTable", "WhiteBlueGreenYellowRed");
		wtr.addInputBinding("https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/netCDFGridContour.owl#font", "helvetica");
		wtr.addInputBinding("https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/netCDFGridContour.owl#cnFillOn", "True");
		wtr.addInputBinding("https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/netCDFGridContour.owl#cnLinesOn", "False");

		// for netCDFGridRaster
		wtr.addInputBinding("https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/netCDFGridRaster.owl#plotVariable", "ch4");
		wtr.addInputBinding("https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/netCDFGridRaster.owl#lbOrientation", "vertical");
		wtr.addInputBinding("https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/netCDFGridRaster.owl#colorTable", "WhiteBlueGreenYellowRed");
		wtr.addInputBinding("https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/netCDFGridRaster.owl#font", "helvetica");

		wtr.setSupportingToolkit("https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/ncl.owl#ncl");
		documentURL = wtr.saveDocument();
		System.out.println(documentURL);
	}
}
