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
 * Created on Apr 6, 2005
 */
package org.mindswap.swrl;

import java.net.URI;

import org.mindswap.common.ServiceFinder;
import org.mindswap.owl.OWLClass;
import org.mindswap.owl.OWLDataProperty;
import org.mindswap.owl.OWLDataValue;
import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLModel;
import org.mindswap.owl.OWLObjectProperty;
import org.mindswap.owl.OWLValue;
import org.mindswap.owl.list.OWLList;

/**
 * @author unascribed
 * @version $Rev: 2347 $; $Author: nick $; $Date: 2011/01/24 06:37:51 $
 */
public class SWRLFactory
{
	/**
	 * Create a new SWRL factory that uses a dedicated new KB.
	 * <p>
	 * This method is equivalent to <code>SWRLFactory.createFactory(OWLFactory.createKB())</code>.
	 *
	 * @return A new SWRL factory.
	 */
	public static ISWRLFactory createFactory()
	{
		return createFactory(OWLFactory.createKB());
	}

	/**
	 * @param model The model to which all SWRL objects created by the returned
	 * 	factory will be added.
	 * @return A new SWRL factory.
	 */
	public static ISWRLFactory createFactory(final OWLModel model)
	{
		return ServiceFinder.instance().createInstance(SWRLProvider.class,
			SWRLProvider.DEFAULT_SWRL_PROVIDER, model);
	}

	public interface ISWRLFactory
	{
		public OWLList<Atom> createList();
		public OWLList<Atom> createList(Atom atom);

		public ClassAtom createClassAtom(OWLClass c, SWRLIndividualObject arg);
		public ClassAtom createClassAtom(OWLClass c, OWLIndividual arg);

		public IndividualPropertyAtom createIndividualPropertyAtom(OWLObjectProperty p, SWRLIndividualObject arg1, SWRLIndividualObject arg2);
		public IndividualPropertyAtom createIndividualPropertyAtom(OWLObjectProperty p, OWLIndividual arg1, OWLIndividual arg2);

		public DataPropertyAtom createDataPropertyAtom(OWLDataProperty p, SWRLIndividualObject arg1, SWRLDataObject arg2);
		public DataPropertyAtom createDataPropertyAtom(OWLDataProperty p, OWLIndividual arg1, OWLValue arg2);

		public SameIndividualAtom createSameIndividualAtom(SWRLIndividualObject arg1, SWRLIndividualObject arg2);
		public SameIndividualAtom createSameIndividualAtom(OWLIndividual arg1, OWLIndividual arg2);

		public DifferentIndividualsAtom createDifferentIndividualsAtom(SWRLIndividualObject arg1, SWRLIndividualObject arg2);
		public DifferentIndividualsAtom createDifferentIndividualsAtom(OWLIndividual arg1, OWLIndividual arg2);

		public BuiltinAtom createBuiltinAtom(OWLIndividual builtin, OWLValue... args);

		public BuiltinAtom createEqual(SWRLDataObject arg1, SWRLDataObject arg2);
		public BuiltinAtom createNotEqual(SWRLDataObject arg1, SWRLDataObject arg2);
		public BuiltinAtom createLessThan(SWRLDataObject arg1, SWRLDataObject arg2);
		public BuiltinAtom createLessThanOrEqual(SWRLDataObject arg1, SWRLDataObject arg2);
		public BuiltinAtom createGreaterThan(SWRLDataObject arg1, SWRLDataObject arg2);
		public BuiltinAtom createGreaterThanOrEqual(SWRLDataObject arg1, SWRLDataObject arg2);

		public BuiltinAtom createAdd(OWLValue result, OWLValue... summands);
		public BuiltinAtom createSubtract(OWLValue result, OWLValue minuend, OWLValue subtrahend);
		public BuiltinAtom createMultiply(OWLValue result, OWLValue... factors);
		public BuiltinAtom createDivide(OWLValue result, OWLValue dividend, OWLValue divisor);
		public BuiltinAtom createMod(OWLValue result, OWLValue arg1, OWLValue arg2);
		public BuiltinAtom createPow(OWLValue result, OWLValue base, OWLValue eponent);

		public SWRLIndividualVariable createIndividualVariable(URI uri);
		public SWRLDataVariable createDataVariable(URI uri);

		public SWRLIndividual wrapIndividual(OWLIndividual ind);
		public SWRLDataValue wrapDataValue(OWLDataValue dv);
	}
}
