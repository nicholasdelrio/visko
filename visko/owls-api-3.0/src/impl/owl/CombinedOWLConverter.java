/*
 * Created on 28.07.2008
 *
 * (c) 2008 Thorsten Möller - University of Basel Switzerland
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

import java.util.List;

import org.mindswap.exceptions.CastingException;
import org.mindswap.owl.OWLObject;
import org.mindswap.owl.OWLObjectConverter;
import org.mindswap.owl.OWLObjectConverterRegistry;
import org.mindswap.utils.Utils;

/**
 *
 * @param <T> The target object type to which this converter can convert to,
 * 	and for which to register this converter, see also
 * 	{@link OWLObjectConverterRegistry#registerConverter(Class, OWLObjectConverter)}.
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:54 $
 */
public class CombinedOWLConverter<T extends OWLObject> implements OWLObjectConverter<T>
{
	// We need to prevent duplicated elements *AND* should also maintain order,
	// albeit the latter is rather an optimization. <-- Most frequently used
	// converters should come first because every cast will try the converters
	// in this list one after the other, beginning with the element at index 0.
	private final List<OWLObjectConverter<? extends T>> converters;
	private final Class<T> target;

	/**
	 * @param target The object type for which to register this converter. Usually,
	 * 	the class is either an interface or an abstract super type of the set of
	 * 	converters.
	 * @param converters The ordered set of converters to use/try to convert to
	 * 	the target object. The order of elements in the linked set determines
	 * 	the order in which the converters will be used, i.e., the first
	 * 	converter will be tried first, followed by the second one, and so on.
	 */
	public CombinedOWLConverter(final Class<T> target,
		final List<OWLObjectConverter<? extends T>> converters)
	{
		assert target != null && converters != null :
			"Illegal: Either Java class and/or converter list was null.";
		this.converters = converters;
		this.target = target;
	}

	/* @see org.mindswap.owl.OWLObjectConverter#canCast(org.mindswap.owl.OWLObject, boolean) */
	public boolean canCast(final OWLObject object, final boolean strictConversion)
	{
		for (final OWLObjectConverter<? extends T> converter : converters)
		{
			if (converter.canCast(object, true)) // true to find the "best" match, i.e., the closest type
			{
				return true;
			}
		}
		return false;
	}

	/* @see org.mindswap.owl.OWLObjectConverter#cast(org.mindswap.owl.OWLObject, boolean) */
	public T cast(final OWLObject object, final boolean strictConversion)
	{
		for (final OWLObjectConverter<? extends T> converter : converters)
		{
			if (converter.canCast(object, true)) // true to find the "best" match, i.e., the closest type
			{
				return converter.cast(object, true);
			}
		}

		throw CastingException.create(object, target);
	}

	/**
	 * Adds the specified converter to this combined converter.
	 *
	 * @param converter The converter to be added.
	 * @return <code>true</code> if this converter did not already contain the
	 * 	specified converter.
	 */
	public boolean addConverter(final OWLObjectConverter<? extends T> converter)
	{
		if (converters.contains(converter)) return false;
		return converters.add(converter);
	}

//	/**
//	 * Removes the specified converter from this combined converter.
//	 *
//	 * @param converter The converter to be removed.
//	 * @return <code>true</code> if this combined converter contained the
//	 * 	specified converter.
//	 */
//	public boolean removeConverter(final OWLObjectConverter<? extends T> converter)
//	{
//		return converters.remove(converter);
//	}

	/* @see java.lang.Object#toString() */
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("Combined converter -> ").append(target);
		sb.append(", consisting of:").append(Utils.LINE_SEPARATOR);
		for (OWLObjectConverter<? extends T> converter : converters)
		{
			sb.append(converter.toString()).append(Utils.LINE_SEPARATOR);
		}
		return sb.toString();
	}
}