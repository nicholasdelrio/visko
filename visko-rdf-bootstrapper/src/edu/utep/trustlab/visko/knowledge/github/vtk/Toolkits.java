package edu.utep.trustlab.visko.knowledge.github.vtk;


import edu.utep.trustlab.repository.Repository;
import edu.utep.trustlab.visko.knowledge.NickConfigurations;
import edu.utep.trustlab.visko.ontology.service.writer.ToolkitWriter;

public class Toolkits {
	public static void main(String[] args) {
		
		Repository.setRepository(NickConfigurations.getLocalFileSystem());
		ToolkitWriter wtr = new ToolkitWriter("vtk");
		wtr.setLabel("Visualization Toolkit");
		String documentURL = wtr.saveDocument();
		System.out.println(documentURL);
	}
}
