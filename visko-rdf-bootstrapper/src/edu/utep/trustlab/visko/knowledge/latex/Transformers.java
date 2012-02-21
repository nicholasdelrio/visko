package edu.utep.trustlab.visko.knowledge.latex;

import edu.utep.trustlab.repository.Repository;
import edu.utep.trustlab.visko.knowledge.NickConfigurations;
import edu.utep.trustlab.visko.ontology.operator.writer.TransformerWriter;

public class Transformers {
	public static void main(String[] args) {
		
		Repository.setRepository(NickConfigurations.getCIServer());
		String name;
		TransformerWriter wtr6 = new TransformerWriter("tex-to-pdf");
		wtr6.addInputFormat("http://rio.cs.utep.edu/ciserver/ciprojects/formats/VNDLATEXZ.owl#VNDLATEXZ");
		wtr6.setOutputFormat("http://rio.cs.utep.edu/ciserver/ciprojects/formats/PDF.owl#PDF");
		name = "PDFLatex Service";
		wtr6.setLabel(name);
		wtr6.setName(name);
		wtr6.saveDocument();
	}
}
