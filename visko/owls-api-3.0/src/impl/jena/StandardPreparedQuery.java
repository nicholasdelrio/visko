/*
 * Created 09.09.2009
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

import org.mindswap.common.ClosableIterator;
import org.mindswap.common.OrdinaryVariable;
import org.mindswap.common.SingleClosableIterator;
import org.mindswap.common.Variable;
import org.mindswap.exceptions.ParseException;
import org.mindswap.owl.OWLModel;
import org.mindswap.owl.OWLValue;
import org.mindswap.owl.list.OWLList;
import org.mindswap.query.ABoxQuery;
import org.mindswap.query.QueryLanguage;
import org.mindswap.query.ValueMap;
import org.mindswap.swrl.Atom;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryException;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.QuerySolutionMap;
import com.hp.hpl.jena.query.Syntax;
import com.hp.hpl.jena.rdf.model.RDFNode;

/**
 * Standard prepared query supporting either SPARQL or RDQL query language.
 *
 * @author unascribed
 * @version $Rev: 2350 $; $Author: nick $; $Date: 2011/01/24 06:37:51 $
 */
class StandardPreparedQuery<V extends Variable> implements org.mindswap.query.Query<V>
{
	private OWLModelImpl model;
	private final List<V> resultVars;
	private final Query query;
	private final QuerySolutionMap fixedInitialBinding;

	StandardPreparedQuery(final ABoxQuery<V> query, final OWLModelImpl model)
	{
		this(query.getBody(), query.getResultVariables(), model);
	}

	StandardPreparedQuery(final OWLList<Atom> atoms, final List<V> resultVars, final OWLModelImpl model)
	{
		this.model = model;
		this.resultVars = resultVars;
		this.fixedInitialBinding = new QuerySolutionMap();
		this.query = SWRLAtomToSparqlQuery.makeQuery(
			model.getImplementation(), atoms, resultVars, fixedInitialBinding);
	}

	StandardPreparedQuery(final Query query, final List<V> resultVars, final OWLModelImpl model)
	{
		this.query = query;
		this.model = model;
		this.resultVars = resultVars;
		this.fixedInitialBinding = new QuerySolutionMap();
//		this.fixedInitialBinding = new QuerySolutionMap(Collections.<String,RDFNode>emptyMap());
	}

	/* @see org.mindswap.query.Query#attach(org.mindswap.owl.OWLModel) */
	public StandardPreparedQuery<V> attach(final OWLModel m)
	{
		if (m != null && m instanceof OWLModelImpl) this.model = (OWLModelImpl) m;
		else throw new IllegalArgumentException("Model null or not an instance of OWLModelImpl.");
		return this;
	}

	/* @see org.mindswap.query.Query#execute() */
	public ClosableIterator<ValueMap<V, OWLValue>> execute(final ValueMap<?, ?> binding)
	{
		final QueryExecution qexec;
//		if (model.getImplementation().getGraph() instanceof PelletInfGraph)
//			qexec = SparqlDLExecutionFactory.create(query, model.getImplementation(), getInitialBinding(binding));
		qexec = QueryExecutionFactory.create(query, model.getImplementation(), getInitialBinding(binding));

		if (query.isAskType())
		{
			return (qexec.execAsk())?
				SingleClosableIterator.<V>oneElement() : SingleClosableIterator.<V>noElement();
		}
		if (query.isSelectType())
		{
			return new StandardClosableIterator<V>(model, qexec.execSelect(), resultVars, qexec);
		}
		throw new IllegalArgumentException("Unsupported query type for " + query +
			". Only SELECT or ASK query can be used.");
	}

	private QuerySolution getInitialBinding(final ValueMap<?, ?> binding)
	{
		final QuerySolutionMap initialBinding = new QuerySolutionMap();
		initialBinding.addAll(fixedInitialBinding);
		if (binding != null)
		{
			for (Entry<? extends Variable, ? extends OWLValue> e : binding)
			{
				initialBinding.add(e.getKey().getName(), (RDFNode) e.getValue().getImplementation());
			}
		}
		return initialBinding;
	}

	/* @see org.mindswap.query.Query#getResultVariables() */
	public List<V> getResultVariables()
	{
		return Collections.unmodifiableList(resultVars); // prevent external modifications
	}

	/* @see org.mindswap.query.Query#getQueryLanguage() */
	public QueryLanguage getQueryLanguage()
	{
		if (Syntax.syntaxSPARQL.equals(query.getSyntax())) return QueryLanguage.SPARQL;
		return QueryLanguage.RDQL;
	}

	static final StandardPreparedQuery<Variable> createQuery(final String queryString,
		final QueryLanguage lang, final OWLModelImpl model) throws ParseException
	{
		// first, parse the query string
		final Query query;
		try
		{
			switch (lang)
			{
				case RDQL:
					query = QueryFactory.create(queryString, Syntax.syntaxRDQL);
					break;
				case SPARQL_DL:
				case SPARQL:
				default:
					query = QueryFactory.create(queryString, Syntax.syntaxSPARQL); // default: SPARQL
					break;
			}
		}
		catch (QueryException e)
		{
			throw new ParseException(queryString, e);
		}

		// second, prepare result variables
		final List<String> varNames = query.getResultVars();
		final List<Variable> resultVars = new ArrayList<Variable>(varNames.size());
		for (String varName : varNames)
		{
			resultVars.add(new OrdinaryVariable(varName));
		}

		return new StandardPreparedQuery<Variable>(query, resultVars, model);
	}

}
