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

package org.mindswap.owls;

import org.mindswap.common.ServiceFinder;
import org.mindswap.owls.process.execution.ProcessExecutionEngine;

/**
 * This factory is able to create various objects relevant when dealing with OWL-S.
 *
 * @author unascribed
 * @version $Rev: 2298 $; $Author: nick $; $Date: 2011/01/24 06:37:55 $
 */
public class OWLSFactory
{
	private static final OWLSProvider factory = ServiceFinder.instance().loadImplementation(
		OWLSProvider.class, OWLSProvider.DEFAULT_OWLS_PROVIDER);

	/**
	 * @return A new standard execution engine.
	 */
	public static final ProcessExecutionEngine createExecutionEngine()
	{
		return factory.createExecutionEngine();
	}

	/**
	 * @return A new generic OWL-S version translator, i.e., one that can be
	 * 	used to translate from multiple OWL-S versions to the default version
	 * 	used.
	 */
	public static final OWLSVersionTranslator createVersionTranslator()
	{
		return factory.createVersionTranslator();
	}

	/**
	 * @param version A particular OWL-S version from which the returned
	 * 	translator supports conversion to the default version used.
	 * @return A new version translator for the given version, or <code>null</code>
	 * 	if none exists.
	 */
	public static final OWLSVersionTranslator createVersionTranslator(final String version)
	{
		return factory.createVersionTranslator(version);
	}
}
