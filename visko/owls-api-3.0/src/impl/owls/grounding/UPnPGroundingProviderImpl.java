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
package impl.owls.grounding;

import impl.owl.GenericOWLConverter;

import java.net.URI;

import org.mindswap.owl.OWLModel;
import org.mindswap.owl.OWLObjectConverter;
import org.mindswap.owl.OWLObjectConverterRegistry;
import org.mindswap.owls.grounding.AtomicGrounding;
import org.mindswap.owls.grounding.Grounding;
import org.mindswap.owls.grounding.MessageMap;
import org.mindswap.owls.grounding.UPnPAtomicGrounding;
import org.mindswap.owls.grounding.UPnPGrounding;
import org.mindswap.owls.grounding.UPnPGroundingProvider;
import org.mindswap.owls.grounding.MessageMap.StringMessageMap;
import org.mindswap.owls.vocabulary.FLAServiceOnt;

/**
 *
 * @author unascribed
 * @version $Rev: 2298 $; $Author: nick $; $Date: 2011/01/24 06:37:52 $
 */
public class UPnPGroundingProviderImpl implements UPnPGroundingProvider
{
	// Implementation note: This class should *not* maintain any state between
	// the registerConverters(..) method derived from OWLObjectConverterProvider
	// and methods directly derived from UPnPGroundingProvider! The reason is
	// that two instances of this class will be created; one used by the
	// OWLObjectConverterRegistry to register all converters and one hold by the
	// UPnPGroundingFactory.

	/* @see org.mindswap.owls.grounding.UPnPGroundingProvider#createUPnPAtomicGrounding(java.net.URI, org.mindswap.owl.OWLModel) */
	public UPnPAtomicGrounding createUPnPAtomicGrounding(final URI uri, final OWLModel model)
	{
		return new UPnPAtomicGroundingImpl(model.createInstance(FLAServiceOnt.UPnPAtomicProcessGrounding, uri));
	}

	/* @see org.mindswap.owls.grounding.UPnPGroundingProvider#createUPnPGrounding(java.net.URI, org.mindswap.owl.OWLModel) */
	public UPnPGrounding createUPnPGrounding(final URI uri, final OWLModel model)
	{
		return new UPnPGroundingImpl(model.createInstance(FLAServiceOnt.UPnPGrounding, uri));
	}

	public MessageMap<String> createUPnPMessageMap(final URI uri, final OWLModel model)
	{
		return new UPnPMessageMapImpl(model.createInstance(FLAServiceOnt.UPnPMap, uri));
	}

	/* @see org.mindswap.owl.OWLObjectConverterProvider#registerConverters(org.mindswap.owl.OWLObjectConverterRegistry) */
	public void registerConverters(final OWLObjectConverterRegistry registry)
	{
		final OWLObjectConverter<UPnPGrounding> gc =	new GenericOWLConverter<UPnPGrounding>(
			UPnPGroundingImpl.class, FLAServiceOnt.UPnPGrounding);

		final OWLObjectConverter<UPnPAtomicGrounding> agc = new GenericOWLConverter<UPnPAtomicGrounding>(
			UPnPAtomicGroundingImpl.class, FLAServiceOnt.UPnPAtomicProcessGrounding);

		final OWLObjectConverter<UPnPMessageMapImpl> mmc =
			new GenericOWLConverter<UPnPMessageMapImpl>(UPnPMessageMapImpl.class, FLAServiceOnt.UPnPMap);

		registry.registerConverter(UPnPGrounding.class, gc);
		registry.registerConverter(UPnPAtomicGrounding.class, agc);

		registry.extendByConverter(Grounding.class, gc);
		registry.extendByConverter(AtomicGrounding.class, agc);

		registry.extendByConverter(MessageMap.class, mmc);
		registry.extendByConverter(StringMessageMap.class, mmc);
	}

}
