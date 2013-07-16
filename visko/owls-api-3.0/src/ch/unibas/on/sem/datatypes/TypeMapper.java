/*
 * Created on 28.02.2008
 *
 * (c) 2008 Thorsten Möller - University of Basel Switzerland
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
package ch.unibas.on.sem.datatypes;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.hp.hpl.jena.datatypes.RDFDatatype;

/**
 * The reason for this class is that we need to add and overwrite some
 * type mappings which do not exist in the super class
 * {@link com.hp.hpl.jena.datatypes.TypeMapper}. Provided that such this will
 * be changed in the future this class becomes obsolete.
 *
 * @author unascribed
 * @version $Rev: 2350 $; $Author: nick $; $Date: 2011/01/24 06:37:55 $
 */
public class TypeMapper extends com.hp.hpl.jena.datatypes.TypeMapper
{
	private static volatile TypeMapper instance = new TypeMapper();

	private final Map<Class<?>, RDFDatatype> classToDTAdditional;

	private TypeMapper()
	{
		super();
		classToDTAdditional = new HashMap<Class<?>, RDFDatatype>(3);

      // overwrite dateTime --> map to Date (super class maps dateTime to special implementation)
      registerDatatype(new XSDDateTimeType("dateTime", Calendar.class));
      classToDTAdditional.put(Date.class, new XSDDateTimeType("dateTime", Date.class)); // add mapping for Calendar class
	}

	/* @see com.hp.hpl.jena.datatypes.TypeMapper#getTypeByClass(java.lang.Class) */
	@Override
	public RDFDatatype getTypeByClass(final Class<?> c)
	{
		final RDFDatatype dt = super.getTypeByClass(c);
		return (dt != null)? dt : classToDTAdditional.get(c);
	}

	/**
	 * @return The singleton instance of this class.
	 */
	public static TypeMapper getInstance()
	{
		return instance;
	}

	private static final class XSDDateTimeType extends com.hp.hpl.jena.datatypes.xsd.impl.XSDDateTimeType
	{
		XSDDateTimeType(final String xsdName, final Class<?> javaClass)
		{
			super(xsdName);
			this.javaClass = javaClass;
		}
	}

}
