/*
 * Created 26.10.2008
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
package impl.owls.grounding;

import impl.owl.WrappedIndividual;

import java.net.URI;

import org.mindswap.owl.OWLDataProperty;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLObjectProperty;
import org.mindswap.owls.grounding.MessageMap;
import org.mindswap.owls.process.variable.Parameter;
import org.mindswap.utils.URIUtils;

/**
 *
 * @author unascribed
 * @version $Rev:$; $Author: nick $; $Date: 2011/01/24 06:37:52 $
 */
public abstract class BaseMessageMap<T> extends WrappedIndividual implements MessageMap<T>
{

	public BaseMessageMap(final OWLIndividual ind)
	{
		super(ind);
	}

	/* @see org.mindswap.owls.grounding.MessageMap#getGroundingParameter() */
	public String getGroundingParameter()
	{
		return getPropertyAsString(groundingParameterProperty());
	}

	/* @see org.mindswap.owls.grounding.MessageMap#setGroundingParameter(java.lang.String) */
	public void setGroundingParameter(final String param)
	{
		setProperty(groundingParameterProperty(), param);
	}

	/* @see org.mindswap.owls.grounding.MessageMap#getGroundingParameterAsURI() */
	public URI getGroundingParameterAsURI()
	{
		return URIUtils.createURI(getGroundingParameter());
	}

	/* @see org.mindswap.owls.grounding.MessageMap#getOWLSParameter() */
	public Parameter getOWLSParameter()
	{
		return getPropertyAs(owlsParameterProperty(), Parameter.class);
	}

	/* @see org.mindswap.owls.grounding.MessageMap#setOWLSParameter(org.mindswap.owls.process.Parameter) */
	public void setOWLSParameter(final Parameter param)
	{
		setProperty(owlsParameterProperty(), param);
	}

	/* @see impl.owl.WrappedIndividual#toString() */
	@Override
	public String toString()
	{
		return "[" + getOWLSParameter() + " -> " + getGroundingParameter() + "]";
	}

	protected abstract OWLDataProperty groundingParameterProperty();
	protected abstract OWLObjectProperty owlsParameterProperty();

}
