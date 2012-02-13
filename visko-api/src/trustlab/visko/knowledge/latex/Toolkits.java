package trustlab.visko.knowledge.latex;


import trustlab.publish.Server;
import trustlab.visko.knowledge.NickCIServer;
import trustlab.visko.ontology.service.writer.ToolkitWriter;

public class Toolkits {
	public static void main(String[] args) {
		Server.setServer(NickCIServer.getServer());

		
		String documentURL;
		ToolkitWriter wtr3 = new ToolkitWriter("latex-suite");
		wtr3.setLabel("Latex Suite");
		documentURL = wtr3.saveDocument();
		System.out.println(documentURL);
	}
}
