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
package org.mindswap.owls.grounding;

import java.net.URI;

import org.mindswap.owl.OWLIndividual;
import org.mindswap.owls.process.variable.Parameter;

/**
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:54 $
 */
public interface MessageMap<T> extends OWLIndividual
{
	/**
	 * @return
	 */
	public Parameter getOWLSParameter();

	/**
	 * @return
	 */
	public String getGroundingParameter();

	/**
	 * @return
	 */
	public URI getGroundingParameterAsURI();

	/**
	 * @return
	 */
	public T getTransformation();

	/**
	 * @param param
	 */
	public void setOWLSParameter(Parameter param);

	/**
	 * @param param
	 */
	public void setGroundingParameter(String param);

	/**
	 * @param transform
	 */
	public void setTransformation(T transform);

	/**
	 *
	 */
	public interface StringMessageMap extends MessageMap<String> {}
}
