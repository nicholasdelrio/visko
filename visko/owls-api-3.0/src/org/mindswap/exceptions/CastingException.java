/*
 * Created on Dec 12, 2004
 */
package org.mindswap.exceptions;

import org.mindswap.owl.OWLObject;
import org.mindswap.owl.OWLType;

/**
 * Used to indicate that conversion of some OWL object instance to another
 * OWL type was not possible.
 * <p>
 * This exception can be thrown by {@link org.mindswap.owl.OWLObjectConverter}.
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:52 $
 */
public class CastingException extends RuntimeException
{
	private static final long serialVersionUID = 7939752388079955235L;

	/**
	 * @see RuntimeException#RuntimeException()
	 */
	public CastingException()
	{
		super();
	}

	/**
	 * @see RuntimeException#RuntimeException(String)
	 */
	public CastingException(final String message)
	{
		super(message);
	}

	/**
	 * Create a conversion exception indicating that the source object cannot
	 * be viewed as an instance of the target type.
	 *
	 * @param source The source object instance whose conversion was impossible.
	 * @param target The target type.
	 * @return A new casting exception.
	 */
	public static CastingException create(final OWLObject source, final Class<? extends OWLObject> target)
	{
		return new CastingException(
			String.format("Instance of %s %s cannot be viewed as %s.", source.getClass(), source, target));
	}

	/**
	 * Create a conversion exception indicating that the source object cannot
	 * be viewed as an instance of the target type.
	 *
	 * @param source The source object instance whose conversion was impossible.
	 * @param target The target type.
	 * @return A new casting exception.
	 */
	public static CastingException create(final OWLObject source, final OWLType target)
	{
		return new CastingException(
			String.format("Instance of %s %s cannot be viewed as %s.", source.getClass(), source, target));
	}
}
