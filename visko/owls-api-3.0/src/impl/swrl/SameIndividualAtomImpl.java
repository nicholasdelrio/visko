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

import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.vocabulary.SWRL;
import org.mindswap.query.ValueMap;
import org.mindswap.swrl.Atom;
import org.mindswap.swrl.AtomVisitor;
import org.mindswap.swrl.SWRLIndividualObject;
import org.mindswap.swrl.SWRLObject;
import org.mindswap.swrl.SameIndividualAtom;
import org.mindswap.swrl.SWRLFactory.ISWRLFactory;

/**
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:53 $
 */
public class SameIndividualAtomImpl extends AtomImpl implements SameIndividualAtom {
	public SameIndividualAtomImpl(final OWLIndividual ind) {
		super(ind);
	}

	/* @see org.mindswap.common.Visitable#accept(org.mindswap.common.Visitor) */
	public void accept(final AtomVisitor visitor)
	{
		visitor.visit(this);
	}

	/* @see org.mindswap.swrl.Atom#apply(org.mindswap.query.ValueMap, org.mindswap.swrl.SWRLFactory.ISWRLFactory) */
	public Atom apply(final ValueMap<?, ?> binding, final ISWRLFactory swrlFactory)
	{
		return swrlFactory.createSameIndividualAtom(
			getIndidividualObject(getArgument1(), binding, swrlFactory),
			getIndidividualObject(getArgument2(), binding, swrlFactory));
	}

	/* @see org.mindswap.swrl.SameIndividualAtom#getArgument1() */
	public SWRLIndividualObject getArgument1() {
		return getPropertyAs(SWRL.argument1, SWRLIndividualObject.class);
	}

	/* @see org.mindswap.swrl.SameIndividualAtom#getArgument2() */
	public SWRLIndividualObject getArgument2() {
		return getPropertyAs(SWRL.argument2, SWRLIndividualObject.class);
	}

	/* @see org.mindswap.swrl.Atom#getArgumentCount() */
	public int getArgumentCount() {
		return 2;
	}

	/* @see org.mindswap.swrl.Atom#getArgument(int) */
	public SWRLObject getArgument(final int index) {
		if(index == 0) return getArgument1();
		if(index == 1) return getArgument2();

		throw new IndexOutOfBoundsException("Illegal argument index: " + index + " for a " +
			SameIndividualAtom.class.getSimpleName());
	}

	/* @see org.mindswap.swrl.Atom#setArgument(int, org.mindswap.swrl.SWRLObject) */
	public void setArgument(final int index, final SWRLObject obj) {
		if(index > 1) throw new IndexOutOfBoundsException("Illegal argument index: " + index + " for a " +
			SameIndividualAtom.class.getSimpleName());

		if(obj instanceof SWRLIndividualObject) {
			if(index == 0) setArgument1((SWRLIndividualObject) obj);
			else setArgument2((SWRLIndividualObject) obj);
		}
		else throw new IllegalArgumentException(SameIndividualAtom.class.getSimpleName() +
			" argument should be a " + SWRLIndividualObject.class.getSimpleName());
	}

	/* @see org.mindswap.swrl.SameIndividualAtom#setArgument1(org.mindswap.swrl.SWRLIndividualObject) */
	public void setArgument1(final SWRLIndividualObject obj) {
		setProperty(SWRL.argument1, obj);
	}

	/* @see org.mindswap.swrl.SameIndividualAtom#setArgument2(org.mindswap.swrl.SWRLIndividualObject) */
	public void setArgument2(final SWRLIndividualObject obj) {
		setProperty(SWRL.argument2, obj);
	}

	/* @see impl.owl.WrappedIndividual#toString() */
	@Override
	public String toString() {
		return "sameAs(" + getArgument1() + ", " + getArgument2() + ")";
	}

	/* @see org.mindswap.swrl.Atom#evaluate(org.mindswap.query.ValueMap) */
	public void evaluate(final ValueMap<?,?> values)
	{
		final SWRLIndividualObject arg1 = getArgument1();
		final SWRLIndividualObject arg2 = getArgument2();
		OWLIndividual ind1 = getVariableIndividualValue(arg1, values);
		OWLIndividual ind2 = getVariableIndividualValue(arg2, values);

		ind1 = (ind1 != null)? ind1 : arg1;
		ind2 = (ind2 != null)? ind2 : arg2;
		ind1.addSameAs(ind2);
	}

}
