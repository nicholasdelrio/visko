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

import java.util.Set;

import org.mindswap.owls.process.Process;

/**
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:52 $
 */
public class ExecutionException extends Exception
{
	private static final long serialVersionUID = -5647891247499430965L;

	private final transient Process process;
	private Set<Exception> causes;

	/**
	 * @see Exception#Exception()
	 */
	public ExecutionException()
	{
		this((String) null, null);
	}

	/**
	 * @see Exception#Exception(String)
	 */
	public ExecutionException(final String message)
	{
		this(message, null);
	}

	/**
	 * @param message The detail message. The detail message is saved for
	 * 	later retrieval by the {@link #getMessage()} method.
	 * @param process The overall process whose execution failed.
	 */
	public ExecutionException(final String message, final Process process)
	{
		super(message);
		this.process = process;
	}

	/**
	 * @see Exception#Exception(Throwable)
	 */
	public ExecutionException(final Throwable t)
	{
		this(t, null);
	}

	/**
	 * @param cause the cause (which is saved for later retrieval by the
	 * 	{@link #getCause()} method).  (A <tt>null</tt> value is permitted,
	 * 	and indicates that the cause is nonexistent or unknown.)
	 * @param process The overall process whose execution failed.
	 */
	public ExecutionException(final Throwable cause, final Process process)
	{
		super(cause);
		this.process = process;
	}

	/**
	 * Create a new exception that was caused by a set of {@link Exception exceptions}.
	 *
	 * @param causes The set of exceptions that caused this execution exception.
	 */
	public ExecutionException(final Set<Exception> causes)
	{
		this();
		this.causes = causes;
	}

	/**
	 * @return The set of exceptions that caused this execution exception.
	 */
	public Set<Exception> getCauses()
	{
		return causes;
	}

	/**
	 * @return The overall process whose execution failed.
	 */
	public Process getProcess()
	{
		return process;
	}

	/**
	 * @return The name of the process, its URI, or its anon identifier.
	 */
	public String getProcessName()
	{
		String pn = getProcess().getName();
		pn = (pn != null)? pn : (process.isAnon())? process.getAnonID() : process.getURI().toString();
		return pn;
	}

	/* @see java.lang.Throwable#toString() */
	@Override
	public String toString()
	{
		if (causes == null) return super.toString();
		return causes.toString();
	}
}
