/*
 * Re-created as top level class on 30.07.2008
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
package impl.owl;

import impl.util.InternalFactory;

import org.mindswap.exceptions.CastingException;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLObject;
import org.mindswap.owl.OWLObjectConverter;
import org.mindswap.owl.OWLValue;
import org.mindswap.owl.list.ListVocabulary;
import org.mindswap.owl.list.OWLList;

/**
 *
 * @author unascribed
 * @version $Rev: 2290 $; $Author: nick $; $Date: 2011/01/24 06:37:54 $
 */
public final class ListConverter<V extends OWLValue> implements OWLObjectConverter<OWLList<V>>
{
	private final ListVocabulary<V> vocabulary;

	/**
	 * @param vocabulary The target list vocabulary, i.e., OWL individuals will
	 * 	be checked according to the class returned by <code>vocabulary.List()</code>.
	 */
	public ListConverter(final ListVocabulary<V> vocabulary)
	{
		this.vocabulary = vocabulary;
	}

	/* @see org.mindswap.owl.OWLObjectConverter#canCast(org.mindswap.owl.OWLObject, boolean) */
	public boolean canCast(final OWLObject object, final boolean strictConversion)
	{
		if (object instanceof OWLIndividual)
		{
			final OWLIndividual ind = (OWLIndividual) object;

			return ind.equals(vocabulary.nil()) || ind.isType(vocabulary.list())
				|| null != isConvertibleList(ind) // support for (up)cast: list -to-> (more general) list type
				|| (ind.hasProperty(vocabulary.first()) && ind.hasProperty(vocabulary.rest()));
		}

		return false;
	}

	/* @see org.mindswap.owl.OWLObjectConverter#cast(org.mindswap.owl.OWLObject, boolean) */
	public OWLList<V> cast(final OWLObject object, final boolean strictConversion)
	{
		// support for list (up)cast: list -to-> (more general) list type
		final OWLList<?> list;
		if ((list = isConvertibleList(object)) != null)
		{
			ListVocabulary<V> lv = new ListVocabulary<V>(list.getVocabulary().list(), list.getVocabulary().first(),
				list.getVocabulary().rest(), list.getVocabulary().nil(), vocabulary.itemAbstraction());
			return InternalFactory.wrapOWLList(list, lv);
		}

		// standard conversion
		if (canCast(object, strictConversion))
		{
			final OWLIndividual listInd = (OWLIndividual) object;
			return InternalFactory.wrapOWLList(listInd, vocabulary);
		}
		throw CastingException.create(object, vocabulary.list());
	}

	/* @see java.lang.Object#toString() */
	@Override
	public String toString()
	{
		return "OWLIndividual -> OWLList (vocabulary " + vocabulary.list() + ", item abstraction " +
			vocabulary.itemAbstraction() + ")";
	}

	private final OWLList<?> isConvertibleList(final OWLObject object)
	{
		if (object instanceof OWLList<?>)
		{
			final OWLList<?> list = (OWLList<?>) object;
			if (vocabulary.itemAbstraction().isAssignableFrom(list.getVocabulary().itemAbstraction()))
				return list;
		}
		return null;
	}
}