package org.openvisko.module;

import java.net.URL;

import org.openvisko.module.registration.AbstractModuleRDFRegistration;
import org.openvisko.module.registration.ModuleOperatorService;
import org.openvisko.module.registration.ModuleWriter;

import edu.utep.trustlab.visko.ontology.pmlp.Format;
import edu.utep.trustlab.visko.ontology.viskoService.Toolkit;
import edu.utep.trustlab.visko.ontology.viskoView.VisualizationAbstraction;
import edu.utep.trustlab.visko.ontology.vocabulary.ViskoV;


public class ModuleRDFRegistration extends AbstractModuleRDFRegistration {

	private static final class Resources {
		//formats
		private static final Format fits = ModuleWriter.getFormat("http://openvisko.org/rdf/pml2/formats/FITS.owl#FITS");
		private static final Format png = ModuleWriter.getFormat("http://openvisko.org/rdf/pml2/formats/PNG.owl#PNG");

		private static final VisualizationAbstraction spherizedImage = ModuleWriter.getView(ViskoV.INDIVIDUAL_URI_2D_SpherizedRaster);
		}

		@Override
		public void populateServices() {
		String wsdlURL = getWSDLURL();

		String operationName = "fits2png";
		ModuleOperatorService service1 = getModuleWriter().createNewOperatorService(null, operationName);
		service1.setComment("Converts Flexible Image Transport System (FITS) to Portable Network Graphic (PNG)");
		service1.setLabel("FITS to PNG");
		service1.setWSDLURL(wsdlURL);
		service1.setInputFormat(Resources.fits);
		service1.setOutputFormat(Resources.png);

		operationName = "spherize";
		ModuleOperatorService service2 = getModuleWriter().createNewOperatorService(null, operationName);
		service2.setComment("Wraps a Portable Network Graphic (PNG) image around a spherical surface");
		service2.setLabel("spherize");
		service2.setWSDLURL(wsdlURL);
		service2.setInputFormat(Resources.png);
		service2.setOutputFormat(Resources.png);
		service2.setView(Resources.spherizedImage);
		}

		@Override
		public void populateToolkit() {
		Toolkit toolkit = getModuleWriter().createNewToolkit("image-magick");
		toolkit.setComment("ImageMagick");
		toolkit.setLabel("ImageMagick");
		
		String stringURL = "http://www.imagemagick.org/";
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
