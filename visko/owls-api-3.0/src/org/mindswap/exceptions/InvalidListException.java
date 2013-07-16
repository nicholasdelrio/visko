/*
 * Created on Dec 21, 2004
 */
package org.mindswap.exceptions;

/**
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:52 $
 */
public class InvalidListException extends RuntimeException
{
	private static final long serialVersionUID = -1533732259442386944L;

	public InvalidListException()
	{
		super();
	}

	public InvalidListException(final String message)
	{
		super(message);
	}
}
