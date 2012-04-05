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
	public static String ps2pdf;
	public static String ps2png;
	public static String pdf2png;
	
	public static void create() {
		
		String name;
		TransformerWriter wtr = new TransformerWriter("ps-to-pdf", false);
		wtr.addInputFormat("https://raw.github.com/nicholasdelrio/visko/master/rdf/formats/POSTSCRIPT.owl#POSTSCRIPT");
		wtr.setOutputFormat("https://raw.github.com/nicholasdelrio/visko/master/rdf/formats/PDF.owl#PDF");
		name = "Convert Adobe PS to PDF";
		wtr.setLabel(name);
		wtr.setName(name);
		System.out.println(wtr.saveDocument());
		ps2pdf = wtr.getURI();

		TransformerWriter wtr1 = new TransformerWriter("ps-to-png", false);
		wtr1.addInputFormat("https://raw.github.com/nicholasdelrio/visko/master/rdf/formats/POSTSCRIPT.owl#POSTSCRIPT");
		wtr1.setOutputFormat("https://raw.github.com/nicholasdelrio/visko/master/rdf/formats/PNG.owl#PNG");
		name = "Convert Adobe PS to PNG Image";
		wtr1.setLabel(name);
		wtr1.setName(name);
		System.out.println(wtr1.saveDocument());
		ps2png = wtr1.getURI();

		TransformerWriter wtr2 = new TransformerWriter("pdf-to-png", false);
		wtr2.addInputFormat("https://raw.github.com/nicholasdelrio/visko/master/rdf/formats/PDF.owl#PDF");
		wtr2.setOutputFormat("https://raw.github.com/nicholasdelrio/visko/master/rdf/formats/PNG.owl#PNG");
		name = "Convert Adobe PDF to PNG Image";
		wtr2.setLabel(name);
		wtr2.setName(name);
		System.out.println(wtr2.saveDocument());
		pdf2png = wtr2.getURI();
	}
}
