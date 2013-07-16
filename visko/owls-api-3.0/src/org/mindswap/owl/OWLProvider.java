/*
 * Created 22.08.2009
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
package org.mindswap.owl;

import impl.owl.CastingList;

import java.net.URI;
import java.util.List;

import org.mindswap.common.Parser;
import org.mindswap.common.Variable;
import org.mindswap.query.ABoxQuery;
import org.mindswap.query.QueryLanguage;

/**
 * Factory like encapsulation of methods that need to be implemented in order
 * to provide major OWL data structures and reasoning engines.
 * <p>
 * The methods defined here directly correspond and are used by the methods
 * provided by {@link OWLFactory}.
 *
 * @author unascribed
 * @version $Rev: 2317 $; $Author: nick $; $Date: 2011/01/24 06:37:52 $
 */
public interface OWLProvider extends OWLObjectConverterProvider
{
	public static final String DEFAULT_OWL_PROVIDER = "impl.jena.JenaOWLProvider";

	public Object createReasoner(String reasonerName);

	public List<String> getReasonerNames();

	public OWLDataType getDataType(Class<?> clazz);

	public OWLKnowledgeBase createKB();

	public OWLOntology createOntology(URI uri);

	public <T extends OWLIndividual> OWLIndividualList<T> createIndividualList();

	public <T extends OWLIndividual> OWLIndividualList<T> createIndividualList(final int capacity);

	public Parser<String, ABoxQuery<Variable>> createABoxQueryParser(OWLModel model, QueryLanguage lang);

	public <T extends OWLIndividual> OWLIndividualList<T> castList(List<? extends OWLIndividual> list, Class<T> castTarget);

	public <T extends OWLIndividual> CastingList<T> wrapList(List<? extends OWLIndividual> list, Class<T> castTarget);

	public <T extends OWLIndividual> OWLIndividualList<T> emptyIndividualList();

	public <T extends OWLIndividual> OWLIndividualList<T> unmodifiableIndividualList(List<T> list);
}