// The MIT License
//
// Copyright (c) 2005 Evren Sirin
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
// IN THE SOFTWARE

/*
 * Created on Jun 27, 2005
 */
package org.mindswap.owls.process.execution;

import java.io.PrintWriter;
import java.io.Writer;

import org.mindswap.exceptions.ExecutionException;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLValue;
import org.mindswap.owls.process.AtomicProcess;
import org.mindswap.owls.process.Process;
import org.mindswap.owls.process.variable.Input;
import org.mindswap.owls.process.variable.Output;
import org.mindswap.owls.process.variable.Parameter;
import org.mindswap.query.ValueMap;

/**
 * A simple process monitor implementation that prints the progress to
 * standard out (default constructor), or to a given writer.
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:52 $
 */
public class DefaultProcessMonitor extends BaseProcessExecutionMonitor
{
	protected PrintWriter out;

	/**
	 * Will write progress to standard out.
	 */
	public DefaultProcessMonitor()
	{
		super();
		this.out = new PrintWriter(System.out);
	}

	/**
	 * @param out The writer to be used for writing progress to.
	 */
	public DefaultProcessMonitor(final Writer out)
	{
		super();
		this.out = (out instanceof PrintWriter)? (PrintWriter) out : new PrintWriter(out);
	}

	/* @see org.mindswap.owls.process.execution.ProcessExecutionMonitor#executionStarted() */
	public void executionStarted() {
		out.println();
		out.flush();
	}

	/* @see org.mindswap.owls.process.execution.ProcessExecutionMonitor#executionFinished() */
	public void executionFinished() {
		out.println();
		out.flush();
	}

	/* @see org.mindswap.owls.process.execution.ProcessExecutionMonitor#executionStarted(org.mindswap.owls.process.Process, org.mindswap.query.ValueMap) */
	public void executionStarted(final Process process, final ValueMap<Input, OWLValue> inputs) {
		out.println("Start executing process " + process);
		out.println("Inputs: ");
		for (Input input : process.getInputs())
		{
			out.print( input.toPrettyString() + " =  " );
			OWLValue value = inputs.getValue( input );
			if(value == null) value = input.getConstantValue();
			if(value == null)	out.println("<< NO VALUE >>");
			else if(value.isDataValue()) out.println(value);
			else {
				OWLIndividual ind = (OWLIndividual) value;
				if(ind.isAnon()) out.println(ind.toRDF(false, false));
				else out.println(value);
			}
		}
		out.flush();
	}

	/* @see org.mindswap.owls.process.execution.ProcessExecutionMonitor#executionFinished(org.mindswap.owls.process.Process, org.mindswap.query.ValueMap, org.mindswap.query.ValueMap) */
	public void executionFinished(final Process process, final ValueMap<Input, OWLValue> inputs,
		final ValueMap<Output, OWLValue> outputs) {
		out.println("Execution finished for " + process);
		out.println("Outputs: ");
		for (Output output : process.getOutputs())
		{
			out.print( output.toPrettyString() + " =  ");
			OWLValue value = outputs.getValue(output);
			if(value == null) out.println("<< NO VALUE >>");
			else if(value.isDataValue()) out.println(value);
			else {
				OWLIndividual ind = (OWLIndividual) value;
				if(ind.isAnon()) out.println(ind.toRDF(false, false));
				else out.println(value);
			}
		}
		out.flush();
	}

	/* @see org.mindswap.owls.process.execution.ProcessExecutionMonitor#executionFailed(org.mindswap.exceptions.ExecutionException) */
	public void executionFailed(final ExecutionException e) {
		out.println("Execution failed: ");
		out.println(e);
		out.flush();
	}

	/* @see org.mindswap.owls.process.execution.ProcessExecutionMonitor#executionContinued(org.mindswap.owls.process.Process) */
	public void executionContinued(final Process process)
	{
		// currently, we do nothing
	}

	/* @see org.mindswap.owls.process.execution.ProcessExecutionMonitor#executionInterrupted(org.mindswap.owls.process.Process, org.mindswap.owls.process.AtomicProcess) */
	public void executionInterrupted(final Process process, final AtomicProcess lastProcess)
	{
		// currently, we do nothing
	}

	/* @see org.mindswap.owls.process.execution.ProcessExecutionMonitor#intermediateResultsReceived(org.mindswap.query.ValueMap) */
	public void intermediateResultsReceived(final ValueMap<Parameter, OWLValue> values)
	{
		// currently, we do nothing
	}

}
