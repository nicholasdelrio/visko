package edu.utep.trustlab.visko.knowledge.ciserver.ncl;

import edu.utep.trustlab.repository.Repository;
import edu.utep.trustlab.visko.knowledge.NickConfigurations;
import edu.utep.trustlab.visko.ontology.operator.writer.TransformerWriter;

public class Transformers {
	public static void main(String[] args) {
		Repository.setRepository(NickConfigurations.getCIServer());

		String name;
		TransformerWriter wtr = new TransformerWriter("contour2");
		wtr.setOutputFormat("http://rio.cs.utep.edu/ciserver/ciprojects/formats/POSTSCRIPT.owl#POSTSCRIPT");
		wtr.addInputFormat("http://rio.cs.utep.edu/ciserver/ciprojects/pmlp/NETCDF.owl#NETCDF");
		name = "Contour Map Generation";
		wtr.setLabel(name);
		wtr.setName(name);
		wtr.setMappedToView("http://rio.cs.utep.edu/ciserver/ciprojects/visko/contour-lines1.owl#contour-lines1");
		wtr.saveDocument();

		TransformerWriter wtr1 = new TransformerWriter("rasterMap");
		wtr1.setOutputFormat("http://rio.cs.utep.edu/ciserver/ciprojects/formats/POSTSCRIPT.owl#POSTSCRIPT");
		wtr1.addInputFormat("http://rio.cs.utep.edu/ciserver/ciprojects/pmlp/NETCDF.owl#NETCDF");
		name = "Raster Map Generation";
		wtr1.setLabel(name);
		wtr1.setName(name);
		wtr1.setMappedToView("http://rio.cs.utep.edu/ciserver/ciprojects/visko/raster.owl#raster");
		wtr1.saveDocument();

		TransformerWriter wtr2 = new TransformerWriter("timeSeriesPlot");
		wtr2.setOutputFormat("http://rio.cs.utep.edu/ciserver/ciprojects/formats/POSTSCRIPT.owl#POSTSCRIPT");
		wtr2.addInputFormat("http://rio.cs.utep.edu/ciserver/ciprojects/pmlp/NETCDF.owl#NETCDF");
		name = "Time Series Plot Generator";
		wtr2.setLabel(name);
		wtr2.setName(name);
		wtr2.setMappedToView("http://rio.cs.utep.edu/ciserver/ciprojects/visko/XYPlot.owl#XYPlot");
		wtr2.saveDocument();
	}
}