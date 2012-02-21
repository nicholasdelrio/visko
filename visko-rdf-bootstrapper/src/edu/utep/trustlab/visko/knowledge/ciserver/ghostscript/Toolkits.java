package edu.utep.trustlab.visko.knowledge.ciserver.ghostscript;

import edu.utep.trustlab.repository.Repository;
import edu.utep.trustlab.visko.knowledge.NickConfigurations;
import edu.utep.trustlab.visko.ontology.service.writer.ToolkitWriter;

public class Toolkits {
	public static void main(String[] args) {
	
		String documentURL;

		Repository.setRepository(NickConfigurations.getCIServer());
		
		ToolkitWriter wtr1 = new ToolkitWriter("ghostscript1");
		wtr1.setLabel("Ghostscript");
		documentURL = wtr1.saveDocument();
		System.out.println(documentURL);
	}
}
