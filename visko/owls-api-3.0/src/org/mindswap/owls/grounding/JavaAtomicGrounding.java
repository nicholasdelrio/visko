// The MIT License
//
// Copyright (c) 2007 University of Zürich Switzerland
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
package org.mindswap.owls.grounding;

import java.net.URI;

import org.mindswap.exceptions.ExecutionException;
import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owls.process.variable.Input;
import org.mindswap.owls.process.variable.Output;

/**
 * This interface encapsulates the access to a JavaAtomicProcessGrounding.
 * <p>
 * A JavaAtomicGrounding grounds an OWL-S Service to a Java method invocation.
 * The method call is specified by its method signature in an OWL-S Ontology.
 * <p>
 * Static methods can be invoked, exceptions during the execution are catched
 * and redirected in a {@link ExecutionException}. At the time, only primitive
 * datatypes (such as int, boolean, and so on) and their wrappers (such as
 * Integer, Boolean, and so on) are supported.
 * <p>
 * The driving parts are:
 * <ul>
 *   <li>fully qualified class name</li>
 *   <li>method name</li>
 *   <li>a map of all input parameters (at the time only primitive datatypes and
 *   their wrapper classes)</li>
 *   <li>an output type (at the time only primitive datatypes and their wrapper
 *   classes)</li>
 * </ul>
 *
 * @author unascribed
 * @version $Rev: 2298 $; $Author: nick $; $Date: 2011/01/24 06:37:54 $
 * @see <a href="http://on.cs.unibas.ch/owl-s/1.2/MoreGroundings.owl">MoreGroundings.owl</a>
 * @see org.mindswap.owls.vocabulary.MoreGroundings
 */
public interface JavaAtomicGrounding extends AtomicGrounding<Object>
{
	/**
	 * Sets the name of the class, on which the method in this grounding should
	 * be invoked.
	 *
	 * @param clazz Fully qualified name of the Java class, which implements the
	 * 	method to invoke.
	 */
    public void setClazz(String clazz);
    /**
     * Returns the name of the class, which implements the {@link #getMethod() method}
     * that should be invoked.
     *
     * @return Fully qualified Java class name specified in this grounding.
     */
    public String getClazz();

    /**
     * Sets the name of the method which should be executed
     *
     * @param method Name of the method to invoke
     */
    public void setMethod(String method);
    /**
     * Returns the name of the method which should be executed
     *
     * @return Name of the method specified in the JavaAtomicProcessGrounding
     */
    public String getMethod();
    /**
     * Sets the Output part of this Java Grounding. Declares the return value of the Java method to invoke.
     *
     * @param uri URI to be assigned to the JavaVariable instance, or
     * 	<code>null</code> to create an anonymous instance.
     * @param type Fully qualified Java class name of the return value of
     * 	the method to invoke.
     * @param owlsParameter Reference to the OWL-S Output Variable.
     */
    public void setOutput(URI uri, String type, Output owlsParameter);
    /**
     * Add an Input Parameter to this Java Grounding. Declares one Parameter of the Java method to invoke.
     *
     * @param uri URI to be assigned to the JavaParameter instance, or
     * 	<code>null</code> to create an anonymous instance.
     * @param type Fully qualified Java class name of the Java Parameter to set.
     * @param index Number of order for this Parameter in the Parameter list.
     * @param owlsParameter Reference to the OWL-S Input Variable.
     */
    public void addInputParameter(URI uri, String type, int index, Input owlsParameter);

    /**
     * Returns the java input parameter related to given OWL-S input
     * @return the java input parameter related to given OWL-S input
     */
    public JavaParameter getInputParamter(Input input);

    /**
     * Returns the input parameters for this java method in correct order
     * @see org.mindswap.owls.vocabulary.MoreGroundings.Java#paramIndex
     * @return a list with all inputs for this java method in correct order
     */
    public OWLIndividualList<JavaParameter> getInputParameters();

    /**
     * Returns the java output variable related to given OWL-S output
     * @return the java output variable related to given OWL-S output
     */
    public JavaVariable getOutput();
}
