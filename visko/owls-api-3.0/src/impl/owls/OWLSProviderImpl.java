/*
 * Created on Mar 31, 2005
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
package impl.owls;

import impl.owls.process.execution.ProcessExecutionEngineImpl;

import org.mindswap.owl.OWLObjectConverterRegistry;
import org.mindswap.owls.OWLSProvider;
import org.mindswap.owls.OWLSVersionTranslator;
import org.mindswap.owls.process.execution.ProcessExecutionEngine;
import org.mindswap.owls.vocabulary.OWLS_1_0;
import org.mindswap.owls.vocabulary.OWLS_1_1;

/**
 * @author unascribed
 * @version $Rev: 2298 $; $Author: nick $; $Date: 2011/01/24 06:37:54 $
 */
public class OWLSProviderImpl implements OWLSProvider
{
	// Implementation note: This class should *not* maintain any state between
	// the registerConverters(..) method derived from OWLObjectConverterProvider
	// and methods directly derived from OWLSProvider! The reason is that two
	// instances of this class will be created; one used by the OWLObjectConverterRegistry
	// to register all converters and one hold by the OWLSFactory.

	/* @see org.mindswap.owls.OWLSProvider#createExecutionEngine() */
	public ProcessExecutionEngine createExecutionEngine()
	{
		return new ProcessExecutionEngineImpl();
	}

	/* @see org.mindswap.owls.OWLSProvider#createVersionTranslator() */
	public OWLSVersionTranslator createVersionTranslator()
	{
		return new GenericVersionTranslator();
	}

	/* @see org.mindswap.owls.OWLSProvider#createVersionTranslator(java.lang.String) */
	public OWLSVersionTranslator createVersionTranslator(final String version)
	{
		if (OWLS_1_0.version.equals(version)) return new OWLSTranslator_1_0();
		if (OWLS_1_1.version.equals(version)) return new OWLSTranslator_1_1();
		throw new IllegalArgumentException("There is no translator for OWL-S version " + version);
	}

	/* @see org.mindswap.owl.OWLObjectConverterProvider#registerConverters(org.mindswap.owl.OWLObjectConverterRegistry) */
	public void registerConverters(final OWLObjectConverterRegistry registry)
	{
		OWLSConverters.registerConverters(registry);
	}

}
