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

/*
 * Created on Dec 28, 2003
 *
 */
package impl.owls.grounding;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import org.mindswap.exceptions.ExecutionException;
import org.mindswap.exceptions.ServiceNotAvailableException;
import org.mindswap.owl.OWLDataValue;
import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owl.OWLObjectProperty;
import org.mindswap.owl.OWLValue;
import org.mindswap.owls.grounding.AtomicGrounding;
import org.mindswap.owls.grounding.MessageMap;
import org.mindswap.owls.grounding.WSDLAtomicGrounding;
import org.mindswap.owls.grounding.WSDLGrounding;
import org.mindswap.owls.grounding.WSDLOperationRef;
import org.mindswap.owls.grounding.MessageMap.StringMessageMap;
import org.mindswap.owls.process.variable.Input;
import org.mindswap.owls.process.variable.Output;
import org.mindswap.owls.process.variable.Parameter;
import org.mindswap.owls.vocabulary.OWLS;
import org.mindswap.query.ValueMap;
import org.mindswap.utils.URIUtils;
import org.mindswap.utils.Utils;
import org.mindswap.utils.XSLTEngine;
import org.mindswap.wsdl.WSDLOperation;
import org.mindswap.wsdl.WSDLParameter;
import org.mindswap.wsdl.WSDLService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

/**
 *
 * @author unascribed
 * @version $Rev: 2335 $; $Author: nick $; $Date: 2011/01/24 06:37:52 $
 */
public class WSDLAtomicGroundingImpl extends MessageMapAtomicGroundingImpl<String> implements WSDLAtomicGrounding
{
	private static final Logger logger = LoggerFactory.getLogger(WSDLAtomicGroundingImpl.class);

	private WSDLOperation wsdlOperation;

	public WSDLAtomicGroundingImpl(final OWLIndividual ind)
	{
		super(ind);
	}

	/* @see org.mindswap.owls.grounding.AtomicGrounding#getGrounding() */
	public WSDLGrounding getGrounding()
	{
		return getGrounding(WSDLGrounding.class);
	}

	/* @see org.mindswap.owls.grounding.WSDLAtomicGrounding#setWSDL(java.net.URI) */
	public void setWSDL(final URI wsdlLoc)
	{
		wsdlOperation = null; // indirect dependency to WSDL document, cf. initWSDLOperation()
		setProperty(OWLS.Grounding.wsdlDocument, wsdlLoc);
	}

	/* @see org.mindswap.owls.grounding.WSDLAtomicGrounding#getWSDL() */
	public URI getWSDL() {
		return getPropertyAsURI(OWLS.Grounding.wsdlDocument);
	}

	/* @see org.mindswap.owls.grounding.WSDLAtomicGrounding#setOperation(java.net.URI) */
	public void setOperation(final URI operation)
	{
		wsdlOperation = null;  // dependency to WSDL operation, cf. initWSDLOperation()
		WSDLOperationRef opRef = getOperationRef();
		if (opRef == null) {
			opRef = getOntology().createWSDLOperationRef(null);
			setOperationRef(opRef);
		}
		opRef.setOperation(operation);
	}

	/* @see org.mindswap.owls.grounding.WSDLAtomicGrounding#getOperation() */
	public URI getOperation()
	{
		return getOperationRef().getOperation();
	}

	/* @see org.mindswap.owls.grounding.WSDLAtomicGrounding#setPortType(java.net.URI) */
	public void setPortType(final URI port)
	{
		WSDLOperationRef opRef = getOperationRef();
		if (opRef == null)
		{
			opRef = getOntology().createWSDLOperationRef(null);
			setOperationRef(opRef);
		}
		opRef.setPortType(port);
	}

