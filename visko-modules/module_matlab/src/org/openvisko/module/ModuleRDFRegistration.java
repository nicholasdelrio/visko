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
		private static final Format matfile = ModuleWriter.getFormat("http://openvisko.org/rdf/pml2/formats/MAT-FILE.owl#MAT-FILE");
		//private static final Format tiff = ModuleWriter.getFormat("http://openvisko.org/rdf/pml2/formats/TIFF.owl#TIFF");
		private static final Format jpeg = ModuleWriter.getFormat("http://openvisko.org/rdf/pml2/formats/JPEG.owl#JPEG");
		
		//semantic type uris
		private static OntResource thing = OWL.getOWLThing();

		// visualization abstractions
		private static final VisualizationAbstraction raster = ModuleWriter.getView(ViskoV.INDIVIDUAL_URI_2D_RasterMap);
	}

	@Override
	public void populateServices() {
		String wsdlURL = getWSDLURL();
		
		String operationName = "matlab2DRaster";
		ModuleOperatorService service1 = getModuleWriter().createNewOperatorService(null, operationName);
		service1.setComment("Convert mat-file with 2D arrays into tiff images");
		service1.setLabel(operationName);
		service1.setWSDLURL(wsdlURL);
		service1.setInputFormat(Resources.matfile);
		service1.setInputDataType(Resources.thing);
		service1.setOutputFormat(Resources.jpeg);
		service1.setView(Resources.raster);
		service1.setOutputDataType(Resources.thing);
		
	}

	@Override
	public void populateToolkit() {
		Toolkit toolkit = getModuleWriter().createNewToolkit("Matlab Language");
		toolkit.setComment("Matlab Language by MathWorks");
		toolkit.setLabel("Matlab Language by MathWorks");
		
		String stringURL = "http://www.mathworks.com/products/matlab/";
		try
		{
			URL toolkitURL = new URL(stringURL);
			toolkit.setDocumentationURL(toolkitURL);
		}
		catch(Exception e){e.printStackTrace();}	
	}

	@Override
	public void populateViewerSets() {
	}

	@Override
	public void populateParameterBindings() {
		ModuleInputParameterBindings bindings1 = getModuleWriter().createNewInputParameterBindings();
		addParticleImageGeneration(bindings1);
	}

	private void addParticleImageGeneration(ModuleInputParameterBindings bindingsSet){
		bindingsSet.addSemanticType(Resources.thing);
		
		bindingsSet.addInputBinding("matlab2DRaster", "outputFile", "test");
		bindingsSet.addInputBinding("matlab2DRaster", "selectedColor", "hot");
	}


}
