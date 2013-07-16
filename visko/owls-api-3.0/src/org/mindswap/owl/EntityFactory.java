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
 * Created on Dec 29, 2003
 */
package org.mindswap.owl;

import java.net.URI;

/**
 * This factory can be used to create OWL entities (i.e., individuals, classes,
 * and properties) whenever no explicit ontology is available at creation time.
 * <p>
 * Note that all entities created by this factory will be added to an internal
 * ontology. This may lead to the following problems at runtime, which is the
 * reason why this class was marked as deprecated:
 * <ol>
 * 	<li>The internal ontology may blow up over time since there is no
 * 	automatic garbage collection mechanism. Consequently, you need to take
 * 	over responsibility in disposing the objects if they are no longer
 * 	referenced.</li>
 * 	<li>Since this class may be referenced by multiple code fragments, each
 * 	able to modify the internal ontology whenever some OWL entity is created,
 * 	inconsistencies of the ontology can easily arise. This is of particular
 * 	importance if reasoning is done (implicitly) on this ontology.</li>
 * </ol>
 *
 * @deprecated Always try to create OWL entities from an existing knowledge base
 * 	or an existing ontology; see also {@link OWLFactory#createKB()} and
 * 	{@link OWLFactory#createOntology(URI, boolean)}.
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:52 $
 */
@Deprecated
public abstract class EntityFactory
{
    private static final OWLOntology factory = OWLFactory.createOntology(
   	 URI.create(OWLOntology.SYNTHETIC_ONT_PREFIX + "entityFactory:Ontology"), false);

    public static final OWLIndividual createInstance(final OWLClass clazz, final URI uri) {
        return factory.createInstance(clazz, uri);
    }

    public static final OWLIndividual createIndividual(final URI uri) {
        return factory.createIndividual(null, uri);
    }

    public static final OWLDataValue createDataValue(final String value) {
        return factory.createDataValue(value);
    }

    public static final OWLDataValue createDataValue(final String value, final String language) {
        return factory.createDataValue(value, language);
    }

    public static final OWLDataValue createDataValue(final Object value, final URI datatypeURI) {
        return factory.createDataValue(value, datatypeURI);
    }

    public static final OWLDataValue createDataValue(final Object value) {
        return factory.createDataValue(value);
    }

    public static final OWLDataType createDataType(final URI uri) {
   	 return factory.createDataType(uri);
    }

    public static final OWLClass createClass(final URI uri) {
        return factory.createClass(uri);
    }

    public static final OWLObjectProperty createObjectProperty(final URI uri) {
        return factory.createObjectProperty(uri);
    }

    public static final OWLDataProperty createDataProperty(final URI uri) {
        return factory.createDataProperty(uri);
    }
}
