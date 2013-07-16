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

package org.mindswap.owls.grounding;

import java.net.URI;

import org.mindswap.owls.process.variable.Parameter;

/**
 * Each instance of WsdlAtomicGrounding must have exactly one value for
 * owlsProcess and exactly one for wsdlOperation, which sets up a one-to-one
 * correspondence between an atomic process and a WSDL operation.
 * <p>
 * In addition, for each message part of the relevant WSDL input message, there
 * must be exactly one value of wsdlInput. For each output of the atomic process,
 * there must be exactly one value of wsdlOutput. These property instances show
 * the correspondences between OWL-S inputs/outputs and WSDL message parts. In
 * addition, single values are recommended (but not required) for
 * {@link #getInputMessage()} and {@link #getOutputMessage()}.
 *
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:54 $
 */
public interface WSDLAtomicGrounding extends AtomicGrounding<String>
{
	public void setWSDL(URI wsdlLoc);
	public URI getWSDL();

	public void setOperation(URI op);
	public URI getOperation();

	public void setPortType(URI port);
	public URI getPortType();

	/**
	 * @param input The input message to set.
	 */
	public void setInputMessage(URI input);
	/**
	 * @return Returns the input message.
	 */
	public URI getInputMessage();

	/**
	 * @param output The output message to set.
	 */
	public void setOutputMessage(URI output);
	/**
	 * @return Returns the output message.
	 */
	public URI getOutputMessage();

	public WSDLOperationRef getOperationRef();
	public void setOperationRef(WSDLOperationRef operationRef);

	public URI getWSDLParameter(Parameter parameter);

// Will the following be ever needed?

// Move this to class JavaDoc
//	 * Note that the value of {@link #getOperation()} may or may not uniquely identify
//	 * a particular WSDL port with which to interact. If there are multiple such ports
//	 * offering the specified operation, an OWL-S enactment engine is free to choose
//	 * any of these ports. If it is desired to further constrain the choice of ports,
//	 * a WsdlAtomicGrounding may do so by specifying values for {@link #getService()}
//	 * and/or {@link #getPort()}.
//
//	/**
//	 * @return A URI for a WSDL Port that provides the operation to which
//	 * 	the {@link #getProcess() atomic process} is grounded. This is a
//	 * 	optional information, see interface documentation.
//	 */
//	public URI getPort();
//
//	/**
//	 * @param wsdlPort A URI for a WSDL Port that provides the operation to
//	 * 	which	the {@link #getProcess() atomic process} is grounded. This
//	 * 	is a optional information, see interface documentation.
//	 */
//	public void setPort(URI wsdlPort);
//
//	/**
//	 * @return A URI for a WSDL Service that provides the operation to which
//	 * 	the {@link #getProcess() atomic process} is grounded. This is a
//	 * 	optional information, see interface documentation.
//	 */
//	public URI getService();
//
//	/**
//	 * @param wsdlService A URI for a WSDL Service that provides the operation
//	 * 	to which the {@link #getProcess() atomic process} is grounded. This
//	 * 	is a optional information, see interface documentation.
//	 */
//	public void setService(URI wsdlService);
}
