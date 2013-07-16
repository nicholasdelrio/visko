/*
 * Created on Dec 17, 2004
 *
 * The MIT License
 *
 * (c) 2004 Evren Sirin
 * (c) 2009 Thorsten Möller - University of Basel Switzerland
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
package org.mindswap.owls.process;

import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owls.expression.Condition;

/**
 * General interface to define a multi-conditional constructs such as
 * {@link Process}, {@link Result}, etc. In contrast to {@link Conditional},
 * multi-conditionals may have none to many conditions.
 *
 * @author unascribed
 * @version $Rev: 2323 $; $Author: nick $; $Date: 2011/01/24 06:37:52 $
 */
public interface MultiConditional
{
	/**
	 * @return The list of all conditions. Returns the empty list if none exist.
	 */
	public OWLIndividualList<Condition> getConditions();

	public void addCondition(Condition<?> condition);

	/**
	 * Removes the given condition by breaking the corresponding property
	 * (as defined by sub classes). The condition itself is not touched at all.
	 *
	 * @param condition The condition to remove. A value of
	 * 	<code>null</code> will remove all conditions.
	 */
	public void removeCondition(Condition<?> condition);

}
