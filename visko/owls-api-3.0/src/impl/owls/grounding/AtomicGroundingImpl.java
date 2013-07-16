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
 */
package impl.owls.grounding;

import impl.owl.WrappedIndividual;

import org.mindswap.exceptions.ExecutionException;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLValue;
import org.mindswap.owls.grounding.AtomicGrounding;
import org.mindswap.owls.grounding.Grounding;
import org.mindswap.owls.process.AtomicProcess;
import org.mindswap.owls.process.variable.Parameter;
import org.mindswap.owls.process.variable.ProcessVar;
import org.mindswap.owls.vocabulary.OWLS;
import org.mindswap.query.ValueMap;

/**
 *
 * @author unascribed
 * @version $Rev: 2335 $; $Author: nick $; $Date: 2011/01/24 06:37:52 $
 */
public abstract class AtomicGroundingImpl<T> extends WrappedIndividual implements AtomicGrounding<T>
{

	public AtomicGroundingImpl(final OWLIndividual ind)
	{
		super(ind);
	}

	/* @see org.mindswap.owls.grounding.AtomicGrounding#getProcess() */
	public final AtomicProcess getProcess()
	{
		return getPropertyAs(OWLS.Grounding.owlsProcess, AtomicProcess.class);
	}

	/* @see org.mindswap.owls.grounding.AtomicGrounding#setProcess(org.mindswap.owls.process.AtomicProcess) */
	public final void setProcess(final AtomicProcess process)
	{
		if (hasProperty(OWLS.Grounding.owlsProcess, process)) return;

		setProperty(OWLS.Grounding.owlsProcess, process);
		process.setGrounding(this);
	}

	protected final <G extends Grounding<A, T>, A extends AtomicGrounding<T>> G getGrounding(final Class<G> gdgCLazz)
	{
		return getIncomingPropertyAs(OWLS.Grounding.hasAtomicProcessGrounding, gdgCLazz);
	}

	/**
	 * Utility method for subclasses. The given value map is searched for a
	 * OWLValue bound to the given parameter. If no value was found in the map
	 * the parameter is checked if it has a constant value associated
	 * ({@link ProcessVar#getConstantValue()}). If also no constant value exists
	 * an {@link ExecutionException} is thrown. The value is returned as soon as
	 * it was found.
	 */
	protected final <P extends Parameter> OWLValue getParameterValue(
		final P p, final ValueMap<P, OWLValue> values) throws ExecutionException
	{
		OWLValue value = values.getValue(p);
		if (value == null) value = p.getConstantValue();
		if (value == null) throw new ExecutionException("No value bound to parameter " + p +
			" (must be either constant value or dynamically provided/created at execution time)!");
		return value;
	}

}
