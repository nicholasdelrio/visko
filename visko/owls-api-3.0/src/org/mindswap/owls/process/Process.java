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
package org.mindswap.owls.process;

import java.net.URI;
import java.util.List;

import org.mindswap.exceptions.ExecutionException;
import org.mindswap.owl.OWLDataValue;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owl.OWLOntology;
import org.mindswap.owl.OWLType;
import org.mindswap.owls.process.execution.ExecutionContext;
import org.mindswap.owls.process.execution.ExecutionSupport;
import org.mindswap.owls.process.variable.Existential;
import org.mindswap.owls.process.variable.Input;
import org.mindswap.owls.process.variable.Output;
import org.mindswap.owls.process.variable.Participant;
import org.mindswap.owls.profile.Profile;
import org.mindswap.owls.service.Service;
import org.mindswap.owls.vocabulary.OWLS_Extensions;
import org.mindswap.utils.ProcessDataFlow;

/**
 * Abstract representation of OWL-S processes. A process can be either a
 * {@link AtomicProcess atomic}, {@link CompositeProcess composite}, or a
 * {@link SimpleProcess simple} process. Properties that all three process
 * types share are defined here.
 * <p>
 * Corresponding OWL-S concept: {@link org.mindswap.owls.vocabulary.OWLS.Process#Process}.
 *
 * @author unascribed
 * @version $Rev: 2350 $; $Author: nick $; $Date: 2011/01/24 06:37:52 $
 */
public interface Process extends OWLIndividual, MultiConditional
{
	public int ANY = 7;
	public int ATOMIC = 1;
	public int COMPOSITE = 2;
	public int SIMPLE = 4;

	/**
	 * Set the service this process belongs to.
	 *
	 * @param service
	 */
	public void setService(Service service);

	/**
	 * Get the service this process belongs to. Actually a process may be used in multiple service
	 * descriptions. Unfortunately, OWL-S 1.0 specification does not make a distinction between
	 * process definition and process occurrence. This implementation treats each process object as
	 * a process occurrence and returns the service object this process is used in.
	 *
	 * @return the service to which this process is bound
	 */
	public Service getService();

	/**
	 * Removes the service from this process by breaking the link
	 * <code>service:describes</code>. The service itself remains untouched.
	 */
	public void removeService();

	/**
	 * @return The profile for the service of this process. This is equivalent
	 * 	to <code>getService().getProfile()</code>.
	 */
	public Profile getProfile();

	public void addInput(Input input);

	/**
	 * @param uri The URI for the input parameter, or <code>null</code> to create
	 * 	an	anonymous instance.
	 * @param paramType The OWL class or datatype of the input parameter. Can be
	 * 	<code>null</code> to create an untyped variable.
	 * @return The newly created input parameter, associated with this process.
	 */
	public Input createInput(URI uri, OWLType paramType);

	public void addInputs(OWLIndividualList<Input> inputs);

	public void addOutput(Output output);

	/**
	 * @param uri The URI for the output parameter, or <code>null</code> to create
	 * 	an	anonymous instance.
	 * @param paramType The OWL class or datatype of the input parameter. Can be
	 * 	<code>null</code> to create an untyped variable.
	 * @return The newly created output parameter, associated with this process.
	 */
	public Output createOutput(URI uri, OWLType paramType);

	public void addOutputs(OWLIndividualList<Output> inputs);

	public void addResult(Result result);

	public Result createResult(URI uri);

	/**
	 * @return The inputs of this process. An empty list is returned if there
	 * 	are no inputs.
	 */
	public OWLIndividualList<Input> getInputs();

	/**
	 * @return The first input found or <code>null</code> if there is no input.
	 */
	public Input getInput();

	public Input getInput(String localName);

	/**
	 * @return The outputs of this process. An empty list is returned if there
	 * 	are no outputs.
	 */
	public OWLIndividualList<Output> getOutputs();

	/**
	 * @return The first output found or <code>null</code> if there is no input.
	 */
	public Output getOutput();

	public Output getOutput(String localName);

	public Result getResult();

	public OWLIndividualList<Result> getResults();

	/**
	 * Removes the given result by breaking the property <code>process:hasResult</code>
	 * The result itself is not touched at all.
	 *
	 * @param result The result to remove. A value of <code>null</code> will remove
	 * 	all results.
	 */
	public void removeResult(Result result);

	public void addParticipant(Participant participant);

	/**
	 * @param uri The URI for the participant, or <code>null</code> to create
	 * 	an	anonymous instance.
	 * @param varType The OWL class or datatype of the input parameter. Can be
	 * 	<code>null</code> to create an untyped variable.
	 * @return The newly created participant, associated with this process.
	 */
	public Participant createParticipant(URI uri, OWLType varType);

	public Participant getParticipant();

	public OWLIndividualList<Participant> getParticipants();

	/**
	 * Removes the given participant by breaking the property <code>process:hasParticipant</code>
	 * The participant itself is not touched at all.
	 *
	 * @param participant The participant to remove. A value of <code>null</code> will remove
	 * 	all participants.
	 */
	public void removeParticipant(Participant participant);

