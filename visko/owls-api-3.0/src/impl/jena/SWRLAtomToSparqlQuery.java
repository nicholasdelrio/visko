/*
 * Created 08.08.2009
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
import java.util.Map.Entry;

import org.mindswap.common.Variable;
import org.mindswap.exceptions.UnsupportedSWRLAtomException;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLObject;
import org.mindswap.owl.OWLValue;
import org.mindswap.owl.list.OWLList;
import org.mindswap.owl.vocabulary.SWRL;
import org.mindswap.owl.vocabulary.SWRLB;
import org.mindswap.query.ABoxQuery;
import org.mindswap.query.ValueMap;
import org.mindswap.swrl.Atom;
import org.mindswap.swrl.AtomVisitor;
import org.mindswap.swrl.BuiltinAtom;
import org.mindswap.swrl.ClassAtom;
import org.mindswap.swrl.DataPropertyAtom;
import org.mindswap.swrl.DifferentIndividualsAtom;
import org.mindswap.swrl.IndividualPropertyAtom;
import org.mindswap.swrl.SWRLDataObject;
import org.mindswap.swrl.SameIndividualAtom;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolutionMap;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.sparql.core.Var;
import com.hp.hpl.jena.sparql.engine.binding.Binding;
import com.hp.hpl.jena.sparql.engine.binding.Binding0;
import com.hp.hpl.jena.sparql.expr.E_Add;
import com.hp.hpl.jena.sparql.expr.E_Divide;
import com.hp.hpl.jena.sparql.expr.E_Equals;
import com.hp.hpl.jena.sparql.expr.E_GreaterThan;
import com.hp.hpl.jena.sparql.expr.E_GreaterThanOrEqual;
import com.hp.hpl.jena.sparql.expr.E_LessThan;
import com.hp.hpl.jena.sparql.expr.E_LessThanOrEqual;
import com.hp.hpl.jena.sparql.expr.E_Multiply;
import com.hp.hpl.jena.sparql.expr.E_NotEquals;
import com.hp.hpl.jena.sparql.expr.E_Subtract;
import com.hp.hpl.jena.sparql.expr.Expr;
import com.hp.hpl.jena.sparql.expr.ExprVar;
import com.hp.hpl.jena.sparql.expr.NodeValue;
import com.hp.hpl.jena.sparql.function.FunctionEnvBase;
import com.hp.hpl.jena.sparql.syntax.ElementFilter;
import com.hp.hpl.jena.sparql.syntax.ElementGroup;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;

/**
 * Transforms SWRL atoms (that may come from conjunctive ABox queries) to
 * SPAQRL constraints. Furthermore, given a (partial) initial binding of
 * variables and a set of desired result variables all this will then be used
 * to build a Jena query execution (SPARQL query), which can then be evaluated
 * against some OntModel.
 * <p>
 * Only a subset of SWRL built-in atoms is supported.
 *
 * @author unascribed
 * @version $Rev: 2350 $; $Author: nick $; $Date: 2011/01/24 06:37:51 $
 */
final class SWRLAtomToSparqlQuery implements AtomVisitor
{
	private final static Binding NULL_BINDING = new Binding0();

	private final OntModel ontModel;
	private final QuerySolutionMap queryInitialBinding;
	private final ElementGroup constraints;

	SWRLAtomToSparqlQuery(final OntModel ontModel, final QuerySolutionMap queryInitialBinding,
		final ElementGroup constraintsGroup)
	{
		this.ontModel = ontModel;
		this.queryInitialBinding = queryInitialBinding;
		this.constraints = constraintsGroup;
	}

