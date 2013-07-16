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
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owl.OWLValue;
import org.mindswap.owls.process.Process;
import org.mindswap.owls.process.variable.Input;
import org.mindswap.owls.service.Service;
import org.mindswap.query.ValueMap;
import org.mindswap.utils.ProcessUtils;

/**
 * Example to show how precondition check can be used to find valid values.
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:53 $
 */
public class PreconditionUsage {
	public void run() throws Exception {
		final long time = System.currentTimeMillis();
		final OWLKnowledgeBase kb = OWLFactory.createKB();

		kb.setReasoner("Pellet");

		final Service service = kb.readService(ExampleURIs.BABELFISH_TRANSLATOR_OWLS12);
		final Process process = service.getProcess();

		final Input inLang = process.getInput("InputLanguage");
		final Input outLang = process.getInput("OutputLanguage");

		final OWLIndividual English = kb.getIndividual(URI.create(ExampleURIs.ONT_FACTBOOK_LANGUAGES + "English"));
		final OWLIndividual Italian = kb.getIndividual(URI.create(ExampleURIs.ONT_FACTBOOK_LANGUAGES + "Italian"));
		final OWLIndividual Russian = kb.getIndividual(URI.create(ExampleURIs.ONT_FACTBOOK_LANGUAGES + "Russian"));
		final OWLIndividual German = kb.getIndividual(URI.create(ExampleURIs.ONT_FACTBOOK_LANGUAGES + "German"));

		final ValueMap<Input, OWLIndividual> values = new ValueMap<Input, OWLIndividual>();

		System.out.println( "Find all possible languages that can be used" );
		System.out.println( "------------------------------------------------" );
		printAllowedValues( process, values );

		System.out.println( "Find languages that English can be translated to" );
		System.out.println( "------------------------------------------------" );
		values.setValue( inLang, English );
		printAllowedValues( process, values );

		System.out.println( "Find languages that Italian can be translated to" );
		System.out.println( "------------------------------------------------" );
		values.clear();
		values.setValue( inLang, Italian );
		printAllowedValues( process, values );

		System.out.println( "Find languages that can be translated from Russian" );
		System.out.println( "--------------------------------------------------" );
		values.clear();
		values.setValue( outLang, Russian );
		printAllowedValues( process, values );

		System.out.println( "Find languages that can be translated from German" );
		System.out.println( "--------------------------------------------------" );
		values.clear();
		values.setValue( outLang, German );
		printAllowedValues( process, values );

		System.out.println("took " + (System.currentTimeMillis() - time) + "ms");
	}

	public static void printAllowedValues(final Process process, final ValueMap<Input, OWLIndividual> values)
	{
		System.out.println("Given Binding:");
		if (values.isEmpty())
			System.out.println("   <NONE>");
		else
		{
			for (final Input input : values.getVariables())
			{
				final OWLIndividual value = values.getIndividualValue(input);
				System.out.println("   " + input.getLocalName() + " = " + value.getLocalName());
			}
		}

		System.out.println("Allowed values:");
		final Map<Input, Set<OWLValue>> allowedValues = ProcessUtils.getAllowedValues(process, values);
		for (final Entry<Input, Set<OWLValue>> entry : allowedValues.entrySet())
		{
			final Input input = entry.getKey();
			final Set<OWLValue> set = entry.getValue();
			System.out.print("   " + input.getLocalName() + " = [");
			for (final Iterator<OWLValue> j = set.iterator(); j.hasNext();)
			{
				final OWLIndividual value = (OWLIndividual) j.next();
				System.out.print(value.getLocalName());
				if (j.hasNext())
					System.out.print(", ");
			}
			System.out.println("]");
		}
		System.out.println();
	}

	public static void main(final String[] args) throws Exception
	{
		final PreconditionUsage test = new PreconditionUsage();
		test.run();
	}
}
