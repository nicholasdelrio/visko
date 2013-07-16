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
 * Created on Dec 27, 2003
 */
package org.mindswap.owls.expression;

import java.util.List;

import org.mindswap.common.ClosableIterator;
import org.mindswap.common.Variable;
import org.mindswap.owl.OWLModel;
import org.mindswap.owl.OWLValue;
import org.mindswap.owl.list.OWLList;
import org.mindswap.query.ValueMap;
import org.mindswap.swrl.Atom;

/**
 * A condition is simply an expression. The truth value of a condition needs to
 * be evaluated w.r.t. a KB. By default, this KB is the one that the condition
 * is coming from but conditions can be evaluated w.r.t. another KB (execution
 * environment may be different).
 * <p>
 * In fact, conditions are queries and they are satisfied (true) w.r.t. some
 * {@link OWLModel} if execution of the query yields a non-empty result set. In
 * case of exactly one element in the result set such a query is said to be
 * <em>unambiguously</em> satisfied, otherwise, in case of multiple results it
 * has <em>multiple</em> solutions that satisfy it.
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:54 $
 */
public interface Condition<B> extends Expression<B>
{

	/**
	 * Check if this condition is entailed by its {@link #getKB() KB}, whether
	 * it has at least one solution. If it consists of multiple atoms then it
	 * is checked if the conjunction of atoms is entailed by its KB.
	 *
	 * @param binding A map of initial bindings possibly binding some of the
	 * 	variables used in this condition. Parameter can be <code>null</code>
	 * 	to evaluate this expression without the attempt to bind variables.
	 * @return <code>true</code> if this condition is satisfied w.r.t. its own
	 *         KB, after applying the given <code>binding</code>, that possibly
	 *         grounds variables contained in this condition. This is equivalent
	 *         to the call <code>condition.isTrue(condition.getKB(), binding)</code>.
	 */
	public boolean isTrue(ValueMap<?, ?> binding);

	/**
	 * Check if this condition is entailed by the given model, whether
	 * it has at least one solution. If it consists of multiple atoms then it
	 * is checked if the conjunction of atoms is entailed by the given model.
	 *
	 * @param model The model in which to evaluate this condition.
	 * @param binding A map of initial bindings possibly binding some of the
	 * 	variables used in this condition. Parameter can be <code>null</code>
	 * 	to evaluate this expression without the attempt to bind variables.
	 * @return <code>true</code> if the condition is satisfied w.r.t. the given
	 *         model, after applying the given <code>binding</code>, that
	 *         possibly grounds variables contained in this condition.
	 */
	public boolean isTrue(OWLModel model, ValueMap<?, ?> binding);

	/**
	 *
	 */
	public <V extends Variable> ClosableIterator<ValueMap<V, OWLValue>> solutions(
		ValueMap<?, ?> binding, final List<V> solutionVars);

	/**
	 *
	 */
	public <V extends Variable> ClosableIterator<ValueMap<V, OWLValue>> solutions(OWLModel model,
		ValueMap<?, ?> binding, final List<V> solutionVars);

	/**
	 * SPARQL Condition.
	 * <p>
	 * Checking the truth value is only supported for the SELECT and ASK query
	 * type. A SELECT query is <code>true</code> if it has at least one solution.
	 * A ASK query is <code>true</code> if it returns <code>true</code>. The query
	 * types CONSTRUCT and DESCRIBE will be ignored for truth checking.
	 * <p>
	 * Retrieving solutions is also only supported for SELECT and ASK queries.
	 * However, for the latter, the result is either the empty solution if the
	 * query returns <code>true</code>, respectively no solution if it returns
	 * <code>false</code>.
	 */
	public interface SPARQL extends Expression.SPARQL, Condition<String> { }

	/**
	 * SWRL Condition.
	 */
	public interface SWRL extends Expression.SWRL, Condition<OWLList<Atom>>	{ }

}
