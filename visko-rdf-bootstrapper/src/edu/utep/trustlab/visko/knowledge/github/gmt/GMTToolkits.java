package edu.utep.trustlab.visko.knowledge.github.gmt;
import edu.utep.trustlab.visko.ontology.service.writer.ToolkitWriter;

public class GMTToolkits {
	public static void create() {
		String documentURL;
		ToolkitWriter wtr1 = new ToolkitWriter("gmt");
		wtr1.setLabel("Generic Mapping Tools");
		System.out.println(wtr1.toRDFString());
		documentURL = wtr1.saveDocument();
		System.out.println(documentURL);
	}
}
