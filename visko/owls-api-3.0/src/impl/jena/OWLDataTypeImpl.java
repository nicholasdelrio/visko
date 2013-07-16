// The MIT License
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
 * Created on Dec 13, 2004
 */
package impl.jena;

import impl.owl.OWLObjectImpl;

import java.net.URI;

import org.mindswap.owl.OWLDataType;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owl.OWLType;
import org.mindswap.utils.URIUtils;

import com.hp.hpl.jena.datatypes.DatatypeFormatException;
import com.hp.hpl.jena.datatypes.RDFDatatype;

/**
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:51 $
 */
public class OWLDataTypeImpl extends OWLObjectImpl implements OWLDataType
{
	private final OWLKnowledgeBase kb;
	private final RDFDatatype datatype;
	private final URI datatypeURI;

	public OWLDataTypeImpl(final OWLKnowledgeBase kb, final RDFDatatype datatype)
	{
		super();
		if (datatype == null) throw new IllegalArgumentException("RDF datatype is null");
		this.datatype = datatype;
		this.datatypeURI = URIUtils.createURI(datatype.getURI());
		this.kb = kb;
	}

	/* @see org.mindswap.owl.OWLObject#getImplementation() */
	public RDFDatatype getImplementation()
	{
		return datatype;
	}

	/* @see org.mindswap.owl.OWLType#isDataType() */
	public final boolean isDataType() {
		return true;
	}

	/* @see org.mindswap.owl.OWLType#isClass() */
	public final boolean isClass() {
		return false;
	}

	/* @see org.mindswap.owl.OWLType#getURI() */
	public URI getURI() {
		return datatypeURI;
	}

	/* @see impl.owl.OWLObjectImpl#equals(java.lang.Object) */
	@Override
	public boolean equals(final Object object)
	{
		if (object instanceof OWLDataTypeImpl)
		{
			return datatype.equals(((OWLDataTypeImpl) object).datatype);
		}
		return false;
	}

	/* @see impl.owl.OWLObjectImpl#hashCode() */
	@Override
	public int hashCode()
	{
		return datatype.hashCode();
	}

	/* @see org.mindswap.owl.OWLType#isDisjointWith(org.mindswap.owl.OWLType) */
	public boolean isDisjointWith(final OWLType type)
	{
		return kb.isDisjoint(this, type);
	}

	/* @see org.mindswap.owl.OWLType#isSubTypeOf(org.mindswap.owl.OWLType) */
	public boolean isSubTypeOf(final OWLType type) {
		return kb.isSubTypeOf(this, type);
	}

	/* @see org.mindswap.owl.OWLType#isEquivalentTo(org.mindswap.owl.OWLType) */
	public boolean isEquivalentTo(final OWLType type) {
		return kb.isEquivalent(this, type);
	}

	/* @see java.lang.Object#toString() */
	@Override
	public String toString()
	{
		return datatype.toString();
	}

	/* @see org.mindswap.owl.OWLDataType#isValid(java.lang.String) */
	public boolean isValid(final String lexicalForm)
	{
		if (datatype != null) return datatype.isValid(lexicalForm);
		throw new UnsupportedOperationException("Datatype without validation support.");
	}

	/* @see org.mindswap.owl.OWLDataType#isValidValue(java.lang.Object) */
	public boolean isValidValue(final Object valueForm)
	{
		if (datatype != null) return datatype.isValidValue(valueForm);
		throw new UnsupportedOperationException("Datatype without validation support.");
	}

	/* @see org.mindswap.owl.OWLDataType#parse(java.lang.String) */
	public Object parse(final String lexicalForm)
	{
		try
		{
			if (datatype != null) return datatype.parse(lexicalForm);
			throw new UnsupportedOperationException("Datatype without parse support.");
		}
		catch (DatatypeFormatException e)
		{
			throw new IllegalArgumentException(e);
		}
	}

	/* @see org.mindswap.owl.OWLDataType#unparse(java.lang.Object) */
	public String unparse(final Object value)
	{
		if (datatype != null) return datatype.unparse(value);
		throw new UnsupportedOperationException("Datatype without parse support.");
	}
}
