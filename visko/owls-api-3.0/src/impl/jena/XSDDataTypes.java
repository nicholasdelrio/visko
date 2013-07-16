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
 * Created on Dec 14, 2004
 */
package impl.jena;

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.mindswap.owl.OWLDataType;
import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owl.vocabulary.XSD;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;

/**
 *
 * @author unascribed
 * @see <a href="http://www.w3.org/TR/xmlschema-2/">XML Schema Part 2: Datatypes Second Edition</a>
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:51 $
 */
class XSDDataTypes
{

	private static final Map<URI, OWLDataType> datatypes;

	static
	{
		final Map<URI, OWLDataType> tmp = new HashMap<URI, OWLDataType>();
		// this is a dummy KB
		final OWLKnowledgeBase kb = OWLFactory.createKB();

		// register built-in primitive types
		tmp.put(XSD.decimal, new OWLDataTypeImpl(kb, XSDDatatype.XSDdecimal));
		tmp.put(XSD.xsdString, new OWLDataTypeImpl(kb, XSDDatatype.XSDstring));
		tmp.put(XSD.xsdBoolean, new OWLDataTypeImpl(kb, XSDDatatype.XSDboolean));
		tmp.put(XSD.xsdFloat, new OWLDataTypeImpl(kb, XSDDatatype.XSDfloat));
		tmp.put(XSD.xsdDouble, new OWLDataTypeImpl(kb, XSDDatatype.XSDdouble));
		tmp.put(XSD.dateTime, new OWLDataTypeImpl(kb, XSDDatatype.XSDdateTime));
		tmp.put(XSD.date, new OWLDataTypeImpl(kb, XSDDatatype.XSDdate));
		tmp.put(XSD.time, new OWLDataTypeImpl(kb, XSDDatatype.XSDtime));
		tmp.put(XSD.gYear, new OWLDataTypeImpl(kb, XSDDatatype.XSDgYear));
		tmp.put(XSD.gMonth, new OWLDataTypeImpl(kb, XSDDatatype.XSDgMonth));
		tmp.put(XSD.gDay, new OWLDataTypeImpl(kb, XSDDatatype.XSDgDay));
		tmp.put(XSD.gYearMonth, new OWLDataTypeImpl(kb, XSDDatatype.XSDgYearMonth));
		tmp.put(XSD.gMonthDay, new OWLDataTypeImpl(kb, XSDDatatype.XSDgMonthDay));
		tmp.put(XSD.duration, new OWLDataTypeImpl(kb, XSDDatatype.XSDduration));
		tmp.put(XSD.anyURI, new OWLDataTypeImpl(kb, XSDDatatype.XSDanyURI));

		// register built-in derived types
		tmp.put(XSD.integer, new OWLDataTypeImpl(kb, XSDDatatype.XSDinteger));
		tmp.put(XSD.nonPositiveInteger, new OWLDataTypeImpl(kb, XSDDatatype.XSDnonPositiveInteger));
		tmp.put(XSD.negativeInteger, new OWLDataTypeImpl(kb, XSDDatatype.XSDnegativeInteger));
		tmp.put(XSD.nonNegativeInteger, new OWLDataTypeImpl(kb, XSDDatatype.XSDnonNegativeInteger));
		tmp.put(XSD.positiveInteger, new OWLDataTypeImpl(kb, XSDDatatype.XSDpositiveInteger));
		tmp.put(XSD.xsdLong, new OWLDataTypeImpl(kb, XSDDatatype.XSDlong));
		tmp.put(XSD.xsdInt, new OWLDataTypeImpl(kb, XSDDatatype.XSDint));
		tmp.put(XSD.xsdShort, new OWLDataTypeImpl(kb, XSDDatatype.XSDshort));
		tmp.put(XSD.xsdByte, new OWLDataTypeImpl(kb, XSDDatatype.XSDbyte));
		tmp.put(XSD.unsignedByte, new OWLDataTypeImpl(kb, XSDDatatype.XSDunsignedByte));
		tmp.put(XSD.unsignedShort, new OWLDataTypeImpl(kb, XSDDatatype.XSDunsignedShort));
		tmp.put(XSD.unsignedInt, new OWLDataTypeImpl(kb, XSDDatatype.XSDunsignedInt));
		tmp.put(XSD.unsignedLong, new OWLDataTypeImpl(kb, XSDDatatype.XSDunsignedLong));

		tmp.put(XSD.normalizedString, new OWLDataTypeImpl(kb, XSDDatatype.XSDnormalizedString));
		tmp.put(XSD.token, new OWLDataTypeImpl(kb, XSDDatatype.XSDtoken));
		tmp.put(XSD.language, new OWLDataTypeImpl(kb, XSDDatatype.XSDlanguage));
		tmp.put(XSD.NMTOKEN, new OWLDataTypeImpl(kb, XSDDatatype.XSDNMTOKEN));
		tmp.put(XSD.Name, new OWLDataTypeImpl(kb, XSDDatatype.XSDName));
		tmp.put(XSD.NCName, new OWLDataTypeImpl(kb, XSDDatatype.XSDNCName));

		datatypes = Collections.unmodifiableMap(tmp);
	}

	static final Set<OWLDataType> getDataTypes()
	{
		return new HashSet<OWLDataType>(datatypes.values());
	}

	static final Map<URI, OWLDataType> getDataTypeMap()
	{
		return datatypes;
	}

	static final OWLDataType getDataType(final URI uri)
	{
		return datatypes.get(uri);
	}

}
