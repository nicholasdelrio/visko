/*
 * Created 09.07.2009
 *
 * (c) 2009 Thorsten Möller - University of Basel Switzerland
 *
 * The MIT License
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 */
package org.mindswap.query;

/**
 * Enumeration of standard query languages (supported by this API).
 *
 * @author unascribed
 * @version $Rev: 2350 $; $Author: nick $; $Date: 2011/01/24 06:37:52 $
 */
public enum QueryLanguage
{
	/** Represents so called ABox queries. */
	ABox,

	/**
	 * Represents the RDQL query language.
	 * @deprecated Use SPARQL instead.
	 */
	@Deprecated RDQL,

	/** Represents the <a href="http://www.w3.org/TR/rdf-sparql-query/">SPARQL</a> query language. */
	SPARQL,

	/**
	 * Represents the <a href="http://clarkparsia.com/files/pdf/sparqldl.pdf">SPARQL-DL</a>
	 * query language. Note that SPARQL-DL has the same syntax as SPARQL. As
	 * such, every SPARQL query is syntactically a SPARQL-DL query and vice
	 * versa. However, SPARQL-DL extends SPARQL by the possibility to use
	 * additional constraints, whose formal semantics do not fit in the
	 * RDF/RDFS framework but require DL based interpretation.
	 */
	SPARQL_DL
}
