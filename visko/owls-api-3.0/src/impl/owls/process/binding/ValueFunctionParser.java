/*
 * Created 26.07.2009
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
package impl.owls.process.binding;

import org.mindswap.common.Parser;
import org.mindswap.exceptions.CastingException;
import org.mindswap.exceptions.ParseException;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owls.expression.Expression;
import org.mindswap.owls.process.variable.Binding;
import org.mindswap.owls.process.variable.ValueFunction;
import org.mindswap.utils.RDFUtils;

/**
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:53 $
 */
class ValueFunctionParser implements Parser<String, ValueFunction<?>>
{
	private final Binding<?> binding;

	ValueFunctionParser(final Binding<?> binding)
	{
		this.binding = binding;
	}

	/* @see org.mindswap.utils.Parser#parse(java.lang.Object) */
	public ValueFunction<?> parse(String input) throws ParseException
	{
		if (input == null) throw new ParseException(input, "Value function literal null.");

		// We assume that (i) we have some RDF/XML literal that (ii) can be parsed
		// to a individual. If we wanted to support different types of functions
		// (e.g. XPath expressions) in the future we should extend this section.

		input = RDFUtils.addRDFTag(input.trim());
		OWLIndividual function = binding.getOntology().parseLiteral(input);
		try
		{
			Expression.SPARQL expression = function.castTo(Expression.SPARQL.class);
			return binding.getOntology().createSPARQLValueFunction(expression, binding);
		}
		catch (CastingException e)
		{
			throw new ParseException(input,
				"Unsupported value function. Only SPARQL expressions are currently supported.");
		}
	}

}
