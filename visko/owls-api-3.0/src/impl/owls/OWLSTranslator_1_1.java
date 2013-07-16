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

/*
 * Created on Dec 27, 2003
 *
 */
package impl.owls;

import org.mindswap.owl.OWLOntology;
import org.mindswap.owls.OWLSVersionTranslator;
import org.mindswap.owls.vocabulary.OWLS_1_1;

/**
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:54 $
 */
public class OWLSTranslator_1_1 implements OWLSVersionTranslator
{
	public OWLSTranslator_1_1()
	{
		super();
	}

	/* @see org.mindswap.owls.OWLSVersionTranslator#getVersion() */
	public String getVersion()
	{
		return OWLS_1_1.version;
	}

	/* @see org.mindswap.owls.OWLSVersionTranslator#canTranslate(org.mindswap.owl.OWLOntology) */
	public boolean canTranslate(final OWLOntology ontology)
	{
		return false; // TODO implement translation OWL-S 1.1 --> 1.2
//		return ontology.getInstances(OWLS_1_1.Service.Service, false).size() > 0;
	}

	/* @see org.mindswap.owls.OWLSVersionTranslator#translate(org.mindswap.owl.OWLOntology, org.mindswap.owl.OWLOntology) */
	public OWLOntology translate(final OWLOntology inputOnt, final OWLOntology outputOnt)
	{
		return inputOnt; // return the input ontology as is since it is known to be OWL-S 1.1
	}
}
