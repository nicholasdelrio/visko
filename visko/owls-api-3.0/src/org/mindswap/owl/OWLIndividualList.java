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
 */
package org.mindswap.owl;

import java.net.URI;
import java.util.List;

/**
 * Represents a list of <code>OWLIndividual</code> objects. This interface
 * shouldn't be confused with <tt>rdf:List</tt> structure (in this library
 * represented by {@link org.mindswap.owl.list.OWLList}). The sole purpose
 * of this interface is to add some access respectively update methods that
 * do not exist in {@link java.util.List List}. In addition, it adds insertion
 * methods that realize semantics of sets, i.e., prevention of duplicates.
 *
 * @author unascribed
 * @version $Rev: 2177 $; $Author: nick $; $Date: 2011/01/24 06:37:52 $
 */
public interface OWLIndividualList<E extends OWLIndividual> extends List<E>
{
	/**
	 * @param individual The individual to be added.
	 * @return <code>true</code> if this list changed as a result of the call.
	 */
	public boolean addWithoutDuplicate(E individual);

	/**
	 * @param individuals The individual to be added.
	 * @return <code>true</code> if this list changed as a result of the call.
	 */
	public boolean addWithoutDuplicate(List<E> individuals);

	/**
	 * Get the individual from the list that has the given URI.
	 *
	 * @param resourceURI The URI of the target individual.
	 * @return The individual having the given URI, or <code>null</code> if no
	 * 	such individual exists.
	 */
	public E getIndividual(URI resourceURI);

	/**
	 * Get the individual from the list that has the given local name. If there
	 * are more than one individual with the same local name than any one of
	 * them is returned.
	 *
	 * @param localName The local name of the target individual.
	 * @return The individual having the given local name, or <code>null</code>
	 * 	if no such individual exists.
	 */
	public E getIndividual(String localName);
}
