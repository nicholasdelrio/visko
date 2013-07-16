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
package impl.owls.process;

import impl.owl.WrappedIndividual;

import java.net.URI;
import java.util.List;

import org.mindswap.owl.OWLDataValue;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owl.OWLType;
import org.mindswap.owls.expression.Condition;
import org.mindswap.owls.process.AtomicProcess;
import org.mindswap.owls.process.CompositeProcess;
import org.mindswap.owls.process.Perform;
import org.mindswap.owls.process.Process;
import org.mindswap.owls.process.Result;
import org.mindswap.owls.process.SimpleProcess;
import org.mindswap.owls.process.execution.ExecutionContext;
import org.mindswap.owls.process.variable.Existential;
import org.mindswap.owls.process.variable.Input;
import org.mindswap.owls.process.variable.Output;
import org.mindswap.owls.process.variable.Participant;
import org.mindswap.owls.profile.Profile;
import org.mindswap.owls.service.Service;
import org.mindswap.owls.vocabulary.OWLS;
import org.mindswap.utils.Utils;

/**
 * @author unascribed
 * @version $Rev: 2350 $; $Author: nick $; $Date: 2011/01/24 06:37:54 $
 */
public abstract class ProcessImpl<P extends ProcessImpl<P>> extends WrappedIndividual implements Process
{
	protected P process; // used for caching purposes for execution

	/**
	 * @see WrappedIndividual#WrappedIndividual(OWLIndividual)
	 */
	public ProcessImpl(final OWLIndividual ind)
	{
		super(ind);
	}

	/* @see org.mindswap.owls.process.Process#getService() */
	public Service getService()
	{
		return getPropertyAs(OWLS.Service.describes, Service.class);
	}

	/* @see org.mindswap.owls.process.Process#setService(org.mindswap.owls.service.Service) */
	public void setService(final Service service)
	{
		if (hasProperty(OWLS.Service.describes, service)) return;
		setProperty(OWLS.Service.describes, service);
		service.setProcess(this);
	}

	/* @see org.mindswap.owls.process.Process#removeService() */
	public void removeService()
	{
		removeProperty(OWLS.Service.describes, getService());
	}

	/* @see org.mindswap.owls.process.Process#getProfile() */
	public Profile getProfile()
	{
		return getService().getProfile();
	}

	/* @see org.mindswap.owls.process.Process#addInput(org.mindswap.owls.process.Input) */
	public void addInput(final Input input)
	{
		addProperty(OWLS.Process.hasInput, input);
	}

	/* @see org.mindswap.owls.process.Process#createInput(java.net.URI, org.mindswap.owl.OWLType) */
	public Input createInput(final URI uri, final OWLType paramType)
	{
		final Input in = getOntology().createInput(uri);
		if (paramType != null) in.setParamType(paramType);
		addInput(in);
		return in;
	}

	/* @see org.mindswap.owls.process.Process#addInputs(org.mindswap.owl.OWLIndividualList) */
	public void addInputs(final OWLIndividualList<Input> inputs)
	{
		for (final Input input : inputs)
		{
			addInput(input);
		}
	}

	/* @see org.mindswap.owls.process.Process#addOutput(org.mindswap.owls.process.Output) */
	public void addOutput(final Output output)
	{
		addProperty(OWLS.Process.hasOutput, output);
	}

	/* @see org.mindswap.owls.process.Process#createOutput(java.net.URI, org.mindswap.owl.OWLType) */
	public Output createOutput(final URI uri, final OWLType paramType)
	{
		final Output out = getOntology().createOutput(uri);
		if (paramType != null) out.setParamType(paramType);
		addOutput(out);
		return out;
	}

	/* @see org.mindswap.owls.process.Process#addOutputs(org.mindswap.owl.OWLIndividualList) */
	public void addOutputs(final OWLIndividualList<Output> outputs)
	{
		for (final Output output : outputs)
		{
			addOutput(output);
		}
	}

	/* @see org.mindswap.owls.process.Process#addResult(org.mindswap.owls.process.Result) */
	public void addResult(final Result result)
	{
		addProperty(OWLS.Process.hasResult, result);
	}

