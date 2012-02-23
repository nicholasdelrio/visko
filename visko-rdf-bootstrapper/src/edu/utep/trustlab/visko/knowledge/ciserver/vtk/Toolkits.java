package edu.utep.trustlab.visko.knowledge.ciserver.vtk;


import edu.utep.trustlab.repository.NickConfigurations;
import edu.utep.trustlab.repository.Repository;
import edu.utep.trustlab.visko.ontology.service.writer.ToolkitWriter;

public class Toolkits {
	public static void main(String[] args) {
		Repository.setRepository(NickConfigurations.getCIServer());
		ToolkitWriter wtr = new ToolkitWriter("vtk1");
		wtr.setLabel("Visualization Toolkit");
		String documentURL = wtr.saveDocument();
		System.out.println(documentURL);
	}
}
