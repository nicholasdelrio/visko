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
import edu.utep.trustlab.repository.Repository;
import edu.utep.trustlab.visko.ontology.operator.writer.ViewerWriter;

public class Viewers {

	public static void create() {
		String documentURL;
		String baseURL = Repository.getRepository().getBaseURL();
		ViewerWriter wtr = new ViewerWriter("imageJ-viewer");
		wtr.setLabel("ImageJ Viewer");
		System.out.println(baseURL);
		wtr.addPartOfSetURI(baseURL + "probeit.owl#probeit");
		wtr.addFormatURI("http://rio.cs.utep.edu/ciserver/ciprojects/formats/GIF.owl#GIF");
		wtr.addFormatURI("http://rio.cs.utep.edu/ciserver/ciprojects/formats/PNG.owl#PNG");
		wtr.addFormatURI("http://rio.cs.utep.edu/ciserver/ciprojects/formats/JPEG.owl#JPEG");
		wtr.addFormatURI("http://rio.cs.utep.edu/ciserver/ciprojects/formats/DICOM.owl#DICOM");
		wtr.addFormatURI("http://rio.cs.utep.edu/ciserver/ciprojects/formats/TIFF.owl#TIFF");
		wtr.addFormatURI("http://rio.cs.utep.edu/ciserver/ciprojects/formats/FITS.owl#FITS");
		wtr.addFormatURI("http://rio.cs.utep.edu/ciserver/ciprojects/formats/RAW.owl#RAW");
		wtr.setViewerComment("ImageJ is a public domain Java image processing program inspired by NIH Image.");
		documentURL = wtr.saveDocument();
		System.out.println(documentURL);

		ViewerWriter wtr1 = new ViewerWriter("parvis-viewer");
		wtr1.addPartOfSetURI(baseURL + "probeit.owl#probeit");
		wtr1.setLabel("Parvis Parallel Coordinates Viewer");
		wtr1.addFormatURI("http://rio.cs.utep.edu/ciserver/ciprojects/formats/VNDWTSTF.owl#VNDWTSTF");
		wtr1.setViewerComment("Parvis is a tool for parallel coordinates (PC) visualisation of multidimensional data sets, as first described in [Inselberg 1981].");
		documentURL = wtr1.saveDocument();
		System.out.println(documentURL);

		ViewerWriter wtr2 = new ViewerWriter("pdf-viewer");
		wtr2.addPartOfSetURI(baseURL + "probeit.owl#probeit");
		wtr2.addPartOfSetURI(baseURL + "mozilla-firefox.owl#mozilla-firefox");
		wtr2.addPartOfSetURI(baseURL + "internet-explorer.owl#internet-explorer");
		wtr2.addFormatURI("http://rio.cs.utep.edu/ciserver/ciprojects/formats/PDF.owl#PDF");
		wtr2.setLabel("Adobe Portable Document Format (PDF) Viewer");
		wtr2.setViewerComment("Renders PDF document and allows for zooming.");
		documentURL = wtr2.saveDocument();
		System.out.println(documentURL);

		ViewerWriter wtr3 = new ViewerWriter("plain-text-viewer");
		wtr3.addPartOfSetURI(baseURL + "probeit.owl#probeit");
		wtr3.addPartOfSetURI(baseURL + "mozilla-firefox.owl#mozilla-firefox");
		wtr3.addPartOfSetURI(baseURL + "internet-explorer.owl#internet-explorer");
		wtr3.addFormatURI("http://rio.cs.utep.edu/ciserver/ciprojects/formats/PLAIN.owl#PLAIN");
		wtr3.addFormatURI("http://rio.cs.utep.edu/ciserver/ciprojects/formats/PLAINTEXT.owl#PLAINTEXT");
		wtr3.addFormatURI("http://rio.cs.utep.edu/ciserver/ciprojects/formats/VNDLATEXZ.owl#VNDLATEXZ");
		wtr3.setLabel("Plain Text Viewer");
		wtr3.setViewerComment("Does nothing really, just extracts the text from the PML conclusion...");
		documentURL = wtr3.saveDocument();
		System.out.println(documentURL);

		ViewerWriter wtr4 = new ViewerWriter("browser-image-viewer");
		wtr4.addPartOfSetURI(baseURL + "mozilla-firefox.owl#mozilla-firefox");
		wtr4.addPartOfSetURI(baseURL + "internet-explorer.owl#internet-explorer");
		wtr4.addFormatURI("http://rio.cs.utep.edu/ciserver/ciprojects/formats/GIF.owl#GIF");
		wtr4.addFormatURI("http://rio.cs.utep.edu/ciserver/ciprojects/formats/PNG.owl#PNG");
		wtr4.addFormatURI("http://rio.cs.utep.edu/ciserver/ciprojects/formats/JPEG.owl#JPEG");
		wtr4.setLabel("Web Browser Image Viewer");
		wtr4.setViewerComment("Views a few standard image formats");
		documentURL = wtr4.saveDocument();
		System.out.println(documentURL);

		ViewerWriter wtr5 = new ViewerWriter("diva-graphics-viewer");
		wtr5.addPartOfSetURI(baseURL + "diva-graphics.owl#diva-graphics");
		wtr5.addFormatURI("http://rio.cs.utep.edu/ciserver/ciprojects/formats/GIF.owl#GIF");
		wtr5.addFormatURI("http://rio.cs.utep.edu/ciserver/ciprojects/formats/PNG.owl#PNG");
		wtr5.addFormatURI("http://rio.cs.utep.edu/ciserver/ciprojects/formats/JPEG.owl#JPEG");
		wtr5.setLabel("Diva Java Graphics");
		wtr5.setViewerComment("Views a few standard image formats");
		documentURL = wtr5.saveDocument();
		System.out.println(documentURL);

		ViewerWriter wtr6 = new ViewerWriter("html-viewer");
		wtr6.addPartOfSetURI(baseURL + "probeit.owl#probeit");
		wtr6.addPartOfSetURI(baseURL + "mozilla-firefox.owl#mozilla-firefox");
		wtr6.addPartOfSetURI(baseURL + "internet-explorer.owl#internet-explorer");
		wtr6.addFormatURI("http://rio.cs.utep.edu/ciserver/ciprojects/formats/HTML.owl#HTML");
		wtr6.setLabel("HyperText Markup Language (HTML) Viewer");
		wtr6.setViewerComment("Renders HTML documents.");
		documentURL = wtr6.saveDocument();
		System.out.println(documentURL);
	}
}
