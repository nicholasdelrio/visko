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
 * Created on Jan 15, 2004
 */
package org.mindswap.owl;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.mindswap.owls.vocabulary.OWLS_1_2;

/**
 * This class is thread safe. More precisely, all public methods support
 * concurrent invocation (i.e. they are atomic) but do <em>not</em> offer any
 * isolation properties beyond that regarding parameter or return objects.
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:52 $
 */
public class OWLConfig
{
	/**
	 * Default language is used to determine which literal will be chosen by the
	 * getProperty(OWLDataProperty) method when there are multiple triples for
	 * the given property. An empty string means the literals with no language
	 * identifier will be chosen. A null value means the first found literal will
	 * be returned without looking at the language identifier. Note that even
	 * when a default language is chosen getProperty(prop, lang) can be used to
	 * get literal value for a certain identifier.
	 * <p>
	 * All the language identifiers in the array will be tried in the order given.
	 * Default elements in the array are
	 *     <pre>DEFAULT_LANGS = {"en", "", "en", null};</pre>
	 *
	 * The order of this array is associated with the following behavior:
	 * <ol>
	 *   <li>Try the default language, which is English (en) initially, and can be
	 *   changed any time using {@link #setDefaultLanguage(String)}.</li>
	 *   <li>Try to find literal with no language identifier.</li>
	 *   <li>Try English literal.</li>
	 *   <li>Try literal with any language identifier.</li>
	 * </ol>
	 * <p>
	 * See <a href="http://www.w3.org/TR/REC-xml#sec-lang-tag">language identifier
	 * specs</a>.
	 */
	public static final String[] DEFAULT_LANGS = {"en", "", "en", null};

	// read access vastly outnumbers write access
	private static final List<String> defaultLanguages = new CopyOnWriteArrayList<String>(Arrays.asList(DEFAULT_LANGS));

	/**
	 * Sets the default language that will be tried first when a literal property
	 * value is searched. Note that this function only sets the first language
	 * but there can be multiple languages defined, see {@link #getDefaultLanguages()}.
	 *
	 * @param lang
	 */
	public static final void setDefaultLanguage(final String lang)
	{
		defaultLanguages.set(0, lang);
	}

	/**
	 * Returns default languages that will be tried when a literal property value
	 * is searched. All the languages in the list will be tried in the order given.
	 * The initial list, if not modified, contains all elements from
	 * {@link #DEFAULT_LANGS}.
	 * <p>
	 * Invokers	may also extend or modify the returned list in order to alter
	 * default languages. Notice, however, that this has a global impact:
	 * <em>all</em> other object instances that depend on this list will then use
	 * the updated list!
	 * <p>
	 * Iterating by means of an {@link Iterator} over the returned list does not
	 * need synchronization. The iterator provides a snapshot of the state of the
	 * list when the iterator was constructed. Also, read and write operations do
	 * not need synchronization since the backing operations are atomic.
	 *
	 * @return The list of default languages
	 */
	public static final List<String> getDefaultLanguages()
	{
		return defaultLanguages;
	}

	/**
	 * @return The implementation version read from the JAR manifest file, i.e.,
	 * 	it is up to the build system that the version information is actually
	 * 	available and that it is properly maintained.
	 */
	public static final String apiVersion()
	{
		return OWLConfig.class.getPackage().getImplementationVersion();
	}

	/**
	 * @return The OWL-S version which is used by default.
	 */
	public static final String owlsVersion()
	{
		return OWLS_1_2.version;
	}
}
