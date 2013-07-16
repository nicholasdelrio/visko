/*
 * Created 09.08.2009
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
public class WSDLGroundingFactory
{
	private static final WSDLGroundingProvider provider = ServiceFinder.instance().loadImplementation(
		WSDLGroundingProvider.class, WSDLGroundingProvider.DEFAULT_WSDL_GROUNDING_PROVIDER);

	public static final WSDLGrounding createWSDLGrounding(final URI uri, final OWLModel model)
	{
		return provider.createWSDLGrounding(uri, model);
	}

	public static final WSDLAtomicGrounding createWSDLAtomicGrounding(final URI uri, final OWLModel model)
	{
		return provider.createWSDLAtomicGrounding(uri, model);
	}

	public static final WSDLOperationRef createWSDLOperationRef(final URI uri, final OWLModel model)
	{
		return provider.createWSDLOperationRef(uri, model);
	}

	public static final MessageMap<String> createWSDLInputMessageMap(final URI uri, final OWLModel model)
	{
		return provider.createWSDLInputMessageMap(uri, model);
	}

	public static final MessageMap<String> createWSDLOutputMessageMap(final URI uri, final OWLModel model)
	{
		return provider.createWSDLOutputMessageMap(uri, model);
	}

}
