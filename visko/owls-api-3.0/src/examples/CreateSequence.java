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

package examples;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owl.OWLOntology;
import org.mindswap.owl.OWLValue;
import org.mindswap.owls.OWLSFactory;
import org.mindswap.owls.grounding.AtomicGrounding;
import org.mindswap.owls.grounding.WSDLAtomicGrounding;
import org.mindswap.owls.grounding.WSDLGrounding;
import org.mindswap.owls.process.AtomicProcess;
import org.mindswap.owls.process.CompositeProcess;
import org.mindswap.owls.process.Perform;
import org.mindswap.owls.process.Process;
import org.mindswap.owls.process.Result;
import org.mindswap.owls.process.Sequence;
import org.mindswap.owls.process.execution.ProcessExecutionEngine;
import org.mindswap.owls.process.variable.Input;
import org.mindswap.owls.process.variable.Output;
import org.mindswap.owls.profile.Profile;
import org.mindswap.owls.service.Service;
import org.mindswap.owls.vocabulary.OWLS;
import org.mindswap.query.ValueMap;
import org.mindswap.utils.URIUtils;
import org.mindswap.utils.Utils;

/**
 * An example to show how service descriptions can be created on the fly,
 * saved and executed.
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:53 $
 */
public class CreateSequence {
	public static final URI baseURI = URI.create("http://www.example.org/BookPrice.owl#");

	OWLOntology ont;

	/**
	 *
	 * Create a new Sequence from the processes of the given services and put them in a new
	 * Service object with a automatically generated Profile. This function assumes that
	 * each service in the list has exactly one input and one output (except the first and
	 * last one) such that in the resulting Service the output of each service will be fed
	 * as input to the next one. The first service does not have to have an input and the
	 * last one does not need to have an output. The resulting service will have an input
	 * (or an output) depending on this.
	 *
	 * @param services List of Service objects
	 * @return The Service which is a Sequence of the given services
	 */
	Service createSequenceService(final List<Service> services) {
		final Service service = ont.createService(URIUtils.createURI(baseURI, "BookService"));
		final CompositeProcess process = ont.createCompositeProcess(URIUtils.createURI(baseURI, "BookProcess"));
		final Profile profile = ont.createProfile(URIUtils.createURI(baseURI, "BookProfile"));
		final WSDLGrounding grounding = ont.createWSDLGrounding(URIUtils.createURI(baseURI, "BookGrounding"));

		System.out.println(ont.getKB().getServices(false));

		createSequenceProcess(process, services);
		createProfile(profile, process);

		final OWLIndividualList<Process> list = process.getComposedOf().getAllProcesses(false);
		for (Process pc : list)
		{
			if (pc instanceof AtomicProcess)
			{
				final AtomicGrounding<?> ag = ((AtomicProcess) pc).getGrounding();
				if (ag == null) continue;
				grounding.addGrounding(ag.castTo(WSDLAtomicGrounding.class));
			}
		}

		profile.setLabel(createLabel(services), null);
		profile.setTextDescription(profile.getLabel(null));

		service.addProfile(profile);
		service.setProcess(process);
		service.addGrounding(grounding);
		return service;
	}

	/**
	 * Create a label for the composite service based on the labels of services.
	 * Basically return the string [Service1 + Service2 + ... + ServiceN] as the
	 * label.
	 */
	String createLabel(final List<Service> services) {
		String label = "[";

		for(int i = 0; i < services.size(); i++) {
			final Service s = services.get(i);

			if(i > 0) label += " + ";

			label += s.getLabel(null);
		}
		label += "]";

		return label;
	}

	/**
	 * Create a Profile for the composite service. We only set the input and
	 * output of the profile based on the process.
	 */
	Profile createProfile(final Profile profile, final Process process)
	{
		for (Input input : process.getInputs())
		{
			profile.addInput(input);
		}

		for (Output output : process.getOutputs())
		{
			profile.addOutput(output);
		}

		return profile;
	}

