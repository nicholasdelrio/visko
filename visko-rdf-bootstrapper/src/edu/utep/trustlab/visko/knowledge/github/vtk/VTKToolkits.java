package edu.utep.trustlab.visko.knowledge.github.vtk;

import edu.utep.trustlab.visko.ontology.service.writer.ToolkitWriter;

public class VTKToolkits {
	public static void create() {
		ToolkitWriter wtr = new ToolkitWriter("vtk");
		wtr.setLabel("Visualization Toolkit");
		String documentURL = wtr.saveDocument();
		System.out.println(documentURL);
	}
}
