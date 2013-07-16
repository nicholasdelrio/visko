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
 * Re-created as top-level class on 30.07.2008
 */
package impl.owl;

import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.Set;

import org.mindswap.exceptions.CastingException;
import org.mindswap.owl.OWLClass;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLObject;
import org.mindswap.owl.OWLObjectConverter;

/**
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:54 $
 */
public class GenericOWLConverter<T extends OWLObject> implements OWLObjectConverter<T>
{
	private final OWLClass target;
	private final Set<T> individuals;
	private final Constructor<? extends T> constructor;

	/* Tests showed that there is almost no difference in terms of performance
	 * between the reflection based instantiation of objects as it is implemented
	 * here and a implementation that uses "new" to create instances. We used to
	 * have an implementation of this class for the latter variant, which was not
	 * considerably faster. In the end we decided to use the reflection based approach
	 * since the "new" based variant requires to introduce (anonymous) subclasses
	 * for every particular converter, roughly as follows:
	 *
	 * GenericOWLConverter<Input> inputConv = new GenericOWLConverter(OWLS.Process.Input) {
	 * 	public Input cast(OWLObject obj)
	 * 	{
	 * 		if (canCast) return new InputImpl(obj);
	 * 	}
	 * };
	 *
	 * If Java had the notion of function pointers (closures) it could be done more
	 * conveniently.
	 */

	/**
	 * @param impl The implementation class of which an instance will be created
	 * 	when an object is casted. Consequently, the class <strong>must not</strong>
	 * 	be abstract nor an interface.
	 * @param target The OWL class that determines the target OWL type of casted
	 * 	objects, i.e., every object resulting from a cast is of this type.
	 * @throws RuntimeException If <code>impl</code> does not implement a
	 * 	single-argument constructor with parameter type {@link OWLIndividual}.
	 */
	public GenericOWLConverter(final Class<? extends T> impl, final OWLClass target)
	{
		this(impl, target, Collections.<T>emptySet());
	}

	/**
	 * @param impl The implementation class of which an instance will be created
	 * 	when an object is casted. Consequently, the class <strong>must not</strong>
	 * 	be abstract nor an interface.
	 * @param target The OWL class that determines the target OWL type of casted
	 * 	objects, i.e., every object resulting from a cast is of this type.
	 * @param individuals A set of individuals. If an OWL object subject to be
	 * 	casted equals one of the individuals in this set it is considered
	 * 	castable. Set may not be <code>null</code>.
	 * @throws RuntimeException If <code>impl</code> does not implement a
	 * 	single-argument constructor with parameter type {@link OWLIndividual}.
	 */
	public GenericOWLConverter(final Class<? extends T> impl, final OWLClass target, final Set<T> individuals)
	{
		this.target = target;
		this.individuals = individuals;

		try
		{
			constructor = impl.getConstructor(new Class[] {OWLIndividual.class});
		}
		catch (final Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	/* @see org.mindswap.owl.OWLObjectConverter#canCast(org.mindswap.owl.OWLObject) */
	public boolean canCast(final OWLObject object, final boolean strictConversion)
	{
		if (object instanceof OWLIndividual)
		{
			OWLIndividual ind = (OWLIndividual) object;
			if (!strictConversion || ind.isType(target) || individuals.contains(object))
			{
				return true;
			}
//			System.out.println(ind.getOntology() + " --- " + ind.getTypes());
		}
		return false;
	}

	/* @see org.mindswap.owl.OWLObjectConverter#cast(org.mindswap.owl.OWLObject) */
	public final T cast(final OWLObject object, final boolean strictConversion)
	{
		if (canCast(object, strictConversion))
		{
			try
			{
				return constructor.newInstance(new Object[] {(OWLIndividual) object});
			}
			catch (final Exception e)
			{
				/* fall through throwing exception */
			}
		}
		throw CastingException.create(object, target);
	}

	/* @see java.lang.Object#toString() */
	@Override
	public String toString()
	{
		return "OWL individual -> " + target + " (implemented by " + constructor.getDeclaringClass() + ")";
	}

	protected final Constructor<? extends T> getConstructor()
	{
		return constructor;
	}

	protected final OWLClass getTarget()
	{
		return target;
	}
}