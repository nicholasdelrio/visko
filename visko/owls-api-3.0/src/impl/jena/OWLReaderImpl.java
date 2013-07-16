//The MIT License
//
// Copyright 2004 Evren Sirin
// Copyright 2009 Thorsten Möller - University of Basel Switzerland
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
package impl.jena;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URI;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.mindswap.owl.OWLErrorHandler;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owl.OWLOntology;
import org.mindswap.owl.OWLReader;
import org.mindswap.utils.URIUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

import com.hp.hpl.jena.ontology.OntDocumentManager;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntDocumentManager.ReadFailureHandler;
import com.hp.hpl.jena.rdf.arp.ParseException;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFErrorHandler;
import com.hp.hpl.jena.rdf.model.RDFReader;
import com.hp.hpl.jena.shared.JenaException;

/**
 *
 * @author unascribed
 * @version $Rev: 2298 $; $Author: nick $; $Date: 2011/01/24 06:37:51 $
 */
public class OWLReaderImpl implements OWLReader
{
	static final Logger logger = LoggerFactory.getLogger(OWLReaderImpl.class);

	private static final FailureHandler READ_FAILURE_HANDLER; // global singleton instance

	OWLErrorHandler errHandler;
	private final OntModelSpec spec;
	private boolean ignoreFailedImport;

	static
	{
		READ_FAILURE_HANDLER = new FailureHandler();
		OntDocumentManager.getInstance().setReadFailureHandler(READ_FAILURE_HANDLER);
	}

	public OWLReaderImpl()
	{
		super();
		spec = JenaOWLProvider.createOntSpec();
		ignoreFailedImport = false;
	}

	/* @see org.mindswap.owl.OWLReader#clear() */
	public void clear()
	{
		OntDocumentManager.getInstance().clearCache();
		READ_FAILURE_HANDLER.errorMsgMap.clear();
	}

	/* @see org.mindswap.owl.OWLReader#setErrorHandler(org.mindswap.owl.OWLErrorHandler) */
	public OWLErrorHandler setErrorHandler(final OWLErrorHandler newErrHandler)
	{
		OWLErrorHandler old = this.errHandler;
		this.errHandler = newErrHandler;
		return old;
	}

	/* @see org.mindswap.owl.OWLReader#read(org.mindswap.owl.OWLKnowledgeBase, java.net.URI) */
	public OWLOntology read(final OWLKnowledgeBase kb, final URI url) throws IOException
	{
		return readInternal(kb, new InputSource(url.toString()), url);
	}

	/* @see org.mindswap.owl.OWLReader#read(org.mindswap.owl.OWLKnowledgeBase, java.io.Reader, java.net.URI) */
	public OWLOntology read(final OWLKnowledgeBase kb, final Reader in, final URI baseURI) throws IOException
	{
		return readInternal(kb, new InputSource(in), baseURI);
	}

	/* @see org.mindswap.owl.OWLReader#read(org.mindswap.owl.OWLKnowledgeBase, java.io.InputStream, java.net.URI) */
	public OWLOntology read(final OWLKnowledgeBase kb, final InputStream in, final URI baseURI) throws IOException
	{
		return readInternal(kb, new InputSource(in), baseURI);
	}

	/* @see org.mindswap.owl.OWLReader#getIgnoredOntologies() */
	public Set<URI> getIgnoredOntologies()
	{
		Set<URI> ignores = new HashSet<URI>();
		for (Iterator<String> it = spec.getDocumentManager().listIgnoredImports(); it.hasNext(); )
		{
			ignores.add(URIUtils.createURI(it.next()));
		}
		return ignores;
	}

	/* @see org.mindswap.owl.OWLReader#addIgnoredOntology(java.net.URI) */
	public void addIgnoredOntology(final URI uri)
	{
		spec.getDocumentManager().addIgnoreImport(URIUtils.standardURI(uri).toString());
	}

	/* @see org.mindswap.owl.OWLReader#setIgnoreFailedImport(boolean) */
	public void setIgnoreFailedImport(final boolean ignore)
	{
		ignoreFailedImport = ignore;
	}

	/* @see org.mindswap.owl.OWLReader#isIgnoreFailedImport() */
	public boolean isIgnoreFailedImport()
	{
		return ignoreFailedImport;
	}

