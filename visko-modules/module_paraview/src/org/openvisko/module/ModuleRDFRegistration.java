package org.openvisko.module;

import java.net.URL;

import org.openvisko.module.registration.AbstractModuleRDFRegistration;
import org.openvisko.module.registration.ModuleViewerSet;
import org.openvisko.module.registration.ModuleWriter;

import com.hp.hpl.jena.ontology.OntResource;

import edu.utep.trustlab.visko.ontology.pmlp.Format;
import edu.utep.trustlab.visko.ontology.viskoService.Toolkit;


import edu.utep.trustlab.visko.ontology.viskoOperator.Viewer;


public class ModuleRDFRegistration extends AbstractModuleRDFRegistration {

	private static final class Resources{
		//formats
		private static final Format xdmf = ModuleWriter.getFormat("http://openvisko.org/rdf/pml2/formats/XDMF.owl#XDMF");
		private static final Format xml = ModuleWriter.getFormat("http://openvisko.org/rdf/pml2/formats/XML.owl#XML");
		
		// types
		private static final OntResource vtkImageData3D = ModuleWriter.getDataType("http://www.vtk.org/vtk-data.owl#vtkImageData3D");
		private static final OntResource vtkPolyData = ModuleWriter.getDataType("http://www.vtk.org/vtk-data.owl#vtkPolyData");
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
		
		Viewer viewer2 = viewerSet.createNewViewer("polyData-viewer");
		viewer2.setComment("Views VTK Poly Data Files (VTP)");
		viewer2.setLabel("VTK Poly Data Viewer");
		viewer2.addInputDataType(Resources.vtkPolyData);
		viewer2.addInputFormat(Resources.xml);
		
		Viewer viewer3 = viewerSet.createNewViewer("imageData-viewer");
		viewer3.setComment("Views VTK Image Data Files (VTP)");
		viewer3.setLabel("VTK Image Data Viewer");
		viewer3.addInputDataType(Resources.vtkImageData3D);
		viewer3.addInputFormat(Resources.xml);
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
