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

package org.mindswap.wsdl;

import java.util.Iterator;
import java.util.Vector;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

import org.apache.axis.AxisFault;
import org.apache.axis.client.Call;
import org.apache.axis.constants.Style;
import org.apache.axis.message.SOAPBodyElement;
import org.apache.axis.message.SOAPEnvelope;
import org.mindswap.owl.vocabulary.RDF;
import org.mindswap.utils.URIUtils;
import org.mindswap.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author unascribed
 * @version $Rev: 2288 $; $Author: nick $; $Date: 2011/01/24 06:37:54 $
 */
public class WSDLOperation
{
	private static final Logger logger = LoggerFactory.getLogger(WSDLOperation.class);

	private WSDLService service = null;
	private final Call call;
	private final Vector<WSDLParameter> inputs;
	private final Vector<WSDLParameter> outputs;
	private String operationName;
	private String inputMessageName;
	private String outputMessageName;
	private String portName;
	private String documentation;
	private SOAPElement response;

	public WSDLOperation(final Call c)
	{
		this.call = c;
		inputs = new Vector<WSDLParameter>();
		outputs = new Vector<WSDLParameter>();
	}

	public WSDLParameter getInput(final int i)
	{
		return getInputs().elementAt(i);
	}

	public WSDLParameter getInput(final String name)
	{
		for (final WSDLParameter in : inputs)
		{
			final String paramName = in.getName();
			if (paramName.equals(name) || URIUtils.getLocalName(paramName).equals(name)) return in;
		}

		return null;
	}

	public WSDLParameter getOutput(final int i)
	{
		return getOutputs().elementAt(i);
	}

	public WSDLParameter getOutput(final String name)
	{
		for (final WSDLParameter out : outputs)
		{
			final String paramName = out.getName();
			if (paramName.equals(name) || URIUtils.getLocalName(paramName).equals(name)) return out;
		}

		return null;
	}

	public Vector<WSDLParameter> getInputs()
	{
		return inputs;
	}

	public Vector<WSDLParameter> getOutputs()
	{
		return outputs;
	}

	void addInput(final String name, final QName type)
	{
		inputs.add(new WSDLParameter(name, type));
	}

	void addOutput(final String name, final QName type)
	{
		outputs.add(new WSDLParameter(name, type));
	}

	public String getName()
	{
		return call.getOperationName().getLocalPart();
	}

	public String getOperationName()
	{
		return operationName;
	}

	public void setOperationName(final String s)
	{
		operationName = s;
	}

	public String getPortName()
	{
		return portName;
	}

	public void setPortName(final String s)
	{
		portName = s;
	}

	public String getInputMessageName()
	{
		return inputMessageName;
	}

	public void setInputMessageName(final String s)
	{
		inputMessageName = s;
	}

	public String getOutputMessageName()
	{
		return outputMessageName;
	}

	public void setOutputMessageName(final String s)
	{
		outputMessageName = s;
	}

	public WSDLService getService()
	{
		return service;
	}

	public void setService(final WSDLService s)
	{
		service = s;
	}

	public String getDocumentation()
	{
		return documentation;
	}

	public void setDocumentation(final String s)
	{
		documentation = s;
	}

	/* @see java.lang.Object#toString() */
	@Override
	public String toString()
	{
		return getName();
	}

	public String getDescription()
	{
		final StringBuilder s = new StringBuilder();
		s.append(getName()).append("(");

		for (final Iterator<WSDLParameter> i = inputs.iterator(); i.hasNext();)
		{
			final WSDLParameter param = i.next();
			s.append(param.getName()).append(":").append(param.getType().getLocalPart());
			if (i.hasNext()) s.append(", ");
		}
		s.append(") -> (");

		for (final Iterator<WSDLParameter> i = outputs.iterator(); i.hasNext();)
		{
			final WSDLParameter param = i.next();
			s.append(param.getName()).append(":").append(param.getType().getLocalPart());
			if (i.hasNext()) s.append(", ");
		}
		s.append(")");

		return s.toString();
	}

	/**
	 * @return The entire response object, after this operation was invoked.
	 */
	public SOAPElement getResponse()
	{
		return response;
	}

	public void invoke() throws AxisFault, SOAPException
	{
		final SOAPEnvelope request = createRequest();

		logger.debug("Invoke WSDL operation {}.", getDescription());
		final SOAPEnvelope reply = call.invoke(request);
//		logger.debug(Utils.formatXML(reply));

		processResult(reply);
	}

