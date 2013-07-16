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
 * Created on Dec 15, 2004
 */
package org.mindswap.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.mindswap.common.ClosableIterator;
import org.mindswap.common.Variable;
import org.mindswap.owl.OWLModel;
import org.mindswap.owl.OWLValue;
import org.mindswap.owl.list.OWLList;
import org.mindswap.swrl.Atom;
import org.mindswap.swrl.ClassAtom;
import org.mindswap.swrl.DataPropertyAtom;
import org.mindswap.swrl.IndividualPropertyAtom;
import org.mindswap.swrl.SWRLObject;

/**
 * Simple implementation of so called ABox queries. ABox queries are a
 * (possibly empty) conjunction of SWRL atoms. Furthermore, there are some
 * limitations on ABox queries. First, the predicate in
 * {@link DataPropertyAtom data property atoms} and {@link IndividualPropertyAtom
 * object property atoms} can not be a variable. Second, the object of a
 * {@link ClassAtom class atom} can not be a variable. (Those constraints
 * are actually subject to be implemented by implementations of the
 * interfaces.)
 *
 * @author unascribed
 * @version $Rev: 2350 $; $Author: nick $; $Date: 2011/01/24 06:37:52 $
 */
public class ABoxQuery<V extends Variable> implements Query<V>
{
	private final OWLList<Atom> body;
	private final List<V> resultVars;
	private Query<V> query;

	/**
	 * Create a new ABox query. Before it can be {@link #execute(ValueMap) executed}
	 * it needs to be {@link #attach(OWLModel) attached} to a model.
	 *
	 * @param body The set of SWRL atoms that make up the body of this query.
	 * @param resultVars The collection of result variables.
	 */
	public ABoxQuery(final OWLList<Atom> body, final Collection<V> resultVars)
	{
		this.body = body;
		this.resultVars = new ArrayList<V>(resultVars);
	}

	/* @see org.mindswap.query.Query#attach(org.mindswap.owl.OWLModel) */
	public ABoxQuery<V> attach(final OWLModel model)
	{
		query = model.makeQuery(this);
		return this;
	}

	/**
	 * Add a result variables to this query. Note that this query needs to be
	 * {@link #attach(OWLModel) re-attached} to a model after this method has
	 * been invoked!
	 *
	 * @param var The result variable to add.
	 */
	public void addResultVar(final V var)
	{
		resultVars.add(var);
		query = null;
	}

	/**
	 * Add a collection of variables to this query. Note this query needs to be
	 * {@link #attach(OWLModel) re-attached} to a model after this method has
	 * been invoked!
	 *
	 * @param vars The collection of result variables to add.
	 */
	public void addResultVars(final Collection<V> vars)
	{
		resultVars.addAll(vars);
		query = null;
	}

	/**
	 * @return The set of SWRL atoms that make up the body of this query.
	 */
	public OWLList<Atom> getBody()
	{
		return body;
	}

	/* @see org.mindswap.query.Query#getQueryLanguage() */
	public QueryLanguage getQueryLanguage()
	{
		return QueryLanguage.ABox;
	}

	/* @see org.mindswap.query.Query#getResultVariables() */
	public List<V> getResultVariables()
	{
		return Collections.unmodifiableList(resultVars); // prevent external modifications
	}

	/* @see org.mindswap.query.Query#getVariables() */
	public List<Variable> getVariables()
	{
		final List<Variable> vars = new ArrayList<Variable>();
		for (final Atom atom : body)
		{
			for (int j = 0; j < atom.getArgumentCount(); j++)
			{
				final SWRLObject term = atom.getArgument(j);
				if (term.isVariable())
				{
					vars.add((Variable) term);
				}
			}
		}
		return vars;
	}

	/* @see org.mindswap.query.Query#execute(org.mindswap.query.ValueMap) */
	public ClosableIterator<ValueMap<V, OWLValue>> execute(final ValueMap<?, ?> binding)
	{
		if (query == null) throw new IllegalStateException("Query is not attached to OWLModel.");
		return query.execute(binding);
	}

	/* @see java.lang.Object#toString() */
	@Override
	public String toString()
	{
		return "query(" + getResultVariables() + ") :- " + getBody().toString();
	}

}
