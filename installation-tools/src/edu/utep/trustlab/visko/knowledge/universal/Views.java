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
	
	public static String contourLines;
	public static String plot2D;
	public static String volume;
	public static String isosurfaces;
	public static String raster;
	public static String xyPlot;

	public static void create() {

		try{
			
		ViewContourWriter wtr = new ViewContourWriter("contour-lines");
		wtr.setLabel("Contour Lines (Isolines)");
		System.out.println(wtr.saveDocument());
		contourLines = wtr.getURI();
		
		Thread.sleep(500);
		
		ViewPointsWriter wtr1 = new ViewPointsWriter("plot-2D");
		wtr1.setLabel("2D Dimensional Plot");
		System.out.println(wtr1.saveDocument());
		plot2D = wtr.getURI();
		
		Thread.sleep(500);
		
		ViewVolumeWriter wtr2 = new ViewVolumeWriter("volume");
		wtr2.setLabel("Volume");
		System.out.println(wtr2.saveDocument());
		volume = wtr2.getURI();

		Thread.sleep(500);
		
		ViewSurfaceWriter wtr3 = new ViewSurfaceWriter("iso-surfaces");
		wtr3.setLabel("Isosurfaces");
		System.out.println(wtr3.saveDocument());
		isosurfaces = wtr3.getURI();
		
		Thread.sleep(500);
		
		ViewRasterWriter wtr4 = new ViewRasterWriter("raster");
		wtr4.setLabel("Raster");
		System.out.println(wtr4.saveDocument());
		raster = wtr4.getURI();

		Thread.sleep(500);
		
		ViewGraphWriter wtr6 = new ViewGraphWriter("XYPlot");
		wtr6.setLabel("1D Plot of Variables");
		System.out.println(wtr6.saveDocument());
		xyPlot = wtr6.getURI();
		
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
