package edu.utep.trustlab.visko.knowledge.github.ncl;
import edu.utep.trustlab.visko.ontology.service.writer.ToolkitWriter;

public class NCLToolkits {
	public static void create() {
		ToolkitWriter wtr = new ToolkitWriter("ncl");
		wtr.setLabel("NCAR Command Language");
		String documentURL = wtr.saveDocument();
		System.out.println(documentURL);
	}
}
