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
 * Created on Dec 12, 2004
 */
package org.mindswap.exceptions;

import org.mindswap.common.Variable;

/**
 *
 * @author unascribe
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:52 $
 */
public class UnboundVariableException extends RuntimeException
{
	private static final long serialVersionUID = -8042856313933773334L;

	private final transient Variable var;

	public UnboundVariableException(final Variable var)
	{
		super();
		this.var = var;
	}

	public UnboundVariableException(final Variable var, final String message)
	{
		super(message);
		this.var = var;
	}

	/**
	 * @return The variable that is unbound.
	 */
	public Variable getVariable()
	{
		return var;
	}

	/* @see java.lang.Throwable#getMessage() */
	@Override
	public String getMessage()
	{
		return (var == null)? super.getMessage() : var + " is not bound!";
	}
}
