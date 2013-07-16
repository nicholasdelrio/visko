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
 * Created on Jun 27, 2003
 */
package org.mindswap.owls.process.execution;

import java.util.EventListener;

import org.mindswap.exceptions.ExecutionException;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owl.OWLValue;
import org.mindswap.owls.process.AtomicProcess;
import org.mindswap.owls.process.CompositeProcess;
import org.mindswap.owls.process.Perform;
import org.mindswap.owls.process.Process;
import org.mindswap.owls.process.variable.Input;
import org.mindswap.owls.process.variable.Output;
import org.mindswap.owls.process.variable.ValueConstant;
import org.mindswap.query.ValueMap;

/**
 * Process execution engines implement the functionality required to execute
 * {@link AtomicProcess atomic} as well as {@link CompositeProcess composite}
 * processes.
 *
 * @author unascribed
 * @version $Rev: 2319 $; $Author: nick $; $Date: 2011/01/24 06:37:52 $
 */
public interface ProcessExecutionEngine extends EventListener
{

	/**
	 * Add the given monitor. This method may even be invoked while this engine
	 * is executing some process, i.e., the new monitor will included immediately
	 * to receive event notifications.
	 *
	 * @param monitor The monitor to add.
	 * @return <code>true</code> if the monitor was added.
	 */
	public boolean addMonitor(ProcessExecutionMonitor monitor);

	/**
	 * Remove the given monitor (provided that is was added before). This method
	 * may even be invoked while this engine is executing some process, i.e., the
	 * monitor will receive no event afterwards.
	 *
	 * @param monitor The monitor to remove.
	 * @return <code>true</code> if the monitor was removed.
	 */
	public boolean removeMonitor(ProcessExecutionMonitor monitor);

	/**
	 * @return The execution validator that is currently used for precondition
	 * 	and result evaluation.
	 */
	public ExecutionValidator getExecutionValidator();

	/**
	 * @param ev The execution validator to be used for precondition and result
	 * 	evaluation.
	 * @return The validator that was set before (if any).
	 * @throws IllegalStateException If invoked while this engine is currently
	 * 	executing some process.
	 */
	public ExecutionValidator setExecutionValidator(ExecutionValidator ev);

	/**
	 * Execute the given process with no input value bindings. Consequently, the
	 * process must not have any input parameters, or, in case of a composite,
	 * all its input value bindings must refer to constant data
	 * (see {@link ValueConstant}).
	 *
	 * @param p Process to execute.
	 * @param kb The KB where preconditions and (intermediate) results will be
	 * 	evaluated. In other words, this is simply the execution environment.
	 * 	If <code>null</code>, the KB of the process being executed is used
	 * 	instead.
	 * @return Value bindings for the output parameters upon execution.
	 * @throws ExecutionException execution failed, i.e., ended prematurely.
	 */
	public ValueMap<Output, OWLValue> execute(Process p, OWLKnowledgeBase kb) throws ExecutionException;

	/**
	 * Execute the given OWL-S process with the given input value bindings. The
	 * inputs map must bind all input parameters of the process to some value.
	 *
	 * @param p Process to execute.
	 * @param inputs Value bindings for the input parameters.
	 * @param kb The KB where preconditions and (intermediate) results will be
	 * 	evaluated. In other words, this is simply the execution environment.
	 * 	If <code>null</code>, the KB of the process being executed is used
	 * 	instead.
	 * @return Value bindings for the output parameters upon execution.
	 * @throws ExecutionException execution failed, i.e., ended prematurely.
	 */
	public ValueMap<Output, OWLValue> execute(Process p, ValueMap<Input, OWLValue> inputs, OWLKnowledgeBase kb)
		throws ExecutionException;

	/**
	 * Execute the given perform construct. Perform should contain all the
	 * required input bindings specified.
	 *
	 * @param p The perform to execute.
	 * @param kb The KB where preconditions and (intermediate) results will be
	 * 	evaluated. In other words, this is simply the execution environment.
	 * 	If <code>null</code>, the KB of the perform being executed is used
	 * 	instead.
	 * @return Value bindings for the output parameters upon execution.
	 * @throws ExecutionException execution failed, i.e., ended prematurely.
	 */
	public ValueMap<Output, OWLValue> execute(Perform p, OWLKnowledgeBase kb) throws ExecutionException;

	/**
	 * Toggle the caching policy. Implementations of this interface may use
	 * (arbitrary) caching strategies in order to boost execution performance.
	 * Since caching may not be permitted in any situation it can be set
	 * accordingly using this method.
	 * <p>
	 * For instance, the default implementation provided by the library (c.f.
	 * {@link org.mindswap.owls.OWLSFactory#createExecutionEngine()}) can cache
	 * control constructs of a composite process in order to boost performance
	 * if control constructs of the composite process are executed repeatedly.
	 * In order not to cause inconsistencies between the backing data model and
	 * the cache, it is required that the backing triples that make up the
	 * composite process do not change during execution, i.e., the process must
	 * not change. Thus, in case applications require such dynamic changes of
	 * the process while it is executed one <strong>must not</strong> activate
	 * caching in this case (when using the default execution engine).
	 *
	 * @param caching <code>true</code> to enable caching, <code>false</code>
	 * 	to disable caching.
	 * @return The previous value.
	 * @throws IllegalStateException If toggling caching policy is currently
	 * 	forbidden, for instance, because the engine is currently executing
	 * 	a process.
	 */
	public boolean setCaching(boolean caching);
}
