package edu.utep.trustlab.visko.knowledge.github.gmt;

import edu.utep.trustlab.visko.ontology.operator.writer.TransformerWriter;

public class GMTTransformers {
	public static void create() {

		String name;

		TransformerWriter wtr = new TransformerWriter("contourer");
		wtr.addInputFormat("http://rio.cs.utep.edu/ciserver/ciprojects/formats/ESRIGRID.owl#ESRIGRID");
		wtr.setOutputFormat("http://rio.cs.utep.edu/ciserver/ciprojects/formats/POSTSCRIPT.owl#POSTSCRIPT");
		name = "contour";
		wtr.setLabel(name);
		wtr.setName(name);
		wtr.setMappedToView("https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/contour-lines.owl#contour-lines");
		wtr.saveDocument();

		System.out.println(wtr);
		TransformerWriter wtr1 = new TransformerWriter("gridder");
		wtr1.addInputFormat("http://rio.cs.utep.edu/ciserver/ciprojects/pmlp/SPACEDELIMITEDTABULARASCII.owl#SPACEDELIMITEDTABULARASCII");
		wtr1.setOutputFormat("http://rio.cs.utep.edu/ciserver/ciprojects/formats/ESRIGRID.owl#ESRIGRID");
		name = "Data Gridder";
		wtr1.setLabel(name);
		wtr1.setName(name);
		wtr1.saveDocument();
		
		TransformerWriter wtr2 = new TransformerWriter("plotter");
		wtr2.addInputFormat("http://rio.cs.utep.edu/ciserver/ciprojects/pmlp/SPACEDELIMITEDTABULARASCII.owl#SPACEDELIMITEDTABULARASCII");
		wtr2.setOutputFormat("http://rio.cs.utep.edu/ciserver/ciprojects/formats/POSTSCRIPT.owl#POSTSCRIPT");
		name = "2D plotter";
		wtr2.setLabel(name);
		wtr2.setName(name);
		wtr2.setMappedToView("https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/plot-2D.owl#plot-2D");
		wtr2.saveDocument();
		
		TransformerWriter wtr3 = new TransformerWriter("rasterer");
		wtr3.addInputFormat("http://rio.cs.utep.edu/ciserver/ciprojects/formats/ESRIGRID.owl#ESRIGRID");
		wtr3.setOutputFormat("http://rio.cs.utep.edu/ciserver/ciprojects/formats/POSTSCRIPT.owl#POSTSCRIPT");
		name = "Raster Map Generator";
		wtr3.setLabel(name);
		wtr3.setName(name);
		wtr3.setMappedToView("https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/raster.owl#raster");
		wtr3.saveDocument();

		TransformerWriter wtr5 = new TransformerWriter("csv-to-tabular-ascii");
		wtr5.setOutputFormat("http://rio.cs.utep.edu/ciserver/ciprojects/pmlp/SPACEDELIMITEDTABULARASCII.owl#SPACEDELIMITEDTABULARASCII");
		wtr5.addInputFormat("http://rio.cs.utep.edu/ciserver/ciprojects/formats/CSV.owl#CSV");
		name = "Comma Separated Values to Tabular ASCII Format";
		wtr5.setLabel(name);
		wtr5.setName(name);
		wtr5.saveDocument();
	}
}
