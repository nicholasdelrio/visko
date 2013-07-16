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
 * Created on Nov 20, 2004
 */
package org.mindswap.owl;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:52 $
 */
public interface OWLClass extends OWLEntity, OWLType
{

	public Set<OWLClass> getSubClasses(boolean direct);

	public Set<OWLClass> getSuperClasses(boolean direct);

	public Set<OWLClass> getEquivalentClasses();

	public boolean isEnumerated();

	public OWLIndividualList<?> getEnumerations();

	/**
	 * The individuals in the backing {@link #getKB() KB} that have this class
	 * among their types.
	 * <p>
	 * Note that the extent of the result is likely to be larger if a reasoner
	 * is attached to the KB, as the reasoner will add inferred instances.
	 *
	 * @return Those instances that have this class as one of the classes to
	 * 	which they belong.
	 */
	public OWLIndividualList<?> getInstances();

	/**
	 * Returns the instance properties at class definition. Use
	 * {@link #getDeclaredProperties(boolean)} whenever you need the declared
	 * properties of an OWL class.
	 *
	 * @return A map with all instance properties at class definition.
	 */
	public Map<OWLProperty, List<OWLValue>> getProperties();

	/**
	 * Returns all declared properties of a class. Use {@link #getProperties()}
	 * whenever you need the instance properties of an OWL class
	 *
	 * @param direct <code>true</code>, if only properties of the given class
	 * 	itself should be returned. <code>false</code>, if properties of super
	 * 	classes should be returned too.
	 * @return a map with all instance properties at class definition
	 */
	public List<OWLProperty> getDeclaredProperties(boolean direct);
}
