/*
 * Created on Jul 7, 2004
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
package org.mindswap.owls.expression;

import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owl.list.OWLList;
import org.mindswap.owls.io.ProcessWriter;
import org.mindswap.owls.vocabulary.OWLS;
import org.mindswap.query.ValueMap;
import org.mindswap.swrl.Atom;

/**
 *
 * @param <B> The abstraction of the expression body. For unquoted expressions
 * 	this can be any kind of {@link OWLIndividual}, whereas for
 * 	{@link QuotedExpression quoted expressions} it is a literal whose syntax
 * 	and semantics depend on the actual type of quoted expression.
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:54 $
 */
public interface Expression<B> extends OWLIndividual
{
	/**
	 * Evaluate this expression in its {@link #getKB() KB}. Depending on the
	 * actual expression this can result in adding new statements or removing
	 * existing statements. However, this kind of modifications to the KB may
	 * not happen with every type of expression. For instance, expressions (such
	 * as a read query) that do not represent a function, or rule whose evaluation
	 * yields <em>new</em> statements which were not existing before.
	 *
	 * @param bindings A map of initial bindings possibly binding some of the
	 * 	variables used in this expression. Parameter can be <code>null</code>
	 * 	to evaluate this expression without the attempt to bind variables.
	 */
	public void evaluate(ValueMap<?, ?> bindings);

	/**
	 * @return The language of this expression (immutable).
	 */
	public LogicLanguage getLanguage();

	public B getBody();
	public void setBody(B body);

	public void writeTo(ProcessWriter writer, String indent);

	public interface QuotedExpression<B> extends Expression<B>
	{
		/**
		 * @param varBinding The variable binding to add.
		 */
		public void addVariableBinding(VariableBinding varBinding);

		/**
		 * @return The first variable binding found or <code>null</code> if there is no none.
		 */
		public VariableBinding getVariableBinding();

		/**
		 * Get the variable binding for which the local name of the
		 * {@link VariableBinding#getTheObject() object} equals the given name.
		 *
		 * @param localName The local name to match.
		 * @return The corresponding variable binding, or <code>null</code> if
		 * 	none was found for the given local name.
		 */
		public VariableBinding getVariableBinding(String localName);

		/**
		 * @return All variable bindings, or an empty list if there are none.
		 */
		public OWLIndividualList<VariableBinding> getVariableBindings();

		/**
		 * Remove some variable binding from this expression by breaking the
		 * relation {@link OWLS.Expression#variableBinding}. The variable
		 * binding itself remains in the backing model.
		 *
		 * @param varBinding The variable binding to remove from this expression.
		 */
		public void removeVariableBinding(VariableBinding varBinding);

		/**
		 * Remove all variable bindings (if any) from this expression by breaking
		 * the relation {@link OWLS.Expression#variableBinding}. The variable
		 * bindings itself remains in the backing model.
		 */
		public void removeVariableBindings();
	}

	/**
	 * A SWRL expression. The body of such expressions consists of a list of
	 * {@link Atom SWRL atoms}.
	 * <p>
	 * Evaluation of SWRL expressions is done by evaluating each Atom in the
	 * body as defined by its semantics. See {@link Atom#evaluate(ValueMap)}
	 * of each type of Atom for details.
	 */
	public interface SWRL extends Expression<OWLList<Atom>> {} // body unquoted

	/**
	 * A SPARQL expression. The body of such expressions is a SPARQL query string.
	 * <p>
	 * Evaluation of SPARQL queries is only supported for CONSTRUCT queries. Its
	 * result will be applied to the backing KB. All other query types (ASK,
	 * DESCRIBE, and SELECT) will be ignored for evaluation.
	 */
	public interface SPARQL extends QuotedExpression<String> {} // body quoted
}
