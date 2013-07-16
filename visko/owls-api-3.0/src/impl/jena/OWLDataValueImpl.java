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
 * Created on Oct 30, 2004
 */
package impl.jena;

import impl.owl.OWLObjectImpl;

import java.net.URI;

import org.mindswap.owl.OWLDataValue;

import com.hp.hpl.jena.rdf.model.Literal;

/**
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:51 $
 */
public class OWLDataValueImpl extends OWLObjectImpl implements OWLDataValue {
	protected final Literal literal;

	public OWLDataValueImpl(final Literal literal) {
		this.literal = literal;
	}

	/* @see org.mindswap.owl.OWLDataValue#getDatatypeURI() */
	public URI getDatatypeURI() {
		final String datatypeURI = literal.getDatatypeURI();
		return (datatypeURI == null)? null : URI.create(datatypeURI);
	}

	/* @see org.mindswap.owl.OWLDataValue#getLanguage() */
	public String getLanguage() {
		return literal.getLanguage();
	}

	/* @see org.mindswap.owl.OWLDataValue#getLexicalValue() */
	public String getLexicalValue() {
		return literal.getLexicalForm();
	}

	/* @see org.mindswap.owl.OWLDataValue#getValue() */
	public Object getValue() {
		return literal.getValue();
	}

	/* @see org.mindswap.owl.OWLObject#getImplementation() */
	public Literal getImplementation() {
		return literal;
	}

	/* @see java.lang.Object#toString() */
	@Override
	public String toString() {
		return getLexicalValue();
	}

	/* @see org.mindswap.owl.OWLValue#isDataValue() */
	public final boolean isDataValue() {
		return true;
	}

	/* @see org.mindswap.owl.OWLValue#isIndividual() */
	public final boolean isIndividual() {
		return false;
	}
}