	private OWLOntology readInternal(final OWLKnowledgeBase kb, final InputSource in, final URI uri)
		throws IOException
	{
		final URI stdURI = (uri != null)? URIUtils.standardURI(uri) :
			URI.create(OWLOntology.SYNTHETIC_ONT_PREFIX + "reader:Ontology" + System.nanoTime());

		OWLOntology ont = kb.getOntology(stdURI);
		if (ont != null)
		{
			logger.debug("Use already loaded ontology {} from given knowledge base.", stdURI);
		}
		else
		{
			final OntModel jenaModel;
			String base;

			if ((base = in.getSystemId()) != null)
			{
				logger.debug("Reading ontology {} via Jena's OntDocumentManager ...", base);
				jenaModel = spec.getDocumentManager().getOntology(base, spec);
			}
			else
			{
				base = stdURI.toString();
				logger.debug("Reading ontology from InputStream/Reader, using base URI {}  ...", base);

				jenaModel = ModelFactory.createOntologyModel(spec);

				final RDFReader reader = jenaModel.getReader();
				if (errHandler != null) reader.setErrorHandler(new DefaultErrorHandler());

				try
				{
					if (in.getByteStream() != null) reader.read(jenaModel, in.getByteStream(), base);
					else if (in.getCharacterStream() != null) reader.read(jenaModel, in.getCharacterStream(), base);
				}
				catch (JenaException e)
				{
					throw new IOException("Failed to read resource. Details: " + e);
				}
				finally
				{
					try
					{
						if (in.getByteStream() != null) in.getByteStream().close();
						else if (in.getCharacterStream() != null) in.getCharacterStream().close();
					}
					catch (IOException e) { /* fall through --> we can't do anything useful to recover anyway */ }
				}

				spec.getDocumentManager().loadImports(jenaModel);
			}

			// Finally, check if reading of ontology or one of its imports failed and throw IOE if necessary.
			// It needs to be done this way because Jena's OntDocumentManager reports failures only to its
			// ReadFailureHandler
			final Set<String> ontsToCheck = new HashSet<String>();
			ontsToCheck.add(base);
			if (!ignoreFailedImport) ontsToCheck.addAll(jenaModel.listImportedOntologyURIs(true));
			final String failures = READ_FAILURE_HANDLER.hasReportedFailure(ontsToCheck);
			if (failures != null)
				throw new IOException("Failed to read ontology. Details: " + failures);

			ont = kb.createOntology(stdURI, jenaModel);
		}

		return ont;
	}

	private final class DefaultErrorHandler implements RDFErrorHandler
	{
		DefaultErrorHandler()
		{
			super();
		}

		/* @see com.hp.hpl.jena.rdf.model.RDFErrorHandler#warning(java.lang.Exception) */
		public void warning(final Exception e)
		{
			errHandler.warning(e);
			if (logger.isDebugEnabled()) logger.debug(ParseException.formatMessage(e));
		}

		/* @see com.hp.hpl.jena.rdf.model.RDFErrorHandler#error(java.lang.Exception) */
		public void error(final Exception e)
		{
			errHandler.error(e);
			if (logger.isDebugEnabled()) logger.debug(ParseException.formatMessage(e));
		}

		/* @see com.hp.hpl.jena.rdf.model.RDFErrorHandler#fatalError(java.lang.Exception) */
		public void fatalError(final Exception e)
		{
			errHandler.fatal(e);
			if (logger.isDebugEnabled()) logger.debug(ParseException.formatMessage(e));
		}
	}

	private static final class FailureHandler implements ReadFailureHandler
	{
		FailureHandler()
		{
			super();
		}

		// Since this class is used as a singleton access may happen concurrently
		// by different threads, thus, we should be thread safe.
		final ConcurrentHashMap<String, String> errorMsgMap = new ConcurrentHashMap<String, String>();

		/* @see com.hp.hpl.jena.ontology.OntDocumentManager.ReadFailureHandler#handleFailedRead(java.lang.String, com.hp.hpl.jena.rdf.model.Model, java.lang.Exception) */
		public void handleFailedRead(final String url, final Model model, final Exception e)
		{
			String previous = errorMsgMap.put(url, e.toString());
			if (previous != null) // if Jena uses model caching a failure may occur once only for every URL
				logger.debug("Bug indication: map already contained value for key. Jena cache seems to have changed.");
		}

		/**
		 * @param ontologyURIs The set of ontology URIs to check.
		 * @return Concatenation of all failure messages reported, or <code>null</code>
		 * 	if none were reported for the given URIs
		 */
		String hasReportedFailure(final Set<String> ontologyURIs)
		{
			final StringBuilder failures = new StringBuilder();
			for (String ontURI : ontologyURIs)
			{
				final String excMsg = errorMsgMap.get(ontURI);
				if (excMsg != null)
				{
					failures.append(excMsg).append(" ");
				}
			}
			return (failures.length() > 0)? failures.toString() : null;
		}
	}
}
