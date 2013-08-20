package org.openvisko.module;

import java.net.URL;

import org.openvisko.module.registration.AbstractModuleRDFRegistration;
import org.openvisko.module.registration.ModuleOperatorService;
import org.openvisko.module.registration.ModuleViewerSet;
import org.openvisko.module.registration.ModuleWriter;

import edu.utep.trustlab.visko.ontology.pmlp.Format;
import edu.utep.trustlab.visko.ontology.viskoOperator.Viewer;
import edu.utep.trustlab.visko.ontology.viskoService.Toolkit;


public class ModuleRDFRegistration extends AbstractModuleRDFRegistration {

	private static final class Resources {
		//formats
		private static final Format pdf = ModuleWriter.getFormat("http://openvisko.org/rdf/pml2/formats/PDF.owl#PDF");
		private static final Format html = ModuleWriter.getFormat("http://openvisko.org/rdf/pml2/formats/HTML.owl#HTML");
		private static final Format png = ModuleWriter.getFormat("http://openvisko.org/rdf/pml2/formats/PNG.owl#PNG");
		private static final Format gif = ModuleWriter.getFormat("http://openvisko.org/rdf/pml2/formats/GIF.owl#GIF");
		private static final Format jpeg = ModuleWriter.getFormat("http://openvisko.org/rdf/pml2/formats/JPEG.owl#JPEG");

	}
	
  @Override
  public void populateViewerSets() {
    // TODO Auto-generated method stub
	  ModuleViewerSet viewerSet = getModuleWriter().createNewViewerSet("VLC-Viewerset");
		viewerSet.setComment("VLC Viewerset");
		viewerSet.setLabel("VLC Viewerset");

		Viewer viewer1 = viewerSet.createNewViewer("vlc-web-browser-image-viewer");
		viewer1.setLabel("VLC Web Browser Image Viewer");
		viewer1.setComment("Views vlc resources");
		viewer1.addInputFormat(Resources.png);
		viewer1.addInputFormat(Resources.jpeg);
		viewer1.addInputFormat(Resources.gif);
  }

  @Override
  public void populateToolkit() {
    // TODO Auto-generated method stub
		Toolkit toolkit = getModuleWriter().createNewToolkit("vlc");
		toolkit.setComment("Virtual Learning Center");
		toolkit.setLabel("Virtual Learning Center");
		
		String stringURL = "http://vlc.cybershare.utep.edu";
		try
		{
			URL toolkitURL = new URL(stringURL);
			toolkit.setDocumentationURL(toolkitURL);
		}
		catch(Exception e){e.printStackTrace();}
	  
  }
  
  @Override
  public void populateServices() {
    // TODO Auto-generated method stub
	  String wsdlURL = getWSDLURL();	

		String operationName = "html2pdf";
		ModuleOperatorService service1 = getModuleWriter().createNewOperatorService(null, operationName);
		service1.setComment("Converts html pages to pdf");
		service1.setLabel("HTML to PDF");
		service1.setWSDLURL(wsdlURL);
		service1.setInputFormat(Resources.html);
		service1.setOutputFormat(Resources.pdf);
  }

  @Override
  public void populateParameterBindings() {
    // TODO Auto-generated method stub
    
  }

}
