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
 * Created on Jan 12, 2004
 */
package org.mindswap.owls.profile;

import org.mindswap.owl.OWLIndividual;

/**
 * Represents the OWL-S service category.
 * <p>
 * Corresponding OWL-S concept: {@link org.mindswap.owls.vocabulary.OWLS.ServiceCategory#ServiceCategory}.
 * <p>
 * Note that this class currently does not have any setter methods. This is due
 * to the fact that the data properties having ServiceCategory as their domain
 * do not specify a <tt>rdfs:range</tt>. As such, it is possible to set any
 * valid value of some OWL datatype. However, using and setting the value for
 * some of those properties can be achieved directly, as shown below:
 * <pre>
 * ServiceCategory myCategory = ...
 * myCategory.setProperty(OWLS.Profile.categoryName, "myCategoryName");
 * </pre>
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:54 $
 */
public interface ServiceCategory extends OWLIndividual
{
	public String getName();

	public String getTaxonomy();

	public String getCode();

	public String getValue();
}
