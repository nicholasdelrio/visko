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
 * Created on 16.04.2005
 */
package examples;

import java.net.URI;

import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLOntology;
import org.mindswap.owl.OWLValue;
import org.mindswap.owl.vocabulary.XSD;
import org.mindswap.owls.OWLSFactory;
import org.mindswap.owls.grounding.JavaAtomicGrounding;
import org.mindswap.owls.grounding.JavaGrounding;
import org.mindswap.owls.process.AtomicProcess;
import org.mindswap.owls.process.execution.ProcessExecutionEngine;
import org.mindswap.owls.process.variable.Input;
import org.mindswap.owls.process.variable.Output;
import org.mindswap.owls.service.Service;
import org.mindswap.query.ValueMap;
import org.mindswap.utils.URIUtils;

/**
 * This example shows how to create a service grounded to a simple Java method.
 * A sample service is built and a Java grounding is created, which matches to
 * the following method call:
 *
 * <pre>public String testIt(int i, Double y)</pre>
 *
 * Since the method is not static an instance of this class will be created
 * using the default no-args constructor, prior to method invocation.
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:53 $
 */
public class CreateJavaGrounding
{

	public static void main(final String[] args) throws Exception
	{
		final CreateJavaGrounding test = new CreateJavaGrounding();
		test.run();
	}

	public void run() throws Exception
	{
		final URI baseURI = ExampleURIs.JGROUNDING_OWLS12;

		final OWLOntology ont = OWLFactory.createKB().createOntology(baseURI);

		final Service service = ont.createService(URIUtils.createURI(baseURI, "MyService"));
		final AtomicProcess process = ont.createAtomicProcess(URIUtils.createURI(baseURI, "MyProcess"));
		service.setProcess(process);

		final Input input1 = ont.createInput(URIUtils.createURI(baseURI, "myInput1"));
		input1.setParamType(ont.getDataType(XSD.xsdInt));
		input1.setProcess(process);

		final Input input2 = ont.createInput(URIUtils.createURI(baseURI, "myInput2"));
		input2.setParamType(ont.getDataType(XSD.xsdDouble));
		input2.setProcess(process);

		final Output output = ont.createOutput(URIUtils.createURI(baseURI, "myOutput"));
		output.setParamType(ont.getDataType(XSD.string));
		output.setProcess(process);

		final JavaAtomicGrounding jAtomicGround = ont.createJavaAtomicGrounding(URIUtils.createURI(baseURI, "MyJAtomGround"));
		jAtomicGround.setOutput(URIUtils.createURI(baseURI, "JPar1"), String.class.getName(), output);
		jAtomicGround.addInputParameter(URIUtils.createURI(baseURI, "JIn1"), int.class.getName(), 0, input1);
		jAtomicGround.addInputParameter(URIUtils.createURI(baseURI, "JIn2"), Double.class.getName(), 1, input2);
		jAtomicGround.setClazz(CreateJavaGrounding.class.getName());
		jAtomicGround.setMethod("testIt"); // name must equal method name to be invoked
		jAtomicGround.setProcess(process);

		final JavaGrounding jGrounding = ont.createJavaGrounding(URIUtils.createURI(baseURI, "MyJGrounding"));
		jGrounding.addGrounding(jAtomicGround);
		jGrounding.setService(service);

		ont.write(System.out, baseURI);

		ValueMap<Input, OWLValue> inputs = new ValueMap<Input, OWLValue>();

		// get the parameter using the local name
		inputs.setValue(process.getInput("myInput1"), ont.createDataValue(Integer.valueOf(72)));
		inputs.setValue(process.getInput("myInput2"), ont.createDataValue(Double.valueOf(80.0)));

		System.out.println();
		System.out.println("Executing service ...");
		System.out.println("with inputs:      " + inputs);

		final ProcessExecutionEngine exec = OWLSFactory.createExecutionEngine();

		long time = System.nanoTime();
		ValueMap<Output, OWLValue> outputs = exec.execute(process, inputs, null);
		time = (System.nanoTime() - time) / 1000;

		System.out.printf("returned outputs: %s%n", outputs);
		System.out.printf("execution took:   %6dµs%n", time);
	}

	/**
	 * This method will be invoked by the Java Grounding specified by the example
	 * service. Since it is not static a instance of this class is created prior
	 * to invocation, using the default no-args constructor.
	 */
	public String testIt(final int x, final Double y) throws Exception
	{
		// calculate some value
		final double z = x * y;
		// something to show correct invocation
		System.out.printf("%d * %.1f = %.1f%n", x, y, z);
		// provoke exception
		//s = Double.parseDouble("ThisThrowsAnException");
		return "Return value of JavaGrounding " + z;
	}
}
