/*
 * Created on 25.04.2008
 *
 * The MIT License
 *
 * (c) 2008 Thorsten Möller - University of Basel Switzerland
 *
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
import org.mindswap.owls.process.AnyOrder;
import org.mindswap.owls.process.AsProcess;
import org.mindswap.owls.process.AtomicProcess;
import org.mindswap.owls.process.Choice;
import org.mindswap.owls.process.CompositeProcess;
import org.mindswap.owls.process.ControlConstruct;
import org.mindswap.owls.process.ControlConstructVisitor;
import org.mindswap.owls.process.ForEach;
import org.mindswap.owls.process.IfThenElse;
import org.mindswap.owls.process.Perform;
import org.mindswap.owls.process.Process;
import org.mindswap.owls.process.Produce;
import org.mindswap.owls.process.RepeatUntil;
import org.mindswap.owls.process.RepeatWhile;
import org.mindswap.owls.process.Sequence;
import org.mindswap.owls.process.Set;
import org.mindswap.owls.process.SimpleProcess;
import org.mindswap.owls.process.Split;
import org.mindswap.owls.process.SplitJoin;

/**
 * This interface specifies the methods that need to be implemented for
 * supporting execution of all {@link ControlConstruct control constructs}
 * specified by OWL-S as well as execution of all {@link Process process
 * types}. It is intended to be implemented <strong>only</strong> by
 * OWL-S execution engines. Furthermore, this interface is not intended to be
 * accessible by clients that want to execute some service. In fact, the methods
 * defined in this interface should be called only by the implementations of
 * sub interfaces of {@link ControlConstruct}, c.f.
 * {@link ControlConstruct#execute(ExecutionContext, ExecutionSupport)}.
 * <p>
 * Note that {@link ControlConstruct control constructs} can also be inspected
 * via {@link ControlConstructVisitor}. This provides a general visitor pattern
 * intended for public use (in contrast to this interface which is dedicated for
 * execution engines).
 *
 * @author unascribed
 * @version $Rev: 2319 $; $Author: nick $; $Date: 2011/01/24 06:37:52 $
 * @param <C> Any kind of context information required for executing one of the
 * 	control constructs respectively process.
 */
public interface ExecutionSupport<C extends ExecutionContext>
{
	/**
	 * Method tries to execute the given atomic process (provided that it is grounded).
	 *
	 * @param process The atomic process to execute.
	 * @param context Specific data that may be required for execution.
	 * @throws ExecutionException To signal any (unrecoverable) exceptional
	 * 	event during execution.
	 */
	public void executeAtomicProcess(AtomicProcess process, C context) throws ExecutionException;

	/**
	 * Method tries to execute the given composite process. Atomic processes at the
	 * leaf of the structure represented by the composite process must be grounded.
	 *
	 * @param process The composite process to execute.
	 * @param context Specific data that may be required for execution.
	 * @throws ExecutionException To signal any (unrecoverable) exceptional
	 * 	event during execution.
	 */
	public void executeCompositeProcess(CompositeProcess process, C context) throws ExecutionException;

	/**
	 * Method tries to execute the given simple process.
	 *
	 * @param process The simple process to execute.
	 * @param context Specific data that may be required for execution.
	 * @throws ExecutionException To signal any (unrecoverable) exceptional
	 * 	event during execution.
	 */
	public void executeSimpleProcess(SimpleProcess process, C context) throws ExecutionException;

	/**
	 * Method tries to execute the given as process construct.
	 *
	 * @param ap The control construct to execute.
	 * @param context Specific data that may be required for execution.
	 * @throws ExecutionException To signal any (unrecoverable) exceptional
	 * 	event during execution.
	 */
	public void executeAsProcess(AsProcess ap, C context) throws ExecutionException;

	/**
	 * Method tries to execute the given any order construct.
	 *
	 * @param ao The control construct to execute.
	 * @param context Specific data that may be required for execution.
	 * @throws ExecutionException To signal any (unrecoverable) exceptional
	 * 	event during execution.
	 */
	public void executeAnyOrder(AnyOrder ao, C context) throws ExecutionException;

