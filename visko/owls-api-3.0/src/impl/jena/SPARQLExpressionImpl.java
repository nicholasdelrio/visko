/*
 * Created 02.07.2009
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
package impl.jena;

import impl.owls.expression.QuotedExpressionImpl;

import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

import org.mindswap.common.ClosableIterator;
import org.mindswap.common.SingleClosableIterator;
import org.mindswap.common.Variable;
import org.mindswap.exceptions.InvalidOWLSException;
import org.mindswap.owl.OWLDataValue;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLModel;
import org.mindswap.owl.OWLValue;
import org.mindswap.owls.expression.Condition;
import org.mindswap.owls.expression.Expression;
import org.mindswap.owls.expression.LogicLanguage;
import org.mindswap.owls.expression.VariableBinding;
import org.mindswap.owls.io.ExpressionWriter;
import org.mindswap.owls.io.ProcessWriter;
import org.mindswap.owls.vocabulary.OWLS;
import org.mindswap.query.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryException;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolutionMap;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.Syntax;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;

/**
 *
 * @author unascribed
 * @version $Rev: 2311 $; $Author: nick $; $Date: 2011/01/24 06:37:51 $
 */
public class SPARQLExpressionImpl extends QuotedExpressionImpl<String> implements Condition.SPARQL
{
	private static final Logger logger = LoggerFactory.getLogger(SPARQLExpressionImpl.class);

	private Query query;

	public SPARQLExpressionImpl(final OWLIndividual ind)
	{
		super(ind, OWLS.Expression.SPARQL, true);
	}

	/**
	 * @param ind The individual to wrap.
	 * @param setLanguage <code>false</code> if the backing ontology already
	 * 	contains a statement that asserts that the individual is a SPARQL
	 * 	expression, i.e., the property {@link OWLS.Expression#expressionLanguage}
	 * 	is already set to refer to {@link OWLS.Expression#SPARQL}.
	 */
	public SPARQLExpressionImpl(final OWLIndividual ind, final boolean setLanguage)
	{
		super(ind, OWLS.Expression.SPARQL, setLanguage);
	}

	/* @see org.mindswap.owls.expression.Expression#evaluate(org.mindswap.query.ValueMap) */
	public void evaluate(final ValueMap<?, ?> bindings)
	{
		if (query == null)
		{
			final String queryString = getBody();

			// nothing to evaluate in this case
			if (queryString == null || queryString.length() == 0) return;

			try
			{
				query = QueryFactory.create(queryString, Syntax.syntaxSPARQL);
			}
			catch (QueryException e)
			{
				logger.error("Parse SPARQL expression {} failed. Details: {}", this, e.toString());
				return;
			}
		}

		final QueryExecution qexec = createQueryExecution(this, getKB(), bindings, query);

		if (query.isConstructType())
		{
			try
			{
				final Model result = qexec.execConstruct();

				// we add constructed statements to base model of KB
				((OWLKnowledgeBaseImpl) getKB()).getImplementation().getBaseModel().add(result);
			}
			finally
			{
				qexec.close();
			}
		}
		else throw new UnsupportedOperationException("Unsupported SPAQRL query type in expression " + this +
			". Only CONSTRUCT queries can be evaluated.");
	}

	/* @see org.mindswap.owls.generic.expression.Expression#getLanguage() */
	public final LogicLanguage getLanguage()
	{
		// We can skip retrieving the language property from the model, because
		// instances can only be created in a way that ensures that this property
		// is set.
		return OWLS.Expression.SPARQL;
	}

	/* @see org.mindswap.owls.expression.Expression#getBody() */
	public String getBody()
	{
		final OWLDataValue body = getQuotedBody();
		return (body == null)? null : body.getValue().toString().trim();
	}

	/* @see org.mindswap.owls.expression.Expression#setBody(java.lang.Object) */
	public void setBody(final String body)
	{
		query = null;
		setQuotedBody(body);
	}

	/* @see org.mindswap.owls.expression.Condition#isTrue(org.mindswap.query.ValueMap) */
	public boolean isTrue(final ValueMap<?, ?> binding)
	{
		return isTrue(getKB(), binding);
	}

	/* @see org.mindswap.owls.expression.Condition#isTrue(org.mindswap.owl.OWLModel, org.mindswap.query.ValueMap) */
	public boolean isTrue(final OWLModel model, final ValueMap<?, ?> binding)
	{
		final boolean isTrue;
		ClosableIterator<ValueMap<Variable, OWLValue>> solutions = null;
		try
		{
			solutions =	queryForSolutions(model, binding, Collections.<Variable>emptyList());
			isTrue = solutions.hasNext();
		}
		finally
		{
			if (solutions != null) solutions.close();
		}
		return isTrue;
	}

