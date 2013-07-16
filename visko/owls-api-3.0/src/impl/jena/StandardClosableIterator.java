/*
 * Created 09.07.2009
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
import java.util.List;

import org.mindswap.common.ClosableIterator;
import org.mindswap.common.OrdinaryVariable;
import org.mindswap.common.Variable;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owl.OWLOntology;
import org.mindswap.owl.OWLValue;
import org.mindswap.query.ValueMap;

import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.RDFNode;

/**
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:51 $
 */
final class StandardClosableIterator<V extends Variable> implements ClosableIterator<ValueMap<V, OWLValue>>
{
	private final OWLModelImpl model;
	private final ResultSet results;
	private final List<V> resultVars;
	private final QueryExecution qexec;

	/**
	 * @param model The model to be used for creating OWL values returned by
	 * 	{@link #next()}. If it is a {@link OWLKnowledgeBase} then its base
	 * 	ontology is used. If it is a {@link OWLOntology} it is used as is.
	 * @param results The query solutions to iterate through.
	 * @param resultVars Each solution will be projected to the variables in
	 * 	this list, i.e., this list determines which values will be contained
	 * 	for each solution returned by {@link #next()}. If it is empty then
	 * 	each solution will also be empty. Must not be <code>null</code>.
	 * @param qexec The backing query execution to be closed eventually upon
	 * 	end of iteration.
	 */
	StandardClosableIterator(final OWLModelImpl model, final ResultSet results, final List<V> resultVars,
		final QueryExecution qexec)
	{
		this.model = model;
		this.results = results;
		this.resultVars = resultVars;
		this.qexec = qexec;
	}

	/* @see org.mindswap.utils.ClosableIterator#close() */
	public void close()
	{
		qexec.close();
	}

	/* @see java.util.Iterator#hasNext() */
	public boolean hasNext()
	{
		return results.hasNext();
	}

	/* @see java.util.Iterator#remove() */
	public void remove()
	{
		results.remove();
	}

	/* @see java.util.Iterator#next() */
	public ValueMap<V, OWLValue> next()
	{
		final QuerySolution querySolution = results.next();
		final ValueMap<V, OWLValue> solution = new ValueMap<V, OWLValue>();

		// do projection to desired result variables, if empty we return an empty solution (valid case)
		for (V var : resultVars)
		{
			final RDFNode value = querySolution.get(var.getName());
			if (value != null)
			{
				final OWLValue owlValue;
				if (value.isLiteral()) owlValue = model.wrapDataValue(value.as(Literal.class));
				else owlValue = model.wrapIndividual(value.as(OntResource.class));
				solution.setValue(var, owlValue);
			}
		}

		return solution;
	}

	/**
	 * @param model The model to be attached to result OWL values returned by
	 * 	{@link #next()}. If it is a {@link OWLKnowledgeBase} then its base
	 * 	ontology is used. If it is a {@link OWLOntology} it is used as is.
	 * @param results The query solutions to iterate through.
	 * @param resultVarNames Each solution will be projected to the variables in
	 * 	this list, i.e., this list determines which values will be contained
	 * 	for each solution returned by {@link #next()}. If it is empty or
	 * 	<code>null</code> then each solution will also be empty.
	 * @param qexec The backing query execution to be closed eventually upon
	 * 	end of iteration.
	 */
	public static final StandardClosableIterator<Variable> create(final OWLModelImpl model,
		final ResultSet results, final List<String> resultVarNames, final QueryExecution qexec)
	{
		List<Variable> resultVars = new ArrayList<Variable>();
		if (resultVarNames != null) for (String varName : resultVarNames)
		{
			resultVars.add(new OrdinaryVariable(varName));
		}
		return new StandardClosableIterator<Variable>(model, results, resultVars, qexec);
	}

}