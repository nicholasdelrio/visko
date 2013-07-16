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

import org.mindswap.owl.OWLClass;
import org.mindswap.query.ValueMap;

/**
 * Evaluation ({@link #evaluate(ValueMap)}) of a datatype property atom will
 * add the following statement to the backing {@link #getOntology() ontology}:
 * ({@link #getArgument1() subject} <tt>rdf:type</tt>
 * {@link #getClassPredicate() object}).
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:51 $
 */
public interface ClassAtom extends Atom {
	/**
	 * Returns the class predicate for this SWRL class atom
	 *
	 * @return the OWL class for this this SWRL class atom
	 */
    public OWLClass getClassPredicate();

    /**
     * Sets  the class predicate for this SWRL class atom
     * @param c the OWL class for this this SWRL class atom
     */
    public void setClassPredicate(OWLClass c);

    /**
     * Gets the argument for this SWRL class atom, i.e. the instance to check
     * or set for the class predicate
     * @return the variable to check/set for the class predicate
     */
    public SWRLIndividualObject getArgument1();

    /**
     * Sets the argument for this SWRL class atom, i.e. the instance to check
     * or set for the class predicate
     * @param obj the variable to check/set for the class predicate
     */
    public void setArgument1(SWRLIndividualObject obj);

}