	public void addExistential(Existential existential);

	/**
	 * @param uri The URI for the input parameter, or <code>null</code> to create
	 * 	an	anonymous instance.
	 * @param varType The OWL class or datatype of the input parameter. Can be
	 * 	<code>null</code> to create an untyped variable.
	 * @return The newly created input parameter, associated with this process.
	 */
	public Existential createExistential(URI uri, OWLType varType);

	public Existential getExistential();

	public OWLIndividualList<Existential> getExistentials();

	/**
	 * Removes the given existential by breaking the property <code>process:hasExistential</code>
	 * The existential itself is not touched at all.
	 *
	 * @param existential The existential to remove. A value of <code>null</code> will remove
	 * 	all existentials.
	 */
	public void removeExistential(Existential existential);

	/**
	 * @return The name defined for this process. See {@link org.mindswap.owl.OWLConfig#DEFAULT_LANGS OWLConfig}
	 * 	to see how the language identifiers will be resolved when searching
	 * 	for a name.
	 */
	public String getName();

	/**
	 * @param lang The language identifier.
	 * @return The name defined for this process. The associated name should have
	 * 	the same language identifier as given in the parameter. If a name for
	 * 	that language is not found null value will be returned even if there is
	 * 	another name with a different language identifier.
	 */
	public String getName(String lang);

	/**
	 * @return All process names written in any language.
	 */
	public List<OWLDataValue> getNames();

	/**
	 * Returns the perform into which this process is possibly embedded.
	 * Attention: A process may be embedded by many Performs. If this is
	 * the case the returned Perform is arbitrarily chosen.
	 *
	 * @return The perform into which this process is embedded. <code>null</code>,
	 * 	if the process is not embedded in a perform.
	 */
	public Perform getPerform();

//	/**
//	 * Removes the perform to which this process is bound by breaking the object
//	 * property {@link OWLS_Extensions.Process#hasPerform}. The perform itself
//	 * remains untouched. Attention: This is only a in-memory property and is not
//	 * stored at all as OWL-S does not define a link from Process to Perform.
//	 */
//	public void removePerform(Perform perform);

//	/**
//	 * Sets the perform to which this process is bound. Attention: This is only a
//	 * in-memory property and is not stored at all as OWL-S does not define a
//	 * link from Process to Perform.
//	 *
//	 * @param perform The perform to which this process is bound.
//	 */
//	public void setPerform(Perform perform);

	/**
	 * Removes the given input by breaking the property <code>process:hasInput</code>
	 * The input itself is not touched at all.
	 *
	 * @param input The input to remove. A value of <code>null</code> will remove
	 * 	all inputs.
	 */
	public void removeInput(Input input);

	/**
	 * Removes the given output by breaking the property <code>process:hasOutput</code>
	 * The output itself is not touched at all.
	 *
	 * @param output The output to remove. A value of <code>null</code> will remove
	 * 	all outputs.
	 */
	public void removeOutput(Output output);

	/**
	 * Executes this process provided that it is grounded.
	 *
	 * @param context Any kind of context information required for execution.
	 * @param target The backing execution object that must be invoked for
	 * 	inner {@link ControlConstruct control constructs} respectively
	 * 	{@link AtomicProcess atomic processes}.
	 * @throws ExecutionException In case of (unrecoverable) failures
	 * 	during execution.
	 */
	public <C extends ExecutionContext> void execute(C context, ExecutionSupport<C> target) throws ExecutionException;

	/**
	 * Prepare this process for subsequent execution. Implementations may set up
	 * means to optimize execution in terms of whatever resources, e.g., space
	 * and time. However, support of this operation is a optional, hence,
	 * implementations may do nothing.
	 *
	 * @param context Any kind of context information to support preparation.
	 * @return The prepared process. Implementations may return the same (but
	 * 	prepared) object instance (<code>this</code>) or a new process
	 * 	instance, just as they like.
	 */
	public Process prepare(ExecutionContext context);

	/**
	 * This method analyzes the data flow defined by this process and returns
	 * a representation of it using the producer-push convention (rather than
	 * the consumer-pull convention of OWL-S). The producer-push convention is
	 * realized through properties defined in {@link OWLS_Extensions}.
	 * <p>
	 * Only {@link CompositeProcess composite processes} can actually have a
	 * data flow, whereas {@link AtomicProcess atomic processes} do not have a
	 * data flow as they can not be further decomposed.
	 * <p>
	 * Note that this operation may be costly, depending on the size of the
	 * process. Consequently, implementations should provide means to optimize
	 * repeated invocations of this method (e.g., by means of internal caches).
	 *
	 * @param dfOntology The backing ontology which will be enriched by the
	 * 	the producer-push data flow representation. Can be left <code>null</code>
	 * 	to create and use a new one (which will belong the associated
	 * 	{@link #getKB() KB}).
	 * @return The data flow structure represented by this process.
	 */
	public ProcessDataFlow getDataFlow(OWLOntology dfOntology);
}
