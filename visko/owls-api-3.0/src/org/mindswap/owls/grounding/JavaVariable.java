//The MIT License
//
// Copyright (c) 2007 University of Zürich Switzerland
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
package org.mindswap.owls.grounding;

import org.mindswap.exceptions.ExecutionException;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLTransformator;
import org.mindswap.owls.process.variable.Parameter;

/**
 *
 * @author unascribed
 * @version $Rev: 2316 $; $Author: nick $; $Date: 2011/01/24 06:37:54 $
 */
public interface JavaVariable extends OWLIndividual
{
	public void setJavaType(String type);
	public String getJavaType();
	public void removeJavaType();

	public void setOWLSParameter(Parameter parameter);
	public Parameter getOWLSParameter();
	public void removeOWLSParameter();

	public void setTransformator(String transformator);
	public String getTransformator();
	public void removeTransformator();

	/**
	 * Convenience method. It assumes that a {@link #getTransformator() OWL
	 * transformator} is specified and that the string is a fully qualified
	 * Java class name. The class will be loaded, instantiated using the default
	 * no-args constructor, and returned.
	 *
	 * @param failIfUnspecified Determines whether to throw {@link ExecutionException}
	 * 	if no transformator is specified for this Java variable, i.e., when
	 * 	the parameter is <code>false</code> and a transformator is not specified
	 * 	it is silently ignored.
	 * @return OWL transformator instance, or <code>null</code> if unspecified
	 * 	and <code>failIfUnspecified</code> is <code>false</code>.
	 * @throws ExecutionException If <code>failIfUnspecified</code> is <code>true</code>
	 * 	and no transformator is specified.
	 */
	public OWLTransformator getTransformator(final boolean failIfUnspecified) throws ExecutionException;

}
