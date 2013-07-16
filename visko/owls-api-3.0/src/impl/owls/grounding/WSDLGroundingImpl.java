/*
 * Created 12.02.2009
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
package impl.owls.grounding;

import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owls.grounding.WSDLAtomicGrounding;
import org.mindswap.owls.grounding.WSDLGrounding;
import org.mindswap.owls.process.AtomicProcess;

/**
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:52 $
 */
public class WSDLGroundingImpl extends GroundingImpl<WSDLAtomicGrounding, String> implements WSDLGrounding
{

	/**
	 * @see GroundingImpl#GroundingImpl(OWLIndividual)
	 */
	public WSDLGroundingImpl(final OWLIndividual ind)
	{
		super(ind);
	}

	/* @see org.mindswap.owls.grounding.Grounding#getAtomicGrounding(org.mindswap.owls.process.AtomicProcess) */
	public WSDLAtomicGrounding getAtomicGrounding(final AtomicProcess process)
	{
		return getAtomicGrounding(process, WSDLAtomicGrounding.class);
	}

	/* @see org.mindswap.owls.grounding.Grounding#getAtomicGroundings() */
	public OWLIndividualList<WSDLAtomicGrounding> getAtomicGroundings()
	{
		return getAtomicGroundings(WSDLAtomicGrounding.class);
	}

	/* @see org.mindswap.owls.grounding.Grounding#getAtomicGroundings(org.mindswap.owls.process.AtomicProcess) */
	public OWLIndividualList<WSDLAtomicGrounding> getAtomicGroundings(final AtomicProcess process)
	{
		return getAtomicGroundings(process, WSDLAtomicGrounding.class);
	}

}
