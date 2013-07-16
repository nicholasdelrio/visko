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
 * Created on Dec 28, 2003
 */
package org.mindswap.owl.vocabulary;

import java.net.URI;

import org.mindswap.owl.OWLClass;
import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owl.OWLOntology;
import org.mindswap.utils.URIUtils;

/**
 * Specification of all syntactical elements of OWL version 1.0.
 *
 * @author unascribed
 * @see <a href="http://www.w3.org/TR/owl-features">OWL 1.0</a>
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:53 $
 */
public class OWL
{
	public final static String ns = "http://www.w3.org/2002/07/owl#";
	public final static URI NS = URI.create(ns);

	public final static URI Full_Lang 	= NS;
	public final static URI DL_Lang		= URI.create("http://www.w3.org/TR/owl-features/#term_OWLDL");
	public final static URI Lite_Lang	= URI.create("http://www.w3.org/TR/owl-features/#term_OWLLite");

	static final OWLKnowledgeBase kb = OWLFactory.createKB();

	public final static OWLClass Thing;
	public final static OWLClass Nothing;

	public final static OWLClass Ontology;
	public final static OWLClass Class;
	public final static OWLClass DeprecatedClass;
	public final static OWLClass Restriction;
	public final static OWLClass DataRange;
	public final static OWLClass AllDifferent;
	public final static OWLClass DatatypeProperty;
	public final static OWLClass SymmetricProperty;
	public final static OWLClass TransitiveProperty;
	public final static OWLClass DeprecatedProperty;
	public final static OWLClass AnnotationProperty;
	public final static OWLClass OntologyProperty;
	public final static OWLClass ObjectProperty;
	public final static OWLClass FunctionalProperty;
	public final static OWLClass InverseFunctionalProperty;

	public final static URI imports						= URI.create(ns + "imports");
	public final static URI inverseOf					= URI.create(ns + "inverseOf");
	public final static URI versionInfo					= URI.create(ns + "versionInfo");
	public final static URI backwardCompatibleWith	= URI.create(ns + "backwardCompatibleWith");
	public final static URI priorVersion				= URI.create(ns + "priorVersion");
	public final static URI incompatibleWith			= URI.create(ns + "incompatibleWith");

	public final static URI sameAs						= URI.create(ns + "sameAs");
	public final static URI differentFrom				= URI.create(ns + "differentFrom");

	public final static URI maxCardinality				= URI.create(ns + "maxCardinality");
	public final static URI equivalentClass			= URI.create(ns + "equivalentClass");
	public final static URI distinctMembers			= URI.create(ns + "distinctMembers");
	public final static URI oneOf							= URI.create(ns + "oneOf");
	public final static URI minCardinality				= URI.create(ns + "minCardinality");
	public final static URI complementOf				= URI.create(ns + "complementOf");
	public final static URI onProperty					= URI.create(ns + "onProperty");
	public final static URI equivalentProperty		= URI.create(ns + "equivalentProperty");
	public final static URI allValuesFrom				= URI.create(ns + "allValuesFrom");
	public final static URI unionOf						= URI.create(ns + "unionOf");
	public final static URI hasValue						= URI.create(ns + "hasValue");
	public final static URI someValuesFrom				= URI.create(ns + "someValuesFrom");
	public final static URI disjointWith				= URI.create(ns + "disjointWith");
	public final static URI cardinality					= URI.create(ns + "cardinality");
	public final static URI intersectionOf				= URI.create(ns + "intersectionOf");

	static
	{
		final OWLOntology ont = kb.createOntology(NS);

		Thing   = ont.createClass(URIUtils.createURI(ns + "Thing"));
		Nothing = ont.createClass(URIUtils.createURI(ns + "Nothing"));

		Ontology                  = ont.createClass(URIUtils.createURI(ns + "Ontology"));
		Class                     = ont.createClass(URIUtils.createURI(ns + "Class"));
		DeprecatedClass           = ont.createClass(URIUtils.createURI(ns + "DeprecatedClass"));
		Restriction               = ont.createClass(URIUtils.createURI(ns + "Restriction"));
		DataRange                 = ont.createClass(URIUtils.createURI(ns + "DataRange"));
		AllDifferent              = ont.createClass(URIUtils.createURI(ns + "AllDifferent"));
		DatatypeProperty          = ont.createClass(URIUtils.createURI(ns + "DatatypeProperty"));
		SymmetricProperty         = ont.createClass(URIUtils.createURI(ns + "SymmetricProperty"));
		TransitiveProperty        = ont.createClass(URIUtils.createURI(ns + "TransitiveProperty"));
		DeprecatedProperty        = ont.createClass(URIUtils.createURI(ns + "DeprecatedProperty"));
		AnnotationProperty        = ont.createClass(URIUtils.createURI(ns + "AnnotationProperty"));
		OntologyProperty          = ont.createClass(URIUtils.createURI(ns + "OntologyProperty"));
		ObjectProperty            = ont.createClass(URIUtils.createURI(ns + "ObjectProperty"));
		FunctionalProperty        = ont.createClass(URIUtils.createURI(ns + "FunctionalProperty"));
		InverseFunctionalProperty = ont.createClass(URIUtils.createURI(ns + "InverseFunctionalProperty"));
	}

}
