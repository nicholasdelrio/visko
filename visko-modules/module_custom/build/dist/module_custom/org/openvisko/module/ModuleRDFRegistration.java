package org.openvisko.module;

import java.net.URL;

import com.hp.hpl.jena.ontology.OntResource;

import org.openvisko.module.registration.AbstractModuleRDFRegistration;
import org.openvisko.module.registration.ModuleInputParameterBindings;
import org.openvisko.module.registration.ModuleOperatorService;
import org.openvisko.module.registration.ModuleWriter;

import edu.utep.trustlab.visko.ontology.pmlp.Format;
import edu.utep.trustlab.visko.ontology.viskoService.Toolkit;

public class ModuleRDFRegistration extends AbstractModuleRDFRegistration {

	private static final class Resources {
		//formats
		private static final Format spaceSeparatedValues = ModuleWriter.getFormat("http://openvisko.org/rdf/pml2/formats/SPACESEPARATEDVALUES.owl#SPACESEPARATEDVALUES");
		private static final Format commaSeparatedvalues = ModuleWriter.getFormat("http://openvisko.org/rdf/pml2/formats/CSV.owl#CSV");
		private static final Format littleEndianSequence = ModuleWriter.getFormat("http://openvisko.org/rdf/pml2/formats/LITTLE-ENDIAN-SEQUENCE.owl#LITTLE-ENDIAN-SEQUENCE");

		private static final Format rdfxml = ModuleWriter.getFormat("http://openvisko.org/rdf/pml2/formats/RDFXML.owl#RDFXML");
		private static final Format json = ModuleWriter.getFormat("http://openvisko.org/rdf/pml2/formats/JSON.owl#JSON");
		private static final Format sparqlResultsXML = ModuleWriter.getFormat("http://openvisko.org/rdf/pml2/formats/SPARQLRESULTSXML.owl#SPARQLRESULTSXML");
		
		//semantic type uris
		private static final OntResource xyzData = ModuleWriter.getDataType("http://rio.cs.utep.edu/ciserver/ciprojects/CrustalModeling/CrustalModeling.owl#d18");
		private static final OntResource fieldTrimmedXYZData = ModuleWriter.getDataType("http://rio.cs.utep.edu/ciserver/ciprojects/CrustalModeling/CrustalModeling.owl#FieldTrimmedXYZData");
		
		private static final OntResource velocityDataURI_1 = ModuleWriter.getDataType("http://rio.cs.utep.edu/ciserver/ciprojects/HolesCode/HolesCodeSAW3.owl#d14-0");
		private static final OntResource velocityDataURI_2 = ModuleWriter.getDataType("http://rio.cs.utep.edu/ciserver/ciprojects/HolesCode/HolesCodeWDO.owl#d2");
		private static final OntResource velocityDataURI_3 = ModuleWriter.getDataType("http://rio.cs.utep.edu/ciserver/ciprojects/HolesCode/HolesCodeSAW3.owl#d2-1");
		
		private static final OntResource griddedTimeURI_1 = ModuleWriter.getDataType("http://rio.cs.utep.edu/ciserver/ciprojects/HolesCode/HolesCodeSAW3.owl#d4-0");
		private static final OntResource griddedTimeURI_2 = ModuleWriter.getDataType("http://rio.cs.utep.edu/ciserver/ciprojects/HolesCode/HolesCodeWDO.owl#d4");
		
		private static final OntResource dusumDataURI_1 = ModuleWriter.getDataType("http://rio.cs.utep.edu/ciserver/ciprojects/HolesCode/HolesCodeSAW3.owl#d8-0");
		private static final OntResource dusumDataURI_2 = ModuleWriter.getDataType("http://rio.cs.utep.edu/ciserver/ciprojects/HolesCode/HolesCodeWDO.owl#d8");
		
		
		//data types
		private static final OntResource array1DFloat = ModuleWriter.getDataType("http://rio.cs.utep.edu/ciserver/ciprojects/HolesCode/HolesCodeSAW3.owl#Array1DFloat");
		private static final OntResource array1DInteger = ModuleWriter.getDataType("http://rio.cs.utep.edu/ciserver/ciprojects/HolesCode/HolesCodeSAW3.owl#Array1DInteger");
		private static final OntResource array1DUnsignedShortInteger = ModuleWriter.getDataType("http://rio.cs.utep.edu/ciserver/ciprojects/HolesCode/HolesCodeSAW3.owl#Array1DUnsignedShortInteger");
		
