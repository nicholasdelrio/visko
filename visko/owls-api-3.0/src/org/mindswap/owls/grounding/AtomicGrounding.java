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
 * Created on Dec 27, 2003
 */
package org.mindswap.owls.grounding;

import java.net.URL;

import org.mindswap.exceptions.ExecutionException;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owl.OWLValue;
import org.mindswap.owls.process.AtomicProcess;
import org.mindswap.owls.process.variable.Input;
import org.mindswap.owls.process.variable.Output;
import org.mindswap.owls.process.variable.Parameter;
import org.mindswap.owls.vocabulary.OWLS;
import org.mindswap.query.ValueMap;

/**
 * Represents a grounding for {@link AtomicProcess atomic processes}.
 * This interface is intended to be the base for various grounding types such
 * as {@link WSDLAtomicGrounding} and {@link UPnPAtomicGrounding} classes.
 * <p>
 * As of OWL-S 1.2 the corresponding concept in the OWL-S ontology is
 * <a href="http://www.daml.org/services/owl-s/1.2/Grounding#AtomicProcessGrounding">
 * AtomicProcessGrounding</a>.
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:54 $
 */
public interface AtomicGrounding<T> extends OWLIndividual
{
	public final static String GROUNDING_WSDL = "WSDL";
	public final static String GROUNDING_UPNP = "UPNP";
	public final static String GROUNDING_JAVA = "Java";

	/**
	 * Get a URL that describes the properties of this grounding. The nature of
	 * the file depends on the actual grounding type, e.g., WSDL grounding will
	 * return the URL of the WSDL document, whereas the UPNP grounding returns
	 * the UPnP presentation URL.
	 *
	 * @return The URL of the document further describing this atomic process
	 * 	grounding.
	 * @throws UnsupportedOperationException If this grounding does not support
	 * 	this property.
	 */
	public URL getDescriptionURL();

	/**
	 * @return The atomic process individual for this grounding.
	 */
	public AtomicProcess getProcess();

	/**
	 * @param process Set the atomic process object for this grounding.
	 */
	public void setProcess(AtomicProcess process);

	public void addMessageMap(Parameter owlsParameter, String groundingParameter, T transformation);

	public void addInputMap(MessageMap<T> map);

	public void addOutputMap(MessageMap<T> map);

	/**
	 * @return The mappings from the inputs of the atomic process to the
	 * 	grounding operation inputs.
	 */
	public OWLIndividualList<? extends MessageMap<T>> getInputMappings();

	/**
	 * @return The mappings from the grounding operation outputs to the
	 * 	outputs of the atomic process.
	 */
	public OWLIndividualList<? extends MessageMap<T>> getOutputMappings();

	/**
	 * Invoke this grounding with the given input values.
	 *
	 * Input values bound to the input parameters of the backing
	 * 	process, i.e., the inputs supposed to be consumed by the process.
	 * @return Output values bound to output parameters of the backing atomic
	 * 	process, i.e., the outputs produced by the process.
	 * @throws ExecutionException If invocation failed or returned prematurely
	 * 	(for whatever reason).
	 */
	public ValueMap<Output, OWLValue> invoke(ValueMap<Input, OWLValue> values) throws ExecutionException;

	/**
	 * Invoke this grounding with the given input values and use the given KB to
	 * store the output values.
	 *
	 * @param values Input values bound to the input parameters of the backing
	 * 	atomic process, i.e., the inputs supposed to be consumed by the process.
	 * @param kb The execution environment where output data is stored
	 * @return Output values bound to output parameters of the backing process,
	 * 	i.e., the outputs produced by the process.
	 * @throws ExecutionException If invocation failed or returned prematurely
	 * 	(for whatever reason).
	 */
	public ValueMap<Output, OWLValue> invoke(ValueMap<Input, OWLValue> values, OWLKnowledgeBase kb)
		throws ExecutionException;

	/**
	 * Get the enclosing grounding this atomic process grounding belongs to.
	 * If it is referenced by more than one grounding pick one arbitrarily.
	 * <p>
	 * In OWL-S every atomic process grounding is enclosed by an instance of
	 * the concept {@link OWLS.Grounding#Grounding}.
	 *
	 * @return The enclosing grounding.
	 */
	public Grounding<? extends AtomicGrounding<T>, T> getGrounding();

	/**
	 * Returns the type of grounding such as WSDL or Java, see public constants
	 * declared by this interface.
	 *
	 * @return A unique name representing the type of this atomic process grounding.
	 */
	public String getGroundingType();
}
