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
package impl.owls.process.execution;

import java.util.Map.Entry;

import org.mindswap.common.ClosableIterator;
import org.mindswap.exceptions.ExecutionException;
import org.mindswap.exceptions.MultipleSatisfiedPreconditionException;
import org.mindswap.exceptions.UnsatisfiedPreconditionException;
import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owl.OWLValue;
import org.mindswap.owls.expression.Condition;
import org.mindswap.owls.expression.Expression;
import org.mindswap.owls.process.Process;
import org.mindswap.owls.process.Result;
import org.mindswap.owls.process.execution.ExecutionValidator;
import org.mindswap.owls.process.variable.Existential;
import org.mindswap.owls.process.variable.ProcessVar;
import org.mindswap.query.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Standard implementation that realizes evaluation of preconditions
 * and effects (which are specified in OWL-S inside results).
 *
 * @author unascribed
 * @version $Rev:$; $Author: nick $; $Date: 2011/01/24 06:37:54 $
 */
public class StandardExecutionValidator implements ExecutionValidator
{
	private static final Logger logger = LoggerFactory.getLogger(StandardExecutionValidator.class);

	private boolean allowMultipleSatisifedPreconditions;
	private boolean checkPreconditions;
	private boolean checkResults;

	/**
	 * Pre-conditions will be checked, results will not be checked, and multiple
	 * satisfied pre-conditions are allowed.
	 */
	public StandardExecutionValidator()
	{
		this.allowMultipleSatisifedPreconditions = true;
		this.checkPreconditions = true;
		this.checkResults = false;
	}

	/* @see org.mindswap.owls.process.execution.ExecutionValidator#checkPreconditions(org.mindswap.owls.process.Process, org.mindswap.query.ValueMap, org.mindswap.owl.OWLKnowledgeBase) */
	public void checkPreconditions(final Process process, final ValueMap<ProcessVar, OWLValue> values,
		final OWLKnowledgeBase kb) throws ExecutionException
	{
		final OWLIndividualList<Condition> preconditions;
		if (checkPreconditions && (preconditions = process.getConditions()).size() > 0)
		{
			final String processName = getProcessName(process);
			final OWLIndividualList<Existential> existentials = process.getExistentials();
			for (final Condition<?> cond : preconditions)
			{
				logger.debug("Checking precondition {} of process {}.", cond, processName);

				final ClosableIterator<ValueMap<Existential, OWLValue>> results = cond.solutions(values, existentials);

				try
				{
					if (results.hasNext())
					{
						final ValueMap<Existential, OWLValue> result = results.next();

						if (results.hasNext() && !allowMultipleSatisifedPreconditions)
							throw new MultipleSatisfiedPreconditionException(process, cond);

						// finally, bind existentials based on the query result
						// every existential should have a binding in the results
						for (Entry<Existential, OWLValue> entry : result)
						{
							Existential var = entry.getKey();
							values.setValue(var, entry.getValue());
						}
					}
					else throw new UnsatisfiedPreconditionException(process, cond);
				}
				finally
				{
					results.close();
				}
			}
		}
	}

	/* @see org.mindswap.owls.process.execution.ExecutionValidator#checkResults(org.mindswap.owls.process.Process, org.mindswap.query.ValueMap, org.mindswap.owl.OWLKnowledgeBase) */
	public void checkResults(final Process process, final ValueMap<ProcessVar, OWLValue> values,
		final OWLKnowledgeBase kb) throws ExecutionException
	{
		final OWLIndividualList<Result> results;
		if (checkResults && (results = process.getResults()).size() > 0)
		{
			final String processName = getProcessName(process);
			OuterLoop: for (Result result : results)
			{
				// before we can apply effects of result we need to check if its conditions are satisfied
				InnerLoop: for (Condition<?> c : result.getConditions())
				{
					if (c.isTrue(kb, values)) continue InnerLoop;

					logger.debug(
						"Condition {} of result {} in process {} not satisifed. Result effect(s) not applied to KB.",
						new Object[] {c, result, processName});
					continue OuterLoop;
				}

				// all conditions of current result were true, thus, we can apply effects now
				for (Expression<?> effect : result.getEffects())
				{
					logger.debug("Applying effect {} in result {} of process {} to KB.",
						new Object[] {effect, result, processName});

					effect.evaluate(values);
				}
			}

			if (kb.isConsistent()) return;
			throw new ExecutionException("Inconsistent KB after applying result effect(s) of process " +
				processName + " to KB. This can be caused by an invalid output value of the process.", process);
		}
	}

	/* @see org.mindswap.owls.process.execution.ExecutionValidator#isAllowMultipleSatisifedPreconditions() */
	public boolean isAllowMultipleSatisifedPreconditions()
	{
		return allowMultipleSatisifedPreconditions;
	}

	/* @see org.mindswap.owls.process.execution.ExecutionValidator#isPreconditionCheck() */
	public boolean isPreconditionCheck()
	{
		return checkPreconditions;
	}

	/* @see org.mindswap.owls.process.execution.ExecutionValidator#isResultCheck() */
	public boolean isResultCheck()
	{
		return checkResults;
	}

	/* @see org.mindswap.owls.process.execution.ExecutionValidator#setAllowMultipleSatisifedPreconditions(boolean) */
	public void setAllowMultipleSatisfiedPreconditions(final boolean allowMultipleSatisifedPreconditions)
	{
		this.allowMultipleSatisifedPreconditions = allowMultipleSatisifedPreconditions;
	}

	/* @see org.mindswap.owls.process.execution.ExecutionValidator#setPreconditionCheck(boolean) */
	public void setPreconditionCheck(final boolean checkPreconditions)
	{
		this.checkPreconditions = checkPreconditions;
	}

	/* @see org.mindswap.owls.process.execution.ExecutionValidator#setResultCheck(boolean) */
	public void setResultCheck(final boolean checkResults)
	{
		this.checkResults = checkResults;
	}

	/* @see java.lang.Object#toString() */
	@Override
	public String toString()
	{
		return "Precondition check: " + checkPreconditions +
			", Result check: " + checkResults +
			", Allow multiple satisfied preconditions: " + allowMultipleSatisifedPreconditions;
	}

	private String getProcessName(final Process p)
	{
		String pn = p.getName();
		pn = (pn != null)? pn : (p.isAnon())? p.getAnonID() : p.getURI().toString();
		return pn;
	}

}
