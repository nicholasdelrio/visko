package trustlab.visko.knowledge.vtk;


import trustlab.server.Server;
import trustlab.visko.knowledge.NickCIServer;
import trustlab.visko.ontology.service.writer.ToolkitWriter;

public class Toolkits {
	public static void main(String[] args) {
		Server.setServer(NickCIServer.getServer());
		ToolkitWriter wtr = new ToolkitWriter("vtk1");
		wtr.setLabel("Visualization Toolkit");
		String documentURL = wtr.saveDocument();
		System.out.println(documentURL);
	}
}