	/* @see org.mindswap.owls.process.Process#createResult(java.net.URI) */
	public Result createResult(final URI uri)
	{
		final Result result = getOntology().createResult(uri);
		addResult(result);
		return result;
	}

	/* @see org.mindswap.owls.process.Process#removeResult(org.mindswap.owls.process.Result) */
	public void removeResult(final Result result)
	{
		removeProperty(OWLS.Process.hasResult, result);
	}

	/* @see org.mindswap.owls.process.Process#getInputs() */
	public OWLIndividualList<Input> getInputs()
	{
		return getPropertiesAs(OWLS.Process.hasInput, Input.class);
	}

	/* @see org.mindswap.owls.process.Process#getInput() */
	public Input getInput()
	{
		return getPropertyAs(OWLS.Process.hasInput, Input.class);
	}

	/* @see org.mindswap.owls.process.Process#getInput(java.lang.String) */
	public Input getInput(final String localName)
	{
		final OWLIndividualList<Input> inputs = getInputs();
		return inputs.getIndividual(localName);
	}

	/* @see org.mindswap.owls.process.Process#getOutputs() */
	public OWLIndividualList<Output> getOutputs()
	{
		return getPropertiesAs(OWLS.Process.hasOutput, Output.class);
	}

	/* @see org.mindswap.owls.process.Process#getOutput() */
	public Output getOutput()
	{
		return getPropertyAs(OWLS.Process.hasOutput, Output.class);
	}

	/* @see org.mindswap.owls.process.Process#getOutput(java.lang.String) */
	public Output getOutput(final String localName)
	{
		final OWLIndividualList<Output> outputs = getOutputs();
		return outputs.getIndividual(localName);
	}

	/* @see org.mindswap.owls.process.MultiConditional#getConditions() */
	public OWLIndividualList<Condition> getConditions()
	{
		return getPropertiesAs(OWLS.Process.hasPrecondition, Condition.class);
	}

	/* @see org.mindswap.owls.process.MultiConditional#addCondition(org.mindswap.owls.process.Condition) */
	public void addCondition(final Condition<?> condition)
	{
		addProperty(OWLS.Process.hasPrecondition, condition);
	}

	/* @see org.mindswap.owls.process.MultiConditional#removeCondition(org.mindswap.owls.expression.Condition) */
	public void removeCondition(final Condition<?> condition)
	{
		removeProperty(OWLS.Process.hasPrecondition, condition);
	}

	/* @see org.mindswap.owls.process.Process#getResults() */
	public OWLIndividualList<Result> getResults()
	{
		return getPropertiesAs(OWLS.Process.hasResult, Result.class);
	}

	/* @see org.mindswap.owls.process.Process#getResult() */
	public Result getResult()
	{
		return getPropertyAs(OWLS.Process.hasResult, Result.class);
	}

	/* @see org.mindswap.owls.process.Process#getName() */
	public String getName()
	{
		return getPropertyAsString(OWLS.Process.name);
	}

	/* @see org.mindswap.owls.process.Process#getName(java.lang.String) */
	public String getName(final String lang)
	{
		return getPropertyAsString(OWLS.Process.name, lang);
	}

	/* @see org.mindswap.owls.process.Process#getNames() */
	public List<OWLDataValue> getNames()
	{
		return getProperties(OWLS.Process.name);
	}

	/* @see impl.owl.WrappedIndividual#toString() */
	@Override
	public String toString()
	{
		final StringBuilder str = new StringBuilder();

		if (this instanceof AtomicProcess)         str.append("Atomic Process    ");
		else if (this instanceof CompositeProcess) str.append("Composite Process ");
		else if (this instanceof SimpleProcess)    str.append("Simple Process    ");
		else                                       str.append("Process           ");

		str.append(getName0()).append(" ");
		str.append(Utils.LINE_SEPARATOR);

		str.append("Inputs            ").append(getInputs()).append(Utils.LINE_SEPARATOR);
		str.append("Outputs           ").append(getOutputs()).append(Utils.LINE_SEPARATOR);

		return str.toString();
	}

