// The MIT License
//
// Copyright (c) 2007 University of Zürich Switzerland
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

import org.mindswap.owl.OWLDataValue;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owls.grounding.JavaParameter;
import org.mindswap.owls.vocabulary.MoreGroundings.Java;

/**
 *
 * @author unascribed
 * @version $Rev: 2298 $; $Author: nick $; $Date: 2011/01/24 06:37:52 $
 */
public class JavaParameterImpl extends JavaVariableImpl implements JavaParameter
{
	public JavaParameterImpl(final OWLIndividual ind)
	{
		super(ind);
	}

	/* @see org.mindswap.owls.grounding.JavaParameter#getParameterIndex() */
	public int getParameterIndex()
	{
		final OWLDataValue index = getProperty(Java.paramIndex);
		return (index != null)? ((Integer) index.getValue()).intValue() : -1;
	}

	/* @see org.mindswap.owls.grounding.JavaParameter#removeParameterIndex() */
	public void removeParameterIndex()
	{
		if (hasProperty(Java.paramIndex))
		{
			removeProperty(Java.paramIndex, null);
		}
	}

	/* @see org.mindswap.owls.grounding.JavaParameter#setParameterIndex(int) */
	public void setParameterIndex(final int index)
	{
		setProperty(Java.paramIndex, Integer.valueOf(index));
	}

}
