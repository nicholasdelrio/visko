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


package edu.utep.trustlab.visko.knowledge.gmt;

import edu.utep.trustlab.repository.Repository;
import edu.utep.trustlab.visko.ontology.operator.writer.TransformerWriter;

public class GMTTransformers {
	public static void create() {

		String name;
		String baseURL = Repository.getRepository().getBaseURL();
		TransformerWriter wtr = new TransformerWriter("contourer");
		wtr.addInputFormat("http://rio.cs.utep.edu/ciserver/ciprojects/formats/ESRIGRID.owl#ESRIGRID");
		wtr.setOutputFormat("http://rio.cs.utep.edu/ciserver/ciprojects/formats/POSTSCRIPT.owl#POSTSCRIPT");
		name = "contour";
		wtr.setLabel(name);
		wtr.setName(name);
		wtr.setMappedToView(baseURL + "contour-lines.owl#contour-lines");
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
		wtr2.setMappedToView(baseURL + "plot-2D.owl#plot-2D");
		wtr2.saveDocument();
		
		TransformerWriter wtr3 = new TransformerWriter("rasterer");
		wtr3.addInputFormat("http://rio.cs.utep.edu/ciserver/ciprojects/formats/ESRIGRID.owl#ESRIGRID");
		wtr3.setOutputFormat("http://rio.cs.utep.edu/ciserver/ciprojects/formats/POSTSCRIPT.owl#POSTSCRIPT");
		name = "Raster Map Generator";
		wtr3.setLabel(name);
		wtr3.setName(name);
		wtr3.setMappedToView(baseURL + "raster.owl#raster");
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
