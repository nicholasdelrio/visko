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
