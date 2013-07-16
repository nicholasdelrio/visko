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

import java.io.IOException;
import java.io.StringReader;

import org.mindswap.owl.OWLDataValue;
import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owl.OWLOntology;
import org.mindswap.owl.vocabulary.RDF;

/**
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:53 $
 */
public class RDFUtils
{
	/**
	 * @param rdf Some string in RDF/XML syntax form.
	 * @return The original string enclosed by an opening and closing top level
	 * 	tag <code>&lt;rdf:RDF xmlns:rdf="..." &gt;</code> respectively
	 * 	<code>&lt;/rdf:RDF&gt;</code>.
	 */
	public static final String addRDFTag(final String rdf)
	{
		return "<rdf:RDF xmlns:rdf=\"" + RDF.getURI() + "\">" + rdf + "</rdf:RDF>";
	}

	/**
	 * Remove the outermost <tt>&lt;rdf:RDF ...&gt;</tt> tag from the given string.
	 *
	 * @param rdf Some string in RDF/XML syntax form. Leading XML processing
	 * 	instructions, DTD element declarations, and comments will be removed
	 * 	as well. More precisely, everything that comes before the
	 * 	<tt>&lt;rdf:RDF ...&gt;</tt> tag.
	 * @param inlineNamespaces If <code>true</code> namespace declarations
	 * 	(<tt>xmlns</tt> attribute) that appear in the <tt>&lt;rdf:RDF ...&gt;</tt>
	 * 	tag will not get lost but appear in-line in the result, that is,
	 * 	namespaces will be declared together with the elements that make use of
	 * 	them. Otherwise, if <code>false</code>, all namespace declarations in the
	 * 	<tt>&lt;rdf:RDF ...&gt;</tt> get	lost.
	 * @return A string containing only elements within the <tt>&lt;rdf:RDF ...&gt;</tt>
	 * 	tag of the given string.
	 * @throws IllegalArgumentException May be thrown if syntax of given string is
	 * 	not valid RDF/XML.
	 */
	public static final String removeRDFTag(String rdf, final boolean inlineNamespaces)
	{
		int openingRDFTagBegin = rdf.indexOf("<rdf:RDF");
		int openingRDFTagEnd = rdf.indexOf(">", openingRDFTagBegin + 8);
		int closingRDFTagBegin = rdf.indexOf("</rdf:RDF>", openingRDFTagEnd + 1);

		if (inlineNamespaces)
		{
			StringBuffer buffer = new StringBuffer();
			buffer.append(rdf.substring(0, openingRDFTagEnd + 1));
			buffer.append("<rdf:Description><rdfs:label rdf:parseType=\"Literal\">");
			buffer.append(rdf.substring( openingRDFTagEnd + 1, closingRDFTagBegin));
			buffer.append("</rdfs:label></rdf:Description>");
			buffer.append(rdf.substring(closingRDFTagBegin));

			OWLKnowledgeBase kb = OWLFactory.createKB();
			OWLOntology ont = null;
			try
			{
				ont = kb.read(new StringReader(buffer.toString()), null);
			}
			catch (IOException e)
			{
				throw new IllegalArgumentException("Given RDF string seems to have invalid syntax.", e);
			}
			OWLIndividual ind = ont.getIndividuals(true).get(0);
			rdf = ind.getLabel(null);
		}
		else
			rdf = rdf.substring(openingRDFTagEnd + 1, closingRDFTagBegin);

		return rdf;
	}

	/**
	 * @param ind
	 * @return
	 */
	public static final OWLDataValue toXMLLiteral(final OWLIndividual ind)
	{
		return ind.getOntology().createDataValue(ind.toRDF(false, true), RDF.XMLLiteral);
	}
}
