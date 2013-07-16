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
 * Created on Dec 30, 2004
 */
package impl.owls.process.binding;

import java.util.Map;

import org.mindswap.owl.OWLDataValue;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLType;
import org.mindswap.owl.OWLValue;
import org.mindswap.owls.process.Perform;
import org.mindswap.owls.process.variable.Binding;
import org.mindswap.owls.process.variable.ParameterValueVisitor;
import org.mindswap.owls.process.variable.ProcessVar;
import org.mindswap.owls.process.variable.ValueConstant;
import org.mindswap.owls.vocabulary.OWLS;
import org.mindswap.query.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:53 $
 */
public class ValueConstantImpl implements ValueConstant
{
	private static final Logger logger = LoggerFactory.getLogger(ValueConstantImpl.class);

	// dataValue and individualValue are mutually exclusive: if one is null the other is not null
	private OWLDataValue dataValue;
	private OWLIndividual individualValue;

	private final Binding<?> enclosingBinding;

	public ValueConstantImpl(final OWLValue constantValue, final Binding<?> enclosingBinding)
	{
		super();
		this.enclosingBinding = enclosingBinding;

		if (constantValue == null) throw new IllegalArgumentException("Value is null for ValueConstant!");

		final ProcessVar param = enclosingBinding.getProcessVar();
		final OWLType paramType = (param == null) ? null : param.getParamType();

		if (constantValue.isIndividual()) // value is an OWLIndividual --> assign it
		{
			if (paramType != null && paramType.isDataType()) throw new IllegalArgumentException(
				"Can not bind constant individual to process variable that is typed to datatype.");

			this.dataValue = null;
			this.individualValue = (OWLIndividual) constantValue;
		}
		else // value is an OWLDataValue
		{
			if (paramType != null && paramType.isClass()) throw new IllegalArgumentException(
				"Can not bind constant data value to process variable that is typed to OWL class.");

			this.dataValue = (OWLDataValue) constantValue;
			this.individualValue = null;
		}
	}

	/* @see org.mindswap.common.Visitable#accept(org.mindswap.common.Visitor) */
	public void accept(final ParameterValueVisitor visitor)
	{
		visitor.visit(this);
	}

	/* @see org.mindswap.owls.process.variable.ValueConstant#getData() */
	public OWLDataValue getData()
	{
		return dataValue;
	}

	/* @see org.mindswap.owls.process.variable.ValueConstant#getIndividual() */
	public OWLIndividual getIndividual()
	{
		return individualValue;
	}

	/* @see org.mindswap.owl.OWLObject#getImplementation() */
	public Object getImplementation()
	{
		return (dataValue != null? dataValue : individualValue).getImplementation();
	}

	/* @see org.mindswap.owls.process.variable.ParameterValue#getEnclosingBinding() */
	public Binding<?> getEnclosingBinding()
	{
		return enclosingBinding;
	}

	/* @see org.mindswap.owls.process.variable.ParameterValue#setToBinding(org.mindswap.owls.process.variable.Binding) */
	public void setToBinding(final Binding<?> binding)
	{
		final ProcessVar param = binding.getProcessVar();
		final OWLType paramType = (param == null) ? null : param.getParamType();
		if (dataValue != null)
		{
			if (paramType != null && paramType.isClass())
				logger.warn("Binding conflict in {}: Do not bind constant data value to process variable {} typed to OWL class.",
					binding, param);

			binding.setProperty(OWLS.Process.valueData, dataValue);
			binding.removeProperty(OWLS.Process.valueObject, null); // make sure that no artifacts remain
		}
		else
		{
			if (paramType != null && paramType.isDataType())
				logger.warn("Binding conflict in {}: Do not bind constant individual to process variable {} typed to data type.",
					binding, param);

			binding.setProperty(OWLS.Process.valueObject, individualValue);
			binding.removeProperty(OWLS.Process.valueData, null); // make sure that no artifacts remain
		}
	}

	/* @see org.mindswap.owls.process.variable.ParameterValue#getValueFromPerformResults(java.util.Map) */
	public OWLValue getValueFromPerformResults(final Map<Perform, ValueMap<ProcessVar, OWLValue>> performResults)
	{
		// since ValueConstant represents some constant data we do not have to search in performResults
		return dataValue != null? dataValue : individualValue;
	}

	/* @see org.mindswap.owls.process.variable.ValueConstant#isDataValue() */
	public boolean isDataValue()
	{
		return dataValue != null;
	}

	/* @see org.mindswap.owls.process.variable.ValueConstant#isIndividualValue() */
	public boolean isIndividualValue()
	{
		return individualValue != null;
	}

	/* @see impl.owl.OWLObjectImpl#toString() */
	@Override
	public String toString()
	{
		return ValueConstant.class.getSimpleName() + ": " + (dataValue != null? dataValue : individualValue);
	}

}
