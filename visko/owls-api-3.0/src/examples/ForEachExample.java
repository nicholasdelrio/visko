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
package examples;

import java.net.URI;

import org.mindswap.owl.OWLClass;
import org.mindswap.owl.OWLDataProperty;
import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owl.OWLOntology;
import org.mindswap.owl.OWLValue;
import org.mindswap.owl.list.OWLList;
import org.mindswap.owls.OWLSFactory;
import org.mindswap.owls.process.CompositeProcess;
import org.mindswap.owls.process.ForEach;
import org.mindswap.owls.process.Perform;
import org.mindswap.owls.process.Process;
import org.mindswap.owls.process.execution.ProcessExecutionEngine;
import org.mindswap.owls.process.variable.Input;
import org.mindswap.owls.process.variable.Loc;
import org.mindswap.owls.process.variable.Output;
import org.mindswap.owls.service.Service;
import org.mindswap.owls.vocabulary.OWLS;
import org.mindswap.query.ValueMap;
import org.mindswap.utils.URIUtils;

/**
 * Example to show how to create and execute a forEach control construct.
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:53 $
 */
public class ForEachExample
{
	public void run() throws Exception
	{
		final String ns = "http://www.example.org/test#";

		// print the inputs and outputs during each iteration of the loop
		final ProcessExecutionEngine exec = OWLSFactory.createExecutionEngine();

		final OWLKnowledgeBase kb = OWLFactory.createKB();

		final Service service = kb.readService(ExampleURIs.FIND_LAT_LONG_OWLS12);
		final Process process = service.getProcess();

		final OWLOntology ont = kb.createOntology(URIUtils.standardURI(ns));
		final CompositeProcess cp = ont.createCompositeProcess(null);
		final Input in = ont.createInput(URI.create(ns + "in"));
		in.setParamType(OWLS.ObjectList.List.list());
		cp.addInput(in);

		// create a ForEach construct
		final ForEach forEach = ont.createForEach(null);
		final Loc loopVar = ont.createLoc(URI.create(ns + "loopVar"));
		cp.setComposedOf(forEach);
		forEach.setListValue(OWLS.Process.ThisPerform, in);
		forEach.setLoopVar(loopVar);

		// perform the process by passing the loop variable
		final Perform perform = ont.createPerform(null);
		perform.setProcess(process);
		perform.addBinding(process.getInput(), loopVar);

		forEach.setComponent(perform);

		// display how the construct looks like in RDF/XML
		ont.write(System.out, null);

		// create some zip code values
		final OWLClass ZipCode = kb.getClass(URI.create(ExampleURIs.ONT_ZIPCODE + "ZipCode"));
		final OWLDataProperty zip = kb.getDataProperty(URI.create(ExampleURIs.ONT_ZIPCODE + "zip"));

		final OWLIndividual zip1 = ont.createInstance(ZipCode, null);
		zip1.setProperty(zip, "20740");
		final OWLIndividual zip2 = ont.createInstance(ZipCode, null);
		zip2.setProperty(zip, "11430");
		final OWLIndividual zip3 = ont.createInstance(ZipCode, null);
		zip3.setProperty(zip, "94102");

		OWLList<OWLIndividual> list = ont.createList(OWLS.ObjectList.List);
		list = list.cons(zip3).cons(zip2).cons(zip1);

		final ValueMap<Input, OWLValue> inputs = new ValueMap<Input, OWLValue>();
		inputs.setValue(cp.getInput("in"), list);
		System.out.println("");
		System.out.println("Inputs:");
		System.out.println(list.toRDF(false, false));
		System.out.println("");

		final ValueMap<Output, OWLValue> outputs = exec.execute(cp, inputs, kb);
		System.out.println("Outputs:");
		System.out.println(outputs);
	}

	public static void main(final String[] args) throws Exception
	{
		final ForEachExample test = new ForEachExample();

		test.run();
	}
}