	private void processResult(final SOAPEnvelope reply) throws SOAPException
	{
		final SOAPElement soapBody = reply.getBody();
		response = (SOAPElement) soapBody.getChildElements().next();
		final Iterator<SOAPElement> messageParts = response.getChildElements();

		for (final WSDLParameter output : outputs)
		{
			final SOAPElement e = messageParts.next();
			output.setTextValue(e.toString());

			if (logger.isDebugEnabled())
			{
				final StringBuilder sb = new StringBuilder("processResult ").append(e).append(Utils.LINE_SEPARATOR);
				sb.append("getValue ").append(e.getValue()).append(Utils.LINE_SEPARATOR);
				sb.append("getType ").append(e.getNodeType() == Node.ELEMENT_NODE).append(Utils.LINE_SEPARATOR);
				sb.append("getValue is null? ").append(e.getValue() == null).append(Utils.LINE_SEPARATOR);
				sb.append("result has children? ").append(e.getChildElements().hasNext()).append(Utils.LINE_SEPARATOR);
				if (e.getChildElements().hasNext())
					sb.append("result first child ").append(e.getChildElements().next());
				logger.debug(sb.toString());
			}

			final Iterator<Node> children = e.getChildElements();
			if (children.hasNext()) {
				final Node child = children.next();
				if (child.getNodeType() == Node.TEXT_NODE) output.setValue(child.toString());
				else output.setValue(e.toString());
			}
			else output.setValue( e.toString() );
		}
	}

	private SOAPEnvelope createRequest() throws SOAPException
	{
		final String targetNamespace = call.getOperationName().getNamespaceURI();
		final String opName = call.getOperationName().getLocalPart();
		final SOAPEnvelope envelope = new SOAPEnvelope();

		logger.debug("SOAP Action = {}, SOAP Action used = {}", call.getSOAPActionURI(), call.useSOAPAction());

		// next two lines automatically done by default constructor of SOAPEnvelope (at least with AXIS 1.4)
//		envelope.addNamespaceDeclaration("xsi", WSDLConsts.xsiURI);
//		envelope.addNamespaceDeclaration("xsd", WSDLConsts.xsdURI);

		//envelope.addNamespaceDeclaration("ns0", targetNamespace);

		// TODO adapt for correct handling of RPC/encoded and Document/literal
		final String inputEncodingStyle = "http://schemas.xmlsoap.org/soap/encoding/";
		envelope.setEncodingStyle(inputEncodingStyle);
		// next line not necessary since it does the same than previous line
//		envelope.addAttribute(WSDLConsts.soapURI, "encodingStyle", inputEncodingStyle);

		// FIXME test this feature
		final String nsOp = call.getOperationStyle().equals(Style.RPC)? "u" : "";
		final SOAPBodyElement soapBody = new SOAPBodyElement(envelope.createName(opName, nsOp, targetNamespace));
		envelope.addBodyElement(soapBody);

		for (final WSDLParameter param : inputs)
		{
			final Object paramValue = param.getValue();

			if (paramValue == null) continue;

			final SOAPElement soapElement = soapBody.addChildElement(URIUtils.getLocalName(param.getName()), "");

			if (paramValue instanceof Node)
			{
				logger.debug("Case 1: DOM Node");
				createSOAPElement(soapElement, (Node) paramValue);
				if (soapElement.getAttributeValue(WSDLConsts.xsiType) == null)
				{
					logger.debug("Case 1a");
					soapElement.addAttribute(WSDLConsts.xsiType, "u:" + param.getType().getLocalPart());
				}
			}
			else // paramValue should be a Java primitive type wrapper, URL, URI, Date, or Calendar in this case
			{
				logger.debug("Case 2: {}", param.getType());
				soapElement.addAttribute(WSDLConsts.xsiType, "xsd:" + param.getType().getLocalPart());
				soapElement.addTextNode(paramValue.toString());
			}
		}
		return envelope;
	}

	private void createSOAPElement(final SOAPElement parent, final Node node) throws SOAPException
	{
		final int type = node.getNodeType();

		switch (type)
		{
			case Node.TEXT_NODE:
			{
				logger.debug("Case 3: DOM Text node.");

				// TODO the following is not thoroughly tested
				// if the enclosing tag of the text node has rdf:datatype set then assume
				// it is a XSD simple type add set the actual XSD type to the SOAP parent
				String datatype = null;
				final NamedNodeMap temp = node.getParentNode().getAttributes();
				if (temp != null)
				{
					final Node n = temp.getNamedItemNS(RDF.ns, "datatype");
					if (n != null)
					{
						datatype = n.getNodeValue();
						if (datatype != null) datatype = datatype.substring(datatype.indexOf("#") + 1);
					}
				}
				if (datatype != null) parent.addAttribute(WSDLConsts.xsiType, "xsd:" + datatype);
				parent.addTextNode(node.getNodeValue());
				break;
			}
			case Node.ELEMENT_NODE:
			{
				if (node.getParentNode() instanceof org.w3c.dom.Document)
				{
					logger.debug("Case 4: DOM Element node whose parent is DOM Document.");
					processChildNodes(parent, node);
				}
				else
				{
					logger.debug("Case 5: DOM Element node whose parent is not DOM Document.");
					processChildNodes(parent.addChildElement(node.getNodeName()), node);
				}
				break;
			}
			case Node.DOCUMENT_NODE:
			{
				logger.debug("Case 3: DOM document node");
				processChildNodes(parent, node);
				break;
			}
			default:
				break;
		}
	}

	private void processChildNodes(final SOAPElement target, final Node source) throws SOAPException
	{
		final NodeList children = source.getChildNodes();
		if (children != null)
		{
			final int len = children.getLength();
			for (int i = 0; i < len; i++)
			{
				createSOAPElement(target, children.item(i));
			}
		}
	}
}
