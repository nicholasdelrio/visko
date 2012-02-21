package edu.utep.trustlab.visko.knowledge.ncl;


import edu.utep.trustlab.repository.Repository;
import edu.utep.trustlab.visko.knowledge.NickConfigurations;
import edu.utep.trustlab.visko.ontology.service.writer.ToolkitWriter;

public class Toolkits {
	public static void main(String[] args) {
		
		Repository.setRepository(NickConfigurations.getCIServer());
		
		ToolkitWriter wtr = new ToolkitWriter("ncl");
		wtr.setLabel("NCAR Command Language");
		String documentURL = wtr.saveDocument();
		System.out.println(documentURL);
	}
}
