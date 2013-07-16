/*
 * Created 05.07.2009
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

import impl.owl.WrappedIndividual;

import org.mindswap.owl.OWLIndividual;
import org.mindswap.owls.expression.VariableBinding;
import org.mindswap.owls.expression.Expression.QuotedExpression;
import org.mindswap.owls.process.variable.ProcessVar;
import org.mindswap.owls.vocabulary.OWLS;

/**
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:54 $
 */
public class VariableBindingImpl extends WrappedIndividual implements VariableBinding
{

	public VariableBindingImpl(final OWLIndividual ind)
	{
		super(ind);
	}

	/* @see org.mindswap.owls.expression.VariableBinding#getExpression() */
	public QuotedExpression<?> getExpression()
	{
		return getIncomingPropertyAs(OWLS.Expression.variableBinding, QuotedExpression.class);
	}

	/* @see org.mindswap.owls.expression.VariableBinding#getExpressionVariable() */
	public String getExpressionVariable()
	{
		return getPropertyAsString(OWLS.Expression.theVariable);
	}

	/* @see org.mindswap.owls.expression.VariableBinding#getTheObject() */
	public OWLIndividual getTheObject()
	{
		return getProperty(OWLS.Expression.theObject);
	}

	/* @see org.mindswap.owls.expression.VariableBinding#getTheObjectAsVar() */
	public ProcessVar getTheObjectAsVar()
	{
		return getPropertyAs(OWLS.Expression.theObject, ProcessVar.class);
	}

	/* @see org.mindswap.owls.expression.VariableBinding#setExpressionVariable(java.lang.String) */
	public void setExpressionVariable(final String variable)
	{
		setProperty(OWLS.Expression.theVariable, variable);
	}

	/* @see org.mindswap.owls.expression.VariableBinding#setTheObject(org.mindswap.owl.OWLIndividual) */
	public void setTheObject(final OWLIndividual theObject)
	{
		setProperty(OWLS.Expression.theObject, theObject);
	}

	/* @see impl.owl.WrappedIndividual#toString() */
	@Override
	public String toString()
	{
		return "Expression variable " + getExpressionVariable() + " <-> " + getTheObject();
	}

}
