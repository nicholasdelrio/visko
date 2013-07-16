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

import org.mindswap.exceptions.ParseException;
import org.mindswap.owl.OWLDataValue;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owls.process.variable.Binding;
import org.mindswap.owls.process.variable.ParameterValue;
import org.mindswap.owls.process.variable.ProcessVar;
import org.mindswap.owls.process.variable.ValueFunction;
import org.mindswap.owls.process.variable.ValueOf;
import org.mindswap.owls.vocabulary.OWLS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:53 $
 */
public abstract class BindingImpl<V extends ProcessVar> extends WrappedIndividual implements Binding<V>
{
	private static final Logger logger = LoggerFactory.getLogger(BindingImpl.class);

	public BindingImpl(final OWLIndividual ind)
	{
		super(ind);
	}

	/* @see org.mindswap.owls.process.Binding#getValue() */
	public ParameterValue getValue()
	{
		// first, try with valueSource property, i.e., binding uses ValueOf
		ParameterValue value = getPropertyAs(OWLS.Process.valueSource, ValueOf.class);
		if (value != null) return value;

		// second, try with valueData property, i.e., binding uses constant (XML) data
		OWLDataValue dataValue = getProperty(OWLS.Process.valueData);
		if (dataValue != null) return getKB().createValueConstant(dataValue, this);

		// third, try with valueObject property, i.e., binding refers to some individual
		OWLIndividual owlValue = getProperty(OWLS.Process.valueObject);
		if (owlValue != null) return getKB().createValueConstant(owlValue, this);

		// lastly, try with value function, i.e., binding uses value function
		value = getValueFunction();

		// TODO implement valueForm, valueType
//        if(value == null)
//            value = (ParameterValue) getProperty(OWLS.Process.valueForm);
//
//        if(value == null)
//            value = (ParameterValue) getPropertyAs(OWLS.Process.valueType, ParameterValue.class);
//

		return value;
	}

	/* @see org.mindswap.owls.process.Binding#isValueDataBinding() */
	public boolean isValueConstantBinding()
	{
		return hasProperty(OWLS.Process.valueData);
	}

	/* @see org.mindswap.owls.process.Binding#isValueFormBinding() */
	public boolean isValueFormBinding()
	{
		return hasProperty(OWLS.Process.valueForm);
	}

	/* @see org.mindswap.owls.process.Binding#isValueFunctionBinding() */
	public boolean isValueFunctionBinding()
	{
		return hasProperty(OWLS.Process.valueFunction);
	}

	/* @see org.mindswap.owls.process.Binding#isValueOfBinding() */
	public boolean isValueOfBinding()
	{
		return hasProperty(OWLS.Process.valueSource);
	}

	/* @see org.mindswap.owls.process.Binding#setValue(org.mindswap.owls.process.ParameterValue) */
	public void setValue(final ParameterValue paramValue)
	{
		if (paramValue == null) throw new NullPointerException("Parameter value in Binding is null!");
		paramValue.setToBinding(this);
	}

	/* @see impl.owl.WrappedIndividual#toString() */
	@Override
	public String toString()
	{
		return "Bound process variable: " + getProcessVar() + ", Value description: " + getValue();
	}

	protected V setProcessVariable(final V procVar)
	{
		if (procVar == null) throw new NullPointerException("Process variable to set for Binding is null!");

		V old = getProcessVar();
		if (old != null) removeProperty(OWLS.Process.toVar, old);
		setProperty(OWLS.Process.toVar, procVar);
		return old;
	}

	private ValueFunction<?> getValueFunction()
	{
		final OWLDataValue valueFunction = getProperty(OWLS.Process.valueFunction);
		final String functionLiteral = (valueFunction == null)? null : valueFunction.getLexicalValue();
		try
		{
			return new ValueFunctionParser(this).parse(functionLiteral);
		}
		catch (ParseException e)
		{
			logger.debug("Failed to parse value function. Details: {}", e.toString());
			return null;
		}
	}


}
