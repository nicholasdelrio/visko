package org.openvisko.module;

import java.net.URL;

import org.openvisko.module.registration.AbstractModuleRDFRegistration;
import org.openvisko.module.registration.ModuleViewerSet;
import org.openvisko.module.registration.ModuleWriter;

import com.hp.hpl.jena.ontology.OntResource;

import edu.utep.trustlab.visko.ontology.pmlp.Format;
import edu.utep.trustlab.visko.ontology.viskoService.Toolkit;
import edu.utep.trustlab.visko.ontology.viskoView.VisualizationAbstraction;
import edu.utep.trustlab.visko.ontology.vocabulary.ViskoV;

import edu.utep.trustlab.visko.ontology.viskoOperator.Viewer;


public class ModuleRDFRegistration extends AbstractModuleRDFRegistration {

	private static final class Resources{
		//formats
		private static final Format xdmf = ModuleWriter.getFormat("http://openvisko.org/rdf/pml2/formats/XDMF.owl#XDMF");
	}
	
  @Override
   public void populateViewerSets() {
		ModuleViewerSet viewerSet = getModuleWriter().createNewViewerSet("paraview");
		viewerSet.setComment("paraview");
		viewerSet.setLabel("paraview");
		
		Viewer viewer1 = viewerSet.createNewViewer("xdmf-viewer");
		viewer1.setComment("XDMF Viewer of the ParaView Distribution");
		viewer1.setLabel("XDMF Viewer");
		viewer1.addInputFormat(Resources.xdmf);
  }
	
  @Override
  public void populateToolkit() {
		Toolkit toolkit = getModuleWriter().createNewToolkit("kitware-paraview");
		toolkit.setComment("Kitware ParaView");
		toolkit.setLabel("Kitware ParaView");
		
		String stringURL = "http://www.paraview.org/";
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
    
  }

  @Override
  public void populateParameterBindings() {
    // TODO Auto-generated method stub
    
  }

}
