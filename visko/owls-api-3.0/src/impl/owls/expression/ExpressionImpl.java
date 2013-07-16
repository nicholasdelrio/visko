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
 * Created on Jul 7, 2004
 */
package impl.owls.expression;

import impl.owl.WrappedIndividual;

import org.mindswap.owl.OWLIndividual;
import org.mindswap.owls.expression.Condition;
import org.mindswap.owls.expression.Expression;
import org.mindswap.owls.expression.LogicLanguage;
import org.mindswap.owls.vocabulary.OWLS;

/**
 * This implementation can be used to subclass it for using it for both quoted
 * as well as unquoted expressions.
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:54 $
 */
public abstract class ExpressionImpl<B> extends WrappedIndividual implements Expression<B>
{
	/**
	 * Wrap an existing individual as an expression object. This constructor is
	 * intended to be used ONLY if it is asserted that the given individual can
	 * be viewed as an {@link Expression} or a {@link Condition}.
	 *
	 * @param ind The individual to wrap.
	 * @param lang The logic language this new expression represents.
	 * @param setLanguage <code>false</code> if the backing ontology already
	 * 	contains a statement that asserts the language for the given individual,
	 * 	i.e., whether the property {@link OWLS.Expression#expressionLanguage}
	 * 	is already set.
	 */
	public ExpressionImpl(final OWLIndividual ind, final LogicLanguage lang, final boolean setLanguage)
	{
		super(ind);
		if (setLanguage) setProperty(OWLS.Expression.expressionLanguage, lang);
	}

	/* @see impl.owl.WrappedIndividual#toPrettyString() */
	@Override
	public String toPrettyString()
	{
		final String string = toString();
		return (string == null || string.length() == 0)? super.toPrettyString() : string;
	}

	protected final OWLIndividual getUnquotedBody()
	{
		return getProperty(OWLS.Expression.expressionObject);
	}

	protected final void setUnquotedBody(final OWLIndividual expression)
	{
		setProperty(OWLS.Expression.expressionObject, expression);
	}
}
