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


package edu.utep.trustlab.visko.knowledge.ncl;
import edu.utep.trustlab.visko.knowledge.universal.Views;
import edu.utep.trustlab.visko.ontology.operator.writer.TransformerWriter;

public class NCLTransformers {

	public static String netCDFContourer;
	public static String netCDFRasterer;
	public static String netCDFTimeSeriesPlotter;
	
	public static void create() {
		String name;
		TransformerWriter wtr = new TransformerWriter("netCDFContourer", true);
		wtr.setOutputFormat("https://raw.github.com/nicholasdelrio/visko/master/rdf/formats/POSTSCRIPT.owl#POSTSCRIPT");
		wtr.addInputFormat("https://raw.github.com/nicholasdelrio/visko/master/rdf/formats/NETCDF.owl#NETCDF");
		name = "Contour Map Generation";
		wtr.setLabel(name);
		wtr.setName(name);
		wtr.setMappedToView(Views.contourLines);
		System.out.println(wtr.saveDocument());
		netCDFContourer = wtr.getURI();

		TransformerWriter wtr1 = new TransformerWriter("netCDFRasterer", true);
		wtr1.setOutputFormat("https://raw.github.com/nicholasdelrio/visko/master/rdf/formats/POSTSCRIPT.owl#POSTSCRIPT");
		wtr1.addInputFormat("https://raw.github.com/nicholasdelrio/visko/master/rdf/formats/NETCDF.owl#NETCDF");
		name = "Raster Map Generation";
		wtr1.setLabel(name);
		wtr1.setName(name);
		wtr1.setMappedToView(Views.raster);
		System.out.println(wtr1.saveDocument());
		netCDFRasterer = wtr1.getURI();
		
		TransformerWriter wtr2 = new TransformerWriter("netCDFTimeSeriesPlotter", true);
		wtr2.setOutputFormat("https://raw.github.com/nicholasdelrio/visko/master/rdf/formats/POSTSCRIPT.owl#POSTSCRIPT");
		wtr2.addInputFormat("https://raw.github.com/nicholasdelrio/visko/master/rdf/formats/NETCDF.owl#NETCDF");
		name = "Time Series Plot Generator";
		wtr2.setLabel(name);
		wtr2.setName(name);
		wtr2.setMappedToView(Views.xyPlot);
		System.out.println(wtr2.saveDocument());
		netCDFTimeSeriesPlotter = wtr2.getURI();
	}
}
