/*
Copyright (c) 2012, University of Texas at El Paso
All rights reserved.
Redistribution and use in source and binary forms, with or without modification, are permitted
provided that the following conditions are met:

	-Redistributions of source code must retain the above copyright notice, this list of conditions
	 and the following disclaimer.
	-Redistributions in binary form must reproduce the above copyright notice, this list of conditions
	 and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.*/

package edu.utep.trustlab.visko.execution;

import org.mindswap.exceptions.ExecutionException;
import org.mindswap.owl.OWLDataValue;
import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owl.OWLValue;
import org.mindswap.owls.OWLSFactory;
import org.mindswap.owls.process.Process;
import org.mindswap.owls.process.execution.ProcessExecutionEngine;
import org.mindswap.owls.process.variable.Input;
import org.mindswap.owls.process.variable.Output;
import org.mindswap.owls.service.Service;
import org.mindswap.query.ValueMap;

import edu.utep.trustlab.visko.execution.provenance.PMLNodesetLogger;
import edu.utep.trustlab.visko.execution.provenance.PMLQueryLogger;
import edu.utep.trustlab.visko.ontology.service.OWLSService;
import edu.utep.trustlab.visko.util.OWLSParameterBinder;

public class PipelineExecutor implements Runnable {	

    private PipelineExecutorJob job;
	private ProcessExecutionEngine exec;
    private Thread t;
    
    private PMLNodesetLogger traceLogger;
	
    //use our own interrupt facility, since calling Thread.interrupt() leaves Jena in a crappy unusable state
    private boolean isScheduledForTermination;
    
	public PipelineExecutor(PipelineExecutorJob pipelineJob) {
		job = pipelineJob;
		
		if(job.getProvenanceLogging())
			traceLogger = new PMLNodesetLogger();

    	job.getJobStatus().setTotalServiceCount(job.getPipeline().size());
	  	exec = OWLSFactory.createExecutionEngine();	
	  	isScheduledForTermination = false;
	}

	public PipelineExecutorJob getJob(){
		return job;
	}
	
	public boolean isAlive(){
		return t.isAlive();
	}
	
	public void scheduleForTermination(){
		isScheduledForTermination = true;
	}
	
	public boolean isScheduledForTermination(){
		return isScheduledForTermination;
	}

	public void process(){
		if(job.getJobStatus().getPipelineState() ==  PipelineExecutorJobStatus.PipelineState.NEW){
			t = new Thread(this);
			t.setDaemon(true);
			t.start();
		}	
	}
	
    public void run(){
    	try{
    	
    		if(!job.getPipeline().hasInputData())
    			job.getJobStatus().setPipelineState(PipelineExecutorJobStatus.PipelineState.NODATA);
    	
    		else if(job.getPipeline().isEmpty())
    			job.getJobStatus().setPipelineState(PipelineExecutorJobStatus.PipelineState.EMPTYPIPELINE);
    	
    		else{
    			executePipeline();
    		
    			if(job.getProvenanceLogging())
    				dumpProvenance();

    			job.getJobStatus().setPipelineState(PipelineExecutorJobStatus.PipelineState.COMPLETE);
    		}
    	}
    	catch(Exception e){
    		e.printStackTrace();
    		job.getJobStatus().setPipelineState(PipelineExecutorJobStatus.PipelineState.ERROR);
    	}
    }
   
    private void executePipeline() throws ExecutionException {		
		job.getJobStatus().setPipelineState(PipelineExecutorJobStatus.PipelineState.RUNNING);
    	String resultURL = job.getPipeline().getArtifactURL();
    	
    	System.out.println(job.getJobStatus());
    	    	
    	for(int i = 0; i < job.getPipeline().size(); i ++){    		
    		OWLSService viskoService = job.getPipeline().getService(i);
 
        	//capture initial dataset
            if(job.getProvenanceLogging() && i == 0)
            	traceLogger.captureInitialDataset(resultURL, viskoService);
    		
       		job.getJobStatus().setCurrentService(viskoService.getURI(), i);
    		System.out.println(job.getJobStatus());
    		
    		resultURL = executeService(viskoService, resultURL, i);
    		
    		if(isScheduledForTermination){
    			System.out.println("This thread's execution was interrupted and will quit!");
    	    	job.getJobStatus().setPipelineState(PipelineExecutorJobStatus.PipelineState.INTERRUPTED);
    	    	break;
    		}
    	}
    	
    	job.setFinalResultURL(resultURL);
    }
    
    private void dumpProvenance(){
    	//dump PML nodeset trace
    	String pmlNodesetURI = traceLogger.dumpNodesets();
    	
    	// dump PMLQuery
    	PMLQueryLogger queryLogger = new PMLQueryLogger();
    	queryLogger.setViskoQuery(job.getPipeline().getParentPipelineSet().getQuery().toString());
    	queryLogger.addAnswer(pmlNodesetURI);
    	String pmlQueryURI = queryLogger.dumpPMLQuery();
    	
    	//set URIs on Job
    	job.setPMLNodesetURI(pmlNodesetURI);
    	job.setPMLQueryURI(pmlQueryURI);
    }
    
    private String executeService(OWLSService owlsService, String inputDataURL, int serviceIndex) throws ExecutionException{		
		OWLKnowledgeBase kb = OWLFactory.createKB();
		Service service = owlsService.getIndividual();
		Process process = service.getProcess();

		ValueMap<Input, OWLValue> inputs = OWLSParameterBinder.buildInputValueMap(process, inputDataURL, job.getPipeline().getParameterBindings(), kb);
		
		String outputDataURL = null;
		
		if (inputs != null){		
        	ValueMap<Output, OWLValue> outputs;
        	
			outputs = exec.execute(process, inputs, kb);
	        OWLDataValue out = (OWLDataValue) outputs.getValue(process.getOutput());
	        outputDataURL =  out.toString();

	        if(job.getProvenanceLogging())
	        	traceLogger.captureProcessingStep(owlsService, inputDataURL, outputDataURL, inputs);
		}
		return outputDataURL;
    }
}