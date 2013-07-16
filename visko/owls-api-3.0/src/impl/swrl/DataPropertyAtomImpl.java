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

import org.mindswap.owl.OWLDataProperty;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLValue;
import org.mindswap.owl.vocabulary.SWRL;
import org.mindswap.query.ValueMap;
import org.mindswap.swrl.Atom;
import org.mindswap.swrl.AtomVisitor;
import org.mindswap.swrl.DataPropertyAtom;
import org.mindswap.swrl.SWRLDataObject;
import org.mindswap.swrl.SWRLDataValue;
import org.mindswap.swrl.SWRLDataVariable;
import org.mindswap.swrl.SWRLIndividualObject;
import org.mindswap.swrl.SWRLObject;
import org.mindswap.swrl.SWRLFactory.ISWRLFactory;

/**
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:53 $
 */
public class DataPropertyAtomImpl extends AtomImpl implements DataPropertyAtom {
	public DataPropertyAtomImpl(final OWLIndividual ind) {
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
		return swrlFactory.createDataPropertyAtom(getPropertyPredicate(),
			getIndidividualObject(getArgument1(), binding, swrlFactory),
			getDataObject(getArgument2(), binding, swrlFactory));
	}

	/* @see org.mindswap.swrl.DataPropertyAtom#getPropertyPredicate() */
	public OWLDataProperty getPropertyPredicate() {
		return getPropertyAs(SWRL.propertyPredicate, OWLDataProperty.class);
	}

	/* @see org.mindswap.swrl.DataPropertyAtom#setPropertyPredicate(org.mindswap.owl.OWLDataProperty) */
	public void setPropertyPredicate(final OWLDataProperty p) {
		setProperty(SWRL.propertyPredicate, p.castTo(OWLIndividual.class));
	}

	/* @see org.mindswap.swrl.DataPropertyAtom#getArgument1() */
	public SWRLIndividualObject getArgument1() {
		return getPropertyAs(SWRL.argument1, SWRLIndividualObject.class);
	}

	/* @see org.mindswap.swrl.DataPropertyAtom#getArgument2() */
	public SWRLDataObject getArgument2()
	{
		SWRLDataObject arg = getPropertyAs(SWRL.argument2, SWRLDataVariable.class);
		if (arg == null) arg = getPropertyAs(SWRL._argument2, SWRLDataValue.class);
		return arg;
	}

	/* @see org.mindswap.swrl.Atom#getArgumentCount() */
	public int getArgumentCount() {
		return 2;
	}

	/* @see org.mindswap.swrl.Atom#getArgument(int) */
	public SWRLObject getArgument(final int index) {
		if(index == 0) return getArgument1();
		if(index == 1) return getArgument2();

		throw new IndexOutOfBoundsException("Illegal argument index: "+index+" for a " +
			DataPropertyAtom.class.getSimpleName());
	}

	/* @see org.mindswap.swrl.Atom#setArgument(int, org.mindswap.swrl.SWRLObject) */
	public void setArgument(final int index, final SWRLObject obj) {
		if(index > 1) throw new IndexOutOfBoundsException("Illegal argument index: "+index+" for a " +
			DataPropertyAtom.class.getSimpleName());


		if(index == 0) {
			if(obj instanceof SWRLIndividualObject) setArgument1((SWRLIndividualObject) obj);
			else throw new IllegalArgumentException("First argument of a " +
				DataPropertyAtom.class.getSimpleName() + " should be a " + SWRLIndividualObject.class.getSimpleName());
		}
		else {
			if(obj instanceof SWRLDataObject) setArgument2((SWRLDataObject) obj);
			else throw new IllegalArgumentException("Second argument of a " +
				DataPropertyAtom.class.getSimpleName() + " should be a " + SWRLDataObject.class.getSimpleName());
		}
	}

	/* @see org.mindswap.swrl.DataPropertyAtom#setArgument1(org.mindswap.swrl.SWRLIndividualObject) */
	public void setArgument1(final SWRLIndividualObject obj) {
		setProperty(SWRL.argument1, obj);
	}

	/* @see org.mindswap.swrl.DataPropertyAtom#setArgument2(org.mindswap.swrl.SWRLDataObject) */
	public void setArgument2(final SWRLDataObject obj) {
		if (obj.isDataValue()) setProperty(SWRL._argument2, obj);
		else setProperty(SWRL.argument2, (SWRLDataVariable) obj);
	}

	/* @see impl.owl.WrappedIndividual#toString() */
	@Override
	public String toString() {
		return "(" + getArgument1() + " " + getPropertyPredicate().getQName() + " " + getArgument2() + ")";
	}

	/* @see org.mindswap.swrl.Atom#evaluate(org.mindswap.query.ValueMap) */
	public void evaluate(final ValueMap<?,?> values)
	{
		final SWRLIndividualObject subject = getArgument1();
		final SWRLDataObject object = getArgument2();
		OWLIndividual subjectInd = getVariableIndividualValue(subject, values);
		OWLValue objectDV = getVariableDataValue(object, values);

		subjectInd = (subjectInd != null)? subjectInd : subject;
		objectDV = (objectDV != null)? objectDV : object;
		subjectInd.setProperty(getPropertyPredicate(), objectDV);
	}

}
