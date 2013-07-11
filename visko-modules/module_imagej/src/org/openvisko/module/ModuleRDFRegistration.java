package org.openvisko.module;

import java.net.URL;

import org.openvisko.module.registration.AbstractModuleRDFRegistration;
import org.openvisko.module.registration.ModuleInputParameterBindings;
import org.openvisko.module.registration.ModuleOperatorService;
import org.openvisko.module.registration.ModuleWriter;

import com.hp.hpl.jena.ontology.OntResource;

import edu.utep.trustlab.visko.ontology.pmlp.Format;
import edu.utep.trustlab.visko.ontology.viskoService.Toolkit;
import edu.utep.trustlab.visko.ontology.viskoView.VisualizationAbstraction;
import edu.utep.trustlab.visko.ontology.vocabulary.ViskoV;
import edu.utep.trustlab.visko.ontology.vocabulary.supplemental.OWL;


public class ModuleRDFRegistration extends AbstractModuleRDFRegistration {

	private static final class Resources {

		//formats
		private static final Format png = ModuleWriter.getFormat("http://openvisko.org/rdf/pml2/formats/PNG.owl#PNG");

		//views
		private static final VisualizationAbstraction surfacePlot = ModuleWriter.getView(ViskoV.INDIVIDUAL_URI_3D_SurfacePlot);

		//semantic types
		private static OntResource thing = OWL.getOWLThing();
		}

		@Override
		public void populateServices() {
		String wsdlURL = getWSDLURL();

		String operationName = "surfacePlotter";
		ModuleOperatorService service1 = getModuleWriter().createNewOperatorService(null, operationName);
		service1.setComment("Creates a surface plot of color values contained in an image");
		service1.setLabel("Create a surface plot of color values contained in an image");
		service1.setWSDLURL(wsdlURL);
		service1.setView(Resources.surfacePlot);
		service1.setInputFormat(Resources.png);
		service1.setOutputFormat(Resources.png);

		}

		@Override
		public void populateToolkit() {
		Toolkit toolkit = getModuleWriter().createNewToolkit("ImageJ");
		toolkit.setComment("ImageJ from NIH");
		toolkit.setLabel("ImageJ");
		
		String stringURL = "http://rsbweb.nih.gov/ij/";
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
		ModuleInputParameterBindings bindings1 = getModuleWriter().createNewInputParameterBindings();
		bindings1.addSemanticType(Resources.thing);

		bindings1.addInputBinding("surfacePlotter", "plotWidth", "400");
		bindings1.addInputBinding("surfacePlotter", "polygonMultiplier", "100");
		bindings1.addInputBinding("surfacePlotter", "showWireframe", "true");
		bindings1.addInputBinding("surfacePlotter", "showGrayscale", "true");
		bindings1.addInputBinding("surfacePlotter", "showAxis", "true");
		bindings1.addInputBinding("surfacePlotter", "whiteBackground", "true");
		bindings1.addInputBinding("surfacePlotter", "blackFill", "false");
		bindings1.addInputBinding("surfacePlotter", "smooth", "true");
		}

}
