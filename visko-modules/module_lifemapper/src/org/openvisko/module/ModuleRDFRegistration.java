package org.openvisko.module;

import java.net.URL;

import org.openvisko.module.registration.AbstractModuleRDFRegistration;
import org.openvisko.module.registration.ModuleInputParameterBindings;
import org.openvisko.module.registration.ModuleOperatorService;
import org.openvisko.module.registration.ModuleWriter;

import com.hp.hpl.jena.ontology.OntResource;

import edu.utep.trustlab.visko.ontology.pmlp.Format;
import edu.utep.trustlab.visko.ontology.viskoService.Toolkit;
import edu.utep.trustlab.visko.ontology.viskoView.VisualizationAbstraction;
import edu.utep.trustlab.visko.ontology.vocabulary.ViskoV;


public class ModuleRDFRegistration extends AbstractModuleRDFRegistration {

	private static final class Resources {
		//formats
		private static final Format tiff = ModuleWriter.getFormat("http://openvisko.org/rdf/pml2/formats/TIFF.owl#TIFF");
		private static final Format html = ModuleWriter.getFormat("http://openvisko.org/rdf/pml2/formats/HTML.owl#HTML");
		
		// visualization abstraction
		private static VisualizationAbstraction speciesMap = ModuleWriter.getView(ViskoV.INDIVIDUAL_URI_2D_SpeciesDistribution_Map);

		//data types
		private static final OntResource snowcover = ModuleWriter.getDataType("http://rgis.unm.edu/edac-data.owl#SnowCover");

	}
	
  @Override
  public void populateViewerSets() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void populateToolkit() {
    // TODO Auto-generated method stub
	  
	  Toolkit tk = getModuleWriter().createNewToolkit("lifemapper");
	  tk.setComment("Life Mapper");
	  tk.setLabel("Life Mapper");
	 
	  try{tk.setDocumentationURL(new URL("http://www.lifemapper.org/"));}
	  catch(Exception e){e.printStackTrace();}
  }
  
  @Override
  public void populateServices() {
    // TODO Auto-generated method stub
    String wsdlURL = this.getWSDLURL();
    
	String operationName = "lifeMapperSimulation";
    ModuleOperatorService service1 = getModuleWriter().createNewOperatorService(null, operationName);
    service1.setWSDLURL(wsdlURL);
    service1.setComment(operationName);
    service1.setLabel(operationName);
    service1.setView(Resources.speciesMap);
    service1.setInputFormat(Resources.tiff);
    service1.setOutputFormat(Resources.html);
  }

	@Override
	public void populateParameterBindings() {
		ModuleInputParameterBindings bindings1 = getModuleWriter().createNewInputParameterBindings();
		addSnowCoverBindings(bindings1);
	}

	private void addSnowCoverBindings(ModuleInputParameterBindings bindingsSet){
		bindingsSet.addSemanticType(Resources.snowcover);

		// for all snow cover simulations
		bindingsSet.addInputBinding("lifeMapperSimulation", "username", "elseweb");
		bindingsSet.addInputBinding("lifeMapperSimulation", "password", "elseweb1");
		bindingsSet.addInputBinding("lifeMapperSimulation", "units", "dd");
		bindingsSet.addInputBinding("lifeMapperSimulation", "algorithm", "BIOCLIM");
	}
}
