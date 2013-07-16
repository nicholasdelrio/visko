/*
 * Created 26.12.2008
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
package impl.owls.process.execution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.mindswap.exceptions.DataFlowException;
import org.mindswap.owl.OWLValue;
import org.mindswap.owls.process.Perform;
import org.mindswap.owls.process.variable.Binding;
import org.mindswap.owls.process.variable.Local;
import org.mindswap.owls.process.variable.ProcessVar;
import org.mindswap.query.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author unascribed
 * @version $Rev:$; $Author: nick $; $Date: 2011/01/24 06:37:54 $
 */
public class ProcessExecutionUtil
{
	private static final Logger logger = LoggerFactory.getLogger(ProcessExecutionUtil.class);

	/**
	 * Create an unordered list of <tt>n</tt> integers whose smallest number is
	 * <tt>0</tt> and whose largest number is <code>n - 1 + exclude.length</code>,
	 * that is, excluding all numbers given by <code>exclude</code>; of course,
	 * only if they are within the interval [0,n). There are no duplicated
	 * numbers in the list.
	 *
	 * @param n Number of integers to create.
	 * @return A list of integers from <tt>0</tt> to <tt>n</tt>, excluding numbers
	 * 	specified by <code>exclude</code>.
	 */
	public static final List<Integer> createRandomIntegers(final int n, final int... exclude)
	{
		List<Integer> integers = new ArrayList<Integer>(n);
		List<Integer> excludes = new ArrayList<Integer>(exclude.length);
		for (int e : exclude)
		{
			excludes.add(Integer.valueOf(e));
		}

		int i = 0;
		while (integers.size() < n)
		{
			Integer integer = Integer.valueOf(i++);
			if (excludes.remove(integer)) continue;
			integers.add(integer);
		}
		Collections.shuffle(integers);
		return integers;
	}

	/**
	 * This method iterates through all bindings given and tries to put the
	 * corresponding parameter mapped to its actual value into the target value
	 * map according to the data flow specification of each binding. The given
	 * perform results map is the source where we try to get the values from.
	 *
	 * @param performResults The map of input and output values associated to
	 * 	their perform (which consumed respectively produced the values).
	 * @param target The target map to put parameter and their corresponding
	 * 	value to.
	 * @param bindings The list of bindings to iterate trough.
	 * @throws DataFlowException In case the data flow specification of some
	 * 	binding refers to an parameter or parameter values that could not be
	 * 	found.
	 */
	public static final void processValues(final Map<Perform, ValueMap<ProcessVar, OWLValue>> performResults,
		final ValueMap<ProcessVar, OWLValue> target, final List<? extends Binding<?>> bindings)
		throws DataFlowException
	{
		ProcessVar procVar;
		OWLValue paramValue;

		for (final Binding<?> binding : bindings)
		{
			procVar = binding.getProcessVar();
			paramValue = binding.getValue().getValueFromPerformResults(performResults);

			OWLValue prevValue = target.setValue(procVar, paramValue);
			if (prevValue != null)
			{
				if (procVar instanceof Local)
				{
					logger.debug("Local variable {} reassigned with new value. Previous value disposed.", procVar);
				}
				else
				{
					throw new DataFlowException("Attempt to reassign non-local variable " + procVar +
						", which is forbidden. This is likely caused by an invalid data flow specification.");
				}
			}
		}
	}

}
