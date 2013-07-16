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
 * Created on Aug 30, 2004
 */
package impl.owls.process.binding;

import impl.owl.WrappedIndividual;

import java.util.Map;

import org.mindswap.exceptions.CastingException;
import org.mindswap.exceptions.DataFlowException;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLValue;
import org.mindswap.owls.process.Perform;
import org.mindswap.owls.process.variable.Binding;
import org.mindswap.owls.process.variable.Loc;
import org.mindswap.owls.process.variable.Local;
import org.mindswap.owls.process.variable.Parameter;
import org.mindswap.owls.process.variable.ParameterValueVisitor;
import org.mindswap.owls.process.variable.ProcessVar;
import org.mindswap.owls.process.variable.ValueOf;
import org.mindswap.owls.vocabulary.OWLS;
import org.mindswap.query.ValueMap;

/**
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:53 $
 */
public class ValueOfImpl extends WrappedIndividual implements ValueOf
{
	public ValueOfImpl(final OWLIndividual ind) {
		super(ind);
	}

	/* @see org.mindswap.common.Visitable#accept(org.mindswap.common.Visitor) */
	public void accept(final ParameterValueVisitor visitor)
	{
		visitor.visit(this);
	}

	/* @see org.mindswap.owls.process.ValueOf#isLocal() */
	public boolean isLocal()
	{
		OWLIndividual ind = getProperty(OWLS.Process.theVar);
		return (ind != null && ind.canCastTo(Local.class));
	}

	/* @see org.mindswap.owls.process.ValueOf#isParameter() */
	public boolean isParameter()
	{
		OWLIndividual ind = getProperty(OWLS.Process.theVar);
		return (ind != null && ind.canCastTo(Parameter.class));
	}

	/* @see org.mindswap.owls.process.ValueOf#getPerform() */
	public Perform getPerform()
	{
		return getPropertyAs(OWLS.Process.fromProcess, Perform.class);
	}

	/* @see org.mindswap.owls.process.ValueOf#getLocal() */
	public Loc getLocal()
	{
		try
		{
			return getPropertyAs(OWLS.Process.theVar, Loc.class);
		}
		catch (CastingException e)
		{
			return null; // seems to be a Parameter
		}
	}

	/* @see org.mindswap.owls.process.ValueOf#getParameter() */
	public Parameter getParameter()
	{
		try
		{
			Parameter param = getPropertyAs(OWLS.Process.theVar, Parameter.class);
			// try alternative property if preferred property is not used
			if (param == null) param = getPropertyAs(OWLS.Process.theParam, Parameter.class);
			return param;
		}
		catch (CastingException e)
		{
			return null; // seems to be a Local
		}
	}

	/* @see org.mindswap.owls.process.variable.ValueOf#getTheVar() */
	public ProcessVar getTheVar()
	{
		ProcessVar theVar = getPropertyAs(OWLS.Process.theVar, ProcessVar.class);
		// try alternative property if preferred property is not used (note that
		// range of theParam is Parameter)
		if (theVar == null) theVar = getPropertyAs(OWLS.Process.theParam, Parameter.class);
		return theVar;
	}

	/* @see org.mindswap.owls.process.variable.ValueOf#setSource(org.mindswap.owls.process.variable.Loc) */
	public void setSource(final Loc local)
	{
		setProperty(OWLS.Process.theVar, local);
		setProperty(OWLS.Process.fromProcess, OWLS.Process.ThisPerform);
		removeProperty(OWLS.Process.theParam, null); // remove previously existing alternative property
	}

	/* @see org.mindswap.owls.process.variable.ValueOf#setSource(org.mindswap.owls.process.variable.Parameter, org.mindswap.owls.process.Perform) */
	public void setSource(final Parameter param, final Perform perform)
	{
		setProperty(OWLS.Process.theVar, param);
		setProperty(OWLS.Process.fromProcess, perform);
		removeProperty(OWLS.Process.theParam, null); // remove previously existing alternative property
	}

	/* @see org.mindswap.owls.process.ParameterValue#getEnclosingBinding() */
	public Binding<?> getEnclosingBinding()
	{
		final OWLIndividual uncastedBinding = getIncomingProperty(OWLS.Process.valueSource);
		Binding<?> binding = null;
		if (uncastedBinding.isType(OWLS.Process.InputBinding))
			binding = new InputBindingImpl(uncastedBinding);
		else if (uncastedBinding.isType(OWLS.Process.OutputBinding))
			binding = new OutputBindingImpl(uncastedBinding);

		return binding;
	}

	/* @see org.mindswap.owls.process.ParameterValue#getValueFromPerformResults(java.util.Map) */
	public OWLValue getValueFromPerformResults(final Map<Perform, ValueMap<ProcessVar, OWLValue>> performsResults)
		throws DataFlowException
	{
		final Perform perform = getPerform();
		final ValueMap<ProcessVar, OWLValue> performResult = performsResults.get(perform);
		if (performResult == null) throw new DataFlowException("Results of perform " + perform +
			" cannot be found! This may be caused by an invalid data flow specification (ValueOf) in " +
			getEnclosingBinding());

		final ProcessVar theVar = getTheVar();
		final OWLValue value = performResult.getValue(theVar);
		if (value != null) return value;

		throw new DataFlowException(
			"Value for process variable " + theVar + " cannot be found in results of perform " + perform +
			"! This may be caused by an invalid data flow specification (ValueOf) in " +
			getEnclosingBinding());
	}

	/* @see impl.owl.OWLObjectImpl#equals(java.lang.Object) */
	@Override
	public boolean equals(final Object object)
	{
		if (object instanceof ValueOf)
		{
			final ValueOf toCompare = (ValueOf) object;
			return getTheVar().equals(toCompare.getTheVar()) &&
				getPerform().equals(toCompare.getPerform());
		}
		return false;
	}

	/* @see org.mindswap.owls.process.ParameterValue#setToBinding(org.mindswap.owls.process.Binding) */
	public void setToBinding(final Binding<?> binding)
	{
		binding.setProperty(OWLS.Process.valueSource, this);
	}

	/* @see impl.owl.OWLObjectImpl#hashCode() */
	@Override
	public int hashCode()
	{
		assert false : "hashCode not designed"; // TODO implement if required
		return 42; // any arbitrary constant will do
	}

}
