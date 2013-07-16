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
 * Created on Dec 28, 2004
 */
package impl.swrl;

import impl.owl.WrappedIndividual;

import org.mindswap.common.Variable;
import org.mindswap.owl.OWLDataValue;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.query.ValueMap;
import org.mindswap.swrl.Atom;
import org.mindswap.swrl.SWRLDataObject;
import org.mindswap.swrl.SWRLIndividualObject;
import org.mindswap.swrl.SWRLFactory.ISWRLFactory;

/**
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:53 $
 */
public abstract class AtomImpl extends WrappedIndividual implements Atom
{
	public AtomImpl(final OWLIndividual ind) {
		super(ind);
	}

	protected SWRLIndividualObject getIndidividualObject(final SWRLIndividualObject arg,
		final ValueMap<?, ?> binding, final ISWRLFactory swrlFactory)
	{
		final OWLIndividual ind = getVariableIndividualValue(arg, binding);
		return (ind != null)? swrlFactory.wrapIndividual(ind) : arg;
	}

	protected OWLIndividual getVariableIndividualValue(final SWRLIndividualObject arg,
		final ValueMap<?, ?> binding)
	{
		return (arg.isVariable() && binding != null)? binding.getIndividualValue((Variable) arg) : null;
	}

	protected SWRLDataObject getDataObject(final SWRLDataObject arg,
		final ValueMap<?, ?> binding, final ISWRLFactory swrlFactory)
	{
		final OWLDataValue dv = getVariableDataValue(arg, binding);
		return (dv != null)? swrlFactory.wrapDataValue(dv) : arg;
	}

	protected OWLDataValue getVariableDataValue(final SWRLDataObject arg,
		final ValueMap<?, ?> binding)
	{
		return (arg.isVariable() && binding != null)? binding.getDataValue((Variable) arg) : null;
	}
}
