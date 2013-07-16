//The MIT License
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
package impl.owls.grounding;

import java.net.URL;

import org.mindswap.exceptions.ExecutionException;
import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owl.OWLObjectProperty;
import org.mindswap.owl.OWLValue;
import org.mindswap.owls.grounding.AtomicGrounding;
import org.mindswap.owls.grounding.MessageMap;
import org.mindswap.owls.grounding.UPnPAtomicGrounding;
import org.mindswap.owls.grounding.UPnPGrounding;
import org.mindswap.owls.grounding.MessageMap.StringMessageMap;
import org.mindswap.owls.process.variable.Input;
import org.mindswap.owls.process.variable.Output;
import org.mindswap.owls.vocabulary.FLAServiceOnt;
import org.mindswap.query.ValueMap;
import org.mindswap.utils.Utils;
import org.mindswap.utils.XSLTEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

import com.fujitsu.fla.upnp.Action;
import com.fujitsu.fla.upnp.Argument;
import com.fujitsu.fla.upnp.ArgumentList;
import com.fujitsu.fla.upnp.ControlPoint;
import com.fujitsu.fla.upnp.Device;
import com.fujitsu.fla.upnp.Service;
import com.fujitsu.fla.upnp.UPnPStatus;

/**
 *
 * @author unascribed
 * @version $Rev: 2335 $; $Author: nick $; $Date: 2011/01/24 06:37:52 $
 */
public class UPnPAtomicGroundingImpl extends MessageMapAtomicGroundingImpl<String> implements UPnPAtomicGrounding
{
	private static final Logger logger = LoggerFactory.getLogger(UPnPAtomicGroundingImpl.class);

	public UPnPAtomicGroundingImpl(final OWLIndividual ind)
	{
		super(ind);
	}

	/* @see org.mindswap.owls.grounding.AtomicGrounding#getGrounding() */
	public UPnPGrounding getGrounding()
	{
		return getGrounding(UPnPGrounding.class);
	}

	/* @see org.mindswap.owls.grounding.UPnPAtomicGrounding#setUPnPService(java.lang.String) */
	public void setUPnPService(final String service)
	{
		setProperty(FLAServiceOnt.upnpServiceID, service);
	}

	/* @see org.mindswap.owls.grounding.UPnPAtomicGrounding#getUPnPService() */
	public String getUPnPService()
	{
		return getPropertyAsString(FLAServiceOnt.upnpServiceID);
	}

	/* @see org.mindswap.owls.grounding.UPnPAtomicGrounding#setUPnPAction(java.lang.String) */
	public void setUPnPAction(final String action)
	{
		setProperty(FLAServiceOnt.upnpCommand, action);
	}

	/* @see org.mindswap.owls.grounding.UPnPAtomicGrounding#getUPnPAction() */
	public String getUPnPAction()
	{
		return getPropertyAsString(FLAServiceOnt.upnpCommand);
	}

	/* @see org.mindswap.owls.grounding.AtomicGrounding#getDescriptionURL() */
	public URL getDescriptionURL() {
		try {
			final ControlPoint cp = new ControlPoint();
			final Device device = cp.getProxyDevice(getUPnPDescription());
			final String url = device.getPresentationURL();
			cp.stop();
			return new URL(url);
		} catch(final Exception e) {
			return null;
		}
	}

	/* @see org.mindswap.owls.grounding.UPnPAtomicGrounding#setUPnPDescription(java.lang.String) */
	public void setUPnPDescription(final String description) {
		setProperty(FLAServiceOnt.upnpDeviceURL, description);
	}

	/* @see org.mindswap.owls.grounding.UPnPAtomicGrounding#getUPnPDescription() */
	public String getUPnPDescription() {
		return getPropertyAsString(FLAServiceOnt.upnpDeviceURL);
	}

	/* @see org.mindswap.owls.grounding.AtomicGrounding#invoke(org.mindswap.query.ValueMap) */
	public ValueMap<Output, OWLValue> invoke(final ValueMap<Input, OWLValue> values) throws ExecutionException
	{
		return invoke(values, OWLFactory.createKB());
	}

	/* @see org.mindswap.owls.grounding.AtomicGrounding#invoke(org.mindswap.query.ValueMap, org.mindswap.owl.OWLKnowledgeBase) */
	public ValueMap<Output, OWLValue> invoke(final ValueMap<Input, OWLValue> inputs, final OWLKnowledgeBase env)
		throws ExecutionException
	{
		final ControlPoint cp = new ControlPoint();
		final Device device = cp.getProxyDevice(getUPnPDescription());
		final Service service = device.getService(getUPnPService());
		final Action action = service.getAction(getUPnPAction());

		final ValueMap<Output, OWLValue> results = new ValueMap<Output, OWLValue>();

		final ArgumentList inArguments = action.getInputArgumentList();
		for (int i = 0; i < inArguments.size(); i++)
		{
			final Argument in = inArguments.getArgument(i);
			final MessageMap<String> mp = getMessageMap(true, in.getName());
			final Input input = mp.getOWLSParameter().castTo(Input.class);

			Object value = getParameterValue(input, inputs);
			Object inputValue = value;

			if (mp.getTransformation() != null) {
				value = XSLTEngine.transform(value.toString(), mp.getTransformation());
				final Node node  = Utils.getAsNode(value.toString());
				inputValue = node.getFirstChild().getNodeValue();
			}
			logger.debug("Parameter {}, value = {}", input, value);

			action.setArgumentValue(mp.getGroundingParameter(), inputValue.toString());
		}

		final boolean ctrlRes = action.postControlAction();
		if (ctrlRes == false) {
			final UPnPStatus err = action.getControlStatus();
			throw new ExecutionException(err.getDescription() + " (" + Integer.toString(err.getCode()) + ")");
		}

		final ArgumentList outArguments = action.getOutputArgumentList();
		for (int i = 0; i < outArguments.size(); i++)
		{
			final Argument out = outArguments.getArgument(i);
			final MessageMap<String> mp = getMessageMap(false, out.getName());

			if (mp == null) continue;

			final Output param = mp.getOWLSParameter().castTo(Output.class);

			Object outputValue = null;

			if (mp.getTransformation() == null) outputValue = out.getValue();
			else outputValue = XSLTEngine.transform(out.getValue(), mp.getTransformation());

			if (param.getParamType().isDataType())
				results.setValue(param, env.createDataValue(outputValue));
			else
				results.setValue(param, env.parseLiteral(outputValue.toString()));
		}

		return results;
	}

	/* @see org.mindswap.owls.grounding.AtomicGrounding#getGroundingType() */
	public String getGroundingType() { return AtomicGrounding.GROUNDING_UPNP; }

	@Override protected MessageMap<String> createInputMessageMap() { return getOntology().createUPnPMessageMap(null); }

	@Override protected MessageMap<String> createOutputMessageMap() { return getOntology().createUPnPMessageMap(null); }

	@Override protected OWLObjectProperty inputMessageMapProperty() { return FLAServiceOnt.upnpInputMapping; }

	@Override protected Class<? extends MessageMap<String>> messageMapType() { return StringMessageMap.class; }

	@Override protected OWLObjectProperty outputMessageMapProperty() { return FLAServiceOnt.upnpOutputMapping; }

}