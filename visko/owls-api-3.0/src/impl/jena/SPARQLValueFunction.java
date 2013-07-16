/*
 * Created 24.07.2009
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

import java.util.Map;

import org.mindswap.exceptions.DataFlowException;
import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owl.OWLValue;
import org.mindswap.owls.expression.Expression;
import org.mindswap.owls.expression.VariableBinding;
import org.mindswap.owls.process.Perform;
import org.mindswap.owls.process.variable.Binding;
import org.mindswap.owls.process.variable.ParameterValueVisitor;
import org.mindswap.owls.process.variable.ProcessVar;
import org.mindswap.owls.process.variable.ValueFunction;
import org.mindswap.owls.vocabulary.OWLS;
import org.mindswap.query.ValueMap;
import org.mindswap.utils.RDFUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryException;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.Syntax;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:51 $
 */
public class SPARQLValueFunction implements ValueFunction<Expression.SPARQL>
{
	private static final Logger logger = LoggerFactory.getLogger(SPARQLValueFunction.class);

	private Expression.SPARQL expression;
	private final Binding<?> enclosingBinding;

	public SPARQLValueFunction(final Expression.SPARQL expression, final Binding<?> enclosingBinding)
	{
		this.expression = expression;
		this.enclosingBinding = enclosingBinding;
	}

	/* @see org.mindswap.common.Visitable#accept(org.mindswap.common.Visitor) */
	public void accept(final ParameterValueVisitor visitor)
	{
		visitor.visit(this);
	}

	/* @see org.mindswap.owls.process.variable.ValueFunction#getFunction() */
	public Expression.SPARQL getFunction()
	{
		return expression;
	}

	/* @see org.mindswap.owls.process.variable.ValueFunction#removeFunction() */
	public void removeFunction()
	{
		enclosingBinding.removeProperty(OWLS.Process.valueFunction, null);
	}

	/* @see org.mindswap.owls.process.variable.ValueFunction#setFunction(java.lang.String) */
	public void setFunction(final Expression.SPARQL expression)
	{
		this.expression = expression;
		setTo(enclosingBinding);
	}

	/* @see org.mindswap.owls.process.variable.ParameterValue#getEnclosingBinding() */
	public Binding<?> getEnclosingBinding()
	{
		return enclosingBinding;
	}

	/* @see org.mindswap.owls.process.variable.ParameterValue#getValueFromPerformResults(java.util.Map) */
	public OWLValue getValueFromPerformResults(final Map<Perform, ValueMap<ProcessVar, OWLValue>> performsResults)
		throws DataFlowException
	{
		String queryString = expression.getBody();
		if (queryString == null || queryString.length() == 0)
			throw new DataFlowException("Invalid value function in binding " + enclosingBinding +
				". SPARQL expression without or empty body.");

		final Query query;
		try
		{
			query = QueryFactory.create(queryString, Syntax.syntaxSPARQL);
		}
		catch (QueryException e) {	throw new DataFlowException(e); }

		final QueryExecution qexec = SPARQLExpressionImpl.createQueryExecution(
			expression, enclosingBinding.getKB(), performsResults.get(OWLS.Process.ThisPerform), query);
		try
		{
			if (query.isSelectType())
			{
				OWLValue value = null;
				final ResultSet solutions = qexec.execSelect();
				if (solutions.hasNext())
				{
					value = retrieveOWLValue(solutions.next());

					if (solutions.hasNext()) logger.debug(
						"Value function in binding {} has more than one solution. First solution found used to bind the process variable!",
						enclosingBinding);
				}
				else logger.debug(
					"Value function in binding {} has no solution. Process variable was not bound to value!",
					enclosingBinding);

				return value;
			}
			throw new DataFlowException("Unsupported SPAQRL query type used in value function of binding " +
				enclosingBinding + ". Only SELECT queries can be used.");
		}
		finally
		{
			qexec.close();
		}
	}

	/* @see org.mindswap.owls.process.variable.ParameterValue#setToBinding(org.mindswap.owls.process.variable.Binding) */
	public void setToBinding(final Binding<?> binding)
	{
		setTo(binding);
	}

	private void setTo(final Binding<?> binding)
	{
		binding.setProperty(OWLS.Process.valueFunction, RDFUtils.toXMLLiteral(expression));
	}

	/* @see java.lang.Object#toString() */
	@Override
	public String toString()
	{
		return "SPARQL value function, Query: " + expression.getBody();
	}

	private OWLValue retrieveOWLValue(final QuerySolution solution)
	{
		final OWLIndividualList<VariableBinding> varBindings = expression.getVariableBindings();

		OWLValue value = null;
		for (VariableBinding varBinding : varBindings)
		{
			String varName = SPARQLExpressionImpl.getSPARQLVariable(varBinding);
			RDFNode resultValue = solution.get(varName);
			if (resultValue != null) // we have found the result projection variable
			{
				if (resultValue.isLiteral()) value = new OWLDataValueImpl(resultValue.as(Literal.class));
				else value = ((OWLKnowledgeBaseImpl) enclosingBinding.getKB()).wrapIndividual(
					resultValue.as(Resource.class));
				break;
			}
		}

		if (value == null) logger.debug("Process variable could not be bound from binding {}. Query solution " +
			"exists but value for variable was not found in the solution according to VariableBinding(s). " +
			"This is likely caused by incomplete or false VariableBinding specifications.",
			enclosingBinding);

		return value;
	}

}
