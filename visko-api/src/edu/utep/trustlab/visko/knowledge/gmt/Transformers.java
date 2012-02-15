package edu.utep.trustlab.visko.knowledge.gmt;


import edu.utep.trustlab.publish.Repository;
import edu.utep.trustlab.visko.knowledge.NickCIServer;
import edu.utep.trustlab.visko.ontology.operator.writer.TransformerWriter;

public class Transformers {
	public static void main(String[] args) {
		Repository.setServer(NickCIServer.getServer());
		String name;

		/*
		 * TransformerWriter wtr = new TransformerWriter("contour1");
		 * wtr.addInputFormat(
		 * "http://rio.cs.utep.edu/ciserver/ciprojects/formats/ESRIGRID.owl#ESRIGRID"
		 * ); wtr.setOutputFormat(
		 * "http://rio.cs.utep.edu/ciserver/ciprojects/formats/POSTSCRIPT.owl#POSTSCRIPT"
		 * ); name = "contour"; wtr.setLabel(name); wtr.setName(name);
		 * wtr.setMappedToView(
		 * "http://rio.cs.utep.edu/ciserver/ciprojects/visko/contour-lines1.owl#contour-lines1"
		 * ); wtr.dumpViskoDocumentOnCIServer();
		 * 
		 * System.out.println(wtr); TransformerWriter wtr1 = new
		 * TransformerWriter("grid-by-surface1"); wtr1.addInputFormat(
		 * "http://rio.cs.utep.edu/ciserver/ciprojects/formats/TABDELIMITEDDATASET.owl#TABDELIMITEDDATASET"
		 * ); wtr1.setOutputFormat(
		 * "http://rio.cs.utep.edu/ciserver/ciprojects/formats/ESRIGRID.owl#ESRIGRID"
		 * ); name = "Grid By Surface Tension"; wtr1.setLabel(name);
		 * wtr1.setName(name); wtr1.dumpViskoDocumentOnCIServer();
		 * 
		 * TransformerWriter wtr2 = new TransformerWriter("plotter1");
		 * wtr2.addInputFormat(
		 * "http://rio.cs.utep.edu/ciserver/ciprojects/formats/TABDELIMITEDDATASET.owl#TABDELIMITEDDATASET"
		 * ); wtr2.setOutputFormat(
		 * "http://rio.cs.utep.edu/ciserver/ciprojects/formats/POSTSCRIPT.owl#POSTSCRIPT"
		 * ); name = "2D plotter"; wtr2.setLabel(name); wtr2.setName(name);
		 * wtr2.setMappedToView(
		 * "http://rio.cs.utep.edu/ciserver/ciprojects/visko/plot-2D1.owl#plot-2D1"
		 * ); wtr2.dumpViskoDocumentOnCIServer();
		 * 
		 * TransformerWriter wtr3 = new
		 * TransformerWriter("grid-to-colored-image1"); wtr3.addInputFormat(
		 * "http://rio.cs.utep.edu/ciserver/ciprojects/formats/ESRIGRID.owl#ESRIGRID"
		 * ); wtr3.setOutputFormat(
		 * "http://rio.cs.utep.edu/ciserver/ciprojects/formats/POSTSCRIPT.owl#POSTSCRIPT"
		 * ); name = "Color Coded Gridded Dataset"; wtr3.setLabel(name);
		 * wtr3.setName(name); wtr3.setMappedToView(
		 * "http://rio.cs.utep.edu/ciserver/ciprojects/visko/raster.owl#raster"
		 * ); wtr3.dumpViskoDocumentOnCIServer();
		 * 
		 * TransformerWriter wtr4 = new
		 * TransformerWriter("grid-by-near-neighbor1"); wtr4.addInputFormat(
		 * "http://rio.cs.utep.edu/ciserver/ciprojects/formats/TABDELIMITEDDATASET.owl#TABDELIMITEDDATASET"
		 * ); wtr4.setOutputFormat(
		 * "http://rio.cs.utep.edu/ciserver/ciprojects/formats/ESRIGRID.owl#ESRIGRID"
		 * ); name = "Grid By Nearneighbor"; wtr4.setLabel(name);
		 * wtr4.setName(name); wtr4.dumpViskoDocumentOnCIServer();
		 */

		TransformerWriter wtr5 = new TransformerWriter("csv-to-tabular-ascii");
		wtr5.setOutputFormat("http://rio.cs.utep.edu/ciserver/ciprojects/formats/TABDELIMITEDDATASET.owl#TABDELIMITEDDATASET");
		wtr5.addInputFormat("http://rio.cs.utep.edu/ciserver/ciprojects/formats/CSV.owl#CSV");
		name = "Comma Separated Values to Tabular ASCII Format";
		wtr5.setLabel(name);
		wtr5.setName(name);
		wtr5.saveDocument();
	}
}
