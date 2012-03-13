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


package edu.utep.trustlab.visko.knowledge.ghostscript;


import edu.utep.trustlab.visko.ontology.operator.writer.TransformerWriter;

public class GSTransformers {
	public static void create() {
		
		String name;
		TransformerWriter wtr3 = new TransformerWriter("ps-to-pdf");
		wtr3.addInputFormat("http://rio.cs.utep.edu/ciserver/ciprojects/formats/POSTSCRIPT.owl#POSTSCRIPT");
		wtr3.setOutputFormat("http://rio.cs.utep.edu/ciserver/ciprojects/formats/PDF.owl#PDF");
		name = "Convert Adobe PS to PDF";
		wtr3.setLabel(name);
		wtr3.setName(name);
		wtr3.saveDocument();

		TransformerWriter wtr4 = new TransformerWriter("ps-to-png");
		wtr4.addInputFormat("http://rio.cs.utep.edu/ciserver/ciprojects/formats/POSTSCRIPT.owl#POSTSCRIPT");
		wtr4.setOutputFormat("http://rio.cs.utep.edu/ciserver/ciprojects/formats/PNG.owl#PNG");
		name = "Convert Adobe PS to PNG Image";
		wtr4.setLabel(name);
		wtr4.setName(name);
		wtr4.saveDocument();

		TransformerWriter wtr5 = new TransformerWriter("pdf-to-png");
		wtr5.addInputFormat("http://rio.cs.utep.edu/ciserver/ciprojects/formats/PDF.owl#PDF");
		wtr5.setOutputFormat("http://rio.cs.utep.edu/ciserver/ciprojects/formats/PNG.owl#PNG");
		name = "Convert Adobe PDF to PNG Image";
		wtr5.setLabel(name);
		wtr5.setName(name);
		wtr5.saveDocument();
	}
}
