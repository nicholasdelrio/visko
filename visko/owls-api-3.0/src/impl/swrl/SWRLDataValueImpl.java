/*
 * Created on Dec 28, 2004
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
package impl.swrl;

import impl.owl.OWLObjectImpl;

import java.net.URI;

import org.mindswap.owl.OWLDataValue;
import org.mindswap.swrl.SWRLDataValue;

/**
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:53 $
 */
public class SWRLDataValueImpl extends OWLObjectImpl implements OWLDataValue, SWRLDataValue
{
	private final OWLDataValue dataValue;

	public SWRLDataValueImpl(final OWLDataValue dataValue)
	{
		this.dataValue = dataValue;

		setNextView(dataValue.getNextView());
		dataValue.setNextView(this);
	}

	/* @see org.mindswap.owl.OWLDataValue#getDatatypeURI() */
	public URI getDatatypeURI()
	{
		return dataValue.getDatatypeURI();
	}

	/* @see org.mindswap.owl.OWLDataValue#getLanguage() */
	public String getLanguage()
	{
		return dataValue.getLanguage();
	}

	/* @see org.mindswap.owl.OWLDataValue#getLexicalValue() */
	public String getLexicalValue()
	{
		return dataValue.getLexicalValue();
	}

	/* @see org.mindswap.owl.OWLDataValue#getValue() */
	public Object getValue()
	{
		return dataValue.getValue();
	}

	/* @see org.mindswap.owl.OWLValue#isDataValue() */
	public final boolean isDataValue()
	{
		return true;
	}

	/* @see org.mindswap.owl.OWLValue#isIndividual() */
	public final boolean isIndividual()
	{
		return false;
	}

	/* @see org.mindswap.owl.OWLObject#getImplementation() */
	public Object getImplementation()
	{
		return dataValue.getImplementation();
	}

	/* @see org.mindswap.swrl.SWRLObject#isVariable() */
	public final boolean isVariable()
	{
		return false;
	}

	/* @see impl.owl.OWLObjectImpl#toString() */
	@Override
	public String toString()
	{
		return dataValue.toString();
	}
}
