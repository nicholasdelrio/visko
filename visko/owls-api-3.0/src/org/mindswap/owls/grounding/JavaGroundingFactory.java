/*
 * Created 24.08.2009
 *
 * (c) 2009 Thorsten Möller - University of Basel Switzerland
 *
 * The MIT License
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 */
package org.mindswap.owls.grounding;

import java.net.URI;

import org.mindswap.common.ServiceFinder;
import org.mindswap.owl.OWLModel;

/**
 *
 * @author unascribed
 * @version $Rev: 2298 $; $Author: nick $; $Date: 2011/01/24 06:37:54 $
 */
public class JavaGroundingFactory
{
	private static final JavaGroundingProvider provider = ServiceFinder.instance().loadImplementation(
		JavaGroundingProvider.class, JavaGroundingProvider.DEFAULT_JAVA_GROUNDING_PROVIDER);

	public static final JavaGrounding createJavaGrounding(final URI uri, final OWLModel model)
	{
		return provider.createJavaGrounding(uri, model);
	}

	public static final JavaAtomicGrounding createJavaAtomicGrounding(final URI uri, final OWLModel model)
	{
		return provider.createJavaAtomicGrounding(uri, model);
	}

	public static final JavaParameter createJavaParameter(final URI uri, final OWLModel model)
	{
		return provider.createJavaParameter(uri, model);
	}

	public static final JavaVariable createJavaVariable(final URI uri, final OWLModel model)
	{
		return provider.createJavaVariable(uri, model);
	}

}
