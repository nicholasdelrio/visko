package edu.utep.trustlab.visko.knowledge.github.universal.profiles;


import edu.utep.trustlab.repository.Repository;
import edu.utep.trustlab.visko.knowledge.NickConfigurations;
import edu.utep.trustlab.visko.ontology.service.writer.ToolkitProfileWriter;

public class GriddedGravityDataProfile {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Repository.setRepository(NickConfigurations.getCIServer());
		// gridded data
		String dataTypeURI = "http://rio.cs.utep.edu/ciserver/ciprojects/CrustalModeling/CrustalModeling.owl#d12";
		ToolkitProfileWriter wtr1 = new ToolkitProfileWriter(
				"griddedGravityDataProfile");
		wtr1.setSupportingToolkit("http://rio.cs.utep.edu/ciserver/ciprojects/visko/gmt1.owl#gmt1");
		wtr1.addDataType(dataTypeURI);
		// for colored image
		wtr1.addInputBinding(
				"http://trust.utep.edu/visko/services/ESRIGriddedToColoredImagePS1.owl#B",
				"1");
		wtr1.addInputBinding(
				"http://trust.utep.edu/visko/services/ESRIGriddedToColoredImagePS1.owl#J",
				"x4c");
		wtr1.addInputBinding(
				"http://trust.utep.edu/visko/services/ESRIGriddedToColoredImagePS1.owl#C",
				"hot");
		// for contour map
		wtr1.addInputBinding(
				"http://trust.utep.edu/visko/services/ESRIGriddedToContourMapPS1.owl#C",
				"10");
		wtr1.addInputBinding(
				"http://trust.utep.edu/visko/services/ESRIGriddedToContourMapPS1.owl#A",
				"20");
		wtr1.addInputBinding(
				"http://trust.utep.edu/visko/services/ESRIGriddedToContourMapPS1.owl#B",
				"0.5");
		wtr1.addInputBinding(
				"http://trust.utep.edu/visko/services/ESRIGriddedToContourMapPS1.owl#S",
				"5");
		wtr1.addInputBinding(
				"http://trust.utep.edu/visko/services/ESRIGriddedToContourMapPS1.owl#J",
				"x4c");
		wtr1.addInputBinding(
				"http://trust.utep.edu/visko/services/ESRIGriddedToContourMapPS1.owl#Wc",
				"thinnest,black");
		wtr1.addInputBinding(
				"http://trust.utep.edu/visko/services/ESRIGriddedToContourMapPS1.owl#Wa",
				"thinnest,black");
		// dump document
		String documentURL = wtr1.saveDocument();
		System.out.println(documentURL);

	}

}
