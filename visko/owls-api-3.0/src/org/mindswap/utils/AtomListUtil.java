// The MIT License
//
// Copyright (c) 2004 Evren Sirin
// Copyright (c) 2009 Thorsten Möller - University of Basel Switzerland
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
 * Created on Oct 26, 2004
 */
package org.mindswap.utils;

import java.util.ArrayList;
import java.util.List;

import org.mindswap.common.Variable;
import org.mindswap.owl.list.OWLList;
import org.mindswap.query.ValueMap;
import org.mindswap.swrl.Atom;
import org.mindswap.swrl.SWRLObject;
import org.mindswap.swrl.SWRLFactory.ISWRLFactory;

/**
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:53 $
 */
public class AtomListUtil
{
	/**
	 * Apply the given values possibly binding some of the unbound variables in
	 * Atoms of the given list to concrete values.
	 * <p>
	 * Note that new atom instances will be created in the backing OWLModel of
	 * the SWRL factory, which may be not the same as the one to which the given
	 * atoms in the list are attached to.
	 *
	 * @param atoms The list of (partially) unbound atoms.
	 * @param binding The map of values, each bound to a variable.
	 * @param swrlFactory Factory which will be used to instantiate bound Atoms
	 * 	to be added to the result list.
	 * @return A list containing the same types of atoms as in the given list,
	 * 	whereby unbound variables of each atom are replaced by a concrete value
	 * 	as retrieved from the given value map. Atoms with unbound variables
	 * 	which could not be bound (because a value does not exist in the given
	 * 	binding) remain unbound in the result.
	 * @throws NullPointerException If one of the parameters is <code>null</code>.
	 */
	public static final OWLList<Atom> apply(final OWLList<Atom> atoms,
		final ValueMap<?, ?> binding, final ISWRLFactory swrlFactory)
	{
		OWLList<Atom> newList = swrlFactory.createList();
		for (Atom atom : atoms)
		{
			atom = atom.apply(binding, swrlFactory);
			newList = newList.with(atom);
		}

		return newList;
	}

	/**
	 * Return all the variables mentioned in the given list of atoms and
	 * which can be viewed as the given variable type.
	 *
	 * @return List of Variable objects
	 */
	public static final <V extends Variable> List<V> getVars(final OWLList<Atom> atoms, final Class<V> varType)
	{
		final List<V> vars = new ArrayList<V>();
		for (Atom atom : atoms)
		{
			for (int j = 0; j < atom.getArgumentCount(); j++)
			{
				final SWRLObject term = atom.getArgument(j);
				if (varType.isInstance(term)) vars.add(varType.cast(term));
			}
		}
		return vars;
	}

}
