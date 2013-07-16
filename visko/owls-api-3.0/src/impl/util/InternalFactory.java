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
package impl.util;

import org.mindswap.common.ServiceFinder;
import org.mindswap.owl.OWLClass;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLObjectConverter;
import org.mindswap.owl.OWLValue;
import org.mindswap.owl.list.ListVocabulary;
import org.mindswap.owl.list.OWLList;
import org.mindswap.owls.expression.Expression;
import org.mindswap.owls.expression.LogicLanguage;

/**
 * This class is only to be used by classes in this package or sub packages.
 * It belongs to the implementation specific part of the API and, thus, should
 * <strong>not</strong> be used by clients.
 *
 * @author unascribed
 * @version $Rev: 2298 $; $Author: nick $; $Date: 2011/01/24 06:37:55 $
 */
public abstract class InternalFactory
{
	private static final IInternalFactory factory = ServiceFinder.instance().loadImplementation(
		IInternalFactory.class, "impl.jena.InternalFactoryImpl");


	/**
	 * Adapter interface that needs to be implemented to realize the enclosing
	 * factory. (Indirection used to prevent dependencies to external libraries
	 * at this level. Implementations are free to introduce such dependencies.
	 */
	public static interface IInternalFactory
	{
		public <B, E extends Expression<B>> OWLObjectConverter<E> createExpressionConverter(
			final Class<E> abstraction, final OWLClass target, final OWLClass exprType, final LogicLanguage language);

		public <T extends OWLValue> OWLList<T> wrapOWLList(OWLIndividual listInd, ListVocabulary<T> listVocabulary);
	}

	public static final <B, E extends Expression<B>> OWLObjectConverter<E> createExpressionConverter(
		final Class<E> abstraction, final OWLClass target, final OWLClass exprType, final LogicLanguage language)
	{
		return factory.createExpressionConverter(abstraction, target, exprType, language);
	}

	/**
	 * Factory method to make a given individual (of which it is known that it
	 * adheres to the given list vocabulary) a OWLList.
	 */
	public static final <T extends OWLValue> OWLList<T> wrapOWLList(
		final OWLIndividual listInd, final ListVocabulary<T> listVocabulary)
	{
		return factory.wrapOWLList(listInd, listVocabulary);
	}
}