	/* @see org.mindswap.owls.process.Process#getPerform() */
	public Perform getPerform()
	{
//		return getPropertyAs(OWLS_Extensions.Process.hasPerform, Perform.class);
		return getIncomingPropertyAs(OWLS.Process.process, Perform.class);
	}

//	/* @see org.mindswap.owls.process.Process#removePerform(org.mindswap.owls.process.Perform) */
//	public void removePerform(final Perform perform)
//	{
//		if (perform != null) perform.removeProcess(this);
//		removeProperty(OWLS_Extensions.Process.hasPerform, perform);
//	}

//	/* @see org.mindswap.owls.process.Process#setPerform(org.mindswap.owls.process.Perform) */
//	public void setPerform(final Perform perform)
//	{
//		if (hasProperty(OWLS_Extensions.Process.hasPerform, perform)) return;
//
//		setProperty(OWLS_Extensions.Process.hasPerform, perform);
//		if (perform != null) perform.setProcess(this);
//	}

	/* @see org.mindswap.owls.process.Process#removeInput(org.mindswap.owls.process.Input) */
	public void removeInput(final Input input)
	{
		removeProperty(OWLS.Process.hasInput, input);
	}

	/* @see org.mindswap.owls.process.Process#removeOutput(org.mindswap.owls.process.Output) */
	public void removeOutput(final Output output)
	{
		removeProperty(OWLS.Process.hasOutput, output);
	}

	/* @see org.mindswap.owls.process.Process#addExistential(org.mindswap.owls.process.variable.Existential) */
	public void addExistential(final Existential existential)
	{
		addProperty(OWLS.Process.hasExistential, existential);
	}

	/* @see org.mindswap.owls.process.Process#addParticipant(org.mindswap.owls.process.variable.Participant) */
	public void addParticipant(final Participant participant)
	{
		addProperty(OWLS.Process.hasParticipant, participant);
	}

	/* @see org.mindswap.owls.process.Process#createExistential(java.net.URI, org.mindswap.owl.OWLType) */
	public Existential createExistential(final URI uri, final OWLType varType)
	{
		final Existential exist = getOntology().createExistential(uri);
		if (varType != null) exist.setParamType(varType);
		addExistential(exist);
		return exist;
	}

	/* @see org.mindswap.owls.process.Process#createParticipant(java.net.URI, org.mindswap.owl.OWLType) */
	public Participant createParticipant(final URI uri, final OWLType varType)
	{
		final Participant parti = getOntology().createParticipant(uri);
		if (varType != null) parti.setParamType(varType);
		addParticipant(parti);
		return parti;
	}

	/* @see org.mindswap.owls.process.Process#getExistential() */
	public Existential getExistential()
	{
		return getPropertyAs(OWLS.Process.hasExistential, Existential.class);
	}

	/* @see org.mindswap.owls.process.Process#getExistentials() */
	public OWLIndividualList<Existential> getExistentials()
	{
		return getPropertiesAs(OWLS.Process.hasExistential, Existential.class);
	}

	/* @see org.mindswap.owls.process.Process#getParticipant() */
	public Participant getParticipant()
	{
		return getPropertyAs(OWLS.Process.hasParticipant, Participant.class);
	}

	/* @see org.mindswap.owls.process.Process#getParticipants() */
	public OWLIndividualList<Participant> getParticipants()
	{
		return getPropertiesAs(OWLS.Process.hasParticipant, Participant.class);
	}

	/* @see org.mindswap.owls.process.Process#removeExistential(org.mindswap.owls.process.variable.Existential) */
	public void removeExistential(final Existential existential)
	{
		removeProperty(OWLS.Process.hasExistential, existential);
	}

	/* @see org.mindswap.owls.process.Process#removeParticipant(org.mindswap.owls.process.variable.Participant) */
	public void removeParticipant(final Participant participant)
	{
		removeProperty(OWLS.Process.hasParticipant, participant);
	}

	/* @see org.mindswap.owls.process.Process#prepare(org.mindswap.owls.process.execution.ExecutionContext) */
	public Process prepare(final ExecutionContext context)
	{
		process.doPrepare(context);
		return process;
	}

	protected String getName0()
	{
		Object o;
		if ((o = getName()) != null) return o.toString();
		if ((o = getLabel(null)) != null) return o.toString();
		if ((o = getURI()) != null) return o.toString();
		return "anon";
	}

	protected abstract void doPrepare(ExecutionContext context);

}
