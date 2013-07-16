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
import org.mindswap.owl.OWLDataValue;
import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owl.OWLObjectProperty;
import org.mindswap.owl.OWLValue;
import org.mindswap.owls.OWLSFactory;
import org.mindswap.owls.process.AtomicProcess;
import org.mindswap.owls.process.Process;
import org.mindswap.owls.process.execution.DefaultProcessMonitor;
import org.mindswap.owls.process.execution.ProcessExecutionEngine;
import org.mindswap.owls.process.variable.Input;
import org.mindswap.owls.process.variable.Output;
import org.mindswap.owls.service.Service;
import org.mindswap.query.ValueMap;
import org.mindswap.utils.Utils;

/**
 *
 * Examples to show how services can be executed. Some examples of simple
 * execution monitoring is included.
 *
 * @author unascribed
 * @version $Rev: 2298 $; $Author: nick $; $Date: 2011/01/24 06:37:53 $
 */
public class RunService
{
	Service service;
//	Profile profile;
	Process process;
//	WSDLService s;
//	WSDLOperation op;
	String inValue;
	String outValue;
	ValueMap<Input, OWLValue> inputs;
	ValueMap<Output, OWLValue> outputs;
	ProcessExecutionEngine exec;

	public RunService() {
		// create an execution engine
		exec = OWLSFactory.createExecutionEngine();

		// Attach a listener to the execution engine
		exec.addMonitor(new DefaultProcessMonitor());
	}

	public void runZipCode() throws Exception {
		final OWLKnowledgeBase kb = OWLFactory.createKB();

		service = kb.readService(ExampleURIs.ZIP_CODE_FINDER_OWLS12);
		process = service.getProcess();

		// initialize the input values to be empty
		inputs = new ValueMap<Input, OWLValue>();

		inputs.setValue(process.getInput("City"), kb.createDataValue("College Park"));
		inputs.setValue(process.getInput("State"), kb.createDataValue("MD"));

		outputs = exec.execute(process, inputs, kb);

		// get the result
		final OWLIndividual out = outputs.getIndividualValue(process.getOutput());

		// display the results
		System.out.println("Executed service '" + service + "'");
		System.out.println("Grounding WSDL: " + ((AtomicProcess) process).getGrounding().getDescriptionURL());
		System.out.println("City   = " + "College Park");
		System.out.println("State  = " + "MD");
		System.out.println("Output = ");
		System.out.println(Utils.formatRDF(out.toRDF(true, true)));
		System.out.println();
	}

	public void runJGroundingTest() throws Exception {
		final OWLKnowledgeBase kb = OWLFactory.createKB();

		// read the service description
		service = kb.readService(ExampleURIs.JGROUNDING_OWLS12);
		process = service.getProcess();

		// get the parameter using the local name
		inputs = new ValueMap<Input, OWLValue>();
		inputs.setValue(process.getInput("FirstParam"), kb.createDataValue("2"));
		inputs.setValue(process.getInput("SecParam"), kb.createDataValue("3"));

		outputs = exec.execute(process, inputs, kb);

		// do something with output
	}

	public void runBookFinder() throws Exception {
		final OWLKnowledgeBase kb = OWLFactory.createKB();

		// read the service description
		service = kb.readService(ExampleURIs.BOOK_FINDER_OWLS12);
		process = service.getProcess();

		// initialize the input values to be empty
		inputs = new ValueMap<Input, OWLValue>();

		// use any book name
		inValue = "City of Glass";

		// get the parameter using the local name
		inputs.setValue(process.getInput("BookName"), kb.createDataValue(inValue));
		outputs = exec.execute(process, inputs, kb);

		// get the output param using the index
		final OWLIndividual out = outputs.getIndividualValue(process.getOutput());

		// display the results
		System.out.println("Executing OWL-S service " + service);
		System.out.println("Grounding WSDL: " + ((AtomicProcess) process).getGrounding().getDescriptionURL());
		System.out.println("BookName = " + inValue);
		System.out.println("BookInfo = ");
		System.out.println(Utils.formatRDF(out.toRDF(true, true)));
		System.out.println();
	}

