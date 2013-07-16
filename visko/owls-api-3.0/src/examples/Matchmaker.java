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
 * Created on Oct 13, 2004
 */
package examples;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.mindswap.common.ClosableIterator;
import org.mindswap.common.Variable;
import org.mindswap.exceptions.ParseException;
import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owl.OWLType;
import org.mindswap.owl.OWLValue;
import org.mindswap.owls.process.variable.Input;
import org.mindswap.owls.process.variable.Output;
import org.mindswap.owls.service.Service;
import org.mindswap.owls.vocabulary.OWLS;
import org.mindswap.query.QueryLanguage;
import org.mindswap.query.ValueMap;

/**
 * An simple form of a semantic matchmaker that finds service matches for
 * composition. The outputs of services are matched with the inputs of services
 * using one of the following matching degrees {@link MatchType#EXACT},
 * {@link MatchType#SUBSUME} and {@link MatchType#PLUG_IN}.
 * <p>
 * Pellet reasoner is used to realize concept hierarchy (required for finding
 * matches) but can be replaced with any other reasoner, see
 * {@link OWLFactory#getReasonerNames()} for a list of provided reasoners.
 *
 * @author unascribed
 * @version $Rev: 2350 $; $Author: nick $; $Date: 2011/01/24 06:37:53 $
 */
public class Matchmaker
{
	private final OWLKnowledgeBase kb;

	public enum MatchType { EXACT, SUBSUME, PLUG_IN, FAIL }

	public static class Match
	{
		private final MatchType matchType;
		boolean listMatch;
		private final Service outputService;
		private final Output output;
		private final Service inputService;
		private final Input input;

		public Match(final MatchType matchType, final Output output, final Input input)
		{
			this.matchType = matchType;
			this.outputService = output.getService();
			this.output = output;
			this.inputService = input.getService();
			this.input = input;
		}

		@Override
		public String toString()
		{
			String str = "";
			str += matchType + " ";
			if (listMatch) str += ".LIST";
			str += outputService.getLocalName() + "." + output.getLocalName();
			str += " -> ";
			str += inputService.getLocalName() + "." + input.getLocalName();
			return str;
		}
	}

	public Matchmaker() {
		kb = OWLFactory.createKB();
		kb.setReasoner("Pellet");
	}

	public void addOntology(final URI ont) throws IOException
	{
		System.out.printf("Reading %s%n", ont);
		kb.read(ont);
	}

	private List<ValueMap<Variable, OWLValue>> findServices(final boolean getProducers)
	{
		final String hasParameter = getProducers ? "process:hasOutput" : "process:hasInput";

		final String queryString =
			"SELECT * " +
			"WHERE " +
			"	   (?process rdf:type process:Process)" +
			"	   (?process " + hasParameter + " ?param)" +
			"USING " +
			"      process FOR <" + OWLS.PROCESS_NS + ">";

		List<ValueMap<Variable, OWLValue>> services = new ArrayList<ValueMap<Variable,OWLValue>>();
		ClosableIterator<ValueMap<Variable, OWLValue>> results = null;
		try
		{
			results = kb.makeQuery(queryString, QueryLanguage.RDQL).execute(null);
			while (results.hasNext())
			{
				services.add(results.next());
			}
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (results != null) results.close();
		}
		return services;
	}

	private List<ValueMap<Variable, OWLValue>> findOutputs() {
		return findServices(true);
	}

	private List<ValueMap<Variable, OWLValue>> findInputs() {
		return findServices(false);
	}

	public MatchType getMatchType(final OWLType outputType, final OWLType inputType) {
		if (outputType.isEquivalentTo(inputType)) return MatchType.EXACT;
		else if (outputType.isSubTypeOf(inputType)) return MatchType.SUBSUME;
		else if (inputType.isSubTypeOf(outputType)) return MatchType.PLUG_IN;
		else return MatchType.FAIL;
	}

	public List<Match> computeMatches()
	{
		final List<Match> matches = new ArrayList<Match>();

		System.out.println("Computing matches (" + kb.size() + " statements in KB) ...");

		final List<ValueMap<Variable, OWLValue>> producers = findOutputs();
		final List<ValueMap<Variable, OWLValue>> consumers = findInputs();

		// match every output against every input
		for (ValueMap<Variable, OWLValue> producer : producers)
		{
			final Output output = producer.getIndividualValue("param").castTo(Output.class);
			final OWLType outputType = output.getParamType();

			for (ValueMap<Variable, OWLValue> consumer : consumers)
			{
				final Input input = consumer.getIndividualValue("param").castTo(Input.class);
				final OWLType inputType = input.getParamType();

//				System.out.println("Trying " +
//				URIUtils.getLocalName(outputType.getURI()) + " " +
//				URIUtils.getLocalName(inputType.getURI()) + " " +
//				producer.getLocalName() + " " +
//				output.getLocalName() + " " +
//				consumer.getLocalName() + " " +
//				input.getLocalName()
//				);

				final MatchType matchType = getMatchType(outputType, inputType);
				if (MatchType.FAIL.equals(matchType)) continue; // no match, what a pity ;-)
				matches.add(new Match(matchType, output, input)); // match --> add it to list of matches
			}
		}

		return matches;
	}

	public static void printMatches(final List<Match> matches)
	{
		if (matches.isEmpty()) System.out.println("<EMPTY>");
		else
		{
			for (Match match : matches)
			{
				System.out.println(match);
			}
		}
		System.out.println();
	}

	public static void main(final String[] args) throws IOException
	{
		final Matchmaker matchmaker = new Matchmaker();

		matchmaker.addOntology(ExampleURIs.AMAZON_BOOK_PRICE_OWLS12);
		matchmaker.addOntology(ExampleURIs.BN_BOOK_PRICE_OWLS12);
		matchmaker.addOntology(ExampleURIs.BOOK_FINDER_OWLS12);
		matchmaker.addOntology(ExampleURIs.CURRENCY_CONVERTER_OWLS12);
		matchmaker.addOntology(ExampleURIs.DICTIONARY_OWLS12);
		matchmaker.addOntology(ExampleURIs.ZIP_CODE_FINDER_OWLS12);
		matchmaker.addOntology(ExampleURIs.FIND_LAT_LONG_OWLS12);
		matchmaker.addOntology(ExampleURIs.BABELFISH_TRANSLATOR_OWLS12);

		final long time = System.currentTimeMillis();
		final List<Match> matches = matchmaker.computeMatches();
		System.out.printf("%nMatches (MATCHING_DEGREE Output -> Input):%n");
		printMatches(matches);
		System.out.printf("Computation of matches took %dms%n", (System.currentTimeMillis() - time));
	}
}
