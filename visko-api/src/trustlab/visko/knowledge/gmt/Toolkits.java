package trustlab.visko.knowledge.gmt;

import trustlab.publish.Server;
import trustlab.visko.knowledge.NickCIServer;
import trustlab.visko.ontology.service.writer.ToolkitWriter;

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
