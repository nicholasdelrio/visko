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
package impl.swrl;

import java.net.URI;

import org.mindswap.owl.OWLClass;
import org.mindswap.owl.OWLDataProperty;
import org.mindswap.owl.OWLDataValue;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLModel;
import org.mindswap.owl.OWLObjectConverterRegistry;
import org.mindswap.owl.OWLObjectProperty;
import org.mindswap.owl.OWLValue;
import org.mindswap.owl.list.OWLList;
import org.mindswap.owl.vocabulary.SWRL;
import org.mindswap.owl.vocabulary.SWRLB;
import org.mindswap.swrl.Atom;
import org.mindswap.swrl.BuiltinAtom;
import org.mindswap.swrl.ClassAtom;
import org.mindswap.swrl.DataPropertyAtom;
import org.mindswap.swrl.DifferentIndividualsAtom;
import org.mindswap.swrl.IndividualPropertyAtom;
import org.mindswap.swrl.SWRLDataObject;
import org.mindswap.swrl.SWRLDataValue;
import org.mindswap.swrl.SWRLDataVariable;
import org.mindswap.swrl.SWRLIndividual;
import org.mindswap.swrl.SWRLIndividualObject;
import org.mindswap.swrl.SWRLIndividualVariable;
import org.mindswap.swrl.SWRLProvider;
import org.mindswap.swrl.SameIndividualAtom;

/**
 *
 * @author unascribed
 * @version $Rev: 2347 $; $Author: nick $; $Date: 2011/01/24 06:37:53 $
 */
public class SWRLProviderImpl implements SWRLProvider
{
	private final OWLModel model;

	/**
	 * Default constructor is only to be used by {@link OWLObjectConverterRegistry}
	 * because it leaves the required {@link OWLModel} (in which SWRL atoms will
	 * be created) <code>null</code>. For
	 */
	public SWRLProviderImpl()
	{
		model = null;
	}

	/**
	 * @param model The mandatory model to which atoms created by this factory
	 * 	will be added.
	 */
	public SWRLProviderImpl(final OWLModel model)
	{
		assert model != null : "Required OWLModel is null";
		this.model = model;
	}

	/* @see org.mindswap.swrl.SWRLFactory.SWRLProvider#createList() */
	public OWLList<Atom> createList()
	{
		return model.createList(SWRL.AtomListVocabulary);
	}

	/* @see org.mindswap.swrl.SWRLFactory.SWRLProvider#createList(org.mindswap.swrl.Atom) */
	public OWLList<Atom> createList(final Atom atom)
	{
		return model.createList(SWRL.AtomListVocabulary, atom);
	}

	public SWRLIndividualVariable createIndividualVariable(final URI uri) {
		return new SWRLIndividualVariableImpl(model.createInstance(SWRL.Variable, uri));
	}

	public SWRLDataVariable createDataVariable(final URI uri) {
		return new SWRLDataVariableImpl(model.createInstance(SWRL.Variable, uri));
	}

	public ClassAtom createClassAtom(final OWLClass c, final OWLIndividual arg) {
		return createClassAtom(c, arg.castTo(SWRLIndividualObject.class));
	}

	public ClassAtom createClassAtom(final OWLClass c, final SWRLIndividualObject arg)
	{
		final ClassAtom atom = new ClassAtomImpl(model.createInstance(SWRL.ClassAtom, null));
		atom.setClassPredicate(c);
		atom.setArgument1(arg);

		return atom;
	}

	public IndividualPropertyAtom createIndividualPropertyAtom(final OWLObjectProperty p,
		final OWLIndividual arg1, final OWLIndividual arg2)
	{
		return createIndividualPropertyAtom(p,
			arg1.castTo(SWRLIndividualObject.class),
			arg2.castTo(SWRLIndividualObject.class));
	}

	public IndividualPropertyAtom createIndividualPropertyAtom(final OWLObjectProperty p,
		final SWRLIndividualObject arg1, final SWRLIndividualObject arg2)
	{
		final IndividualPropertyAtom atom = new IndividualPropertyAtomImpl(
			model.createInstance(SWRL.IndividualPropertyAtom, null));
		atom.setPropertyPredicate(p);
		atom.setArgument1(arg1);
		atom.setArgument2(arg2);

		return atom;
	}

	public DataPropertyAtom createDataPropertyAtom(final OWLDataProperty p, final OWLIndividual arg1,
		final OWLValue arg2)
	{
		return createDataPropertyAtom(p,
			arg1.castTo(SWRLIndividualObject.class),
			arg2.castTo(SWRLDataObject.class));
	}

	public DataPropertyAtom createDataPropertyAtom(final OWLDataProperty p, final SWRLIndividualObject arg1,
		final SWRLDataObject arg2)
	{
		final DataPropertyAtom atom = new DataPropertyAtomImpl(
			model.createInstance(SWRL.DatavaluedPropertyAtom, null));
		atom.setPropertyPredicate(p);
		atom.setArgument1(arg1);
		atom.setArgument2(arg2);

		return atom;
	}

