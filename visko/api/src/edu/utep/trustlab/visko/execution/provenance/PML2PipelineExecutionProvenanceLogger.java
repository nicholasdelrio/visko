package edu.utep.trustlab.visko.execution.provenance;


import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.Vector;

import org.inference_web.pml.v2.pmlj.IWInferenceStep;
import org.inference_web.pml.v2.pmlj.IWNodeSet;
import org.inference_web.pml.v2.pmlj.IWQuery;
import org.inference_web.pml.v2.pmlp.IWInformation;
import org.inference_web.pml.v2.util.PMLObjectManager;
import org.inference_web.pml.v2.vocabulary.PMLJ;
import org.inference_web.pml.v2.vocabulary.PMLP;
import org.mindswap.owl.OWLValue;
import org.mindswap.owls.process.variable.Input;
import org.mindswap.query.ValueMap;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import edu.utep.trustlab.contentManagement.ContentManager;
import edu.utep.trustlab.visko.execution.PipelineExecutorJob;
import edu.utep.trustlab.visko.ontology.model.ViskoModel;
import edu.utep.trustlab.visko.ontology.viskoOperator.Transformer;
import edu.utep.trustlab.visko.ontology.viskoService.Service;
import edu.utep.trustlab.visko.util.FileUtils;


public class PML2PipelineExecutionProvenanceLogger implements PipelineExecutionProvenanceLogger {

	/*
     * These two loggers are used to log the provenance of the current job in PML-2.
     */
    private PMLNodesetLogger traceLogger;
    private PMLQueryLogger   queryLogger;
	private String documentURL;
	/*
     * As new Jobs are provided with {@link #setJob(PipelineExecutorJob)}, the previous jobs
     * are stored in this collection.
     */
    private ArrayList<PMLNodesetLogger> nodesetLoggers;

	public PML2PipelineExecutionProvenanceLogger(){
		queryLogger = new PMLQueryLogger();
		documentURL = queryLogger.getDocumentURL();
		nodesetLoggers = new ArrayList<PMLNodesetLogger>();
	}
	
	@Override
	public void recordVisKoQuery(String query) {
		queryLogger.setViskoQuery(query);
	}

	@Override
	public void recordPipelineStart() {
		traceLogger = new PMLNodesetLogger(documentURL);
	}

	@Override
	public void recordInitialDataset(String datasetURL, Service initialService) {
		traceLogger.captureInitialDataset(datasetURL, initialService);
		
	}

	@Override
	public void recordServiceInvocation(Service service, String inDatasetURL,
		String outDatasetURL, ValueMap<Input, OWLValue> inputValueMap) {
		traceLogger.captureProcessingStep(service, inDatasetURL, outDatasetURL, inputValueMap);
	}

	@Override
	public void recordPipelineEnd(PipelineExecutorJob job) {
		// TODO Auto-generated method stub
		nodesetLoggers.add(traceLogger);
	}

	@Override
	public URI finish() {
		// TODO Auto-generated method stub
		for(PMLNodesetLogger logger : nodesetLoggers)
			queryLogger.addAnswer(logger.getRootNodeset());
		
		return queryLogger.dumpPMLQuery();		
	}
	
	private class PMLNodesetLogger {
		
		private Vector<IWNodeSet> serviceNodesets;
		
		private String url;
		private String baseFileName             = "visko-pipeline-provenance";
		private String baseNodesetNameService   = "pipeline-step";
		private String baseNodesetNameParameter = "parameter-assertion";
		
		private String fileName;
			
		/**
		 * 
		 */
		public PMLNodesetLogger(String documentURL) {
			serviceNodesets = new Vector<IWNodeSet>();
			//fileName = baseFileName + "-" + FileUtils.getRandomFileName() + ".owl";
			//String baseURL = ContentManager.getProvenanceContentManager().getBaseURL();
			//url = baseURL + fileName;
			url = documentURL;
		}

