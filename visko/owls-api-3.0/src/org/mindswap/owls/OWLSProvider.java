/*
 * Created 22.08.2009
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
package org.mindswap.owls;

import org.mindswap.owl.OWLObjectConverterProvider;
import org.mindswap.owls.process.execution.ProcessExecutionEngine;

/**
 * Factory like encapsulation of methods that need to be implemented in order
 * to provide OWL-S execution engines and translators.
 * <p>
 * The methods defined here directly correspond and are used by the methods
 * provided by {@link OWLSFactory}.
 *
 * @author unascribed
 * @version $Rev: 2298 $; $Author: nick $; $Date: 2011/01/24 06:37:55 $
 */
public interface OWLSProvider extends OWLObjectConverterProvider
{
	public static final String DEFAULT_OWLS_PROVIDER = "impl.owls.OWLSProviderImpl";

	public ProcessExecutionEngine createExecutionEngine();

	public OWLSVersionTranslator createVersionTranslator();

	public OWLSVersionTranslator createVersionTranslator(final String version);
}