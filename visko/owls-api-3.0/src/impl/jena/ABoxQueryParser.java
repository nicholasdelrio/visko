/*
 * Created on Mar 30, 2005
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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mindswap.common.Parser;
import org.mindswap.common.Variable;
import org.mindswap.exceptions.NotImplementedException;
import org.mindswap.exceptions.ParseException;
import org.mindswap.owl.OWLClass;
import org.mindswap.owl.OWLDataProperty;
import org.mindswap.owl.OWLDataValue;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLModel;
import org.mindswap.owl.OWLObjectProperty;
import org.mindswap.owl.OWLProperty;
import org.mindswap.owl.list.OWLList;
import org.mindswap.query.ABoxQuery;
import org.mindswap.query.QueryLanguage;
import org.mindswap.swrl.Atom;
import org.mindswap.swrl.BuiltinAtom;
import org.mindswap.swrl.SWRLDataObject;
import org.mindswap.swrl.SWRLDataVariable;
import org.mindswap.swrl.SWRLFactory;
import org.mindswap.swrl.SWRLIndividualObject;
import org.mindswap.swrl.SWRLIndividualVariable;
import org.mindswap.swrl.SWRLFactory.ISWRLFactory;

import com.hp.hpl.jena.enhanced.EnhGraph;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.graph.query.Expression;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryException;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.Syntax;
import com.hp.hpl.jena.rdf.model.impl.LiteralImpl;
import com.hp.hpl.jena.sparql.expr.E_Equals;
import com.hp.hpl.jena.sparql.expr.E_GreaterThan;
import com.hp.hpl.jena.sparql.expr.E_GreaterThanOrEqual;
import com.hp.hpl.jena.sparql.expr.E_LessThan;
import com.hp.hpl.jena.sparql.expr.E_LessThanOrEqual;
import com.hp.hpl.jena.sparql.expr.E_NotEquals;
import com.hp.hpl.jena.sparql.expr.Expr;
import com.hp.hpl.jena.sparql.expr.ExprFunction;
import com.hp.hpl.jena.sparql.expr.NodeValue;
import com.hp.hpl.jena.sparql.lang.rdql.ParsedLiteral;
import com.hp.hpl.jena.sparql.lang.rdql.Q_Equal;
import com.hp.hpl.jena.sparql.lang.rdql.Q_GreaterThan;
import com.hp.hpl.jena.sparql.lang.rdql.Q_GreaterThanOrEqual;
import com.hp.hpl.jena.sparql.lang.rdql.Q_LessThan;
import com.hp.hpl.jena.sparql.lang.rdql.Q_LessThanOrEqual;
import com.hp.hpl.jena.sparql.lang.rdql.Q_NotEqual;
import com.hp.hpl.jena.sparql.syntax.Element;
import com.hp.hpl.jena.sparql.syntax.ElementFilter;
import com.hp.hpl.jena.sparql.syntax.ElementGroup;
import com.hp.hpl.jena.sparql.syntax.ElementTriplesBlock;
import com.hp.hpl.jena.sparql.syntax.ElementVisitorBase;
import com.hp.hpl.jena.vocabulary.RDF;

/**
 * This implementation is able to parse RDQL as well as SPARQL query strings
 * into conjunctive ABox queries which are based on SWRL atoms. Not every kind
 * of such queries can be parsed into a ABox query as there are some limitations
 * on ABox queries.
 *
 * @author unascribed
 * @version $Rev: 2308 $; $Author: nick $; $Date: 2011/01/24 06:37:51 $
 */
public class ABoxQueryParser implements Parser<String, ABoxQuery<Variable>>
{
	private static final String varNS = "var:";

	/** The model in which ABoxQuery will be created. */
	private final OWLModel model;
	/** Denotes the language/syntax of query strings to be parsed. */
	private final QueryLanguage lang;

	/**
	 * Creates a parser for queries according to the given language.
	 *
	 * @param model The model in which the query will be created.
	 * @param lang The syntax of the language. Defaults to
	 * 	{@link QueryLanguage#SPARQL} if parameter is <code>null</code>.
	 */
	public ABoxQueryParser(final OWLModel model, final QueryLanguage lang)
	{
		this.model = model;
		this.lang = lang;
	}

