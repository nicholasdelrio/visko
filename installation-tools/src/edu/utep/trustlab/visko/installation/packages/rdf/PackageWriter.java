package edu.utep.trustlab.visko.installation.packages.rdf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.mindswap.owl.OWLIndividual;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntResource;

import edu.utep.trustlab.contentManagement.ContentManager;
import edu.utep.trustlab.visko.ontology.model.ViskoModel;
import edu.utep.trustlab.visko.ontology.pmlp.Format;
import edu.utep.trustlab.visko.ontology.viskoService.OWLSService;
import edu.utep.trustlab.visko.ontology.viskoService.Toolkit;
import edu.utep.trustlab.visko.ontology.viskoView.VisualizationAbstraction;

public class PackageWriter {
	
	private static ViskoModel loadingModel = new ViskoModel();
	private static OntModel dataTypesModel;
	
	private String baseURL;
	private String baseFileURL;
	private String fileName;
	
	private ViskoModel viskoModel;
	private Toolkit toolkit;
	
	private boolean servicesAddedToModel;
	
	private ArrayList<PackageViewerSet> viewerSets;
	private HashMap<String, PackageOperatorService> operatorServices;
	private ArrayList<PackageInputParameterBindings> bindingsSet;
	
	private int counter;
	
	private void addOperatorService(PackageOperatorService operatorService){
		operatorServices.put(operatorService.getName(), operatorService);
	}
	
	public PackageInputParameterBindings createNewInputParameterBindings(){
		if(!servicesAddedToModel)
			addServicesToModel();
		
		servicesAddedToModel = true;
	
		ViskoModel paramsModel = new ViskoModel();
		OWLSService owlsService;
		Iterator<String> keysIterator = operatorServices.keySet().iterator();
		String key;
		while(keysIterator.hasNext()){
			key = keysIterator.next();
			owlsService = operatorServices.get(key).getOWLSService();
			
			for(OWLIndividual input : owlsService.getIndividual().getProfile().getInputs()){
				paramsModel.addToModel(input.toRDF(true, false));				
			}
		}		
		
		String bindingsName = "bindings-" + counter++;
		PackageInputParameterBindings bindings = new PackageInputParameterBindings(bindingsName, viskoModel, baseFileURL, paramsModel, operatorServices);
		bindingsSet.add(bindings);
		return bindings;
		
	}
	
	public static void setDataTypesModel(OntModel model){
		dataTypesModel = model;
	}
	
	public PackageWriter(String url, String packageFileName){
		baseURL = url;
		baseFileURL = baseURL + packageFileName;
		fileName = packageFileName;
		
		viskoModel = new ViskoModel();		
		
		viewerSets = new ArrayList<PackageViewerSet>();
		operatorServices = new HashMap<String, PackageOperatorService>();
		bindingsSet = new ArrayList<PackageInputParameterBindings>();
		counter = 0;
		servicesAddedToModel = false;
	}
	
	public PackageOperatorService createNewOperatorService(String name, String operationName){
		PackageOperatorService service = new PackageOperatorService(operationName, viskoModel, baseURL, baseFileURL);
		
		if(name != null)
			service.setName(name);
		else
			service.setName(operationName);		
		
		service.setToolkit(toolkit);
		operatorServices.put(name, service);
		addOperatorService(service);
		return service;
	}
	
	public PackageViewerSet createNewViewerSet(String name){
		PackageViewerSet viewerSet =  new PackageViewerSet(name, viskoModel, baseFileURL);
		viewerSets.add(viewerSet);
		return viewerSet;
	}

	public Toolkit createNewToolkit(String name){
		toolkit = new Toolkit(baseFileURL, name, viskoModel);
		return toolkit;
	}
	
	public static OntResource getDataType(String dataTypeURI){
		try{
			OntResource dataTypeResource = dataTypesModel.getOntResource(dataTypeURI);
			return dataTypeResource;
		}catch(Exception e){
			System.out.println("You are referencing a data type and did\n"
					+ "not provide an ontology in the package directory\n"
					+ "dataTypes or your referenced data type is not contained\n"
					+ "in the ontology. URI being referenced: " + dataTypeURI);
			e.printStackTrace();
		}
		return null;
	}
	
	public static Format getFormat(String formatURI){
		return new Format(formatURI, loadingModel);
	}
		
	public static VisualizationAbstraction getView(String viewURI){
		return new VisualizationAbstraction(viewURI, loadingModel);
	}
	
	private void addViewerSetsToModel(){
		for(PackageViewerSet viewerSet : viewerSets){
			viewerSet.addToModel();
		}
	}

	private void addServicesToModel(){
		Iterator<String> keysIterator = operatorServices.keySet().iterator();
		String key;
		while(keysIterator.hasNext()){
			key = keysIterator.next();
			operatorServices.get(key).addToModel();
		}

	}
	
	private void addBindingsToModel(){
		for(PackageInputParameterBindings bindingSet : bindingsSet){
			bindingSet.addToModel();
		}
	}
	
	private void addToModel(){
		addServicesToModel();		
		if(toolkit != null)
			toolkit.getIndividual();
		
		addViewerSetsToModel();
		addBindingsToModel();
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
		Iterator<String> keysIterator = operatorServices.keySet().iterator();
		String key;
		while(keysIterator.hasNext()){
			key = keysIterator.next();
			owlsService = operatorServices.get(key).getOWLSService();
			manager.saveDocument(owlsService.getModel().getModelAsRDFString(), owlsService.getFileName());
		}		
	}
}