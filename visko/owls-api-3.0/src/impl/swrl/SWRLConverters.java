/*
 * Created 26.08.2009
 *
 * (c) 2009 Thorsten Möller - University of Basel Switzerland
 *
 * The MIT License
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 */
package impl.swrl;

import impl.owl.CombinedOWLConverter;
import impl.owl.GenericOWLConverter;
import impl.owl.ListConverter;

import java.util.ArrayList;
import java.util.List;

import org.mindswap.exceptions.CastingException;
import org.mindswap.owl.OWLDataValue;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLObject;
import org.mindswap.owl.OWLObjectConverter;
import org.mindswap.owl.OWLObjectConverterRegistry;
import org.mindswap.owl.vocabulary.SWRL;
import org.mindswap.owl.vocabulary.SWRLB;
import org.mindswap.owls.vocabulary.OWLS;
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
import org.mindswap.swrl.SameIndividualAtom;

/**
 *
 * @author unascribed
 * @version $Rev: 2298 $; $Author: nick $; $Date: 2011/01/24 06:37:53 $
 */
class SWRLConverters
{
	static final void registerConverters(final OWLObjectConverterRegistry registry)
	{
		final ListConverter<Atom> atomListConverter = new ListConverter<Atom>(SWRL.AtomListVocabulary);
		final ListConverter<SWRLDataObject> builtinAtomArgumentsListConverter =
			new ListConverter<SWRLDataObject>(SWRLB.BuiltInArgsListVocabulary);

		final OWLObjectConverter<ClassAtom> classAtomConverter =
			new GenericOWLConverter<ClassAtom>(ClassAtomImpl.class, SWRL.ClassAtom);
		final OWLObjectConverter<IndividualPropertyAtom> indPropAtomConverter =
			new GenericOWLConverter<IndividualPropertyAtom>(IndividualPropertyAtomImpl.class, SWRL.IndividualPropertyAtom);
		final OWLObjectConverter<DataPropertyAtom> dataPropAtomConverter =
			new GenericOWLConverter<DataPropertyAtom>(DataPropertyAtomImpl.class, SWRL.DatavaluedPropertyAtom);
		final OWLObjectConverter<SameIndividualAtom> sameIndAtomConverter =
			new GenericOWLConverter<SameIndividualAtom>(SameIndividualAtomImpl.class, SWRL.SameIndividualAtom);
		final OWLObjectConverter<DifferentIndividualsAtom> diffIndAtomConverter =
			new GenericOWLConverter<DifferentIndividualsAtom>(DifferentIndividualsAtomImpl.class, SWRL.DifferentIndividualsAtom);
		final OWLObjectConverter<BuiltinAtom> builtinAtomConverter =
			new GenericOWLConverter<BuiltinAtom>(BuiltinAtomImpl.class, SWRL.BuiltinAtom);

		final List<OWLObjectConverter<? extends Atom>> atCs =
			new ArrayList<OWLObjectConverter<? extends Atom>>(6);
		atCs.add(classAtomConverter);
		atCs.add(indPropAtomConverter);
		atCs.add(dataPropAtomConverter);
		atCs.add(sameIndAtomConverter);
		atCs.add(diffIndAtomConverter);
		atCs.add(builtinAtomConverter);
		final OWLObjectConverter<Atom> atomConverter = new CombinedOWLConverter<Atom>(Atom.class, atCs);

		final List<OWLObjectConverter<? extends SWRLIndividualVariable>> sivCs =
			new ArrayList<OWLObjectConverter<? extends SWRLIndividualVariable>>(8);
		sivCs.add(new GenericOWLConverter<SWRLIndividualVariable>(SWRLIndividualVariableImpl.class, SWRL.Variable));
		sivCs.add(new GenericOWLConverter<SWRLIndividualVariable>(SWRLIndividualVariableImpl.class, OWLS.Process.Input));
		sivCs.add(new GenericOWLConverter<SWRLIndividualVariable>(SWRLIndividualVariableImpl.class, OWLS.Process.Output));
		sivCs.add(new GenericOWLConverter<SWRLIndividualVariable>(SWRLIndividualVariableImpl.class, OWLS.Process.Loc));
		sivCs.add(new GenericOWLConverter<SWRLIndividualVariable>(SWRLIndividualVariableImpl.class, OWLS.Process.Link));
		sivCs.add(new GenericOWLConverter<SWRLIndividualVariable>(SWRLIndividualVariableImpl.class, OWLS.Process.Existential));
		sivCs.add(new GenericOWLConverter<SWRLIndividualVariable>(SWRLIndividualVariableImpl.class, OWLS.Process.ResultVar));
		sivCs.add(new GenericOWLConverter<SWRLIndividualVariable>(SWRLIndividualVariableImpl.class, OWLS.Process.Participant));
		final OWLObjectConverter<SWRLIndividualVariable> swrlIndVarConverter =
			new CombinedOWLConverter<SWRLIndividualVariable>(SWRLIndividualVariable.class, sivCs);

		final SWRLIndividualConverter swrlIndConverter = new SWRLIndividualConverter();

		final List<OWLObjectConverter<? extends SWRLIndividualObject>> sioCs =
			new ArrayList<OWLObjectConverter<? extends SWRLIndividualObject>>(2);
		sioCs.add(swrlIndVarConverter);
		sioCs.add(swrlIndConverter);
		final OWLObjectConverter<SWRLIndividualObject> swrlIndObjConverter =
			new CombinedOWLConverter<SWRLIndividualObject>(SWRLIndividualObject.class, sioCs);

		final List<OWLObjectConverter<? extends SWRLDataVariable>> sdvCs =
			new ArrayList<OWLObjectConverter<? extends SWRLDataVariable>>(8);
		sdvCs.add(new GenericOWLConverter<SWRLDataVariable>(SWRLDataVariableImpl.class, SWRL.Variable));
		sdvCs.add(new GenericOWLConverter<SWRLDataVariable>(SWRLDataVariableImpl.class, OWLS.Process.Input));
		sdvCs.add(new GenericOWLConverter<SWRLDataVariable>(SWRLDataVariableImpl.class, OWLS.Process.Output));
		sdvCs.add(new GenericOWLConverter<SWRLDataVariable>(SWRLDataVariableImpl.class, OWLS.Process.Loc));
		sdvCs.add(new GenericOWLConverter<SWRLDataVariable>(SWRLDataVariableImpl.class, OWLS.Process.Link));
		sdvCs.add(new GenericOWLConverter<SWRLDataVariable>(SWRLDataVariableImpl.class, OWLS.Process.Existential));
		sdvCs.add(new GenericOWLConverter<SWRLDataVariable>(SWRLDataVariableImpl.class, OWLS.Process.ResultVar));
		sdvCs.add(new GenericOWLConverter<SWRLDataVariable>(SWRLDataVariableImpl.class, OWLS.Process.Participant));
		final OWLObjectConverter<SWRLDataVariable> swrlDataVarConverter =
			new CombinedOWLConverter<SWRLDataVariable>(SWRLDataVariable.class, sdvCs);

		final SWRLDataValueConverter swrlDataValueConverter = new SWRLDataValueConverter();

		final List<OWLObjectConverter<? extends SWRLDataObject>> sdoCs =
			new ArrayList<OWLObjectConverter<? extends SWRLDataObject>>(2);
		sdoCs.add(swrlDataVarConverter);
		sdoCs.add(swrlDataValueConverter);
		final OWLObjectConverter<SWRLDataObject> swrlDataObjConverter =
			new CombinedOWLConverter<SWRLDataObject>(SWRLDataObject.class, sdoCs);

		registry.registerListConverter(SWRL.AtomListVocabulary, atomListConverter);
		registry.registerListConverter(SWRLB.BuiltInArgsListVocabulary, builtinAtomArgumentsListConverter);

		registry.registerConverter(Atom.class, atomConverter);
		registry.registerConverter(ClassAtom.class, classAtomConverter);
		registry.registerConverter(IndividualPropertyAtom.class, indPropAtomConverter);
		registry.registerConverter(SameIndividualAtom.class, sameIndAtomConverter);
		registry.registerConverter(DifferentIndividualsAtom.class, diffIndAtomConverter);
		registry.registerConverter(DataPropertyAtom.class, dataPropAtomConverter);
		registry.registerConverter(BuiltinAtom.class, builtinAtomConverter);
		registry.registerConverter(SWRLIndividual.class, swrlIndConverter);
		registry.registerConverter(SWRLIndividualVariable.class, swrlIndVarConverter);
		registry.registerConverter(SWRLIndividualObject.class, swrlIndObjConverter);
		registry.registerConverter(SWRLDataVariable.class, swrlDataVarConverter);
		registry.registerConverter(SWRLDataValue.class, swrlDataValueConverter);
		registry.registerConverter(SWRLDataObject.class, swrlDataObjConverter);
	}

