/*
 * Created 17.06.2009
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
package impl.owls.process.variable;

import org.mindswap.owl.OWLDataValue;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLType;
import org.mindswap.owl.OWLValue;
import org.mindswap.owls.process.variable.Loc;
import org.mindswap.owls.vocabulary.OWLS;
import org.mindswap.utils.RDFUtils;

/**
 *
 * @author unascribed
 * @version $Rev: 2287 $; $Author: nick $; $Date: 2011/01/24 06:37:53 $
 */
public class LocImpl extends LocalImpl implements Loc
{
	public LocImpl(final OWLIndividual ind)
	{
		super(ind);
	}

	/* @see org.mindswap.owls.process.variable.Loc#getInitialValue() */
	public OWLValue getInitialValue()
	{
		final OWLDataValue value = getProperty(OWLS.Process.initialValue);
		final OWLType type = getParamType();
		final OWLIndividual ind = getAsIndividual(value, type);

		//     ****parsing ok****   ****parsing failed****   **data value**
		return (ind != null)? ind : (type.isClass())? null : value;
	}

	/* @see org.mindswap.owls.process.variable.Loc#getInitialValueAsDataValue() */
	public OWLDataValue getInitialValueAsDataValue()
	{
		return getProperty(OWLS.Process.initialValue);
	}

	/* @see org.mindswap.owls.process.variable.Loc#getInitialValueAsIndividual() */
	public OWLIndividual getInitialValueAsIndividual()
	{
		return getAsIndividual(getProperty(OWLS.Process.initialValue), getParamType());
	}

	/* @see org.mindswap.owls.process.variable.Loc#removeInitialValue() */
	public void removeInitialValue()
	{
		removeProperty(OWLS.Process.initialValue, null);
	}

	/* @see org.mindswap.owls.process.variable.Loc#setInitialValue(org.mindswap.owl.OWLValue) */
	public void setInitialValue(final OWLValue value)
	{
		if (value.isDataValue()) setProperty(OWLS.Process.initialValue, value);
		else setProperty(OWLS.Process.initialValue, RDFUtils.toXMLLiteral((OWLIndividual) value));
	}

	private OWLIndividual getAsIndividual(final OWLDataValue value, final OWLType paramType)
	{
		if (value != null && paramType != null && paramType.isClass())
		{
			String literal = value.getLexicalValue().trim();
			literal = (literal.indexOf("rdf:RDF") == -1)? RDFUtils.addRDFTag(literal) : literal;
			return getOntology().parseLiteral(literal);
		}
		return null;
	}
}
