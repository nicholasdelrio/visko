package edu.utep.trustlab.visko.knowledge.universal;


import edu.utep.trustlab.publish.Server;
import edu.utep.trustlab.visko.knowledge.NickCIServer;
import edu.utep.trustlab.visko.ontology.operator.writer.ViewerSetWriter;

public class ViewerSets {

	public static void main(String[] args) {
		Server.setServer(NickCIServer.getServer());	
		ViewerSetWriter wtr = new ViewerSetWriter("probeit1");
		wtr.setLabel("Probe-It!");
		String documentURL = wtr.saveDocument();
		System.out.println(documentURL);

		ViewerSetWriter wtr1 = new ViewerSetWriter("mozilla-firefox1");
		wtr1.setLabel("Mozilla Firefox");
		documentURL = wtr1.saveDocument();
		System.out.println(documentURL);

		ViewerSetWriter wtr2 = new ViewerSetWriter("internet-explorer1");
		wtr2.setLabel("Microsoft Internet Explorer");
		documentURL = wtr2.saveDocument();
		System.out.println(documentURL);

		ViewerSetWriter wtr3 = new ViewerSetWriter("diva-graphics1");
		wtr3.setLabel("Diva Graphics Java Package");
		documentURL = wtr3.saveDocument();
		System.out.println(documentURL);
	}
}
