package edu.utep.trustlab.visko.installation.packages.rdf;

import edu.utep.trustlab.visko.ontology.model.ViskoModel;
import edu.utep.trustlab.visko.ontology.pmlp.Format;
import edu.utep.trustlab.visko.ontology.service.Toolkit;
import edu.utep.trustlab.visko.ontology.view.View;

public class PackageWriter {
	
	private String baseURL;
	private String baseFileURL;
	
	private ViskoModel viskoModel;
	private static ViskoModel loadingModel = new ViskoModel();
	private Toolkit toolkit;

	public PackageWriter(String url, String packageFileName){
		baseURL = url;
		baseFileURL = baseURL + packageFileName;
		
		viskoModel = new ViskoModel();		
		viskoModel.createOntology(baseFileURL);
	}
	
	public PackageOperatorService createNewOperatorService(String name){
		PackageOperatorService service = new PackageOperatorService(name, viskoModel, baseURL, baseFileURL);
		service.setToolkit(toolkit);
		return service;
	}
	
	public PackageViewerSet createNewViewerSet(String name){
		return new PackageViewerSet(name, viskoModel, baseURL);
	}

	public Toolkit createNewToolkit(String name){
		toolkit = new Toolkit(baseURL, name, viskoModel);
		return toolkit;
	}
	
	public static Format getFormat(String formatURI){
		return new Format(formatURI, loadingModel);
	}
	
	public static View getView(String viewURI){
		return new View(viewURI, loadingModel);
	}
	
	public void write(){
		
	}
}