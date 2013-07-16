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
package org.mindswap.owls.grounding;

import java.net.URI;

import org.mindswap.owl.OWLModel;
import org.mindswap.owl.OWLObjectConverterProvider;

/**
 * Factory like encapsulation of methods that need to be implemented in order
 * to provide a UPnP grounding implementation.
 * <p>
 * The methods defined here directly correspond and are used by the methods
 * provided by {@link UPnPGroundingFactory}.
 *
 * @author unascribed
 * @version $Rev: 2298 $; $Author: nick $; $Date: 2011/01/24 06:37:54 $
 */
public interface UPnPGroundingProvider extends OWLObjectConverterProvider
{
	public static final String DEFAULT_UPNP_GROUNDING_PROVIDER = "impl.owls.grounding.UPnPGroundingProviderImpl";

	public UPnPGrounding createUPnPGrounding(URI uri, OWLModel model);

	public UPnPAtomicGrounding createUPnPAtomicGrounding(URI uri, OWLModel model);

	public MessageMap<String> createUPnPMessageMap(final URI uri, final OWLModel model);
}