/*
 * Created 11.01.2009
 *
 * (c) 2009 Thorsten Möller - University of Basel Switzerland
 *
 * The MIT License
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 */
package org.mindswap.utils;

import java.net.URI;

import org.mindswap.common.ClosableIterator;
import org.mindswap.common.OrdinaryVariable;
import org.mindswap.common.Variable;
import org.mindswap.exceptions.ParseException;
import org.mindswap.owl.OWLDataValue;
import org.mindswap.owl.OWLEntity;
import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owl.OWLOntology;
import org.mindswap.owl.OWLType;
import org.mindswap.owl.OWLValue;
import org.mindswap.owls.process.CompositeProcess;
import org.mindswap.owls.process.Process;
import org.mindswap.owls.process.variable.Parameter;
import org.mindswap.owls.process.variable.ProcessVar;
import org.mindswap.owls.vocabulary.OWLS_Extensions;
import org.mindswap.query.QueryLanguage;
import org.mindswap.query.ValueMap;

/**
 * This class realizes the following: given a {@link CompositeProcess} it
 * analyzes all its {@link org.mindswap.owls.process.variable.Binding bindings}
 * and those of its sub composite process(es) (if any). Bindings may be of
 * various kinds but for the data flow only {@link org.mindswap.owls.process.variable.ValueOf}
 * bindings are relevant. Each ValueOf binding represents a data flow
 * from a source parameter (producer) to a sink parameter (consumer), whereby
 * the sink parameter type may be more specific than the source parameter type,
 * that is, a sub class respectively a sub data type. For each source parameter
 * of each binding this class stores the most specific sink parameter in an
 * internal data structure (which is actually an OWL ontology), i.e., it
 * searches for the most specific type among all consumers of some source
 * parameter. Additionally, for every source parameter it also keeps the set of
 * all sinks parameters, that is, the <em>data flow</em>.
 * <p>
 * Note that OWL-S implements a <em>consumer-pull</em> convention to describe
 * the data flow of composite processes. However, this class realizes the so
 * called <em>producer-push</em> convention. Having only the former makes forward
 * navigation in the data flow in the direction of the execution precedence
 * order (control flow) much more difficult. This is basically the motivation
 * for this class.
 * <p>
 * One should make sure that the information represented by an instance of this
 * class is kept consistent with the data flow of the process from which it was
 * derived. This is relevant if the process' data flow is subject to be changed
 * during the lifetime of an instance of this class.
 *
 * @author unascribed
 * @version $Rev: 2350 $; $Author: nick $; $Date: 2011/01/24 06:37:53 $
 */
public class ProcessDataFlow
{
	private final OWLOntology ontology;

	/**
	 * @param indexOnt The target ontology in which the data flow index will be
	 * 	represented as statements, see {@link OWLS_Extensions.Process} for
	 * 	the vocabulary that is used for this purpose.
	 */
	public ProcessDataFlow(final OWLOntology indexOnt)
	{
		this.ontology = indexOnt;
	}

	/**
	 * @return The data flow materialized in a ontology. For the vocabulary
	 * 	that is used see {@link OWLS_Extensions.Process}.
	 */
	public OWLOntology asOntology()
	{
		return ontology;
	}

	/**
	 * @param source The source process variable.
	 * @return The most specific type among the sinks of the given source.
	 */
	public OWLType getMostSpecificSinkType(final ProcessVar source)
	{
		OWLDataValue msstDV = ontology.getProperty(source, OWLS_Extensions.Process.mssType);
		return ontology.getKB().getType((URI) msstDV.getValue());
	}

	/**
	 * Get the list of all sinks to which the data flow connects the
	 * source process variable to.
	 *
	 * @param source The source process variable.
	 * @return The list of all sinks the source is connected to. An empty list
	 * 	if the source is connected to nothing (which is rather an indication
	 * 	of a false data flow specification).
	 */
	public OWLIndividualList<Parameter> getSinkParameters(final ProcessVar source)
	{
		return OWLFactory.castList(
			ontology.getProperties(source, OWLS_Extensions.Process.connectedTo), Parameter.class);
	}

	@Override
	public String toString()
	{
		final Variable source = new OrdinaryVariable("source"), sink = new OrdinaryVariable("sink"),
			msst = new OrdinaryVariable("msst");
		final String query =
			"SELECT " + source + " " + sink + " " + msst + " " +
			"WHERE { " +
				source + " <" + OWLS_Extensions.Process.connectedTo.getURI() + "> " + sink + ";" +
							" <" + OWLS_Extensions.Process.mssType.getURI() + "> " + msst + "." +
			" } ORDER BY " + source;

		ClosableIterator<ValueMap<Variable, OWLValue>> results;
		try
		{
			results = ontology.makeQuery(query, QueryLanguage.SPARQL).execute(null);
		}
		catch (ParseException e) {	return "toString failure - details: " + e; }

		StringBuffer line = null, all = new StringBuffer();
		while (results.hasNext())
		{
			ValueMap<Variable, OWLValue> result = results.next();
			String sou = getShortName(result.getIndividualValue(source));
			String sin = getShortName(result.getIndividualValue(sink));
			if (line != null && line.indexOf(sou) == 0)
			{
				line.append(", ").append(sin);
			}
			else
			{
				finishLine(line, all);
				line = new StringBuffer();
				line.append(sou).append(" [msst = ").append(result.getValue(msst)).append("]");
				line.append(" ~~> {").append(sin);
			}
		}
		finishLine(line, all);
		return all.toString();
	}

	private void finishLine(final StringBuffer line, final StringBuffer all)
	{
		if (line != null)
		{
			line.append("}").append(Utils.LINE_SEPARATOR);
			all.append(line);
		}
	}

	private String getShortName(final OWLEntity entity)
	{
		return (entity.isAnon())? "_" + entity.getAnonID() : entity.getLocalName();
	}

	/**
	 * Factory method to create the producer-push style data flow structure,
	 * representing the data flow of the given process. Actually, only
	 * {@link org.mindswap.owls.process.CompositeProcess composite processes} may
	 * contain a data flow, whereas {@link org.mindswap.owls.process.AtomicProcess
	 * atomic processes} can not (they are atomic!). Consequently, for atomic
	 * processes, this method returns an instance representing an empty data
	 * flow.
	 *
	 * @param ont The backing ontology which will be enriched by the
	 * 	the producer-push data flow representation. Can be left <code>null</code>
	 * 	to create and use a new one (which will belong the KB associated to
	 * 	the process then).
	 * @param process The process to be analyzed.
	 * @return The index structure for forward data flow navigation.
	 */
	public static final ProcessDataFlow index(final OWLOntology ont, final Process process)
	{
		return process.getDataFlow(ont);
	}

}