	static final class SWRLIndividualConverter implements OWLObjectConverter<SWRLIndividual>
	{
		/* @see org.mindswap.owl.OWLObjectConverter#canCast(org.mindswap.owl.OWLObject, boolean) */
		public boolean canCast(final OWLObject object, final boolean strictConversion)
		{
			return (object instanceof OWLIndividual);
		}

		/* @see org.mindswap.owl.OWLObjectConverter#cast(org.mindswap.owl.OWLObject, boolean) */
		public SWRLIndividual cast(final OWLObject object, final boolean strictConversion)
		{
			if (canCast(object, strictConversion)) return new SWRLIndividualImpl((OWLIndividual) object);

			throw CastingException.create(object, SWRLIndividualObject.class);
		}

		/* @see java.lang.Object#toString() */
		@Override
		public String toString()
		{
			return "Converter " + OWLIndividual.class.getSimpleName() + " -> " + SWRLIndividualObject.class.getSimpleName();
		}
	}

	static final class SWRLDataValueConverter implements OWLObjectConverter<SWRLDataValue>
	{
		/* @see org.mindswap.owl.OWLObjectConverter#canCast(org.mindswap.owl.OWLObject, boolean) */
		public boolean canCast(final OWLObject object, final boolean strictConversion)
		{
			return (object instanceof OWLDataValue);
		}

		/* @see org.mindswap.owl.OWLObjectConverter#cast(org.mindswap.owl.OWLObject, boolean) */
		public SWRLDataValue cast(final OWLObject object, final boolean strictConversion)
		{
			if (canCast(object, strictConversion)) return new SWRLDataValueImpl((OWLDataValue) object);

			throw CastingException.create(object, OWLDataValue.class);
		}

		/* @see java.lang.Object#toString() */
		@Override
		public String toString()
		{
			return "Converter " + OWLDataValue.class.getSimpleName() + " -> " + OWLDataValue.class.getSimpleName();
		}
	}

}
