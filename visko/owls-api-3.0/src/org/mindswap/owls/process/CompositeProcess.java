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
 * Created on Dec 27, 2003
 */
package org.mindswap.owls.process;

import java.net.URI;

import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owl.OWLType;
import org.mindswap.owls.expression.Expression;
import org.mindswap.owls.process.variable.Binding;
import org.mindswap.owls.process.variable.Link;
import org.mindswap.owls.process.variable.Loc;
import org.mindswap.owls.process.variable.Local;

/**
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:52 $
 */
public interface CompositeProcess extends Process
{
	public void addLocal(Local local);

	public void addLocals(OWLIndividualList<? extends Local> locals);

	/**
	 * @param uri The URI for the local variable, or <code>null</code> to create
	 * 	an	anonymous instance.
	 * @param varType The OWL class or datatype of the local variable. Can be
	 * 	<code>null</code> to create an untyped variable.
	 * @return The newly created local variable, associated with this process.
	 */
	public Loc createLoc(URI uri, OWLType varType);

	/**
	 * @param uri The URI for the link variable, or <code>null</code> to create
	 * 	an	anonymous instance.
	 * @param varType The OWL class or datatype of the local variable. Can be
	 * 	<code>null</code> to create an untyped variable.
	 * @return The newly created link variable, associated with this process.
	 */
	public Link createLink(URI uri, OWLType varType);

	/**
	 * @return The first local variable found or <code>null</code> if there is no none.
	 */
	public Local getLocal();

	public Local getLocal(String localName);

	public OWLIndividualList<Local> getLocals();

	/**
	 * Removes the given local by breaking the property <code>process:hasLocal</code>
	 * The local itself is not touched at all.
	 *
	 * @param local The local to remove. A value of <code>null</code> will remove
	 * 	all local variable declarations.
	 */
	public void removeLocal(Local local);

	/**
	 * Returns the control construct of which the composite process is composed of.
	 * The <tt>process:composedOf</tt> property is read to retrieve the correct
	 * construct.
	 *
	 * @return The control construct this composite process is composed of.
	 */
	public ControlConstruct getComposedOf();

	/**
	 * Sets the control construct of which the composite process is composed of.
	 * The <code>process:composedOf</code> property is written to set the correct
	 * construct.
	 *
	 * @param cc The control construct this composite process is composed of.
	 */
	public void setComposedOf(ControlConstruct cc);

	/**
	 * Removes the Control Construct from the composite process by breaking the
	 * link <tt>process:composedOf</tt>. The control construct itself remains
	 * untouched.
	 */
	public void removeComposedOf();

	/**
	 * Returns all data flow bindings within this composite process. It does not
	 * distinguish whether the flow is coming from outside into the process or
	 * the flow is completely embedded into the process or the flow goes from
	 * the process out to some outside perform.
	 *
	 * @return A list of bindings that have at least their source or sink in
	 * 	this process.
	 */
	public OWLIndividualList<Binding<?>> getAllBindings();

	/**
	 * Invocable is a flag that tells whether this composite process bottoms
	 * out in atomic processes. If so, it is "invocable", as soon as there is
	 * a {@link SimpleProcess} it is no longer invocable.
	 * <p>
	 * Note that this flag should not be mixed up with whether a composite
	 * process is executable as simple processes are indirectly executable.
	 * <p>
	 * Note that OWL-S has a dedicated property to make this information
	 * explicit. However, implementations should rather compute the boolean
	 * value dynamically by traversing the entire control constructs hierarchy
	 * and check if leave nodes are atomic processes; i.e., consider this
	 * property as synthetic. (This is also the reason why there is no setter
	 * method for this property.)
	 *
	 * @return <code>true</code> if this composite process bottoms out in
	 * 	atomic processes.
	 */
	public boolean isInvocable();

	/* The following computed* properties are typed to Expression even though
	 * OWL-S declares the range of them to owl:Thing. This seems to be reasonable
	 * since OWL-S and this API already has the notion of a (logic) expression. */

	public Expression<?> getComputedInput();
	public void setComputedInput(Expression<?> expression);

	public Expression<?> getComputedOutput();
	public void setComputedOutput(Expression<?> expression);

	public Expression<?> getComputedPrecondition();
	public void setComputedPrecondition(Expression<?> expression);

	public Expression<?> getComputedEffect();
	public void setComputedEffect(Expression<?> expression);
}
