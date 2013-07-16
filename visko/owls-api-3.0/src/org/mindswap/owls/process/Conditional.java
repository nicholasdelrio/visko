/*
 * Created on Aug 26, 2004
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

import org.mindswap.owls.expression.Condition;

/**
 * General interface to define a conditional constructs such as
 * {@link IfThenElse}, {@link RepeatWhile}, etc. In contrast to
 * {@link MultiConditional}, conditionals have at most one condition.
 *
 * @author unascribed
 * @version $Rev: 2323 $; $Author: nick $; $Date: 2011/01/24 06:37:52 $
 */
public interface Conditional
{
	/**
	 * @return The condition or <code>null</code> if none is specified. If there
	 * 	is more than one return the first one found arbitrarily (which is
	 * 	actually an invalid situation because a conditional may have at most
	 * 	one condition).
	 */
	public Condition<?> getCondition();

	public void setCondition(Condition<?> condition);
}