		private static final OntResource viskoDataTransformationPaths = ModuleWriter.getDataType("http://openvisko.org/rdf/ontology/visko.owl#VisKo_DataTransformationPaths");
		private static final OntResource viskoOperatorPaths = ModuleWriter.getDataType("http://openvisko.org/rdf/ontology/visko.owl#VisKo_OperatorPaths");
		private static final OntResource viskoInstanceSummary = ModuleWriter.getDataType("http://openvisko.org/rdf/ontology/visko.owl#VisKo_InstanceSummary");
		private static final OntResource viskoKnoweldgeBase = ModuleWriter.getDataType("http://openvisko.org/rdf/ontology/visko.owl#VisKo_KnowledgeBase");
		
		private static final OntResource generalD3Graph = ModuleWriter.getDataType("http://d3js.org/d3.owl#Graph");
		
		private static final OntResource gravityData = ModuleWriter.getDataType("http://rio.cs.utep.edu/ciserver/ciprojects/CrustalModeling/CrustalModeling.owl#d19");
		private static final OntResource mbsf = ModuleWriter.getDataType("http://data.oceandrilling.org#mbsf");
	}

	@Override
	public void populateServices() {
		String wsdlURL = getWSDLURL();
		
		String operationName = "csv2tabular";
		ModuleOperatorService service1 = getModuleWriter().createNewOperatorService(null, operationName);
		service1.setComment("Convert comma separated values into ASCII tabular data");
		service1.setLabel(operationName);
		service1.setWSDLURL(wsdlURL);
		service1.setInputFormat(Resources.commaSeparatedvalues);
		service1.setOutputFormat(Resources.spaceSeparatedValues);
		
		operationName = "XYZDataFieldFilter";
		ModuleOperatorService service2 = getModuleWriter().createNewOperatorService(null, operationName);
		service2.setComment("Filter out field (i.e., columns) from gravity datasets");
		service2.setLabel(operationName);
		service2.setWSDLURL(wsdlURL);
		service2.setInputFormat(Resources.spaceSeparatedValues);
		service2.setOutputFormat(Resources.spaceSeparatedValues);
		service2.setInputDataType(Resources.xyzData);
		service2.setOutputDataType(Resources.fieldTrimmedXYZData);
		
		operationName = "int2Short";
		ModuleOperatorService service3 = getModuleWriter().createNewOperatorService(null, operationName);
		service3.setInputFormat(Resources.littleEndianSequence);
		service3.setOutputFormat(Resources.littleEndianSequence);
		service3.setLabel(operationName);
		service3.setComment("Converts integer arrays to unsigned short integers");
		service3.setWSDLURL(wsdlURL);
		service3.setInputDataType(Resources.array1DInteger);
		service3.setOutputDataType(Resources.array1DUnsignedShortInteger);
		
		operationName = "float2ShortThr";
		ModuleOperatorService service4 = getModuleWriter().createNewOperatorService(null, operationName);
		service4.setInputFormat(Resources.littleEndianSequence);
		service4.setOutputFormat(Resources.littleEndianSequence);
		service4.setLabel(operationName);
		service4.setComment("Converts float arrays to short ints");
		service4.setWSDLURL(wsdlURL);
		service4.setInputDataType(Resources.array1DFloat);
		service4.setOutputDataType(Resources.array1DUnsignedShortInteger);
		
		operationName = "jsonGraph_OperatorPaths";
		ModuleOperatorService service5 = getModuleWriter().createNewOperatorService(null, operationName);
		service5.setInputFormat(Resources.rdfxml);
		service5.setOutputFormat(Resources.json);
		service5.setLabel(operationName);
		service5.setComment("Converts a Visko KB to Operator Paths encoded in JSON");
		service5.setWSDLURL(wsdlURL);
		service5.setInputDataType(Resources.viskoKnoweldgeBase);
		service5.setOutputDataType(Resources.viskoOperatorPaths);
		
		operationName = "jsonGraph_DataTransformations";
		ModuleOperatorService service6 = getModuleWriter().createNewOperatorService(null, operationName);
		service6.setInputFormat(Resources.rdfxml);
		service6.setOutputFormat(Resources.json);
		service6.setLabel(operationName);
		service6.setComment("Converts a Visko KB to Data Transformation Paths encoded in JSON");
		service6.setWSDLURL(wsdlURL);
		service6.setInputDataType(Resources.viskoKnoweldgeBase);
		service6.setOutputDataType(Resources.viskoDataTransformationPaths);
		
		operationName = "jsonBars_Instances";
		ModuleOperatorService service7 = getModuleWriter().createNewOperatorService(null, operationName);
		service7.setInputFormat(Resources.rdfxml);
		service7.setOutputFormat(Resources.json);
		service7.setLabel(operationName);
		service7.setComment("Converts a Visko KB to instance summary encoded in JSON");
		service7.setWSDLURL(wsdlURL);
		service7.setInputDataType(Resources.viskoKnoweldgeBase);
		service7.setOutputDataType(Resources.viskoInstanceSummary);
		
		operationName = "sPARQLResultsToJSONGraph";
		ModuleOperatorService service8 = getModuleWriter().createNewOperatorService(null, operationName);
		service8.setInputFormat(Resources.sparqlResultsXML);
		service8.setOutputFormat(Resources.json);
		service8.setLabel(operationName);
		service8.setComment("Converts sparql query result triple (s p o) to a simple graph in JSON");
		service8.setWSDLURL(wsdlURL);
		service8.setOutputDataType(Resources.generalD3Graph);
	}

