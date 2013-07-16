/*
 * Created 22.03.2009
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
package org.mindswap.owl.vocabulary;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owl.OWLOntology;
import org.mindswap.utils.URIUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class can be extended by concrete OWL vocabulary declaration classes
 * and it realizes a TBox containing the union of all (lazily initialized)
 * vocabularies that extend this class.
 * <p>
 * Currently, this class is extended by the OWL-S vocabulary classes, plus some
 * extensions.
 *
 * @author unascribed
 * @version $Rev: 2320 $; $Author: nick $; $Date: 2011/01/24 06:37:53 $
 */
public abstract class Vocabulary
{
	private static final Logger logger = LoggerFactory.getLogger(Vocabulary.class);

	/** Contains all ontologies. Note that ontologies are lazily loaded, i.e., when used for the first time.*/
	private static final OWLKnowledgeBase kb = OWLFactory.createKB();

	/**
	 * This method simply delegates to {@link OWLKnowledgeBase#addAlternativeLocation(URI, URI)}
	 * of the internal KB. The given strings will be converted to standard URIs
	 * first, see {@link URIUtils#standardURI(String)}.
	 */
	protected static String addAlternativeLocation(final String original, final String copy)
	{
		URI origURI = URIUtils.standardURI(original);
		URI altnURI = URIUtils.standardURI(copy);
		return kb.addAlternativeLocation(origURI, altnURI).toString();
	}

	/**
	 * @param ontologyURI The URI of the ontology that is supposed to be in this
	 * 	knowledge base. (Note that it must have been loaded before,
	 * 	see {@link #loadOntology(String)}).
	 * @return The target ontology or <code>null</code> if it does not exist in
	 * 	this knowledge base.
	 */
	protected static OWLOntology getOntology(final URI ontologyURI)
	{
		return kb.getOntology(ontologyURI);
	}

	/**
	 * Utility method to be used by the various vocabulary definitions, i.e.,
	 * the sub classes. If the ontology to be loaded imports other ontologies
	 * they will be loaded as well, i.e., the method loads the ontology including
	 * its import closure (if any).
	 *
	 * @param ontologyURL The URL where the ontology can be loaded from.
	 * @return A reference to the loaded ontology.
	 * @throws InternalError In case the attempt to load the ontology failed
	 * 	for some I/O problem. Note that this "hard" exception is thrown
	 * 	because this represents a fatal error rendering the API unusable.
	 */
	protected static OWLOntology loadOntology(final String ontologyURL)
	{
		return loadOntology(ontologyURL, null);
	}

	/**
	 * Utility method to be used by the various vocabulary definitions, i.e.,
	 * the sub classes. If the ontology to be loaded imports other ontologies
	 * they will be loaded as well, i.e., the method loads the ontology including
	 * its import closure (if any).
	 * <p>
	 * The loaded ontology is in read-only state by default, i.e.,
	 * {@link OWLOntology#isReadOnly()} returns <code>true</code>.
	 *
	 * @param ontologyURL The URL where the ontology can be loaded from.
	 * @param local An alternative local copy of the ontology in the search path
	 * 	that will be tried after loading the ontology from its URL failed. Value
	 * 	may be <code>null</code> - skips attempt to try alternative.
	 * @return A reference to the loaded ontology. The ontology is read-only.
	 * @throws RuntimeException In case the attempt to load the ontology failed
	 * 	for some I/O problem. Note that this unchecked exception is thrown
	 * 	because this represents a fatal error rendering the API unusable.
	 */
	protected static OWLOntology loadOntology(final String ontologyURL, final String local)
	{
		logger.info("Loading ontology {} ...", ontologyURL);

		URI uri = URIUtils.standardURI(ontologyURL);
		OWLOntology loadedOnt;
		try
		{
			loadedOnt = kb.read(uri);
			loadedOnt.setReadOnly(true);
			return loadedOnt;
		}
		catch (final IOException ioe)
		{
			String details = ioe.toString();
			if (local != null)
			{
				InputStream in = ClassLoader.getSystemResourceAsStream(local);
				try
				{
					loadedOnt = kb.read(in, uri);
					loadedOnt.setReadOnly(true);
					return loadedOnt;
				}
				catch (IOException iioe)
				{
					details += ", " + iioe.toString();
				}
				finally
				{
					if (in != null) try
					{
						in.close();
					}
					catch (IOException ignore)
					{
						// fall through, we can not recover anyway
					}
				}
			}
			throw new RuntimeException(
				"Fatal: Failed to load OWL ontology file " + ontologyURL + "! Details: " + details);
		}
	}

}
