package edu.utep.trustlab.visko.knowledge.latex;


import edu.utep.trustlab.repository.Repository;
import edu.utep.trustlab.visko.knowledge.NickCIServer;
import edu.utep.trustlab.visko.ontology.service.writer.ToolkitWriter;

public class Toolkits {
	public static void main(String[] args) {
		Repository.setServer(NickCIServer.getServer());

		
		String documentURL;
		ToolkitWriter wtr3 = new ToolkitWriter("latex-suite");
		wtr3.setLabel("Latex Suite");
		documentURL = wtr3.saveDocument();
		System.out.println(documentURL);
	}
}
