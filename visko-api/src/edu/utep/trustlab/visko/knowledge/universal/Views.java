package edu.utep.trustlab.visko.knowledge.universal;


import edu.utep.trustlab.publish.Server;
import edu.utep.trustlab.visko.ontology.view.writer.*;
import edu.utep.trustlab.visko.knowledge.NickCIServer;

public class Views {

	public static void main(String[] args) {
		Server.setServer(NickCIServer.getServer());
		String documentURL;

		/*
		 * ViewContourWriter wtr = new ViewContourWriter("contour-lines1");
		 * wtr.setLabel("Contour Lines (Isolines)"); documentURL =
		 * wtr.dumpViskoDocumentOnCIServer(); System.out.println(documentURL);
		 * 
		 * ViewPointsWriter wtr1 = new ViewPointsWriter("plot-2D1");
		 * wtr1.setLabel("2D Dimensional Plot"); documentURL =
		 * wtr1.dumpViskoDocumentOnCIServer(); System.out.println(documentURL);
		 * 
		 * ViewVolumeWriter wtr3 = new ViewVolumeWriter("volume1");
		 * wtr3.setLabel("Volume"); documentURL =
		 * wtr3.dumpViskoDocumentOnCIServer(); System.out.println(documentURL);
		 * 
		 * ViewSurfaceWriter wtr4 = new ViewSurfaceWriter("iso-surfaces1");
		 * wtr4.setLabel("Isosurfaces"); documentURL =
		 * wtr4.dumpViskoDocumentOnCIServer(); System.out.println(documentURL);
		 * 
		 * ViewRasterWriter wtr5 = new ViewRasterWriter("raster");
		 * wtr5.setLabel("Raster"); documentURL =
		 * wtr5.dumpViskoDocumentOnCIServer(); System.out.println(documentURL);
		 */

		ViewGraphWriter wtr6 = new ViewGraphWriter("XYPlot");
		wtr6.setLabel("1D Plot of Variables");
		documentURL = wtr6.saveDocument();
		System.out.println(documentURL);
		System.out.println(wtr6.getURI());
	}
}