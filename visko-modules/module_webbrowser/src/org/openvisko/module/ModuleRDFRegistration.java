package org.openvisko.module;

import java.net.URL;

import org.openvisko.module.registration.AbstractModuleRDFRegistration;
import org.openvisko.module.registration.ModuleViewerSet;
import org.openvisko.module.registration.ModuleWriter;

import edu.utep.trustlab.visko.ontology.pmlp.Format;
import edu.utep.trustlab.visko.ontology.viskoOperator.Viewer;
import edu.utep.trustlab.visko.ontology.viskoService.Toolkit;


public class ModuleRDFRegistration extends AbstractModuleRDFRegistration {

	private static final class Resources {
		//formats
		private static final Format pdf = ModuleWriter.getFormat("http://openvisko.org/rdf/pml2/formats/PDF.owl#PDF");
		private static final Format png = ModuleWriter.getFormat("http://openvisko.org/rdf/pml2/formats/PNG.owl#PNG");
		private static final Format jpg = ModuleWriter.getFormat("http://openvisko.org/rdf/pml2/formats/JPEG.owl#JPEG");
		private static final Format gif = ModuleWriter.getFormat("http://openvisko.org/rdf/pml2/formats/GIF.owl#GIF");
		private static final Format plain = ModuleWriter.getFormat("http://openvisko.org/rdf/pml2/formats/PLAIN.owl#PLAIN");
		private static final Format plainText = ModuleWriter.getFormat("http://openvisko.org/rdf/pml2/formats/PLAINTEXT.owl#PLAINTEXT");
		private static final Format html = ModuleWriter.getFormat("http://openvisko.org/rdf/pml2/formats/HTML.owl#HTML");
		//browsers may show tiff files but not necessarily inside of html documents
		private static final Format tiff = ModuleWriter.getFormat("http://openvisko.org/rdf/pml2/formats/TIFF.owl#TIFF");
		}


		@Override
		public void populateServices() {
		// TODO Auto-generated method stub

		}

		@Override
		public void populateToolkit() {
		Toolkit toolkit = getModuleWriter().createNewToolkit("web-browser");
		toolkit.setComment("Web browser");
		toolkit.setLabel("Web browser");
		
		String stringURL = "http://en.wikipedia.org/wiki/Web_browser";
		try
		{
			URL toolkitURL = new URL(stringURL);
			toolkit.setDocumentationURL(toolkitURL);
		}
		catch(Exception e){e.printStackTrace();}
		
		}

		@Override
		public void populateViewerSets() {
		ModuleViewerSet viewerSet = getModuleWriter().createNewViewerSet("web-browser");
		viewerSet.setComment("Web browser");
		viewerSet.setLabel("Web browser");

		Viewer viewer1 = viewerSet.createNewViewer("web-browser-image-viewer");
		viewer1.setLabel("Web Browser Image Viewer");
		viewer1.setComment("Views a few standard image formats");
		viewer1.addInputFormat(Resources.gif);
		viewer1.addInputFormat(Resources.png);
		viewer1.addInputFormat(Resources.jpg);

		Viewer viewer2 = viewerSet.createNewViewer("web-browser-pdf-viewer");
		viewer2.setLabel("Adobe Portable Document Format (PDF) Viewer");
		viewer2.setComment("Renders PDF document and allows for zooming.");
		viewer2.addInputFormat(Resources.pdf);

		Viewer viewer3 = viewerSet.createNewViewer("web-browser-text-viewer");
		viewer3.setLabel("Plain Text Viewer");
		viewer3.setComment("Just a plain old text viewer");
		viewer3.addInputFormat(Resources.plain);
		viewer3.addInputFormat(Resources.plainText);

		Viewer viewer4 = viewerSet.createNewViewer("web-browser-html-viewer");
		viewer4.setLabel("HyperText Markup Language (HTML) Viewer");
		viewer4.setComment("Renders HTML documents.");
		viewer4.addInputFormat(Resources.html);
		
		Viewer viewer5 = viewerSet.createNewViewer("web-browser-quicktime-viewer");
		viewer5.setLabel("Apple QuickTime Viewer");
		viewer5.setComment("Renders Movies and Images");
		viewer5.addInputFormat(Resources.tiff);
		}


		@Override
		public void populateParameterBindings() {
		// TODO Auto-generated method stub

		}
		

}
