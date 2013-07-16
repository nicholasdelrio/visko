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
package impl.owls.grounding;

import org.mindswap.owl.OWLDataProperty;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLObjectProperty;
import org.mindswap.owls.grounding.MessageMap.StringMessageMap;
import org.mindswap.owls.vocabulary.FLAServiceOnt;
import org.mindswap.owls.vocabulary.OWLS;

/**
 *
 * @author unascribed
 * @version $Rev: 2333 $; $Author: nick $; $Date: 2011/01/24 06:37:52 $
 */
public class UPnPMessageMapImpl extends BaseMessageMap<String> implements StringMessageMap
{
	public UPnPMessageMapImpl(final OWLIndividual ind)
	{
		super(ind);
	}

	/* @see org.mindswap.owls.grounding.MessageMap#getTransformation() */
	public String getTransformation()
	{
	    return getPropertyAsString(OWLS.Grounding.xsltTransformationString);
	}

	/* @see org.mindswap.owls.grounding.MessageMap#setTransformation(java.lang.Object) */
	public void setTransformation(final String xsltTransformation)
	{
	    setProperty(OWLS.Grounding.xsltTransformationString, xsltTransformation);
	}

	@Override protected OWLDataProperty groundingParameterProperty() { return FLAServiceOnt.upnpParameter; }
	@Override protected OWLObjectProperty owlsParameterProperty() { return FLAServiceOnt.owlsParameter; }

}
