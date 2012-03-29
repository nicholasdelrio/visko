/*
Copyright (c) 2012, University of Texas at El Paso
All rights reserved.
Redistribution and use in source and binary forms, with or without modification, are permitted
provided that the following conditions are met:

	-Redistributions of source code must retain the above copyright notice, this list of conditions
	 and the following disclaimer.
	-Redistributions in binary form must reproduce the above copyright notice, this list of conditions
	 and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.*/


package edu.utep.trustlab.visko.knowledge.universal;

import edu.utep.trustlab.visko.ontology.view.writer.*;

public class Views {
	
	

	public static void create() {
		String documentURL;

		ViewContourWriter wtr = new ViewContourWriter("contour-lines");
		wtr.setLabel("Contour Lines (Isolines)");
		documentURL = wtr.saveDocument();
		System.out.println(documentURL);

		ViewPointsWriter wtr1 = new ViewPointsWriter("plot-2D");
		wtr1.setLabel("2D Dimensional Plot");
		documentURL = wtr1.saveDocument();
		System.out.println(documentURL);

		ViewVolumeWriter wtr3 = new ViewVolumeWriter("volume");
		wtr3.setLabel("Volume");
		documentURL = wtr3.saveDocument();
		System.out.println(documentURL);

		ViewSurfaceWriter wtr4 = new ViewSurfaceWriter("iso-surfaces");
		wtr4.setLabel("Isosurfaces");
		documentURL = wtr4.saveDocument();
		System.out.println(documentURL);

		ViewRasterWriter wtr5 = new ViewRasterWriter("raster");
		wtr5.setLabel("Raster");
		documentURL = wtr5.saveDocument();
		System.out.println(documentURL);

		ViewGraphWriter wtr6 = new ViewGraphWriter("XYPlot");
		wtr6.setLabel("1D Plot of Variables");
		documentURL = wtr6.saveDocument();
		System.out.println(documentURL);
	}
}