	/* @see org.mindswap.owls.grounding.WSDLAtomicGrounding#getPortType() */
	public URI getPortType()
	{
		return getOperationRef().getPortType();
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
		initWSDLOperation();
		for (final WSDLParameter in : wsdlOperation.getInputs())
		{
			final MessageMap<String> mp = getMessageMap(true, in.getName());
			if (mp == null) continue;

			final Input input = mp.getOWLSParameter().castTo(Input.class);
			final OWLValue inputValue = getParameterValue(input, inputs);

			Object inputValueObj;
			final String xslt = mp.getTransformation();
			if (inputValue.isIndividual())
			{
				final String rdfXML = ((OWLIndividual) inputValue).toRDF(true, true);
				if (xslt != null)
				{
					final String xsltResult = XSLTEngine.transform(rdfXML, xslt, inputs);
					inputValueObj = Utils.getAsNode(xsltResult);
					if (inputValueObj == null) inputValueObj = xsltResult.trim();

//					inputValue = XSLTEngine.transformToNode(rdfXML, xslt, values);
				}
				else
				{
					logger.debug("No XSLT transformation for input parameter " + input + " specified." +
						" OWLIndividual bound to this parameter should be transformed rather than using" +
						" its RDF/XML serialization.");
					inputValueObj = rdfXML;
				}
			}
			else // it is a OWLDataValue
			{
				if (xslt != null) throw new ExecutionException("XSLT transformation for input parameter " +
					input + " cannot be applied to OWL data value (only to OWL individual).");

				inputValueObj = ((OWLDataValue) inputValue).getValue();
			}

			in.setValue(inputValueObj);
		}

		try
		{
			wsdlOperation.invoke();
		}
		catch(final Exception e)
		{
			throw new ExecutionException(e);
		}

		final ValueMap<Output, OWLValue> results = new ValueMap<Output, OWLValue>();
		for (final Output outputParam : getProcess().getOutputs())
		{
			final MessageMap<String> mp = getMessageMap(outputParam);
			final String wsdlMessagePart = mp.getGroundingParameter();
			final String xslt = mp.getTransformation();
			Object outputValue;

			if (wsdlMessagePart != null && xslt == null) // mp is a DirectOutputMessageMap
			{
				final WSDLParameter out = wsdlOperation.getOutput(wsdlMessagePart);
				if (out == null) throw new ExecutionException("WSDL message part " + wsdlMessagePart +
					" is not found in the response of the WSDL operation " + wsdlOperation);

				outputValue = out.getValue();
				if (outputValue == null) throw new ExecutionException("Value of output parameter " + out +
					" is not set in the response of the WSDL operation " + wsdlOperation);
			}
			else if (wsdlMessagePart == null && xslt != null) // mp is a XSLTOutputMessageMap
			{
				final Node response = wsdlOperation.getResponse();
				outputValue = XSLTEngine.transform(response, xslt);
				if (outputValue == null) throw new ExecutionException(
					"Null value returned when applying XSLT transformtion for output parameter " + outputParam);
			}
			else // invalid: neither DirectOutputMessageMap nor XSLTOutputMessageMap
			{
				throw new ExecutionException("WSDLOutputMessageMap for Output " + outputParam + " is neither a " +
					"DirectOutputMessageMap nor XSLTOutputMessageMap.");
			}

			if (outputParam.getParamType().isDataType())
				results.setValue(outputParam, env.createDataValue(outputValue));
			else
				results.setValue(outputParam, env.parseLiteral(outputValue.toString()));
		}

		return results;
	}

	/* @see org.mindswap.owls.grounding.AtomicGrounding#getDescriptionURL() */
	public URL getDescriptionURL() {
		try {
			return getWSDL().toURL();
		} catch(final MalformedURLException e) {
			return null;
		}
	}

	/* @see org.mindswap.owls.grounding.WSDLAtomicGrounding#getInputMessage() */
	public URI getInputMessage() {
		return getPropertyAsURI(OWLS.Grounding.wsdlInputMessage);
	}

	/* @see org.mindswap.owls.grounding.WSDLAtomicGrounding#setInputMessage(java.net.URI) */
	public void setInputMessage(final URI inputMessage) {
		setProperty(OWLS.Grounding.wsdlInputMessage, inputMessage);
	}