	/**
	 * Create a Sequence process for the processes of given services. Creates
	 * the data flow assuming each service has one output and one input (except
	 * first and last one).
	 */
	CompositeProcess createSequenceProcess(final CompositeProcess compositeProcess, final List<Service> services) {
		final Sequence sequence = ont.createSequence(null);
		compositeProcess.setComposedOf(sequence);

		final Perform[] performs = new Perform[services.size()];
		for (int i = 0; i < services.size(); i++) {
			final Service s = services.get(i);
			final Process p = s.getProcess();

			performs[i] = ont.createPerform(null);
			performs[i].setProcess(p);

			sequence.addComponent(performs[i]);

			if (i > 0) {
				final Perform prevPerform = performs[i - 1];
				final Input input = p.getInputs().get(0);
				final Output output = prevPerform.getProcess().getOutputs().get(0);

				// the value of 'input' is the value of 'output' from 'prevPerform'
				performs[i].addBinding(input, prevPerform, output);
			}
		}

		final Perform firstPerform = performs[0];
		final Perform lastPerform = performs[services.size()-1];
		final boolean createInput = firstPerform.getProcess().getInputs().size() > 0;
		final boolean createOutput = lastPerform.getProcess().getOutputs().size() > 0;

		if (createInput) {
			final Input input = firstPerform.getProcess().getInputs().get(0);
			final Input newInput = ont.createInput(URIUtils.createURI(baseURI, "TestInput"));
			newInput.setLabel(input.getLabel(null), null);
			newInput.setParamType(input.getParamType());
			newInput.setProcess(compositeProcess);

			// input of the first perform is directly read from the input of the composite process
			performs[0].addBinding(input, OWLS.Process.ThisPerform, newInput);
		}

		if (createOutput) {
			final Output output = lastPerform.getProcess().getOutputs().get(0);
			final Output newOutput = ont.createOutput(URIUtils.createURI(baseURI, "TestOutput"));
			newOutput.setLabel(output.toPrettyString(), null);
			newOutput.setParamType(output.getParamType());
			newOutput.setProcess(compositeProcess);

			// the output of the composite process is the output pf last process
			final Result result = ont.createResult(null);
			result.addBinding(newOutput, lastPerform, output);

			compositeProcess.addResult(result);
		}

		return compositeProcess;
	}


	public void runTest() throws Exception {
		// create an OWL-S knowledge base
		final OWLKnowledgeBase kb = OWLFactory.createKB();

		// create an empty ontology in this KB
		ont = kb.createOntology(URIUtils.standardURI(baseURI));

		// create an execution engine
		final ProcessExecutionEngine exec = OWLSFactory.createExecutionEngine();

		// load two services
		final Service s1 = kb.readService(ExampleURIs.BOOK_FINDER_OWLS12);
		final Service s2 = kb.readService(ExampleURIs.BN_BOOK_PRICE_OWLS12);

		// put the services in a list
		final List<Service> services = new ArrayList<Service>();
		services.add(s1);
		services.add(s2);

		// create a new service as a sequence of the list
		final Service s = createSequenceService(services);

		// print the description of new service to standard output
		ont.write(System.out, baseURI);
		System.out.println();

		// get the process of the new service
		final Process process = s.getProcess();
		// initialize the input values to be empty
		ValueMap<Input, OWLValue> inputs = new ValueMap<Input, OWLValue>();
		// get the parameter using the local name
		inputs.setValue(process.getInputs().get(0), kb.createDataValue("City of Glass"));

		// execute the service
		System.out.print("Executing...");
		ValueMap<Output, OWLValue> outputs = exec.execute(process, inputs, kb);
		System.out.println("done");

		// get the output parameter using the index
		final OWLIndividual outValue = outputs.getIndividualValue(process.getOutput());

		// display the result
		System.out.println("Book Price = ");
		System.out.println(Utils.formatRDF(outValue.toRDF(true, true)));
		System.out.println();
	}

	public static void main(final String[] args) throws Exception {
		final CreateSequence test = new CreateSequence();
		test.runTest();
	}
}
