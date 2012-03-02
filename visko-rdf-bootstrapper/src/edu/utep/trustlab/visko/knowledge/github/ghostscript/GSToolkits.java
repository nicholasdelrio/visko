package edu.utep.trustlab.visko.knowledge.github.ghostscript;

import edu.utep.trustlab.visko.ontology.service.writer.ToolkitWriter;

public class GSToolkits {
	public static void create() {
	
		String documentURL;
		ToolkitWriter wtr1 = new ToolkitWriter("ghostscript");
		wtr1.setLabel("Ghostscript");
		documentURL = wtr1.saveDocument();
		System.out.println(documentURL);
	}
}