		/**
		 * 
		 * @param inDatasetURL
		 * @param ingestingService
		 */
		public void captureInitialDataset(String inDatasetURL, Service ingestingService) {
			IWInferenceStep is = (IWInferenceStep) PMLObjectManager.createPMLObject(PMLJ.InferenceStep_lname);
			is.setHasInferenceRule(  PMLResourceURI.RULE_DIRECT_ASSERTION);
			is.setHasInferenceEngine(PMLResourceURI.ENGINE_VISKO_WEB_SERVICE);
			
			// Set up conclusion
			IWInformation conclusion = (IWInformation) PMLObjectManager.createPMLObject(PMLP.Information_lname);
			String formatURI = ingestingService.getConceptualOperator().getInputFormats().firstElement().getURI();
			conclusion.setHasFormat(formatURI);
			conclusion.setHasURL(inDatasetURL);
			
			// Set up nodeset
			String nodesetNameService = baseNodesetNameService + "-" + FileUtils.getRandomFileName();
			IWNodeSet ns = (IWNodeSet) PMLObjectManager.createPMLObject(PMLJ.NodeSet_lname);
			ns.setIdentifier(PMLObjectManager.getObjectID(url + "#" + nodesetNameService));
			ns.setHasConclusion(conclusion);
			ns.addIsConsequentOf(is);
			
			serviceNodesets.add(ns);
		}
		
		/**
		 * 
		 * @param service
		 * @param inDatasetURL
		 * @param outDatasetURL
		 * @param inputValueMap
		 */
		public void captureProcessingStep(Service service, String inDatasetURL, String outDatasetURL, 
										  ValueMap<Input, OWLValue> inputValueMap) {
			// Set up transformer
			Transformer transformer = new Transformer(service.getConceptualOperator().getURI(), new ViskoModel());
			
			// Set up inference step
			IWInferenceStep is = (IWInferenceStep) PMLObjectManager.createPMLObject(PMLJ.InferenceStep_lname);
			is.setHasInferenceRule(service.getConceptualOperator().getURI());
			is.setHasInferenceEngine(PMLResourceURI.ENGINE_VISKO_WEB_SERVICE);
			
			// Set up conclusion
			IWInformation conclusion = (IWInformation) PMLObjectManager.createPMLObject(PMLP.Information_lname);
			String formatURI = transformer.getOutputFormat().getURI();
			conclusion.setHasFormat(formatURI);
			setConclusionURL(conclusion, outDatasetURL);
			
			// Set up nodeset
			String nodesetNameService = baseNodesetNameService + "-" + FileUtils.getRandomFileName();
			IWNodeSet ns = (IWNodeSet) PMLObjectManager.createPMLObject(PMLJ.NodeSet_lname);
			ns.setIdentifier(PMLObjectManager.getObjectID(url + "#" + nodesetNameService));
			ns.setHasConclusion(conclusion);
			ns.addIsConsequentOf(is);
			
			for (Input var : inputValueMap.getVariables()) {
				OWLValue value = inputValueMap.getValue(var);
				String valueString = value.toString();

				if (!valueString.equals(inDatasetURL)) {				
					// Set up inference step
					IWInferenceStep paramIS = (IWInferenceStep) PMLObjectManager.createPMLObject(PMLJ.InferenceStep_lname);
					paramIS.setHasInferenceRule(PMLResourceURI.RULE_DIRECT_ASSERTION);
					paramIS.setHasInferenceEngine(PMLResourceURI.ENGINE_VISKO_PARAMETER_BINDER);
					
					// Set up conclusion
					IWInformation paramConclusion = (IWInformation) PMLObjectManager.createPMLObject(PMLP.Information_lname);
					paramConclusion.setHasFormat(PMLResourceURI.FORMAT_PLAIN_TEXT);
					paramConclusion.setHasRawString(var.getURI() + " = " + valueString);
					
					// Set up nodeset
					String nodesetNameParameter = baseNodesetNameParameter + "-" + FileUtils.getRandomFileName();
					IWNodeSet paramNS = (IWNodeSet) PMLObjectManager.createPMLObject(PMLJ.NodeSet_lname);
					paramNS.setIdentifier(PMLObjectManager.getObjectID(url + "#" + nodesetNameParameter));
					paramNS.setHasConclusion(paramConclusion);
					paramNS.addIsConsequentOf(paramIS);

					is.addAntecedent(paramNS);
				}
			}
			serviceNodesets.add(ns);
		}

