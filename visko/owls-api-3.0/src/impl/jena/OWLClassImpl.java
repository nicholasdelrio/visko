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
 * Created on Dec 12, 2004
 */
package impl.jena;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.mindswap.owl.OWLClass;
import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owl.OWLProperty;
import org.mindswap.owl.OWLType;
import org.mindswap.owl.OWLValue;

import com.hp.hpl.jena.ontology.OntClass;

/**
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:51 $
 */
public class OWLClassImpl extends OWLEntityImpl<OntClass> implements OWLClass
{
	public OWLClassImpl(final OWLOntologyImpl ont, final OntClass ontClass)
	{
		super(ont, ontClass);
	}

	public Map<OWLProperty, List<OWLValue>> getProperties() {
		return ontology.getKB().getProperties(this);
	}

	public List<OWLProperty> getDeclaredProperties(final boolean direct) {
		return ontology.getKB().getDeclaredProperties(this, direct);
	}

	public Set<OWLClass> getSubClasses(final boolean direct) {
		return ontology.getKB().getSubClasses(this, direct);
	}

	public Set<OWLClass> getSuperClasses( final boolean direct ) {
		return ontology.getKB().getSuperClasses( this, direct );
	}

	public Set<OWLClass> getEquivalentClasses() {
		return ontology.getKB().getEquivalentClasses(this);
	}

	public OWLIndividualList<?> getInstances()
	{
		return ontology.getKB().getInstances(this, false);
	}

	public final boolean isDataType() {
		return false;
	}

	public final boolean isClass() {
		return true;
	}

	public boolean isDisjointWith(final OWLType type)
	{
		return ontology.getKB().isDisjoint(this, type);
	}

	public boolean isSubTypeOf(final OWLType type) {
		return ontology.getKB().isSubTypeOf(this, type);
	}

	public boolean isEquivalentTo(final OWLType type) {
		return ontology.getKB().isEquivalent(this, type);
	}

	public boolean isEnumerated() {
		return ontology.getKB().isEnumerated(this);
	}

	public OWLIndividualList<?> getEnumerations() {
		return ontology.getKB().getEnumerations(this);
	}
}
