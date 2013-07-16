/*
 * Created 21.05.2009
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
package impl.jena;

import impl.owls.expression.ExpressionConverter;
import impl.util.InternalFactory.IInternalFactory;

import org.mindswap.exceptions.NotImplementedException;
import org.mindswap.owl.OWLClass;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLObjectConverter;
import org.mindswap.owl.OWLValue;
import org.mindswap.owl.list.ListVocabulary;
import org.mindswap.owl.list.OWLList;
import org.mindswap.owls.expression.Condition;
import org.mindswap.owls.expression.Expression;
import org.mindswap.owls.expression.LogicLanguage;

/**
 *
 * @author unascribed
 * @version $Rev: 2298 $; $Author: nick $; $Date: 2011/01/24 06:37:51 $
 */
public class InternalFactoryImpl implements IInternalFactory
{
	/* @see impl.InternalFactory.IInternalFactory#createExpressionConverter(java.lang.Class, org.mindswap.owl.OWLClass, org.mindswap.owl.OWLClass, org.mindswap.owls.expression.LogicLanguage) */
	public <B, E extends Expression<B>> OWLObjectConverter<E> createExpressionConverter(
		final Class<E> abstraction, final OWLClass target, final OWLClass exprType, final LogicLanguage language)
	{
		if (Expression.SPARQL.class.equals(abstraction) || Condition.SPARQL.class.equals(abstraction))
		{
			Class<? extends E> impl = SPARQLExpressionImpl.class.asSubclass(abstraction);
			return new ExpressionConverter<B, E>(impl, target, exprType, language);
		}
		else if (Expression.SWRL.class.equals(abstraction) || Condition.SWRL.class.equals(abstraction))
		{
			Class<? extends E> impl = SWRLExpressionImpl.class.asSubclass(abstraction);
			return new ExpressionConverter<B, E>(impl, target, exprType, language);
		}
		throw new NotImplementedException(
			"Support for Expressions other than SWRL and SPARQL not yet implemented.");
	}

	/* @see impl.InternalFactory.IInternalFactory#wrapOWLList(org.mindswap.owl.OWLIndividual, org.mindswap.owl.list.ListVocabulary) */
	public <T extends OWLValue> OWLList<T> wrapOWLList(final OWLIndividual listInd,
		final ListVocabulary<T> listVocabulary)
	{
		return new OWLListImpl<T>(((OWLIndividualImpl) listInd).getOntology(),
			((OWLIndividualImpl) listInd).getImplementation(), listVocabulary);
	}

}