	/**
	 * Method tries to execute the given choice construct.
	 *
	 * @param c The control construct to execute.
	 * @param context Specific data that may be required for execution.
	 * @throws ExecutionException To signal any (unrecoverable) exceptional
	 * 	event during execution.
	 */
	public void executeChoice(Choice c, C context) throws ExecutionException;

	/**
	 * Method tries to execute the given for-each construct.
	 *
	 * @param fe The control construct to execute.
	 * @param context Specific data that may be required for execution.
	 * @throws ExecutionException To signal any (unrecoverable) exceptional
	 * 	event during execution.
	 */
	public void executeForEach(ForEach fe, C context) throws ExecutionException;

	/**
	 * Method tries to execute the given if-then-else construct.
	 *
	 * @param ite The control construct to execute.
	 * @param context Specific data that may be required for execution.
	 * @throws ExecutionException To signal any (unrecoverable) exceptional
	 * 	event during execution.
	 */
	public void executeIfThenElse(IfThenElse ite, C context) throws ExecutionException;

	/**
	 * Method tries to perform the given construct.
	 *
	 * @param perform The control construct to execute.
	 * @param context Specific data that may be required for execution.
	 * @throws ExecutionException To signal any (unrecoverable) exceptional
	 * 	event during execution.
	 */
	public void executePerform(Perform perform, C context) throws ExecutionException;

	/**
	 * Method tries to perform the given produce construct.
	 *
	 * @param produce The control construct to execute.
	 * @param context Specific data that may be required for execution.
	 * @throws ExecutionException To signal any (unrecoverable) exceptional
	 * 	event during execution.
	 */
	public void executeProduce(Produce produce, C context) throws ExecutionException;

	/**
	 * Method tries to execute the given repeat-until construct.
	 *
	 * @param cc The control construct to execute.
	 * @param context Specific data that may be required for execution.
	 * @throws ExecutionException To signal any (unrecoverable) exceptional
	 * 	event during execution.
	 */
	public void executeRepeatUntil(RepeatUntil cc, C context) throws ExecutionException;

	/**
	 * Method tries to execute the given repeat-while construct.
	 *
	 * @param cc The control construct to execute.
	 * @param context Specific data that may be required for execution.
	 * @throws ExecutionException To signal any (unrecoverable) exceptional
	 * 	event during execution.
	 */
	public void executeRepeatWhile(RepeatWhile cc, C context) throws ExecutionException;

	/**
	 * Method tries to execute the given sequence construct.
	 *
	 * @param seq The control construct to execute.
	 * @param context Specific data that may be required for execution.
	 * @throws ExecutionException To signal any (unrecoverable) exceptional
	 * 	event during execution.
	 */
	public void executeSequence(Sequence seq, C context) throws ExecutionException;

	/**
	 * Method tries to execute the given set construct.
	 *
	 * @param set The control construct to execute.
	 * @param context Specific data that may be required for execution.
	 * @throws ExecutionException To signal any (unrecoverable) exceptional
	 * 	event during execution.
	 */
	public void executeSet(Set set, C context) throws ExecutionException;

	/**
	 * Method tries to execute the given split construct in parallel (without
	 * respecting any order).
	 *
	 * @param split The control construct to execute.
	 * @param context Specific data that may be required for execution.
	 * @throws ExecutionException To signal any (unrecoverable) exceptional
	 * 	event during execution.
	 */
	public void executeSplit(Split split, C context) throws ExecutionException;

	/**
	 * Method tries to execute the given split-join construct in parallel (without
	 * respecting any order).
	 *
	 * @param split The control construct to execute.
	 * @param context Specific data that may be required for execution.
	 * @throws ExecutionException To signal any (unrecoverable) exceptional
	 * 	event during execution.
	 */
	public void executeSplitJoin(SplitJoin split, C context) throws ExecutionException;

}
