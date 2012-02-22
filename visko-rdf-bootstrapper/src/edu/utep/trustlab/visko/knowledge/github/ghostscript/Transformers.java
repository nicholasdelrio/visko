package edu.utep.trustlab.visko.knowledge.github.ghostscript;

import edu.utep.trustlab.repository.Repository;
import edu.utep.trustlab.visko.knowledge.NickConfigurations;
import edu.utep.trustlab.visko.ontology.operator.writer.TransformerWriter;

public class Transformers {
	public static void main(String[] args) {
		Repository.setRepository(NickConfigurations.getLocalFileSystem());
		
		String name;
		TransformerWriter wtr3 = new TransformerWriter("ps-to-pdf");
		wtr3.addInputFormat("http://rio.cs.utep.edu/ciserver/ciprojects/formats/POSTSCRIPT.owl#POSTSCRIPT");
		wtr3.setOutputFormat("http://rio.cs.utep.edu/ciserver/ciprojects/formats/PDF.owl#PDF");
		name = "Convert Adobe PS to PDF";
		wtr3.setLabel(name);
		wtr3.setName(name);
		wtr3.saveDocument();

		TransformerWriter wtr4 = new TransformerWriter("ps-to-png");
		wtr4.addInputFormat("http://rio.cs.utep.edu/ciserver/ciprojects/formats/POSTSCRIPT.owl#POSTSCRIPT");
		wtr4.setOutputFormat("http://rio.cs.utep.edu/ciserver/ciprojects/formats/PNG.owl#PNG");
		name = "Convert Adobe PS to PNG Image";
		wtr4.setLabel(name);
		wtr4.setName(name);
		wtr4.saveDocument();

		TransformerWriter wtr5 = new TransformerWriter("pdf-to-png");
		wtr5.addInputFormat("http://rio.cs.utep.edu/ciserver/ciprojects/formats/PDF.owl#PDF");
		wtr5.setOutputFormat("http://rio.cs.utep.edu/ciserver/ciprojects/formats/PNG.owl#PNG");
		name = "Convert Adobe PDF to PNG Image";
		wtr5.setLabel(name);
		wtr5.setName(name);
		wtr5.saveDocument();
	}
}
