/*
 * Created on May 3, 2005
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
package org.mindswap.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.mindswap.common.ClosableIterator;
import org.mindswap.exceptions.NotImplementedException;
import org.mindswap.owl.OWLClass;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owl.OWLType;
import org.mindswap.owl.OWLValue;
import org.mindswap.owl.list.OWLList;
import org.mindswap.owls.expression.Expression;
import org.mindswap.owls.process.Process;
import org.mindswap.owls.process.variable.Input;
import org.mindswap.owls.process.variable.ProcessVar;
import org.mindswap.query.ValueMap;
import org.mindswap.swrl.Atom;
import org.mindswap.swrl.ClassAtom;
import org.mindswap.swrl.SWRLFactory;
import org.mindswap.swrl.SWRLIndividualObject;
import org.mindswap.swrl.SWRLFactory.ISWRLFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author unascribed
 * @version $Rev: 2350 $; $Author: nick $; $Date: 2011/01/24 06:37:53 $
 */
public class ProcessUtils
{
	private static final Logger logger = LoggerFactory.getLogger(ProcessUtils.class);

	/**
	 * Returns a map from inputs of the given process to a set of values, which
	 * show the allowed values for that input found in the KB. Only the
	 * KB that the process belongs to is searched (there might be other allowed
	 * values that are not in the KB). Preconditions of the process (if any)
	 * possibly constrain the set of allowed values.
	 *
	 * @param process
	 * @return
	 */
	public static Map<Input, Set<OWLValue>> getAllowedValues(final Process process)
	{
		return getAllowedValues(process.getInputs(), process.getConditions(),
			new ValueMap<Input, OWLIndividual>(), process.getKB());
	}

	/**
	 * Given a partial input binding, returns a map from inputs to a set of
	 * values which show the allowed values for that input that are found in the
	 * KB. Only the KB that the process belongs to is searched (there might be
	 * other allowed values that are not in the KB). There is no entry in the
	 * returned map for the inputs that are already bound in the given binding.
	 * Preconditions of the process (if any) possibly constrain the set of allowed
	 * values.
	 *
	 * @param process
	 * @param initialBinding
	 * @return
	 */
	public static Map<Input, Set<OWLValue>> getAllowedValues(final Process process,
		final ValueMap<Input, ?> initialBinding)
	{
		return getAllowedValues(process.getInputs(), process.getConditions(), initialBinding, process.getKB());
	}

	/**
	 * Given a partial process variable binding, returns a map from process
	 * variables to a set of values which show the allowed values for that
	 * variable that are found in the KB (there might be other allowed values
	 * that are not in the KB). There is no entry in the returned map for the
	 * process variables that are already bound in the given binding.
	 * Preconditions of the process (if any) possibly constrain the set of
	 * allowed values.
	 *
	 * @param procVars The process variables to query for allowed values.
	 * @param constraints The set of expressions that constrain the set of allowed
	 * 	values.
	 * @param initialBindings Initial bindings to be applied to be applied to the
	 * 	given process variables.
	 * @param kb The KB to query.
	 * @return Each variable that was not bound by the initial bindings associated
	 * 	with the set of allowed values.
	 */
	public static final <V extends ProcessVar> Map<V, Set<OWLValue>> getAllowedValues(
		final OWLIndividualList<V> procVars, final OWLIndividualList<? extends Expression> constraints,
		final ValueMap<V, ?> initialBindings, final OWLKnowledgeBase kb)
	{
		if (constraints.size() > 1)
			logger.warn("Multiple constraints are not supported, taking into account the first constraint only!");


		Expression<?> constraint;
		if (constraints.isEmpty()) constraint = null;
		else constraint = constraints.get(0);

		if (constraint == null || constraint instanceof Expression.SWRL)
		{
			return getAllowedValuesSWRL(procVars, (Expression.SWRL) constraint, initialBindings, kb);
		}

		// TODO implement, at least to support SPARQL
		throw new NotImplementedException("Support for expression languages other than SRWL not yet implemented.");
	}

	private static final <V extends ProcessVar> Map<V, Set<OWLValue>> getAllowedValuesSWRL(
		final OWLIndividualList<V> procVars, final Expression.SWRL constraint,
		final ValueMap<V, ?> initBinding, final OWLKnowledgeBase kb)
	{
		final ISWRLFactory swrlFactory = SWRLFactory.createFactory(kb);

		OWLList<Atom> atoms = null;
		if (constraint == null) atoms = swrlFactory.createList();
		else atoms = constraint.getBody();

		// The set of allowed values of some process variable is the set of instances
		// of the process variable type, thus, we use a SWRL ClassAtom to express this.
		final Map<V, Set<OWLValue>> allowedValues = new HashMap<V, Set<OWLValue>>();
		final List<V> resultVars = new ArrayList<V>();
		for (V procVar : procVars)
		{
			// skip process variables which already have a binding
			if (initBinding.hasValue(procVar)) continue;

			final OWLType inputType = procVar.getParamType();
			if (inputType.isClass())
			{
				allowedValues.put(procVar, new HashSet<OWLValue>());
				resultVars.add(procVar);

				final ClassAtom typeAtom =
					swrlFactory.createClassAtom((OWLClass) inputType, procVar.castTo(SWRLIndividualObject.class));
				atoms = atoms.cons(typeAtom);
			}
		}

		final ClosableIterator<ValueMap<V, OWLValue>> results = kb.makeQuery(atoms, resultVars).execute(initBinding);
		try
		{
			while (results.hasNext())
			{
				final ValueMap<V, OWLValue> binding = results.next();
				for (Entry<V, OWLValue> entry : binding)
				{
					final V procVariable = entry.getKey();
					final Set<OWLValue> set = allowedValues.get(procVariable);
					if (set != null)
					{
						set.add(entry.getValue());
					}
				}
			}
		}
		finally
		{
			results.close();
		}

		return allowedValues;
	}

}