	/**
	 * This method is similar to {@link #makeQueryExecution(OntModel, OWLList, List, ValueMap)}
	 * except that it does create a query instead of a query execution. In fact,
	 * this method is used by the aforementioned method.
	 *
	 * @param model The model to query.
	 * @param atoms List of SWRL atoms representing query constraints.
	 * @param resultVars Desired result variables. May be empty or <code>null</code>.
	 * @return A query ready to be executed.
	 */
	static final Query makeQuery(final OntModel model, final OWLList<Atom> atoms,
		final List<? extends Variable> resultVars, final QuerySolutionMap initialBindings)
	{
		final ElementGroup constraints = new ElementGroup();
		final AtomVisitor visitor = new SWRLAtomToSparqlQuery(model, initialBindings, constraints);
		for (Atom atom : atoms)
		{
			atom.accept(visitor);
		}

		final Query query = QueryFactory.make(); // SPARQL syntax by default
		query.setQueryType(Query.QueryTypeSelect);
		query.setQueryPattern(constraints);

		// set projection part
		if (resultVars == null || resultVars.isEmpty()) query.setQueryResultStar(true);
		else for (Variable variable : resultVars)
		{
			query.addResultVar(variable.getName());
		}

		return query;
	}

	/**
	 * Turn the given ABox query into a SPARQL query execution over the given model.
	 *
	 * @param model The model to query.
	 * @param aboxQuery The ABoxQuery to turn into a SPARQL query.
	 * @return A query execution ready to be executed.
	 */
	static final QueryExecution makeQueryExecution(final OntModel model, final ABoxQuery<?> aboxQuery)
	{
		return makeQueryExecution(model, aboxQuery.getBody(), aboxQuery.getResultVariables(), null);
	}

	/**
	 * Build a SPARQL query execution over the given OntModel based on the given
	 * SWRL atoms, result variables, and initial binding. In short, this method
	 * does the following to build up the query. It iterates over the given atoms
	 * and each will be transformed into a corresponding SPARQL constraint, e.g.,
	 * a basic graph pattern (BGP). For instance, {@link ClassAtom class atoms}
	 * will be transformed to <tt>(arg1 rdf:type class)</tt>, where <tt>arg1</tt>
	 * is the object returned by {@link ClassAtom#getArgument1()} and <tt>class</tt>
	 * is the OWL class returned by {@link ClassAtom#getClassPredicate()}.
	 * Then, the list of result variables is taken as the projection of the query.
	 * If this list is empty or null <tt>*</tt> will be used, i.e., <tt>SELECT *</tt>.
	 * Finally, the query execution takes into account the given initial binding
	 * containing mappings of variables (used within the atoms) to some value.
	 * If a value exists, the variable will be bound to this value when the query
	 * gets executed, i.e., it is no longer a free variable.
	 *
	 * @param model The model to query.
	 * @param atoms List of SWRL atoms representing query constraints.
	 * @param resultVars Desired result variables. May be empty or <code>null</code>.
	 * @param binding Initial binding of variables. May be empty or <code>null</code>.
	 * @return A query execution ready to be executed.
	 */
	static final QueryExecution makeQueryExecution(final OntModel model, final OWLList<Atom> atoms,
		final List<? extends Variable> resultVars, final ValueMap<?, ?> binding)
	{
		final QuerySolutionMap initialBinding = new QuerySolutionMap();

		// add initial bindings from value map
		if (binding != null)
		{
			for (Entry<? extends Variable, ? extends OWLValue> e : binding)
			{
				initialBinding.add(e.getKey().getName(), (RDFNode) e.getValue().getImplementation());
			}
		}

//		if (model.getGraph() instanceof PelletInfGraph)
//			return SparqlDLExecutionFactory.create(
//				makeQuery(model, atoms, resultVars, initialBinding), model, initialBinding));
		return QueryExecutionFactory.create(
			makeQuery(model, atoms, resultVars, initialBinding), model, initialBinding);
	}

