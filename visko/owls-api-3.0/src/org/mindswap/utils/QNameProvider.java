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
 * Created on May 13, 2004
 */
package org.mindswap.utils;

import java.net.URI;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import org.mindswap.owl.vocabulary.OWL;
import org.mindswap.owl.vocabulary.RDF;
import org.mindswap.owl.vocabulary.RDFS;
import org.mindswap.owl.vocabulary.XSD;

/**
 * A class to convert URIs to QNames. Borrowed from SWOOP code.
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:53 $
 */
public class QNameProvider {

	// REFACTOR candidate for using a Bidi Map
	Map<String, String> uriToPrefix;
	Map<String, String> prefixToUri;

	/**
	 *
	 */
	public QNameProvider() {
		uriToPrefix = new Hashtable<String, String>();
		prefixToUri = new Hashtable<String, String>();

		// initialize it with standard stuff
		setMapping(OWL.class.getSimpleName().toLowerCase(), OWL.ns);
		setMapping(RDF.class.getSimpleName().toLowerCase(), RDF.ns);
		setMapping(RDFS.class.getSimpleName().toLowerCase(), RDFS.ns);
		setMapping(XSD.class.getSimpleName().toLowerCase(), XSD.ns);
	}

	/**
	 * @param ch
	 * @return
	 */
	public static boolean isNameStartChar(final char ch) {
		return (Character.isLetter(ch) || ch == '_');
	}

	/**
	 * @param ch
	 * @return
	 */
	public static boolean isNameChar(final char ch) {
		return (isNameStartChar(ch) || Character.isDigit(ch) || ch == '.' || ch == '-');
	}

	/**
	 * @param str
	 * @return
	 */
	public static int findNameStartIndex(final String str) {
		final char[] strChars = str.toCharArray();
		int nameStartIndex = -1;
		boolean foundNameChar = false;

		for(int strIndex = strChars.length - 1; strIndex >= 0; strIndex--) {
			final char letter = strChars[strIndex];

			if (isNameStartChar(letter)) {
				nameStartIndex = strIndex;
				foundNameChar = true;
			}
			else if (foundNameChar && !isNameChar(letter)) break;
		}
		return nameStartIndex;
	}

	/**
	 * @param str
	 * @return
	 */
	public static int findLastNameIndex(final String str) {
		final char[] strChars = str.toCharArray();
		int nameIndex = -1;

		for(int strIndex = strChars.length - 1; strIndex >= 0; strIndex--) {
			final char letter = strChars[strIndex];
			if (isNameChar(letter)) nameIndex = strIndex;
			else break;
		}
		return nameIndex;
	}

	public static int findNextNonNameIndex(final String str, final int startIndex) {
		final char[] strChars = str.toCharArray();
		int nameIndex = startIndex;
		for(nameIndex = startIndex; nameIndex < strChars.length; nameIndex++) {
			final char letter = strChars[nameIndex];
			if (!isNameChar(letter)) break;
		}
		return nameIndex;
	}

	/**
	 * @param uriString
	 * @return
	 */
	protected static String[] splitURI(final String uriString) {
		int nameStart, prefixStart, prefixEnd;
		String base, prefix, name;
		final String[] bpn = new String[3];

		nameStart = findLastNameIndex(uriString);
		if(nameStart < 0) return null;
		name = uriString.substring(nameStart);
		if(nameStart == 0) {
			base = "";
			prefix = "a"; // Pick a unique prefix later
		}
		else {
			base = uriString.substring(0, nameStart);
			prefixStart = findNameStartIndex(base);
			if(prefixStart < 0) {
				prefix = "b"; // Pick a unique prefix later
			}
			else {
				prefixEnd = findNextNonNameIndex(base, prefixStart + 1);
				prefix = uriString.substring(prefixStart, prefixEnd);
			}
		}
		bpn[0] = base;
		bpn[1] = prefix;
		bpn[2] = name;

		return bpn;
	}

	/**
	 * @param uri
	 * @return
	 */
	public String getPrefix(final String uri) {
		return uriToPrefix.get(uri);
	}

	/**
	 * @param prefix
	 * @return
	 */
	public String getURI(final String prefix) {
		return prefixToUri.get(prefix);
	}

	/**
	 * @param prefix
	 */
	public void removePrefix(final String prefix) {
		final String uri = prefixToUri.remove(prefix);
		if(uri != null)
			uriToPrefix.remove(uri);
	}

	/**
	 * @param prefix
	 * @param uri
	 * @return
	 */
	public boolean setMapping(final String prefix, final String uri) {
		final String currentUri = getURI(prefix);
		if (currentUri == null) {
			//System.out.println("Setting prefix "+prefix+": "+uri);
			prefixToUri.put(prefix, uri);
			uriToPrefix.put(uri, prefix);
			return true;
		}
		else if (currentUri.equals(uri)) {
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * @return
	 */
	public Set<String> getPrefixSet() {
		return prefixToUri.keySet();
	}

	/**
	 * @return
	 */
	public Set<String> getURISet() {
		return uriToPrefix.keySet();
	}

	/**
	 * @param uri
	 * @return
	 */
	public String shortForm(final URI uri) {
		return shortForm(uri.toString());
	}

	/**
	 * @param uri
	 * @return
	 */
	public String shortForm(final String uri) {
		//System.out.println("Shortform for " + uri);
		return shortForm(uri, true);
	}

	/**
	 * @param uri
	 * @param default_to_uri
	 * @return
	 */
	public String shortForm(final String uri, final boolean default_to_uri) {
		final String[] bpn = splitURI(uri);
		String base, possible_prefix, prefix, name;
		String qname;

		if(bpn == null) {
			if(default_to_uri) return uri;
			return null;
		}

		base = bpn[0];
		possible_prefix = bpn[1];
		name = bpn[2];

		if(possible_prefix.endsWith(".owl") || possible_prefix.endsWith(".rdf")
			|| possible_prefix.endsWith(".xml"))
			possible_prefix = possible_prefix.substring(0, possible_prefix
				.length() - 4);

		if(Character.isLowerCase(possible_prefix.charAt(1)))
			possible_prefix = Character.toLowerCase(possible_prefix.charAt(0))
			+ possible_prefix.substring(1, possible_prefix.length());

		prefix = getPrefix(base);
		if(prefix == null) {
			// Check prefix for uniqueness
			prefix = possible_prefix;
			int mod = 0;
			while(!setMapping(prefix, base)) {
				prefix = possible_prefix + mod;
				mod++;
			}
		}

		qname = prefix.length() == 0 ? name : prefix + ":" + name;
		return qname;
	}

	/**
	 * @param qname
	 * @return
	 */
	public String longForm(final String qname) {
		final String[] str = qname.split(":");
		final String prefix = str[0];
		final String localName = str[1];
		final String uri = getURI(prefix);
		if (uri == null) throw new IllegalArgumentException("Prefix " + prefix + " is not known!");
		return  uri + localName;
	}
}