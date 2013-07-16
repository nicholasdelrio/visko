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

import java.util.HashMap;
import java.util.Map;

import org.mindswap.exceptions.InvalidOWLSException;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.list.OWLList;
import org.mindswap.owl.vocabulary.SWRL;
import org.mindswap.owl.vocabulary.SWRLB;
import org.mindswap.query.ValueMap;
import org.mindswap.swrl.Atom;
import org.mindswap.swrl.AtomVisitor;
import org.mindswap.swrl.BuiltinAtom;
import org.mindswap.swrl.SWRLDataObject;
import org.mindswap.swrl.SWRLObject;
import org.mindswap.swrl.SWRLFactory.ISWRLFactory;

/**
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:53 $
 */
public class BuiltinAtomImpl extends AtomImpl implements BuiltinAtom
{
	private static final int MANY = -1;
	private static final Map<OWLIndividual, Integer> ARG_COUNT = new HashMap<OWLIndividual, Integer>(9);

	static
	{
		ARG_COUNT.put(SWRLB.add, Integer.valueOf(MANY));
		ARG_COUNT.put(SWRLB.subtract, Integer.valueOf(3));
		ARG_COUNT.put(SWRLB.multiply, Integer.valueOf(MANY));
		ARG_COUNT.put(SWRLB.divide, Integer.valueOf(3));
		ARG_COUNT.put(SWRLB.mod, Integer.valueOf(3));
		ARG_COUNT.put(SWRLB.pow, Integer.valueOf(3));
	}

	public BuiltinAtomImpl(final OWLIndividual ind) {
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
		final OWLIndividual builtin = getBuiltin();
		if (builtin == null) throw new InvalidOWLSException("SWRL BuiltinAtom is missing builtin property.");

		final OWLList<SWRLDataObject> args = getArguments();
		int count = getArgumentCount(builtin);
		count = (count == MANY)? args.size() : count;
		final SWRLDataObject[] argsArr = new SWRLDataObject[count];

		count = 0;
		for (SWRLDataObject arg : args)
		{
			argsArr[count] = getDataObject(arg, binding, swrlFactory);
			count++;
		}

		return swrlFactory.createBuiltinAtom(builtin, argsArr);
	}

	/* @see org.mindswap.swrl.BuiltinAtom#getBuiltin() */
	public OWLIndividual getBuiltin() {
		return getProperty(SWRL.builtin);
	}

	/* @see org.mindswap.swrl.BuiltinAtom#setBuiltin(org.mindswap.owl.OWLIndividual) */
	public void setBuiltin(final OWLIndividual builtin) {
		setProperty(SWRL.builtin, builtin);
	}

	/* @see org.mindswap.swrl.Atom#getArgumentCount() */
	public int getArgumentCount()
	{
		return getArgumentCount(getBuiltin());
	}

	/* @see org.mindswap.swrl.BuiltinAtom#getArguments() */
	public OWLList<SWRLDataObject> getArguments()
	{
		// The fact that we search in ontology (and not its KB) is a deliberate
		// decision. We want to make sure that subsequent modifications of the
		// list will be written to our ontology (and not the base ontology of the
		// KB, which would be the case if we would search the entire KB). This
		// is a slight limitation as we assume all statements that make up the
		// arguments list to be part of our ontology.
		// Note that if (some) statements that make up the arguments list would be
		// part of a imported ontology of our ontology they would be found, but
		// modifications would be written to our ontology.
		final OWLIndividual args = getOntology().getProperty(this, SWRL.arguments);
		return (args == null)? getOntology().createList(SWRLB.BuiltInArgsListVocabulary)
			: args.castToList(SWRLB.BuiltInArgsListVocabulary);
	}

	/* @see org.mindswap.swrl.Atom#getArgument(int) */
	public SWRLDataObject getArgument(final int index)
	{
		final OWLList<SWRLDataObject> args = getArguments();
		return args.get(index);
	}

	/* @see org.mindswap.swrl.BuiltinAtom#addArgument(org.mindswap.swrl.SWRLDataObject) */
	public void addArgument(final SWRLDataObject arg)
	{
		OWLList<SWRLDataObject> args = getArguments();
		if (args.isEmpty())
		{
			args = args.with(arg);
			setProperty(SWRL.arguments, args);
		}
		else args.with(arg);
	}

	/* @see org.mindswap.swrl.Atom#setArgument(int, org.mindswap.swrl.SWRLObject) */
	public void setArgument(final int index, final SWRLObject arg)
	{
		if(!(arg instanceof SWRLDataObject)) throw new IllegalArgumentException(
			BuiltinAtom.class.getSimpleName() + " argument should be a " +	SWRLDataObject.class.getSimpleName());

		OWLList<SWRLDataObject> args = getArguments();
		args.set(index, (SWRLDataObject) arg);
	}

	/* @see impl.owl.WrappedIndividual#toString() */
	@Override
	public String toString()
	{
		final OWLIndividual builtin = getBuiltin();
		if (builtin == null) return "Missing_class_predicate";

		final StringBuilder str = new StringBuilder("(").append(getArgument(0));
		if (builtin.equals(SWRLB.equal))
		{
			str.append(" = ").append(getArgument(1));
		}
		else if (builtin.equals(SWRLB.lessThan))
		{
			str.append(" < ").append(getArgument(1));
		}
		else if (builtin.equals(SWRLB.greaterThan))
		{
			str.append(" > " ).append(getArgument(1));
		}
//		else if (builtin.equals(SWRLB.add))
//		{
//			str.append(" = " ).append(getArgument(1)).append(" + " ).append(getArgument(2));
//		}
		else if (builtin.equals(SWRLB.subtract))
		{
			str.append(" = ").append(getArgument(1)).append(" - ").append(getArgument(2));
		}
//		else if (builtin.equals(SWRLB.multiply))
//		{
//			str.append(" = ").append(getArgument(1)).append(" * ").append(getArgument(2));
//		}
		else if (builtin.equals(SWRLB.divide))
		{
			str.append(" = ").append(getArgument(1)).append(" / ").append(getArgument(2));
		}
		else if (builtin.equals(SWRLB.mod))
		{
			str.append(" = ").append(getArgument(1)).append(" % ").append(getArgument(2));
		}
		else if (builtin.equals(SWRLB.pow))
		{
			str.append(" = ").append(getArgument(1)).append(" ^ ").append(getArgument(2));
		}
		else
		{
			str.insert(0, builtin.getLocalName());
			for (SWRLDataObject arg : getArguments())
			{
				str.append(", ");
				str.append(arg);
			}
		}
		str.append(")");

		return str.toString();
	}

	/* @see org.mindswap.swrl.Atom#evaluate(org.mindswap.query.ValueMap) */
	public void evaluate(final ValueMap<?,?> values)
	{
		throw new UnsupportedOperationException("Builtin SWRL atoms cannot be evaluated.");
	}

	private int getArgumentCount(final OWLIndividual builtin)
	{
		final Integer count =  ARG_COUNT.get(builtin);
		return (count != null)? count.intValue(): 2;
	}

}
