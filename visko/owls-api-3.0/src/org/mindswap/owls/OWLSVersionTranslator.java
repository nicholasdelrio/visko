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
 * Created on Dec 16, 2004
 */
package org.mindswap.owls;

import org.mindswap.owl.OWLOntology;

/**
 * Transforms an older version of an OWL-S ontology to the default version that
 * is supported by the API. Usually, this would be the latest version of OWL-S.
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:55 $
 */
public interface OWLSVersionTranslator
{
	/**
	 * @return The OWL-S version this translator reads.
	 */
	public String getVersion();

	/**
	 * @param ontology The ontology subject to be translated.
	 * @return <code>true</code> if this translator can translate the given
	 * 	ontology.
	 */
	public boolean canTranslate(OWLOntology ontology);

	/**
	 * Translates the statements in the input ontology to the default OWL-S
	 * version that is supported by the API and adds all translated statements
	 * to the output ontology.
	 * <p>
	 * Imported ontologies of the input ontology (if any) are added to the
	 * output ontology in untranslated form, i.e., without any changes.
	 *
	 * @param inputOnt The ontology subject to be translated.
	 * @param outputOnt The ontology that will be added by all translated
	 * 	statements.
	 * @return The translated ontology. Usually, it would be the
	 * 	<code>outputOnt</code>. In case this translator actually does not need
	 * 	to do any translation at all, it may also return the <code>inputOnt</code>
	 * 	as is.
	 */
	public OWLOntology translate(OWLOntology inputOnt, OWLOntology outputOnt);
}
