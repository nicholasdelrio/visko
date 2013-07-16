/*
 * Created 08.09.2009
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
package org.mindswap.query;

import java.util.List;

import org.mindswap.common.ClosableIterator;
import org.mindswap.common.Variable;
import org.mindswap.owl.OWLModel;
import org.mindswap.owl.OWLValue;

/**
 * Representation of a prepared query. Instances can be used to efficiently
 * execute the query they represent multiple times. Means to do so are not
 * further detailed here. This is up to implementations.
 * <p>
 * This interface is also agnostic to the actual query language used.
 *
 * @author unascribed
 * @version $Rev: 2350 $; $Author: nick $; $Date: 2011/01/24 06:37:52 $
 */
public interface Query<V extends Variable>
{
	/**
	 * Re-attach this query to the given model. The query can be
	 * {@link #execute(ValueMap) executed} against this model afterwards.
	 * <p>
	 * Implementations may use this method for initial query optimization.
	 *
	 * @param model The target model.
	 * @return <code>this</code> for cascading, in particular, to enable
	 * 	convenient consecutive {@link #execute(ValueMap) execution}.
	 */
	public Query<V> attach(OWLModel model);

	/**
	 * Answer this query against the {@link OWLModel} to which it is attached
	 * and return an iterator over the results, containing for each result the
	 * projection variable(s) bound to their result value. An empty iterator
	 * (i.e. <code>hasNext()</code> returns <code>false</code>) indicates that
	 * there are no results in this model for the query. For an ASK query
	 * (having no result variable at all but a boolean result), the returned
	 * iterator has exactly one <em>empty</em> value map object if the query
	 * result is <code>true</code> and an empty iterator in case the result is
	 * <code>false</code>.
	 * <p>
	 * Note that execution of {@link QueryLanguage#SPARQL_DL} queries requires
	 * an appropriate reasoner (Pellet) to be attached to the OWLModel.
	 *
	 * @return An iterator of the query results.
	 * @throws IllegalStateException If this query has not been
	 * 	{@link #attach(OWLModel) attached} to a {@link OWLModel}.
	 */
	public ClosableIterator<ValueMap<V, OWLValue>> execute(ValueMap<?, ?> initialBinding);

	/**
	 * @return The language used to express this query.
	 */
	public QueryLanguage getQueryLanguage();

	/**
	 * @return A list of the variables requested, i.e., a non-empty query result
	 * 	set binds each variable in this list to a value for each result.
	 */
	public List<V> getResultVariables();

}
