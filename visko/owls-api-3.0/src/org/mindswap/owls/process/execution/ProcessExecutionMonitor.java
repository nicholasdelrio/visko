// The MIT License
//
// Copyright (c) 2009 Thorsten Möller - University of Basel Switzerland
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
 * Created on Jun 27, 2005
 */
package org.mindswap.owls.process.execution;

import org.mindswap.exceptions.ExecutionException;
import org.mindswap.owl.OWLValue;
import org.mindswap.owls.process.AtomicProcess;
import org.mindswap.owls.process.Process;
import org.mindswap.owls.process.variable.Input;
import org.mindswap.owls.process.variable.Output;
import org.mindswap.owls.process.variable.Parameter;
import org.mindswap.query.ValueMap;

/**
 * An interface that describes functions to monitor process execution.
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:52 $
 */
public interface ProcessExecutionMonitor
{
	/**
	 * Called only once when the execution of the top-most process starts.
	 */
	public void executionStarted();

	/**
	 * Called only once when the execution of the top-most process finishes.
	 * Note that if execution fails for any reason this function will not be
	 * called. Instead {@link #executionFailed(ExecutionException)} will be
	 * called.
	 */
	public void executionFinished();

	/**
	 * Called before the execution of a process starts. The user has the option
	 * to modify the contents of the inputs. {@link #setMonitorFilter(int)} can
	 * be used to control if this function will be called for all the processes
	 * or only for a specific type of process (e.g. only atomic processes).
	 *
	 * @param process
	 * @param inputs
	 */
	public void executionStarted(Process process, ValueMap<Input, OWLValue> inputs);

	/**
	 * Called after the execution of a process finishes. The user has the option to
	 * modify the contents of outputs. {@link #setMonitorFilter(int)} can be used to
	 * control if this function will be called for all the processes or only for
	 * a specific type of process (e.g. only atomic processes).
	 *
	 * @param process
	 * @param inputs
	 * @param outputs
	 */
	public void executionFinished(Process process, ValueMap<Input, OWLValue> inputs,
		ValueMap<Output, OWLValue> outputs);

	/**
	 * Called when the execution fails due to an exception. This function is intended
	 * to be hook where the user can do something to fix the problem, i.e. ask for
	 * additional inputs. There is no such support at the moment. The execution engine
	 * will re-throw the exception right after this function returns.
	 *
	 * @param e The exception.
	 */
	public void executionFailed(ExecutionException e);

	/**
	 * Control if executionStarted and executionFinished will be called for all the
	 * processes or only for a specific type of processes. The constant values are
	 * defined in {@link Process} interface:
	 * <ul>
	 * <li><code>Process.ANY</code></li>
	 * <li><code>Process.ATOMIC</code></li>
	 * <li><code>Process.COMPOSITE</code></li>
	 * <li><code>Process.SIMPLE</code></li>
	 * </ul>
	 * Bitwise combinations (<code>bitwise or</code>) of these values are also valid.
	 *
	 * @param processType Integer number representing the filter bits.
	 */
	public void setMonitorFilter(int processType);

	public int getMonitorFilter();

	/**
	 * Called after the execution of a process interrupts. {@link #setMonitorFilter(int)}
	 * can be used to control if this method will be called for all the
	 * processes or only for a specific type of processes (e.g. only atomic
	 * processes).
	 *
	 * @param process The process whose execution was interrupted.
	 */
	public void executionInterrupted(Process process, AtomicProcess lastProcess);

	/**
	 * Called after the interrupted execution of a process has been continued.
	 * {@link #setMonitorFilter(int)} can be used to control if this function
	 * will be called for all the processes or only for a specific type of
	 * process (e.g. only atomic processes).
	 *
	 * @param process The process whose execution has been continued.
	 */
	public void executionContinued(Process process);

	/**
	 * Called after the perform of a process to return the outputs of the process.
	 * Therefore, value changes for intermediate results can be monitored.
	 * {@link #setMonitorFilter(int)} can be used to control if this function
	 * will be called for all the processes or only for a specific type of
	 * process (e.g. only atomic processes).
	 *
	 * @param values The outputs returned by the performed process.
	 */
	public void intermediateResultsReceived(ValueMap<Parameter, OWLValue> values);
}
