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

import org.mindswap.owl.OWLClass;
import org.mindswap.owl.OWLDataProperty;
import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owl.OWLObjectProperty;
import org.mindswap.owl.OWLOntology;
import org.mindswap.owl.list.ListVocabulary;
import org.mindswap.swrl.Atom;
import org.mindswap.utils.URIUtils;

/**
 *
 * @author unascribed
 * @see <a href="http://www.w3.org/Submission/SWRL/">SWRL</a>
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:53 $
 */
public class SWRL
{
	public final static String NS = "http://www.w3.org/2003/11/swrl#";
	static final OWLKnowledgeBase kb = OWLFactory.createKB();

	public final static OWLClass AtomList;
	public final static OWLClass Atom;
	public final static OWLClass ClassAtom;
	public final static OWLClass IndividualPropertyAtom;
	public final static OWLClass DifferentIndividualsAtom;
	public final static OWLClass SameIndividualAtom;
	public final static OWLClass DatavaluedPropertyAtom;
	public final static OWLClass DataRangeAtom;
	public final static OWLClass BuiltinAtom;

	public final static OWLClass Builtin;
	public final static OWLClass Variable;

	public final static OWLObjectProperty classPredicate;
	public final static OWLObjectProperty propertyPredicate;
	public final static OWLObjectProperty argument1;
	public final static OWLObjectProperty argument2;
	public final static OWLDataProperty _argument2;
	public final static OWLObjectProperty dataRange;
	public final static OWLObjectProperty builtin;
	public final static OWLObjectProperty arguments;

	public final static ListVocabulary<Atom> AtomListVocabulary;

	static
	{
		final OWLOntology ont = kb.createOntology(URIUtils.standardURI(NS));

		AtomList                 = ont.createClass(URIUtils.createURI(NS + "AtomList"));
		Atom                     = ont.createClass(URIUtils.createURI(NS + "Atom"));
		ClassAtom                = ont.createClass(URIUtils.createURI(NS + "ClassAtom"));
		IndividualPropertyAtom   = ont.createClass(URIUtils.createURI(NS + "IndividualPropertyAtom"));
		DifferentIndividualsAtom = ont.createClass(URIUtils.createURI(NS + "DifferentIndividualsAtom"));
		SameIndividualAtom       = ont.createClass(URIUtils.createURI(NS + "SameIndividualAtom"));
		DatavaluedPropertyAtom   = ont.createClass(URIUtils.createURI(NS + "DatavaluedPropertyAtom"));
		DataRangeAtom            = ont.createClass(URIUtils.createURI(NS + "DataRangeAtom"));
		BuiltinAtom              = ont.createClass(URIUtils.createURI(NS + "BuiltinAtom"));

		Builtin  = ont.createClass(URIUtils.createURI(NS + "Builtin"));
		Variable = ont.createClass(URIUtils.createURI(NS + "Variable"));

		classPredicate    = ont.createObjectProperty(URIUtils.createURI(NS + "classPredicate"));
		propertyPredicate = ont.createObjectProperty(URIUtils.createURI(NS + "propertyPredicate"));
		argument1         = ont.createObjectProperty(URIUtils.createURI(NS + "argument1"));
		argument2         = ont.createObjectProperty(URIUtils.createURI(NS + "argument2"));
		_argument2        = ont.createDataProperty(URIUtils.createURI(NS + "argument2"));
		dataRange         = ont.createObjectProperty(URIUtils.createURI(NS + "dataRange"));
		builtin           = ont.createObjectProperty(URIUtils.createURI(NS + "builtin"));
		arguments         = ont.createObjectProperty(URIUtils.createURI(NS + "arguments"));

		AtomListVocabulary =	new ListVocabulary<Atom>(AtomList, ont.createObjectProperty(RDF.first),
			ont.createObjectProperty(RDF.rest),	ont.createIndividual(null, RDF.nil), Atom.class);
	}
}
