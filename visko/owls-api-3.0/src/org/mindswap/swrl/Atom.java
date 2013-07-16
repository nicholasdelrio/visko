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
 * Created on Oct 26, 2004
 */
package org.mindswap.swrl;

import org.mindswap.common.Visitable;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.query.ValueMap;
import org.mindswap.swrl.SWRLFactory.ISWRLFactory;

/**
 * Encapsulation for SWRL Atom.
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:51 $
 * @see <a href="http://www.w3.org/Submission/SWRL/">SWRL on W3C</a>
 */
public interface Atom extends OWLIndividual, Visitable<AtomVisitor, Atom> //, Iterable<SWRLObject>
{
	/**
	 * Apply the given values possibly binding some variable argument in this
	 * Atom to a concrete value. That is, search the given value map if it
	 * contains a value to which a variable argument can be bound.
	 * <p>
	 * Note that a new atom instance will be created in the backing OWLModel of
	 * the SWRL factory, which may be not the same as the one to which this atom
	 * is attached to.
	 *
	 * @param binding The map of values, each bound to a variable.
	 * @param swrlFactory Factory which will be used to instantiate the returned
	 * 	Atom, thus, determining in which model it will be created.
	 * @return The same type of atom where each unbound variable is replaced by
	 * 	a concrete value as retrieved from the given value map.
	 * 	If some binding(s) is/are missing in the given map the returned atom
	 * 	may be the same ungrounded atom, or just partially grounded.
	 * @throws NullPointerException If <code>swrlFactory</code> is <code>null</code>.
	 */
	public Atom apply(ValueMap<?, ?> binding, ISWRLFactory swrlFactory);

	/**
	 * @return The number of arguments this atom has.
	 */
	public int getArgumentCount();

	/**
	 * Returns the argument at position <code>index</code>.
	 *
	 * @param index the integer index of the argument in the argument list
	 * @return the argument at the given index. <code>null</code> if there is no
	 * 	argument at the given index.
	 */
	public SWRLObject getArgument(int index);

	/**
	 * Sets the argument at the given position
	 *
	 * @param index the position to which the argument is added
	 * @param arg the argument to add at the given position
	 */
	public void setArgument(int index, SWRLObject arg);

	/**
	 * Evaluates the atom. For details about the semantics of this operation see
	 * documentation of each specific sub interface.
	 *
	 * @param values A map of variable bindings possibly binding some or all
	 * 	of the variables in this Atom. Parameter can be <code>null</code>
	 * 	to force to evaluate this Atom without attempts to bind variables.
	 * @throws NullPointerException If parameter is <code>null</code>.
	 */
	public void evaluate(ValueMap<?, ?> values);

}