		/**
		 * 
		 * @param conclusion
		 * @param dataURL
		 */
		private void setConclusionURL(IWInformation conclusion, String dataURL) {
			
			ContentManager dataManager = ContentManager.getDataContentManager();
			
			String newURL = dataURL;
			
			if(dataManager != null) {
				newURL = dataManager.saveDocument(dataURL);
			}
			
			conclusion.setHasURL(newURL);
		}
		
		public IWNodeSet getRootNodeset(){
			linkUpSteps();
			IWNodeSet rootNodeset = serviceNodesets.get(serviceNodesets.size() - 1);		
			return rootNodeset;
		}
		
		/**
		 * 
		 * @return
		 */
		public String dumpTrace() {

			linkUpSteps();

			StringWriter writer = new StringWriter();
			IWNodeSet rootNodeset = serviceNodesets.get(serviceNodesets.size() - 1);
			PMLObjectManager.getOntModel(rootNodeset).write(writer, "RDF/XML-ABBREV");
			PMLObjectManager.getOntModel(rootNodeset).close();
			
			ContentManager manager = ContentManager.getProvenanceContentManager();
			manager.saveDocument(writer.toString(), fileName);
			
			return rootNodeset.getIdentifier().getURIString();
		}
		
		/**
		 * 
		 */
		private void linkUpSteps(){
			IWInferenceStep is;
			for (int i = (serviceNodesets.size() - 1); i > 0; i--){
				is = (IWInferenceStep) serviceNodesets.get(i).getIsConsequentOf().get(0);
				is.addAntecedent(serviceNodesets.get(i - 1));
			}		
		}
	}
	
	private class PMLQueryLogger {

		private String  queryName;
		private IWQuery query;
		private OntModel queryModel;
		private String queryDocumentURL;
		
		/**
		 * 
		 */
		public PMLQueryLogger(){
			String baseQueryName = "visko-query";
			queryName = baseQueryName + "-" + FileUtils.getRandomFileName() + ".owl";
			String baseURL = ContentManager.getProvenanceContentManager().getBaseURL();
			queryDocumentURL = baseURL + queryName;
			query = (IWQuery)PMLObjectManager.createPMLObject(PMLJ.Query_lname);
			query.setIdentifier(PMLObjectManager.getObjectID(queryDocumentURL + "#" + "query"));
			queryModel = ModelFactory.createOntologyModel();
		}
		
		public String getDocumentURL(){
			return queryDocumentURL;
		}
		
		/**
		 * 
		 * @return
		 */
		public URI dumpPMLQuery(){
			StringWriter rdfStringWriter = new StringWriter();
						
			queryModel.getBaseModel().add(PMLObjectManager.getOntModel(query).getBaseModel());
			queryModel.getBaseModel().write(rdfStringWriter, "RDF/XML-ABBREV");
			queryModel.close();
			
	  		ContentManager.getProvenanceContentManager().saveDocument(rdfStringWriter.toString(), queryName);
	  		
	  		URI queryURI = null;
	  		try{queryURI = new URI(query.getIdentifier().getURIString());}
	  		catch(Exception e){e.printStackTrace();}
	  		
	  		return queryURI;
		}
		
		/**
		 * 
		 * @param nodeset
		 */
		public void addAnswer(IWNodeSet nodeset){		
			query.addHasAnswer(nodeset.getIdentifier().getURIString());
			queryModel.getBaseModel().add(PMLObjectManager.getOntModel(nodeset).getBaseModel());
		}
				
		/**
		 * 
		 * @param viskoQuery
		 */
		public void setViskoQuery(String viskoQuery){
			// create Information instance as conclusion
	  		IWInformation content = (IWInformation)PMLObjectManager.createPMLObject(PMLP.Information_lname);
	  		content.setHasRawString(viskoQuery);
	  		query.setHasContent(content);
		}
	}
}