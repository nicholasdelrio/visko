/*
 * Created 02.07.2009
 *
 * (c) 2009 Thorsten Möller - University of Basel Switzerland
 *
 * The MIT License
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 */
package impl.owls.expression;

import org.mindswap.owl.OWLDataValue;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owls.expression.LogicLanguage;
import org.mindswap.owls.expression.VariableBinding;
import org.mindswap.owls.expression.Expression.QuotedExpression;
import org.mindswap.owls.vocabulary.OWLS;

/**
 * This implementation can be used to subclass it for quoted expressions.
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:54 $
 */
public abstract class QuotedExpressionImpl<B> extends ExpressionImpl<B> implements QuotedExpression<B>
{

	public QuotedExpressionImpl(final OWLIndividual ind, final LogicLanguage lang, final boolean setLanguage)
	{
		super(ind, lang, setLanguage);
	}

	/* @see org.mindswap.owls.expression.Expression.QuotedExpression#addVariableBinding(org.mindswap.owls.expression.VariableBinding) */
	public void addVariableBinding(final VariableBinding varBinding)
	{
		addProperty(OWLS.Expression.variableBinding, varBinding);
	}

	/* @see org.mindswap.owls.expression.Expression.QuotedExpression#getVariableBinding() */
	public VariableBinding getVariableBinding()
	{
		return getPropertyAs(OWLS.Expression.variableBinding, VariableBinding.class);
	}

	/* @see org.mindswap.owls.expression.Expression.QuotedExpression#getVariableBinding(java.lang.String) */
	public VariableBinding getVariableBinding(final String localName)
	{
		OWLIndividualList<VariableBinding> varBindings = getVariableBindings();
		for (VariableBinding varBinding : varBindings)
		{
			if (varBinding.getTheObject().getLocalName().equals(localName)) return varBinding;
		}
		return null;
	}

	/* @see org.mindswap.owls.expression.Expression.QuotedExpression#getVariableBindings() */
	public OWLIndividualList<VariableBinding> getVariableBindings()
	{
		return getPropertiesAs(OWLS.Expression.variableBinding, VariableBinding.class);
	}

	/* @see org.mindswap.owls.expression.Expression.QuotedExpression#removeVariableBinding(org.mindswap.owls.expression.VariableBinding) */
	public void removeVariableBinding(final VariableBinding varBinding)
	{
		removeProperty(OWLS.Expression.variableBinding, varBinding);
	}

	/* @see org.mindswap.owls.expression.Expression.QuotedExpression#removeVariableBindings() */
	public void removeVariableBindings()
	{
		removeProperty(OWLS.Expression.variableBinding, null);
	}

	protected final OWLDataValue getQuotedBody()
	{
		OWLDataValue body = getProperty(OWLS.Expression.expressionData);
		return (body != null)? body : getProperty(OWLS.Expression.expressionBody); // for backwards compatibility
	}

	protected final void setQuotedBody(final String expression)
	{
		// We want to use expressionData property, thus, we need to make sure that any
		// existing expressionBody property is removed, in order not to have it remaining
		// from an earlier state of this expression where it was used.
		if (hasProperty(OWLS.Expression.expressionBody)) removeProperty(OWLS.Expression.expressionBody, null);
		setProperty(OWLS.Expression.expressionData, expression);
	}

	protected final void setQuotedBody(final OWLDataValue expression)
	{
		// We want to use expressionData property, thus, we need to make sure that any
		// existing expressionBody property is removed, in order not to have it remaining
		// from an earlier state of this expression where it was used.
		if (hasProperty(OWLS.Expression.expressionBody)) removeProperty(OWLS.Expression.expressionBody, null);
		setProperty(OWLS.Expression.expressionData, expression);
	}
}
