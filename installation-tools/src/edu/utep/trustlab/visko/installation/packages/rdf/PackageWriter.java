package edu.utep.trustlab.visko.installation.packages.rdf;

import edu.utep.trustlab.visko.ontology.model.OWLSModel;
import edu.utep.trustlab.visko.ontology.model.ViskoModel;
import edu.utep.trustlab.visko.ontology.pmlp.Format;
import edu.utep.trustlab.visko.ontology.service.Toolkit;

public class PackageWriter {
	private String baseURL;
	
	private ViskoModel viskoModel;
	private OWLSModel owlsModel;
	
	private static ViskoModel loadingModel = new ViskoModel();
	
	private Toolkit toolkit;
	
	public PackageWriter(String url){
		baseURL = url;
		viskoModel = new ViskoModel();
		owlsModel = new OWLSModel();
	}
	
	public PackageOperatorService createNewOperatorService(String name){
		PackageOperatorService service = new PackageOperatorService(name, viskoModel, owlsModel, baseURL);
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
	
	public String getRDFString(){
		return viskoModel.getModelAsRDFString();
	}
}