	/* @see org.mindswap.common.Parser#parse(java.lang.Object) */
	public ABoxQuery<Variable> parse(final String queryString) throws ParseException
	{
		if (queryString == null) throw new ParseException(queryString, "Query string null.");

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
					query = QueryFactory.create(queryString, Syntax.syntaxSPARQL);
					break;
			}
		}
		catch (QueryException e)
		{
			throw new ParseException(queryString, e);
		}

		final ISWRLFactory swrlFactory = SWRLFactory.createFactory(model);
		final Map<String, Variable> vars = new HashMap<String, Variable>();
		final Element elmnt = query.getQueryPattern();
		final ElementVisitor v = new ElementVisitor(model, swrlFactory, vars);
		elmnt.visit(v);
		final OWLList<Atom> atomList = v.getAtomList();

		final List<Variable> resultVars = new ArrayList<Variable>();
		for (String var : query.getResultVars())
		{
			resultVars.add(vars.get("?" + var));
		}

		return new ABoxQuery<Variable>(atomList, resultVars);
	}

	static final class ElementVisitor extends ElementVisitorBase
	{
		private final OWLModel owlModel;
		private final ISWRLFactory swrlFactory;
		private final Map<String, Variable> vars;
		private OWLList<Atom> atoms;

		ElementVisitor(final OWLModel model, final ISWRLFactory swrlFactory, final Map<String, Variable> vars)
		{
			this.owlModel = model;
			this.swrlFactory = swrlFactory;
			this.vars = vars;
			this.atoms = swrlFactory.createList();
		}

		OWLList<Atom> getAtomList() { return atoms; }

		/* @see com.hp.hpl.jena.sparql.syntax.ElementVisitorBase#visit(com.hp.hpl.jena.sparql.syntax.ElementTriplesBlock) */
		@Override
		public void visit(final ElementTriplesBlock elementtriplesblock)
		{
			for (Triple t : elementtriplesblock.getPattern())
			{
				try
				{
					if (t.getPredicate().isVariable()) throw new IllegalArgumentException(
						"Variables cannot be used in predicate position in ABoxQuery");

					Atom atom = null;
					final String predURI = t.getPredicate().getURI();
					if (RDF.type.getURI().equals(predURI))
					{
						if (t.getObject().isVariable()) throw new IllegalArgumentException(
							"Variables cannot be used as objects of rdf:type triples in ABoxQuery");

						final OWLClass c = owlModel.createClass(new URI(t.getObject().getURI()));
						final SWRLIndividualObject arg = makeIndividalObject(t.getSubject());

						atom = swrlFactory.createClassAtom(c, arg);
					}
					else
					{
						final OWLProperty p = owlModel.getProperty(new URI(predURI));

						if (p == null)	throw new IllegalArgumentException(
							predURI + " is unknown [Object|Datatype]Property in the backing OWL model.");
						else if (p.isDatatypeProperty())
						{
							final OWLDataProperty dp = owlModel.createDataProperty(p.getURI());
							final SWRLIndividualObject arg1 = makeIndividalObject(t.getSubject());
							final SWRLDataObject arg2 = makeDataObject(t.getObject());

							atom = swrlFactory.createDataPropertyAtom(dp, arg1, arg2);
						}
						else if (p.isObjectProperty())
						{
							final OWLObjectProperty op = owlModel.createObjectProperty(p.getURI());
							final SWRLIndividualObject arg1 = makeIndividalObject(t.getSubject());
							final SWRLIndividualObject arg2 = makeIndividalObject(t.getObject());

							atom = swrlFactory.createIndividualPropertyAtom(op, arg1, arg2);
						}
						// else it could be a annotation property, which are not relevant anyway
					}

					if (atom != null) atoms = atoms.cons(atom);
				}
				catch(final URISyntaxException e)
				{
					throw new IllegalArgumentException(
						e.getInput() + " appearing in the query string is not a valid URI!");
				}
			}
		}

		/* @see com.hp.hpl.jena.sparql.syntax.ElementVisitorBase#visit(com.hp.hpl.jena.sparql.syntax.ElementFilter) */
		@Override
		public void visit(final ElementFilter elementfilter)
		{
			final Expr expr = elementfilter.getExpr();
			final BuiltinAtom atom;
			try
			{
				if (expr instanceof Expression) // RDQL
				{
					Expression e = (Expression) expr;
					SWRLDataObject arg1 = makeDataObject(e.getArg(0));
					SWRLDataObject arg2 = makeDataObject(e.getArg(1));

					if (e instanceof Q_Equal) atom = swrlFactory.createEqual(arg1, arg2);
					else if (e instanceof Q_NotEqual) atom = swrlFactory.createNotEqual(arg1, arg2);
					else if (e instanceof Q_GreaterThan) atom = swrlFactory.createGreaterThan(arg1, arg2);
					else if (e instanceof Q_GreaterThanOrEqual) atom = swrlFactory.createGreaterThanOrEqual(arg1, arg2);
					else if (e instanceof Q_LessThan) atom = swrlFactory.createLessThan(arg1, arg2);
					else if (e instanceof Q_LessThanOrEqual) atom = swrlFactory.createLessThanOrEqual(arg1, arg2);
					else
						throw new IllegalArgumentException(
							"Unsupported constraint expression " + e + " used in RDQL query.");
				}
				else if (expr.isFunction()) // SPARQL
				{
					ExprFunction f = (ExprFunction) expr;
					SWRLDataObject arg1 = makeDataObject(f.getArg(0));
					SWRLDataObject arg2 = makeDataObject(f.getArg(1));

					if (f instanceof E_Equals) atom = swrlFactory.createEqual(arg1, arg2);
					else if (f instanceof E_NotEquals) atom = swrlFactory.createNotEqual(arg1, arg2);
					else if (f instanceof E_GreaterThan) atom = swrlFactory.createGreaterThan(arg1, arg2);
					else if (f instanceof E_GreaterThanOrEqual) atom = swrlFactory.createGreaterThanOrEqual(arg1, arg2);
					else if (f instanceof E_LessThan) atom = swrlFactory.createLessThan(arg1, arg2);
					else if (f instanceof E_LessThanOrEqual) atom = swrlFactory.createLessThanOrEqual(arg1, arg2);
					else
						throw new IllegalArgumentException(
							"Unsupported constraint (filter) " + f + " used in SPARQL query.");
				}
				else return;
				atoms = atoms.cons(atom);
			}
			catch(URISyntaxException e)
			{
				throw new IllegalArgumentException(e.getInput() + " is not a valid URI!");
			}
		}

		/* @see com.hp.hpl.jena.sparql.syntax.ElementVisitorBase#visit(com.hp.hpl.jena.sparql.syntax.ElementGroup) */
		@Override
		public void visit(final ElementGroup elementgroup)
		{
			for (Element e : elementgroup.getElements())
			{
				e.visit(this);
			}
		}

		private SWRLDataObject makeDataObject(final Expr expr) throws URISyntaxException
		{
			if (expr.isVariable())
			{
				return swrlFactory.createDataVariable(new URI(varNS + expr.getVarName()));
			}
			else if (expr.isConstant())
			{
				NodeValue nv = (NodeValue) expr;
				OWLDataValue value;
				if (nv.isInteger()) value = owlModel.createDataValue(nv.getInteger());
				else if (nv.isDouble()) value = owlModel.createDataValue(Double.valueOf(nv.getDouble()));
				else if (nv.isFloat()) value = owlModel.createDataValue(Float.valueOf(nv.getFloat()));
				else if (nv.isBoolean()) value = owlModel.createDataValue(Boolean.valueOf(nv.getBoolean()));
				else if (nv.isString()) value = owlModel.createDataValue(nv.getString());
				else if (nv.isDecimal()) value = owlModel.createDataValue(nv.getDecimal());
				else if (nv.isDate()) value = owlModel.createDataValue(nv.getDate());
				else if (nv.isDateTime()) value = owlModel.createDataValue(nv.getDateTime());
				else /* if (nv.isLiteral()) */ throw new NotImplementedException();

				return swrlFactory.wrapDataValue(value);
			}
			else
			{
				throw new IllegalArgumentException("Nested constraint (filter) near " + expr +
					" can not be transformed to SWRL atom.");
			}
		}

		private SWRLDataObject makeDataObject(final Expression expr) throws URISyntaxException
		{
			if (expr.isVariable())
			{
				return swrlFactory.createDataVariable(new URI(varNS + expr.getName()));
			}
			else if (expr.isConstant())
			{
				OWLDataValue value = null;
				if (expr instanceof ParsedLiteral)
				{
					final ParsedLiteral lit = (ParsedLiteral) expr;
					if (lit.isInt()) value = owlModel.createDataValue(Long.valueOf(lit.getInt()));
					else if (lit.isDouble()) value = owlModel.createDataValue(Double.valueOf(lit.getDouble()));
					else if (lit.isBoolean()) value = owlModel.createDataValue(Boolean.valueOf(lit.getBoolean()));
					else if (lit.isString()) value = owlModel.createDataValue(lit.getString());
					else if (lit.isURI()) value = owlModel.createDataValue(URI.create(lit.getURI()));
					else /* if (lit.isNode()) */ throw new NotImplementedException();
				}
				else
					value = owlModel.createDataValue(expr.getValue());

				return swrlFactory.wrapDataValue(value);
			}
			else
			{
				throw new IllegalArgumentException("Nested constraint expressions near " + expr +
					" can not be transformed to SWRL atom.");
			}
		}

		private SWRLDataObject makeDataObject(final Node node) throws URISyntaxException
		{
			if (node.isVariable())
			{
				final SWRLDataVariable var = swrlFactory.createDataVariable(new URI(varNS + node.getName()));
				vars.put(node.toString(), var);
				return var;
			}
			final OWLDataValue value = new OWLDataValueImpl(new LiteralImpl(node, (EnhGraph) owlModel.getImplementation()));
			return swrlFactory.wrapDataValue(value);
		}

		private SWRLIndividualObject makeIndividalObject(final Node node) throws URISyntaxException
		{
			if (node.isVariable())
			{
				final SWRLIndividualVariable var = swrlFactory.createIndividualVariable(new URI(varNS + node.getName()));
				vars.put(node.toString(), var);
				return var;
			}
			final OWLIndividual ind = owlModel.createIndividual(null, new URI(node.getURI()));
			return swrlFactory.wrapIndividual(ind);
		}
	}
}
