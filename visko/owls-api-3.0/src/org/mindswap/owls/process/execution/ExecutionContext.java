/*
 * Created 10.09.2009
 *
 * The MIT License
 *
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
package org.mindswap.owls.process.execution;

/**
 * Execution contexts provide information used by {@link ProcessExecutionEngine
 * execution engines} to drive execution of {@link org.mindswap.owls.process.Process
 * processes}.
 * <p>
 * This interface is only a stub and sub interfaces are expected to enrich it.
 *
 * @author unascribed
 * @version $Rev: 2319 $; $Author: nick $; $Date: 2011/01/24 06:37:52 $
 */
public interface ExecutionContext
{
	/**
	 * @return <code>true</code> if execution can use caching in order to boost
	 * 	performance.
	 */
	public boolean isCachingPermitted();

//	/**
//    * Get a parameter that was explicitly set with setParameter.
//    *
//    * @param name of <code>Object</code> to get
//    * @return A parameter that has been set with {@link #setParameter(String, Object)}.
//    */
//   public abstract Object getParameter(String name);
//
//	/**
//    * Add a parameter supposed to be used by the execution engine.
//    *
//    * @param name The name of the parameter.
//    * @param value The value object.  This can be any valid Java object.
//    * @throws NullPointerException If value is null.
//    */
//    public abstract void setParameter(String name, Object value);

}