	/* @see org.mindswap.owls.grounding.WSDLAtomicGrounding#getOutputMessage() */
	public URI getOutputMessage() {
		return getPropertyAsURI(OWLS.Grounding.wsdlOutputMessage);
	}

	/* @see org.mindswap.owls.grounding.WSDLAtomicGrounding#setOutputMessage(java.net.URI) */
	public void setOutputMessage(final URI outputMessage) {
		setProperty(OWLS.Grounding.wsdlOutputMessage, outputMessage);
	}

	/* @see org.mindswap.owls.grounding.WSDLAtomicGrounding#getOperationRef() */
	public WSDLOperationRef getOperationRef()
	{
		return getPropertyAs(OWLS.Grounding.wsdlOperation, WSDLOperationRef.class);
	}

	/* @see org.mindswap.owls.grounding.WSDLAtomicGrounding#setOperationRef(org.mindswap.owls.grounding.WSDLOperationRef) */
	public void setOperationRef(final WSDLOperationRef operationRef)
	{
		setProperty(OWLS.Grounding.wsdlOperation, operationRef);
	}

	/* @see org.mindswap.owls.grounding.AtomicGrounding#getGroundingType() */
	public String getGroundingType() { return AtomicGrounding.GROUNDING_WSDL; }

	public void removeOperationRef() {
		if (hasProperty(OWLS.Grounding.wsdlOperation)) {
			final WSDLOperationRef opRef = getOperationRef();
			opRef.removeOperation();
			opRef.removePortType();
			removeProperty(OWLS.Grounding.wsdlOperation, null);
			opRef.delete();
		}
	}

	private void removeAll() {
		removeProperty(OWLS.Grounding.wsdlDocument, null);
		removeOperationRef();

		removeProperty(OWLS.Grounding.wsdlInputMessage, null);
		removeProperty(OWLS.Grounding.wsdlOutputMessage, null);

		removeMessageMaps(true);
		removeMessageMaps(false);
	}

	/* @see impl.owl.WrappedIndividual#delete() */
	@Override
	public void delete() {
		removeAll();
		super.delete();
	}

	/* @see org.mindswap.owls.grounding.WSDLAtomicGrounding#getWSDLParameter(org.mindswap.owls.process.Parameter) */
	public URI getWSDLParameter(final Parameter parameter) {
		URI uri = getWSDLParameter(parameter, getInputMappings());
		if (uri == null) uri = getWSDLParameter(parameter, getOutputMappings());
		return uri;
	}

	@Override protected MessageMap<String> createInputMessageMap() { return getOntology().createWSDLInputMessageMap(null); }

	@Override protected MessageMap<String> createOutputMessageMap() { return getOntology().createWSDLOutputMessageMap(null); }

	@Override protected OWLObjectProperty inputMessageMapProperty() { return OWLS.Grounding.wsdlInput; }

	@Override protected OWLObjectProperty outputMessageMapProperty() { return OWLS.Grounding.wsdlOutput; }

	@Override protected Class<? extends MessageMap<String>> messageMapType() { return StringMessageMap.class; }

	private URI getWSDLParameter(final Parameter parameter,
		final OWLIndividualList<? extends MessageMap<String>> list)
	{
		for (final MessageMap<String> messageMap : list)
		{
			if (messageMap.getOWLSParameter().equals(parameter)) return messageMap.getGroundingParameterAsURI();
		}
		return null;
	}

	/** Assigns the {@link #wsdlOperation WSDL Operation} field. */
	private void initWSDLOperation() throws ExecutionException
	{
		if (wsdlOperation == null)
		{
			final String operation = getOperation().toString();
			final WSDLService s;
			try
			{
				s = WSDLService.createService(getWSDL());
			}
			catch (final Exception e)
			{
				throw new ServiceNotAvailableException(getProcess(), e);
			}
			wsdlOperation = s.getOperation(operation);
			if (wsdlOperation == null) wsdlOperation = s.getOperation(URIUtils.getLocalName(operation));
			if (wsdlOperation == null)
				throw new ExecutionException("Operation " + operation + " cannot be found in the WSDL description");
		}
	}

}