	/* @see org.mindswap.swrl.AtomVisitor#visit(org.mindswap.swrl.BuiltinAtom) */
	public void visit(final BuiltinAtom atom)
	{
		final OWLIndividual builtin = atom.getBuiltin();

//		final List<Node> nodes = new ArrayList<Node>(3); // max 3 elements
		final List<Expr> args = new ArrayList<Expr>(3);

		for (SWRLDataObject o : atom.getArguments())
		{
			final Node n = makeNode(o);
//			nodes.add(n);
			args.add((n.isVariable())? new ExprVar(n.getName()) : NodeValue.makeNode(n));
		}

//		Node n0, n1;
		Expr a0, a1, a2 = null;
		if (args.size() < 2 || args.size() > 3) throw new UnsupportedSWRLAtomException(
			"Number of arguments in SWRL built-in atom " + atom + " invalid (<2), or unsupported (>3).");
//		n0 = nodes.get(0);
//		n1 = nodes.get(1);
		a0 = args.get(0);
		a1 = args.get(1);
		if (args.size() == 3) a2 = args.get(2);

		Expr c = null;
		if (SWRLB.equal.equals(builtin))                   c = new E_Equals(a0, a1);
//		{
//			if (a0.isConstant())
//			{
//				if (a1.isConstant())
//					c = new E_Equals(a0, a1);
//				else
//					queryInitialBinding.add(n1.getName(), ontModel.asRDFNode(a0.getConstant().asNode()));
//			}
//			else
//			{
//				if (a1.isConstant())
//					queryInitialBinding.add(n0.getName(), ontModel.asRDFNode(a1.getConstant().asNode()));
//				else
//					c = new E_Equals(a0, a1);
//			}
//		}
		else if (SWRLB.notEqual.equals(builtin))           c = new E_NotEquals(a0, a1);
		else if (SWRLB.greaterThan.equals(builtin))        c = new E_GreaterThan(a0, a1);
		else if (SWRLB.greaterThanOrEqual.equals(builtin)) c = new E_GreaterThanOrEqual(a0, a1);
		else if (SWRLB.lessThan.equals(builtin))           c = new E_LessThan(a0, a1);
		else if (SWRLB.lessThanOrEqual.equals(builtin))    c = new E_LessThanOrEqual(a0, a1);
		else if (SWRLB.add.equals(builtin) && a2 != null)
		{
			if (!a0.isConstant() && a1.isConstant() && a2.isConstant()) // ?a0 = a1 + a2
				eval(new E_Add(a1, a2), a0);
			else if (a0.isConstant() && !a1.isConstant() && a2.isConstant()) // a0 = ?a1 + a2 -> ?a1 = a0 - a2
				eval(new E_Subtract(a0, a2), a1);
			else if (a0.isConstant() && a1.isConstant() && !a2.isConstant()) // a0 = a1 + ?a2 -> ?a2 = a0 - a1
				eval(new E_Subtract(a0, a1), a2);
			else
				c = new E_Equals(a0, new E_Add(a1, a2));
		}
		else if (SWRLB.subtract.equals(builtin) && a2 != null)
		{
			if (!a0.isConstant() && a1.isConstant() && a2.isConstant()) // ?a0 = a1 - a2
				eval(new E_Subtract(a1, a2), a0);
			else if (a0.isConstant() && !a1.isConstant() && a2.isConstant()) // a0 = ?a1 - a2 -> ?a1 = a0 + a2
				eval(new E_Add(a0, a2), a1);
			else if (a0.isConstant() && a1.isConstant() && !a2.isConstant()) // a0 = a1 - ?a2 -> ?a2 = a1 - a0
				eval(new E_Subtract(a1, a0), a2);
			else
				c = new E_Equals(a0, new E_Subtract(a1, a2));
		}
		else if (SWRLB.multiply.equals(builtin) && a2 != null)
		{
			if (!a0.isConstant() && a1.isConstant() && a2.isConstant()) // ?a0 = a1 * a2
				eval(new E_Multiply(a1, a2), a0);
			else if (a0.isConstant() && !a1.isConstant() && a2.isConstant()) // a0 = ?a1 * a2 -> ?a1 = a0 / a2
				eval(new E_Divide(a0, a2), a1);
			else if (a0.isConstant() && a1.isConstant() && !a2.isConstant()) // a0 = a1 * ?a2 -> ?a2 = a0 / a1
				eval(new E_Divide(a0, a1), a2);
			else
				c = new E_Equals(a0, new E_Multiply(a1, a2));
		}
		else if (SWRLB.divide.equals(builtin) && a2 != null)
		{
			if (!a0.isConstant() && a1.isConstant() && a2.isConstant()) // ?a0 = a1 / a2
				eval(new E_Divide(a1, a2), a0);
			else if (a0.isConstant() && !a1.isConstant() && a2.isConstant()) // a0 = ?a1 / a2 -> ?a1 = a0 * a2
				eval(new E_Multiply(a0, a2), a1);
			else if (a0.isConstant() && a1.isConstant() && !a2.isConstant()) // a0 = a1 / ?a2 -> ?a2 = a1 / a0
				eval(new E_Divide(a1, a0), a2);
			else
				c = new E_Equals(a0, new E_Divide(a1, a2));
		}
		else throw new UnsupportedSWRLAtomException(
			"Transformation to SPARQL constraint is not supported/implemented for SWRL built-in atom " + atom);

		if (c != null) constraints.addElement(new ElementFilter(c));
	}

