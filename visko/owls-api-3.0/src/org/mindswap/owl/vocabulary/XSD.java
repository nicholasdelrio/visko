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
 * Created on Dec 28, 2003
 */
package org.mindswap.owl.vocabulary;

import java.net.URI;

/**
 *
 * @author unascribed
 * @see <a href="http://www.w3.org/TR/xmlschema-2/">XML Schema Part 2: Datatypes Second Edition</a>
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:53 $
 */
public class XSD
{
	public final static String ns = "http://www.w3.org/2001/XMLSchema#";

	public final static URI getURI() { return URI.create(ns); }

	/** URI URI for xsd:float */
	public static final URI xsdFloat = URI.create(ns + "float");

	/** URI URI for xsd:double */
	public static final URI xsdDouble = URI.create(ns + "double");

	/** URI URI for xsd:int */
	public static final URI xsdInt = URI.create(ns + "int");

	/** URI URI for xsd:long */
	public static final URI xsdLong = URI.create(ns + "long");

	/** URI URI for xsd:short */
	public static final URI xsdShort = URI.create(ns + "short");

	/** URI URI for xsd:byte */
	public static final URI xsdByte = URI.create(ns + "byte");

	/** URI URI for xsd:boolean */
	public static final URI xsdBoolean = URI.create(ns + "boolean");

	/** URI URI for xsd:string */
	public static final URI xsdString = URI.create(ns + "string");

	/** URI URI for xsd:string */
	public static final URI string = URI.create(ns + "string");

	/** URI URI for xsd:unsignedByte */
	public static final URI unsignedByte = URI.create(ns + "unsignedByte");

	/** URI URI for xsd:unsignedShort */
	public static final URI unsignedShort = URI.create(ns + "unsignedShort");

	/** URI URI for xsd:unsignedInt */
	public static final URI unsignedInt = URI.create(ns + "unsignedInt");

	/** URI URI for xsd:unsignedLong */
	public static final URI unsignedLong = URI.create(ns + "unsignedLong");

	/** URI URI for xsd:decimal */
	public static final URI decimal = URI.create(ns + "decimal");

	/** URI URI for xsd:integer */
	public static final URI integer = URI.create(ns + "integer");

	/** URI URI for xsd:nonPositiveInteger */
	public static final URI nonPositiveInteger = URI.create(ns + "nonPositiveInteger");

	/** URI URI for xsd:nonNegativeInteger */
	public static final URI nonNegativeInteger = URI.create(ns + "nonNegativeInteger");

	/** URI URI for xsd:positiveInteger */
	public static final URI positiveInteger = URI.create(ns + "positiveInteger");

	/** URI URI for xsd:negativeInteger */
	public static final URI negativeInteger = URI.create(ns + "negativeInteger");

	/** URI URI for xsd:normalizedString */
	public static final URI normalizedString = URI.create(ns + "normalizedString");

	/** URI URI for xsd:anyURI */
	public static final URI anyURI = URI.create(ns + "anyURI");

	/** URI URI for xsd:token */
	public static final URI token = URI.create(ns + "token");

	/** URI URI for xsd:Name */
	public static final URI Name = URI.create(ns + "Name");

	/** URI URI for xsd:QName */
	public static final URI QName = URI.create(ns + "QName");

	/** URI URI for xsd:language */
	public static final URI language = URI.create(ns + "language");

	/** URI URI for xsd:NMTOKEN */
	public static final URI NMTOKEN = URI.create(ns + "NMTOKEN");

	/** URI URI for xsd:ENTITIES */
	public static final URI ENTITIES = URI.create(ns + "ENTITIES");

	/** URI URI for xsd:NMTOKENS */
	public static final URI NMTOKENS = URI.create(ns + "NMTOKENS");

	/** URI URI for xsd:ENTITY */
	public static final URI ENTITY = URI.create(ns + "ENTITY");

	/** URI URI for xsd:ID */
	public static final URI ID = URI.create(ns + "ID");

	/** URI URI for xsd:NCName */
	public static final URI NCName = URI.create(ns + "NCName");

	/** URI URI for xsd:IDREF */
	public static final URI IDREF = URI.create(ns + "IDREF");

	/** URI URI for xsd:IDREFS */
	public static final URI IDREFS = URI.create(ns + "IDREFS");

	/** URI URI for xsd:NOTATION */
	public static final URI NOTATION = URI.create(ns + "NOTATION");

	/** URI URI for xsd:hexBinary */
	public static final URI hexBinary = URI.create(ns + "hexBinary");

	/** URI URI for xsd:base64Binary */
	public static final URI base64Binary = URI.create(ns + "base64Binary");

	/** URI URI for xsd:date */
	public static final URI date = URI.create(ns + "date");

	/** URI URI for xsd:time */
	public static final URI time = URI.create(ns + "time");

	/** URI URI for xsd:dateTime */
	public static final URI dateTime = URI.create(ns + "dateTime");

	/** URI URI for xsd:duration */
	public static final URI duration = URI.create(ns + "duration");

	/** URI URI for xsd:gDay */
	public static final URI gDay = URI.create(ns + "gDay");

	/** URI URI for xsd:gMonth */
	public static final URI gMonth = URI.create(ns + "gMonth");

	/** URI URI for xsd:gYear */
	public static final URI gYear = URI.create(ns + "gYear");

	/** URI URI for xsd:gYearMonth */
	public static final URI gYearMonth = URI.create(ns + "gYearMonth");

	/** URI URI for xsd:gMonthDay */
	public static final URI gMonthDay = URI.create(ns + "gMonthDay");

}
