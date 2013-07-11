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
		private static final Format pdb = ModuleWriter.getFormat("http://openvisko.org/rdf/pml2/formats/PDB.owl#PDB");
		private static final Format pqr = ModuleWriter.getFormat("http://openvisko.org/rdf/pml2/formats/PQR.owl#PQR");
		private static final Format jpeg = ModuleWriter.getFormat("http://openvisko.org/rdf/pml2/formats/JPEG.owl#JPEG");

		private static final VisualizationAbstraction molecularStructure = ModuleWriter.getView(ViskoV.INDIVIDUAL_URI_3D_MolecularStructure);
		private static final VisualizationAbstraction molecularStructureCartoon = ModuleWriter.getView(ViskoV.INDIVIDUAL_URI_3D_MolecularStructure_Cartoon);
		private static final VisualizationAbstraction molecularStructureRibbon = ModuleWriter.getView(ViskoV.INDIVIDUAL_URI_3D_MolecularStructure_Ribbon);
		}

	@Override
		public void populateViewerSets() {
	}

	@Override
	public void populateServices() {
		String wsdlURL = getWSDLURL();

		String operationName = "moleculeRendering";
		ModuleOperatorService service_molRendering = getModuleWriter().createNewOperatorService(null, operationName);
		service_molRendering.setComment("Renders a PDB molecular structure into a JPEG image");
		service_molRendering.setLabel("PDB to JPEG");
		service_molRendering.setWSDLURL(wsdlURL);
		service_molRendering.setInputFormat(Resources.pdb);
		service_molRendering.setOutputFormat(Resources.jpeg);
		service_molRendering.setView(Resources.molecularStructure);

		String operationNameCartoon = "moleculeRenderingCartoon";
		ModuleOperatorService service_molRendering_cartoon = getModuleWriter().createNewOperatorService(null, operationNameCartoon);
		service_molRendering_cartoon.setComment("Renders a PDB molecular structure into a JPEG image");
		service_molRendering_cartoon.setLabel("PDB to JPEG");
		service_molRendering_cartoon.setWSDLURL(wsdlURL);
		service_molRendering_cartoon.setInputFormat(Resources.pdb);
		service_molRendering_cartoon.setOutputFormat(Resources.jpeg);
		service_molRendering_cartoon.setView(Resources.molecularStructureCartoon);

		String operationNameRibbon = "moleculeRenderingRibbon";
		ModuleOperatorService service_molRendering_ribbon = getModuleWriter().createNewOperatorService(null, operationNameRibbon);
		service_molRendering_ribbon.setComment("Renders a PDB molecular structure into a JPEG image");
		service_molRendering_ribbon.setLabel("PDB to JPEG");
		service_molRendering_ribbon.setWSDLURL(wsdlURL);
		service_molRendering_ribbon.setInputFormat(Resources.pdb);
		service_molRendering_ribbon.setOutputFormat(Resources.jpeg);
		service_molRendering_ribbon.setView(Resources.molecularStructureRibbon);
	}

	@Override
	public void populateToolkit() {
		Toolkit toolkit = getModuleWriter().createNewToolkit("Jmol");
		toolkit.setComment("jmol");
		toolkit.setLabel("jmol");
		
		String stringURL = "http://jmol.sourceforge.net/";
		try
		{
			URL toolkitURL = new URL(stringURL);
			toolkit.setDocumentationURL(toolkitURL);
		}
		catch(Exception e){e.printStackTrace();}		
	}


	@Override
	public void populateParameterBindings() {
		// TODO Auto-generated method stub
	}

}
