package edu.utep.trustlab.visko.installation.packages.rdf;

import edu.utep.trustlab.visko.ontology.model.OWLSModel;
import edu.utep.trustlab.visko.ontology.model.ViskoModel;
import edu.utep.trustlab.visko.ontology.service.Toolkit;

public class PackageWriter {
	private String baseURL;
	
	private ViskoModel viskoModel;
	private OWLSModel owlsModel;
	
	private Toolkit toolkit;
	
	public PackageWriter(String url){
		baseURL = url;
	}
	
	public PackageOperatorService createNewOperatorService(){
		PackageOperatorService service = new PackageOperatorService(viskoModel, owlsModel, baseURL);
		service.setToolkit(toolkit);
		return service;
	}
	
	public PackageViewerSet createNewViewerSet(){
		return new PackageViewerSet(viskoModel, baseURL);
	}

	public void createToolkit(String name, String label, String comment){
		toolkit = new Toolkit(baseURL, name, viskoModel);
		toolkit.setComment(comment);
		toolkit.setLabel(label);
	}
	
	public String getRDFString(){
		return viskoModel.getModelAsRDFString();
	}
}