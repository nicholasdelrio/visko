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
 * Created on Dec 28, 2003
 *
 */
package org.mindswap.owls.process;

/**
 * An If-Then-Else is a binary branching construct. Based on the evaluation of
 * a condition, the execution branches to one branch or the other. For deeper
 * branching, nest this construct, or consider if you can use a {@link Choice}
 * instead.
 * <p>
 * Corresponding OWL-S concept {@link org.mindswap.owls.vocabulary.OWLS.Process#IfThenElse}.
 *
 * @author unascribed
 * @version $Rev: 2319 $; $Author: nick $; $Date: 2011/01/24 06:37:52 $
 */
public interface IfThenElse extends ControlConstruct, Conditional
{
	/**
	 * @return The control construct on which the if then part consists.
	 */
	public ControlConstruct getThen();
	/**
	 * @param cc The control construct on which the if then part consists.
	 */
	public void setThen(ControlConstruct cc);
	/**
	 * Removes the <code>process:then</code> property from this construct.
	 * Note, that the then part itself is not touched at all.
	 */
	public void removeThen();

	/**
	 * @return The control construct on which the else part consists.
	 */
	public ControlConstruct getElse();
	/**
	 * @param cc The control construct on which the else part consists.
	 */
	public void setElse(ControlConstruct cc);
	/**
	 * Removes the <code>process:else</code> property from this construct.
	 * Note, that the else part itself is not touched at all.
	 */
	public void removeElse();
}
