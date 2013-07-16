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

/*
 * Created on Jan 4, 2005
 */
package org.mindswap.owls.process;

import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owls.process.variable.Link;
import org.mindswap.owls.process.variable.LinkBinding;
import org.mindswap.owls.process.variable.Loc;
import org.mindswap.owls.process.variable.Output;
import org.mindswap.owls.process.variable.OutputBinding;
import org.mindswap.owls.process.variable.Parameter;
import org.mindswap.owls.process.variable.ParameterValue;

/**
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:52 $
 */
public interface Produce extends ControlConstruct
{
	public void addBinding(LinkBinding binding);

	public void addBinding(OutputBinding binding);

	public void addBinding(Link targetLink, ParameterValue sourceParamValue);

	public void addBinding(Link targetLink, Perform sourcePerform, Parameter sourceParam);

	public void addBinding(Link targetLink, Loc sourceLocal);

	public void addBinding(Output targetOutput, ParameterValue sourceParamValue);

	public void addBinding(Output targetOutput, Perform sourcePerform, Parameter sourceParam);

	public void addBinding(Output targetOutput, Loc sourceLocal);

	public LinkBinding getBindingFor(Link link);

	public OutputBinding getBindingFor(Output output);

	public OWLIndividualList<LinkBinding> getLinkBindings();

	public OWLIndividualList<OutputBinding> getOutputBindings();

	public void removeBinding(LinkBinding binding);

	public void removeBinding(OutputBinding binding);

	public void removeBindings();
}
