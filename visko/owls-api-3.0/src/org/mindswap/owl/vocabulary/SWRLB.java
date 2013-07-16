/*
 * Created on Jul 31, 2004
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

import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLOntology;
import org.mindswap.owl.list.ListVocabulary;
import org.mindswap.swrl.SWRLDataObject;
import org.mindswap.utils.URIUtils;

/**
 * Constant declarations for SWRL built-in atoms. Note that the set of constants
 * declared here is not complete, i.e., the standard defines more of them which
 * are missing here.
 *
 * @author unascribed
 * @see <a href="http://www.w3.org/Submission/SWRL/">SWRL</a>
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:53 $
 */
public class SWRLB
{
	public final static String NS = "http://www.w3.org/2003/11/swrlb#";

	public final static OWLIndividual equal;
	public final static OWLIndividual notEqual;
	public final static OWLIndividual lessThan;
	public final static OWLIndividual lessThanOrEqual;
	public final static OWLIndividual greaterThan;
	public final static OWLIndividual greaterThanOrEqual;

	public final static OWLIndividual add;
	public final static OWLIndividual subtract;
	public final static OWLIndividual multiply;
	public final static OWLIndividual divide;
	public final static OWLIndividual mod;
	public final static OWLIndividual pow;

	public final static ListVocabulary<SWRLDataObject> BuiltInArgsListVocabulary;

	static
	{
		final OWLOntology ont = SWRL.kb.createOntology(URIUtils.standardURI(NS));

		equal              = ont.createIndividual(null, URIUtils.createURI(NS + "equal"));
		notEqual           = ont.createIndividual(null, URIUtils.createURI(NS + "notEqual"));
		lessThan           = ont.createIndividual(null, URIUtils.createURI(NS + "lessThan"));
		lessThanOrEqual    = ont.createIndividual(null, URIUtils.createURI(NS + "lessThanOrEqual"));
		greaterThan        = ont.createIndividual(null, URIUtils.createURI(NS + "greaterThan"));
		greaterThanOrEqual = ont.createIndividual(null, URIUtils.createURI(NS + "greaterThanOrEqual"));

		add      = ont.createIndividual(null, URIUtils.createURI(NS + "add"));
		subtract = ont.createIndividual(null, URIUtils.createURI(NS + "subtract"));
		multiply = ont.createIndividual(null, URIUtils.createURI(NS + "multiply"));
		divide   = ont.createIndividual(null, URIUtils.createURI(NS + "divide"));
		mod      = ont.createIndividual(null, URIUtils.createURI(NS + "mod"));
		pow      = ont.createIndividual(null, URIUtils.createURI(NS + "pow"));

		BuiltInArgsListVocabulary = RDF.ListVocabulary.specialize(RDF.ListVocabulary.list(), SWRLDataObject.class);
	}
}
