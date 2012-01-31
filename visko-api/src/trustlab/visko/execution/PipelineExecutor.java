// The MIT License
//
// Copyright (c) 2004 Evren Sirin
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to
// deal in the Software without restriction, including without limitation the
// rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
// sell copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
// FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
// IN THE SOFTWARE.

/*
 * Created on Mar 19, 2004
 */
package trustlab.visko.execution;

import java.net.URI;
import java.util.HashMap;
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

import trustlab.visko.ontology.service.OWLSService;

//import trustlab.visko.provenance.PMLLogger;

/**
 * 
 * Examples to show how services can be executed. Some examples of simple
 * execution monitoring is included.
 * 
 * @author unascribed
 * @version $Rev: 2298 $; $Author: nick $; $Date: 2012/01/30 20:27:47 $
 */
public class PipelineExecutor {
	private Service service;
	private Process process;
	private String inValue;
	private ValueMap<Output, OWLValue> outputs;
	private ProcessExecutionEngine exec;

	private static final String HARD_CODED_DATA_PML = "http://rio.cs.utep.edu/ciserver/ciprojects/pmlj/HTTP_Subsetter_03730391059904816.owl#answer";

//	private PMLLogger logger;
	private String pmlURI;

	public PipelineExecutor(boolean provenance) {
	/*	if (provenance)
			logger = new PMLLogger();*/
	}

	public URI getURI(String uriString) {
		try {
			return new URI(uriString);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private ValueMap<Input, OWLValue> buildInputValueMap(Process process,
			String datasetURL, HashMap<String, String> bindings,
			OWLKnowledgeBase kb) {
		// initialize the input values to be empty
		ValueMap<Input, OWLValue> inputs = new ValueMap<Input, OWLValue>();

		String value;
		String uri;
		boolean error = false;
		for (Input input : process.getInputs()) {
			uri = input.getURI().toASCIIString();

			System.out.println("getting value for parameter: " + uri);

			if (uri.contains("url") || uri.contains("URL")
					|| uri.contains("fileLoc"))
				inputs.setValue(input, kb.createDataValue(datasetURL));

			else {
				value = bindings.get(input.getURI().toASCIIString());

				if (value != null)
					inputs.setValue(input, kb.createDataValue(value));
				else
					error = true;
			}
		}

		if (error)
			return null;
		else
			return inputs;
	}

	public String executeServiceChain(Pipeline pipeline, String inputDatasetURL)
			throws Exception {
		// create an execution engine
		exec = OWLSFactory.createExecutionEngine();

		// Attach a listener to the execution engine
		// exec.addMonitor(new DefaultProcessMonitor());

		OWLKnowledgeBase kb = OWLFactory.createKB();
		String nextInput = inputDatasetURL;

		for (OWLSService owlsService : pipeline) {
			System.out.println("owl service uri " + owlsService.getURI());

			service = owlsService.getIndividual();

			process = service.getProcess();

			inValue = nextInput;

			ValueMap<Input, OWLValue> inputs = buildInputValueMap(process,
					inValue, pipeline.getParameterBindings(), kb);

			if (inputs == null) {
				System.out
						.println("not all parameters could be bound to a value!");
				System.out.println("cannot execute pipeline!!!");
				return null;
			}

			outputs = exec.execute(process, inputs, kb);

			// get the output
			final OWLDataValue out = (OWLDataValue) outputs.getValue(process
					.getOutput());

			// display the results
			// System.out.println("Executed service '" + service + "'");
			// System.out.println("Grounding WSDL: " + ((AtomicProcess)
			// process).getGrounding().getDescriptionURL());
			// System.out.println("Input  = " + inValue);
			System.out.println("Output URL: " + out.toString());

			manySec(1);

			/*
			if (logger != null)
				logger.captureProcessingStep(owlsService, out.toString(), inputs);*/

			nextInput = out.toString();
		}

		/*
		if (logger != null) {
			if (inputDatasetURL.startsWith("http://giovanni.gsfc.nasa.gov/session"))
				pmlURI = logger.dumpNodesets(HARD_CODED_DATA_PML);
			else
				pmlURI = logger.dumpNodesets(null);

			nextInput = pmlURI;
		}*/

		return nextInput;
	}

	public String getPMLURI() {
		return pmlURI;
	}

	private static void manySec(long s) {
		try {
			Thread.sleep(s * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
