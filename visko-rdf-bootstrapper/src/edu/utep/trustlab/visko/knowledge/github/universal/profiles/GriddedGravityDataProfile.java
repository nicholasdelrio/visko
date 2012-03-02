package edu.utep.trustlab.visko.knowledge.github.universal.profiles;
import edu.utep.trustlab.visko.ontology.service.writer.ToolkitProfileWriter;

public class GriddedGravityDataProfile {

	/**
	 * @param args
	 */
	public static void create() {
		// gridded data
		String dataTypeURI = "http://rio.cs.utep.edu/ciserver/ciprojects/CrustalModeling/CrustalModeling.owl#d12";
		ToolkitProfileWriter wtr1 = new ToolkitProfileWriter(
				"griddedGravityDataProfile");
		wtr1.setSupportingToolkit("https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/gmt.owl#gmt");
		wtr1.addDataType(dataTypeURI);
		// for colored image
		wtr1.addInputBinding(
				"http://trust.utep.edu/visko/services/ESRIGriddedToColoredImagePS.owl#B",
				"1");
		wtr1.addInputBinding(
				"http://trust.utep.edu/visko/services/ESRIGriddedToColoredImagePS.owl#J",
				"x4c");
		wtr1.addInputBinding(
				"http://trust.utep.edu/visko/services/ESRIGriddedToColoredImagePS.owl#C",
				"hot");
		// for contour map
		wtr1.addInputBinding(
				"http://trust.utep.edu/visko/services/ESRIGriddedToContourMapPS.owl#C",
				"10");
		wtr1.addInputBinding(
				"http://trust.utep.edu/visko/services/ESRIGriddedToContourMapPS.owl#A",
				"20");
		wtr1.addInputBinding(
				"http://trust.utep.edu/visko/services/ESRIGriddedToContourMapPS.owl#B",
				"0.5");
		wtr1.addInputBinding(
				"http://trust.utep.edu/visko/services/ESRIGriddedToContourMapPS.owl#S",
				"5");
		wtr1.addInputBinding(
				"http://trust.utep.edu/visko/services/ESRIGriddedToContourMapPS.owl#J",
				"x4c");
		wtr1.addInputBinding(
				"http://trust.utep.edu/visko/services/ESRIGriddedToContourMapPS.owl#Wc",
				"thinnest,black");
		wtr1.addInputBinding(
				"http://trust.utep.edu/visko/services/ESRIGriddedToContourMapPS.owl#Wa",
				"thinnest,black");
		// dump document
		String documentURL = wtr1.saveDocument();
		System.out.println(documentURL);

	}

}
