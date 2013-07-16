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
 * Created on Dec 27, 2003
 *
 */
package org.mindswap.owls.process;


/**
 * Simple processes provide an (optional) level of abstraction.  They
 * describe themselves in the same way as Atomic processes, but, unlike
 * atomics, they give additional characterization of how they work, in
 * terms of other processes (using the <tt>process:expandsTo</tt> and
 * <tt>process:realizedBy</tt> properties). They are not directly callable.
 * <p>
 * A simple process can be thought of as a <em>view</em> on either an atomic
 * or a composite process. Simple processes provide a means of characterizing
 * other processes at varying levels of granularity, for purposes of
 * planning and reasoning.
 *
 * @author unascribed
 * @version $Rev: 2319 $; $Author: nick $; $Date: 2011/01/24 06:37:52 $
 */
public interface SimpleProcess extends Process
{
	public AtomicProcess getAtomicProcess();
	public void removeAtomicProcess();
	public void setAtomicProcess(AtomicProcess process);

	public CompositeProcess getCompositeProcess();
	public void removeCompositeProcess();
	public void setCompositeProcess(CompositeProcess process);

}