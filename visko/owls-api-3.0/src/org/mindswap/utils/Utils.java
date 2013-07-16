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
package org.mindswap.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.mindswap.owl.vocabulary.RDF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXParseException;

/**
 * Collection of some utility functions.
 *
 * @author unascribed
 * @version $Rev: 2297 $; $Author: nick $; $Date: 2011/01/24 06:37:53 $
 */
public class Utils
{
	/** The platform dependent line separator character. */
	public static final String LINE_SEPARATOR = (System.getProperty("line.separator") != null) ? System
		.getProperty("line.separator") : "\n"; // fallback usage of \n

	static final Logger logger = LoggerFactory.getLogger(Utils.class);

	private static final DocumentBuilderFactory parserFactory = DocumentBuilderFactory.newInstance();

	/**
	 * Reverse lookup over maps. Returns the key a given value is mapped from,
	 * provided that there exist a mapping which maps to the value.
	 *
	 * @return The first key found that maps to the value or <code>null</code>
	 * 	is no mapping to the given value exists at all.
	 */
	public static <K, V> K getMapKey(final Map<K, V> map, final V value)
	{
		for (final Entry<K, V> entry : map.entrySet())
		{
			if(entry.getValue().equals(value)) return entry.getKey();
		}
		return null;
	}

	public static Node getAsNode(final String in)
	{
		try
		{
			final DocumentBuilder parser = parserFactory.newDocumentBuilder();

			parser.setErrorHandler(new ErrorHandler() {
				public void warning(final SAXParseException exception) {
					logger.debug("XML parser warning {}", exception.toString());
				}

				public void error(final SAXParseException exception) {
					logger.debug("XML parser error {}", exception.toString());
				}

				public void fatalError(final SAXParseException exception) {
					logger.debug("XML parser fatal error", exception);
				}
			});

			return parser.parse(new InputSource(new StringReader(in))).getDocumentElement();
		}
		catch(final Exception e)
		{
			logger.debug("Exception while XML parsing. Details: {}", e.toString());
		}

		return null;
	}

	public static String readURL(final URL fileURL) throws IOException
	{
		return readAll(new InputStreamReader(fileURL.openStream()));
	}

	public static String readFile(final String fileName) throws IOException
	{
		return readAll(new FileReader(fileName));
	}

	public static String readAll(final Reader reader) throws IOException
	{
		final StringBuffer buffer = new StringBuffer();

		final BufferedReader in = new BufferedReader(reader);
		int ch;
		while ((ch = in.read()) > -1)
		{
			buffer.append((char) ch);
		}
		in.close();
		return buffer.toString();
	}

// currently unused
//	public static String formatXML(final Node node)
//	{
//		try
//		{
//			final StringWriter out = new StringWriter();
//			final Document doc = node.getOwnerDocument();
//			final OutputFormat format = new OutputFormat(doc);
//			format.setIndenting(true);
//			format.setLineWidth(0);
//			format.setPreserveSpace(false);
//			format.setOmitXMLDeclaration(false);
//
//			final XMLSerializer serial = new XMLSerializer(out, format);
//			serial.asDOMSerializer();
//			serial.serialize(doc);
//			return out.toString();
//		}
//		catch (final IOException e)
//		{
//			logger.debug("Problem serializing node. Details: {}", e.toString());
//			return "Problem serializing node " + e;
//		}
//	}

	public static String formatRDF(final String rdf)
	{
		final Node node = getAsNode(rdf);
		if (node == null) return rdf;
		return formatNode(node, " ").substring(LINE_SEPARATOR.length());
	}


	public static String formatNode(final Node node, final String indent)
	{
		switch (node.getNodeType())
		{
			case Node.TEXT_NODE:
			{
				return node.getNodeValue().trim();
			}
			case Node.ELEMENT_NODE:
			{
				final StringBuilder sb = new StringBuilder();
				if (!(node.getParentNode() instanceof Document))
				{
					sb.append(LINE_SEPARATOR).append(indent).append(node.getLocalName()).append(": ");
				}

				final NodeList children = node.getChildNodes();
				if (children != null)
				{
					final int len = children.getLength();
					for (int i = 0; i < len; i++)
					{
						sb.append(formatNode(children.item(i), indent + "  "));
					}
				}
				else
				{
					final Node rdfRes = node.getAttributes().getNamedItemNS(RDF.getURI().toString(), "resource");
					if (rdfRes instanceof Attr) sb.append(URIUtils.getLocalName(((Attr) rdfRes).getValue()));
				}
				return sb.toString();
			}
			default:
				return "";
		}
	}
}
