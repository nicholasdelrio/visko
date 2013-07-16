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
 * Created on Dec 12, 2004
 */
package impl.owl;

import org.mindswap.exceptions.CastingException;
import org.mindswap.owl.OWLEntity;
import org.mindswap.owl.OWLObject;
import org.mindswap.owl.OWLObjectConverter;
import org.mindswap.owl.OWLObjectConverterRegistry;
import org.mindswap.owl.OWLValue;
import org.mindswap.owl.list.ListVocabulary;
import org.mindswap.owl.list.OWLList;

/**
 *
 * @author unascribed
 * @version $Rev: 2291 $; $Author: nick $; $Date: 2011/01/24 06:37:54 $
 */
public abstract class OWLObjectImpl implements OWLObject
{
	private OWLObject next;

	public OWLObjectImpl() {
		next = this;
	}

	/* @see org.mindswap.owl.OWLObject#getNextView() */
	public final OWLObject getNextView() {
		return next;
	}

	/* @see org.mindswap.owl.OWLObject#setNextView(org.mindswap.owl.OWLObject) */
	public final void setNextView(final OWLObject nextView) {
		next = nextView;
	}

	private final void addView(final OWLObject newView) {
		newView.setNextView(next);
		next = newView;
	}

	private final <T extends OWLObject> T findView(final Class<T> javaClass) {
		OWLObject obj = this;
		do {
			if(javaClass.isInstance(obj)) return javaClass.cast(obj);

			obj = obj.getNextView();
		} while(obj != this);

		return null;
	}

	private final <T extends OWLValue> OWLList<T> findListView(final ListVocabulary<T> vocabulary)
	{
		OWLObject obj = this;
		do
		{
			if(obj instanceof OWLList<?>)
			{
				final OWLList<OWLValue> list = (OWLList<OWLValue>) obj;
				if (vocabulary.equals(list.getVocabulary()))
				{
					return list.specialize(vocabulary.itemAbstraction());
				}
			}
			obj = obj.getNextView();
		} while (obj != this);

		return null;
	}

	/* @see org.mindswap.owl.OWLObject#castTo(java.lang.Class) */
	public final <T extends OWLObject> T castTo(final Class<T> javaClass)
	{
		T view = findView(javaClass);

		if (view == null)
		{
			final OWLObjectConverter<T> converter = OWLObjectConverterRegistry.instance().getConverter(javaClass);
			if (converter == null) throw new CastingException("No converter found for " + javaClass);

			view = converter.cast(this, isStrictConversion());

			addView(view);
		}

		return view;
	}

	/* @see org.mindswap.owl.OWLObject#canCastTo(java.lang.Class) */
	public final boolean canCastTo(final Class<? extends OWLObject> javaClass)
	{
		final OWLObjectConverter<?> converter = OWLObjectConverterRegistry.instance().getConverter(javaClass);
		return (converter != null && converter.canCast(this, isStrictConversion()));
	}

	/* @see org.mindswap.owl.OWLObject#canCastToList(org.mindswap.owl.list.ListVocabulary) */
	public final boolean canCastToList(final ListVocabulary<? extends OWLValue> vocabulary)
	{
		final ListConverter<?> converter = OWLObjectConverterRegistry.instance().getListConverter(vocabulary);
		return (converter != null && converter.canCast(this, isStrictConversion()));
	}

	/* @see org.mindswap.owl.OWLObject#castToList(org.mindswap.owl.list.ListVocabulary) */
	public final <T extends OWLValue> OWLList<T> castToList(final ListVocabulary<T> vocabulary)
	{
		OWLList<T> view = findListView(vocabulary);
		if (view == null)
		{
			final ListConverter<T> converter = OWLObjectConverterRegistry.instance().getListConverter(vocabulary);
			if (converter == null)
				throw new CastingException("No list converter found for list item type" + vocabulary);

			view = converter.cast(this, isStrictConversion());

			addView(view);
		}
		return view;
	}

	/* @see java.lang.Object#toString() */
	@Override
	public String toString()
	{
		return getImplementation().toString();
	}

	/* @see java.lang.Object#equals(java.lang.Object) */
	@Override
	public boolean equals(final Object object)
	{
		if (object instanceof OWLObjectImpl)
		{
			return getImplementation().equals(((OWLObjectImpl)object).getImplementation());
		}
		return false;
	}

	/* @see java.lang.Object#hashCode() */
	@Override
	public int hashCode()
	{
		return getImplementation().hashCode();
	}

	private final boolean isStrictConversion()
	{
		if (this instanceof OWLEntity) return ((OWLEntity) this).getOntology().getKB().isStrictConversion();
		return true; // actually, it doesn't matter if true or false for non OWL entities
	}
}
