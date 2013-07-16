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
package org.mindswap.owls.process.execution;

import org.mindswap.exceptions.ExecutionException;
import org.mindswap.owl.OWLValue;
import org.mindswap.owls.process.Process;
import org.mindswap.owls.process.variable.Input;
import org.mindswap.owls.process.variable.Output;
import org.mindswap.owls.process.variable.Parameter;
import org.mindswap.query.ValueMap;

/**
 * Simple monitor to show monitoring for threaded execution.
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:52 $
 */
public class SimpleThreadedMonitor extends DefaultProcessMonitor {

	/* @see org.mindswap.owls.process.execution.DefaultProcessMonitor#executionFailed(org.mindswap.exceptions.ExecutionException) */
	@Override
	public void executionFailed(final ExecutionException e) {
		super.executionFailed(e);
		System.out.println("--------------");
	}

	/* @see org.mindswap.owls.process.execution.DefaultProcessMonitor#executionFinished(org.mindswap.owls.process.Process, org.mindswap.query.ValueMap, org.mindswap.query.ValueMap) */
	@Override
	public void executionFinished(final Process process, final ValueMap<Input, OWLValue> inputs,
		final ValueMap<Output, OWLValue> outputs) {
		super.executionFinished(process, inputs, outputs);
		System.out.println("--------------");
	}

	/* @see org.mindswap.owls.process.execution.DefaultProcessMonitor#executionStarted(org.mindswap.owls.process.Process, org.mindswap.query.ValueMap) */
	@Override
	public void executionStarted(final Process process, final ValueMap<Input, OWLValue> inputs) {
		super.executionStarted(process, inputs);
		System.out.println("--------------");
	}

	/* @see org.mindswap.owls.process.execution.DefaultProcessMonitor#executionContinued(org.mindswap.owls.process.Process) */
	@Override
	public void executionContinued(final Process process) {
		System.out.println("Execution of process: " + process.getLocalName() + " has continued");
		System.out.println("--------------");
	}

	public void executionInterrupted(final Process process) {
		System.out.println("Execution of process: " + process.getLocalName() + " has been interrupted");
		System.out.println("--------------");
	}

	public void getExecutionResults(final ValueMap<Parameter, OWLValue> values) {
		for (Parameter var : values.getVariables())
		{
			System.out.println(values.getStringValue(var));
		}
		System.out.println("--------------");
	}

}
