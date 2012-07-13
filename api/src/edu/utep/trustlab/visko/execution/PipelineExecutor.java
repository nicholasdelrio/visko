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

import edu.utep.trustlab.visko.planning.Pipeline;
import edu.utep.trustlab.visko.execution.provenance.PMLNodesetLogger;
import edu.utep.trustlab.visko.execution.provenance.PMLQueryLogger;
import edu.utep.trustlab.visko.ontology.service.OWLSService;
import edu.utep.trustlab.visko.util.OWLSParameterBinder;

public class PipelineExecutor implements Runnable {
	private Service service;
	private Process process;
	private ValueMap<Output, OWLValue> outputs;
	private ProcessExecutionEngine exec;
	private Pipeline pipeline;
	private String resultURL;
	
    private boolean complete = false;
    private boolean running = false;
    private String statusMessage = "Pipeline execution has not begun.";
    private Thread t;
    
    //provenance variables
    private PMLNodesetLogger traceLogger;
    private PMLQueryLogger queryLogger;
    private String pmlNodesetURI;
    private String pmlQueryURI;
    private boolean provenance;
	
	public PipelineExecutor(Pipeline aPipeline, boolean captureProvenance) {
		pipeline = aPipeline;
		provenance = captureProvenance;
		
		if(provenance){
			traceLogger = new PMLNodesetLogger();
			queryLogger = new PMLQueryLogger();
		}
	}
	
	public Pipeline getPipeline(){
		return pipeline;
	}

	private static void manySec(double s) {
		try {
			Thread.sleep((long)(s * 1000));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void process(){
		if(!isRunning()){
			this.statusMessage = "Starting pipeline execution.";
			try{
				t = new Thread(this);
				t.setDaemon(true);
				t.start();
                //
                // don't return until the new thread is running.
                //
				while(isRunning() == false){System.out.println("wating for thread to start.");}
			}catch(Exception e){
                e.printStackTrace();
			}
		}
	}
	
    /**
     * Triggers the long running process.
     */
    public void run(){
    	System.out.println("Running process");

		statusMessage = "Kicking off pipeline execution process.";
		complete = false;
		running  = true;

		manySec(0.5);
    	
    	if(pipeline.getArtifactURL() == null){
  			statusMessage = "No input data to process.";
			complete = true;
			running  = false;
			return;
    	}
    	
    	resultURL = pipeline.getArtifactURL();
    	
    	if(pipeline.size() == 0){
 			statusMessage = "Input data is already visualizable by a viewer.";
			complete = true;
			running  = false;
			return;    		
    	}
    	
    	running = true;
    	complete = false;	
    	statusMessage = "Service 0  of " + pipeline.size() + " is running.";
    
    	exec = OWLSFactory.createExecutionEngine();			
    	OWLKnowledgeBase kb = OWLFactory.createKB();

    	for(int i = 0; i < pipeline.size(); i ++){
    		statusMessage = "Service " + pipeline.get(i) + " " + (i + 1) + " of " + pipeline.size() + " is running.";	
    		
    		System.out.println("STATUS: " + this.statusMessage);
    		OWLSService owlsService = pipeline.getService(i);

    		System.out.println("owl service uri " + owlsService.getURI());	
    		service = owlsService.getIndividual();
    		process = service.getProcess();
    					
    		ValueMap<Input, OWLValue> inputs = OWLSParameterBinder.buildInputValueMap(process, resultURL, pipeline.getParameterBindings(), kb);
	
    		if (inputs == null || resultURL == null){    			 			
    			return;
    		}
    		
    		String inputDataURL = resultURL;
    		resultURL = executeService(process, inputs, kb, i);
    		
    		if(provenance)
    			traceLogger.captureProcessingStep(owlsService, inputDataURL, resultURL, inputs);
    		
    		manySec(0.5);
    	}
    	  
    	if(provenance)
    		dumpProvenance();
 
    	complete = true;
    	running  = false;	
    }
    
    private void dumpProvenance(){
    	pmlNodesetURI = traceLogger.dumpNodesets();
    	
    	queryLogger.setViskoQuery(pipeline.getParentPipelineSet().getQuery().toString());
    	queryLogger.addAnswer(pmlNodesetURI);
    	pmlQueryURI = queryLogger.dumpPMLQuery();
    }
    
    private String executeService(Process process, ValueMap<Input,OWLValue> inputs, OWLKnowledgeBase kb, int serviceIndex){
    	try{
			outputs = exec.execute(process, inputs, kb);
			final OWLDataValue out = (OWLDataValue) outputs.getValue(process.getOutput());
			System.out.println("Output URL: " + out.toString());
			return out.toString();
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("error==========================================================");
	    	running = false;
	    	complete = true;	
	    	statusMessage = "Error executing service " + (serviceIndex + 1) + " of " + pipeline.size() + ": " + pipeline.get(serviceIndex);
	    	
			return null;
		}
    }
    
    public String getResultURL(){
    	return resultURL;
    }
    
    public String getPMLNodesetURI(){
    	return pmlNodesetURI;
    }
    
    public String getPMLQueryURI(){
    	return pmlQueryURI;
    }

    public int getPipelineLength(){
        return pipeline.size();
    }

    public boolean isComplete(){
        return complete;
    }

    public boolean isRunning(){
        return this.running;
    }

    public String getStatusMessage(){
        return this.statusMessage;
    }
}