	public SameIndividualAtom createSameIndividualAtom(final OWLIndividual arg1, final OWLIndividual arg2)
	{
		return createSameIndividualAtom(
			arg1.castTo(SWRLIndividualObject.class),
			arg2.castTo(SWRLIndividualObject.class));
	}

	public SameIndividualAtom createSameIndividualAtom(final SWRLIndividualObject arg1,
		final SWRLIndividualObject arg2)
	{
		final SameIndividualAtom atom = new SameIndividualAtomImpl(
			model.createInstance(SWRL.SameIndividualAtom, null));
		atom.setArgument1(arg1);
		atom.setArgument2(arg2);

		return atom;
	}

	public DifferentIndividualsAtom createDifferentIndividualsAtom(final OWLIndividual arg1,
		final OWLIndividual arg2)
	{
		return createDifferentIndividualsAtom(
			arg1.castTo(SWRLIndividualObject.class),
			arg2.castTo(SWRLIndividualObject.class));
	}

	public DifferentIndividualsAtom createDifferentIndividualsAtom(final SWRLIndividualObject arg1,
		final SWRLIndividualObject arg2)
	{
		final DifferentIndividualsAtom atom = new DifferentIndividualsAtomImpl(
			model.createInstance(SWRL.DifferentIndividualsAtom, null));
		atom.setArgument1(arg1);
		atom.setArgument2(arg2);

		return atom;
	}

	public BuiltinAtom createEqual(final SWRLDataObject arg1, final SWRLDataObject arg2) {
		return create(SWRLB.equal, arg1, arg2);
	}

	public BuiltinAtom createNotEqual(final SWRLDataObject arg1, final SWRLDataObject arg2) {
		return create(SWRLB.notEqual, arg1, arg2);
	}

	public BuiltinAtom createLessThan(final SWRLDataObject arg1, final SWRLDataObject arg2) {
		return create(SWRLB.lessThan, arg1, arg2);
	}

	public BuiltinAtom createLessThanOrEqual(final SWRLDataObject arg1, final SWRLDataObject arg2) {
		return create(SWRLB.lessThanOrEqual, arg1, arg2);
	}

	public BuiltinAtom createGreaterThan(final SWRLDataObject arg1, final SWRLDataObject arg2) {
		return create(SWRLB.greaterThan, arg1, arg2);
	}

	public BuiltinAtom createGreaterThanOrEqual(final SWRLDataObject arg1, final SWRLDataObject arg2) {
		return create(SWRLB.greaterThanOrEqual, arg1, arg2);
	}

	public BuiltinAtom createAdd(final OWLValue result, final OWLValue... summands) {
		final OWLValue[] args = new OWLValue[summands.length + 1];
		args[0] = result;
		System.arraycopy(summands, 0, args, 1, summands.length);
		return create(SWRLB.add, args);
	}

	public BuiltinAtom createSubtract(final OWLValue result, final OWLValue minuend, final OWLValue subtrahend) {
		return create(SWRLB.subtract, minuend, subtrahend);
	}

	public BuiltinAtom createMultiply(final OWLValue result, final OWLValue... factors) {
		final OWLValue[] args = new OWLValue[factors.length + 1];
		args[0] = result;
		System.arraycopy(factors, 0, args, 1, factors.length);
		return create(SWRLB.multiply, args);
	}

	public BuiltinAtom createDivide(final OWLValue result, final OWLValue arg1, final OWLValue arg2) {
		return create(SWRLB.divide, result, arg1, arg2);
	}

	public BuiltinAtom createMod(final OWLValue result, final OWLValue arg1, final OWLValue arg2)
	{
		return create(SWRLB.mod, result, arg1, arg2);
	}

	public BuiltinAtom createPow(final OWLValue result, final OWLValue arg1, final OWLValue arg2)
	{
		return create(SWRLB.pow, result, arg1, arg2);
	}

	public BuiltinAtom createBuiltinAtom(final OWLIndividual builtin, final OWLValue... args)
	{
		if (args == null || args.length < 1)
			throw new IllegalArgumentException("Invalid number of arguments (min 1).");
		return create(builtin, args);
	}

	public SWRLIndividual wrapIndividual(final OWLIndividual ind)
	{
		return new SWRLIndividualImpl(ind);
	}

	public SWRLDataValue wrapDataValue(final OWLDataValue dv)
	{
		return new SWRLDataValueImpl(dv);
	}

	/* @see org.mindswap.owl.OWLObjectConverterProvider#registerConverters(org.mindswap.owl.OWLObjectConverterRegistry) */
	public void registerConverters(final OWLObjectConverterRegistry registry)
	{
		SWRLConverters.registerConverters(registry);
	}

	private BuiltinAtom create(final OWLIndividual kind, final OWLValue... args)
	{
		final BuiltinAtom atom = new BuiltinAtomImpl(model.createInstance(SWRL.BuiltinAtom, null));
		atom.setBuiltin(kind);
		for (final OWLValue value : args)
		{
			atom.addArgument(value.castTo(SWRLDataObject.class));
		}

		return atom;
	}

}
