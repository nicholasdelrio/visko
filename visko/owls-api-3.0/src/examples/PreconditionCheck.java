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

import org.mindswap.exceptions.ExecutionException;
import org.mindswap.exceptions.PreconditionException;
import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owl.OWLValue;
import org.mindswap.owls.OWLSFactory;
import org.mindswap.owls.expression.Condition;
import org.mindswap.owls.process.Process;
import org.mindswap.owls.process.execution.ProcessExecutionEngine;
import org.mindswap.owls.process.variable.Input;
import org.mindswap.owls.service.Service;
import org.mindswap.query.ValueMap;

/**
 * Example to show how precondition check works.
 *
 * @author unascribed
 * @version $Rev: 2323 $; $Author: nick $; $Date: 2011/01/24 06:37:53 $
 */
public class PreconditionCheck
{
	public void run() throws Exception
	{
		final OWLKnowledgeBase kb = OWLFactory.createKB();
		final ProcessExecutionEngine exec = OWLSFactory.createExecutionEngine();

		kb.setReasoner("Pellet");

		final Service service = kb.readService(ExampleURIs.BABELFISH_TRANSLATOR_OWLS12);
		final Process process = service.getProcess();

		final OWLIndividual English = kb.getIndividual(URI.create(ExampleURIs.ONT_FACTBOOK_LANGUAGES + "English"));
		final OWLIndividual German = kb.getIndividual(URI.create(ExampleURIs.ONT_FACTBOOK_LANGUAGES + "German"));
		final OWLIndividual Italian = kb.getIndividual(URI.create(ExampleURIs.ONT_FACTBOOK_LANGUAGES + "Italian"));

		final ValueMap<Input, OWLValue> values = new ValueMap<Input, OWLValue>();

		exec.getExecutionValidator().setPreconditionCheck(false);

		System.out.println("Precondition check disabled");
		System.out.println("---------------------------");

		try
		{
			values.setValue(process.getInput("InputString"), kb.createDataValue("ciao mondo!"));
			values.setValue(process.getInput("InputLanguage"), Italian);
			values.setValue(process.getInput("OutputLanguage"), German);

			System.out.println("Trying unsupported language pair...");
			Condition<?> precondition = process.getConditions().get(0); // we know that there is one precondition
			System.out.println("Precondition satisfied: " + precondition.isTrue(values));
			exec.execute(process, values, kb);
			System.out.println("Execution successecful!");
		}
		catch (final PreconditionException e)
		{
			System.out.println("Precondition evaluation failed!");
			System.out.println(e);
		}
		catch (final ExecutionException e)
		{
			System.out.println("Execution failed!");
			System.out.println(e);
		}
		System.out.println();

		exec.getExecutionValidator().setPreconditionCheck(true);

		System.out.println("Precondition check enabled");
		System.out.println("---------------------------");

		try
		{
			values.setValue(process.getInput("InputString"), kb.createDataValue("ciao mondo!"));
			values.setValue(process.getInput("InputLanguage"), Italian);
			values.setValue(process.getInput("OutputLanguage"), German);

			System.out.println("Trying unsupported language pair...");
			Condition<?> precondition = process.getConditions().get(0); // we know that there is one precondition
			System.out.println("Precondition satisfied: " + precondition.isTrue(values));
			exec.execute(process, values, kb);
			System.out.println("Execution successecful!");
		}
		catch (final PreconditionException e)
		{
			System.out.println("Precondition evaluation failed!");
			System.out.println(e);
		}
		catch (final ExecutionException e)
		{
			System.out.println("Execution failed!");
			System.out.println(e);
		}

		System.out.println();
		try
		{
			values.setValue(process.getInput("InputString"), kb.createDataValue("ciao mondo!"));
			values.setValue(process.getInput("InputLanguage"), Italian);
			values.setValue(process.getInput("OutputLanguage"), English);

			System.out.println("Trying supported language pair...");
			Condition<?> precondition = process.getConditions().get(0); // we know that there is one precondition
			System.out.println("Precondition satisfied: " + precondition.isTrue(values));
			exec.execute(process, values, kb);
			System.out.println("Execution successecful!");
		}
		catch (final PreconditionException e)
		{
			System.out.println("Precondition evaluation failed!");
			System.out.println(e);
		}
		catch (final ExecutionException e)
		{
			System.out.println("Execution failed!");
			System.out.println(e);
		}
	}

	public static void main(final String[] args) throws Exception
	{
		final PreconditionCheck test = new PreconditionCheck();

		test.run();
	}
}
