package edu.utep.trustlab.visko.knowledge.github.universal;


import edu.utep.trustlab.repository.NickConfigurations;
import edu.utep.trustlab.repository.Repository;
import edu.utep.trustlab.visko.ontology.operator.writer.ViewerSetWriter;

public class ViewerSets {

	public static void main(String[] args) {
		Repository.setRepository(NickConfigurations.getLocalFileSystem());	
		
		ViewerSetWriter wtr = new ViewerSetWriter("probeit");
		wtr.setLabel("Probe-It!");
		String documentURL = wtr.saveDocument();
		System.out.println(documentURL);

		ViewerSetWriter wtr1 = new ViewerSetWriter("mozilla-firefox");
		wtr1.setLabel("Mozilla Firefox");
		documentURL = wtr1.saveDocument();
		System.out.println(documentURL);

		ViewerSetWriter wtr2 = new ViewerSetWriter("internet-explorer");
		wtr2.setLabel("Microsoft Internet Explorer");
		documentURL = wtr2.saveDocument();
		System.out.println(documentURL);

		ViewerSetWriter wtr3 = new ViewerSetWriter("diva-graphics");
		wtr3.setLabel("Diva Graphics Java Package");
		documentURL = wtr3.saveDocument();
		System.out.println(documentURL);
	}
}
