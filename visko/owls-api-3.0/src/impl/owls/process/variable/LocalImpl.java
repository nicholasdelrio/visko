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
package impl.owls.process.variable;

import org.mindswap.owl.OWLIndividual;
import org.mindswap.owls.process.CompositeProcess;
import org.mindswap.owls.process.Process;
import org.mindswap.owls.process.variable.Local;
import org.mindswap.owls.vocabulary.OWLS;

/**
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:53 $
 */
public abstract class LocalImpl extends ProcessVarImpl implements Local
{
	public LocalImpl(final OWLIndividual ind)
	{
		super(ind);
	}

	/* @see org.mindswap.owls.process.variable.ProcessVar#getProcess() */
	public CompositeProcess getProcess()
	{
		return getIncomingPropertyAs(OWLS.Process.hasLocal, CompositeProcess.class);
	}

	/* @see org.mindswap.owls.process.variable.ProcessVar#setProcess(org.mindswap.owls.process.Process) */
	public void setProcess(final Process process)
	{
		if (process instanceof CompositeProcess) setProcess((CompositeProcess) process);
		throw new IllegalArgumentException("Local variables can be used only in composite processes.");
	}

	/* @see org.mindswap.owls.process.variable.Local#setProcess(org.mindswap.owls.process.CompositeProcess) */
	public void setProcess(final CompositeProcess process)
	{
		process.addLocal(this);
	}
}