	public void runBookPrice() throws Exception {
		final OWLKnowledgeBase kb = OWLFactory.createKB();

		// read the service description
		service = kb.readService(ExampleURIs.BOOK_PRICE_OWLS12);
		process = service.getProcess();

		// initialize the input values to be empty
		inputs = new ValueMap<Input, OWLValue>();

		// use an arbitrary book name
		inValue = "City of Glass";
		// get the parameter using the local name
		inputs.setValue(process.getInput("BookName"), kb.createDataValue(inValue));
		inputs.setValue(process.getInput("Currency"),
			kb.getIndividual(URI.create(ExampleURIs.ONT_CURRENCIES + "EUR")));
		outputs = exec.execute(process, inputs, kb);

		// get the output param using the index
		final OWLIndividual out = outputs.getIndividualValue(process.getOutput());

		// display the results
		System.out.println("Executed service " + service);
		System.out.println("Book Name = " + inValue);
		System.out.println("Price = ");
		System.out.println(Utils.formatRDF(out.toRDF(true, true)));
		System.out.println();
	}

	public void runFindCheaperBook() throws Exception {
		final OWLKnowledgeBase kb = OWLFactory.createKB();

		// we need to check preconditions so that local variables will be assigned values
		exec.getExecutionValidator().setPreconditionCheck(true);

		// read the service description
		service = kb.readService(ExampleURIs.FIND_CHEAPER_BOOK_OWLS12);
		process = service.getProcess();

		// initialize the input values to be empty
		inputs = new ValueMap<Input, OWLValue>();

		// use an arbitrary book name
		inputs.setValue(process.getInput("BookName"), kb.createDataValue("City of Glass"));
		outputs = exec.execute(process, inputs, kb);

		// get the output values
		final OWLIndividual price = outputs.getIndividualValue(process.getOutput("BookPrice"));
		final String bookstore = outputs.getStringValue(process.getOutput("Bookstore"));

		// display the results
		System.out.println("Executed service " + service);
		System.out.println("Bookstore = " + bookstore);
		System.out.println("Price = ");
		System.out.println(Utils.formatRDF(price.toRDF(true, true)));
		System.out.println();
	}

	public void runCurrencyConverter() throws Exception {
		final OWLKnowledgeBase kb = OWLFactory.createKB();
		// read the service description
		service = kb.readService(ExampleURIs.CURRENCY_CONVERTER_OWLS12);
		process = service.getProcess();

		// initialize the input values to be empty
		inputs = new ValueMap<Input, OWLValue>();


		final OWLIndividual EUR = kb.getIndividual(URI.create(ExampleURIs.ONT_CURRENCIES + "EUR"));
		inputs.setValue(process.getInput("OutputCurrency"), EUR);

		final OWLIndividual USD = kb.getIndividual(URI.create(ExampleURIs.ONT_CURRENCIES + "USD"));

		final OWLClass Price = kb.getClass(URI.create(ExampleURIs.ONT_MINDSWAP_CONCEPTS + "Price"));
		final OWLObjectProperty currency = kb.getObjectProperty(URI.create(ExampleURIs.ONT_MINDSWAP_CONCEPTS + "currency"));
		final OWLDataProperty amount = kb.getDataProperty(URI.create(ExampleURIs.ONT_MINDSWAP_CONCEPTS + "amount"));

		final OWLIndividual inputPrice = kb.createInstance(Price, null);
		inputPrice.addProperty(currency, USD);
		inputPrice.addProperty(amount, "100");

		System.out.println(inputPrice.toRDF(true, true));

		// get the parameter using the local name
		inputs.setValue(process.getInput("InputPrice"), inputPrice);

		outputs = exec.execute(process, inputs, kb);

		// get the output parameter using the index
		final OWLIndividual out = outputs.getIndividualValue(process.getOutput());

		// display the results
		System.out.println("Executed service " + service);
		System.out.println("Grounding WSDL: " + ((AtomicProcess) process).getGrounding().getDescriptionURL());
		System.out.println("Input  = ");
		System.out.println(Utils.formatRDF(inputPrice.toRDF(true, true)));
		System.out.println("Output = ");
		System.out.println(Utils.formatRDF(out.toRDF(true, true)));
		System.out.println();
	}

