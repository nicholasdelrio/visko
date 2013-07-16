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

package org.mindswap.wsdl;

import javax.xml.namespace.QName;

/**
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:54 $
 */
public class WSDLParameter
{
	private String name;
	private QName type;
	private Object value;
	private String textValue;

	public WSDLParameter(final String name, final QName type)
	{
		this(name, type, null);
	}

	public WSDLParameter(final String name, final QName type, final Object value)
	{
		setName(name);
		setType(type);
		setValue(value);
	}

	/* @see java.lang.Object#toString() */
	@Override
	public String toString()
	{
		return name;
	}

	private void setName(final String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	private void setType(final QName type)
	{
		this.type = type;
	}

	public QName getType()
	{
		return type;
	}

	public void setValue(final Object value)
	{
		this.value = value;
	}

	public Object getValue()
	{
		return value;
	}

	public void setTextValue(final String textValue)
	{
		this.textValue = textValue;
	}

	public String getTextValue()
	{
		return textValue;
	}

	/* @see java.lang.Object#equals(java.lang.Object) */
	@Override
	public boolean equals(final Object o)
	{
		if (o instanceof WSDLParameter) return name.equals(((WSDLParameter) o).getName());

		return false;
	}

	/* @see java.lang.Object#hashCode() */
	@Override
	public int hashCode()
	{
		assert false : "hashCode not designed"; // TODO implement if required
		return 42; // any arbitrary constant will do
	}
}
