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

import org.mindswap.owl.OWLClass;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.vocabulary.SWRL;
import org.mindswap.query.ValueMap;
import org.mindswap.swrl.Atom;
import org.mindswap.swrl.AtomVisitor;
import org.mindswap.swrl.ClassAtom;
import org.mindswap.swrl.SWRLIndividualObject;
import org.mindswap.swrl.SWRLObject;
import org.mindswap.swrl.SWRLFactory.ISWRLFactory;

/**
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:53 $
 */
public class ClassAtomImpl extends AtomImpl implements ClassAtom {
	public ClassAtomImpl(final OWLIndividual ind) {
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
		return swrlFactory.createClassAtom(getClassPredicate(),
			getIndidividualObject(getArgument1(), binding, swrlFactory));
	}

	/* @see org.mindswap.swrl.ClassAtom#getClassPredicate() */
	public OWLClass getClassPredicate() {
		return getPropertyAs(SWRL.classPredicate, OWLClass.class);
	}

	/* @see org.mindswap.swrl.ClassAtom#setClassPredicate(org.mindswap.owl.OWLClass) */
	public void setClassPredicate(final OWLClass c) {
		setProperty(SWRL.classPredicate, c.castTo(OWLIndividual.class));
	}

	/* @see org.mindswap.swrl.ClassAtom#getArgument1() */
	public SWRLIndividualObject getArgument1() {
		return getPropertyAs(SWRL.argument1, SWRLIndividualObject.class);
	}

	/* @see org.mindswap.swrl.Atom#getArgumentCount() */
	public int getArgumentCount() {
		return 1;
	}

	/* @see org.mindswap.swrl.Atom#getArgument(int) */
	public SWRLObject getArgument(final int index) {
		if(index > 0) throw new IndexOutOfBoundsException("Illegal argument index: " + index + " for a " +
			ClassAtom.class.getSimpleName());

		return getArgument1();
	}

	/* @see org.mindswap.swrl.Atom#setArgument(int, org.mindswap.swrl.SWRLObject) */
	public void setArgument(final int index, final SWRLObject term) {
		if(index > 0) throw new IndexOutOfBoundsException("Illegal argument index: " + index + " for a " +
			ClassAtom.class.getSimpleName());

		if(term instanceof SWRLIndividualObject) setArgument1((SWRLIndividualObject) term);
		else throw new IllegalArgumentException(ClassAtom.class.getSimpleName() + " argument should be a " +
			SWRLIndividualObject.class.getSimpleName());
	}

	/* @see org.mindswap.swrl.ClassAtom#setArgument1(org.mindswap.swrl.SWRLIndividualObject) */
	public void setArgument1(final SWRLIndividualObject obj) {
		setProperty(SWRL.argument1, obj);
	}

	/* @see impl.owl.WrappedIndividual#toString() */
	@Override
	public String toString() {
		final OWLClass classPredicate = getClassPredicate();
		if (classPredicate == null) return "Missing_class_predicate";
		return "(" + getArgument1() + " rdf:type " + classPredicate.getQName() + ")";
	}

	/* @see org.mindswap.swrl.Atom#evaluate(org.mindswap.query.ValueMap) */
	public void evaluate(final ValueMap<?,?> values)
	{
		SWRLIndividualObject subject = getArgument1();
		OWLIndividual subjectInd = getVariableIndividualValue(subject, values);
		if (subjectInd != null) subjectInd.addType(getClassPredicate());
		else subject.addType(getClassPredicate());
	}

}
