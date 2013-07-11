package edu.utep.trustlab.visko.execution.provenance;

import java.net.URI;

import org.mindswap.owl.OWLValue;
import org.mindswap.owls.process.variable.Input;
import org.mindswap.query.ValueMap;

import edu.utep.trustlab.visko.execution.PipelineExecutorJob;
import edu.utep.trustlab.visko.ontology.viskoService.Service;

/**
 * The set of important events that occur during the execution of a pipeline.
 * 	
 *	  <query> -> :pipeline_1> -> <initial dataset, service, activity> ()* ... :final dataset_1
 *	             :pipeline_2> -> <initial dataset, service, activity> ()* ... :final dataset_2
 *	             :pipeline_3> -> <initial dataset, service, activity> ()* ... :final dataset_3
 */
public interface PipelineExecutionProvenanceLogger {
	
	/**
	 * 
	 * @param query - the value of the VisKo query that led to this pipeline being executed. 
	 *                e.g. VISUALIZE .. AS ... IN ... WHERE FORMAT = ... AND TYPE = ...
	 */
	public abstract void recordVisKoQuery(String query);
	
	/**
	 * 
	 */
	public abstract void recordPipelineStart();
	
	/**
	 * 
	 * @param inDatasetURL
	 * @param ingestingService
	 */
	public abstract void recordInitialDataset(String datasetURL, Service initialService);
	
	/**
	 * 
	 * @param service
	 * @param inDatasetURL
	 * @param outDatasetURL
	 * @param inputValueMap
	 */
	public abstract void recordServiceInvocation(Service service, String inDatasetURL,
												 String outDatasetURL, 
												 ValueMap<Input, OWLValue> inputValueMap);
	/**
	 * 
	 * @param job
	 */
	public abstract void recordPipelineEnd(PipelineExecutorJob job);
	
	/**
	 * Called when no more provenance is to be recorded for any other pipeline.
	 * 
	 * @param out - the stream to write the provenance to.
	 * @return - the URI of the visko query element from which all other pipeline are associated with
	 */
	public abstract URI finish();
}