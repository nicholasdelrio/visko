/*
 * Created on Oct 31, 2004
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
package impl.owls.process.binding;

import org.mindswap.owl.OWLIndividual;
import org.mindswap.owls.process.variable.Input;
import org.mindswap.owls.process.variable.InputBinding;
import org.mindswap.owls.vocabulary.OWLS;

/**
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:53 $
 */
public class InputBindingImpl extends BindingImpl<Input> implements InputBinding
{
	public InputBindingImpl(final OWLIndividual ind)
	{
		super(ind);
	}

	/* @see org.mindswap.owls.process.Binding#getParameter() */
	public Input getProcessVar()
	{
		return getPropertyAs(OWLS.Process.toVar, Input.class);
	}

	/* @see org.mindswap.owls.process.Binding#setParameter(org.mindswap.owls.process.Parameter) */
	public Input setProcessVar(final Input param)
	{
		return setProcessVariable(param);
	}

	/* @see impl.owls.process.binding.BindingImpl#toString() */
	@Override
	public String toString()
	{
		return "Input binding: " + super.toString();
	}
}
