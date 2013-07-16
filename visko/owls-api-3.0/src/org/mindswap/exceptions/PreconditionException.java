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
 * Created on Jan 5, 2005
 */
package org.mindswap.exceptions;

import org.mindswap.owls.expression.Condition;
import org.mindswap.owls.process.Process;

/**
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:52 $
 */
public abstract class PreconditionException extends ExecutionException
{
	private static final long serialVersionUID = 4320590750984989018L;

	private final Condition<?> condition;

	/**
	 * @param process The overall process whose execution failed.
	 * @param condition The precondition that was not satisfied and caused the
	 * 	execution failure.
	 */
	public PreconditionException(final Process process, final Condition<?> condition)
	{
		super((String) null, process);
		this.condition = condition;
	}

	/**
	 * @return The precondition that was not satisfied and caused the
	 * 	execution failure.
	 */
	public Condition<?> getCondition()
	{
		return condition;
	}
}
