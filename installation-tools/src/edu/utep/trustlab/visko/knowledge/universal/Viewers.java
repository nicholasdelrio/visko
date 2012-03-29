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
import edu.utep.trustlab.visko.ontology.operator.writer.ViewerWriter;

public class Viewers {

	public static String imageJ;
	public static String parvis;
	public static String pdfViewer;
	public static String plainTextViewer;
	public static String browserImageViewer;
	public static String divaGraphicsViewer;
	public static String htmlViewer;
	
	public static void create() {
		ViewerWriter wtr = new ViewerWriter("imageJ-viewer");
		wtr.setLabel("ImageJ Viewer");
		wtr.addPartOfSetURI(ViewerSets.probeit);
		wtr.addFormatURI("https://raw.github.com/nicholasdelrio/visko/master/rdf/formats/GIF.owl#GIF");
		wtr.addFormatURI("https://raw.github.com/nicholasdelrio/visko/master/rdf/formats/PNG.owl#PNG");
		wtr.addFormatURI("https://raw.github.com/nicholasdelrio/visko/master/rdf/formats/JPEG.owl#JPEG");
		wtr.addFormatURI("https://raw.github.com/nicholasdelrio/visko/master/rdf/formats/DICOM.owl#DICOM");
		wtr.addFormatURI("https://raw.github.com/nicholasdelrio/visko/master/rdf/formats/TIFF.owl#TIFF");
		wtr.addFormatURI("https://raw.github.com/nicholasdelrio/visko/master/rdf/formats/FITS.owl#FITS");
		wtr.addFormatURI("https://raw.github.com/nicholasdelrio/visko/master/rdf/formats/RAW.owl#RAW");
		wtr.setViewerComment("ImageJ is a public domain Java image processing program inspired by NIH Image.");
		System.out.println(wtr.saveDocument());
		imageJ = wtr.getURI();

		ViewerWriter wtr1 = new ViewerWriter("parvis-viewer");
		wtr1.addPartOfSetURI(ViewerSets.probeit);
		wtr1.setLabel("Parvis Parallel Coordinates Viewer");
		wtr1.addFormatURI("https://raw.github.com/nicholasdelrio/visko/master/rdf/formats/VNDWTSTF.owl#VNDWTSTF");
		wtr1.setViewerComment("Parvis is a tool for parallel coordinates (PC) visualisation of multidimensional data sets, as first described in [Inselberg 1981].");
		System.out.println(wtr1.saveDocument());
		parvis = wtr1.getURI();

		ViewerWriter wtr2 = new ViewerWriter("pdf-viewer");
		wtr2.addPartOfSetURI(ViewerSets.probeit);
		wtr2.addPartOfSetURI(ViewerSets.firefox);
		wtr2.addPartOfSetURI(ViewerSets.internetExplorer);
		wtr2.addFormatURI("https://raw.github.com/nicholasdelrio/visko/master/rdf/formats/PDF.owl#PDF");
		wtr2.setLabel("Adobe Portable Document Format (PDF) Viewer");
		wtr2.setViewerComment("Renders PDF document and allows for zooming.");
		System.out.println(wtr2.saveDocument());
		pdfViewer = wtr2.getURI();
		
		ViewerWriter wtr3 = new ViewerWriter("plain-text-viewer");
		wtr3.addPartOfSetURI(ViewerSets.probeit);
		wtr3.addPartOfSetURI(ViewerSets.firefox);
		wtr3.addPartOfSetURI(ViewerSets.internetExplorer);
		wtr3.addFormatURI("https://raw.github.com/nicholasdelrio/visko/master/rdf/formats/PLAIN.owl#PLAIN");
		wtr3.addFormatURI("https://raw.github.com/nicholasdelrio/visko/master/rdf/formats/PLAINTEXT.owl#PLAINTEXT");
		wtr3.addFormatURI("https://raw.github.com/nicholasdelrio/visko/master/rdf/formats/VNDLATEXZ.owl#VNDLATEXZ");
		wtr3.setLabel("Plain Text Viewer");
		wtr3.setViewerComment("Does nothing really, just extracts the text from the PML conclusion...");
		System.out.println(wtr3.saveDocument());
		plainTextViewer = wtr3.getURI();

		ViewerWriter wtr4 = new ViewerWriter("browser-image-viewer");
		wtr4.addPartOfSetURI(ViewerSets.firefox);
		wtr4.addPartOfSetURI(ViewerSets.internetExplorer);
		wtr4.addFormatURI("https://raw.github.com/nicholasdelrio/visko/master/rdf/formats/GIF.owl#GIF");
		wtr4.addFormatURI("https://raw.github.com/nicholasdelrio/visko/master/rdf/formats/PNG.owl#PNG");
		wtr4.addFormatURI("https://raw.github.com/nicholasdelrio/visko/master/rdf/formats/JPEG.owl#JPEG");
		wtr4.setLabel("Web Browser Image Viewer");
		wtr4.setViewerComment("Views a few standard image formats");
		System.out.println(wtr4.saveDocument());
		browserImageViewer = wtr4.getURI();

		ViewerWriter wtr5 = new ViewerWriter("diva-graphics-viewer");
		wtr5.addPartOfSetURI(ViewerSets.divaGraphics);
		wtr5.addFormatURI("https://raw.github.com/nicholasdelrio/visko/master/rdf/formats/GIF.owl#GIF");
		wtr5.addFormatURI("https://raw.github.com/nicholasdelrio/visko/master/rdf/formats/PNG.owl#PNG");
		wtr5.addFormatURI("https://raw.github.com/nicholasdelrio/visko/master/rdf/formats/JPEG.owl#JPEG");
		wtr5.setLabel("Diva Java Graphics");
		wtr5.setViewerComment("Views a few standard image formats");
		System.out.println(wtr5.saveDocument());
		divaGraphicsViewer = wtr5.getURI();

		ViewerWriter wtr6 = new ViewerWriter("html-viewer");
		wtr6.addPartOfSetURI(ViewerSets.probeit);
		wtr6.addPartOfSetURI(ViewerSets.firefox);
		wtr6.addPartOfSetURI(ViewerSets.internetExplorer);
		wtr6.addFormatURI("https://raw.github.com/nicholasdelrio/visko/master/rdf/formats/HTML.owl#HTML");
		wtr6.setLabel("HyperText Markup Language (HTML) Viewer");
		wtr6.setViewerComment("Renders HTML documents.");
		System.out.println(wtr6.saveDocument());
		htmlViewer = wtr6.getURI();
	}
}
