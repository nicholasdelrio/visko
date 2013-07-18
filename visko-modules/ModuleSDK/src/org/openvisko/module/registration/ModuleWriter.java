package org.openvisko.module.registration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.mindswap.owl.OWLIndividual;
import org.openvisko.module.util.ModuleNameProperty;
import org.openvisko.module.util.ServerProperties;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntResource;

import edu.utep.trustlab.contentManagement.ContentManager;
import edu.utep.trustlab.visko.ontology.model.ViskoModel;
import edu.utep.trustlab.visko.ontology.pmlp.Format;
import edu.utep.trustlab.visko.ontology.viskoService.OWLSService;
import edu.utep.trustlab.visko.ontology.viskoService.Toolkit;
import edu.utep.trustlab.visko.ontology.viskoService.Module;
import edu.utep.trustlab.visko.ontology.viskoView.VisualizationAbstraction;

public class ModuleWriter {
	
	private static ViskoModel loadingModel = new ViskoModel();
	private static OntModel dataTypesModel;
	
	private String baseURL;
	private String baseFileURL;
	private String fileName;
	
	private ViskoModel viskoModel;
	private Toolkit toolkit;
	private Module module;
	
	private boolean servicesAddedToModel;
	
	private ArrayList<ModuleViewerSet> viewerSets;
	private HashMap<String, ModuleOperatorService> operatorServices;
	private ArrayList<ModuleInputParameterBindings> bindingsSet;
	
	private int counter;
	
	private void addOperatorService(ModuleOperatorService operatorService){
		operatorServices.put(operatorService.getName(), operatorService);
	}
	
	public ModuleInputParameterBindings createNewInputParameterBindings(){
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
		ModuleInputParameterBindings bindings = new ModuleInputParameterBindings(bindingsName, viskoModel, baseFileURL, paramsModel, operatorServices);
		bindingsSet.add(bindings);
		return bindings;
		
	}
	
	public static void setDataTypesModel(OntModel model){
		dataTypesModel = model;
	}
	
	public ModuleWriter(String url, String packageFileName){
		baseURL = url;
		baseFileURL = baseURL + packageFileName;
		fileName = packageFileName;
		
		viskoModel = new ViskoModel();		
		
		viewerSets = new ArrayList<ModuleViewerSet>();
		operatorServices = new HashMap<String, ModuleOperatorService>();
		bindingsSet = new ArrayList<ModuleInputParameterBindings>();
		counter = 0;
		servicesAddedToModel = false;
	}
	
	public ModuleOperatorService createNewOperatorService(String name, String operationName){
		ModuleOperatorService service = new ModuleOperatorService(operationName, viskoModel, baseURL, baseFileURL);
		
		if(name != null)
			service.setName(name);
		else
			service.setName(operationName);		
		
		service.setToolkit(toolkit);
		operatorServices.put(name, service);
		addOperatorService(service);
		return service;
	}
	
	public ModuleViewerSet createNewViewerSet(String name){
		ModuleViewerSet viewerSet =  new ModuleViewerSet(name, viskoModel, baseFileURL);
		viewerSets.add(viewerSet);
		return viewerSet;
	}

	public Toolkit createNewToolkit(String name){
		toolkit = new Toolkit(baseFileURL, name, viskoModel);
		createNewModule();
		return toolkit;
	}
	
	private void createNewModule(){
		module = new Module(baseFileURL, ModuleNameProperty.getInstance().getName(), viskoModel);
		module.setToolkit(toolkit);
		module.setLabel(ModuleNameProperty.getInstance().getName());
		module.setDocumentationURL(ServerProperties.getInstance().getModuleHTMLDescription());
		module.setSourceCodeURL(ServerProperties.getInstance().getModuleSourceCode());
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
		for(ModuleViewerSet viewerSet : viewerSets){
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
		for(ModuleInputParameterBindings bindingSet : bindingsSet){
			bindingSet.addToModel();
		}
	}
	
	private void addToModel(){
		addServicesToModel();		
		if(toolkit != null){
			toolkit.getIndividual();
			module.getIndividual();
		}

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