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
 * Created on Mar 29, 2005
 */
package impl.jena;

import org.mindswap.owl.OWLProperty;

import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDFS;

/**
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:51 $
 */
public abstract class OWLPropertyImpl<P extends OntProperty> extends OWLEntityImpl<P> implements OWLProperty
{
	public OWLPropertyImpl(final OWLOntologyImpl ontology, final P property)
	{
		super(ontology, property);
	}

	/* @see org.mindswap.owl.OWLProperty#isAnnotationProperty() */
	public final boolean isAnnotationProperty()
	{
		return getImplementation().isAnnotationProperty();
	}

	/* @see org.mindswap.owl.OWLProperty#isDatatypeProperty() */
	public final boolean isDatatypeProperty()
	{
		return getImplementation().isDatatypeProperty();
	}

	/* @see org.mindswap.owl.OWLProperty#isObjectProperty() */
	public final boolean isObjectProperty()
	{
		return getImplementation().isObjectProperty();
	}

	protected final boolean isEquivalentProperty(final Property p)
	{
		return ontology.getKB().ontModel.contains(resource, OWL.equivalentProperty, p);
	}

	protected final boolean isSubPropertyOf(final Property p)
	{
		return ontology.getKB().ontModel.contains(resource, RDFS.subPropertyOf, p);
	}
}