	@Override
	public void populateToolkit() {
		Toolkit toolkit = getModuleWriter().createNewToolkit("custom");
		toolkit.setComment("Custom");
		toolkit.setLabel("Custom");
		
		String stringURL = "http://trust.utep.edu/members/nick/";
		try
		{
			URL toolkitURL = new URL(stringURL);
			toolkit.setDocumentationURL(toolkitURL);
		}
		catch(Exception e){e.printStackTrace();}
	}

	@Override
	public void populateViewerSets() {
	}

	@Override
	public void populateParameterBindings() {
		ModuleInputParameterBindings bindings1 = getModuleWriter().createNewInputParameterBindings();
		ModuleInputParameterBindings bindings2 = getModuleWriter().createNewInputParameterBindings();
		ModuleInputParameterBindings bindings3 = getModuleWriter().createNewInputParameterBindings();
		ModuleInputParameterBindings bindings4 = getModuleWriter().createNewInputParameterBindings();

		addGravityDataBindings(bindings1);
		addDuSumParameterBindings(bindings2);
		addGriddedTimeParameterBindings(bindings3);
		addMBSFParameterBindings(bindings4);
	}

	private void addGravityDataBindings(ModuleInputParameterBindings bindingsSet){
		bindingsSet.addSemanticType(Resources.gravityData);
		
		// for GMT surface
		bindingsSet.addInputBinding("XYZDataFieldFilter", "indexOfX", "0");
		bindingsSet.addInputBinding("XYZDataFieldFilter", "indexOfY", "1");
		bindingsSet.addInputBinding("XYZDataFieldFilter", "indexOfZ", "2");
	}

	private void addMBSFParameterBindings(ModuleInputParameterBindings bindingsSet){
		bindingsSet.addSemanticType(Resources.mbsf);
		
		// for GMT surface
		bindingsSet.addInputBinding("XYZDataFieldFilter", "indexOfX", "0");
		bindingsSet.addInputBinding("XYZDataFieldFilter", "indexOfY", "1");
		bindingsSet.addInputBinding("XYZDataFieldFilter", "indexOfZ", "-1");
	}
	
	public void addGriddedTimeParameterBindings(ModuleInputParameterBindings bindingsSet){
		bindingsSet.addSemanticType(Resources.griddedTimeURI_1);
		bindingsSet.addSemanticType(Resources.griddedTimeURI_2);
		
		bindingsSet.addSemanticType(Resources.velocityDataURI_1);
		bindingsSet.addSemanticType(Resources.velocityDataURI_2);
		bindingsSet.addSemanticType(Resources.velocityDataURI_3);
		
		// for float2shortThr
		bindingsSet.addInputBinding("float2ShortThr", "scalingFactor", "1000");
		bindingsSet.addInputBinding("float2ShortThr", "offset", "0");
	}

	public void addDuSumParameterBindings(ModuleInputParameterBindings bindingsSet){
		bindingsSet.addSemanticType(Resources.dusumDataURI_1);
		bindingsSet.addSemanticType(Resources.dusumDataURI_2);
		
		// for float2shortThr
		bindingsSet.addInputBinding("float2ShortThr", "scalingFactor", "1");
		bindingsSet.addInputBinding("float2ShortThr", "offset", "10000");
	}
}
