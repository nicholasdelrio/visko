/*
 * Created on Jul 7, 2004
 *
 * (c) 2008 Thorsten Möller - University of Basel Switzerland
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
package org.mindswap.owls.process.variable;

import java.util.Map;

import org.mindswap.common.Visitable;
import org.mindswap.exceptions.DataFlowException;
import org.mindswap.owl.OWLValue;
import org.mindswap.owls.process.Perform;
import org.mindswap.query.ValueMap;

/**
 * Specification that describes <em>how</em> a {@link ProcessVar} is bound
 * to its actual data value. Process variable value specifications are used in
 * {@link Binding bindings}. The OWL-S properties
 * {@link org.mindswap.owls.vocabulary.OWLS.Process#valueSource valueSource},
 * {@link org.mindswap.owls.vocabulary.OWLS.Process#valueSpecifier valueSpecifier},
 * {@link org.mindswap.owls.vocabulary.OWLS.Process#valueData valueData},
 * {@link org.mindswap.owls.vocabulary.OWLS.Process#valueObject valueObject},
 * {@link org.mindswap.owls.vocabulary.OWLS.Process#valueForm valueForm}, and
 * {@link org.mindswap.owls.vocabulary.OWLS.Process#valueFunction valueFunction}
 * are used to link the process variable to its value.
 * <p>
 * Note that there are no corresponding OWL-S concepts to this interface and the
 * sub interfaces {@link ValueConstant}, {@link ValueForm}, {@link ValueFunction}.
 * Only {@link ValueOf} is defined in terms of a OWL concept.
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:53 $
 */
public interface ParameterValue extends Visitable<ParameterValueVisitor, ParameterValue>
{

	/**
	 * Returns the binding which encloses this parameter value specification.
	 *
	 * @return the data binding in which this parameter value is enclosed in
	 */
	public Binding<?> getEnclosingBinding();

	/**
	 * This method can get used at execution time of some composite process. It
	 * gets the actual value from the given perform results map, i.e., binds this
	 * parameter to its actual value and returns it.
	 *
	 * @param performsResults The map containing all parameter-value mappings of
	 * 	preceding performs.
	 * @return The parameter value according to the data flow specification, or
	 * 	the source {@link Parameter parameter} in case of a self bindings.
	 * @throws DataFlowException In case the value respectively parameter could
	 * 	not be found in the given value map.
	 */
	public OWLValue getValueFromPerformResults(Map<Perform, ValueMap<ProcessVar, OWLValue>> performsResults)
		throws DataFlowException;

	/**
	 * Sets the value represented by this parameter value specification to the
	 * given binding (which should be the enclosing binding).
	 *
	 * @param binding The binding to which to set the value.
	 */
	public void setToBinding(Binding<?> binding);
}
