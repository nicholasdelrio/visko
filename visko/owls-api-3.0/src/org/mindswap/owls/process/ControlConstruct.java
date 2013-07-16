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
 *
 */
package org.mindswap.owls.process;

import org.mindswap.common.Visitable;
import org.mindswap.exceptions.ExecutionException;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owls.process.execution.ExecutionContext;
import org.mindswap.owls.process.execution.ExecutionSupport;
import org.mindswap.owls.process.variable.Binding;

/**
 * @author unascribed
 * @version $Rev: 2319 $; $Author: nick $; $Date: 2011/01/24 06:37:52 $
 */
public interface ControlConstruct extends OWLIndividual, Visitable<ControlConstructVisitor, ControlConstruct>
{
	/**
	 * Returns a list of all the Process objects used (directly or indirectly) in
	 * this control construct. These include all the processes that are performed
	 * as a result of executing this control construct.
	 *
	 * @param recursive If <code>true</code> all the processes inside a Perform
	 * 	will be recursively added to the results.
	 * @return List of Process objects executed when this construct is executed.
	 */
	public OWLIndividualList<Process> getAllProcesses(boolean recursive);

	/**
	 * Returns a list of all the Binding objects used (directly or indirectly)
	 * in this control construct.
	 *
	 * @return List of binding objects.
	 */
	public OWLIndividualList<Binding<?>> getAllBindings();

	/**
	 * Returns the immediate sub-constructs of this ControlConstruct. The result
	 * list is computed differently for each control construct. For example, in
	 * case of {@link IfThenElse} the returned list would contain two elements if
	 * it has both then and else branch specified (via <code>process:then</code>
	 * and <code>process:else</code> property), or a list of one element if the
	 * else branch is unspecified. For all {@link CollectionControlConstruct
	 * collection control constructs}, this method will look at the
	 * <code>process:components</code> property and return the elements of the
	 * referred list or bag.
	 * <p>
	 * Note that modifications to the returned list itself are <strong>not</strong>
	 * reflected in the backing OWL model. On the other hand, modifications of
	 * the control constructs themselves will be reflected in the backing model.
	 *
	 * @return The immediate sub-constructs this control construct contains.
	 * 	Returns the empty list if none exists.
	 */
	public OWLIndividualList<ControlConstruct> getConstructs();

	/**
	 * @return The name of this construct which is equal to local name defined
	 * 	in OWL-S process ontology, i.e., Sequence, Choice, If-Then-Else, etc.
	 */
	public String getConstructName();

	/**
	 * @return The composite process that contains this control construct.
	 * 	Returns the first one found if this control construct is referenced by
	 * 	more than one process.
	 */
	public CompositeProcess getEnclosingProcess();

	/**
	 * @return Get the interval of time allowed for completion of this control
	 * 	construct (relative to the start of control construct execution).
	 * 	Returns <code>null</code> if timeout is not specified.
	 */
	public OWLIndividual getTimeout();

	/**
	 * @param timeout Set the interval of time allowed for completion of this
	 * 	control construct (relative to the start of control construct execution).
	 * 	An existing timeout property on this control construct will be removed
	 * 	first.
	 */
	public void setTimeout(OWLIndividual timeout);

	/**
	 * Removes the given CC, which is contained in this CC. Control flow is rerouted from
	 * predecessor to successor of the given CC. Data flow from and to the given CC
	 * is removed too.
	 *
	 * @param cc the ControlConstruct to remove.
	 * @return <code>true</code> if removal was successful, <code>false</code> otherwise.
	 * @see #getAllBindings() for retrieving the bindings from and to the given CC.
	 */
	public boolean removeConstruct(ControlConstruct cc);

	/**
	 * Executes this control construct, provided that it bottoms out in grounded
	 * {@link AtomicProcess atomic processe(s)}.
	 *
	 * @param context Any kind of context information required for execution.
	 * @param target The backing execution object that must be invoked for
	 * 	inner {@link ControlConstruct control constructs} respectively
	 * 	{@link AtomicProcess atomic processes}.
	 * @throws ExecutionException In case of (unrecoverable) failures
	 * 	during execution.
	 */
	public <C extends ExecutionContext> void execute(C context, ExecutionSupport<C> target) throws ExecutionException;

	/**
	 * Prepare this control construct for subsequent execution. Implementations
	 * may set up means to optimize execution in terms of whatever resources,
	 * e.g., space and time. However, support of this operation is a optional,
	 * hence, implementations may do nothing.
	 *
	 * @param context Any kind of context information to support preparation.
	 * @return The prepared control construct. Implementations may return the
	 * 	same (but prepared) object instance (<code>this</code>) or a new
	 * 	control construct instance, just as they like.
	 */
	public ControlConstruct prepare(ExecutionContext context);

}
