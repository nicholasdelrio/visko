package org.openvisko.module;

import java.net.URL;

import org.openvisko.module.registration.AbstractModuleRDFRegistration;
import org.openvisko.module.registration.ModuleOperatorService;
import org.openvisko.module.registration.ModuleWriter;

import edu.utep.trustlab.visko.ontology.pmlp.Format;
import edu.utep.trustlab.visko.ontology.viskoService.Toolkit;


public class ModuleRDFRegistration extends AbstractModuleRDFRegistration {

	private static final class Resources {
		//formats
		private static final Format ps = ModuleWriter.getFormat("http://openvisko.org/rdf/pml2/formats/POSTSCRIPT.owl#POSTSCRIPT");
		private static final Format pdf = ModuleWriter.getFormat("http://openvisko.org/rdf/pml2/formats/PDF.owl#PDF");
		private static final Format png = ModuleWriter.getFormat("http://openvisko.org/rdf/pml2/formats/PNG.owl#PNG");
	}

	@Override
	public void populateServices() {
		String wsdlURL = getWSDLURL();	

		String operationName = "ps2pdf";
		ModuleOperatorService service1 = getModuleWriter().createNewOperatorService(null, operationName);
		service1.setComment("Converts PostScript (PS) to Portable Document Format (PDF)");
		service1.setLabel("PS to PDF");
		service1.setWSDLURL(wsdlURL);
		service1.setInputFormat(Resources.ps);
		service1.setOutputFormat(Resources.pdf);

		operationName = "ps2png";
		ModuleOperatorService service2 = getModuleWriter().createNewOperatorService(null, operationName);
		service2.setComment("Converts PostScript (PS) to Portable Network Graphic (PNG)");
		service2.setLabel("PS to PNG");
		service2.setWSDLURL(wsdlURL);
		service2.setInputFormat(Resources.ps);
		service2.setOutputFormat(Resources.png);

		operationName = "pdf2png";
		ModuleOperatorService service3 = getModuleWriter().createNewOperatorService(null, operationName);
		service3.setComment("Converts Portable Document Format (PDF) to Portable Network Graphic (PNG)");
		service3.setLabel("PDF to PNG");
		service3.setWSDLURL(wsdlURL);
		service3.setInputFormat(Resources.pdf);
		service3.setOutputFormat(Resources.png);

	}

	@Override
	public void populateToolkit() {
		Toolkit toolkit = getModuleWriter().createNewToolkit("ghostscript");
		toolkit.setComment("GhostScript PostScript and PDF Tools");
		toolkit.setLabel("GhostScript (GS)");
		
		String stringURL = "http://www.ghostscript.com/";
		try
		{
			URL toolkitURL = new URL(stringURL);
			toolkit.setDocumentationURL(toolkitURL);
		}
		catch(Exception e){e.printStackTrace();}	

	}

	@Override
	public void populateViewerSets() {
		// TODO Auto-generated method stub

	}

	@Override
	public void populateParameterBindings() {
		// TODO Auto-generated method stub

	}
}
