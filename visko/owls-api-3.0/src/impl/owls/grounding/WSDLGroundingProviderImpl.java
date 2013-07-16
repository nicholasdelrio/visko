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
import org.mindswap.owls.grounding.WSDLAtomicGrounding;
import org.mindswap.owls.grounding.WSDLGrounding;
import org.mindswap.owls.grounding.WSDLGroundingProvider;
import org.mindswap.owls.grounding.WSDLOperationRef;
import org.mindswap.owls.grounding.MessageMap.StringMessageMap;
import org.mindswap.owls.vocabulary.OWLS;

/**
 *
 * @author unascribed
 * @version $Rev: 2298 $; $Author: nick $; $Date: 2011/01/24 06:37:52 $
 */
public class WSDLGroundingProviderImpl implements WSDLGroundingProvider
{
	// Implementation note: This class should *not* maintain any state between
	// the registerConverters(..) method derived from OWLObjectConverterProvider
	// and methods directly derived from WSDLGroundingProvider! The reason is
	// that two instances of this class will be created; one used by the
	// OWLObjectConverterRegistry to register all converters and one hold by the
	// WSDLGroundingFactory.

	/* @see org.mindswap.owls.grounding.WSDLGroundingProvider#createWSDLAtomicGrounding(java.net.URI, org.mindswap.owl.OWLModel) */
	public WSDLAtomicGrounding createWSDLAtomicGrounding(final URI uri, final OWLModel model)
	{
		return new WSDLAtomicGroundingImpl(model.createInstance(OWLS.Grounding.WsdlAtomicProcessGrounding, uri));
	}

	/* @see org.mindswap.owls.grounding.WSDLGroundingProvider#createWSDLGrounding(java.net.URI, org.mindswap.owl.OWLModel) */
	public WSDLGrounding createWSDLGrounding(final URI uri, final OWLModel model)
	{
		return new WSDLGroundingImpl(model.createInstance(OWLS.Grounding.WsdlGrounding, uri));
	}

	/* @see org.mindswap.owls.grounding.WSDLGroundingProvider#createWSDLInputMessageMap(java.net.URI, org.mindswap.owl.OWLModel) */
	public MessageMap<String> createWSDLInputMessageMap(final URI uri, final OWLModel model)
	{
		return new WSDLMessageMapImpl(model.createInstance(OWLS.Grounding.WsdlInputMessageMap, uri));
	}

	/* @see org.mindswap.owls.grounding.WSDLGroundingProvider#createWSDLOutputMessageMap(java.net.URI, org.mindswap.owl.OWLModel) */
	public MessageMap<String> createWSDLOutputMessageMap(final URI uri, final OWLModel model)
	{
		return new WSDLMessageMapImpl(model.createInstance(OWLS.Grounding.WsdlOutputMessageMap, uri));
	}

	/* @see org.mindswap.owls.grounding.WSDLGroundingProvider#createWSDLOperationRef(java.net.URI, org.mindswap.owl.OWLModel) */
	public WSDLOperationRef createWSDLOperationRef(final URI uri, final OWLModel model)
	{
		return new WSDLOperationRefImpl(model.createInstance(OWLS.Grounding.WsdlOperationRef, uri));
	}

	/* @see org.mindswap.owl.OWLObjectConverterProvider#registerConverters(org.mindswap.owl.OWLObjectConverterRegistry) */
	public void registerConverters(final OWLObjectConverterRegistry registry)
	{
		final OWLObjectConverter<WSDLGrounding> gc = new GenericOWLConverter<WSDLGrounding>(
			WSDLGroundingImpl.class, OWLS.Grounding.WsdlGrounding);

		final OWLObjectConverter<WSDLAtomicGrounding> agc = new GenericOWLConverter<WSDLAtomicGrounding>(
			WSDLAtomicGroundingImpl.class, OWLS.Grounding.WsdlAtomicProcessGrounding);

		final OWLObjectConverter<WSDLMessageMapImpl> immc =
			new GenericOWLConverter<WSDLMessageMapImpl>(WSDLMessageMapImpl.class, OWLS.Grounding.WsdlInputMessageMap);
		final OWLObjectConverter<WSDLMessageMapImpl> ommc =
			new GenericOWLConverter<WSDLMessageMapImpl>(WSDLMessageMapImpl.class, OWLS.Grounding.WsdlOutputMessageMap);

		registry.registerConverter(WSDLOperationRef.class,
			new GenericOWLConverter<WSDLOperationRef>(WSDLOperationRefImpl.class, OWLS.Grounding.WsdlOperationRef));

		registry.registerConverter(WSDLGrounding.class, gc);
		registry.registerConverter(WSDLAtomicGrounding.class, agc);

		registry.extendByConverter(Grounding.class, gc);
		registry.extendByConverter(AtomicGrounding.class, agc);

		registry.extendByConverter(MessageMap.class, immc);
		registry.extendByConverter(MessageMap.class, ommc);
		registry.extendByConverter(StringMessageMap.class, immc);
		registry.extendByConverter(StringMessageMap.class, ommc);
	}

}
