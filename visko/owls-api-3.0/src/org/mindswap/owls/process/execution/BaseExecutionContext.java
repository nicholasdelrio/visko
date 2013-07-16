/*
 * Created on 26.04.2008
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

import java.util.Map.Entry;

import org.mindswap.owl.OWLValue;
import org.mindswap.owls.process.ControlConstruct;
import org.mindswap.owls.process.variable.Input;
import org.mindswap.owls.process.variable.Local;
import org.mindswap.owls.process.variable.Output;
import org.mindswap.owls.process.variable.ProcessVar;
import org.mindswap.query.ValueMap;

/**
 * This class stores the (current) values of process variables in the scope
 * of a process instance that is currently being executed. It is passed to
 * enclosed {@link ControlConstruct control constructs} in the course of
 * execution. Another synonymous name for this class could be <em>process
 * whiteboard</em>.
 * <p>
 * Instances of this class are immutable after they have been created, but its
 * property {@link #getValues()} remains modifiable!
 *
 * @author unascribed
 * @version $Rev: 2319 $; $Author: nick $; $Date: 2011/01/24 06:37:52 $
 */
public class BaseExecutionContext implements ExecutionContext
{
	private final boolean cachingPermitted;
	private final ValueMap<ProcessVar, OWLValue> values;

	/**
	 * Creates a new context initially having no {@link #getValues() values}.
	 * Caching is initially permitted, i.e., {@link #isCachingPermitted()}
	 * returns <code>true</code>.
	 */
	public BaseExecutionContext()
	{
		this(new ValueMap<ProcessVar, OWLValue>(), true);
	}

	/**
	 * Creates a new context initially having no {@link #getValues() values}.
	 * Caching will be set according to the parameter.
	 *
	 * @param cachingPermitted Whether the execution engine may use caching in
	 * 	order to boost performance. The details when caching can be used and
	 * 	what is actually cached are up to be defined by process execution
	 * 	engine implementations.
	 */
	public BaseExecutionContext(final boolean cachingPermitted)
	{
		this(new ValueMap<ProcessVar, OWLValue>(), cachingPermitted);
	}

	/**
	 * Creates a new context initially containing the given process variable
	 * mappings. Caching is initially permitted, i.e., {@link #isCachingPermitted()}
	 * returns <code>true</code>.
	 *
	 * @param values A container of initial process variable mappings;
	 * 	usually inputs supposed to be	consumed (outputs produced, and locals).
	 * @throws IllegalArgumentException In case <code>values</code> is <code>null</code>.
	 */
	public BaseExecutionContext(final ValueMap<ProcessVar, OWLValue> values)
	{
		this(values, true);
	}

	/**
	 * Creates a new context initially containing the given process variable
	 * mappings. Caching will be set according to the second parameter.
	 *
	 * @param values A container of initial process variable mappings;
	 * 	usually inputs supposed to be	consumed (outputs produced, and locals).
	 * @param cachingPermitted Whether the execution engine may use caching in
	 * 	order to boost performance. The details when caching can be used and
	 * 	what is actually cached are up to be defined by process execution
	 * 	engine implementations.
	 * @throws IllegalArgumentException In case <code>values</code> is <code>null</code>.
	 */
	public BaseExecutionContext(final ValueMap<ProcessVar, OWLValue> values, final boolean cachingPermitted)
	{
		if (values == null) throw new IllegalArgumentException("Process variable map was null.");
		this.cachingPermitted = cachingPermitted;
		this.values = values;
	}

	/**
	 * Add the given collection of input-value mappings to this value map. If
	 * this map already contains a mapping for some of the given inputs it will
	 * be replaced.
	 *
	 * @param inputs The collection of input-value mappings to add.
	 * @return A value larger than zero if previous input-value mappings
	 * 	were replaced, whereby the number reflects how many previous
	 * 	mappings were replaced.
	 */
	public int addInputs(final ValueMap<Input, OWLValue> inputs)
	{
		return addValues(inputs);
	}

	/**
	 * Add the given collection of local-value mappings to this value map. If
	 * this map already contains a mapping for some of the given locals it will
	 * be replaced.
	 *
	 * @param locals The collection of local-value mappings to add.
	 * @return A value larger than zero if previous local-value mappings
	 * 	were replaced, whereby the number reflects how many previous
	 * 	mappings were replaced.
	 */
	public int addLocals(final ValueMap<Local, OWLValue> locals)
	{
		return addValues(locals);
	}

	/**
	 * Add the given collection of output-value mappings to this value map. If
	 * this map already contains a mapping for some of the given outputs it will
	 * be replaced.
	 *
	 * @param inputs The collection of input-value mappings to add.
	 * @return A value larger than zero if previous output-value mappings
	 * 	were replaced, whereby the number reflects how many previous
	 * 	mappings were replaced.
	 */
	public int addOutputs(final ValueMap<Output, OWLValue> inputs)
	{
		return addValues(inputs);
	}

	/**
	 * @return A filtered view of the {@link #getValues() map of values} containing
	 * 	only mappings that were injected at object instantiation time, i.e., the
	 * 	input mappings.
	 */
	public ValueMap<Input, OWLValue> getInputs()
	{
		return getValues(Input.class);
	}

	/**
	 * @return A filtered view of the {@link #getValues() map of values} containing
	 * 	only mappings that were injected at object instantiation time, i.e., the
	 * 	input mappings.
	 */
	public ValueMap<Local, OWLValue> getLocals()
	{
		return getValues(Local.class);
	}

	/**
	 * @return A filtered view of the {@link #getValues() map of values} containing
	 * 	only mappings that were added after object instantiation time, i.e., the
	 * 	output mappings.
	 */
	public ValueMap<Output, OWLValue> getOutputs()
	{
		return getValues(Output.class);
	}

	/**
	 * @return The union of input, local, and output mappings.
	 */
	public ValueMap<ProcessVar, OWLValue> getValues()
	{
		return values;
	}

	/* @see org.mindswap.owls.process.execution.ExecutionContext#isCachingPermitted() */
	public boolean isCachingPermitted()
	{
		return cachingPermitted;
	}

	private int addValues(final ValueMap<? extends ProcessVar, OWLValue> valueMappings)
	{
		int replaced = 0;
		for (Entry<? extends ProcessVar, OWLValue> entry : valueMappings)
		{
			if (values.setValue(entry.getKey(), entry.getValue()) != null) replaced++;
		}
		return replaced;
	}

	private <P extends ProcessVar> ValueMap<P, OWLValue> getValues(final Class<P> paramType)
	{
		ValueMap<P, OWLValue> result = new ValueMap<P, OWLValue>();
		ProcessVar p;
		for (Entry<ProcessVar, OWLValue> entry : values)
		{
			p = entry.getKey();
			if (paramType.isInstance(p)) result.setValue(paramType.cast(p), entry.getValue());
		}
		return result;
	}
}
