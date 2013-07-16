package org.openvisko.module;

import java.net.URL;

import com.hp.hpl.jena.ontology.OntResource;

import org.openvisko.module.registration.AbstractModuleRDFRegistration;
import org.openvisko.module.registration.ModuleViewerSet;
import org.openvisko.module.registration.ModuleWriter;

import edu.utep.trustlab.visko.ontology.pmlp.Format;
import edu.utep.trustlab.visko.ontology.viskoOperator.Viewer;
import edu.utep.trustlab.visko.ontology.viskoService.Toolkit;
import edu.utep.trustlab.visko.ontology.viskoView.VisualizationAbstraction;
import edu.utep.trustlab.visko.ontology.vocabulary.ViskoV;

public class ModuleRDFRegistration extends AbstractModuleRDFRegistration {

	private static final class Resources {		
		// formats
		private static final Format json = ModuleWriter.getFormat("http://openvisko.org/rdf/pml2/formats/JSON.owl#JSON");

		// data types
		private static final OntResource generalD3Graph = ModuleWriter.getDataType("http://d3js.org/d3.owl#Graph");
		private static final OntResource viskoDataTransformationPaths = ModuleWriter.getDataType("http://openvisko.org/rdf/ontology/visko.owl#VisKo_DataTransformationPaths");
		private static final OntResource viskoOperatorPaths = ModuleWriter.getDataType("http://openvisko.org/rdf/ontology/visko.owl#VisKo_OperatorPaths");
		private static final OntResource viskoInstanceSummary = ModuleWriter.getDataType("http://openvisko.org/rdf/ontology/visko.owl#VisKo_InstanceSummary");

		// views
		private static final VisualizationAbstraction viskoInstancesBarChart = ModuleWriter.getView(ViskoV.INDIVIDUAL_URI_2D_VisKo_Instances_BarChart);
		private static final VisualizationAbstraction viskoOperatorPathsforceGraph = ModuleWriter.getView(ViskoV.INDIVIDUAL_URI_2D_VisKo_OperatorPaths_ForceGraph);
		private static final VisualizationAbstraction viskoDataTransformationsForceGraph = ModuleWriter.getView(ViskoV.INDIVIDUAL_URI_2D_VisKo_DataTransformations_ForceGraph);
		private static final VisualizationAbstraction forceGraph = ModuleWriter.getView(ViskoV.INDIVIDUAL_URI_2D_ForceGraph);
	}

	@Override
	public void populateToolkit() {
		Toolkit toolkit = getModuleWriter().createNewToolkit("data-driven-documents");
		toolkit.setComment("Data Driven Documents");
		toolkit.setLabel("Data Driven Documents");
		
		String stringURL = "http://d3js.org/";
		try
		{
			URL toolkitURL = new URL(stringURL);
			toolkit.setDocumentationURL(toolkitURL);
		}
		catch(Exception e){e.printStackTrace();}

	}

	@Override
	public void populateViewerSets() {

		ModuleViewerSet viewerSet = getModuleWriter().createNewViewerSet("d3-viewer-set");
		viewerSet.setComment("Data Driven Documents Viewer Set");
		viewerSet.setLabel("D3 Viewer Set");
		
		Viewer viewer1 = viewerSet.createNewViewer("visko-operator-paths-viewer");
		viewer1.setLabel("Force Graph Viewer for Visko Operator Paths");
		viewer1.setComment("Force Graph Viewer for Visko Operator Paths");
		viewer1.addInputFormat(Resources.json);
		viewer1.addInputDataType(Resources.viskoOperatorPaths);
		viewer1.setVisualizationAbstraction(Resources.viskoOperatorPathsforceGraph);		
		String viewerURL1 = getWebServerUrl() + "/" + getWebappName() + "/" + "ForceGraph_OperatorPaths.html";
		viewer1.setEndpointURL(viewerURL1);

		Viewer viewer2 = viewerSet.createNewViewer("visko-instances-viewer");
		viewer2.setLabel("Bar Chart Viewer for Visko KB");
		viewer2.setComment("Bar Chart Viewer for Visko KB");
		viewer2.addInputFormat(Resources.json);
		viewer2.addInputDataType(Resources.viskoInstanceSummary);
		viewer2.setVisualizationAbstraction(Resources.viskoInstancesBarChart);
		String viewerURL2 = getWebServerUrl() + "/" + getWebappName() + "/" + "Bars_Instances.html";
		viewer2.setEndpointURL(viewerURL2);

		Viewer viewer3 = viewerSet.createNewViewer("visko-data-transformations-viewer");
		viewer3.setLabel("Force Graph Viewer for Visko Data Transformation Paths");
		viewer3.setComment("Force Graph Viewer for Visko Data Transformation Paths");
		viewer3.addInputFormat(Resources.json);
		viewer3.addInputDataType(Resources.viskoDataTransformationPaths);
		viewer3.setVisualizationAbstraction(Resources.viskoDataTransformationsForceGraph);
		String viewerURL3 = getWebServerUrl() + "/" + getWebappName() + "/" + "ForceGraph_DataTransformations.html";
		viewer3.setEndpointURL(viewerURL3);
		
		Viewer viewer4 = viewerSet.createNewViewer("general-graph-viewer");
		viewer4.setLabel("Force Graph Viewer for Graphs");
		viewer4.setComment("Force Graph Viewer for Graphs");
		viewer4.addInputFormat(Resources.json);
		viewer4.addInputDataType(Resources.generalD3Graph);
		viewer4.setVisualizationAbstraction(Resources.forceGraph);
		String viewerURL4 = getWebServerUrl() + "/" + getWebappName() + "/" + "ForceGraph_Generic.html";
		viewer4.setEndpointURL(viewerURL4);
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
