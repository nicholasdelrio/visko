package edu.utep.trustlab.visko.knowledge.gmt;

import edu.utep.trustlab.publish.Server;
import edu.utep.trustlab.visko.knowledge.NickCIServer;
import edu.utep.trustlab.visko.ontology.service.writer.ToolkitWriter;

public class Toolkits {
	public static void main(String[] args) {

		Server.setServer(NickCIServer.getServer());

		String documentURL;
		ToolkitWriter wtr1 = new ToolkitWriter("gmt1");
		wtr1.setLabel("Generic Mapping Tools");
		System.out.println(wtr1.toRDFString());
		documentURL = wtr1.saveDocument();
		System.out.println(documentURL);
	}
}
