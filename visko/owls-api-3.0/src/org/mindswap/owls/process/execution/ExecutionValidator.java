/*
 * Created 27.12.2008
 *
 * (c) 2008 Thorsten Möller - University of Basel Switzerland
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
package org.mindswap.owls.process.execution;

import org.mindswap.exceptions.ExecutionException;
import org.mindswap.exceptions.MultipleSatisfiedPreconditionException;
import org.mindswap.exceptions.UnsatisfiedPreconditionException;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owl.OWLValue;
import org.mindswap.owls.process.Process;
import org.mindswap.owls.process.variable.ProcessVar;
import org.mindswap.query.ValueMap;

/**
 * An execution validator is used in the course of (composite) process
 * execution to validate preconditions and results. Depending on the
 * logic language used to express those preconditions and result effects,
 * various implementations may exist. If such a logic language is not
 * automatically decidable it is even imaginable to implement a validator
 * that integrates humans in the loop of validation (by means of whatever
 * kind of user interface that allows the user to state if some precondition
 * is satisfied respectively some result is valid).
 *
 * @author unascribed
 * @version $Rev:$; $Author: nick $; $Date: 2011/01/24 06:37:52 $
 */
public interface ExecutionValidator
{
	/**
	 * @param process The process whose execution will be done next.
	 * @param values The set of process variable bindings that exist at the
	 * 	time of precondition evaluation. Since precondition evaluation happens
	 * 	prior to execution the map would contain only inputs bound to their
	 * 	value.
	 * 	Note that evaluation of preconditions may bind existential process
	 * 	variables (if any). If so, implementations of this method must put them
	 * 	in this map.
	 * @param kb All the (domain) knowledge available right now.
	 * @throws UnsatisfiedPreconditionException In case {@link #isPreconditionCheck()}
	 * 	returns <code>true</code> and some precondition of the given process
	 * 	is not satisfied.
	 * @throws MultipleSatisfiedPreconditionException In case {@link #isPreconditionCheck()}
	 * 	returns <code>true</code>, {@link #isAllowMultipleSatisifedPreconditions()}
	 * 	returns <code>false</code> and some precondition of the given process
	 * 	has multiple different "solutions", i.e., it can be satisfied by different
	 * 	bindings of variables in the precondition.
	 */
	public abstract void checkPreconditions(Process process, ValueMap<ProcessVar, OWLValue> values,
		OWLKnowledgeBase kb) throws ExecutionException;

	/**
	 * @param process The current process whose execution was done and whose result(s)
	 * 	are to be validated.
	 * @param values The set of process variables that exist in the scope of the given
	 * 	process. In case of result evaluation this can be inputs that were consumed
	 * 	by the process, outputs produced by the process, locals used within the
	 * 	process, and existentials bound by preconditions.
	 * @param kb All the (domain) knowledge available right now.
	 * @throws ExecutionException In case some result is not satisfied.
	 */
	public void checkResults(final Process process, final ValueMap<ProcessVar, OWLValue> values,
		final OWLKnowledgeBase kb) throws ExecutionException;

	/**
	 * @return <code>true</code> if multiple satisfied preconditions are allowed.
	 */
	public abstract boolean isAllowMultipleSatisifedPreconditions();

	/**
	 * @return <code>true</code> if validation of preconditions will be done.
	 */
	public abstract boolean isPreconditionCheck();

	/**
	 * @return <code>true</code> if validation of results (effects) will be done.
	 */
	public abstract boolean isResultCheck();

	/**
	 * Set the behavior that determines if evaluation of a precondition that can
	 * be satisfied by multiple different bindings of variables occurring in the
	 * precondition is regarded a evaluation failure or not. If this option is
	 * enabled then in such a situation the execution engine will arbitrarily pick
	 * a value from any of the different bindings that exist and continue.
	 *
	 * @param allowMultipleSatisfiedPreconditions If <code>true</code> multiple
	 *        value bindings for variables used in preconditions is allowed. If
	 *        <code>false</code> then such a situation would cause throwing a
	 *        {@link MultipleSatisfiedPreconditionException}.
	 */
	public abstract void setAllowMultipleSatisfiedPreconditions(
		final boolean allowMultipleSatisfiedPreconditions);

	/**
	 * Enable/disable precondition validation. Note that if there are existential
	 * variables bound by the precondition then the overall execution may fail when
	 * precondition evaluation is disabled, in particular if the are required for
	 * result evaluation.
	 */
	public abstract void setPreconditionCheck(final boolean checkPreconditions);

	/**
	 * Enable/disable result (effects) validation.
	 */
	public abstract void setResultCheck(final boolean checkResults);

}