	/* @see org.mindswap.swrl.AtomVisitor#visit(org.mindswap.swrl.ClassAtom) */
	public void visit(final ClassAtom a)
	{
		// slightly faster if we do not use atom.getClassPredicate() <-- less OWL object casting
		addTriplePattern(a.getArgument1(), RDF.type.asNode(), a.getProperty(SWRL.classPredicate));
	}

	/* @see org.mindswap.swrl.AtomVisitor#visit(org.mindswap.swrl.DataPropertyAtom) */
	public void visit(final DataPropertyAtom a)
	{
		// slightly faster if we do not use atom.getPropertyPredicate() <-- less OWL object casting
		addTriplePattern(a.getArgument1(), makeNode(a.getProperty(SWRL.propertyPredicate)), a.getArgument2());
	}

	/* @see org.mindswap.swrl.AtomVisitor#visit(org.mindswap.swrl.DifferentIndividualsAtom) */
	public void visit(final DifferentIndividualsAtom a)
	{
		addTriplePattern(a.getArgument1(), OWL.differentFrom.asNode(), a.getArgument2());
	}

	/* @see org.mindswap.swrl.AtomVisitor#visit(org.mindswap.swrl.IndividualPropertyAtom) */
	public void visit(final IndividualPropertyAtom a)
	{
		// slightly faster if we do not use atom.getPropertyPredicate() <-- less OWL object casting
		addTriplePattern(a.getArgument1(), makeNode(a.getProperty(SWRL.propertyPredicate)), a.getArgument2());
	}

	/* @see org.mindswap.swrl.AtomVisitor#visit(org.mindswap.swrl.SameIndividualAtom) */
	public void visit(final SameIndividualAtom a)
	{
		addTriplePattern(a.getArgument1(), OWL.sameAs.asNode(), a.getArgument2());
	}

	private final void addTriplePattern(final OWLObject s, final Node p, final OWLObject o)
	{
		constraints.addTriplePattern(Triple.create(makeNode(s), p, makeNode(o)));
	}

	private final void eval(final Expr expr, final Expr resultVar)
	{
		final NodeValue result = expr.eval(NULL_BINDING, FunctionEnvBase.createTest());
		queryInitialBinding.add(resultVar.getVarName(), ontModel.asRDFNode(result.getConstant().asNode()));
	}

	private final Node makeNode(final OWLObject term)
	{
		if (term instanceof Variable)
		{
			final String varName = ((Variable) term).getName();
			if (varName == null || varName.trim().equals(""))
			{
				throw new IllegalArgumentException("Variable " + term + " has no or empty name! " +
					"This is usually caused by anon resources. Make sure that variable has a URI.");
			}
			return Var.alloc(Node.createVariable(varName));
		}
		return ((RDFNode) term.getImplementation()).asNode();
	}

}