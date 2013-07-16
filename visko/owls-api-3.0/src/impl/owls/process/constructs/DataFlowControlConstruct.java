/*
 * Created 28.06.2009
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
package impl.owls.process.constructs;

import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owls.process.Perform;
import org.mindswap.owls.process.variable.Binding;
import org.mindswap.owls.process.variable.Loc;
import org.mindswap.owls.process.variable.Parameter;
import org.mindswap.owls.process.variable.ProcessVar;
import org.mindswap.owls.process.variable.ValueOf;

/**
 * Just another layer of abstraction used to extract often occurring data flow
 * related functionality from sub classes.
 *
 * @author unascribed
 * @version $Rev: 2319 $; $Author: nick $; $Date: 2011/01/24 06:37:51 $
 */
public abstract class DataFlowControlConstruct<C extends DataFlowControlConstruct<C>>
	extends ControlConstructImpl<C>
{
	public DataFlowControlConstruct(final OWLIndividual ind)
	{
		super(ind);
	}

	protected ValueOf createValueOf(final Perform sourcePerform, final Parameter sourceParam)
	{
		final ValueOf valueOf = getOntology().createValueOf(null);
		valueOf.setSource(sourceParam, sourcePerform);
		return valueOf;
	}

	protected ValueOf createValueOf(final Loc sourceLocal)
	{
		final ValueOf valueOf = getOntology().createValueOf(null);
		valueOf.setSource(sourceLocal);
		return valueOf;
	}

	protected <B extends Binding<V>, V extends ProcessVar> B getBindingFor(
		final OWLIndividualList<B> bindings, final V procVar)
	{
		for (final B binding : bindings)
		{
			if (binding.getProcessVar().equals(procVar))
			{
				return binding;
			}
		}
		return null;
	}

}
