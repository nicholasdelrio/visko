package edu.utep.trustlab.visko.installation.packages.rdf;

import java.util.ArrayList;
import java.util.HashMap;

import edu.utep.trustlab.contentManagement.ContentManager;
import edu.utep.trustlab.visko.ontology.model.ViskoModel;
import edu.utep.trustlab.visko.ontology.pmlp.Format;
import edu.utep.trustlab.visko.ontology.service.OWLSService;
import edu.utep.trustlab.visko.ontology.service.Toolkit;
import edu.utep.trustlab.visko.ontology.view.View;

public class PackageWriter {
	
	private static ViskoModel loadingModel = new ViskoModel();
	
	private String baseURL;
	private String baseFileURL;
	private String fileName;
	
	private ViskoModel viskoModel;
	private Toolkit toolkit;
	
	private ArrayList<PackageViewerSet> viewerSets;
	private ArrayList<PackageOperatorService> services;
	
	private HashMap<String, PackageOperatorService> operatorServices;
	
	private void addOperatorService(PackageOperatorService operatorService){
		operatorServices.put(operatorService.getName(), operatorService);
	}
	
	public PackageOperatorService getOperatorService(String operationName){
		return operatorServices.get(operationName);
	}
	
	public PackageWriter(String url, String packageFileName){
		baseURL = url;
		baseFileURL = baseURL + packageFileName;
		fileName = packageFileName;
		
		viskoModel = new ViskoModel();		
		viskoModel.createOntology(baseFileURL);
		
		viewerSets = new ArrayList<PackageViewerSet>();
		services = new ArrayList<PackageOperatorService>();
		operatorServices = new HashMap<String, PackageOperatorService>();
	}
	
	public PackageOperatorService createNewOperatorService(String name){
		PackageOperatorService service = new PackageOperatorService(name, viskoModel, baseURL, baseFileURL);
		service.setToolkit(toolkit);
		services.add(service);
		
		addOperatorService(service);
		
		return service;
	}
	
	public PackageViewerSet createNewViewerSet(String name){
		PackageViewerSet viewerSet =  new PackageViewerSet(name, viskoModel, baseURL);
		viewerSets.add(viewerSet);
		return viewerSet;
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
	
	private void addToModel(){
		for(PackageViewerSet viewerSet : viewerSets){
			viewerSet.addToModel();
		}
		
		for(PackageOperatorService service : services){
			service.addToModel();
		}
	}
	
	public void dumpPackageRDF(ContentManager manager){
		addToModel();
		writeViskoRDF(manager);
		writeOWLSRDF(manager);
	}
	
	private void writeViskoRDF(ContentManager manager){
		manager.saveDocument(viskoModel.getModelAsRDFString(), fileName);
	}
	
	private void writeOWLSRDF(ContentManager manager){
		OWLSService owlsService;
		for(PackageOperatorService service : services){
			owlsService = service.getOWLSService();
			manager.saveDocument(owlsService.getModel().getModelAsRDFString(), owlsService.getFileName());
		}
	}
}