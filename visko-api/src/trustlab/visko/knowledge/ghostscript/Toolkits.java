package trustlab.visko.knowledge.ghostscript;

import trustlab.publish.Server;
import trustlab.visko.knowledge.NickCIServer;
import trustlab.visko.ontology.service.writer.ToolkitWriter;

public class Toolkits {
	public static void main(String[] args) {
	
		String documentURL;

		Server.setServer(NickCIServer.getServer());
		
		ToolkitWriter wtr1 = new ToolkitWriter("ghostscript1");
		wtr1.setLabel("Ghostscript");
		documentURL = wtr1.saveDocument();
		System.out.println(documentURL);
	}
}
