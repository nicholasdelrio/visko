/*
 * Made top level class on 05.07.2009
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

import impl.owl.GenericOWLConverter;

import org.mindswap.owl.OWLClass;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLObject;
import org.mindswap.owls.expression.Expression;
import org.mindswap.owls.expression.LogicLanguage;
import org.mindswap.owls.vocabulary.OWLS;

/**
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:54 $
 * @param <B> The body of the expression.
 * @param <E> The particular kind of expression.
 */
public final class ExpressionConverter<B, E extends Expression<B>> extends GenericOWLConverter<E>
{
	final OWLClass exprType;
	final LogicLanguage language;

	public ExpressionConverter(final Class<? extends E> impl, final OWLClass target, final OWLClass exprType,
		final LogicLanguage language)
	{
		super(impl, target);
		this.exprType = exprType;
		this.language = language;
	}

	/* @see impl.owl.GenericOWLConverter#canCast(org.mindswap.owl.OWLObject, boolean) */
	@Override
	public boolean canCast(final OWLObject object, final boolean strictConversion)
	{
		if (object instanceof OWLIndividual)
		{
			final OWLIndividual ind = (OWLIndividual) object;

			if (strictConversion)
			{
				if (ind.equals(OWLS.Expression.AlwaysTrue)) return true;

				return ind.isType(getTarget()) ||
					(ind.isType(exprType) && ind.hasProperty(OWLS.Expression.expressionLanguage, language));

			}

			return true; // sufficient criteria for non-strict conversion is that object is an instanceof OWLIndinvidual
		}

		return false;
	}
}