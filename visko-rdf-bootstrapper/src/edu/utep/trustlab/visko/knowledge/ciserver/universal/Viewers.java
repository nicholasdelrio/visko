package edu.utep.trustlab.visko.knowledge.ciserver.universal;


import edu.utep.trustlab.repository.Repository;
import edu.utep.trustlab.visko.knowledge.NickConfigurations;
import edu.utep.trustlab.visko.ontology.operator.writer.ViewerWriter;

public class Viewers {

	public static void main(String[] args) {
		Repository.setRepository(NickConfigurations.getCIServer());		String documentURL;

		ViewerWriter wtr = new ViewerWriter("imageJ-viewer1");
		wtr.setLabel("ImageJ Viewer");
		wtr.addPartOfSetURI("http://rio.cs.utep.edu/ciserver/ciprojects/visko/probeit1.owl#probeit1");
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

		ViewerWriter wtr1 = new ViewerWriter("parvis-viewer1");
		wtr1.addPartOfSetURI("http://rio.cs.utep.edu/ciserver/ciprojects/visko/probeit1.owl#probeit1");
		wtr1.setLabel("Parvis Parallel Coordinates Viewer");
		wtr1.addFormatURI("http://rio.cs.utep.edu/ciserver/ciprojects/formats/VNDWTSTF.owl#VNDWTSTF");
		wtr1.setViewerComment("Parvis is a tool for parallel coordinates (PC) visualisation of multidimensional data sets, as first described in [Inselberg 1981].");
		documentURL = wtr1.saveDocument();
		System.out.println(documentURL);

		ViewerWriter wtr2 = new ViewerWriter("pdf-viewer1");
		wtr2.addPartOfSetURI("http://rio.cs.utep.edu/ciserver/ciprojects/visko/probeit1.owl#probeit1");
		wtr2.addPartOfSetURI("http://rio.cs.utep.edu/ciserver/ciprojects/visko/mozilla-firefox1.owl#mozilla-firefox1");
		wtr2.addPartOfSetURI("http://rio.cs.utep.edu/ciserver/ciprojects/visko/internet-explorer1.owl#internet-explorer1");
		wtr2.addFormatURI("http://rio.cs.utep.edu/ciserver/ciprojects/formats/PDF.owl#PDF");
		wtr2.setLabel("Adobe Portable Document Format (PDF) Viewer");
		wtr2.setViewerComment("Renders PDF document and allows for zooming.");
		documentURL = wtr2.saveDocument();
		System.out.println(documentURL);

		ViewerWriter wtr3 = new ViewerWriter("plain-text-viewer1");
		wtr3.addPartOfSetURI("http://rio.cs.utep.edu/ciserver/ciprojects/visko/probeit1.owl#probeit1");
		wtr3.addPartOfSetURI("http://rio.cs.utep.edu/ciserver/ciprojects/visko/mozilla-firefox1.owl#mozilla-firefox1");
		wtr3.addPartOfSetURI("http://rio.cs.utep.edu/ciserver/ciprojects/visko/internet-explorer1.owl#internet-explorer1");
		wtr3.addFormatURI("http://rio.cs.utep.edu/ciserver/ciprojects/formats/PLAIN.owl#PLAIN");
		wtr3.addFormatURI("http://rio.cs.utep.edu/ciserver/ciprojects/formats/PLAINTEXT.owl#PLAINTEXT");
		wtr3.addFormatURI("http://rio.cs.utep.edu/ciserver/ciprojects/formats/VNDLATEXZ.owl#VNDLATEXZ");
		wtr3.setLabel("Plain Text Viewer");
		wtr3.setViewerComment("Does nothing really, just extracts the text from the PML conclusion...");
		documentURL = wtr3.saveDocument();
		System.out.println(documentURL);

		ViewerWriter wtr4 = new ViewerWriter("browser-image-viewer1");
		wtr4.addPartOfSetURI("http://rio.cs.utep.edu/ciserver/ciprojects/visko/mozilla-firefox1.owl#mozilla-firefox1");
		wtr4.addPartOfSetURI("http://rio.cs.utep.edu/ciserver/ciprojects/visko/internet-explorer1.owl#internet-explorer1");
		wtr4.addFormatURI("http://rio.cs.utep.edu/ciserver/ciprojects/formats/GIF.owl#GIF");
		wtr4.addFormatURI("http://rio.cs.utep.edu/ciserver/ciprojects/formats/PNG.owl#PNG");
		wtr4.addFormatURI("http://rio.cs.utep.edu/ciserver/ciprojects/formats/JPEG.owl#JPEG");
		wtr4.setLabel("Web Browser Image Viewer");
		wtr4.setViewerComment("Views a few standard image formats");
		documentURL = wtr4.saveDocument();
		System.out.println(documentURL);

		ViewerWriter wtr5 = new ViewerWriter("diva-graphics-viewer1");
		wtr5.addPartOfSetURI("http://rio.cs.utep.edu/ciserver/ciprojects/visko/diva-graphics1.owl#diva-graphics1");
		wtr5.addFormatURI("http://rio.cs.utep.edu/ciserver/ciprojects/formats/GIF.owl#GIF");
		wtr5.addFormatURI("http://rio.cs.utep.edu/ciserver/ciprojects/formats/PNG.owl#PNG");
		wtr5.addFormatURI("http://rio.cs.utep.edu/ciserver/ciprojects/formats/JPEG.owl#JPEG");
		wtr5.setLabel("Diva Java Graphics");
		wtr5.setViewerComment("Views a few standard image formats");
		documentURL = wtr5.saveDocument();
		System.out.println(documentURL);

		ViewerWriter wtr6 = new ViewerWriter("html-viewer1");
		wtr6.addPartOfSetURI("http://rio.cs.utep.edu/ciserver/ciprojects/visko/probeit1.owl#probeit1");
		wtr6.addPartOfSetURI("http://rio.cs.utep.edu/ciserver/ciprojects/visko/mozilla-firefox1.owl#mozilla-firefox1");
		wtr6.addPartOfSetURI("http://rio.cs.utep.edu/ciserver/ciprojects/visko/internet-explorer1.owl#internet-explorer1");
		wtr6.addFormatURI("http://rio.cs.utep.edu/ciserver/ciprojects/formats/HTML.owl#HTML");
		wtr6.setLabel("HyperText Markup Language (HTML) Viewer");
		wtr6.setViewerComment("Renders HTML documents.");
		documentURL = wtr6.saveDocument();
		System.out.println(documentURL);
	}
}
