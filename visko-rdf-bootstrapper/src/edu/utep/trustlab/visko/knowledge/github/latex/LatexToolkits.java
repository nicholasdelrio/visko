package edu.utep.trustlab.visko.knowledge.github.latex;

import edu.utep.trustlab.visko.ontology.service.writer.ToolkitWriter;

public class LatexToolkits {
	public static void create() {
		String documentURL;
		ToolkitWriter wtr3 = new ToolkitWriter("latex-suite");
		wtr3.setLabel("Latex Suite");
		documentURL = wtr3.saveDocument();
		System.out.println(documentURL);
	}
}
