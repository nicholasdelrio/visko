package edu.utep.trustlab.visko.knowledge.ncl;
import edu.utep.trustlab.visko.ontology.operator.writer.TransformerWriter;

public class NCLTransformers {
	public static void create() {
		String name;
		TransformerWriter wtr = new TransformerWriter("netCDFContourer");
		wtr.setOutputFormat("http://rio.cs.utep.edu/ciserver/ciprojects/formats/POSTSCRIPT.owl#POSTSCRIPT");
		wtr.addInputFormat("http://rio.cs.utep.edu/ciserver/ciprojects/pmlp/NETCDF.owl#NETCDF");
		name = "Contour Map Generation";
		wtr.setLabel(name);
		wtr.setName(name);
		wtr.setMappedToView("https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/contour-lines.owl#contour-lines");
		wtr.saveDocument();

		TransformerWriter wtr1 = new TransformerWriter("netCDFRasterer");
		wtr1.setOutputFormat("http://rio.cs.utep.edu/ciserver/ciprojects/formats/POSTSCRIPT.owl#POSTSCRIPT");
		wtr1.addInputFormat("http://rio.cs.utep.edu/ciserver/ciprojects/pmlp/NETCDF.owl#NETCDF");
		name = "Raster Map Generation";
		wtr1.setLabel(name);
		wtr1.setName(name);
		wtr1.setMappedToView("https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/raster.owl#raster");
		wtr1.saveDocument();

		TransformerWriter wtr2 = new TransformerWriter("netCDFTimeSeriesPlotter");
		wtr2.setOutputFormat("http://rio.cs.utep.edu/ciserver/ciprojects/formats/POSTSCRIPT.owl#POSTSCRIPT");
		wtr2.addInputFormat("http://rio.cs.utep.edu/ciserver/ciprojects/pmlp/NETCDF.owl#NETCDF");
		name = "Time Series Plot Generator";
		wtr2.setLabel(name);
		wtr2.setName(name);
		wtr2.setMappedToView("https://raw.github.com/nicholasdelrio/visko/master/visko-rdf/XYPlot.owl#XYPlot");
		wtr2.saveDocument();
	}
}