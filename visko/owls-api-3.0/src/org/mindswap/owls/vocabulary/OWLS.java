//The MIT License
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

package org.mindswap.owls.vocabulary;

import org.mindswap.owl.OWLOntology;
import org.mindswap.owl.vocabulary.Vocabulary;
import org.mindswap.utils.URIUtils;

/**
 * This class merely represents that version of OWL-S which is supported <strong>by
 * default</strong> by this library.
 * <p>
 * A switch to newer version of OWL-S will be made by re-assigning the super class
 * (which represents the actual vocabulary definition).
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:54 $
 */
public abstract class OWLS extends OWLS_1_2
{
	/**
	 * Import the OWL-S ontologies (Service, Profile, Process and Grounding)
	 * into the given ontology. This will add both an import statement and the
	 * actual data models.
	 *
	 * @param ont The ontology which to enrich by import statements.
	 */
	public static final void addOWLSImports(final OWLOntology ont)
	{
		// We use an arbitrary constant of each OWL-S ontology since this will cause the
		// ontologies to be loaded first if accessed for the first time.
		ont.addImport(OWLS.Service.Service.getOntology());
		ont.addImport(OWLS.Profile.Profile.getOntology());
		ont.addImport(OWLS.Process.Process.getOntology());
		ont.addImport(OWLS.Grounding.WsdlGrounding.getOntology());
	}

	/**
	 * Import one of the OWL-S ontologies into the given ontology. This will add
	 * both an import statement and the actual data model.For instance, if one
	 * would like to import the Profile and Service ontologies, it would have to
	 * be done as follows.
	 * <pre>
	 * OWLOntology myOnt = ...
	 * OWLS.addOWLSImport(myOnt, OWLS.PROFILE_URI);
	 * OWLS.addOWLSImport(myOnt, OWLS.SERVICE_URI);
	 * </pre>
	 *
	 * @param ont The ontology which to enrich by an import statement.
	 * @param owlsOntURI The URI of the OWLS ontology to import.
	 * @return <code>true</code> if the given URI refers a OWL-S ontology.
	 */
	public static final boolean addOWLSImport(final OWLOntology ont, final String owlsOntURI)
	{
		OWLOntology owlsOnt = Vocabulary.getOntology(URIUtils.standardURI(owlsOntURI));
		if (owlsOnt != null)
		{
			ont.addImport(owlsOnt);
			return true;
		}
		return false;
	}

	/**
	 * @return The version number as string. Derived from the value of the
	 * 	version constant of the super class.
	 */
	public static final String version()
	{
		return version;
	}
}