	public void runFrenchDictionary() throws Exception {
		final OWLKnowledgeBase kb = OWLFactory.createKB();
		// we need a reasoner that can evaluate the precondition of the translator
		kb.setReasoner("Pellet");

		service = kb.readService(ExampleURIs.FRENCH_DICTIONARY_OWLS12);
		process = service.getProcess();

		// initialize the input values to be empty
		inputs = new ValueMap<Input, OWLValue>();

		inValue = "mere";
		inputs.setValue(process.getInput("InputString"), kb.createDataValue(inValue));
//		exec.setPreconditionCheck(false);
		outputs = exec.execute(process, inputs, kb);

		// get the output using local name
		outValue = outputs.getValue(process.getOutputs().getIndividual("OutputString")).toString();

		// display the results
		System.out.println("Executed service " + service);
		System.out.println("Input  = " + inValue);
		System.out.println("Output = " + outValue);
		System.out.println();
	}

	public void runTranslator() throws Exception {
		final OWLKnowledgeBase kb = OWLFactory.createKB();
		// we at least need RDFS reasoning to evaluate preconditions (to understand
		// that process:Parameter is subclass of swrl:Variable)
		kb.setReasoner("RDFS");

		service = kb.readService(ExampleURIs.BABELFISH_TRANSLATOR_OWLS12);
		process = service.getProcess();

		// get the references for these values
		final OWLIndividual English = kb.getIndividual(URI.create(ExampleURIs.ONT_FACTBOOK_LANGUAGES + "English"));
		final OWLIndividual French = kb.getIndividual(URI.create(ExampleURIs.ONT_FACTBOOK_LANGUAGES + "French"));

		// initialize the input values to be empty
		inputs = new ValueMap<Input, OWLValue>();

		inputs.setValue(process.getInput("InputString"), kb.createDataValue("Hello world!"));
		inputs.setValue(process.getInput("InputLanguage"), English);
		inputs.setValue(process.getInput("OutputLanguage"), French);
		outputs = exec.execute(process, inputs, kb);

		// get the output using local name
		outValue = outputs.getValue(process.getOutput()).toString();

		// display the results
		System.out.println("Executed service '" + service + "'");
		System.out.println("Grounding WSDL: " + ((AtomicProcess) process).getGrounding().getDescriptionURL());
		System.out.println("Output = " + outValue);
		System.out.println();
	}

	public void runDictionary() throws Exception {
		final OWLKnowledgeBase kb = OWLFactory.createKB();
		service = kb.readService(ExampleURIs.DICTIONARY_OWLS12);
		process = service.getProcess();

		// initialize the input values to be empty
		inputs = new ValueMap<Input, OWLValue>();

		inValue = "hello";
		inputs.setValue(process.getInput("InputString"), kb.createDataValue(inValue));
		outputs = exec.execute(process, inputs, kb);

		// get the output
		final OWLDataValue out = (OWLDataValue) outputs.getValue(process.getOutput());

		// display the results
		System.out.println("Executed service '" + service + "'");
		System.out.println("Grounding WSDL: " + ((AtomicProcess) process).getGrounding().getDescriptionURL());
		System.out.println("Input  = " + inValue);
		System.out.println("Output = " + out.toString());
		System.out.println();
	}

	public void runAll() throws Exception {
//		try {
//		runCurrencyConverter();
//		} catch(Exception e) {
//		}

//		try {
//		runCurrencyConverter();
//		} catch(Exception e) {
//		e.printStackTrace();
//		}

//		try {
//		runZipCode();
//		} catch(Exception e) {
//		e.printStackTrace();
//		}

//		try {
//		runTranslator();
//		} catch(Exception e) {
//		e.printStackTrace();
//		}

//		try {
//		runDictionary();
//		} catch(Exception e) {
//		e.printStackTrace();
//		}

//		try {
//		runBookFinder();
//		} catch(Exception e) {
//		e.printStackTrace();
//		}

		try {
			runFrenchDictionary();
		} catch(final Exception e) {
			e.printStackTrace();
		}

//		try {
//	runBookPrice();
//		} catch(Exception e) {
//		e.printStackTrace();
//		}

//		try {
//		runFindCheaperBook();
//		} catch(Exception e) {
//		e.printStackTrace();
//		}
	}

	public static void main(final String[] args) throws Exception {
		final RunService test = new RunService();
		test.runAll();

	}
}
