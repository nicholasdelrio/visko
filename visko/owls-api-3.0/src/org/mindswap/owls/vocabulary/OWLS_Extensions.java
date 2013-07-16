// The MIT License
//
// Copyright (c) 2008 Thorsten Möller - University of Basel Switzerland
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
package org.mindswap.owls.vocabulary;

import org.mindswap.owl.OWLClass;
import org.mindswap.owl.OWLDataProperty;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLObjectProperty;
import org.mindswap.owl.OWLOntology;
import org.mindswap.owl.vocabulary.Vocabulary;
import org.mindswap.utils.URIUtils;

/**
 *
 * @author unascribed
 * @version $Rev: 2350 $; $Author: nick $; $Date: 2011/01/24 06:37:54 $
 */
public abstract class OWLS_Extensions
{
	public static final String baseURI = "http://on.cs.unibas.ch/owl-s/";
	public static final String version = OWLS_1_2.version;
	public static final String PROCESS_NS = baseURI + version + "/OWLSExtensions.owl#";
	public static final String TERMINATION_NS = baseURI + version + "/ProfileServiceTermination.owl#";

	public static abstract class Process extends Vocabulary
	{

//		/**
//		 * Used to refer to the enclosing {@link org.mindswap.owls.process.Perform perform}
//		 * of some {@link org.mindswap.owls.process.Process process}.
//		 */
//		public static final OWLObjectProperty hasPerform;

		/**
		 * Used to complement the <em>consumer-pull</em> data flow specification
		 * convention that OWL-S implements with a <em>producer-push</em> oriented
		 * convention. This property makes forward navigation possible, that is,
		 * it allows to navigate from some source parameter to its sink parameter(s).
		 * Consequently, it describes the inverse direction of ValueOf binding
		 * descriptions (which realize backwards data flow navigation, aka
		 * consumer-pull).
		 * <p>
		 * Note that connections expressed using this property should always remain
		 * consistent with the actual ValueOf binding descriptions of the
		 * corresponding process specification!
		 */
		public static final OWLObjectProperty connectedTo;

		/**
		 * Sub property of {@link #connectedTo} whose domain is limited to the
		 * union of {@link OWLS.Process#Input} and {@link OWLS.Process#Output},
		 * and its range is limited to {@link OWLS.Process#Input}.
		 */
		public static final OWLObjectProperty connectedToInput;

		/**
		 * Sub property of {@link #connectedTo} whose domain is limited to the
		 * union of {@link OWLS.Process#Input} and {@link OWLS.Process#Output},
		 * and its range is limited to {@link OWLS.Process#Output}.
		 */
		public static final OWLObjectProperty connectedToOutput;

		/**
		 * In general, a data flow specification connects some source parameter
		 * to one ore more sink parameter(s). This property is intended to be
		 * used to track the <em>most specific sink<em> parameter type(s) from
		 * the set of consumers of some source parameter. Hence, this property
		 * is meant to be computed by some tool when analyzing the data flow
		 * specification of some composite process.
		 */
		public static final OWLDataProperty mssType;

		static
		{
			final OWLOntology ont = loadOntology(PROCESS_NS);

//			hasPerform        = ont.getObjectProperty(URIUtils.createURI(PROCESS_NS + "hasPerform"));
			connectedTo       = ont.getObjectProperty(URIUtils.createURI(PROCESS_NS + "connectedTo"));
			connectedToInput  = ont.getObjectProperty(URIUtils.createURI(PROCESS_NS + "connectedToInput"));
			connectedToOutput = ont.getObjectProperty(URIUtils.createURI(PROCESS_NS + "connectedToOutput"));
			mssType           = ont.getDataProperty(URIUtils.createURI(PROCESS_NS + "mssType"));
		}
	}

	public static abstract class Termination extends Vocabulary
	{
		public static final OWLClass Retriable;
		public static final OWLClass NonRetriable;
		public static final OWLClass Compensatable;
		public static final OWLClass Pivot;
		public static final OWLClass Compensator;

		public static final OWLClass CompensationServiceParameter;
		public static final OWLClass RetriabilityServiceParameter;

		public static final OWLClass Boolean;

		public static final OWLIndividual True;
		public static final OWLIndividual False;

		public static final OWLObjectProperty compensator;
		public static final OWLObjectProperty retriable;

		static
		{
			final OWLOntology ont = loadOntology(TERMINATION_NS);

			Retriable     = ont.getClass(URIUtils.createURI(TERMINATION_NS + "Retriable"));
			NonRetriable  = ont.getClass(URIUtils.createURI(TERMINATION_NS + "NonRetriable"));
			Compensatable = ont.getClass(URIUtils.createURI(TERMINATION_NS + "Compensatable"));
			Pivot         = ont.getClass(URIUtils.createURI(TERMINATION_NS + "Pivot"));
			Compensator   = ont.getClass(URIUtils.createURI(TERMINATION_NS + "Compensator"));

			CompensationServiceParameter = ont.getClass(URIUtils.createURI(TERMINATION_NS + "CompensationServiceParameter"));
			RetriabilityServiceParameter = ont.getClass(URIUtils.createURI(TERMINATION_NS + "RetriabilityServiceParameter"));

			Boolean = ont.getClass(URIUtils.createURI(TERMINATION_NS + "Boolean"));
			True    = ont.getIndividual(URIUtils.createURI(TERMINATION_NS + "True"));
			False   = ont.getIndividual(URIUtils.createURI(TERMINATION_NS + "False"));

			compensator = ont.getObjectProperty(URIUtils.createURI(TERMINATION_NS + "compensator"));
			retriable   = ont.getObjectProperty(URIUtils.createURI(TERMINATION_NS + "retriable"));
		}
	}
}
