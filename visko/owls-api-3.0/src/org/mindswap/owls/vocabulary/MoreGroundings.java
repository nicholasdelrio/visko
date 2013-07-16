//The MIT License
//
// Copyright (c) 2004 Evren Sirin
// Copyright (c) 2009 Thorsten Möller - University of Basel Switzerland
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
 * Created on 13.04.2005
 */
package org.mindswap.owls.vocabulary;

import org.mindswap.owl.OWLClass;
import org.mindswap.owl.OWLDataProperty;
import org.mindswap.owl.OWLObjectProperty;
import org.mindswap.owl.OWLOntology;
import org.mindswap.owl.vocabulary.Vocabulary;
import org.mindswap.utils.URIUtils;

/**
 * Static description for the additional OWL-S Extensions groundings
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:54 $
 * @see <a href="http://on.cs.unibas.ch/owl-s/1.2/MoreGroundings.owl">MoreGroundings.owl</a>
 */
public abstract class MoreGroundings
{
	public static final String baseURI = "http://on.cs.unibas.ch/owl-s/";
	public static final String version = OWLS_1_2.version;
	public static final String JAVA_GRDG_NS = baseURI + version + "/MoreGroundings.owl#";

	public static abstract class Java extends Vocabulary
	{
		public static final OWLObjectProperty owlsParameter;
		public static final OWLClass JavaGrounding;
		public static final OWLClass JavaAtomicProcessGrounding;
		public static final OWLDataProperty javaClass;
		public static final OWLDataProperty javaMethod;
		public static final OWLClass JavaVariable;
		public static final OWLDataProperty javaType;
		public static final OWLClass JavaParameter;
		public static final OWLDataProperty paramIndex;
		public static final OWLObjectProperty javaOutput;
		public static final OWLObjectProperty hasJavaParameter;
		public static final OWLDataProperty transformatorClass;

		static
		{
			final OWLOntology ont = loadOntology(JAVA_GRDG_NS);

			JavaGrounding = ont.getClass(URIUtils.createURI(JAVA_GRDG_NS + "JavaGrounding"));

			JavaAtomicProcessGrounding = ont.getClass(URIUtils.createURI(JAVA_GRDG_NS + "JavaAtomicProcessGrounding"));

			javaClass	     = ont.getDataProperty(URIUtils.createURI(JAVA_GRDG_NS + "javaClass"));
			javaMethod	     = ont.getDataProperty(URIUtils.createURI(JAVA_GRDG_NS + "javaMethod"));

			JavaVariable     = ont.getClass(URIUtils.createURI(JAVA_GRDG_NS + "JavaVariable"));
			javaType         = ont.getDataProperty(URIUtils.createURI(JAVA_GRDG_NS + "javaType"));
			JavaParameter    = ont.getClass(URIUtils.createURI(JAVA_GRDG_NS + "JavaParameter"));
			paramIndex       = ont.getDataProperty(URIUtils.createURI(JAVA_GRDG_NS + "paramIndex"));
			transformatorClass = ont.getDataProperty(URIUtils.createURI(JAVA_GRDG_NS + "transformatorClass"));

			javaOutput       = ont.getObjectProperty(URIUtils.createURI(JAVA_GRDG_NS + "javaOutput"));
			hasJavaParameter = ont.getObjectProperty(URIUtils.createURI(JAVA_GRDG_NS + "hasJavaParameter"));

			owlsParameter    = ont.getObjectProperty(URIUtils.createURI(JAVA_GRDG_NS + "owlsParameter"));
		}
	}
}
