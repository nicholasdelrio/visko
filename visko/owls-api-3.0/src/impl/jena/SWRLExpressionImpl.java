/*
 * Created on Dec 28, 2004
 *
 * (c) 2004 Evren Sirin
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
package impl.jena;

import impl.owls.expression.ExpressionImpl;

import java.util.Collections;
import java.util.List;

import org.mindswap.common.ClosableIterator;
import org.mindswap.common.SingleClosableIterator;
import org.mindswap.common.Variable;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLModel;
import org.mindswap.owl.OWLValue;
import org.mindswap.owl.list.OWLList;
import org.mindswap.owls.expression.Condition;
import org.mindswap.owls.expression.LogicLanguage;
import org.mindswap.owls.io.ExpressionWriter;
import org.mindswap.owls.io.ProcessWriter;
import org.mindswap.owls.vocabulary.OWLS;
import org.mindswap.query.Query;
import org.mindswap.query.ValueMap;
import org.mindswap.swrl.Atom;

/**
 *
 * @author unascribed
 * @version $Rev: 2350 $; $Author: nick $; $Date: 2011/01/24 06:37:51 $
 */
public class SWRLExpressionImpl extends ExpressionImpl<OWLList<Atom>> implements Condition.SWRL
{
	private Query<?> query;

	/**
	 * @param ind The individual to wrap.
	 * @param setLanguage <code>false</code> if the backing ontology already
	 * 	contains a statement that asserts that the individual is a SWRL
	 * 	expression, i.e., the property {@link OWLS.Expression#expressionLanguage}
	 * 	is already set to refer to {@link OWLS.Expression#SWRL}.
	 */
	public SWRLExpressionImpl(final OWLIndividual ind, final boolean setLanguage)
	{
		super(ind, OWLS.Expression.SWRL, setLanguage);
	}

	/**
	 * In contrast to {@link #SWRLExpressionImpl(OWLIndividual, boolean)}, this
	 * constructor will set a property assertion {@link OWLS.Expression#expressionLanguage}
	 * to refer to {@link OWLS.Expression#SWRL} in any case.
	 *
	 * @param ind The individual to wrap.
	 */
	public SWRLExpressionImpl(final OWLIndividual ind)
	{
		super(ind, OWLS.Expression.SWRL, true);
	}

	/* @see org.mindswap.owls.expression.Expression#evaluate(org.mindswap.query.ValueMap) */
	public void evaluate(final ValueMap<?, ?> bindings)
	{
		for (Atom atom : getBody())
		{
			atom.evaluate(bindings);
		}
	}

	/* @see org.mindswap.owls.generic.expression.Expression#getLanguage() */
	public final LogicLanguage getLanguage()
	{
		// We can skip retrieving the language property from the model, because
		// instances can only be created in a way that ensures that this property
		// is set.
		return OWLS.Expression.SWRL;
	}

	/* @see org.mindswap.owls.generic.expression.Expression#getBody() */
	public OWLList<Atom> getBody()
	{
		// The fact that we search in ontology (and not its KB) is a deliberate
		// decision. We want to make sure that subsequent modifications of the
		// list will be written to our ontology (and not the base ontology of the
		// KB, which would be the case if we would search the entire KB). This
		// is a slight limitation as we assume all statements that make up the
		// atoms list to be part of our ontology.
		// Note that if (some) statements that make up the atoms list would be
		// part of a imported ontology of our ontology they would be found, but
		// modifications would be written to our ontology.
		OWLIndividual ind = getOntology().getProperty(this, OWLS.Expression.expressionObject);
		return (ind == null)? null : ind.castToList(org.mindswap.owl.vocabulary.SWRL.AtomListVocabulary);
	}

	/* @see org.mindswap.owls.generic.expression.Expression#setBody(org.mindswap.owl.list.OWLList) */
	public void setBody(final OWLList<Atom> body)
	{
		query = null;
		setUnquotedBody(body);
	}

	/* @see org.mindswap.owls.process.Condition#isTrue(org.mindswap.query.ValueMap) */
	public boolean isTrue(final ValueMap<?, ?> binding)
	{
		return isTrue(getKB(), binding);
	}

	/* @see org.mindswap.owls.process.Condition#isTrue(org.mindswap.owl.OWLModel, org.mindswap.query.ValueMap) */
	public boolean isTrue(final OWLModel model, final ValueMap<?, ?> binding)
	{
		final ClosableIterator<ValueMap<Variable, OWLValue>> solutions =
			solutions(model, binding, Collections.<Variable>emptyList());
		final boolean isTrue;
		try
		{
			isTrue = solutions.hasNext();
		}
		finally
		{
			solutions.close();
		}
		return isTrue;
	}

	/* @see org.mindswap.owls.expression.Condition#solutions(org.mindswap.query.ValueMap) */
	public <V extends Variable> ClosableIterator<ValueMap<V, OWLValue>> solutions(
		final ValueMap<?, ?> binding, final List<V> resultVars)
	{
		return solutions(getKB(), binding, resultVars);
	}

	/* @see org.mindswap.owls.expression.Condition#solutions(org.mindswap.owl.OWLModel, org.mindswap.query.ValueMap) */
	public <V extends Variable> ClosableIterator<ValueMap<V, OWLValue>> solutions(final OWLModel model,
		final ValueMap<?, ?> binding, final List<V> resultVars)
	{
		if (query == null)
		{
			final OWLList<Atom> atoms = getBody();

			// Assume that no expression body (no query constraints) has a
			// solution in any case (even for an empty model).
			if (atoms == null || atoms.isEmpty()) return SingleClosableIterator.oneElement();

			query = new StandardPreparedQuery<V>(atoms, resultVars, (OWLModelImpl) model);
		}

		// cast is not critical as long as consecutive invocations of solutions(..) use same Variable parameter
		return ((Query<V>) query).execute(binding);
	}

	/* @see impl.owl.WrappedIndividual#toString() */
	@Override
	public String toString()
	{
		final StringBuilder sb = new StringBuilder();
		for (OWLValue atom : getBody())
		{
			if (sb.length() > 0) sb.append(" AND ");
			sb.append(atom.toString());
		}

		return (sb.length() > 0)? sb.toString() : super.toString();
	}

	/* @see org.mindswap.owls.expression.Expression#writeTo(org.mindswap.owls.io.ProcessWriter, java.lang.String) */
	public void writeTo(final ProcessWriter procWriter, final String indent)
	{
		final ExpressionWriter<OWLList<Atom>> exprWriter = procWriter.getSwrlExpressionWriter();
		exprWriter.setIndent(indent);
		exprWriter.write(this);
	}

}
