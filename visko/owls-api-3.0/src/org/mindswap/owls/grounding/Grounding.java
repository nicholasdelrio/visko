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
 * Created on Dec 27, 2003
 *
 */
package org.mindswap.owls.grounding;

import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owls.process.AtomicProcess;
import org.mindswap.owls.service.Service;
import org.mindswap.owls.vocabulary.OWLS;

/**
 * Represents the grounding that some {@link Service} supports.
 * <p>
 * As of OWL-S 1.2 the corresponding concept in the OWL-S ontology is
 * <a href="http://www.daml.org/services/owl-s/1.2/Grounding#Grounding">
 * Grounding</a>
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:54 $
 */
public interface Grounding<A extends AtomicGrounding<T>, T> extends OWLIndividual
{
	/**
	 * @param service The service to be supported by this grounding.
	 */
	public void setService(Service service);

	/**
	 * @return The service supported by this grounding.
	 */
	public Service getService();

	/**
	 * Removes the service supported by this grounding by breaking the link
	 * {@link OWLS.Service#supportedBy}. The service itself remains untouched.
	 */
	public void removeService();

	/**
	 * @param apg Add the given atomic process grounding.
	 */
	public void addGrounding(A apg);

	/**
	 * Get the atomic process grounding defined for the given atomic process.
	 * If there are multiple groundings specified one of them is selected
	 * arbitrarily. Consider to use {@link #getAtomicGroundings(AtomicProcess)}
	 * instead.
	 *
	 * @param process The atomic process for which to get its atomic process
	 * 	grounding defined in this grounding.
	 * @return The atomic process grounding, or <code>null</code> if none is
	 * 	specified for the given process.
	 */
	public A getAtomicGrounding(AtomicProcess process);

	/**
	 * @return All the atomic process groundings defined in this grounding.
	 */
	public OWLIndividualList<A> getAtomicGroundings();

	/**
	 * @param process The atomic process for which to get its atomic process
	 * 	groundings defined in this grounding.
	 * @return The list of atomic process groundings.
	 */
	public OWLIndividualList<A> getAtomicGroundings(AtomicProcess process);

	/**
	 * Removes the atomic groundings for the given atomic process from this
	 * grounding by removing the corresponding statements, e.g.,
	 * {@link OWLS.Grounding#hasAtomicProcessGrounding} for WSDL groundings.
	 * The atomic groundings itself are not changed at all.
	 *
	 * @param process The atomic process to which the removable atomic
	 * 	groundings are bound.
	 */
	public void removeAtomicGroundings(AtomicProcess process);

	/**
	 * Removes the atomic grounding from this grounding by removing the
	 * corresponding statement, e.g., {@link OWLS.Grounding#hasAtomicProcessGrounding}
	 * for WSDL groundings. The atomic grounding itself is not changed at all.
	 *
	 * @param apg The atomic process grounding to be removed.
	 */
	public void removeAtomicGrounding(A apg);

	/**
	 * Removes all atomic groundings from this grounding by removing the
	 * the corresponding statements, e.g.,
	 * {@link OWLS.Grounding#hasAtomicProcessGrounding} for WSDL groundings.
	 * The atomic groundings itself are not changed at all.
	 */
	public void removeAtomicGroundings();

}
