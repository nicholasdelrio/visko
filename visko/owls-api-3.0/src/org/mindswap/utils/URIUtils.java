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
 * Created on Dec 27, 2003
 */
package org.mindswap.utils;

import java.net.URI;
import java.net.URISyntaxException;

import org.mindswap.exceptions.InvalidURIException;

/**
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:53 $
 */
public class URIUtils {
	public static String getLocalName( final URI uri ) {
		return getLocalName( uri.toString() );
	}

	public static String getLocalName( final String uri ) {
		int index = splitPos( uri );

		if( index == -1 ) return null;

		return uri.substring( index + 1 );
	}

	public static String getNameSpace( final URI uri ) {
		return getNameSpace( uri.toString() );
	}

	public static String getNameSpace( final String uri ) {
		int index = splitPos( uri );

		if( index == -1 ) return null;

		return uri.substring( 0, index + 1 );
	}

	private static int splitPos( final String uri ) {
		int pos = uri.indexOf( "#" );

		if( pos == -1 ) pos = QNameProvider.findLastNameIndex( uri ) - 1;

		return pos;
	}

	public static URI createURI( final String uri ) throws InvalidURIException {
		try {
			return new URI( uri.replaceAll( " ", "%20" ) );
		}
		catch(URISyntaxException e) {
			throw new InvalidURIException(e);
		}
	}

	/**
	 * Create a URI with the given base URI and fragment identifier, i.e., the
	 * resulting URI has the form <tt>baseURI#fragment</tt>.
	 *
	 * @param baseURI
	 * @param fragment
	 * @return A new URI according to the parameter values.
	 * @throws InvalidURIException In case the parameter values do not adhere to
	 * 	the syntactic structure required for URIs.
	 */
	public static URI createURI(final URI baseURI, final String fragment) throws InvalidURIException
	{
		try
		{
			return new URI(baseURI.getScheme(), baseURI.getSchemeSpecificPart(), fragment);
		}
		catch (URISyntaxException e)
		{
			throw new InvalidURIException(e);
		}
		catch (NullPointerException e)
		{
			throw new InvalidURIException(e);
		}
	}

	/**
	 * Create a URI with the given scheme specific part, local name, and fragment
	 * identifier.
	 *
	 * @param scheme The scheme for the URI, or <code>null</code> to be left
	 * 	undefined.
	 * @param ssp The scheme specific part for the URI, or <code>null</code> to
	 * 	be left undefined.
	 * @param fragment The fragment identifier, or <code>null</code> to be left
	 * 	undefined.
	 * @return A new URI according to the parameter values.
	 * @throws InvalidURIException In case the parameter values do not adhere to
	 * 	the syntactic structure required for URIs.
	 * @see URI#URI(String, String, String)
	 */
	public static URI createURI(final String scheme, final String ssp, final String fragment)
		throws InvalidURIException
	{
		try
		{
			return new URI(scheme, ssp, fragment);
		}
		catch (URISyntaxException e)
		{
			throw new InvalidURIException(e);
		}
	}

	/**
	 * Removes the fragment identifier from the given URI an returns it. If the
	 * given URI does not have a fragment the same instance is returned.
	 *
	 * @param uri The source URI.
	 * @return The source URI without the fragment identifier (if any).
	 * @throws InvalidURIException In case the parameter value does not adhere to
	 * 	the syntactic structure required for URIs.
	 */
	public static URI standardURI(URI uri) throws InvalidURIException
	{
		if (uri.getFragment() != null)
		{
			try
			{
				uri = new URI(uri.getScheme(), uri.getSchemeSpecificPart(), null);
			}
			catch (URISyntaxException e)
			{
				throw new InvalidURIException(e);
			}
			catch (NullPointerException e)
			{
				throw new InvalidURIException(e);
			}
		}

		return uri;
	}

	/**
	 * Removes the fragment identifier from the given URI string and returns it.
	 * If the given URI does not have a fragment it will parsed to an URI as is.
	 *
	 * @param uri The source URI string.
	 * @return The source URI without the fragment identifier (if any).
	 * @throws InvalidURIException In case the parameter value does not adhere to
	 * 	the syntactic structure required for URIs.
	 */
	public static URI standardURI(final String uri) throws InvalidURIException
	{
		int index = uri.indexOf("#");
		return createURI((index > 0)? uri.substring(0, index) : uri);
	}

	public static boolean relaxedMatch( final String uri1, final String uri2 ) {
		if( uri1 == null || uri2 == null ) return false;

		if( uri1.equals( uri2 ) ) return true;

		String name1 = getLocalName( uri1 );
		String name2 = getLocalName( uri2 );

		if( name1 == null || name2 == null ) return false;

		return name1.equals( name2 );
	}

	/**
	 * @return <code>true</code> if the given string conforms with the
	 * 	syntax rules of URI.
	 */
	public static boolean isValidURI(final String uri) {
		try {
			URI.create(uri);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