	/* @see org.mindswap.owls.expression.Condition#solutions(org.mindswap.query.ValueMap) */
	public <V extends Variable> ClosableIterator<ValueMap<V, OWLValue>> solutions(
		final ValueMap<?, ?> binding, final List<V> resultVars)
	{
		return queryForSolutions(getKB(), binding, resultVars);
	}

	/* @see org.mindswap.owls.expression.Condition#solutions(org.mindswap.owl.OWLModel, org.mindswap.query.ValueMap) */
	public <V extends Variable> ClosableIterator<ValueMap<V, OWLValue>> solutions(final OWLModel model,
		final ValueMap<?, ?> binding, final List<V> resultVars)
	{
		return queryForSolutions(model, binding, resultVars);
	}

	/* @see impl.owl.WrappedIndividual#toString() */
	@Override
	public String toString()
	{
		final String body = getBody();
		return (body != null)? body : super.toString();
	}

	/* @see org.mindswap.owls.expression.Expression#writeTo(org.mindswap.owls.io.ProcessWriter, java.lang.String) */
	public void writeTo(final ProcessWriter procWriter, final String indent)
	{
		final ExpressionWriter<String> exprWriter = procWriter.getSparqlExpressionWriter();
		exprWriter.setIndent(indent);
		exprWriter.write(this);
	}

	/** Create a query execution by taking into account the given initial binding of variables. */
	static final QueryExecution createQueryExecution(final Expression.SPARQL expression, final OWLModel model,
		final ValueMap<?, ?> initialBinding, final Query query)
	{
		final QueryExecution qexec;
		if (initialBinding == null || initialBinding.isEmpty())
		{
			qexec = QueryExecutionFactory.create(query, (OntModel) model.getImplementation());
		}
		else
		{
			// Create initial bindings according to given bindings; we assume that for some
			// variable(s) occurring in the SPARQL query there exists a VariableBinding in
			// this expression that associates it with exactly one Variable, and that this
			// Variable has associated a OWLValue in 'bindings'
			final QuerySolutionMap queryInitBinding = new QuerySolutionMap();
			for (Entry<? extends Variable, ? extends OWLValue> entry : initialBinding)
			{
				Variable var = entry.getKey();
				OWLValue val = entry.getValue();
				VariableBinding varBinding = expression.getVariableBinding(var.getName());
				if (varBinding != null)
				{
					String sparqlVar = getSPARQLVariable(varBinding);
					queryInitBinding.add(sparqlVar, (RDFNode) val.getImplementation());
				}
			}
			qexec = QueryExecutionFactory.create(query, (OntModel) model.getImplementation(), queryInitBinding);
		}
		return qexec;
	}

	/** Get the expression variable from the varBinding and make sure that it starts with '?' or '$'. */
	static final String getSPARQLVariable(final VariableBinding varBinding)
	{
		String sparqlVar = varBinding.getExpressionVariable();
		if (sparqlVar == null || (sparqlVar = sparqlVar.trim()).length() == 0)
		{
			throw new InvalidOWLSException(
				"Required property 'theVariable' is either missing or invalid for VariableBinding " + varBinding);
		}
		return (sparqlVar.startsWith("?") || sparqlVar.startsWith("$"))? sparqlVar : "?" + sparqlVar;
	}

	private <V extends Variable> ClosableIterator<ValueMap<V, OWLValue>> queryForSolutions(final OWLModel model,
		final ValueMap<?, ?> initialBinding, final List<V> resultVars)
	{
		if (query == null)
		{
			final String queryString = getBody();

			// assume that no expression body (no query constraints) has a solution in any case (even for an empty model)
			if (queryString == null || queryString.length() == 0) return SingleClosableIterator.oneElement();

			try
			{
				query = QueryFactory.create(queryString, Syntax.syntaxSPARQL);
			}
			catch (QueryException e)
			{
				logger.error("Parse SPARQL expression {} failed. Details: {}", this, e.toString());
				return SingleClosableIterator.<V>noElement();
			}
		}

		final QueryExecution qexec = createQueryExecution(this, model, initialBinding, query);
		if (query.isAskType())
		{
			return (qexec.execAsk())? SingleClosableIterator.<V>oneElement() : SingleClosableIterator.<V>noElement();
		}
		else if (query.isSelectType())
		{
			final ResultSet solutions = qexec.execSelect();
			return new StandardClosableIterator<V>((OWLModelImpl) model, solutions, resultVars, qexec);
		}
		else throw new UnsupportedOperationException("Unsupported SPARQL query type in condition " + this +
			". Either SELECT or ASK query must be used to express conditions.");
	}

}
