/*
 * Created on Dec 13, 2004
 */
package org.mindswap.exceptions;

/**
 * @author Evren Sirin
 */
public class NotImplementedException extends RuntimeException
{
	private static final long serialVersionUID = -5103063180927264823L;

	/**
	 * 
	 */
	public NotImplementedException()
	{
		super("Not implemented yet!");
	}

	public NotImplementedException(String msg)
	{
		super(msg);
	}
}
